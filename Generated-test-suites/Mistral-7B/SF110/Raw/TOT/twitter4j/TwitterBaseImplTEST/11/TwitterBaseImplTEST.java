package twitter4j.test;

import static org.junit.Assert.*;
import static twitter4j.conf.Configuration.Builder.*;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.XAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.http.HttpResponse;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.Request;
import twitter4j.http.RequestFilter;
import twitter4j.http.StatusLine;
import twitter4j.oauth.OAuth;
import twitter4j.oauth.OAuthFactory;
import twitter4j.oauth.OAuthRequestToken;
import twitter4j.oauth.OAuthResponse;
import twitter4j.oauth.OAuthStatus;
import twitter4j.oauth.RateLimitStatus;
import twitter4j.oauth.RateLimitStatusEvent;
import twitter4j.oauth.RateLimitStatusListener;
import twitter4j.util.JSONArray;
import twitter4j.util.JSONObject;

import java.util.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration configuration;
 private HttpClient httpClient;
 private OAuthFactory oauthFactory;
 private OAuthAuthorization oauthAuthorization;
 private XAuthAuthorization xAuthAuthorization;

 @Before
 public void setUp() throws Exception {
 configuration = new Configuration.Builder().setDebugEnabled(true).setOAuthConsumerKey("testKey").setOAuthConsumerSecret("testSecret").build();
 twitterBaseImpl = new TwitterBaseImpl(configuration, null);
 httpClient = HttpClientFactory.getInstance(twitterBaseImpl.getConfiguration().getHttpClientConfiguration());
 oauthFactory = new OAuthFactory();
 oauthAuthorization = new OAuthAuthorization(configuration);
 xAuthAuthorization = new XAuthAuthorization(oauthAuthorization);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 twitterBaseImpl.getScreenName();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithDisabledAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 twitterBaseImpl.setAuthorization(null);
 twitterBaseImpl.getScreenName();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithNoScreenName() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 twitterBaseImpl.setScreenName(null);
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 twitterBaseImpl.getId();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithDisabledAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 twitterBaseImpl.setAuthorization(null);
 twitterBaseImpl.getId();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithNoID() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 twitterBaseImpl.setID(0L);
 twitterBaseImpl.getId();
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 User user = twitterBaseImpl.fillInIDAndScreenName();
 assertEquals("testScreenName", user.getScreenName());
 assertEquals(123456789L, user.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameWithRateLimitExceeded() throws TwitterException {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 5, 15);
 twitterBaseImpl.setRateLimitStatus(rateLimitStatus);
 twitterBaseImpl.fillInIDAndScreenName();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 }

 @Test
 public void testOnRateLimitStatus() throws TwitterException {
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 5, 15);
 twitterBaseImpl.setRateLimitStatus(rateLimitStatus);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(httpClient, null, new StatusLine(200, "OK"), null, null, null));
 }

 @Test
 public void testOnRateLimitReached() throws TwitterException {
 twitterBaseImpl.addRateLimitStatusListener(new TestRateLimitStatusListener());
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 5, 15);
 twitterBaseImpl.setRateLimitStatus(rateLimitStatus);
 twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(httpClient, null, new StatusLine(429, "Too Many Requests"), null, null, null));
 }

 @Test
 public void testGetAuthorization() {
 Authorization auth = twitterBaseImpl.getAuthorization();
 assertNotNull(auth);
 }

 @Test
 public void testGetConfiguration() {
 Configuration conf = twitterBaseImpl.getConfiguration();
 assertEquals("testKey", conf.getOAuthConsumerKey());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthConsumerWithNullConsumerKey() {
 twitterBaseImpl.setOAuthConsumer(null, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOAuthConsumerWithNullConsumerSecret() {
 twitterBaseImpl.setOAuthConsumer("testKey", null);
 }

 @Test
 public void testSetOAuthConsumer() throws TwitterException {
 twitterBaseImpl.setOAuthConsumer("testKey", "testSecret");
 Authorization auth = twitterBaseImpl.getAuthorization();
 assertEquals("testKey", auth.getConsumerKey());
 assertEquals("testSecret", auth.getConsumerSecret());
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 OAuthRequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken);
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthRequestTokenWithRateLimitExceeded() throws TwitterException {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 5, 15);
 twitterBaseImpl.setRateLimitStatus(rateLimitStatus);
 twitterBaseImpl.getOAuthRequestToken();
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken);
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithRateLimitExceeded() throws TwitterException {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 5, 15);
 twitterBaseImpl.setRateLimitStatus(rateLimitStatus);
 twitterBaseImpl.getOAuthAccessToken();
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(new OAuthResponse(new OAuthStatus(200, "OK"), new JSONObject("access_token", "testAccessToken")));
 assertEquals("testAccessToken", accessToken.getToken());
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testOAuthVerifier", new OAuthResponse(new OAuthStatus(200, "OK"), new JSONObject("access_token", "testAccessToken", "oauth_token_secret", "testOAuthTokenSecret")));
 assertEquals("testAccessToken", accessToken.getToken());
 assertEquals("testOAuthTokenSecret", accessToken.getTokenSecret());
 }

 @Test
 public void testSetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = new AccessToken(123456789L, "testAccessToken", "testOAuthTokenSecret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 AccessToken accessTokenFromTwitter = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(accessToken.getToken(), accessTokenFromTwitter.getToken());
 assertEquals(accessToken.getTokenSecret(), accessTokenFromTwitter.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testScreenName", "testPassword");
 assertEquals("testScreenName", accessToken.getScreenName());
 }

 @Test
 public void testGetOAuth2Token() throws TwitterException {
 OAuth2Token oAuth2Token = twitterBaseImpl.getOAuth2Token();
 assertNotNull(oAuth2Token);
 }

 @Test
 public void testSetOAuth2Token() throws TwitterException {
 OAuth2Token oAuth2Token = new OAuth2Token("testTokenType", "testAccessToken");
 twitterBaseImpl.setOAuth2Token(oAuth2Token);
 OAuth2Token oAuth2TokenFromTwitter = twitterBaseImpl.getOAuth2Token();
 assertEquals(oAuth2Token.getTokenType(), oAuth2TokenFromTwitter.getTokenType());
 assertEquals(oAuth2Token.getAccessToken(), oAuth2TokenFromTwitter.getAccessToken());
 }

 @Test
 public void testInvalidateOAuth2Token() throws TwitterException {
 twitterBaseImpl.invalidateOAuth2Token();
 assertNull(twitterBaseImpl.getOAuth2Token());
 }

 @After
 public void tearDown() throws Exception {
 }

 class TestRateLimitStatusListener implements RateLimitStatusListener {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 }
 }
}