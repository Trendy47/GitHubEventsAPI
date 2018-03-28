package com.trendydev.githubevents.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Trendy on 9/13/2017.
 */

public class GitHubEvent {

    private String eventType;
    private String timestamp;
    private HashMap<String, String> actorInfoDict;

    public GitHubEvent(JSONObject eventObject) {
        try {
            String eventType = eventObject.getString("type");
            String timestamp = eventObject.getString("created_at");

            setEventType(eventType);
            setTimestamp(timestamp);

            JSONObject actorObject = eventObject.getJSONObject("actor");

            String id = actorObject.getString("id");
            String login = actorObject.getString("login");
            String displayLogin = actorObject.getString("display_login");
            String gravatarId = actorObject.getString("gravatar_id");
            String url = actorObject.getString("url");
            String avatarUrl = actorObject.getString("avatar_url");

            actorInfoDict = new HashMap<>();
            actorInfoDict.put("id", id);
            actorInfoDict.put("login", login);
            actorInfoDict.put("display_login", displayLogin);
            actorInfoDict.put("gravatar_id", gravatarId);
            actorInfoDict.put("url", url);
            actorInfoDict.put("avatar_url", avatarUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public HashMap<String, String> getActorInfoDict() {
        return this.actorInfoDict;
    }
}
