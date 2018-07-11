package lv.st.sbogdano.redditreader.data.database.posts;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//@Entity(tableName = "posts",
//        indices = {@Index(value = {"postTitle"}, unique = true)})
public class PostEntry {

//    @PrimaryKey(autoGenerate = true)
    private String subredditId;
    private String subredditName;
    private String postTitle;
    private String postAuthor;
    private String postPermLink;
    private String postThumbnail;
    private int postScore;
    private int postCommentsCount;

    public PostEntry(String subredditId, String subredditName, String postTitle, String postAuthor, String postPermLink, String postThumbnail, int postScore, int postCommentsCount) {
        this.subredditId = subredditId;
        this.subredditName = subredditName;
        this.postTitle = postTitle;
        this.postAuthor = postAuthor;
        this.postPermLink = postPermLink;
        this.postThumbnail = postThumbnail;
        this.postScore = postScore;
        this.postCommentsCount = postCommentsCount;
    }

//    @Ignore
//    public PostEntry(String subredditName, String postTitle, String postAuthor, String postPermLink, String postThumbnail, int postScore, int postCommentsCount) {
//        this.subredditName = subredditName;
//        this.postTitle = postTitle;
//        this.postAuthor = postAuthor;
//        this.postPermLink = postPermLink;
//        this.postThumbnail = postThumbnail;
//        this.postScore = postScore;
//        this.postCommentsCount = postCommentsCount;
//    }


    public String getSubredditId() {
        return subredditId;
    }

    public void setSubredditId(String subredditId) {
        this.subredditId = subredditId;
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
