package twitter4j;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.io.*;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws Exception {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);
        String expectedScreenName = "expectedScreenName";
        User user = mock(User.class);
        when(user.getScreenName()).thenReturn(expectedScreenName);
        when(twitterBaseImpl.fillInIDAndScreenName(any(HttpParameter[].class))).thenReturn(user);

        String actualScreenName = twitterBaseImpl.getScreenName();
        assertEquals(expectedScreenName, actualScreenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithDisabledAuth() throws Exception {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(false);

        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws Exception {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(true);
        long expectedId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(expectedId);
        when(twitterBaseImpl.fillInIDAndScreenName(any(HttpParameter[].class))).thenReturn(user);

        long actualId = twitterBaseImpl.getId();
        assertEquals(expectedId, actualId);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithDisabledAuth() throws Exception {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = null;
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        when(auth.isEnabled()).thenReturn(false);

        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new ConfigurationBuilder().build(), NullAuthorization.getInstance());

        RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
        twitterBaseImpl.addRateLimitStatusListener(listener);
        assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
    }
}