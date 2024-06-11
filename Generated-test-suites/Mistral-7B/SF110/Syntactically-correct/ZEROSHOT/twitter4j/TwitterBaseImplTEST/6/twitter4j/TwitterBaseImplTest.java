import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Consumer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TwitterBaseImpl.class })
public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;
 private HttpClient http;
 private List<RateLimitStatusListener> rateLimitStatusListeners;
 private ObjectFactory factory;

 @Before
 public void setUp() {
 conf = new ConfigurationBuilder()
 .setDebugEnabled(true)
 .setOAuthConsumerKey("consumerKey")
 .setOAuthConsumerSecret("consumerSecret")
 .setOAuthAccessToken("accessToken")
 .setOAuthAccessTokenSecret("accessTokenSecret")
 .build();
 auth = Mockito.mock(Authorization.class);
 http = Mockito.mock(HttpClient.class);
 rateLimitStatusListeners = new ArrayList<>();
 factory = Mockito.mock(ObjectFactory.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 PowerMockito.mockStatic(HttpClientFactory.class);
 when(HttpClientFactory.getInstance(any(Configuration.class))).thenReturn(http);
 }

 @Test
 public void testInit() throws Exception {
 // Test initialization with OAuth consumer key and secret
 TwitterBaseImpl twitterBaseImplWithOAuth = new TwitterBaseImpl(conf, auth);
 verify(auth, times(1)).setOAuthConsumer(eq("consumerKey"), eq("consumerSecret"));
 verify(http, times(1)).get(eq(twitterBaseImplWithOAuth.conf.getRestBaseURL() + "account/verify_credentials.json"), any(HttpParameter[].class), eq(auth), eq(factory), anyInt());

 // Test initialization with null credentials
 TwitterBaseImpl twitterBaseImplWithNullCredentials = new TwitterBaseImpl(conf, null);
 verify(auth, times(0)).setOAuthConsumer(anyString(), anyString());
 }

 @Test
 public void testGetScreenName() throws Exception {
 // Test getting screen name with BasicAuthorization
 PowerMockito.whenNew(BasicAuthorization.class).withArguments(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret()).thenReturn(auth);
 String screenName = twitterBaseImpl.getScreenName();
 assertEquals("screenName", screenName);

 // Test getting screen name with null BasicAuthorization
 PowerMockito.whenNew(BasicAuthorization.class).withArguments(anyString()).thenReturn(null);
 try {
 twitterBaseImpl.getScreenName();
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 assertTrue(e.getMessage().contains("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"));
 }
 }

 @Test
 public void testGetId() throws Exception {
 // Test getting id with BasicAuthorization
 PowerMockito.whenNew(BasicAuthorization.class).withArguments(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret()).thenReturn(auth);
 long id = twitterBaseImpl.getId();
 assertEquals(1L, id);

 // Test getting id with null BasicAuthorization
 PowerMockito.whenNew(BasicAuthorization.class).withArguments(anyString()).thenReturn(null);
 try {
 twitterBaseImpl.getId();
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 assertTrue(e.getMessage().contains("Neither user ID/password combination nor OAuth consumer key/secret combination supplied"));
 }
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = Mockito.mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listener);
 verify(rateLimitStatusListeners, times(1)).add(eq(listener));
 }

 @Test
 public void testOnRateLimitStatus() {
 Consumer<RateLimitStatusEvent> action = Mockito.mock(Consumer.class);
 RateLimitStatusEvent event = Mockito.mock(RateLimitStatusEvent.class);
 when(rateLimitStatusListeners.get(0)).then(i -> Mockito.mock(RateLimitStatusListener.class));
 twitterBaseImpl.onRateLimitStatus(action);
 verify(action, times(1)).accept(eq(event));
 }
}