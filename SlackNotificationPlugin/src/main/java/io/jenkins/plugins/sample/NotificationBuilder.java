package io.jenkins.plugins.sample;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;

public class NotificationBuilder extends Builder implements SimpleBuildStep {

    private final String webhookUrl;
    private final String channel;
    private final String message;

    @DataBoundConstructor
    public NotificationBuilder(String webhookUrl, String channel, String message) {
        this.webhookUrl = webhookUrl;
        this.channel = channel;
        this.message = message;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener) {
        try {
            run.addAction(new NotificationAction(webhookUrl, channel, message));
            listener.getLogger().println("Notification sent to " + channel);
        } catch (Exception e) {
            listener.getLogger().println("Failed to send notification: " + e.getMessage());
        }
    }


    @Symbol("simpleSlackNotifier")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Send Notification";
        }


    }
}
