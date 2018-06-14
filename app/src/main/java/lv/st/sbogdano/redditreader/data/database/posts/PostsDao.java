package lv.st.sbogdano.redditreader.data.database.posts;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;

@Dao
public interface PostsDao {

    @Query("SELECT * FROM posts")
    LiveData<List<PostEntry>> getPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostEntry> postEntries);

    @Query("DELETE FROM posts")
    void deleteOldPosts();
}
