package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.tasks.Notifier;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationAction extends Notifier implements Action {

    private String name;

    private String webhookUrl;
    private String channel;
    private String message;

    public NotificationAction(String webhookUrl, String channel, String message) throws IOException {
        this.webhookUrl = webhookUrl;
        this.channel = channel;
        this.message = message;
        sendNotification(webhookUrl,channel,message);
    }
    public String getName() {
        return name;
    }
    @Override
    public String getIconFileName() {
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        return "Greeting";
    }

    @Override
    public String getUrlName() {
        return "greeting";
    }

    private void sendNotification(String webhookUrl, String channel, String message) throws IOException {
        URL url = new URL(webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        String payload = "{\"channel\": \"" + channel + "\", \"text\": \"" + message + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to send notification: HTTP error code " + responseCode);
        }
    }
}
