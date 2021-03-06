package tr.edu.boun.cmpe.sculture.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryViewWithCommentAdapter;
import tr.edu.boun.cmpe.sculture.models.response.CommentListResponse;
import tr.edu.boun.cmpe.sculture.models.response.CommentResponse;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.StoryResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_COMMENT_LIST;
import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_DELETE;
import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_GET;
import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_REPORT;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_IS_EDIT;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_STORY_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_STORY_ID;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_SEARCH;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_STORY_GET;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

/**
 * Story viewing screen
 * <pre></pre>
 * {@link tr.edu.boun.cmpe.sculture.Constants#BUNDLE_STORY_ID}: long, ID of the story
 */
public class StoryShowActivity extends AppCompatActivity {

    private static final int SIZE = 10;

    Bundle bundle;
    long story_id;

    private StoryViewWithCommentAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int PAGE = 1;
    private boolean is_loading_more = false;
    private boolean is_reach_end = false;
    MenuItem report_menu;
    MenuItem edit_menu;
    MenuItem delete_menu;
    StoryShowActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_story_show);
        bundle = getIntent().getExtras();
        if (bundle != null)
            story_id = bundle.getLong(BUNDLE_STORY_ID);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.story_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StoryViewWithCommentAdapter(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                load_comment();
            }
        });
        getStory(story_id);
    }

    private void load_comment() {
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleIndex = mLayoutManager.findLastVisibleItemPosition();


        boolean loadMore = lastVisibleIndex == totalItemCount - 1;


        if ((loadMore && !is_loading_more && !is_reach_end) || PAGE == 1) {
            is_loading_more = true;
            final JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_STORY_ID, story_id);
                requestBody.put(FIELD_SIZE, SIZE);
                requestBody.put(FIELD_PAGE, PAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PAGE++;

            addRequest(API_COMMENT_LIST, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    CommentListResponse commentListResponse = new CommentListResponse(response);
                    for (CommentResponse comment : commentListResponse.result)
                        mAdapter.addComment(comment);
                    if (commentListResponse.result.size() == 0)
                        is_reach_end = true;
                    is_loading_more = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorResponse errorResponse = new ErrorResponse(error);
                    Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    Log.e("COMMENT LIST", errorResponse.toString());
                }
            }, REQUEST_TAG_SEARCH);
        }
    }


    public void getStory(long id) {

        final JSONObject requestObject = new JSONObject();
        try {
            requestObject.put(FIELD_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addRequest(API_STORY_GET, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        StoryResponse story = new StoryResponse(response);
                        mAdapter.addStory(story);

                        edit_menu.setVisible(story.owner.id == BaseApplication.baseApplication.getUSER_ID());
                        report_menu.setVisible(story.owner.id != BaseApplication.baseApplication.getUSER_ID());
                        delete_menu.setVisible(story.owner.id == BaseApplication.baseApplication.getUSER_ID());

                        load_comment();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = new ErrorResponse(error);
                        Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                        Log.e("STORY GET", errorResponse.toString());
                    }
                },
                REQUEST_TAG_STORY_GET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_story_show, menu);

        report_menu = menu.findItem(R.id.action_report);
        edit_menu = menu.findItem(R.id.action_edit);
        delete_menu = menu.findItem(R.id.action_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_report:
                //TODO This request will probably be changed.
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("story_id", story_id);
                    requestBody.put("user_id", BaseApplication.baseApplication.getUSER_ID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addRequest(API_STORY_REPORT, requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.report_success, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = new ErrorResponse(error);
                        Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                        Log.e("STORY REPORT", errorResponse.toString());
                    }
                }, null);

                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, StoryCreateActivity.class);
                intent.putExtra(BUNDLE_STORY_ID, story_id);
                intent.putExtra(BUNDLE_IS_EDIT, true);
                startActivity(intent);
                break;
            case R.id.action_delete:
                action_delete_clicked();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void action_delete_clicked() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_delete)
                .setMessage(R.string.delete_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_story();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void delete_story() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", story_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addRequest(API_STORY_DELETE, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(mActivity, R.string.deleted, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = new ErrorResponse(error);
                Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                Log.e("STORY DELETE", errorResponse.toString());
            }
        }, null);
    }
}
