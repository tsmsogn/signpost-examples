package com.tsmsogn.signpost.twitter;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TwitterOAuthWithPIN extends Activity implements OnClickListener {
    private final static String TAG = TwitterOAuthWithPIN.class
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
            Log.v(TAG, "Token: " + consumer.getToken());
            Log.v(TAG, "Token secret:" + consumer.getTokenSecret());
        }
    }

    private String getPin() {
        return mTwitterPinEditText.getText().toString();
    }

}
