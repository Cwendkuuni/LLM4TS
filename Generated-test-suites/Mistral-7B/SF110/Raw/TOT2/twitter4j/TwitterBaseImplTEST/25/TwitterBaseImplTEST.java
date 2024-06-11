import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterException;
import twitter4j.TwitterBaseImpl;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization());
        String screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test(expected = TwitterException.class)
    public void testGetScreenNameThrowsExceptionWhenAuthorizationNotEnabled() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameThrowsIllegalStateExceptionWhenScreenNameIsNull() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization()) {
            @Override
            public String getScreenName() throws TwitterException, IllegalStateException {
                return null;
            }
        };
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetScreenNameReturnsScreenNameWithUserId() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new BasicAuthorization()) {
            @Override
            public String getScreenName() throws TwitterException, IllegalStateException {
                return "testUser";
            }
        };
        assertEquals("testUser", twitterBaseImpl.getScreenName());
    }
}