package org.example;

import org.example.providers.Slack;
import org.json.JSONObject;

import java.util.Date;

public class Library {
    public static void main(String[] args){
        Nofee nofee = new Nofee("");
        nofee.useProject("");
        Slack slack = new Slack(nofee);
        JSONObject response = slack.setChannel("#nofee")
                                    .setText("Hello how is this")
                                    .setPriority(Priority.MEDIUM)
                                    .schedule(new Date().getTime())
                                    .send();

        System.out.println(response);


    }
}
