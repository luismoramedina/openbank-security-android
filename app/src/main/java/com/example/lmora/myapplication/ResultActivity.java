package com.example.lmora.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.bouncycastle.x509.X509Store;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ResultActivity extends AppCompatActivity {

    private static final String DOCUMENT_ENDPOINT_URI = "http://172.26.0.132:8080/document?id=blah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String documentId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView contentView = (TextView) this.findViewById(R.id.documentcontent);
        contentView.setText("Downloading document... " + documentId);

        new DownloadDocTask(contentView).execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accept_reject, menu);
        return true;
    }

    private class DownloadDocTask extends AsyncTask<URL, Integer, String> {

        private final View view;

        public DownloadDocTask(View view) {
            this.view = view;
        }


        protected String doInBackground(URL... urls) {
            // Create connection objects
            HttpClient httpClient = new DefaultHttpClient();
            URI endpointUri = null;
            String str = null;
            try {
                str = doHttpConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            String fileData;
            try {
                JSONObject object = new JSONObject(result);
                try {
                    String data = (String) object.get("data");
                    if (!verifyPKCS7Signature(data)) {
                        throw new IllegalArgumentException("Document signature verification is KO");
                    }
                    fileData = extractDocumentData(data);
//                    byte[] decode = Base64.decode(data, Base64.DEFAULT);
//                    fileData = new String(decode);
                } catch (Exception e) {
                    fileData = (String) object.get("result");
                    Snackbar.make(view, "Operation locked. Checks your Latch", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } catch (Exception e) {
                fileData = e.getMessage();
                e.printStackTrace();
            }

            ((TextView) view).setText(fileData);
        }

    }

    private String doHttpConnection() throws URISyntaxException, IOException {
        // Create connection objects

        HttpClient httpClient = new DefaultHttpClient();
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        URI endpointUri = new URI(DOCUMENT_ENDPOINT_URI);

        // Make the connection
        HttpResponse response = httpClient.execute(new HttpGet(endpointUri));

        // Print the results of the document endpoint
        return IOUtils.toString(response.getEntity().getContent());
    }

    private String extractDocumentData(String b64SignedData) throws Exception {
        CMSSignedData decodedSignedData = unmarshallPkcs7Data(b64SignedData);

        CMSProcessable signedData = decodedSignedData.getSignedContent();
        byte[] signedContent = (byte[]) signedData.getContent();
        return new String(signedContent, "UTF-8");
    }

    private boolean verifyPKCS7Signature(String b64SignedData) throws Exception {
        CMSSignedData decodedSignedData = unmarshallPkcs7Data(b64SignedData);

        Object[] firstSignerIdAndCertificate = getFirstSignerIdAndCertificate(decodedSignedData);
        SignerInformation signer = (SignerInformation) firstSignerIdAndCertificate[0];
        X509Certificate certificate = (X509Certificate) firstSignerIdAndCertificate[1];

        return signer.verify(certificate.getPublicKey(), "BC");
    }

    private CMSSignedData unmarshallPkcs7Data(String pkcs7DataB64Encoded) throws Exception {
        byte[] pkcs7Bytes = Base64.decode(pkcs7DataB64Encoded, Base64.DEFAULT);

        // Se crea un objeto SignedData con los datos firmados a partir de los bytes recibidos.
        return new CMSSignedData(pkcs7Bytes);
    }

    private Object[] getFirstSignerIdAndCertificate(CMSSignedData signedData) throws Exception {

        CertStore store = signedData.getCertificatesAndCRLs("Collection", "BC");
        SignerInformationStore signers = signedData.getSignerInfos();
        Collection signerCollection = signers.getSigners();
        Iterator it = signerCollection.iterator();
        SignerInformation signer = (SignerInformation) it.next();
        Collection certCollection = store.getCertificates(signer.getSID());
        Iterator certIt = certCollection.iterator();

        Certificate resultCert = (Certificate) certIt.next();

        return new Object[]{signer, resultCert};
    }
}
