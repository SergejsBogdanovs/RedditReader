package lv.st.sbogdano.redditreader.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import lv.st.sbogdano.redditreader.AppDelegate;

public class SubredditPreferences {

    public static final String SETTINGS_NAME = "post_prefs";
    public static final String USER_SUBREDDITS_KEY = "user_subreddit_key";


    @NonNull
    public static SharedPreferences getPrefs() {
        return AppDelegate.getAppContext()
                .getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static void setPrefs(String userSubreddits) {
        SharedPreferences prefs = getPrefs();
        prefs.edit().putString(USER_SUBREDDITS_KEY, userSubreddits).apply();
    }

}
