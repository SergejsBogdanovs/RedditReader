package lv.st.sbogdano.redditreader.data.database.subreddits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "subreddits")
public class SubredditEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String subredditName;
    private boolean favorite;

    public SubredditEntry(int id, String subredditName, boolean favorite) {
        this.id = id;
        this.subredditName = subredditName;
        this.favorite = favorite;
    }

    @Ignore
    public SubredditEntry(String subredditName, boolean favorite) {
        this.subredditName = subredditName;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
