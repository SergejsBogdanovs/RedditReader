package lv.st.sbogdano.redditreader.ui.login;

import android.content.pm.PackageManager;
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

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        try {
            final Authentication authentication = Authentication.getInstance(this);
            final URL authenticationUrl = authentication.getAuthUrl();
            mWebView.loadUrl(authenticationUrl.toExternalForm());

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (url.contains("code=")) {
                        Log.d(LOG_TAG, "webview url: " + url);
                        // We've detected the redirect URL
                        onUserChallenge(url, authentication.getCredentials());
                    } else if (url.contains("error=")) {
                        Toast.makeText(
                                LoginActivity.this,
                                "Press 'ALLOW' to log in with this account",
                                Toast.LENGTH_SHORT).show();
                        mWebView.loadUrl(authenticationUrl.toExternalForm());
                    }
                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onUserChallenge(String url, Credentials credentials) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    OAuthData data = AuthenticationManager.get().getRedditClient().getOAuthHelper()
                            .onUserChallenge(params[0], credentials);
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
