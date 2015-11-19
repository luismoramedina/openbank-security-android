package com.example.lmora.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

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
            //TODO parse json
            ((TextView) view).setText(result);
        }

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


}
