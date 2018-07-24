package lv.st.sbogdano.redditreader.ui.widget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.model.UserSubreddit;
import lv.st.sbogdano.redditreader.util.AppDelegate;

public class PostWidgetManager {

    private static final String KEY_SUBREDDIT = "Subreddits";
    private static final String PREFERENCES_NAME = "PostWidgetManager";

    private final SharedPreferences mSharedPreferences;
    private final Application mApplication;
    private final ObjectMapper mObjectMapper;

    public PostWidgetManager() {
        mApplication = AppDelegate.getAppContext();
        mSharedPreferences = mApplication.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        mObjectMapper = new ObjectMapper();
    }

    public void updatePostWidget(List<SubredditEntry> subredditEntries) {

        List<UserSubreddit> userSubreddits = transformToUserSubreddits(subredditEntries);

        String subredditJson = null;
        try {
            subredditJson = mObjectMapper.writeValueAsString(userSubreddits);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        mSharedPreferences.edit().putString(KEY_SUBREDDIT, subredditJson).apply();

        updateWidget();
    }

    private void updateWidget() {

        Intent intent = new Intent(mApplication, PostWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mApplication);
        ComponentName componentName = new ComponentName(mApplication, PostWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        mApplication.sendBroadcast(intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    public List<UserSubreddit> getUserSubreddits() {
        String subredditsJson = mSharedPreferences.getString(KEY_SUBREDDIT, null);
        TypeReference<List<UserSubreddit>> mapType = new TypeReference<List<UserSubreddit>>() {};
        List<UserSubreddit> userSubreddits = null;
        try {
            userSubreddits = mObjectMapper.readValue(subredditsJson, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userSubreddits;
    }

    private List<UserSubreddit> transformToUserSubreddits(List<SubredditEntry> subredditEntries) {
        List<UserSubreddit> userSubreddits = new ArrayList<>();
        for (SubredditEntry subredditEntry : subredditEntries) {
            UserSubreddit userSubreddit = new UserSubreddit(subredditEntry.getSubredditName());
            userSubreddits.add(userSubreddit);
        }
        return userSubreddits;
    }

}
