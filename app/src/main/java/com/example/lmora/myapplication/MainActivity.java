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
    public final static String EXTRA_MESSAGE = "com.example.lmora.myapplication.MESSAGE";

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
                startViewDocumentActivity("this is a code");
                //startScanQRIntent();
            }

            private void startScanQRIntent() {
                Intent scanQRIntent = new Intent(SCAN_QR_INTENT_ID);
                scanQRIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(scanQRIntent, 0);
            }
        });
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(myIntent);
            }
        });
*/

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanQRIntent();
                startScanQRIntent();
            }

            private void startScanQRIntent() {
                Intent scanQRIntent = new Intent(SCAN_QR_INTENT_ID);
                scanQRIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(scanQRIntent, 0);
            }
        });
        */
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new DownloadDocTask(view).execute();
            }

            private void startScanQRIntent() {
                Intent scanQRIntent = new Intent(SCAN_QR_INTENT_ID);
                scanQRIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(scanQRIntent, 0);
            }
        });
*/
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

    /**
     * QR read callback
     */
    @Override
    public void onActivityResult(int resultCode, int requestCode, Intent intent) {
        if (resultCode == 0) {
            if (intent != null) {
                String qrCode = intent.getStringExtra("SCAN_RESULT");
                startViewDocumentActivity(qrCode);
            }
        }

    }

    private void startViewDocumentActivity(String qrCode) {
        Intent documentViewIntent = new Intent(this, ResultActivity.class);
        documentViewIntent.putExtra(EXTRA_MESSAGE, qrCode);
        startActivity(documentViewIntent);
    }
}
