import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.TwitterBaseImpl;
import twitter4j.Configuration;
import twitter4j.auth.NullAuthorization;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        String screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_AuthenticationNotEnabled() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), null);
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_ScreenNameIsNull() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        twitterBaseImpl.screenName = null;
        try {
            twitterBaseImpl.getScreenName();
        } catch (TwitterException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_ScreenNameEmpty() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        twitterBaseImpl.screenName = "";
        try {
            twitterBaseImpl.getScreenName();
        } catch (TwitterException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }
}