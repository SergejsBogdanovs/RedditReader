package lv.st.sbogdano.jrawlibrary;

import net.dean.jraw.http.UserAgent;

public class AppInfo {

    private String mClientId;
    private String mRedirectUrl;
    private UserAgent mUserAgent;

    public AppInfo(String clientId, String redirectUrl, UserAgent userAgent) {
        mClientId = clientId;
        mRedirectUrl = redirectUrl;
        mUserAgent = userAgent;
    }

    public String getClientId() {
        return mClientId;
    }

    public String getRedirectUrl() {
        return mRedirectUrl;
    }

    public UserAgent getUserAgent() {
        return mUserAgent;
    }
}
