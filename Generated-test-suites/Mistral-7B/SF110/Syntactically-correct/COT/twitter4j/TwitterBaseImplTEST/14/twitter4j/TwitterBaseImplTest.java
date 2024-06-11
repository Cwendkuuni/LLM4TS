package twitter4j; // or the package where the TwitterBaseImpl class is located

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Configuration;
import twitter4j.OAuthAuthorization;
import twitter4j.OAuthConsumer;
import twitter4j.OAuthRequestToken;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        String screenName = twitterBaseImpl.getScreenName();
        assertNull(screenName);

        twitterBaseImpl.fillInIDAndScreenName();
        screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithDisabledAuth() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        long id = twitterBaseImpl.getId();
        assertEquals(0, id);

        twitterBaseImpl.fillInIDAndScreenName();
        id = twitterBaseImpl.getId();
        assertNotEquals(0, id);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithDisabledAuth() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getId();
    }

    @Test
    public void testAddRateLimitStatusListener() {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), null);
        twitterBaseImpl.addRateLimitStatusListener((listener) -> {});
        assertFalse(twitterBaseImpl.rateLimitStatusListeners.isEmpty());
    }

    @Test
    public void testOnRateLimitStatus() throws TwitterException {
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(new Configuration(), null);
        RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, null, true);
        Consumer<RateLimitStatusEvent> action = (event1) -> {};
        twitterBaseImpl.onRateLimitStatus(action);
        twitterBaseImpl.onRateLimitReached(action);
        twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, new TwitterException(420, "Rate limit exceeded")));
        assertTrue(action.isCalled());
    }

    @Test
    public void testSetOAuthConsumer() throws TwitterException {
        Configuration conf = new Configuration();
        String consumerKey = "consumerKey";
        String consumerSecret = "consumerSecret";
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
        assertTrue(twitterBaseImpl.auth instanceof OAuthAuthorization);
        OAuthAuthorization auth = (OAuthAuthorization) twitterBaseImpl.auth;
        assertEquals(consumerKey, auth.getConsumerKey());
        assertEquals(consumerSecret, auth.getConsumerSecret());
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerWithNullConsumerKey() throws TwitterException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumerWithNullConsumerSecret() throws TwitterException {
        Configuration conf = new Configuration();
        String consumerKey = "consumerKey";
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthConsumer(consumerKey, null);
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOAuthRequestTokenWithDisabledAuth() throws TwitterException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getOAuthRequestToken();
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
        assertEquals(Twitter.UNVERIFIED, accessToken.getStatus());

        twitterBaseImpl.fillInIDAndScreenName();
        accessToken = twitterBaseImpl.getOAuthAccessToken();
        assertNotNull(accessToken);
        assertEquals(Twitter.VERIFIED, accessToken.getStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOAuthAccessTokenWithDisabledAuth() throws TwitterException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getOAuthAccessToken();
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessToken);
        assertEquals(Twitter.UNVERIFIED, accessToken.getStatus());

        twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[] {});
        accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessToken);
        assertEquals(Twitter.VERIFIED, accessToken.getStatus());
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthAccessTokenWithInvalidVerifier() throws TwitterException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "invalidVerifier");
    }

    @Test
    public void testGetOAuthAccessTokenWithVerifier() throws TwitterException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        String verifier = "verifier";
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, verifier);
        assertNotNull(accessToken);
        assertEquals(Twitter.VERIFIED, accessToken.getStatus());
    }

    @Test
    public void testSetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        AccessToken accessToken = new AccessToken("accessToken", "accessTokenSecret", Twitter.VERIFIED);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        AccessToken storedAccessToken = twitterBaseImpl.getOAuthAccessToken();
        assertEquals(accessToken.getScreenName(), storedAccessToken.getScreenName());
        assertEquals(accessToken.getId(), storedAccessToken.getId());
    }

    @Test
    public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
        Configuration conf = new Configuration();
        String screenName = "screenName";
        String password = "password";
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(screenName, password);
        assertNotNull(accessToken);
        assertEquals(Twitter.UNVERIFIED, accessToken.getStatus());

        twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[] {});
        accessToken = twitterBaseImpl.getOAuthAccessToken(screenName, password);
        assertNotNull(accessToken);
        assertEquals(Twitter.VERIFIED, accessToken.getStatus());
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthAccessTokenWithInvalidScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        String invalidScreenName = "invalidScreenName";
        twitterBaseImpl.getOAuthAccessToken(invalidScreenName, "password");
    }

    @Test(expected = TwitterException.class)
    public void testGetOAuthAccessTokenWithInvalidPassword() throws TwitterException {
        Configuration conf = new Configuration();
        String screenName = "screenName";
        String invalidPassword = "invalidPassword";
        twitterBaseImpl.getOAuthAccessToken(screenName, invalidPassword);
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthRequestTokenAndVerifier() throws TwitterException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        String verifier = "verifier";
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, verifier);
        assertNotNull(accessToken);
        assertEquals(Twitter.UNVERIFIED, accessToken.getStatus());

        twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[] {});
        accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, verifier);
        assertNotNull(accessToken);
        assertEquals(Twitter.VERIFIED, accessToken.getStatus());
    }

    @Test
    public void testGetOAuthAccessTokenWithOAuthRequestTokenVerifierAndPassword() throws TwitterException {
        Configuration conf = new Configuration();
        OAuthAuthorization auth = new OAuthAuthorization(conf);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        String verifier = "verifier";
        String password = "password";
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, verifier, password);
        assertNotNull(accessToken);
        assertEquals(Twitter.UNVERIFIED, accessToken.getStatus());

        twitterBaseImpl.fillInIDAndScreenName(new HttpParameter[] {});
        accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, verifier, password);
        assertNotNull(accessToken);
        assertEquals(Twitter.VERIFIED, accessToken.getStatus());
    }
}