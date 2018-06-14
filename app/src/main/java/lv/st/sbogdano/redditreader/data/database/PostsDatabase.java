package lv.st.sbogdano.redditreader.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.posts.PostsDao;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditsDao;

@Database(entities = {PostEntry.class, SubredditEntry.class} ,version = 1)
public abstract class PostsDatabase extends RoomDatabase{

    private static final String DATABASE_NAME = "reddit";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static PostsDatabase sInstance;

    public static PostsDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        PostsDatabase.class,
                        PostsDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract PostsDao postsDao();
    public abstract SubredditsDao subredditsDao();
}
