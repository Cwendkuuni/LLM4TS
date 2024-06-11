package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

private Request request;

@Before
public void setUp() {
request = new Request(Verb.GET, "http://example.com");
}

@Test
public void testRequestWithGetVerbAndEmptyParameters() throws IOException {
Response response = request.send();
assertEquals(200, response.getStatusCode());
}

@Test(expected = OAuthException.class)
public void testRequestWithInvalidURL() {
request.setUrl("http://example.com:");
request.send();
}

@Test
public void testRequestWithGetVerbAndQuerystringParameters() throws IOException {
Map<String, String> querystringParams = new HashMap<>();
querystringParams.put("param1", "value1");
querystringParams.put("param2", "value2");
request.addQuerystringParameters(querystringParams);
Response response = request.send();
assertEquals(200, response.getStatusCode());
Map<String, String> actualQuerystringParams = request.getQueryStringParams();
assertEquals(querystringParams, actualQuerystringParams);
}

@Test
public void testRequestWithPostVerbAndBodyParameters() throws IOException {
Map<String, String> bodyParams = new HashMap<>();
bodyParams.put("param1", "value1");
bodyParams.put("param2", "value2");
request.setVerb(Verb.POST);
request.addBodyParameters(bodyParams);
request.addPayload("payload");
Response response = request.send();
assertEquals(200, response.getStatusCode());
}

@Test
public void testRequestWithPutVerbAndBodyParameters() throws IOException {
Map<String, String> bodyParams = new HashMap<>();
bodyParams.put("param1", "value1");
bodyParams.put("param2", "value2");
request.setVerb(Verb.PUT);
request.addBodyParameters(bodyParams);
request.addPayload("payload");
byte[] bytePayload = "payload".getBytes(StandardCharsets.UTF_8);
request.addPayload(bytePayload);
Response response = request.send();
assertEquals(200, response.getStatusCode());
}

@Test
public void testRequestWithHeaders() throws IOException {
Map<String, String> headers = new HashMap<>();
headers.put("Header1", "Value1");
headers.put("Header2", "Value2");
request.addHeaders(headers);
Response response = request.send();
assertEquals(200, response.getStatusCode());
Map<String, String> actualHeaders = request.getHeaders();
assertEquals(headers, actualHeaders);
}

@Test
public void testRequestWithConnectionTimeout() throws IOException {
request.setConnectTimeout(5, TimeUnit.SECONDS);
URL url = new URL("http://example.com");
HttpURLConnection connection = (HttpURLConnection)url.openConnection();
try {
request.send();
fail();
} catch (IOException e) {
// Expected
}
assertEquals(5000, connection.getConnectTimeout());
}

@Test
public void testRequestWithReadTimeout() throws IOException {
request.setReadTimeout(5, TimeUnit.SECONDS);
URL url = new URL("http://example.com");
HttpURLConnection connection = (HttpURLConnection)url.openConnection();
InputStream inputStream = connection.getInputStream();
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
byte[] buffer = new byte[1024];
int bytesRead;
while ((bytesRead = inputStream.read(buffer)) != -1) {
outputStream.write(buffer, 0, bytesRead);
}
outputStream.flush();
byte[] expectedByteArray = outputStream.toByteArray();
request.send();
byte[] actualByteArray = Arrays.copyOfRange(request.getByteBodyContents(), 0, request.getByteBodyContents().length);
Assert.assertArrayEquals(expectedByteArray, actualByteArray);
assertEquals(5000, connection.getReadTimeout());
}
}