import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.TwitterBaseImpl;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws Exception {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        String screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }
}