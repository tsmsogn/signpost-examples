package com.tsmsogn.signpost.twitter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.tsmsogn.signpost.twitter.R;

public class TwitterOAuthWithPINActivity extends Activity implements
        OnClickListener {
    private final static String TAG = TwitterOAuthWithPINActivity.class
            .getCanonicalName();
    private Button mTwitterButton;
    private EditText mTwitterPinEditText;
    final private CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
            Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    final private CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(
            Constants.REQUEST_TOKEN_ENDPOINT_URL,
            Constants.ACCESS_TOKEN_ENDPOINT_URL,
            Constants.AUTHORIZATION_WEBSITE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_oauth_with_pin);

        // we do not support callbacks, thus pass OOB
        try {
            String authUrl = provider.retrieveRequestToken(consumer,
                    OAuth.OUT_OF_BAND);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTwitterButton = (Button) findViewById(R.id.button1);
        mTwitterPinEditText = (EditText) findViewById(R.id.editText1);
        mTwitterButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == mTwitterButton) {
            try {
                provider.retrieveAccessToken(consumer, getPin());
            } catch (OAuthMessageSignerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OAuthNotAuthorizedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.v(TAG, "Access token: " + consumer.getToken());
            Log.v(TAG, "Token secret:" + consumer.getTokenSecret());
            consumer.setTokenWithSecret(consumer.getToken(),
                    consumer.getTokenSecret());
            URL url = null;
            try {
                url = new URL(
                        "https://api.twitter.com/1.1/statuses/mentions_timeline.json");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            HttpURLConnection request = null;
            try {
                request = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                consumer.sign(request);
            } catch (OAuthMessageSignerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                request.connect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                System.out.println("Response: " + request.getResponseCode()
                        + " " + request.getResponseMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String getPin() {
        return mTwitterPinEditText.getText().toString();
    }

}
