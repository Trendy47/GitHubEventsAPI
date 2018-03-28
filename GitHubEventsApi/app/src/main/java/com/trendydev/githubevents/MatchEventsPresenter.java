package com.trendydev.githubevents;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.trendydev.githubevents.Interfaces.MatchEventInterface;
import com.trendydev.githubevents.Model.GitHubEvent;
import com.trendydev.githubevents.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Trendy on 9/14/2017.
 */

public class MatchEventsPresenter {

    private Context context;
    private MatchEventInterface delegate;

    private String githubEventsUrl;
    private String eventTypeString;

    public MatchEventsPresenter(Context context, String urlString, String eventString) {
        this.context = context;
        this.githubEventsUrl = urlString;
        this.eventTypeString = eventString;
    }

    public void onTakeView(MatchEventInterface view) {
        if (view != null) {
            this.delegate = view;

            getEventsList();
        }
    }

    private void getEventsList() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, githubEventsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseEventsJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                delegate.onVolleyError(error);
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void parseEventsJson(JSONArray jsonString) {
        // Parsing may take a while so I'm going to have it in an async task
        // to avoid blocking the UI thread

        delegate.updateDialogText("Parsing JSON Response...");

        try {
            for (int i = 0; i < jsonString.length(); i++) {
                JSONObject jsonObject = jsonString.getJSONObject(i);
                String type = jsonObject.getString("type");

                if (type.equalsIgnoreCase(eventTypeString)) {
                    delegate.addEventToAdapter(new GitHubEvent(jsonObject));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        delegate.onCompletion();
    }
}
