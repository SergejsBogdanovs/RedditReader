package lv.st.sbogdano.redditreader.data.database.posts;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PostsDao {

//    @Query("SELECT * FROM posts")
//    DataSource.Factory<Integer, PostEntry> getPosts();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<PostEntry> postEntries);
//
//    @Query("DELETE FROM posts")
//    void deleteAllPosts();
//
//    @Query("DELETE FROM posts WHERE subredditName = :name")
//    void deletePostsByName(String name);

}
