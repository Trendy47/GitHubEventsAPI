package com.trendydev.githubevents;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.trendydev.githubevents.Interfaces.MatchEventInterface;
import com.trendydev.githubevents.Model.GitHubEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Trendy on 9/13/2017.
 */

public class MatchingEventsFragment extends Fragment implements MatchEventInterface {

    private Context context;
    private TextView noEventsTextView;
    private ProgressDialog progressDialog;

    private CustomAdapter customAdapter;
    private MatchEventsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.matching_list_fragment, null, false);
        ListView matchingListView = view.findViewById(R.id.matchingListView);
        noEventsTextView = view.findViewById(R.id.noEventsTextView);

        this.context = getActivity();

        customAdapter = new CustomAdapter(getActivity(), R.layout.matching_list_item);
        matchingListView.setAdapter(customAdapter);

        // create url string
        String ownerString = getArguments().getString("owner");
        String repoString = getArguments().getString("repo");
        String eventTypeString = getArguments().getString("event");

        String githubEventsUrl = String.format("https://api.github.com/repos/%s/%s/events", ownerString, repoString);

        presenter = new MatchEventsPresenter(context, githubEventsUrl, eventTypeString);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // show dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Finding Event Matches...");
        progressDialog.show();

        // download and parse events from GitHub
        presenter.onTakeView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (!getActivity().isChangingConfigurations()) {
            presenter = null;
        }
    }

    /**
     * Interface Functions
     */

    @Override
    public void addEventToAdapter(GitHubEvent gitHubEvent) {
        // gets called from background thread so don't update UI
        customAdapter.addEvent(gitHubEvent);
    }

    @Override
    public void onCompletion() {
        Toast.makeText(context, "Matches Found", Toast.LENGTH_LONG).show();
        customAdapter.notifyDataSetChanged();

        if (customAdapter.getCount() == 0) {
            noEventsTextView.setVisibility(View.VISIBLE);
        }

        dismissDialog();
    }

    @Override
    public void onVolleyError(VolleyError error) {
        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateDialogText(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        }
    }

    @Override
    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * end region
     */

    private class CustomAdapter extends ArrayAdapter<GitHubEvent> {

        private ArrayList<GitHubEvent> events;

        public CustomAdapter(Context context, int resourceId) {
            super(context, resourceId);
            events = new ArrayList<>();
        }

        public void addEvent(GitHubEvent event) {
            events.add(event);
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public GitHubEvent getItem(int position) {
            return events.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.matching_list_item, null);
            }

            GitHubEvent event = getItem(position);

            if (event != null) {
                TextView eventTypeText = view.findViewById(R.id.eventTypeText);
                TextView timestampText = view.findViewById(R.id.timestampText);

                // actor info text views
                TextView aiIDText = view.findViewById(R.id.ai_idText);
                TextView aiLoginText = view.findViewById(R.id.ai_loginText);
                TextView aiDisplayLoginText = view.findViewById(R.id.ai_displayLoginText);
                TextView aiGravatarIdText = view.findViewById(R.id.ai_gravatarIdText);
                TextView aiUrlText = view.findViewById(R.id.ai_urlText);
                TextView aiAvatarUrlText = view.findViewById(R.id.ai_avatarUrlText);

                String eventType = event.getEventType();
                String timestamp = event.getTimestamp();
                HashMap<String, String> actorInfoDict = event.getActorInfoDict();

                // set text views
                eventTypeText.setText(eventType);
                timestampText.setText(timestamp);

                aiIDText.setText(actorInfoDict.get("id"));
                aiLoginText.setText(actorInfoDict.get("login"));
                aiDisplayLoginText.setText(actorInfoDict.get("display_login"));
                aiGravatarIdText.setText(actorInfoDict.get("gravatar_id"));
                aiUrlText.setText(actorInfoDict.get("url"));
                aiAvatarUrlText.setText(actorInfoDict.get("avatar_url"));
            }
            return view;
        }
    }
}
