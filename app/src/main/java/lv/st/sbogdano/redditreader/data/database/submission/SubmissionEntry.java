package lv.st.sbogdano.redditreader.data.database.submission;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "submissions")
public class SubmissionEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String submissionId;
    private boolean isPostLiked;

    public SubmissionEntry(int id, String submissionId, boolean isPostLiked) {
        this.id = id;
        this.submissionId = submissionId;
        this.isPostLiked = isPostLiked;
    }

    @Ignore
    public SubmissionEntry(String submissionId, boolean isPostLiked) {
        this.submissionId = submissionId;
        this.isPostLiked = isPostLiked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public boolean isPostLiked() {
        return isPostLiked;
    }

    public void setPostLiked(boolean postLiked) {
        isPostLiked = postLiked;
    }
}
