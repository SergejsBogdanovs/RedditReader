package lv.st.sbogdano.redditreader;

import android.app.Application;

import com.facebook.stetho.Stetho;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.http.LoggingMode;

import lv.st.sbogdano.jrawlibrary.AndroidRedditClient;
import lv.st.sbogdano.jrawlibrary.AndroidTokenStore;

public class AppDelegate extends Application{

    private static AppDelegate sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        RedditClient redditClient = new AndroidRedditClient(this);
        redditClient.setLoggingMode(LoggingMode.ALWAYS);
        AuthenticationManager.get().init(
                redditClient,
                new RefreshTokenHandler(new AndroidTokenStore(this), redditClient));

        Stetho.initializeWithDefaults(this);
    }

    public static AppDelegate getAppContext() {
        return sInstance;
    }


}
