import twitter4j.TwitterBaseImpl;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.auth.BasicAuthorization;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        String screenName = twitterBaseImpl.getScreenName();
        assertNull(screenName);

        twitterBaseImpl.setFactory();
        twitterBaseImpl.fillInIDAndScreenName();
        screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);

        twitterBaseImpl.setAuthorization(new BasicAuthorization("testConsumerKey", "testConsumerSecret"));
        screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
        assertEquals("testConsumerKey", twitterBaseImpl.getScreenName().substring(0, "testConsumerKey".length()));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithDisabledAuthorization() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithNoScreenName() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("testConsumerKey", "testConsumerSecret"));
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = TwitterException.class)
    public void testGetScreenNameWithFailedRequest() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("testConsumerKey", "testConsumerSecret"));
        twitterBaseImpl.setRateLimitStatusListener((event) -> {
            throw new TwitterException("Test error message");
        });
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetScreenNameWithScreenNameContainingAtSign() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("testConsumerKey", "testConsumerSecret"));
        twitterBaseImpl.setFactory();
        twitterBaseImpl.fillInIDAndScreenName();
        String screenName = twitterBaseImpl.getScreenName();
        assertTrue(screenName.startsWith("@"));
    }
}