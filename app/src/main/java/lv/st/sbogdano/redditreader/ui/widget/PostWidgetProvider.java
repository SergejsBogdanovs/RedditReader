package lv.st.sbogdano.redditreader.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import lv.st.sbogdano.redditreader.R;

public class PostWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "PostWidgetProvider";

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId,
                                PostWidgetManager postWidgetManager) {

        if (postWidgetManager.getUserSubreddits() != null) {
            String widgetTitle = context.getString(R.string.my_subscribed_subreddits);
            Log.v(TAG, "updateAppWidget: " + postWidgetManager.getUserSubreddits().size() );
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.post_widget);
            remoteViews.setTextViewText(R.id.widget_title, widgetTitle);
            remoteViews.setRemoteAdapter(R.id.widget_list,
                    new Intent(context, PostWidgetRemoteViewService.class));

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        PostWidgetManager postWidgetManager = new PostWidgetManager();

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, postWidgetManager);
        }
    }
}
