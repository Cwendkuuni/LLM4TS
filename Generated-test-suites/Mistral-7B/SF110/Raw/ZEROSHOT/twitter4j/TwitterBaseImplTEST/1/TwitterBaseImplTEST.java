import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

 @Mock
 private Configuration configuration;

 @Mock
 private Authorization authorization;

 @Mock
 private HttpClient httpClient;

 @Mock
 private ObjectFactory objectFactory;

 @Mock
 private OAuthSupport oAuthSupport;

 @Mock
 private OAuth2Support oAuth2Support;

 @InjectMocks
 private TwitterBaseImpl twitterBaseImpl;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
 when(authorization.isEnabled()).thenReturn(true);
 when(httpClient.getInstance(any())).thenReturn(httpClient);
 when(objectFactory.getJSONImplFactory(configuration)).thenReturn(objectFactory);
 }

 @After
 public void tearDown() {
 twitterBaseImpl = null;
 }

 @Test
 public void testGetScreenName() throws TwitterException, IllegalStateException {
 String expectedScreenName = "testUser";
 when(authorization.getUserId()).thenReturn(expectedScreenName);

 String actualScreenName = twitterBaseImpl.getScreenName();

 assertEquals(expectedScreenName, actualScreenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWhenAuthorizationNotEnabled() throws TwitterException, IllegalStateException {
 when(authorization.isEnabled()).thenReturn(false);

 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() throws TwitterException, IllegalStateException {
 long expectedId = 1L;
 User user = mock(User.class);
 when(user.getId()).thenReturn(expectedId);
 when(user.getScreenName()).thenReturn("testUser");

 when(authorization.isEnabled()).thenReturn(true);
 when(authorization.getInstance(User.class)).thenReturn(user);

 long actualId = twitterBaseImpl.getId();

 assertEquals(expectedId, actualId);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWhenAuthorizationNotEnabled() throws TwitterException, IllegalStateException {
 when(authorization.isEnabled()).thenReturn(false);

 twitterBaseImpl.getId();
 }

 @Test
 public void testGetIdAndScreenName() throws TwitterException {
 String expectedScreenName = "testUser";
 long expectedId = 1L;
 User user = mock(User.class);
 when(user.getId()).thenReturn(expectedId);
 when(user.getScreenName()).thenReturn(expectedScreenName);
 when(authorization.getInstance(User.class)).thenReturn(user);

 User actualUser = twitterBaseImpl.fillInIDAndScreenName();

 assertEquals(expectedScreenName, actualUser.getScreenName());
 assertEquals(expectedId, actualUser.getId());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

 twitterBaseImpl.addRateLimitStatusListener(listener);

 verify(listener).onRateLimitStatus(any(RateLimitStatusEvent.class));
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

 twitterBaseImpl.onRateLimitStatus(event);
 twitterBaseImpl.onRateLimitStatus(event);
 twitterBaseImpl.onRateLimitStatus(event);

 verify(listener, times(3)).onRateLimitStatus(event);
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatusEvent event = mock(RateLimitStatusEvent.class);
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);

 twitterBaseImpl.onRateLimitReached(event);
 twitterBaseImpl.onRateLimitReached(event);
 twitterBaseImpl.onRateLimitReached(event);

 verify(listener, times(3)).onRateLimitReached(event);
 }

 @Test
 public void testHttpResponseReceived() {
 HttpResponseEvent event = mock(HttpResponseEvent.class);
 RateLimitStatusListener listener = mock(RateLimitStatusListener.class);
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 10, 15);

 twitterBaseImpl.onRateLimitStatus(event);
 twitterBaseImpl.onRateLimitReached(event);
 twitterBaseImpl.httpResponseReceived(event);

 verify(listener, atLeastOnce()).onRateLimitStatus(any(RateLimitStatusEvent.class));
 verify(listener, atLeastOnce()).onRateLimitReached(any(RateLimitStatusEvent.class));
 verify(listener).onRateLimitStatus(argThat(new IsRateLimitStatusMatchingExpected(rateLimitStatus)));
 }

 @Test
 public void testGetAuthorization() {
 Authorization expectedAuthorization = mock(Authorization.class);
 when(authorization.isEnabled()).thenReturn(true);

 Authorization actualAuthorization = twitterBaseImpl.getAuthorization();

 assertEquals(expectedAuthorization, actualAuthorization);
 }

 @Test
 public void testGetConfiguration() {
 Configuration expectedConfiguration = mock(Configuration.class);

 Configuration actualConfiguration = twitterBaseImpl.getConfiguration();

 assertEquals(expectedConfiguration, actualConfiguration);
 }

 @Test
 public void testEnsureAuthorizationEnabled() {
 twitterBaseImpl = spy(twitterBaseImpl);
 doThrow(new IllegalStateException("Authorization not enabled")).when(twitterBaseImpl).ensureAuthorizationEnabled();

 try {
 twitterBaseImpl.ensureAuthorizationEnabled();
 fail("Expected IllegalStateException to be thrown");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testEnsureOAuthEnabled() {
 twitterBaseImpl = spy(twitterBaseImpl);
 doThrow(new IllegalStateException("OAuth not enabled")).when(twitterBaseImpl).ensureOAuthEnabled();

 try {
 twitterBaseImpl.ensureOAuthEnabled();
 fail("Expected IllegalStateException to be thrown");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testSetOAuthConsumer() {
 String consumerKey = "testConsumerKey";
 String consumerSecret = "testConsumerSecret";

 twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);

 verify(oAuthSupport).setOAuthConsumer(consumerKey, consumerSecret);
 }

 @Test
 public void testGetOAuthRequestToken() throws TwitterException {
 RequestToken expectedRequestToken = mock(RequestToken.class);
 when(oAuthSupport.getOAuthRequestToken()).thenReturn(expectedRequestToken);

 RequestToken actualRequestToken = twitterBaseImpl.getOAuthRequestToken();

 assertEquals(expectedRequestToken, actualRequestToken);
 }

 @Test
 public void testGetOAuthAccessToken() throws TwitterException {
 AccessToken expectedAccessToken = mock(AccessToken.class);
 when(oAuthSupport.getOAuthAccessToken()).thenReturn(expectedAccessToken);

 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken();

 assertEquals(expectedAccessToken, actualAccessToken);
 }

 @Test
 public void testGetOAuthAccessTokenWithOAuthVerifier() throws TwitterException {
 AccessToken expectedAccessToken = mock(AccessToken.class);
 RequestToken requestToken = mock(RequestToken.class);
 when(oAuthSupport.getOAuthAccessToken(requestToken)).thenReturn(expectedAccessToken);
 when(requestToken.getToken()).thenReturn("testToken");
 when(requestToken.getTokenSecret()).thenReturn("testTokenSecret");

 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);

 assertEquals(expectedAccessToken, actualAccessToken);
 }

 @Test
 public void testSetOAuthAccessToken() throws TwitterException {
 AccessToken expectedAccessToken = mock(AccessToken.class);

 twitterBaseImpl.setOAuthAccessToken(expectedAccessToken);

 verify(oAuthSupport).setOAuthAccessToken(expectedAccessToken);
 }

 @Test
 public void testGetOAuthAccessTokenWithScreenNameAndPassword() throws TwitterException {
 AccessToken expectedAccessToken = mock(AccessToken.class);
 when(oAuthSupport.getOAuthAccessToken("testScreenName", "testPassword")).thenReturn(expectedAccessToken);

 AccessToken actualAccessToken = twitterBaseImpl.getOAuthAccessToken("testScreenName", "testPassword");

 assertEquals(expectedAccessToken, actualAccessToken);
 }

 @Test
 public void testGetOAuth2Token() throws TwitterException {
 OAuth2Token expectedOAuth2Token = mock(OAuth2Token.class);
 when(oAuth2Support.getOAuth2Token()).thenReturn(expectedOAuth2Token);

 OAuth2Token actualOAuth2Token = twitterBaseImpl.getOAuth2Token();

 assertEquals(expectedOAuth2Token, actualOAuth2Token);
 }

 @Test
 public void testSetOAuth2Token() throws TwitterException {
 OAuth2Token expectedOAuth2Token = mock(OAuth2Token.class);

 twitterBaseImpl.setOAuth2Token(expectedOAuth2Token);

 verify(oAuth2Support).setOAuth2Token(expectedOAuth2Token);
 }

 @Test
 public void testInvalidateOAuth2Token() throws TwitterException {
 twitterBaseImpl.invalidateOAuth2Token();

 verify(oAuth2Support).invalidateOAuth2Token();
 }

 @Test
 public void testEquals() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);

 assertTrue(twitterBaseImpl1.equals(twitterBaseImpl2));
 }

 @Test
 public void testNotEqualsDifferentInstance() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(configuration, authorization);

 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl3));
 }

 @Test
 public void testNotEqualsDifferentAuthorization() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, mock(Authorization.class));

 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
 }

 @Test
 public void testNotEqualsDifferentConfiguration() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration1, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration2, authorization);

 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
 }

 @Test
 public void testNotEqualsDifferentHttpClient() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, mock(HttpClient.class));

 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
 }

 @Test
 public void testNotEqualsDifferentRateLimitStatusListeners() {
 TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(configuration, authorization);
 TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(configuration, authorization);
 List<RateLimitStatusListener> rateLimitStatusListeners1 = Arrays.asList(mock(RateLimitStatusListener.class), mock(RateLimitStatusListener.class));
 List<RateLimitStatusListener> rateLimitStatusListeners2 = Arrays.asList(mock(RateLimitStatusListener.class), mock(RateLimitStatusListener.class), mock(RateLimitStatusListener.class));

 twitterBaseImpl1.setRateLimitStatusListeners(rateLimitStatusListeners1);
 twitterBaseImpl2.setRateLimitStatusListeners(rateLimitStatusListeners2);

 assertFalse(twitterBaseImpl1.equals(twitterBaseImpl2));
 }

 private static class IsRateLimitStatusMatchingExpected {
 private final RateLimitStatus expectedRateLimitStatus;

 IsRateLimitStatusMatchingExpected(RateLimitStatus expectedRateLimitStatus) {
 this.expectedRateLimitStatus = expectedRateLimitStatus;
 }

 @SuppressWarnings("unchecked")
 boolean apply(RateLimitStatus rateLimitStatus) {
 return this.expectedRateLimitStatus.getRateLimitStatus() == rateLimitStatus.getRateLimitStatus()
 && this.expectedRateLimitStatus.getRemaining() == rateLimitStatus.getRemaining()
 && this.expectedRateLimitStatus.getRetries() == rateLimitStatus.getRetries();
 }
 }
}