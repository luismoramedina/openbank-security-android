package com.example.lmora.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

public class FingerprintActivity extends AppCompatActivity {

    private SpassFingerprint spassFingerprint;

    private SpassFingerprint.IdentifyListener listener = new SpassFingerprint.IdentifyListener() {
        @Override
        public void onFinished(int eventStatus) {
            if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
                doOkAction();
                System.out.println("eventStatus = " + eventStatus);
            } else {
                System.out.println("ERROR");
            }
        }

        @Override
        public void onReady() {
            System.out.println("FingerprintActivity.onReady");
        }

        @Override
        public void onStarted() {
            System.out.println("FingerprintActivity.onStarted");
        }
    };

    private void doOkAction() {
        View view = findViewById(R.id.toolbar);
        Snackbar.make(view, "Fingerprint ok", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private Spass pass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = new Spass();
                try {
                    pass.initialize(FingerprintActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean featureEnabled = pass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);
                System.out.println("featureEnabled = " + featureEnabled);
                if (featureEnabled) {
                    spassFingerprint = new SpassFingerprint(FingerprintActivity.this);
                } else {
                    System.out.println("featureEnabled = " + featureEnabled);
                }

                spassFingerprint.startIdentifyWithDialog(FingerprintActivity.this, listener, false);
            }
        });
    }


    }
