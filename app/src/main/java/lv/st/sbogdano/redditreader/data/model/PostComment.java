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


    public String getCommentAuthor() {
        return mCommentAuthor;
    }


    public Date getCommentDate() {
        return mCommentDate;
    }


    public int getDepth() {
        return mDepth;
    }

}
