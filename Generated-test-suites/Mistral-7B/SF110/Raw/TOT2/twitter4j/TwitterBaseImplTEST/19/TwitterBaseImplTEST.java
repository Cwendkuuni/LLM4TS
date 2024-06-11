import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        String screenName = twitterBaseImpl.getScreenName();
        assertNull(screenName);
    }

    @Test
    public void testGetId() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        long id = twitterBaseImpl.getId();
        assertEquals(-1L, id);
    }

    @Test
    public void testFillInIDAndScreenName() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), new NullAuthorization());
        User user = twitterBaseImpl.fillInIDAndScreenName();
        assertNull(user);
    }
}