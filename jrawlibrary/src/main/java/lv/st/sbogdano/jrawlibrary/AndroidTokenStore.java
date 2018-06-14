package lv.st.sbogdano.jrawlibrary;

import android.content.Context;
import android.content.SharedPreferences;

import net.dean.jraw.auth.NoSuchTokenException;
import net.dean.jraw.auth.TokenStore;

public class AndroidTokenStore implements TokenStore{

    private final Context mContext;

    public AndroidTokenStore(Context context) {
        mContext = context;
    }

    @Override
    public boolean isStored(String key) {
        return getSharedPreferences().contains(key);
    }

    @Override
    public String readToken(String key) throws NoSuchTokenException {
        String token = getSharedPreferences().getString(key, null);
        if (token == null) {
            throw new NoSuchTokenException("Token for key '" + key + "' does not exist");
        }
        return token;
    }

    @Override
    public void writeToken(String key, String token) {
        getSharedPreferences().edit()
                .putString(key, token)
                .apply();
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(mContext.getString(R.string.prefs_file), Context.MODE_PRIVATE);
    }
}
