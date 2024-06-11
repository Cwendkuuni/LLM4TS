package org.junit.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(new MockURLConnection() {
            @Override
            public int getResponseCode() {
                return 200;
            }

            @Override
            public String getResponseMessage() {
                return "OK";
            }

            @Override
            public Map<String, List<String>> getHeaderFields() {
                return new HashMap<String, List<String>>();
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream("Test body".getBytes());
            }
        });

        String expectedBody = "Test body";
        String actualBody = response.getBody();

        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetBody_EmptyResponseBody() {
        Response response = new Response(new MockURLConnection() {
            @Override
            public int getResponseCode() {
                return 200;
            }

            @Override
            public String getResponseMessage() {
                return "OK";
            }

            @Override
            public Map<String, List<String>> getHeaderFields() {
                return new HashMap<String, List<String>>();
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(new byte[0]);
            }
        });

        String actualBody = response.getBody();

        assertEquals(Response.EMPTY, actualBody);
    }

    @Test
    public void testGetBody_ErrorStatusCode() {
        Response response = new Response(new MockURLConnection() {
            @Override
            public int getResponseCode() {
                return 404;
            }

            @Override
            public String getResponseMessage() {
                return "Not Found";
            }

            @Override
            public Map<String, List<String>> getHeaderFields() {
                return new HashMap<String, List<String>>();
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }
        });

        String actualBody = response.getBody();

        assertEquals(Response.EMPTY, actualBody);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBody_NullResponseObject() {
        Response response = null;

        String actualBody = response.getBody();
    }

    static class MockURLConnection extends HttpURLConnection {
        @Override
        public int getResponseCode() {
            return -1;
        }

        @Override
        public String getResponseMessage() {
            return "";
        }

        @Override
        public Map<String, List<String>> getHeaderFields() {
            return new HashMap<String, List<String>>();
        }

        @Override
        public InputStream getInputStream() {
            return null;
        }

        @Override
        public void disconnect() throws IOException {
            // Do nothing
        }
    }

}
//