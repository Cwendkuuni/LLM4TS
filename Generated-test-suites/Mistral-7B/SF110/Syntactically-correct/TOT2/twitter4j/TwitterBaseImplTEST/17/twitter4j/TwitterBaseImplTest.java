import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterBaseImpl;
import twitter4j.conf.Configuration;
import twitter4j.auth.BasicAuthorization;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenNameWhenNull() throws TwitterException, IllegalStateException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("consumerKey", "consumerSecret"));
        assertNull(twitterBaseImpl.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWhenIllegalState() throws TwitterException, IllegalStateException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization("consumerKey", "consumerSecret"));
        String expectedScreenName = "testUser";
        twitterBaseImpl.setScreenName(expectedScreenName);
        String actualScreenName = twitterBaseImpl.getScreenName();
        assertEquals(expectedScreenName, actualScreenName);
    }
}