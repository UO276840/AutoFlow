package io.jenkins.plugins.sample;

import hudson.model.Run;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationTest {

    @Mock
    private Run<?, ?> run;

    @Mock
    private URL mockUrl;

    @Mock
    private HttpURLConnection mockConnection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNotificationBuilder_Getters() {
        NotificationBuilder builder = new NotificationBuilder("https://hooks.slack.com/services/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", "#general", "Test Message");
        assertEquals("https://hooks.slack.com/services/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", builder.getWebhookUrl());
        assertEquals("#general", builder.getChannel());
        assertEquals("Test Message", builder.getMessage());
    }

    @Test
    public void testSendNotification_Success() throws IOException {
        lenient().when(mockConnection.getResponseCode()).thenReturn(200);

        try {
            NotificationAction action = new NotificationAction("https://hooks.slack.com/services/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", "#general", "Test Message");
            assertNotNull(action);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }


}
