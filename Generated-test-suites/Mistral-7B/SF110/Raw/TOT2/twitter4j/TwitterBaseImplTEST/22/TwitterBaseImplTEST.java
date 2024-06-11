import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterBaseImpl;

public class TwitterBaseImplTest {
    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        String expectedScreenName = "testUser";
        twitterBaseImpl.setScreenName(expectedScreenName);
        String actualScreenName = twitterBaseImpl.getScreenName();
        assertEquals(expectedScreenName, actualScreenName);
    }
}