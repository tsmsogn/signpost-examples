package com.tsmsogn.signpost.twitter;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

public class TwitterOauthActivity extends Activity {
    private final static String TAG = TwitterOauthActivity.class
            .getCanonicalName();
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
        if (uri != null
                && uri.toString().startsWith(Constants.CALLBACK_URL)) {
            String veriﬁer = uri
                    .getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

            try {
                // this will populate token and token_secret in consumer
                provider.retrieveAccessToken(consumer, veriﬁer);
                String userKey = consumer.getToken();
                String userSecret = consumer.getTokenSecret();

                // Save user_key and user_secret in user preferences and return
                SharedPreferences settings = getBaseContext()
                        .getSharedPreferences("your_app_prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_key", userKey);
                editor.putString("user_secret", userSecret);
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
