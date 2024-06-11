package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test exception message");
        assertEquals("Test exception message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Test IOException message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String responseMessage = "Test response message";
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream(responseMessage.getBytes());
            }
        };
        TwitterException exception = new TwitterException("Test exception message", response);
        assertEquals("Test exception message" + "\n" + responseMessage, exception.getMessage());
        assertEquals(response.getStatusCode(), exception.getStatusCode());
        assertEquals(response, exception.getResponse());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }
        });
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        String jsonResponse = "{\"rate_limited\":true,\"remaining_hits\":1,\"reset_time\":12345678}";
        HttpResponse response = new HttpResponse() {
            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream(jsonResponse.getBytes());
            }
        };
        TwitterException exception = new TwitterException("Test exception message", response);
        RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
        assertTrue(rateLimitStatus.isRateLimited());
        assertEquals(1, rateLimitStatus.getRemainingHits());
        assertEquals(12345678, rateLimitStatus.getResetTime());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        String jsonResponse = "{\"rate_limited\":true,\"remaining_hits\":1,\"reset_time\":12345678,\"limit_reset\":\"2023-02-24T12:00:00Z\",\"methods\":{\"search\":{\"remaining_hits\":2,\"hitsPerRequest\":100,\"resetTime\":\"2023-02-24T13:00:00Z\"}}}";
        HttpResponse response = new HttpResponse() {
            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream(jsonResponse.getBytes());
            }
        };
        TwitterException exception = new TwitterException("Test exception message", response);
        RateLimitStatus featureSpecificRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
        assertTrue(featureSpecificRateLimitStatus != null);
        assertEquals(2, featureSpecificRateLimitStatus.getRemainingHits("search"));
        assertEquals(100, featureSpecificRateLimitStatus.getHitsPerRequest("search"));
        assertEquals("2023-02-24T13:00:00Z", featureSpecificRateLimitStatus.getResetTime("search"));
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test IOException message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        };
        TwitterException exception = new TwitterException("Test exception message", response);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse response = new HttpURLConnection(new URL("http://example.com/non-existent")) {
            @Override
            public int getResponseCode() {
                return 404;
            }
        }.asResponse();
        TwitterException exception = new TwitterException("Test exception message", response);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponse() {
            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("400: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.".getBytes());
            }
        });
        assertEquals("400:The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException parentException = new TwitterException("Test parent exception message");
        TwitterException childException = new TwitterException("Test child exception message");
        childException.setNested();
        parentException.initCause(childException);
        assertTrue(parentException.isNested());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        });
        TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        });
        assertTrue(exception1.equals(exception2));

        TwitterException exception3 = new TwitterException("Test exception message 3", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 401;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        });
        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception message 1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        });
        TwitterException exception2 = new TwitterException("Test exception message 2", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        });
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        assertEquals(hashCode1, hashCode2);

        TwitterException exception3 = new TwitterException("Test exception message 3", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 401;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        });
        int hashCode3 = exception3.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public InputStream getResponseBodyAsStream() throws IOException {
                return new ByteArrayInputStream("".getBytes());
            }
        };
        TwitterException exception = new TwitterException("Test exception message", response);
        String expectedString = "TwitterException{exceptionCode=[], statusCode=400, retryAfter=-1, rateLimitStatus=null, version=unknown}";
        assertEquals(expectedString, exception.toString());
    }
}