package lv.st.sbogdano.redditreader.ui.login;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;

public class LoginActivity extends AppCompatActivity {

    public static final Credentials CREDENTIALS = Credentials.installedApp("PT-oBeSZSm4eVA",
            "https://github.com/SergejsBogdanovs/RedditReader");

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        final OAuthHelper helper = AuthenticationManager.get().getRedditClient().getOAuthHelper();

        String[] scopes = {"identity", "read", "subscribe", "mysubreddits", "vote"};

        final URL authorizationUrl = helper.getAuthorizationUrl(
                CREDENTIALS,
                true,
                true,
                scopes);

        mWebView.loadUrl(authorizationUrl.toExternalForm());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("code=")) {
                    onUserChallenge(url);
                } else if (url.contains("error=")) {
                    Toast.makeText(LoginActivity.this, R.string.must_login, Toast.LENGTH_SHORT).show();
                    mWebView.loadUrl(authorizationUrl.toExternalForm());
                }
            }
        });

    }

    private void onUserChallenge(String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    OAuthData data = AuthenticationManager
                            .get()
                            .getRedditClient()
                            .getOAuthHelper()
                            .onUserChallenge(params[0], CREDENTIALS);

                    AuthenticationManager.get().getRedditClient().authenticate(data);
                    return AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
                } catch (NetworkException | OAuthException e) {
                    Log.e(LOG_TAG, "Could not log in", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                Log.i(LOG_TAG, s);
                LoginActivity.this.finish();
            }
        }.execute(url);
    }
}
