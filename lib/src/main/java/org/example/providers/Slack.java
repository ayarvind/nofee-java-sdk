package org.example.providers;

import org.example.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Slack implements Provider {
    private final String endpoints;
    private int priority = 1;
    private String scheduleTime;
    private String text;
    private String channel;
    private final Nofee nofee;

    // Constructor to initialize Nofee and endpoint
    public Slack(Nofee nofee) {
        Property property = new Property();
        this.nofee = nofee;
        this.endpoints = property.get("notification.endpoints");
    }

    // Set priority with validation
    public Slack setPriority(int priority) {
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("Priority must be between 1 and 10");
        }
        this.priority = priority;
        return this;
    }

    // Set schedule time using Date
    public Slack schedule(long date) {
        this.scheduleTime = Long.toString(date);
        return this;
    }

    // Set the message text
    public Slack setText(String text) {
        this.text = text;
        return this;
    }

    // Set the Slack channel
    public Slack setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    // Prepare the payload to be sent
    private JSONObject preparePayload() {
        JSONObject payload = new JSONObject();
        if(this.text == null) throw new IllegalArgumentException("Text is required");
        if(this.channel == null) throw new IllegalArgumentException("Channel is required");
        if(nofee.getAuthToken() == null) throw new IllegalArgumentException("Auth token is required");
        if(nofee.getProjectID() == null) throw new IllegalArgumentException("Project ID is required");
        if(this.priority < 1 || this.priority > 10) throw new IllegalArgumentException("Priority must be between 1 and 10");
        if(this.scheduleTime != null && this.scheduleTime.isEmpty()) throw new IllegalArgumentException("Schedule time is required");
        try {
            payload.put("projectId", nofee.getProjectID());
            payload.put("provider", "slack");
            JSONObject messageData = new JSONObject();
            messageData.put("text", this.text);
            messageData.put("channel", this.channel);
            payload.put("payload", messageData);
            if (this.scheduleTime != null) {
                payload.put("scheduleTime", this.scheduleTime);
            }
        } catch (JSONException e) {
            System.err.println("Error creating JSON payload: " + e.getMessage());
        }
        return payload;
    }

    // Send the message to the Slack endpoint
    @Override
    public JSONObject send() {
        JSONObject payload = preparePayload();
        if(payload == null) return null;
        System.out.println("Sending payload: " + payload);
        ApiRequest request = new ApiRequest(this.endpoints, nofee.getAuthToken(), payload);
        return request.post();
    }


}
