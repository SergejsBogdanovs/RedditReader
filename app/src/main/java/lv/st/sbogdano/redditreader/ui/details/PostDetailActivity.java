package lv.st.sbogdano.redditreader.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import net.dean.jraw.models.Submission;
import net.dean.jraw.models.VoteDirection;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.submission.SubmissionEntry;
import lv.st.sbogdano.redditreader.ui.comments.CommentsActivity;
import lv.st.sbogdano.redditreader.util.Constants;
import lv.st.sbogdano.redditreader.viewmodels.PostDetailViewModel;
import lv.st.sbogdano.redditreader.viewmodels.ViewModelFactory;

public class PostDetailActivity extends AppCompatActivity {

    public static final String SUBMISSION_EXTRA = "SUBMISSION_EXTRA";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.post_detail_title)
    TextView mPostDetailTitle;
    @BindView(R.id.post_detail_author)
    TextView mPostDetailUsername;
    @BindView(R.id.post_detail_date)
    TextView mPostDetailDate;
    @BindView(R.id.post_detail_image)
    ImageView mPostDetailImage;
    @BindView(R.id.post_detail_share)
    ImageView mPostDetailShare;
    @BindView(R.id.post_detail_vote_count)
    TextView mPostDetailVoteCount;
    @BindView(R.id.post_detail_vote_down_img)
    ImageView mPostDetailVoteDownImg;
    @BindView(R.id.post_detail_vote_up_img)
    ImageView mPostDetailVoteUpImg;
    @BindView(R.id.post_detail_comment_count)
    TextView mPostDetailCommentCount;
    @BindView(R.id.post_detail_comment_img)
    ImageView mPostDetailCommentImg;
    @BindView(R.id.post_detail_body)
    TextView mPostDetailBody;
    @BindView(R.id.coordinator)
    LinearLayout mCoordinator;
    @BindView(R.id.view)
    View mView;

    private Submission mSubmission = null;
    private String mSubmissionJson;
    private Boolean mIsPostLiked;
    private PostDetailViewModel mPostDetailViewModel;

    public static void start(AppCompatActivity activity, String dataNode) {
        Intent postDetailIntent = new Intent(activity, PostDetailActivity.class);
        postDetailIntent.putExtra(SUBMISSION_EXTRA, dataNode);
        activity.startActivity(postDetailIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        if (getIntent().getExtras().containsKey(SUBMISSION_EXTRA)) {
            mSubmissionJson = getIntent().getStringExtra(SUBMISSION_EXTRA);
            try {
                mSubmission = new Submission(new ObjectMapper().readTree(mSubmissionJson));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mPostDetailViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(PostDetailViewModel.class);

        setupToolBar();
        bindUi();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(mSubmission.getSubredditName());
        }
    }

    private void bindUi() {

        /************************
         * Title
         ***********************/
        mPostDetailTitle.setText(mSubmission.getTitle());

        /************************
         * Post author
         ***********************/
        mPostDetailUsername.setText(mSubmission.getAuthor());

        /************************
         * Date
         ***********************/
        Date date = mSubmission.getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mPostDetailDate.setText(sdf.format(date));

        /************************
         * Thumbnail
         ***********************/
        String posterThumbnail = mSubmission.getThumbnail();
        if (posterThumbnail == null || posterThumbnail.isEmpty()) {
            mPostDetailImage.setVisibility(View.GONE);
        } else {
            switch (mSubmission.getPostHint()) {
                case SELF:
                    try {
                        mPostDetailBody.setVisibility(View.VISIBLE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                            mPostDetailBody.setText(Html.fromHtml(StringEscapeUtils
                                            .unescapeHtml4(mSubmission.data(Constants.SELF_TEXT)),
                                    Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            mPostDetailBody.setText(Html.fromHtml(StringEscapeUtils
                                    .unescapeHtml4(mSubmission.data(Constants.SELF_TEXT))));
                        }

                        mPostDetailBody.setMovementMethod(LinkMovementMethod.getInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LINK:
                case VIDEO:
                    try {
                        mPostDetailBody.setVisibility(View.VISIBLE);
                        mPostDetailBody.setText(mSubmission.getUrl());
                        mPostDetailBody.setTextColor(Color.RED);
                        mPostDetailBody.setOnClickListener(view -> {
                            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                            customTabsIntent.launchUrl(this, Uri.parse(mSubmission.getUrl()));
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case IMAGE:
                    mPostDetailImage.setVisibility(View.VISIBLE);
                    String imageResolution = mSubmission.getDataNode().get(Constants.PREVIEW)
                            .get(Constants.IMAGES).get(0)
                            .get(Constants.RESOLUTIONS).toString();
                    try {
                        JSONArray resolutionsArr = new JSONArray(imageResolution);
                        for (int i = 0; i < resolutionsArr.length(); i++) {
                            JSONObject imageObject = resolutionsArr.getJSONObject(i);
                            if (imageObject.getInt(Constants.WIDTH) == 216) {
                                String imgUrl;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    imgUrl = Html.fromHtml(StringEscapeUtils
                                                    .unescapeHtml4(imageObject.getString(Constants.URL)),
                                            Html.FROM_HTML_MODE_LEGACY).toString();
                                } else {
                                    imgUrl = Html.fromHtml(StringEscapeUtils
                                            .unescapeHtml4(imageObject.getString(Constants.URL))).toString();
                                }

                                Picasso.get().load(imgUrl).into(mPostDetailImage);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    break;
                case UNKNOWN:
                    try {
                        mPostDetailBody.setVisibility(View.VISIBLE);
                        mPostDetailBody.setText(Html.fromHtml(StringEscapeUtils
                                .unescapeHtml4(mSubmission.data(Constants.SELF_TEXT))));
                        mPostDetailBody.setMovementMethod(LinkMovementMethod.getInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        /************************
         * Votes
         ***********************/
        mPostDetailVoteCount.setText(String.valueOf(mSubmission.getScore()));

        if (mSubmission.data(Constants.LIKES) != null) {
            if (Boolean.parseBoolean(mSubmission.data(Constants.LIKES))) {
                mIsPostLiked = true;
                setVoteUpDrawable();
            } else {
                mIsPostLiked = false;
                setVoteDownDrawable();
            }
        } else if (mSubmission.data(Constants.LIKES) == null) {
            mPostDetailViewModel.getSubmissionId(mSubmission.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MaybeObserver<SubmissionEntry>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(SubmissionEntry submissionEntry) {
                            if (submissionEntry != null) {
                                if (submissionEntry.isPostLiked()) {
                                    setVoteUpDrawable();
                                } else {
                                    setVoteDownDrawable();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mPostDetailVoteUpImg.setImageResource(R.drawable.trending_up);
            mPostDetailVoteDownImg.setImageResource(R.drawable.trending_down);
        }

        mPostDetailVoteUpImg.setOnClickListener(view -> {
            mIsPostLiked = true;
            mPostDetailViewModel.votePost(mSubmission, VoteDirection.UPVOTE);
            mPostDetailViewModel.saveSubmissionId(mSubmission.getId(), mIsPostLiked);
            setVoteUpDrawable();
        });

        mPostDetailVoteDownImg.setOnClickListener(view -> {
            mIsPostLiked = false;
            mPostDetailViewModel.votePost(mSubmission, VoteDirection.DOWNVOTE);
            mPostDetailViewModel.saveSubmissionId(mSubmission.getId(), mIsPostLiked);
            setVoteDownDrawable();
        });


        /************************
         * Comments
         ***********************/
        mPostDetailCommentCount.setText(String.valueOf(mSubmission.getCommentCount()));

        mPostDetailCommentImg.setOnClickListener(view -> CommentsActivity.start(this, mSubmissionJson));
    }

    private void setVoteUpDrawable() {
        mPostDetailVoteUpImg.setImageResource(R.drawable.trending_up_green);
        mPostDetailVoteDownImg.setVisibility(View.INVISIBLE);
    }

    private void setVoteDownDrawable() {
        mPostDetailVoteDownImg.setImageResource(R.drawable.trending_down_red);
        mPostDetailVoteUpImg.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.post_detail_share)
    public void sharePost() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(getString(R.string.reddit_url) + mSubmission.getPermalink())
                .getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        startActivity(shareIntent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
