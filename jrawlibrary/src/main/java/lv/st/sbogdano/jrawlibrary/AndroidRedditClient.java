package lv.st.sbogdano.jrawlibrary;

import android.content.Context;
import android.content.pm.PackageManager;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.HttpRequest;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.RestResponse;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.InvalidScopeException;

public class AndroidRedditClient extends RedditClient{

    private static UserAgent getUserAgent(Context context) {

        ManifestAppInfoProvider manifestAppInfoProvider = new ManifestAppInfoProvider(context);

        try {
            AppInfo appInfo = manifestAppInfoProvider.provide();
            return appInfo.getUserAgent();
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException("Could not find package metadata for own package", e);
        }
    }

    public AndroidRedditClient(Context context) {
        this(getUserAgent(context));
    }

    public AndroidRedditClient(UserAgent userAgent) {
        super(userAgent);
    }

    @Override
    public RestResponse execute(HttpRequest request) throws NetworkException, InvalidScopeException {
        if (getUserAgent().trim().isEmpty()) {
            throw new IllegalStateException("No UserAgent specified");
        }
        return super.execute(request);
    }

}
