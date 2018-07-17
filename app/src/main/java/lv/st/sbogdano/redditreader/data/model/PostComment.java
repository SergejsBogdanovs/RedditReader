package lv.st.sbogdano.redditreader.data.model;

import java.util.Date;

public class PostComment {

    private String mCommentBody;
    private String mCommentAuthor;
    private Date mCommentDate;
    private int mDepth;

    public PostComment(String commentBody, String commentAuthor, Date commentDate, int depth) {
        mCommentBody = commentBody;
        mCommentAuthor = commentAuthor;
        mCommentDate = commentDate;
        mDepth = depth;
    }

    public String getCommentBody() {
        return mCommentBody;
    }

    public void setCommentBody(String commentBody) {
        mCommentBody = commentBody;
    }

    public String getCommentAuthor() {
        return mCommentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        mCommentAuthor = commentAuthor;
    }

    public Date getCommentDate() {
        return mCommentDate;
    }

    public void setCommentDate(Date commentDate) {
        mCommentDate = commentDate;
    }

    public int getDepth() {
        return mDepth;
    }

    public void setDepth(int depth) {
        mDepth = depth;
    }
}
