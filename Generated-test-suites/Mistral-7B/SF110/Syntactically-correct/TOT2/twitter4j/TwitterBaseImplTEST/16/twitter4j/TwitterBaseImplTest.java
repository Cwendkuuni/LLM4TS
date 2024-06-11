package twitter4j.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.auth.NullAuthorization;

public class TwitterBaseImplTest {

private TwitterBaseImpl twitterBaseImpl;
private Configuration conf;

@Before
public void setUp() {
conf = new Configuration();
twitterBaseImpl = new TwitterBaseImpl(conf, NullAuthorization.getInstance());
}

@Test
public void testGetScreenName() throws TwitterException {
Assert.assertNull(twitterBaseImpl.getScreenName());
twitterBaseImpl.addRateLimitStatusListener(e -> {});
twitterBaseImpl.fillInIDAndScreenName();
Assert.assertEquals("test_screen_name", twitterBaseImpl.getScreenName());
}

@Test
public void testGetId() throws TwitterException {
Assert.assertEquals(0L, twitterBaseImpl.getId());
twitterBaseImpl.fillInIDAndScreenName();
Assert.assertEquals(1234567890L, twitterBaseImpl.getId());
}

@Test
public void testAddRateLimitStatusListener() {
twitterBaseImpl.addRateLimitStatusListener(e -> {});
RateLimitStatusEvent event = new RateLimitStatusEvent(twitterBaseImpl, null, true);
twitterBaseImpl.httpResponseReceived(new HttpResponseEvent(null, null, null, null, null, null, event));
twitterBaseImpl.onRateLimitStatus(event);
twitterBaseImpl.onRateLimitReached(event);
Assert.assertTrue(e.wasCalled());
}

@Test(expected = IllegalArgumentException.class)
public void testAddNullRateLimitStatusListener() {
twitterBaseImpl.addRateLimitStatusListener(null);
}

@Test
public void testInitialization() {
twitterBaseImpl = new TwitterBaseImpl(conf, NullAuthorization.getInstance());
Assert.assertNull(twitterBaseImpl.getScreenName());
Assert.assertEquals(0L, twitterBaseImpl.getId());
Assert.assertNull(twitterBaseImpl.getAuthorization());
Assert.assertNotNull(twitterBaseImpl.getConfiguration());
Assert.assertNotNull(twitterBaseImpl.getHttpClient());
Assert.assertEquals(0, twitterBaseImpl.getRateLimitStatusListeners().size());
}

@Test(expected = IllegalStateException.class)
public void testIllegalStateException() {
twitterBaseImpl = new TwitterBaseImpl(conf, NullAuthorization.getInstance());
twitterBaseImpl.addRateLimitStatusListener(e -> {});
twitterBaseImpl.setAuthorization(null);
twitterBaseImpl.getScreenName();
}

@Test
public void testSerialization() throws IOException, ClassNotFoundException {
ByteArrayOutputStream baos = new ByteArrayOutputStream();
ObjectOutputStream oos = new ObjectOutputStream(baos);
oos.writeObject(twitterBaseImpl);
ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
TwitterBaseImpl twitterBaseImplDeserialized = (TwitterBaseImpl) ois.readObject();
Assert.assertEquals(twitterBaseImpl.getScreenName(), twitterBaseImplDeserialized.getScreenName());
Assert.assertEquals(twitterBaseImpl.getId(), twitterBaseImplDeserialized.getId());
Assert.assertEquals(twitterBaseImpl.getConfiguration(), twitterBaseImplDeserialized.getConfiguration());
Assert.assertEquals(twitterBaseImpl.getAuthorization(), twitterBaseImplDeserialized.getAuthorization());
Assert.assertEquals(twitterBaseImpl.getHttpClient(), twitterBaseImplDeserialized.getHttpClient());
Assert.assertEquals(twitterBaseImpl.getRateLimitStatusListeners().size(), twitterBaseImplDeserialized.getRateLimitStatusListeners().size());
}

@Test
public void testSetAuthorization() {
twitterBaseImpl = new TwitterBaseImpl(conf, NullAuthorization.getInstance());
Assert.assertNull(twitterBaseImpl.getAuthorization());
twitterBaseImpl.setAuthorization(new NullAuthorization());
Assert.assertEquals(twitterBaseImpl.getAuthorization(), twitterBaseImpl.getAuthorization());
twitterBaseImpl.setAuthorization(new OAuthAuthorization(conf));
Assert.assertNotNull(twitterBaseImpl.getAuthorization());
Assert.assertEquals(twitterBaseImpl.getAuthorization().getClass(), OAuthAuthorization.class);
twitterBaseImpl.setAuthorization(new OAuth2Authorization(conf));
Assert.assertNotNull(twitterBaseImpl.getAuthorization());
Assert.assertEquals(twitterBaseImpl.getAuthorization().getClass(), OAuth2Authorization.class);
}
}