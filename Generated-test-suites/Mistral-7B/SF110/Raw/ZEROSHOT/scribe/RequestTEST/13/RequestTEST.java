package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class RequestTest {

	@Test
	public void testRequestInstance() {
		Request request = new Request(Verb.GET, "http://example.com");
		assertNotNull(request);
		assertEquals(Verb.GET, request.getVerb());
		assertEquals("http://example.com", request.getUrl());
		assertTrue(request.getHeaders().isEmpty());
		assertTrue(request.getBodyParams().isEmpty());
		assertTrue(request.getQuerystringParams().isEmpty());
		assertNull(request.getPayload());
		assertNull(request.getBytePayload());
	}

	@Test
	public void testAddQuerystringParameter() {
		Request request = new Request(Verb.GET, "http://example.com");
		request.addQuerystringParameter("key", "value");
		Map<String, String> params = request.getQueryStringParams();
		assertEquals("value", params.get("key"));
		assertEquals(1, params.size());
	}

	@Test
	public void testAddBodyParameter() {
		Request request = new Request(Verb.POST, "http://example.com");
		request.addBodyParameter("key", "value");
		Map<String, String> bodyParams = request.getBodyParams();
		assertEquals("value", bodyParams.get("key"));
		assertEquals(1, bodyParams.size());
	}

	@Test
	public void testAddPayload() {
		Request request = new Request(Verb.POST, "http://example.com");
		request.addPayload("payload");
		assertEquals("payload", request.getPayload());
		assertNull(request.getBytePayload());
	}

	@Test
	public void testAddPayloadByteArray() {
		Request request = new Request(Verb.POST, "http://example.com");
		byte[] payload = "payload".getBytes(StandardCharsets.UTF_8);
		request.addPayload(payload);
		assertEquals(payload, request.getBytePayload());
		assertNull(request.getPayload());
	}

	@Test
	public void testAddHeader() {
		Request request = new Request(Verb.GET, "http://example.com");
		request.addHeader("Key", "Value");
		Map<String, String> headers = request.getHeaders();
		assertEquals("Value", headers.get("Key"));
		assertEquals(1, headers.size());
	}

	@Test
	public void testSendGetRequest() throws IOException, OAuthException {
		Request request = new Request(Verb.GET, "http://example.com");
		Response response = request.send();
		assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
		assertTrue(response.getContentLength() > 0);
	}

	@Test(expected = OAuthException.class)
	public void testSendGetRequestWithMalformedUrl() throws IOException, OAuthException {
		Request request = new Request(Verb.GET, "http://example.com:/invalid");
		request.send();
	}

	@Test
	public void testSendPostRequestWithEmptyBody() throws IOException, OAuthException {
		Request request = new Request(Verb.POST, "http://example.com");
		Response response = request.send();
		assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testSendPostRequestWithTextBody() throws IOException, OAuthException {
		Request request = new Request(Verb.POST, "http://example.com");
		request.addPayload("payload");
		Response response = request.send();
		assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
		assertTrue(response.getContentLength() > 0);
		assertEquals("payload", new String(response.getContent(), StandardCharsets.UTF_8));
	}

	@Test
	public void testSendPostRequestWithByteBody() throws IOException, OAuthException {
		String payload = "payload";
		Request request = new Request(Verb.POST, "http://example.com");
		request.addPayload(payload.getBytes(StandardCharsets.UTF_8));
		Response response = request.send();
		assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
		assertTrue(response.getContentLength() > 0);
		assertEquals(payload, new String(response.getContent(), StandardCharsets.UTF_8));
	}

	@Test
	public void testSetConnectTimeout() throws IOException, InterruptedException, OAuthException {
		Request request = new Request(Verb.GET, "http://example.com");
		request.setConnectTimeout(1, TimeUnit.SECONDS);
		try {
			Thread.sleep(2000);
			request.send();
			fail("Exception expected");
		} catch (OAuthException e) {
			assertEquals("Timeout waiting for connection", e.getMessage());
		}
	}

	@Test
	public void testSetReadTimeout() throws IOException, InterruptedException, OAuthException {
		Request request = new Request(Verb.GET, "http://example.com");
		request.setReadTimeout(1, TimeUnit.SECONDS);
		URL url = new URL("http://example.com");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		try {
			connection.disconnect();
			request.setConnection(connection);
			Response response = request.send();
			fail("Exception expected");
		} catch (IOException e) {
			assertEquals("Timeout reading the response", e.getMessage());
		}
	}

	@Test
	public void testSetCharset() {
		Request request = new Request(Verb.GET, "http://example.com");
		request.setCharset("UTF-8");
		assertEquals("UTF-8", request.getCharset());
	}

	@Test
	public void testSetConnectionKeepAlive() {
		Request request = new Request(Verb.GET, "http://example.com");
		request.setConnectionKeepAlive(true);
		assertTrue(request.isConnectionKeepAlive());
	}

}