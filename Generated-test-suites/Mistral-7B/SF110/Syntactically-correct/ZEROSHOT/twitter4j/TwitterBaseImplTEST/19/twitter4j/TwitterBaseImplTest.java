package test;

import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Test;

public class TwitterBaseImplTest {

private TwitterBaseImpl twitterBaseImpl;
private Configuration conf;
private Authorization auth;

@Before
public void setUp() {
conf = new Configuration();
auth = new BasicAuthorization("testConsumerKey", "testConsumerSecret");
twitterBaseImpl = new TwitterBaseImpl(conf, auth);
}

@Test
public void testGetScreenName() throws TwitterException, IllegalStateException {
// Test when screenName is not initialized
assertNull(twitterBaseImpl.getScreenName());

// Test when screenName is initialized
twitterBaseImpl.fillInIDAndScreenName();
assertNotNull(twitterBaseImpl.getScreenName());
assertEquals("testScreenName", twitterBaseImpl.getScreenName());
}

@Test
public void testGetId() throws TwitterException, IllegalStateException {
// Test when id is not initialized
assertNull(twitterBaseImpl.getId());

// Test when id is initialized
twitterBaseImpl.fillInIDAndScreenName();
assertNotNull(twitterBaseImpl.getId());
assertEquals(1L, twitterBaseImpl.getId());
}

@Test
public void testFillInIDAndScreenName() throws TwitterException {
// Test with null parameters
twitterBaseImpl.fillInIDAndScreenName(null);

// Test with valid parameters
HttpParameter[] parameters = new HttpParameter[0];
twitterBaseImpl.fillInIDAndScreenName(parameters);
assertNotNull(twitterBaseImpl.getScreenName());
assertNotNull(twitterBaseImpl.getId());
}

@Test
public void testAddRateLimitStatusListener() {
RateLimitStatusListener listener = new RateLimitStatusListener() {
@Override
public void onRateLimitStatus(final RateLimitStatusEvent event) {
fail("Should not be called");
}
};
twitterBaseImpl.addRateLimitStatusListener(listener);
assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
}

@Test
public void testOnRateLimitStatus() {
Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
@Override
public void accept(RateLimitStatusEvent event) {
fail("Should not be called");
}
};
twitterBaseImpl.onRateLimitStatus(action);
assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
}

@Test
public void testOnRateLimitReached() {
Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
@Override
public void accept(RateLimitStatusEvent event) {
fail("Should not be called");
}
};
twitterBaseImpl.onRateLimitReached(action);
assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
}

@Test
public void testHttpResponseReceived() throws Exception {
HttpResponseEvent event = new HttpResponseEvent(null, null, null, null, null, null, null, null, null);
twitterBaseImpl.httpResponseReceived(event);
assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
}

@Test
public void testGetAuthorization() {
assertEquals(auth, twitterBaseImpl.getAuthorization());
}

@Test
public void testGetConfiguration() {
assertEquals(conf, twitterBaseImpl.getConfiguration());
}

@Test(expected = IllegalStateException.class)
public void testEnsureAuthorizationEnabled() throws Exception {
twitterBaseImpl.ensureAuthorizationEnabled();
fail("Should throw IllegalStateException");
}

@Test(expected = IllegalStateException.class)
public void testEnsureOAuthEnabled() throws Exception {
twitterBaseImpl.ensureOAuthEnabled();
fail("Should throw IllegalStateException");
}

@Test
public void testWriteObject() throws IOException {
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
twitterBaseImpl.writeObject(objectOutputStream);
objectOutputStream.close();
byte[] byteArray = byteArrayOutputStream.toByteArray();
twitterBaseImpl = (TwitterBaseImpl)new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
assertEquals(twitterBaseImpl.getScreenName(), "testScreenName");
assertEquals(twitterBaseImpl.getId(), 1L);
}

@Test
public void testReadObject() throws IOException, ClassNotFoundException {
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
objectOutputStream.writeObject(twitterBaseImpl);
objectOutputStream.close();
byte[] byteArray = byteArrayOutputStream.toByteArray();
twitterBaseImpl = (TwitterBaseImpl)new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
assertEquals(twitterBaseImpl.getScreenName(), "testScreenName");
assertEquals(twitterBaseImpl.getId(), 1L);
}

@Test
public void testSetOAuthConsumer() {
twitterBaseImpl.setOAuthConsumer("newConsumerKey", "newConsumerSecret");
assertEquals(auth.getConsumerKey(), "newConsumerKey");
assertEquals(auth.getConsumerSecret(), "newConsumerSecret");
}

@Test
public void testGetOAuthRequestToken() throws TwitterException {
RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
assertNotNull(requestToken.getToken());
assertNotNull(requestToken.getTokenSecret());
}

@Test
public void testGetOAuthRequestTokenCallbackUrl() throws TwitterException {
String callbackUrl = "http://test.com";
RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken(callbackUrl);
assertEquals(callbackUrl, requestToken.getCallbackUrl());
}

@Test
public void testGetOAuthRequestTokenXAuthAccessType() throws TwitterException {
String xAuthAccessType = "read";
RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://test.com", xAuthAccessType);
assertEquals(xAuthAccessType, requestToken.getXAuthAccessType());
}

@Test
public void testGetOAuthRequestTokenXAuthMode() throws TwitterException {
String xAuthMode = "client_auth";
RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://test.com", null, xAuthMode);
assertEquals(xAuthMode, requestToken.getXAuthMode());
}

@Test
public void testGetOAuthAccessToken() throws TwitterException {
AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
assertNotNull(accessToken.getToken());
assertNotNull(accessToken.getTokenSecret());
assertEquals("testScreenName", accessToken.getScreenName());
assertEquals(1L, accessToken.getUserId());
}

@Test
public void testGetOAuthAccessTokenOAuthVerifier() throws TwitterException {
String oauthVerifier = "testOAuthVerifier";
AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(oauthVerifier);
assertEquals(oauthVerifier, accessToken.getOAuthVerifier());
assertEquals("testScreenName", accessToken.getScreenName());
assertEquals(1L, accessToken.getUserId());
}

@Test
public void testGetOAuthAccessTokenRequestToken() throws TwitterException {
RequestToken requestToken = new OAuthToken("testToken", "testTokenSecret");
AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
assertEquals(requestToken.getToken(), accessToken.getToken());
assertEquals(requestToken.getTokenSecret(), accessToken.getTokenSecret());
assertEquals("testScreenName", accessToken.getScreenName());
assertEquals(1L, accessToken.getUserId());
}

@Test
public void testGetOAuthAccessTokenRequestTokenOAuthVerifier() throws TwitterException {
RequestToken requestToken = new OAuthToken("testToken", "testTokenSecret");
String oauthVerifier = "testOAuthVerifier";
AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, oauthVerifier);
assertEquals(requestToken.getToken(), accessToken.getToken());
assertEquals(requestToken.getTokenSecret(), accessToken.getTokenSecret());
assertEquals(oauthVerifier, accessToken.getOAuthVerifier());
assertEquals("testScreenName", accessToken.getScreenName());
assertEquals(1L, accessToken.getUserId());
}

@Test
public void testSetOAuthAccessToken() throws TwitterException {
AccessToken accessToken = new OAuthToken("testToken", "testTokenSecret");
twitterBaseImpl.setOAuthAccessToken(accessToken);
assertEquals(accessToken.getToken(), twitterBaseImpl.getOAuthAccessToken().getToken());
assertEquals(accessToken.getTokenSecret(), twitterBaseImpl.getOAuthAccessToken().getTokenSecret());
assertEquals("testScreenName", twitterBaseImpl.getOAuthAccessToken().getScreenName());
assertEquals(1L, twitterBaseImpl.getOAuthAccessToken().getUserId());
}

@Test
public void testGetOAuthAccessTokenScreenNamePassword() throws TwitterException {
String screenName = "testScreenName";
String password = "testPassword";
AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(screenName, password);
assertEquals(screenName, accessToken.getScreenName());
assertEquals(password, twitterBaseImpl.getOAuthAccessToken().getPassword());
assertEquals(1L, accessToken.getUserId());
}

@Test
public void testGetOAuth2Token() throws TwitterException {
OAuth2Token oauth2Token = twitterBaseImpl.getOAuth2Token();
assertNotNull(oauth2Token.getTokenType());
assertNotNull(oauth2Token.getAccessToken());
assertNotNull(oauth2Token.getAccessTokenSecret());
}

@Test
public void testSetOAuth2Token() throws TwitterException {
OAuth2Token oauth2Token = new OAuth2Token("testTokenType", "testAccessToken", "testAccessTokenSecret");
twitterBaseImpl.setOAuth2Token(oauth2Token);
assertEquals(oauth2Token.getTokenType(), twitterBaseImpl.getOAuth2Token().getTokenType());
assertEquals(oauth2Token.getAccessToken(), twitterBaseImpl.getOAuth2Token().getAccessToken());
assertEquals(oauth2Token.getAccessTokenSecret(), twitterBaseImpl.getOAuth2Token().getAccessTokenSecret());
}

@Test
public void testInvalidateOAuth2Token() throws TwitterException {
twitterBaseImpl.invalidateOAuth2Token();
assertNull(twitterBaseImpl.getOAuth2Token());
}
}