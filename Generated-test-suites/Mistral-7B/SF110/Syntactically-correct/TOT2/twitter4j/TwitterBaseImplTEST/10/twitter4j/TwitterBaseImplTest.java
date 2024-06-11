package twitter4j.test;

import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration configuration;
    private Authorization authorization;

    @Before
    public void setUp() {
        configuration = new Configuration();
        authorization = mock(Authorization.class);
        twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        when(authorization.isEnabled()).thenReturn(true);
        String screenName = twitterBaseImpl.getScreenName();
        assertEquals("test", screenName);

        verify(authorization).isEnabled();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameIllegalStateException() {
        when(authorization.isEnabled()).thenReturn(false);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetScreenNameNullAuthorization() {
        twitterBaseImpl = new TwitterBaseImpl(new Configuration(), null);
        String screenName = twitterBaseImpl.getScreenName();
        assertEquals(null, screenName);
    }

    @Test(expected = TwitterException.class)
    public void testGetScreenNameTwitterException() throws Exception {
        doThrow(new TwitterException("Test TwitterException"))
                .when(twitterBaseImpl)
                .getScreenName();
        twitterBaseImpl.getScreenName();
    }

    // Test other methods similarly

    @After
    public void tearDown() {
        reset(authorization);
    }
}