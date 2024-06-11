package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionEmptyConstructor() {
        TwitterException exception = new TwitterException("Message");
        assertEquals("Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("", exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionExceptionConstructor() {
        Exception cause = new IOException("Network error");
        TwitterException exception = new TwitterException("Message", cause);
        assertEquals("Message: Network error", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("", exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionStringHttpResponseConstructor() {
        String causeMsg = "Server error";
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 500;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("{\"message\":\"" + causeMsg + "\"}".getBytes());
            }
        };
        TwitterException exception = new TwitterException("Message", response);
        assertEquals("Message: Server error", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("500:Server error", exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testTwitterExceptionStringExceptionConstructor() {
        String causeMsg = "Server error";
        Exception cause = new IOException("Network error");
        TwitterException exception = new TwitterException("Message", cause, 500);
        assertEquals("Message: Server error: Network error", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertNull(exception.getRetryAfter());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("500:Server error", exception.getExceptionCode());
        assertFalse(exception.isNested());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Message", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        });
        assertEquals(420, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        Map<String, Integer> headerMap = new HashMap<>();
        headerMap.put("X-Rate-Limit-Limit", 100);
        headerMap.put("X-Rate-Limit-Remaining", 50);
        HttpResponse response = new HttpResponse() {
            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }

            @Override
            public Map<String, List<String>> getResponseHeaders() {
                return Map.of("X-Rate-Limit-Headers", List.of(new StringBuffer().append(new GsonBuilder().create().toJson(headerMap)).toString()));
            }
        };
        TwitterException exception = new TwitterException("Message", response);
        assertEquals(100, exception.getRateLimitStatus().getLimit());
        assertEquals(50, exception.getRateLimitStatus().getRemaining());
        assertEquals(100, exception.getRateLimitStatus().getLimitPerHour());
        assertEquals(50, exception.getRateLimitStatus().getRemainingPerHour());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("X-Rate-Limit-Resource", "search");
        headerMap.put("X-Rate-Limit-Limit-Normal", 10);
        headerMap.put("X-Rate-Limit-Remaining-Normal", 5);
        headerMap.put("X-Rate-Limit-Limit-1h", 20);
        headerMap.put("X-Rate-Limit-Remaining-1h", 10);
        HttpResponse response = new HttpResponse() {
            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }

            @Override
            public Map<String, List<String>> getResponseHeaders() {
                return Map.of("X-Rate-Limit-Headers", List.of(new StringBuffer().append(new GsonBuilder().create().toJson(headerMap)).toString()));
            }
        };
        TwitterException exception = new TwitterException("Message", response);
        assertEquals("search", exception.getFeatureSpecificRateLimitStatus().getResource());
        assertEquals(10, exception.getFeatureSpecificRateLimitStatus().getLimitNormal());
        assertEquals(5, exception.getFeatureSpecificRateLimitStatus().getRemainingNormal());
        assertEquals(20, exception.getFeatureSpecificRateLimitStatus().getLimitPerHour());
        assertEquals(10, exception.getFeatureSpecificRateLimitStatus().getRemainingPerHour());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Message", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "120";
                }
                return null;
            }
        });
        assertEquals(120, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Network error");
        TwitterException exception = new TwitterException("Message", cause);
        assertTrue(exception.isCausedByNetworkIssue());

        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        };
        exception = new TwitterException("Message", response);
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        };
        TwitterException exception = new TwitterException("Message", response);
        assertTrue(exception.exceededRateLimitation());

        response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }
        };
        exception = new TwitterException("Message", response);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        };
        TwitterException exception = new TwitterException("Message", response);
        assertTrue(exception.resourceNotFound());

        response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }
        };
        exception = new TwitterException("Message", response);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Message1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "120";
                }
                return null;
            }
        });

        TwitterException exception2 = new TwitterException("Message2", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "120";
                }
                return null;
            }
        });

        assertTrue(exception1.equals(exception2));

        TwitterException exception3 = new TwitterException("Message1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "240";
                }
                return null;
            }
        });

        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Message1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "120";
                }
                return null;
            }
        });

        TwitterException exception2 = new TwitterException("Message2", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "120";
                }
                return null;
            }
        });

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();

        assertEquals(hashCode1, hashCode2);

        TwitterException exception3 = new TwitterException("Message1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "240";
                }
                return null;
            }
        });

        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if ("Retry-After".equalsIgnoreCase(name)) {
                    return "120";
                }
                return null;
            }
        };
        TwitterException exception = new TwitterException("Message", response);
        String expected = "TwitterException{statusCode=420, retryAfter=120, rateLimitStatus=null, version=Unknown, exceptionCode=}";
        assertEquals(expected, exception.toString());
    }
}