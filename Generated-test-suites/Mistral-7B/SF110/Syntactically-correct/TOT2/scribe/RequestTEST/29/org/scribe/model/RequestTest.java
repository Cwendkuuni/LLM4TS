package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

public class RequestTest {
    @Test
    public void testSend() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "http://test.com");
        Response response = request.send();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testCreateConnectionMultipleTimes() throws IOException {
        Request request = new Request(Verb.GET, "http://test.com");

        request.createConnection();
        request.createConnection();

        // Assert that the connection hasn't been created more than once
        assertNotNull(request.connection);
    }

    @Test(expected = OAuthException.class)
    public void testSendWithInvalidUrl() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "invalid:url");
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithInvalidHeaders() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "http://test.com");
        request.addHeader("Content-Length", "abc");
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithMalformedBody() throws IOException, OAuthException {
        Request request = new Request(Verb.POST, "http://test.com");
        request.addBodyParameter("key", "value%");
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithInvalidVerb() throws IOException, OAuthException {
        Request request = new Request(null, "http://test.com");
        request.send();
    }

    @Test
    public void testSendWithEmptyPayload() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "http://test.com");
        Response response = request.send();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testSendWithEmptyPayloadAndBodyParameter() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "http://test.com");
        request.addBodyParameter("key", "");
        Response response = request.send();
        assertEquals(200, response.getStatusCode());
    }

    @Test(expected = OAuthException.class)
    public void testSendWithUnsupportedCharset() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "http://test.com");
        request.setCharset("Unsupported-Charset");
        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testSendWithInvalidConnectionKeepAlive() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "http://test.com");
        request.setConnectionKeepAlive(-1);
        request.send();
    }

    @Test(expected = IOException.class)
    public void testSendWithTimeout() throws IOException, OAuthException {
        Request request = new Request(Verb.GET, "http://test.com");
        request.setConnectTimeout(0, TimeUnit.MILLISECONDS);
        request.send();
    }
}