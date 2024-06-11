import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.*;
import static twitter4j.conf.Configuration.Builder.*;
import java.util.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;

 @Before
 public void setUp() {
 Configuration conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret")
 .build();
 Authorization auth = new OAuthAuthorization(conf);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws Exception {
 String expected = "testUser";
 twitterBaseImpl.setScreenName(expected);
 String actual = twitterBaseImpl.getScreenName();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithAuthDisabled() throws Exception {
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws Exception {
 long expected = 123456789L;
 twitterBaseImpl.setId(expected);
 long actual = twitterBaseImpl.getId();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithAuthDisabled() throws Exception {
 twitterBaseImpl.getId();
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 User expectedUser = new UserJSONImpl(new StatusJSONImpl(), conf);
 expectedUser.setScreenName("testUser");
 expectedUser.setId(123456789L);
 twitterBaseImpl.fillInIDAndScreenName(expectedUser.getStatuses());
 User actualUser = twitterBaseImpl.getUser();
 assertEquals(expectedUser.getScreenName(), actualUser.getScreenName());
 assertEquals(expectedUser.getId(), actualUser.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameWithNullParameters() throws TwitterException {
 twitterBaseImpl.fillInIDAndScreenName(null);
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(mock(RateLimitStatusListener.class));
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(), true);
 Consumer<RateLimitStatusEvent> consumer = mock(Consumer.class);
 twitterBaseImpl.onRateLimitStatus(consumer);
 verify(consumer).accept(event);
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(), true);
 Consumer<RateLimitStatusEvent> consumer = mock(Consumer.class);
 twitterBaseImpl.onRateLimitReached(consumer);
 verify(consumer).accept(event);
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 HttpResponseEvent event = new HttpResponseEvent(twitterBaseImpl, new HttpResponse(200, "OK"));
 twitterBaseImpl.httpResponseReceived(event);
 }

 @Test
 public void testSetOAuthConsumer() throws Exception {
 String consumerKey = "consumerKey";
 String consumerSecret = "consumerSecret";
 twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
 Authorization auth = twitterBaseImpl.getAuthorization();
 assertEquals(consumerKey, auth.getApiKey());
 assertEquals(consumerSecret, auth.getApiSecret());
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerWithNullConsumerKey() throws Exception {
 twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testSetOAuthConsumerWithNullConsumerSecret() throws Exception {
 twitterBaseImpl.setOAuthConsumer("consumerKey", null);
 }

 @Test
 public void testGetOAuthRequestToken() throws Exception {
 RequestToken expectedRequestToken = new RequestTokenImpl();
 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken();
 assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
 assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthRequestTokenWithCallbackUrl() throws Exception {
 String callbackUrl = "http://test.com/callback";
 RequestToken expectedRequestToken = new RequestTokenImpl();
 expectedRequestToken.setCallback(callbackUrl);
 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);
 assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
 assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
 assertEquals(expectedRequestToken.getCallback(), actualRequestToken.getCallback());
 }

 @Test
 public void testGetOAuthRequestTokenWithXAuthAccessType() throws Exception {
 String xAuthAccessType = "read";
 RequestToken expectedRequestToken = new RequestTokenImpl();
 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken(null, xAuthAccessType);
 assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
 assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
 assertEquals(xAuthAccessType, actualRequestToken.getXAuthAccessType());
 }

 @Test
 public void testGetOAuthRequestTokenWithXAuthAccessTypeAndXAuthMode() throws Exception {
 String xAuthAccessType = "read";
 String xAuthMode = "device";
 RequestToken expectedRequestToken = new RequestTokenImpl();
 expectedRequestToken.setXAuthAccessType(xAuthAccessType);
 expectedRequestToken.setXAuthMode(xAuthMode);
 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken(null, xAuthAccessType, xAuthMode);
 assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
 assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
 assertEquals(xAuthAccessType, actualRequestToken.getXAuthAccessType());
 assertEquals(xAuthMode, actualRequestToken.getXAuthMode());
 }

 @Test
 public void testGetOAuthAccessToken() throws Exception {
 AccessToken expectedAccessToken = new AccessTokenImpl("accessToken", "accessTokenSecret");
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
 assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 assertEquals(expectedAccessToken.getScreenName(), twitterBaseImpl.getScreenName());
 assertEquals(expectedAccessToken.getId(), twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws Exception {
 String oAuthVerifier = "oauthVerifier";
 AccessToken expectedAccessToken = new AccessTokenImpl("accessToken", "accessTokenSecret");
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken(oAuthVerifier);
 assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
 assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 assertEquals(expectedAccessToken.getScreenName(), twitterBaseImpl.getScreenName());
 assertEquals(expectedAccessToken.getId(), twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestToken() throws Exception {
 RequestToken requestToken = new RequestTokenImpl();
 AccessToken expectedAccessToken = new AccessTokenImpl("accessToken", "accessTokenSecret");
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
 assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
 assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 assertEquals(expectedAccessToken.getScreenName(), twitterBaseImpl.getScreenName());
 assertEquals(expectedAccessToken.getId(), twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestTokenAndOAuthVerifier() throws Exception {
 RequestToken requestToken = new RequestTokenImpl();
 String oAuthVerifier = "oauthVerifier";
 AccessToken expectedAccessToken = new AccessTokenImpl("accessToken", "accessTokenSecret");
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, oAuthVerifier);
 assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
 assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 assertEquals(expectedAccessToken.getScreenName(), twitterBaseImpl.getScreenName());
 assertEquals(expectedAccessToken.getId(), twitterBaseImpl.getId());
 }

 @Test
 public void testSetOAuthAccessToken() throws Exception {
 AccessToken accessToken = new AccessTokenImpl("accessToken", "accessTokenSecret");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(accessToken.getToken(), actualAccessToken.getToken());
 assertEquals(accessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws Exception {
 String screenName = "testUser";
 String password = "testPassword";
 AccessToken expectedAccessToken = new AccessTokenImpl("accessToken", "accessTokenSecret");
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken(screenName, password);
 assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
 assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 assertEquals(screenName, twitterBaseImpl.getScreenName());
 assertEquals(expectedAccessToken.getId(), twitterBaseImpl.getId());
 }

 @Test
 public void testGetOAuth2Token() throws Exception {
 OAuth2Token expectedOAuth2Token = new OAuth2TokenImpl();
 OAuth2Token actualOAuth2Token = twitterBaseImpl.getOAuth2Token();
 assertEquals(expectedOAuth2Token.getTokenType(), actualOAuth2Token.getTokenType());
 assertEquals(expectedOAuth2Token.getAccessToken(), actualOAuth2Token.getAccessToken());
 assertEquals(expectedOAuth2Token.getTokenSecret(), actualOAuth2Token.getTokenSecret());
 }

 @Test
 public void testSetOAuth2Token() throws Exception {
 OAuth2Token oAuth2Token = new OAuth2TokenImpl();
 twitterBaseImpl.setOAuth2Token(oAuth2Token);
 OAuth2Token actualOAuth2Token = twitterBaseImpl.getOAuth2Token();
 assertEquals(oAuth2Token.getTokenType(), actualOAuth2Token.getTokenType());
 assertEquals(oAuth2Token.getAccessToken(), actualOAuth2Token.getAccessToken());
 assertEquals(oAuth2Token.getTokenSecret(), actualOAuth2Token.getTokenSecret());
 }

 @Test
 public void testInvalidateOAuth2Token() throws Exception {
 twitterBaseImpl.invalidateOAuth2Token();
 }

}