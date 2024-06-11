import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;

public class TwitterBaseImplTest {
    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new Authorization());
        String screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }
}