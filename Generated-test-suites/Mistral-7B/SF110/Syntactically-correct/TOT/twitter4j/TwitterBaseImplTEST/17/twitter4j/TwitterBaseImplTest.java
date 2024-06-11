import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import twitter4j.*;
import twitter4j.conf.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;
 private HttpClient httpMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 conf = new ConfigurationBuilder().build();
 auth = mock(Authorization.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 httpMock = mock(HttpClient.class);
 when(twitterBaseImpl.getHttpClient()).thenReturn(httpMock);
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 // Test with valid basic authentication
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getUserId()).thenReturn("testUser");
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals("testUser", twitterBaseImpl.getScreenName());

 // Test with null screenName and basic authentication
 twitterBaseImpl.screenName = null;
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.screenName);

 // Test with invalid basic authentication
 when(auth.isEnabled()).thenReturn(false);
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());

 // Test with OAuth2 authentication
 doReturn(new OAuth2Token("tokenType", "accessToken")).when(auth).getOAuth2Token();
 twitterBaseImpl.auth = auth;
 assertEquals("testUser@testDomain", twitterBaseImpl.getScreenName());
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 // Test with valid basic authentication
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getUserId()).thenReturn(1L);
 twitterBaseImpl.fillInIDAndScreenName();
 assertEquals(1L, twitterBaseImpl.getId());

 // Test with null id and basic authentication
 twitterBaseImpl.id = 0L;
 twitterBaseImpl.fillInIDAndScreenName();
 assertNotNull(twitterBaseImpl.id);

 // Test with invalid basic authentication
 when(auth.isEnabled()).thenReturn(false);
 assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listenerMock = mock(RateLimitStatusListener.class);
 twitterBaseImpl.addRateLimitStatusListener(listenerMock);
 verify(listenerMock).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verifyNoMoreInteractions(listenerMock);
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent eventMock = mock(RateLimitStatusEvent.class);
 Consumer<RateLimitStatusEvent> actionMock = mock(Consumer.class);
 twitterBaseImpl.onRateLimitStatus(actionMock);
 verifyZeroInteractions(actionMock);

 RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusEvent);
 verify(actionMock).accept(eq(rateLimitStatusEvent));
 }
}