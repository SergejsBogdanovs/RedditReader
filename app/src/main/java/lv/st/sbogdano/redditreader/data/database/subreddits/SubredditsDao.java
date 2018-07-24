package lv.st.sbogdano.redditreader.data.database.subreddits;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SubredditsDao {

    @Query("SELECT * FROM subreddits")
    LiveData<List<SubredditEntry>> getSubbreddits();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SubredditEntry> subredditEntries);

    @Query("DELETE FROM subreddits")
    void deleteSubreddits();

    @Query("DELETE FROM subreddits WHERE subredditName = :name")
    void deleteSubreddit(String name);

}
