package test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;

 @Before
 public void setup() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret")
 .setOAuthAccessToken("accessToken")
 .setOAuthAccessTokenSecret("accessTokenSecret")
 .build();
 auth = new OAuthAuthorization(conf);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 String expectedScreenName = "expectedScreenName";
 twitterBaseImpl.setScreenName(expectedScreenName);
 String actualScreenName = twitterBaseImpl.getScreenName();
 assertEquals(expectedScreenName, actualScreenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getScreenName();
 fail("Should throw IllegalStateException when authorization is not enabled.");
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 long expectedId = 1L;
 twitterBaseImpl.fillInIDAndScreenName();
 long actualId = twitterBaseImpl.getId();
 assertEquals(expectedId, actualId);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuthorization() throws TwitterException, IllegalStateException {
 twitterBaseImpl.getId();
 fail("Should throw IllegalStateException when authorization is not enabled.");
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 assertTrue(twitterBaseImpl.rateLimitStatusListeners.contains(listener));
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(15, 15), true);
 Consumer<RateLimitStatusEvent> action = e -> assertEquals(event.getUser(), twitterBaseImpl);
 twitterBaseImpl.onRateLimitStatus(action);
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(15, 15), true);
 Consumer<RateLimitStatusEvent> action = e -> assertEquals(event.getUser(), twitterBaseImpl);
 twitterBaseImpl.onRateLimitReached(action);
 }

 @Test
 public void testHttpResponseReceived() throws TwitterException {
 HttpResponseEvent event = mock(HttpResponseEvent.class);
 twitterBaseImpl.httpResponseReceived(event);
 verify(event, times(2)).getTwitterException();
 }

 @Test
 public void testGetAuthorization() {
 Authorization expectedAuthorization = auth;
 Authorization actualAuthorization = twitterBaseImpl.getAuthorization();
 assertEquals(expectedAuthorization, actualAuthorization);
 }

 @Test
 public void testGetConfiguration() {
 Configuration expectedConfiguration = conf;
 Configuration actualConfiguration = twitterBaseImpl.getConfiguration();
 assertEquals(expectedConfiguration, actualConfiguration);
 }

 @Test
 public void testEnsureAuthorizationEnabled() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationEnabled();
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAuthorizationDisabled() throws TwitterException, IllegalStateException {
 twitterBaseImpl.ensureAuthorizationDisabled();
 fail("Should throw IllegalStateException when authorization is enabled.");
 }

 @Test
 public void testWriteObject() throws IOException {
 ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("output.txt"));
 twitterBaseImpl.writeObject(out);
 out.close();
 File file = new File("output.txt");
 assertTrue(file.exists());
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ObjectInputStream stream = new ObjectInputStream(new FileInputStream("output.txt"));
 TwitterBaseImpl readTwitterBaseImpl = (TwitterBaseImpl)stream.readObject();
 stream.close();
 assertEquals(twitterBaseImpl.getScreenName(), readTwitterBaseImpl.getScreenName());
 }

 @Test
 public void testSetOAuthConsumer() {
 String consumerKey = "consumerKey";
 String consumerSecret = "consumerSecret";
 twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
 Authorization actualAuthorization = twitterBaseImpl.getAuthorization();
 assertTrue(actualAuthorization.isOAuth());
 assertEquals(consumerKey, actualAuthorization.getConsumerKey());
 assertEquals(consumerSecret, actualAuthorization.getConsumerSecret());
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken expectedRequestToken = new RequestToken("oauth_token", "oauth_token_secret");
 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken();
 assertEquals(expectedRequestToken.getToken(), actualRequestToken.getToken());
 assertEquals(expectedRequestToken.getTokenSecret(), actualRequestToken.getTokenSecret());
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken expectedAccessToken = new AccessToken("accessToken", "accessTokenSecret", "userId", "screenName");
 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();
 assertEquals(expectedAccessToken.getToken(), actualAccessToken.getToken());
 assertEquals(expectedAccessToken.getTokenSecret(), actualAccessToken.getTokenSecret());
 assertEquals(expectedAccessToken.getUserId(), actualAccessToken.getUserId());
 assertEquals(expectedAccessToken.getScreenName(), actualAccessToken.getScreenName());
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("accessToken", "accessTokenSecret", 1L, "screenName");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertEquals(accessToken.getToken(), twitterBaseImpl.getOAuthAccessToken().getToken());
 assertEquals(accessToken.getTokenSecret(), twitterBaseImpl.getOAuthAccessToken().getTokenSecret());
 }
}