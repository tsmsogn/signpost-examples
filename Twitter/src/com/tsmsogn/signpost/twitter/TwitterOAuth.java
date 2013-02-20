package com.tsmsogn.signpost.twitter;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class TwitterOAuth extends Activity {
    private final static String TAG = TwitterOAuth.class.getCanonicalName();
    final private CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
            Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    final private CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(
            Constants.REQUEST_TOKEN_ENDPOINT_URL,
            Constants.ACCESS_TOKEN_ENDPOINT_URL,
            Constants.AUTHORIZATION_WEBSITE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_oauth);

        // we do not support callbacks, thus pass OOB
        try {
            String authUrl = provider.retrieveRequestToken(consumer,
                    Constants.CALLBACK_URL);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith(Constants.CALLBACK_URL)) {
            String verifier = uri
                    .getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

            try {
                // this will populate token and token_secret in consumer
                provider.retrieveAccessToken(consumer, verifier);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v(TAG, "Token: " + consumer.getToken());
            Log.v(TAG, "Token secret:" + consumer.getTokenSecret());
        }

    }

}
