package lv.st.sbogdano.redditreader.ui.comments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.model.PostComment;

public class CommentsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_author)
    TextView mCommentAuthor;
    @BindView(R.id.comment_date)
    TextView mCommentDate;
    @BindView(R.id.comment_body)
    TextView mCommentBody;

    public static CommentsViewHolder getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentsViewHolder(view);
    }

    private CommentsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(PostComment postComment) {
        mCommentAuthor.setText(postComment.getCommentAuthor());

        Date date = postComment.getCommentDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        mCommentDate.setText(sdf.format(date));

        mCommentBody.setText(postComment.getCommentBody());
    }
}
