import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterBaseImpl;
import twitter4j.conf.Configuration;
import twitter4j.auth.NullAuthorization;

public class TwitterBaseImplTest {
    @Test
    public void testGetScreenName() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        String screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test
    public void testGetScreenName_AuthNotEnabled() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization()) {
            @Override
            public boolean isEnabled() {
                return false;
            }
        };
        try {
            twitterBaseImpl.getScreenName();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetScreenName_NullScreenName() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization()) {
            @Override
            public String getScreenName() {
                return null;
            }
        };
        assertNull(twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetScreenName_ContainsAtSymbol() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization()) {
            @Override
            public String getScreenName() {
                return "test@example.com";
            }
        };
        assertNull(twitterBaseName.getScreenName());
    }

    @Test(expected = TwitterException.class)
    public void testGetScreenName_TwitterException() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        twitterBaseImpl.expectTwitterException();
        twitterBaseImpl.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_IllegalStateException() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization()) {
            @Override
            public boolean isEnabled() {
                return false;
            }
        };
        twitterBaseImpl.getScreenName();
    }

    private void expectTwitterException(TwitterBaseImpl twitterBaseImpl) {
        try {
            twitterBaseImpl.getScreenName();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Unexpected IllegalStateException", e);
        }
        fail("TwitterException expected");
    }
}