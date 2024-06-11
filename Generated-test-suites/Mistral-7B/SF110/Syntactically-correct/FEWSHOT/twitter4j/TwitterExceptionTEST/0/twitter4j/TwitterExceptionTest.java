package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        IOException cause = mock(IOException.class);
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithMessageAndHttpResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("5");
        TwitterException exception = new TwitterException("Test Exception Message", httpResponse);
        assertEquals("Test Exception Message\nRate limitation is not exceeded", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(5, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("5");
        IOException cause = mock(IOException.class);
        TwitterException exception = new TwitterException("Test Exception Message", cause, 420, httpResponse);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals("Test Exception Message", exception.getCause().getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(420, exception.getStatusCode());
        assertEquals(5, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponseWithRateLimitStatus() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("5");
        when(httpResponse.getContentAsString()).thenReturn("{\"rate_limit_status\":{\"remaining_hits\":5,\"hits_per_minute\":15}}");
        RateLimitStatusJSONImpl rateLimitStatusJSONImpl = mock(RateLimitStatusJSONImpl.class);
        when(rateLimitStatusJSONImpl.createFromResponseHeader(httpResponse)).thenReturn(new RateLimitStatus(5, 15));
        TwitterException exception = new TwitterException("Test Exception Message", null, 420, httpResponse, rateLimitStatusJSONImpl);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(new RateLimitStatus(5, 15), exception.getRateLimitStatus());
        assertEquals(null, exception.getFeatureSpecificRateLimitStatus());
        assertEquals(420, exception.getStatusCode());
        assertEquals(5, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndHttpResponseWithFeatureSpecificRateLimitStatus() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(420);
        when(httpResponse.getResponseHeader("Retry-After")).thenReturn("5");
        when(httpResponse.getContentAsString()).thenReturn("{\"rate_limit_status\":{}, \"feature_specific_rate_limit_status\":{\"limit\":10,\"remaining_hits\":5,\"hits_per_minute\":15}}");
        RateLimitStatusJSONImpl rateLimitStatusJSONImpl = mock(RateLimitStatusJSONImpl.class);
        when(rateLimitStatusJSONImpl.createFromResponseHeader(httpResponse)).thenReturn(null);
        when(rateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(httpResponse)).thenReturn(new FeatureSpecificRateLimitStatus(10, 5, 15));
        TwitterException exception = new TwitterException("Test Exception Message", null, 420, httpResponse, rateLimitStatusJSONImpl);
        assertEquals("Test Exception Message", exception.getMessage());
        assertEquals(null, exception.getRateLimitStatus());
        assertEquals(new FeatureSpecificRateLimitStatus(10, 5, 15), exception.getFeatureSpecificRateLimitStatus());
        assertEquals(420, exception.getStatusCode());
        assertEquals(5, exception.getRetryAfter());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception 1");
        TwitterException exception2 = new TwitterException("Test Exception 2");
        TwitterException exception3 = new TwitterException("Test Exception 1");
        TwitterException exception4 = new TwitterException("Test Exception 1");
        TwitterException exception5 = new TwitterException("Test Exception 1");
        TwitterException exception6 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"));
        TwitterException exception7 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        });
        TwitterException exception8 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus(5, 15));
        TwitterException exception9 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new FeatureSpecificRateLimitStatus(10, 5, 15));
        TwitterException exception10 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 421;
            }
        });

        assertTrue(exception1.equals(exception1));
        assertTrue(exception1.equals(exception3));
        assertTrue(exception1.equals(exception4));
        assertTrue(exception1.equals(exception5));
        assertFalse(exception1.equals(exception2));
        assertFalse(exception1.equals(exception6));
        assertFalse(exception1.equals(exception7));
        assertFalse(exception1.equals(exception8));
        assertFalse(exception1.equals(exception9));
        assertFalse(exception1.equals(exception10));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception 1");
        TwitterException exception2 = new TwitterException("Test Exception 2");
        TwitterException exception3 = new TwitterException("Test Exception 1");
        TwitterException exception4 = new TwitterException("Test Exception 1");
        TwitterException exception5 = new TwitterException("Test Exception 1");
        TwitterException exception6 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"));
        TwitterException exception7 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        });
        TwitterException exception8 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new RateLimitStatus(5, 15));
        TwitterException exception9 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        }, new FeatureSpecificRateLimitStatus(10, 5, 15));

        assertEquals(Arrays.hashCode(new Object[] { 420, 5, null }), exception1.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, null, null }), exception2.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, 5, null }), exception3.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, 5, null }), exception4.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, 5, new IOException("Test IO Exception") }), exception5.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, 5, new IOException("Test IO Exception") }), exception6.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, 5, null }), exception7.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, 5, new RateLimitStatus(5, 15) }), exception8.hashCode());
        assertEquals(Arrays.hashCode(new Object[] { 420, 5, new FeatureSpecificRateLimitStatus(10, 5, 15) }), exception9.hashCode());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception1 = new TwitterException("Test Exception 1");
        IOException ioException = mock(IOException.class);
        TwitterException exception2 = new TwitterException("Test Exception 2", ioException);
        assertTrue(exception2.isCausedByNetworkIssue());
        assertFalse(exception1.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Exception 1");
        TwitterException exception2 = new TwitterException("Test Exception 1", null, 400, null);
        TwitterException exception3 = new TwitterException("Test Exception 1", null, 420, null);
        TwitterException exception4 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 400, null);
        TwitterException exception5 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 420, null);
        TwitterException exception6 = new TwitterException("Test Exception 1", new IOException("Test IO Exception"), 429, null);
        TwitterException exception7 = new TwitterException("Test Exception 1", null, 404, null);

        assertTrue(exception2.exceededRateLimitation());
        assertTrue(exception3.exceededRateLimitation());
        assertFalse(exception1.exceededRateLimitation());
        assertFalse(exception4.exceededRateLimitation());
        assertFalse(exception5.exceededRateLimitation());
        assertFalse(exception6.exceededRateLimitation());
        assertFalse(exception7.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception1 = new TwitterException("Test Exception 1");
        TwitterException exception2 = new TwitterException("Test Exception 1", null, 400, null);
        TwitterException exception3 = new TwitterException("Test Exception 1", null, 420, null);
        TwitterException exception4 = new TwitterException("Test Exception 1", null, 400, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        });
        TwitterException exception5 = new TwitterException("Test Exception 1", null, 420, new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        });

        assertFalse(exception1.resourceNotFound());
        assertFalse(exception2.resourceNotFound());
        assertFalse(exception3.resourceNotFound());
        assertTrue(exception4.resourceNotFound());
        assertTrue(exception5.resourceNotFound());
    }
}