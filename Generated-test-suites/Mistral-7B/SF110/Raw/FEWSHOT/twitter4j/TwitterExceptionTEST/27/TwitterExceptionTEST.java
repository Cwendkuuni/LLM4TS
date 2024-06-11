package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpResponse;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test exception message");
        assertEquals("Test exception message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = new IOException("Test cause message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        String causeMessage = "Test cause message";
        HttpResponse httpResponse = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public String getResponseHeader(String name) {
                if (name.equals("Retry-After")) {
                    return "120";
                }
                return null;
            }
        };

        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        assertEquals("Test exception message" + System.lineSeparator() + causeMessage + ": 400: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.", exception.getMessage());
        assertEquals(Integer.valueOf(120), exception.getRetryAfter());
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        IOException cause = new IOException("Test cause message");
        TwitterException exception = new TwitterException("Test exception message", cause, 400);
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test cause message");
        TwitterException exception = new TwitterException("Test exception message", cause);
        assertTrue(exception.isCausedByNetworkIssue());

        Exception notANetworkIssue = new RuntimeException("Test exception message");
        TwitterException exception2 = new TwitterException("Test exception message", notANetworkIssue);
        assertFalse(exception2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse httpResponse = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        };
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        assertTrue(exception.exceededRateLimitation());

        HttpResponse httpResponse2 = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }

            @Override
            public String getResponseHeader(String name) {
                if (name.equals("Retry-After")) {
                    return "120";
                }
                return null;
            }
        };
        TwitterException exception2 = new TwitterException("Test exception message", httpResponse2);
        assertFalse(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        HttpResponse httpResponse = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        };
        TwitterException exception = new TwitterException("Test exception message", httpResponse);
        assertTrue(exception.resourceNotFound());

        HttpResponse httpResponse2 = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        };
        TwitterException exception2 = new TwitterException("Test exception message", httpResponse2);
        assertFalse(exception2.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("TwitterException: failed to get statuses/home_timeline");
        String exceptionCode = exception.getExceptionCode();
        String expectedExceptionCode = "TwitterException: failed to get statuses/home_timeline:63331017:1:400:The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
        assertEquals(expectedExceptionCode.substring(0, 8), exceptionCode.substring(0, 8), "The first 8 characters of getExceptionCode and expected exception code should match.");
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception message 1", new IOException("Test cause message 1"));
        TwitterException exception2 = new TwitterException("Test exception message 2", new IOException("Test cause message 2"));
        TwitterException exception3 = new TwitterException("Test exception message 1", new IOException("Test cause message 1"));

        assertNotEquals(exception1, exception2);
        assertEquals(exception1, exception3);

        HttpResponse httpResponse1 = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if (name.equals("Retry-After")) {
                    return "120";
                }
                return null;
            }
        };
        TwitterException exception4 = new TwitterException("Test exception message 1", httpResponse1);
        HttpResponse httpResponse2 = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if (name.equals("Retry-After")) {
                    return "120";
                }
                return null;
            }
        };
        TwitterException exception5 = new TwitterException("Test exception message 1", httpResponse2);
        TwitterException exception6 = new TwitterException("Test exception message 1", new IOException("Test cause message 3"), 420);
        TwitterException exception7 = new TwitterException("Test exception message 1", new IOException("Test cause message 3"), 420, "test");

        assertEquals(exception4, exception5);
        assertNotEquals(exception4, exception6);
        assertNotEquals(exception4, exception7);
        assertNotEquals(exception6, exception7);
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test exception message 1", new IOException("Test cause message 1"));
        TwitterException exception2 = new TwitterException("Test exception message 2", new IOException("Test cause message 2"));
        TwitterException exception3 = new TwitterException("Test exception message 1", new IOException("Test cause message 1"));

        int exception1HashCode = exception1.hashCode();
        int exception2HashCode = exception2.hashCode();
        int exception3HashCode = exception3.hashCode();

        assertNotEquals(exception1HashCode, exception2HashCode);
        assertEquals(exception1HashCode, exception3HashCode);

        HttpResponse httpResponse1 = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if (name.equals("Retry-After")) {
                    return "120";
                }
                return null;
            }
        };
        HttpResponse httpResponse2 = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if (name.equals("Retry-After")) {
                    return "120";
                }
                return null;
            }
        };
        TwitterException exception4 = new TwitterException("Test exception message 1", httpResponse1);
        TwitterException exception5 = new TwitterException("Test exception message 1", httpResponse2);
        TwitterException exception6 = new TwitterException("Test exception message 1", new IOException("Test cause message 3"), 420);
        TwitterException exception7 = new TwitterException("Test exception message 1", new IOException("Test cause message 3"), 420, "test");

        int exception4HashCode = exception4.hashCode();
        int exception5HashCode = exception5.hashCode();
        int exception6HashCode = exception6.hashCode();
        int exception7HashCode = exception7.hashCode();

        assertEquals(exception4HashCode, exception5HashCode);
        assertNotEquals(exception4HashCode, exception6HashCode);
        assertNotEquals(exception4HashCode, exception7HashCode);
        assertNotEquals(exception6HashCode, exception7HashCode);
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test exception message", new IOException("Test cause message"), 420);
        String expectedToString = String.format(Locale.ROOT, "TwitterException{exceptionCode=[%s], statusCode=%d, retryAfter=%d, rateLimitStatus=%s, version=%s}", new Object[] { exception.getExceptionCode(), exception.getStatusCode(), exception.getRetryAfter(), exception.getRateLimitStatus(), Version.getVersion() });
        assertEquals(expectedToString, exception.toString());
    }
}