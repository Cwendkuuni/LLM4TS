package org.scribe.model;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {

    @Test
    public void testResponseInstance() {
        try {
            URL url = new URL("http://example.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
            assertNull(response.getBody());
            assertNotNull(response.getHeaders());
            assertNull(response.getStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyContents() {
        String body = "Hello World";
        try {
            URL url = new URL("http://example.com/");
            HttpURLConnection connection = mock(HttpURLConnection.class);
            when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
            Response response = new Response(connection);
            assertEquals(body, response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseHeaders() {
        Map<String, List<String>> headerFields = new HashMap<String, List<String>>() {{
            put("Content-Type", Collections.singletonList("application/json"));
            put("Cache-Control", Collections.singletonList("max-age=0"));
        }};
        try {
            URL url = new URL("http://example.com/");
            HttpURLConnection connection = mock(HttpURLConnection.class);
            when(connection.getHeaderFields()).thenReturn(headerFields);
            Response response = new Response(connection);
            Map<String, String> headers = response.getHeaders();
            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("max-age=0", headers.get("Cache-Control"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(new Response(new HttpURLConnection(new URL("http://example.com/")) {
            {
                this.setDoOutput(true);
                this.setRequestMethod("GET");
                this.setResponseCode(200);
            }
        }).wasSuccessful());

        assertFalse(new Response(new HttpURLConnection(new URL("http://example.com/")) {
            {
                this.setDoOutput(true);
                this.setRequestMethod("GET");
                this.setResponseCode(404);
            }
        }).wasSuccessful());

        assertFalse(new Response(new HttpURLConnection(new URL("http://example.com/")) {
            {
                this.setDoOutput(true);
                this.setRequestMethod("GET");
                this.disconnect();
            }
        }).wasSuccessful());
    }

    @Test
    public void testGetBody() {
        try {
            URL url = new URL("http://example.com/");
            HttpURLConnection connection = new HttpURLConnection(url) {
                {
                    this.setDoOutput(true);
                    this.setRequestMethod("GET");
                    this.setResponseCode(200);
                    this.setRequestProperty("Accept-Charset", "UTF-8");
                }
            };
            connection.connect();
            String expectedBody = "{\"name\": \"John\", \"age\": 30}";
            String body = new Response(connection).getBody();
            assertEquals(expectedBody, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetStream() {
        try {
            URL url = new URL("http://example.com/");
            HttpURLConnection connection = new HttpURLConnection(url) {
                {
                    this.setDoOutput(true);
                    this.setRequestMethod("GET");
                    this.setResponseCode(200);
                    this.setRequestProperty("Accept-Charset", "UTF-8");
                }
            };
            connection.connect();
            InputStream inputStream = new Response(connection).getStream();
            byte[] expectedData = "{\"name\": \"John\", \"age\": 30}".getBytes("UTF-8");
            byte[] data = StreamUtils.toByteArray(inputStream);
            assertArrayEquals(expectedData, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCode() {
        assertEquals(HttpURLConnection.HTTP_OK, new Response(new HttpURLConnection(new URL("http://example.com/")) {
            {
                this.setDoOutput(true);
                this.setRequestMethod("GET");
                this.setResponseCode(200);
            }
        }).getCode());

        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, new Response(new HttpURLConnection(new URL("http://example.com/")) {
            {
                this.setDoOutput(true);
                this.setRequestMethod("GET");
                this.setResponseCode(404);
            }
        }).getCode());
    }

    @Test
    public void testGetHeaders() {
        try {
            URL url = new URL("http://example.com/");
            HttpURLConnection connection = new HttpURLConnection(url) {
                {
                    this.setDoOutput(true);
                    this.setRequestMethod("GET");
                    this.setResponseCode(200);
                    this.setRequestProperty("Content-Type", "application/json");
                    this.setRequestProperty("Cache-Control", "max-age=0");
                    Map<String, List<String>> headers = new HashMap<String, List<String>>() {{
                        put("Content-Type", Collections.singletonList("application/json"));
                        put("Cache-Control", Collections.singletonList("max-age=0"));
                    }};
                    this.setHeaderFields(headers);
                }
            };
            connection.connect();
            Map<String, String> headers = new Response(connection).getHeaders();
            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("max-age=0", headers.get("Cache-Control"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetHeader() {
        try {
            URL url = new URL("http://example.com/");
            HttpURLConnection connection = new HttpURLConnection(url) {
                {
                    this.setDoOutput(true);
                    this.setRequestMethod("GET");
                    this.setResponseCode(200);
                    this.setRequestProperty("Content-Type", "application/json");
                    Map<String, List<String>> headers = new HashMap<String, List<String>>() {{
                        put("Content-Type", Collections.singletonList("application/json"));
                        put("Cache-Control", Collections.singletonList("max-age=0"));
                        put("My-Header", Collections.singletonList("Test Value"));
                    }};
                    this.setHeaderFields(headers);
                }
            };
            connection.connect();
            assertEquals("Test Value", new Response(connection).getHeader("My-Header"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}