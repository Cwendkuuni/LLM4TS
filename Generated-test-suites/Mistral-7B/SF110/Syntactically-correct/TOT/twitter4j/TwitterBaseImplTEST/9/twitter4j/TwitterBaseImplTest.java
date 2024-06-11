package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 private Configuration conf;
 private Authorization auth;
 private HttpClient http;
 private RateLimitStatusListener rateLimitStatusListener;

 @Before
 public void setup() {
 conf = new ConfigurationBuilder().build();
 auth = mock(Authorization.class);
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 http = mock(HttpClient.class);
 twitterBaseImpl.setHttp(http);
 rateLimitStatusListener = mock(RateLimitStatusListener.class);
 }

 @Test
 public void testGetScreenName_Success() throws TwitterException, IllegalStateException {
 // Given
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getInstance(OAuthAuthorization.class)).thenReturn(auth);
 when(auth.getInstance(OAuth2Authorization.class)).thenReturn(auth);
 when(auth.getInstance(NullAuthorization.class)).thenReturn(auth);
 when(auth.getInstance(BasicAuthorization.class)).thenReturn(auth);
 when(auth.getUserId()).thenReturn("testUser");
 when(auth.getScreenName()).thenReturn("TestUser");
 when(twitterBaseImpl.getAuthorization().getUserId()).thenReturn("testUser");
 when(twitterBaseImpl.getAuthorization().getScreenName()).thenReturn("TestUser");

 // When
 String screenName = twitterBaseImpl.getScreenName();

 // Then
 assertEquals("TestUser", screenName);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenName_NoCredentials() throws TwitterException, IllegalStateException {
 // Given
 when(auth.isEnabled()).thenReturn(false);

 // When & Then
 IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
 assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", exception.getMessage());
 }

 @Test
 public void testGetId_Success() throws TwitterException, IllegalStateException {
 // Given
 when(auth.isEnabled()).thenReturn(true);
 when(auth.getInstance(OAuthAuthorization.class)).thenReturn(auth);
 when(auth.getInstance(OAuth2Authorization.class)).thenReturn(auth);
 when(auth.getInstance(NullAuthorization.class)).thenReturn(auth);
 when(auth.getInstance(BasicAuthorization.class)).thenReturn(auth);
 when(auth.getUserId()).thenReturn("testUserID");
 when(twitterBaseImpl.getAuthorization().getUserId()).thenReturn("testUserID");

 // When
 long id = twitterBaseImpl.getId();

 // Then
 assertEquals("testUserID", id);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetId_NoCredentials() throws TwitterException, IllegalStateException {
 // Given
 when(auth.isEnabled()).thenReturn(false);

 // When & Then
 IllegalStateException exception = assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
 assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", exception.getMessage());
 }

 @Test
 public void testAddRateLimitStatusListener() {
 // Given

 // When
 twitterBaseImpl.addRateLimitStatusListener(rateLimitStatusListener);

 // Then
 verify(twitterBaseImpl).rateLimitStatusListeners.add(rateLimitStatusListener);
 }

 @Test
 public void testOnRateLimitStatus() {
 // Given
 RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);

 // When
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusListener);

 // Then
 verify(rateLimitStatusListener).onRateLimitStatus(statusEvent);
 }

 @Test
 public void testOnRateLimitReached() {
 // Given
 RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(twitterBaseImpl, null, true);

 // When
 twitterBaseImpl.onRateLimitReached(rateLimitStatusListener);

 // Then
 verify(rateLimitStatusListener).onRateLimitReached(statusEvent);
 }
}