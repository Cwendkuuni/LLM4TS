package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.http.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(BasicAuthorization.class)).thenReturn(new BasicAuthorization("1234567890", "password"));

        String screenName = twitterBaseImpl.getScreenName();
        assertEquals("1234567890", screenName);

        ArgumentCaptor<HttpParameter[]> parametersCaptor = ArgumentCaptor.forClass(HttpParameter[].class);
        verify(twitterBaseImpl).fillInIDAndScreenName(parametersCaptor.capture());
        HttpParameter[] parameters = parametersCaptor.getValue();
        assertEquals(1, parameters.length);
        assertEquals("get", parameters[0].getName());
        assertEquals("account/verify_credentials.json", parameters[0].getValue());

        twitterBaseImpl.tearDown();

        when(auth.isEnabled()).thenReturn(false);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
        assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", exception.getMessage());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(BasicAuthorization.class)).thenReturn(new BasicAuthorization("1234567890", "password"));

        User user = mock(User.class);
        when(user.getId()).thenReturn(1234567890L);
        when(user.getScreenName()).thenReturn("1234567890");
        when(twitterBaseImpl.fillInIDAndScreenName(any(HttpParameter[].class))).thenReturn(user);

        long id = twitterBaseImpl.getId();
        assertEquals(1234567890L, id);

        ArgumentCaptor<HttpParameter[]> parametersCaptor = ArgumentCaptor.forClass(HttpParameter[].class);
        verify(twitterBaseImpl).fillInIDAndScreenName(parametersCaptor.capture());
        HttpParameter[] parameters = parametersCaptor.getValue();
        assertEquals(1, parameters.length);
        assertEquals("get", parameters[0].getName());
        assertEquals("account/verify_credentials.json", parameters[0].getValue());

        twitterBaseImpl.tearDown();

        when(auth.isEnabled()).thenReturn(false);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
        assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", exception.getMessage());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance(BasicAuthorization.class)).thenReturn(new BasicAuthorization("1234567890", "password"));

        User user = mock(User.class);
        when(user.getId()).thenReturn(1234567890L);
        when(user.getScreenName()).thenReturn("1234567890");
        when(twitterBaseImpl.fillInIDAndScreenName(any(HttpParameter[].class))).thenReturn(user);

        User result = twitterBaseImpl.fillInIDAndScreenName();
        assertEquals(user, result);

        ArgumentCaptor<HttpParameter[]> parametersCaptor = ArgumentCaptor.forClass(HttpParameter[].class);
        verify(twitterBaseImpl).fillInIDAndScreenName(parametersCaptor.capture());
        HttpParameter[] parameters = parametersCaptor.getValue();
        assertEquals(1, parameters.length);
        assertEquals("get", parameters[0].getName());
        assertEquals("account/verify_credentials.json", parameters[0].getValue());

        twitterBaseImpl.tearDown();

        when(auth.isEnabled()).thenReturn(false);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.fillInIDAndScreenName());
        assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", exception.getMessage());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), NullAuthorization.getInstance());
        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

        twitterBaseImpl.addRateLimitStatusListener(listener);
        verify(twitterBaseImpl.rateLimitStatusListeners, contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), NullAuthorization.getInstance());

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);

        twitterBaseImpl.onRateLimitStatus(action);
        verify(twitterBaseImpl.rateLimitStatusListeners, atLeastOnce()).forEach(l -> l.onRateLimitStatus(event));
    }

    @Test
    public void testOnRateLimitReached() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), NullAuthorization.getInstance());

        RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);

        twitterBaseImpl.onRateLimitReached(action);
        verify(twitterBaseImpl.rateLimitStatusListeners, atLeastOnce()).forEach(l -> l.onRateLimitReached(event));
    }

    @Test
    public void testHttpResponseReceived() throws IOException {
        TwitterBaseImpl twitterBaseImpl = mock(TwitterBaseImpl.class);
        HttpResponseEvent event = mock(HttpResponseEvent.class);
        RateLimitStatus status = mock(RateLimitStatus.class);

        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(2);

        doAnswer(invocation -> {
            RateLimitStatusListener listener = invocation.getArgument(0);
            listener.onRateLimitStatus(status);
            listener.onRateLimitReached(status);
            return null;
        }).when(twitterBaseImpl).onRateLimitStatus(eq(status));

        doAnswer(invocation -> {
            RateLimitStatusListener listener = invocation.getArgument(0);
            listener.onRateLimitReached(status);
            return null;
        }).when(twitterBaseImpl).onRateLimitReached(eq(status));

        doReturn(status).when(event).getRateLimitStatus();

        doReturn(twitterBaseImpl).when(twitterBaseImpl).getAuthorization();
        doReturn(new Configuration()).when(twitterBaseImpl).getConfiguration();

        twitterBaseImpl.rateLimitStatusListeners = rateLimitStatusListeners;

        twitterBaseImpl.httpResponseReceived(event);

        verify(rateLimitStatusListeners, times(2)).forEach(l -> l.onRateLimitStatus(status));
        verify(rateLimitStatusListeners, times(2)).forEach(l -> l.onRateLimitReached(status));
    }
}