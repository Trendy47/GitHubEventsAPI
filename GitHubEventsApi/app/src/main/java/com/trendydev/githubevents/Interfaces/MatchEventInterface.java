package com.trendydev.githubevents.Interfaces;

import com.android.volley.VolleyError;
import com.trendydev.githubevents.Model.GitHubEvent;

/**
 * Created by Trendy on 9/14/2017.
 */

public interface MatchEventInterface {
    void addEventToAdapter(GitHubEvent event);
    void onCompletion();
    void onVolleyError(VolleyError error);
    void updateDialogText(String message);
    void dismissDialog();
}
