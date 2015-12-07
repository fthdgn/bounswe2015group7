package tr.edu.boun.cmpe.sculture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;

import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.models.response.BaseStoryResponse;
import tr.edu.boun.cmpe.sculture.models.response.SearchResponse;
import tr.edu.boun.cmpe.sculture.models.response.TagResponse;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_GET;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_STORIES;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_TAG_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class TagActivity extends AppCompatActivity {
    private TextView tagTitle;
    private TextView tagDescription;
    private TextView editTagDesc;
    private TextView save;
    private TextView writer;
    private TextView update_time;
    private Button createDescription;
    private RecyclerView story_list_recycler;

    LinearLayoutManager mLayoutManager;
    StoryListViewAdapter mStoryListViewAdapter;

    private boolean is_looding_more = false;
    private boolean is_reach_end = false;

    private boolean is_on_edit = false;
    private String previous = "";

    private Activity mActivity;

    Bundle bundle;
    String tag_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_description);
        bundle = getIntent().getExtras();
        if(bundle != null)
            tag_title = bundle.getString(BUNDLE_TAG_TITLE);

        Log.i("HERE", "" + tag_title);
        tagTitle = (TextView) findViewById(R.id.tag_title);
        tagDescription = (TextView) findViewById(R.id.tag_description);
        writer = (TextView) findViewById(R.id.tag_creation);
        update_time = (TextView) findViewById(R.id.tag_update);
        editTagDesc = (TextView) findViewById(R.id.edit_cancel_text);
        save = (TextView) findViewById(R.id.save_Text);
        createDescription = (Button) findViewById(R.id.create_description);

        story_list_recycler = (RecyclerView) findViewById(R.id.profile_story_list);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mStoryListViewAdapter = new StoryListViewAdapter(getParent());
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);

        getTag(tag_title);

        setRecyclerListeners();

        load_story();


        if(tagDescription.getText().toString() != "") {
            createDescription.setVisibility(View.GONE);
        } else {
            createDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), TagDescCreateActivity.class);
                    startActivity(i);
                }
            });
        }

        writer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open profile page

            }
        });

        editTagDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_on_edit) {
                    tagDescription.setEnabled(false);
                    editTagDesc.setText(mActivity.getString(R.string.edit));
                    tagDescription.setText(previous);
                    save.setVisibility(View.GONE);
                } else {
                    previous = tagDescription.getText().toString();
                    tagDescription.setEnabled(true);
                    editTagDesc.setText(mActivity.getString(R.string.cancel));
                    save.setVisibility(View.VISIBLE);
                }
                is_on_edit = !is_on_edit;
            }
        });

    }

    private void setRecyclerListeners() {
        story_list_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                load_story();
            }
        });
    }

    private void load_story() {
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleIndex = mLayoutManager.findLastVisibleItemPosition();

        boolean loadMore = lastVisibleIndex == totalItemCount - 1;

        if (is_reach_end)
            Log.i("HERE", "HERE");
        if ((loadMore && !is_looding_more && !is_reach_end)) {
            is_looding_more = true;
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_TITLE, baseApplication.getUSER_ID());
                requestBody.put(FIELD_SIZE, 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addRequest(API_USER_STORIES, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SearchResponse searchResponse = new SearchResponse(response);

                    for(BaseStoryResponse story : searchResponse.result)
                        mStoryListViewAdapter.addElement(story);

                    if(searchResponse.result.size() == 0)
                        is_reach_end = true;

                    is_looding_more = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, null);
        }
    }

    private void getTag(String title) {
        final JSONObject requestObject = new JSONObject();
        try {
            requestObject.put(FIELD_TITLE, title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addRequest(API_TAG_GET, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TagResponse tag = new TagResponse(response);
                        tagTitle.setText(tag.tag_title);
                        tagDescription.setText(tag.description);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public  void onErrorResponse(VolleyError error) {

                    }
                }, null);
    }
}
