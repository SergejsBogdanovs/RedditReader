package lv.st.sbogdano.jrawlibrary;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import net.dean.jraw.http.UserAgent;

public class ManifestAppInfoProvider implements AppInfoProvider {

    private static final String KEY_USER_AGENT_OVERRIDE = "lv.st.sbogdano.redditreader.USER_AGENT_OVERRIDE";
    private static final String KEY_REDDIT_USERNAME = "lv.st.sbogdano.redditreader.REDDIT_USERNAME";
    private static final String KEY_CLIENT_ID = "lv.st.sbogdano.redditreader.CLIENT_ID";
    private static final String KEY_REDIRECT_URL = "lv.st.sbogdano.redditreader.REDIRECT_URL";
    private static final String PLATFORM = "android";

    private final Context mContext;

    public ManifestAppInfoProvider(Context context) {
        mContext = context;
    }

    @Override
    public AppInfo provide() throws PackageManager.NameNotFoundException {

        Bundle metadata = mContext
                .getPackageManager()
                .getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA)
                .metaData;

        String version = mContext
                .getPackageManager()
                .getPackageInfo(mContext.getPackageName(), 0)
                .versionName;

        UserAgent userAgent = userAgent(metadata, version);
        String clientId = requireString(metadata, KEY_CLIENT_ID, "client ID");
        String redirectUrl = requireString(metadata, KEY_REDIRECT_URL, "redirect URL");

        return new AppInfo(clientId, redirectUrl, userAgent);
    }

    private String requireString(Bundle metadata, String key, String what) {
        String client = metadata.getString(key);
        if (client != null) {
            return client;
        }

        throw new IllegalStateException("Could not produce a " + what +
                " from the manifest. Make sure to include a <meta-data> tag with the " + key + " key.");
    }

    private UserAgent userAgent(Bundle metadata, String version) {

        String userName = metadata.getString(KEY_REDDIT_USERNAME);
        if (userName != null) {
            return UserAgent.of(PLATFORM, mContext.getPackageName(), version, userName);
        }

        String override = metadata.getString(KEY_USER_AGENT_OVERRIDE);
        if (override != null) {
            return UserAgent.of(override);
        }

        throw new IllegalStateException("Could produce a UserAgent from the manifest. Make sure to " +
                "include a <meta-data> tag with either the" + KEY_REDDIT_USERNAME + " or " +
                KEY_USER_AGENT_OVERRIDE + " key.");
    }

}
