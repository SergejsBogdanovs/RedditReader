package lv.st.sbogdano.redditreader.ui.login;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.NoSuchTokenException;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.auth.TokenStore;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.net.URL;

public class Authentication {

    private static final String LOG_TAG = Authentication.class.getSimpleName();

    private static Authentication INSTANCE;
    private Context mContext;
    private final Credentials mCredentials;

    public static Authentication getInstance(Context context) throws PackageManager.NameNotFoundException {
        if (INSTANCE == null) {
            synchronized (Authentication.class) {
                INSTANCE = new Authentication(context);
            }
        }
        return INSTANCE;
    }

    private Authentication(Context context) throws PackageManager.NameNotFoundException {

        AppInfoProvider appInfoProvider = new ManifestAppInfoProvider(context);
        AppInfo appInfo = appInfoProvider.provide();

        mContext = context;
        mCredentials = Credentials.installedApp(appInfo.getClientId(), appInfo.getRedirectUrl());

        TokenStore tokenStore = new TokenStore() {
            @Override
            public boolean isStored(String key) {
                return PreferenceManager.getDefaultSharedPreferences(mContext).contains(key);
            }

            @Override
            public String readToken(String key) throws NoSuchTokenException {
                String token = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext)
                        .getString(key, null);
                if (token == null) {
                    throw new NoSuchTokenException(key);
                }
                return token;
            }

            @Override
            public void writeToken(String key, String token) {
                android.preference.PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                        .putString(key, token);
            }
        };

        UserAgent userAgent = appInfo.getUserAgent();
        RedditClient redditClient = new RedditClient(userAgent);

        RefreshTokenHandler handler = new RefreshTokenHandler(tokenStore, redditClient);

        AuthenticationManager.get().init(redditClient, handler);
    }


    public URL getAuthUrl() {
        final OAuthHelper helper = AuthenticationManager.get().getRedditClient().getOAuthHelper();
        String[] scopes = {"identity", "read", "mysubreddits", "vote", "submit"};
        final URL authorizationUrl = helper.getAuthorizationUrl(
                mCredentials,
                true,
                true,
                scopes);

        return authorizationUrl;
    }

    public Credentials getCredentials() {
        return mCredentials;
    }


    public void refreshAuthenTokenAsync() {

        new AsyncTask<Credentials, Void, Void>() {
            @Override
            protected Void doInBackground(Credentials... params) {
                try {
                    AuthenticationManager.get().refreshAccessToken(mCredentials);
                } catch (NoSuchTokenException | OAuthException e) {
                    Log.e(LOG_TAG, "Could not refresh access token ", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                Log.d(LOG_TAG, "Reauthenticate");
            }
        }.execute();
    }

}
