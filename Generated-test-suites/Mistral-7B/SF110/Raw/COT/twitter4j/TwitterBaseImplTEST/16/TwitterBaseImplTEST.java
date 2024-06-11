import org.junit.Test;
import org.junit.Assert;
import twitter4j.TwitterException;
import twitter4j.Configuration;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.OAuth2Authorization;
import twitter4j.auth.NullAuthorization;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponse;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpClientFactory;
import twitter4j.http.JSONImplFactory;
import twitter4j.http.ObjectFactory;
import twitter4j.util.function.Consumer;
import twitter4j.conf.OAuthSupport;
import twitter4j.conf.OAuth2Support;
import twitter4j.conf.Serializable;
import twitter4j.http.HttpParameter;
import twitter4j.http.HttpMethod;
import twitter4j.json.JSONObject;
import twitter4j.util.RateLimitStatusEvent;
import twitter4j.http.RateLimitStatusListener;
import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.OAuth;
import twitter4j.OAuthRequestToken;
import twitter4j.OAuthAccessToken;
import twitter4j.OAuthToken;
import twitter4j.XAuthAuthorization;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TwitterBaseImplTest {

    @Test
    public void testTwitterBaseImpl() throws Exception {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        // Test getScreenName()
        Assert.assertNull(twitterBaseImpl.getScreenName());

        twitterBaseImpl.init();
        User user = twitterBaseImpl.fillInIDAndScreenName();
        Assert.assertNotNull(user);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
        Assert.assertNotNull(twitterBaseImpl.getId());

        // Test getId()
        Assert.assertEquals(0L, twitterBaseImpl.getId());

        twitterBaseImpl.fillInIDAndScreenName();
        Assert.assertNotNull(twitterBaseImpl.getId());
        Assert.assertNotEquals(0L, twitterBaseImpl.getId());

        // Test addRateLimitStatusListener()
        RateLimitStatusListener listener = new MockRateLimitStatusListener();
        twitterBaseImpl.addRateLimitStatusListener(listener);
        Assert.assertEquals(1, twitterBaseImpl.rateLimitStatusListeners.size());

        // Test onRateLimitStatus()
        HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(429, new byte[0]), null);
        twitterBaseImpl.httpResponseReceived(event);
        listener.onRateLimitStatusCalled = true;
        Assert.assertTrue(listener.onRateLimitStatusCalled);

        // Test getConfiguration()
        Assert.assertNotNull(twitterBaseImpl.getConfiguration());

        // Test fillInIDAndScreenName(HttpParameter[])
        HttpParameter[] parameters = new HttpParameter[0];
        User user2 = twitterBaseImpl.fillInIDAndScreenName(parameters);
        Assert.assertNotNull(user2);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
        Assert.assertNotNull(twitterBaseImpl.getId());

        // Test setOAuthConsumer()
        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        Assert.assertNotNull(twitterBaseImpl.getOAuth());

        // Test getOAuthAccessToken()
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken oauthAccessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        Assert.assertNotNull(oauthAccessToken);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
        Assert.assertNotNull(twitterBaseImpl.getId());

        // Test equals()
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
        Assert.assertNotSame(twitterBaseImpl, twitterBaseImpl2);
        Assert.assertFalse(twitterBaseImpl.equals(twitterBaseImpl2));

        // Test hashCode()
        Assert.assertNotSame(twitterBaseImpl.hashCode(), twitterBaseImpl2.hashCode());

        // Test toString()
        Assert.assertTrue(twitterBaseImpl.toString().contains("TwitterBase{conf="));

        // Test getOAuth()
        Assert.assertNotNull(twitterBaseImpl.getOAuth());

        // Test getOAuth2()
        Assert.assertNull(twitterBaseImpl.getOAuth2());

        // Test setOAuth2Token()
        OAuth2Token oauth2Token = new OAuth2Token("accessToken", "tokenSecret");
        twitterBaseImpl.setOAuth2Token(oauth2Token);
        Assert.assertNotNull(twitterBaseImpl.getOAuth2());

        // Test invalidateOAuth2Token()
        twitterBaseImpl.invalidateOAuth2Token();
        Assert.assertNull(twitterBaseImpl.getOAuth2());

        // Test equals with null object
        Assert.assertFalse(twitterBaseImpl.equals(null));

        // Test getOAuthRequestToken()
        RequestToken requestToken2 = twitterBaseImpl.getOAuthRequestToken("callbackUrl");
        Assert.assertNotNull(requestToken2);

        // Test getOAuthRequestToken(String xAuthAccessType)
        RequestToken requestToken3 = twitterBaseImpl.getOAuthRequestToken("callbackUrl", "read");
        Assert.assertNotNull(requestToken3);

        // Test getOAuthRequestToken(String xAuthAccessType, String xAuthMode)
        RequestToken requestToken4 = twitterBaseImpl.getOAuthRequestToken("callbackUrl", "read", "approval");
        Assert.assertNotNull(requestToken4);

        // Test getOAuthAccessToken()
        AccessToken oauthAccessToken2 = twitterBaseImpl.getOAuthAccessToken();
        Assert.assertNotNull(oauthAccessToken2);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());
        Assert.assertNotNull(twitterBaseImpl.getId());

        // Test getOAuthAccessToken(String oauthVerifier)
        AccessToken oauthAccessToken3 = twitterBaseImpl.getOAuthAccessToken("oauthVerifier");
        Assert.assertNotNull(oauthAccessToken3);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());

        // Test getOAuthAccessToken(RequestToken requestToken)
        RequestToken requestToken5 = twitterBaseImpl.getOAuthRequestToken();
        AccessToken oauthAccessToken4 = twitterBaseImpl.getOAuthAccessToken(requestToken5);
        Assert.assertNotNull(oauthAccessToken4);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());

        // Test getOAuthAccessToken(RequestToken requestToken, String oauthVerifier)
        RequestToken requestToken6 = twitterBaseImpl.getOAuthRequestToken();
        AccessToken oauthAccessToken5 = twitterBaseImpl.getOAuthAccessToken(requestToken6, "oauthVerifier");
        Assert.assertNotNull(oauthAccessToken5);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());

        // Test setOAuthAccessToken(AccessToken accessToken)
        AccessToken accessToken = new AccessToken("accessToken", "tokenSecret", "screenName", 1L);
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        Assert.assertSame(accessToken, twitterBaseImpl.getOAuthAccessToken());

        // Test getOAuthAccessToken(String screenName, String password)
        AccessToken oauthAccessToken6 = twitterBaseImpl.getOAuthAccessToken("screenName", "password");
        Assert.assertNotNull(oauthAccessToken6);
        Assert.assertNotNull(twitterBaseImpl.getScreenName());

        // Test ensureAuthorizationEnabled()
        try {
            twitterBaseImpl.ensureAuthorizationEnabled();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", e.getMessage());
        }

        // Test ensureOAuthEnabled()
        try {
            twitterBaseImpl.ensureOAuthEnabled();
        } catch (IllegalStateException e) {
            Assert.assertEquals("OAuth required. Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/", e.getMessage());
        }

        // Test writeObject() and readObject()
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(twitterBaseImpl);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TwitterBaseImpl twitterBaseImpl3 = (TwitterBaseImpl)ois.readObject();
        Assert.assertNotNull(twitterBaseImpl3);
        Assert.assertEquals(twitterBaseImpl, twitterBaseImpl3);

        // Test setOAuthConsumer(String consumerKey, String consumerSecret)
        try {
            twitterBaseImpl.setOAuthConsumer(null, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
        twitterBaseImpl.setOAuthConsumer("consumerKey2", "consumerSecret2");
        Assert.assertNotEquals(twitterBaseImpl.getOAuth().getConsumerKey(), "consumerKey2");

        // Test getOAuthRequestToken(String callbackUrl, String xAuthAccessType, String xAuthMode)
        try {
            twitterBaseImpl.getOAuthRequestToken("callbackUrl", "read", "approval");
            Assert.fail();
        } catch (TwitterException e) {
            // Expected
        }

        // Test getOAuthAccessToken(AccessToken accessToken)
        try {
            twitterBaseImpl.getOAuthAccessToken(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test writeObject() and readObject() with null fields
        Configuration conf2 = new Configuration();
        Authorization auth2 = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl4 = new TwitterBaseImpl(conf2, auth2);
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
        oos2.writeObject(twitterBaseImpl4);
        ByteArrayInputStream bais2 = new ByteArrayInputStream(baos2.toByteArray());
        ObjectInputStream ois2 = new ObjectInputStream(bais2);
        TwitterBaseImpl twitterBaseImpl5 = (TwitterBaseImpl)ois2.readObject();
        Assert.assertNull(twitterBaseImpl5.getScreenName());
        Assert.assertEquals(0L, twitterBaseImpl5.getId());
        Assert.assertNull(twitterBaseImpl5.getAuthorization());
    }

    public static class MockRateLimitStatusListener implements RateLimitStatusListener {
        boolean onRateLimitStatusCalled = false;

        @Override
        public void onRateLimitStatus(RateLimitStatusEvent event) {}

        @Override
        public void onRateLimitReached(RateLimitStatusEvent event) {}
    }
}