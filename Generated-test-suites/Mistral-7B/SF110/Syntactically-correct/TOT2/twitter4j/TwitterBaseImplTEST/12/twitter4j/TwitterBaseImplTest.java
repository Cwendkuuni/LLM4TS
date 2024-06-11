package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.impl.BaseTest;
import twitter4j.impl.BaseTestImpl;
import twitter4j.util.function.Consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterBaseImplTest extends BaseTest {

 private TwitterBaseImpl twitterImpl;
 private Configuration conf;
 private Authorization auth;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 conf = new ConfigurationBuilder().setDebugEnabled(true).build();
 auth = new BasicAuthorization("testConsumerKey", "testConsumerSecret");
 twitterImpl = new TwitterBaseImpl(conf, auth);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 String screenName = twitterImpl.getScreenName();
 Assert.assertNotNull(screenName);
 }

 @Test
 public void testGetScreenNameNullAuth() throws TwitterException, IllegalStateException {
 twitterImpl.auth = null;
 try {
 twitterImpl.getScreenName();
 Assert.fail();
 } catch (IllegalStateException e) {
 Assert.assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 long id = twitterImpl.getId();
 Assert.assertEquals(0, id);
 }

 @Test
 public void testGetIdNullAuth() throws TwitterException, IllegalStateException {
 twitterImpl.auth = null;
 try {
 twitterImpl.getId();
 Assert.fail();
 } catch (IllegalStateException e) {
 Assert.assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterImpl.addRateLimitStatusListener(new RateLimitStatusListener() {
 @Override
 public void onRateLimitStatus(RateLimitStatusEvent event) {
 // handle event
 }

 @Override
 public void onRateLimitReached(RateLimitStatusEvent event) {
 // handle event
 }
 });
 }

 @Test
 public void testOnRateLimitReached() throws TwitterException {
 twitterImpl.onRateLimitReached(new Consumer<RateLimitStatusEvent>() {
 @Override
 public void accept(RateLimitStatusEvent event) {
 // handle event
 }
 });
 }

 @Test
 public void testHttpResponseReceived() throws IOException {
 HttpResponseEvent event = new HttpResponseEvent(new ByteArrayInputStream(new byte[0]), null, true, null);
 twitterImpl.httpResponseReceived(event);
 }

 @Test
 public void testHttpResponseReceivedWithRateLimitExceeded() throws IOException {
 HttpResponseEvent event = new HttpResponseEvent(new ByteArrayInputStream(new byte[0]), new TwitterException(420, "Rate limit exceeded"), true, null);
 twitterImpl.httpResponseReceived(event);
 }

 @Test
 public void testHttpResponseReceivedWithServerError() throws IOException {
 HttpResponseEvent event = new HttpResponseEvent(new ByteArrayInputStream(new byte[0]), new TwitterException(503, "Server Error"), true, null);
 twitterImpl.httpResponseReceived(event);
 }

 @Test
 public void testHttpResponseReceivedWithForbidden() throws IOException {
 HttpResponseEvent event = new HttpResponseEvent(new ByteArrayInputStream(new byte[0]), new TwitterException(403, "Forbidden"), true, null);
 twitterImpl.httpResponseReceived(event);
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken requestToken = twitterImpl.getOAuthRequestToken();
 Assert.assertNotNull(requestToken);
 }

 @Test
 public void testGetOAuthRequestTokenNullAuth() throws TwitterException {
 twitterImpl.auth = null;
 try {
 twitterImpl.getOAuthRequestToken();
 Assert.fail();
 } catch (TwitterException e) {
 Assert.assertEquals("OAuth consumer key/secret combination not supplied", e.getMessage());
 }
}

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = twitterImpl.getOAuthAccessToken();
 Assert.assertNotNull(accessToken);
 }

 @Test
 public void testGetOAuthAccessTokenNullAuth() throws TwitterException {
 twitterImpl.auth = null;
 try {
 twitterImpl.getOAuthAccessToken();
 Assert.fail();
 } catch (TwitterException e) {
 Assert.assertEquals("OAuth consumer key/secret combination not supplied", e.getMessage());
 }
 }

 @Test
 public void testGetOAuthAccessTokenWithVerifier() throws TwitterException {
 AccessToken accessToken = twitterImpl.getOAuthAccessToken("oauth_verifier");
 Assert.assertNotNull(accessToken);
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestToken() throws TwitterException {
 RequestToken requestToken = new RequestToken("testRequestToken");
 AccessToken accessToken = twitterImpl.getOAuthAccessToken(requestToken);
 Assert.assertNotNull(accessToken);
 }

 @Test
 public void testGetOAuthAccessTokenWithRequestTokenAndVerifier() throws TwitterException {
 RequestToken requestToken = new RequestToken("testRequestToken");
 AccessToken accessToken = twitterImpl.getOAuthAccessToken(requestToken, "oauth_verifier");
 Assert.assertNotNull(accessToken);
 }

 @Test
 public void testSetOAuthAccessToken() throws TwitterException {
 AccessToken accessToken = new AccessToken("testAccessToken", "testAccessTokenSecret", "testScreenName", 12345L);
 twitterImpl.setOAuthAccessToken(accessToken);
 }

 @Test
 public void testSetOAuthAccessTokenNullAuth() throws TwitterException {
 twitterImpl.auth = null;
 try {
 twitterImpl.setOAuthAccessToken(new AccessToken("testAccessToken", "testAccessTokenSecret", "testScreenName", 12345L));
 Assert.fail();
 } catch (TwitterException e) {
 Assert.assertEquals("Authentication credentials are missing", e.getMessage());
 }
}

 @Test
 public void testSetOAuthConsumer() throws TwitterException {
 twitterImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");
 }

 @Test
 public void testSetOAuthConsumerNullAuth() throws TwitterException {
 twitterImpl.auth = null;
 try {
 twitterImpl.setOAuthConsumer("testConsumerKey", "testConsumerSecret");
 Assert.fail();
 } catch (TwitterException e) {
 Assert.assertEquals("Authentication credentials are missing", e.getMessage());
 }
}

 @Test
 public void testGetConfiguration() {
 Configuration conf = twitterImpl.getConfiguration();
 Assert.assertEquals(this.conf, conf);
 }

 @Test
 public void testGetFactory() {
 JSONImplFactory factory = twitterImpl.getFactory();
 Assert.assertNotNull(factory);
 }

 @Test
 public void testGetAuthorization() {
 Authorization auth = twitterImpl.getAuthorization();
 Assert.assertNotNull(auth);
 }

 @Test
 public void testSetFactory() {
 twitterImpl.setFactory();
 JSONImplFactory factory = twitterImpl.getFactory();
 Assert.assertNotNull(factory);
 }

 @Test
 public void testFillInIDAndScreenName() throws TwitterException {
 User user = twitterImpl.fillInIDAndScreenName();
 Assert.assertNotNull(user);
 }

 @Test
 public void testFillInIDAndScreenNameNullAuth() throws TwitterException {
 twitterImpl.auth = null;
 try {
 twitterImpl.fillInIDAndScreenName();
 Assert.fail();
 } catch (TwitterException e) {
 Assert.assertEquals("Authentication credentials are missing", e.getMessage());
 }
}

 @Test
 public void testFillInIDAndScreenNameWithParameters() throws TwitterException {
 HttpParameter[] parameters = new HttpParameter[0];
 User user = twitterImpl.fillInIDAndScreenName(parameters);
 Assert.assertNotNull(user);
 }

 @Test
 public void testFillInIDAndScreenNameWithParametersNullAuth() throws TwitterException {
 twitterImpl.auth = null;
 try {
 twitterImpl.fillInIDAndScreenName(new HttpParameter[0]);
 Assert.fail();
 } catch (TwitterException e) {
 Assert.assertEquals("Authentication credentials are missing", e.getMessage());
 }
}

 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 twitterImpl.writeObject(oos);
 byte[] bytes = baos.toByteArray();
 TwitterBaseImpl twitterImpl2 = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 Assert.assertEquals(twitterImpl, twitterImpl2);
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(twitterImpl);
 byte[] bytes = baos.toByteArray();
 TwitterBaseImpl twitterImpl2 = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 Assert.assertEquals(twitterImpl, twitterImpl2);
 }

 @Test
 public void testWriteObjectNullAuth() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 twitterImpl.auth = null;
 oos.writeObject(twitterImpl);
 byte[] bytes = baos.toByteArray();
 TwitterBaseImpl twitterImpl2 = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 Assert.assertNotNull(twitterImpl2);
 Assert.assertNotEquals(twitterImpl, twitterImpl2);
}

 @Test
 public void testReadObjectNullAuth() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(new TwitterBaseImpl(conf, null));
 byte[] bytes = baos.toByteArray();
 TwitterBaseImpl twitterImpl2 = (TwitterBaseImpl) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 Assert.assertNotNull(twitterImpl2);
 Assert.assertNotEquals(twitterImpl, twitterImpl2);
}

 @After
 public void tearDown() {
 twitterImpl = null;
 conf = null;
 auth = null;
 }
}