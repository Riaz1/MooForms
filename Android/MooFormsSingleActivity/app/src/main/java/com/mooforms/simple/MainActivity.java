package com.mooforms.simple;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //TODO: get the below information from your MooForms account (integration page)
    public static String domain = "";
    public static String publicURL = "";
    public static String privateURL = "";
    public static String privateKey = "";

    public static boolean usePrivateURL = false; //change to true if using a private form

    protected WebView myWebView;
    public static String offlineURL = "file:///android_asset/offline.html";

    private static String appID = null;
    private static final String PREF_APP_ID = "PREF_APP_ID";
    ProgressBar progressBar;

    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //M - MARSHMALLOW

            List<String> permissionsList = new ArrayList<String>();

            addPermission(permissionsList, Manifest.permission.INTERNET);
            addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE);

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }


        // To show the ProgressBar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        myWebView = (WebView) findViewById(R.id.webview);
        //url loading from javascript
        myWebView.setWebViewClient(new MyWebViewClient());

        //enable javascript
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //are we using the private or public url?
        String formURL = getFormURL();

        myWebView.loadUrl(offlineURL);
        if (isNetworkAvailable() ) {
            myWebView.loadUrl(formURL); // loading online
        }
    }

    private String getFormURL() {
        String URLToUse;
        if (usePrivateURL) { //are we using a private URL
            URLToUse = privateURL;
            URLToUse = addURLParam(URLToUse, "app_id", id(this)); //add the app id
            URLToUse = addURLParam(URLToUse, "key", privateKey); //add the private key
        } else { //public URL
            URLToUse = publicURL;
            URLToUse = addURLParam(URLToUse, "app_id", id(this)); //add the app id
        }

        return URLToUse;
    }

    private String addURLParam(String url, String param, String value) {
        String uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(param, value)
                .build().toString();

        return uri;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public synchronized static String id(Context context) {
        if (appID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_APP_ID, Context.MODE_PRIVATE);
            appID = sharedPrefs.getString(PREF_APP_ID, null);

            if (appID == null) {
                appID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_APP_ID, appID);
                editor.commit();
            }
        }
        return appID;
    }


    private void addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void showErrorView() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE); // To hide the ProgressBar
        }
        myWebView.loadUrl(offlineURL); //offlineURL
    }

    private void pageFinished() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE); // To hide the ProgressBar
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (Uri.parse(url).getHost().equals(domain)) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {

            super.onReceivedError(view, request, error);
            showErrorView();
        }

        /*
          Added in API level 23
        */
        @Override
        public void onReceivedHttpError(WebView view,
                                        WebResourceRequest request, WebResourceResponse errorResponse) {

            super.onReceivedHttpError(view, request, errorResponse);
            showErrorView();
        }

        @Override
        public void onPageFinished(WebView view, String url){
            pageFinished();
        }

    }

}
