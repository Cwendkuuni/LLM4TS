package twitter4j.test;

import static org.junit.Assert.*;

import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import org.junit.Before;
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
 assertNull(twitterBaseImpl.getScreenName());
 twitterBaseImpl.getScreenName();
 assertNotNull(twitterBaseImpl.getScreenName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuthorization() throws IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 assertEQ(0L, twitterBaseImpl.getId());
 twitterBaseImpl.getId();
 assertNotNull(twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuthorization() throws IllegalStateException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.getId();
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 User user = twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(user);
 assertNotNull(user.getScreenName());
 assertNotNull(user.getId());
 }

 @Test(expected = TwitterException.class)
 public void testFillInIDAndScreenNameWithoutAuthorization() throws TwitterException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 twitterBaseImpl.fillInIDAndScreenName();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 fail("Should not be called");
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 fail("Should not be called");
 }
 });
 }

 @Test
 public void testOnRateLimitStatus() {
 twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(1, 2), false));
 }

 @Test
 public void testOnRateLimitReached() {
 twitterBaseImpl.onRateLimitReached(new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(1, 2), false));
 }

 @Test
 public void testHttpResponseReceived() {
 HttpResponseEvent event = new HttpResponseEvent(null, null, null, null, null, null, null, null, null);
 twitterBaseImpl.httpResponseReceived(event);
 }

 @Test
 public void testGetAuthorization() {
 assertSame(auth, twitterBaseImpl.getAuthorization());
 }

 @Test
 public void testGetConfiguration() {
 assertSame(conf, twitterBaseImpl.getConfiguration());
 }

 @Test
 public void testEnsureAuthorizationEnabled() {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 try {
 twitterBaseImpl.ensureAuthorizationEnabled();
 fail("Should have thrown IllegalStateException");
 }
 catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testEnsureOAuthEnabled() {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 try {
 twitterBaseImpl.ensureOAuthEnabled();
 fail("Should have thrown IllegalStateException");
 }
 catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testWriteObject() throws IOException {
 ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
 twitterBaseImpl.writeObject(out);
 out.close();
 byte[] bytes = ((ByteArrayOutputStream)out.sync()).toByteArray();
 TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 assertSame(twitterBaseImpl, twitterBaseImpl2);
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(new byte[]{}));
 try {
 twitterBaseImpl = (TwitterBaseImpl)in.readObject();
 fail("Should have thrown IOException");
 }
 catch (IOException e) {
 // Expected
 }
 twitterBaseImpl = (TwitterBaseImpl)in.readObject();
 assertSame(twitterBaseImpl, twitterBaseImpl);
 }

 @Test
 public void testSetOAuthConsumer() {
 twitterBaseImpl.setOAuthConsumer("consumerKey", "consumerSecret");
 assertSame("consumerKey", twitterBaseImpl.getAuthorization().getConsumerKey());
 assertSame("consumerSecret", twitterBaseImpl.getAuthorization().getConsumerSecret());
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
 assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthRequestTokenWithCallbackUrl() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("http://test.com/callback");
 assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthRequestTokenWithXAuthAccessType() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("read write", "XAuthAccessType");
 assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthRequestTokenWithXAuthAccessTypeAndXAuthMode() throws TwitterException {
 RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken("read write", "XAuthAccessType", "XAuthMode");
 assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken();
 assertNotNull(accessToken);
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("oauthVerifier");
 assertNotNull(accessToken);
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestToken() throws TwitterException {
 RequestToken requestToken = new RequestToken("requestTokenOAuth");
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
 assertNotNull(accessToken);
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestTokenAndOAuthVerifier() throws TwitterException {
 RequestToken requestToken = new RequestToken("requestTokenOAuth");
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken, "oauthVerifier");
 assertNotNull(accessToken);
 }

 @Test
 public void testSetOAuthAccessToken() {
 AccessToken accessToken = new AccessToken("accessToken");
 twitterBaseImpl.setOAuthAccessToken(accessToken);
 assertSame(accessToken, twitterBaseImpl.getAuthorization().getAccessToken());
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken("testScreenName", "testPassword");
 assertNotNull(accessToken);
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuthAccessTokenWithoutOAuthSupport() throws TwitterException {
 twitterBaseImpl = new TwitterBaseImpl(conf, null);
 try {
 twitterBaseImpl.getOAuth().getOAuthAccessToken();
 fail("Should have thrown IllegalStateException");
 }
 catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test(expected = TwitterException.class)
 public void testGetOAuth2Token() throws TwitterException {
 OAuth2Token oauth2Token = twitterBaseImpl.getOAuth2Token();
 fail("Should have thrown IllegalStateException");
 }

 @Test
 public void testSetOAuth2Token() {
 OAuth2Token oauth2Token = new OAuth2Token("tokenType", "accessToken");
 twitterBaseImpl.setOAuth2Token(oauth2Token);
 assertSame(oauth2Token, twitterBaseImpl.getAuthorization().getOAuth2Token());
 }

 @Test
 public void testInvalidateOAuth2Token() throws TwitterException {
 twitterBaseImpl.invalidateOAuth2Token();
 assertNull(twitterBaseImpl.getAuthorization().getOAuth2Token());
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf, auth);
 assertTrue(twitterBaseImpl.equals(twitterBaseImpl2));
 }

 @Test
 public void testNotEquals() {
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf2, auth);
 assertFalse(twitterBaseImpl.equals(twitterBaseImpl2));
 }
}