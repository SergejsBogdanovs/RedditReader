package lv.st.sbogdano.redditreader.util;

import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.http.LoggingMode;

import lv.st.sbogdano.jrawlibrary.AndroidRedditClient;
import lv.st.sbogdano.jrawlibrary.AndroidTokenStore;

public class AppDelegate extends MultiDexApplication{

    private static AppDelegate sInstance;
    private static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        RedditClient redditClient = new AndroidRedditClient(this);
        redditClient.setLoggingMode(LoggingMode.ALWAYS);
        AuthenticationManager.get().init(
                redditClient,
                new RefreshTokenHandler(new AndroidTokenStore(this), redditClient));

        Stetho.initializeWithDefaults(this);

        MobileAds.initialize(this, ADMOB_APP_ID);
    }

    public static AppDelegate getAppContext() {
        return sInstance;
    }

}
