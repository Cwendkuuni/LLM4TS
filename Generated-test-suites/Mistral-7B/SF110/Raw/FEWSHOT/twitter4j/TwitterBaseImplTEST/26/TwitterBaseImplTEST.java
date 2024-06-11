package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
        when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");

        Authorization auth = mock(Authorization.class);
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance()).thenReturn(new BasicAuthorization("userId"));

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertEquals("userId", twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetScreenName_nullOrEmptyAuth() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
        when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");

        Authorization nullAuth = null;
        Authorization emptyAuth = new NullAuthorization();

        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, nullAuth);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, emptyAuth);

        assertNull(twitterBaseImpl1.getScreenName());
        assertNull(twitterBaseImpl2.getScreenName());
    }

    @Test
    public void testGetScreenName_applicationOnlyAuth() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(true);
        when(conf.getOAuth2TokenType()).thenReturn("Bearer");
        when(conf.getOAuth2AccessToken()).thenReturn("accessToken");

        Authorization auth = mock(Authorization.class);
        when(auth.isEnabled()).thenReturn(true);

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertEquals(null, twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
        when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");

        Authorization auth = mock(Authorization.class);
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance()).thenReturn(new BasicAuthorization("userId"));

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test
    public void testGetId_nullOrEmptyAuth() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
        when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");

        Authorization nullAuth = null;
        Authorization emptyAuth = new NullAuthorization();

        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf, nullAuth);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, emptyAuth);

        assertEquals(0L, twitterBaseImpl1.getId());
        assertEquals(0L, twitterBaseImpl2.getId());
    }

    @Test
    public void testGetId_applicationOnlyAuth() throws TwitterException, IllegalStateException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(true);

        Authorization auth = mock(Authorization.class);
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance()).thenReturn(new BasicAuthorization("userId"));

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        doReturn(user).when(twitterBaseImpl).fillInIDAndScreenName(any());

        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
        when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");

        Authorization auth = mock(Authorization.class);
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance()).thenReturn(new BasicAuthorization("userId"));

        HttpParameter[] parameters = new HttpParameter[0];

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getScreenName()).thenReturn("screenName");

        doReturn(user).when(twitterBaseImpl).fillInIDAndScreenName(parameters);

        assertEquals("screenName", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test
    public void testFillInIDAndScreenName_nullParameters() throws TwitterException {
        Configuration conf = mock(Configuration.class);
        when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
        when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
        when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");

        Authorization auth = mock(Authorization.class);
        when(auth.isEnabled()).thenReturn(true);
        when(auth.getInstance()).thenReturn(new BasicAuthorization("userId"));

        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getScreenName()).thenReturn("screenName");

        doReturn(user).when(twitterBaseImpl).fillInIDAndScreenName(null);

        assertEquals("screenName", twitterBaseImpl.getScreenName());
        assertEquals(1L, twitterBaseImpl.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

        twitterBaseImpl.addRateLimitStatusListener(listener);

        verify(listener, times(1)).onRateLimitStatus(any(RateLimitStatusEvent.class));
        verify(listener, times(1)).onRateLimitReached(any(RateLimitStatusEvent.class));
    }

    @Test
    public void testOnRateLimitStatus() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());

        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);

        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);

        twitterBaseImpl.onRateLimitStatus(action);

        verify(action, times(1)).accept(rateLimitStatusEvent);
    }

    @Test
    public void testOnRateLimitReached() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());

        Consumer<RateLimitStatusEvent> action = mock(Consumer.class);

        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);

        twitterBaseImpl.onRateLimitReached(action);

        verify(action, times(1)).accept(rateLimitStatusEvent);
    }
}