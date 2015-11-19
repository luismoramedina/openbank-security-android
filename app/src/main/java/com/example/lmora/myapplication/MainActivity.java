package com.example.lmora.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String SCAN_QR_INTENT_ID = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanQRIntent();
            }

            private void startScanQRIntent() {
                Intent scanQRIntent = new Intent(SCAN_QR_INTENT_ID);
                scanQRIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(scanQRIntent, 0);
            }
        });
    }

    private static final String DOCUMENT_ENDPOINT_URI = "https://dl.dropboxusercontent.com/u/1368598/data.txt";

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int resultCode, int requestCode, Intent intent) {
        if (resultCode == 0) {
            String documentCode = intent.getStringExtra("SCAN_RESULT");
        }

//    private class DownloadDocTask extends AsyncTask<URL, Integer, String> {
//        private final View view;
//
//        public DownloadDocTask(View view) {
//            this.view = view;
//        }
//
//        protected String doInBackground(URL... urls) {
//            // Create connection objects
//            HttpClient httpClient = new DefaultHttpClient();
//            URI endpointUri = null;
//            String str = null;
//            try {
//                str = doHttpConnection();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return str;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//            //setProgressPercent(progress[0]);
//        }
//
//        protected void onPostExecute(String result) {
//            Snackbar.make(view, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//        }
//
//    }
    }
}
