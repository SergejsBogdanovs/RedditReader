package lv.st.sbogdano.redditreader.data.database.posts;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "posts")
public class PostEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String subredditName;
    private String postTitle;
    private String postAuthor;
    private String postPermLink;
    private String postThumbnail;
    private int postScore;
    private int postCommentsCount;

    public PostEntry(int id, String subredditName, String postTitle, String postAuthor, String postPermLink, String postThumbnail, int postScore, int postCommentsCount) {
        this.id = id;
        this.subredditName = subredditName;
        this.postTitle = postTitle;
        this.postAuthor = postAuthor;
        this.postPermLink = postPermLink;
        this.postThumbnail = postThumbnail;
        this.postScore = postScore;
        this.postCommentsCount = postCommentsCount;
    }

    @Ignore
    public PostEntry(String subredditName, String postTitle, String postAuthor, String postPermLink, String postThumbnail, int postScore, int postCommentsCount) {
        this.subredditName = subredditName;
        this.postTitle = postTitle;
        this.postAuthor = postAuthor;
        this.postPermLink = postPermLink;
        this.postThumbnail = postThumbnail;
        this.postScore = postScore;
        this.postCommentsCount = postCommentsCount;
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

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostPermLink() {
        return postPermLink;
    }

    public void setPostPermLink(String postPermLink) {
        this.postPermLink = postPermLink;
    }

    public String getPostThumbnail() {
        return postThumbnail;
    }

    public void setPostThumbnail(String postThumbnail) {
        this.postThumbnail = postThumbnail;
    }

    public int getPostScore() {
        return postScore;
    }

    public void setPostScore(int postScore) {
        this.postScore = postScore;
    }

    public int getPostCommentsCount() {
        return postCommentsCount;
    }

    public void setPostCommnetCount(int postCommentsCount) {
        this.postCommentsCount = postCommentsCount;
    }
}
