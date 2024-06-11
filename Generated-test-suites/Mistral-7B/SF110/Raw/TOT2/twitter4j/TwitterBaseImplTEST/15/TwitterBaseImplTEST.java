import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;

public class TwitterBaseImplTest {
    private static final Configuration conf = new Configuration();
    private static final Authorization auth = new BasicAuthorization("consumerKey", "consumerSecret");
    private static final TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String expectedScreenName = "testUser";
        conf.setOAuthConsumerKey("testConsumerKey");
        conf.setOAuthConsumerSecret("testConsumerSecret");
        twitterBaseImpl.setScreenName(expectedScreenName);
        assertEquals(expectedScreenName, twitterBaseImpl.getScreenName());
    }
}