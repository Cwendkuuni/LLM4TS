package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test message");;
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpURLConnection httpResponse = mock(HttpURLConnection.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertEquals("Test message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionWithCauseAndHttpResponse() {
        HttpURLConnection httpResponse = mock(HttpURLConnection.class);
        when(httpResponse.getStatusCode()).thenReturn(400);
        Exception cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test message", cause, 400);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponseAndRateLimitStatus() {
        HttpURLConnection httpResponse = mock(HttpURLConnection.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getHeaderFields()).thenReturn(new Header[] { new Header("Retry-After", "123") });
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        TwitterException exception = new TwitterException("Test message", httpResponse, rateLimitStatus);
        assertEquals("Test message" + System.lineSeparator() + getCause(httpResponse), exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(123, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithCauseAndHttpResponseAndRateLimitStatus() {
        HttpURLConnection httpResponse = mock(HttpURLConnection.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getHeaderFields()).thenReturn(new Header[] { new Header("Retry-After", "123") });
        RateLimitStatus rateLimitStatus = mock(RateLimitStatus.class);
        Exception cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test message", cause, 420, rateLimitStatus);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(420, exception.getStatusCode());
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
        assertEquals(123, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        });
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        });
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message 1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus() {
            @Override
            public int getLimit() {
                return 100;
            }

            @Override
            public int getRemaining() {
                return 99;
            }
        });

        TwitterException exception2 = new TwitterException("Test message 2", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus() {
            @Override
            public int getLimit() {
                return 100;
            }

            @Override
            public int getRemaining() {
                return 99;
            }
        });

        TwitterException exception3 = new TwitterException("Test message 3", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus() {
            @Override
            public int getLimit() {
                return 100;
            }

            @Override
            public int getRemaining() {
                return 100;
            }
        });

        assertEquals(exception1, exception1);
        assertEquals(exception1, exception2);
        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message 1", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus() {
            @Override
            public int getLimit() {
                return 100;
            }

            @Override
            public int getRemaining() {
                return 99;
            }
        });

        TwitterException exception2 = new TwitterException("Test message 2", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus() {
            @Override
            public int getLimit() {
                return 100;
            }

            @Override
            public int getRemaining() {
                return 99;
            }
        });

        TwitterException exception3 = new TwitterException("Test message 3", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus() {
            @Override
            public int getLimit() {
                return 100;
            }

            @Override
            public int getRemaining() {
                return 100;
            }
        });

        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();
        int hashCode3 = exception3.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertFalse(hashCode1 == hashCode3);
        assertFalse(hashCode1.equals(hashCode3));
    }

    private String getCause(HttpURLConnection httpResponse) {
        final int statusCode = httpResponse.getStatusCode();
        String cause = null;
        switch (statusCode) {
            case 304: {
                cause = "There was no new data to return.";
                break;
            }
            case 400: {
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            }
            case 401: {
                cause = "Authentication credentials were missing or incorrect.";
                break;
            }
            case 403: {
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            }
            case 404: {
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            }
            case 406: {
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            }
            case 420: {
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            }
            case 500: {
                cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
                break;
            }
            case 502: {
                cause = "Twitter is down or being upgraded.";
                break;
            }
            case 503: {
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            }
            default: {
                cause = "";
                break;
            }
        }
        return statusCode + ":" + cause;
    }
}