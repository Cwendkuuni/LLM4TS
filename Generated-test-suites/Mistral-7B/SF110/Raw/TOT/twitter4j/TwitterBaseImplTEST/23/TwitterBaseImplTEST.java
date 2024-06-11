package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.io.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterBaseImplTest {

 private TwitterBaseImpl twitterBaseImpl;
 @Mock
 private Configuration conf;
 @Mock
 private Authorization auth;
 @Mock
 private HttpClient httpClient;
 @Mock
 private ObjectFactory factory;
 @Mock
 private RateLimitStatusListener listener;
 @Captor
 private ArgumentCaptor<RateLimitStatusEvent> rateLimitStatusEventCaptor;

 @Before
 public void setUp() {
 twitterBaseImpl = new TwitterBaseImpl(conf, auth);
 Mockito.when(conf.getHttpClientConfiguration()).thenReturn(new HttpClientConfigurationImpl());
 Mockito.when(conf.getOAuthConsumerKey()).thenReturn("consumerKey");
 Mockito.when(conf.getOAuthConsumerSecret()).thenReturn("consumerSecret");
 Mockito.when(conf.isApplicationOnlyAuthEnabled()).thenReturn(false);
 Mockito.when(auth.isEnabled()).thenReturn(true);
 Mockito.when(auth.getInstance()).thenReturn(auth);
 Mockito.when(auth.getType()).thenReturn(AuthType.OAuth);
 Mockito.when(auth.getOAuthToken()).thenReturn(new AccessToken("accessToken", "accessTokenSecret"));
 Mockito.when(auth.getOAuthAccessToken()).thenReturn(new AccessToken("accessToken", "accessTokenSecret"));
 Mockito.when(auth.getOAuthAccessToken(Mockito.anyString())).thenReturn(new AccessToken("accessToken", "accessTokenSecret"));
 Mockito.when(auth.getOAuthAccessToken(Mockito.any(RequestToken.class), Mockito.anyString())).thenReturn(new AccessToken("accessToken", "accessTokenSecret"));
 Mockito.when(auth.getOAuthAccessToken(Mockito.any(RequestToken.class), Mockito.anyString(), Mockito.anyString())).thenReturn(new AccessToken("accessToken", "accessTokenSecret"));
 Mockito.when(auth.setOAuthAccessToken(Mockito.any(AccessToken.class))).thenReturn(voidVal);
 Mockito.when(conf.getRestBaseURL()).thenReturn("http://twitter.com/api");
 Mockito.when(factory.getJSONParser()).thenReturn(new JSONParser());
 twitterBaseImpl.setFactory();
 twitterBaseImpl.addRateLimitStatusListener(listener);
 }

 @Test
 public void testGetScreenName() {
 Mockito.when(auth.getInstance()).thenReturn(new OAuthAuthorization(conf));
 Mockito.when(auth.getType()).thenReturn(AuthType.OAuth);
 Mockito.when(auth.getOAuthAccessToken()).thenReturn(null);
 Mockito.when(auth.getUserId()).thenReturn("testUser");
 twitterBaseImpl.fillInIDAndScreenName();
 Mockito.verify(listener).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
 Mockito.verifyNoMoreInteractions(listener);
 Mockito.verifyNoMoreInteractions(httpClient);
 RateLimitStatus rateLimitStatus = rateLimitStatusEventCaptor.getValue().getRateLimitStatus();
 assertEquals("testUser", twitterBaseImpl.getScreenName());
 assertEquals(RateLimitStatus.LIMIT_NONE, rateLimitStatus);
 }

 @Test
 public void testGetScreenNameWithOAuthAccessToken() {
 Mockito.when(auth.getInstance()).thenReturn(new OAuthAuthorization(conf));
 Mockito.when(auth.getType()).thenReturn(AuthType.OAuth);
 Mockito.when(auth.getOAuthAccessToken()).thenReturn(new AccessToken("accessToken", "accessTokenSecret"));
 Mockito.when(auth.getUserId()).thenReturn("testUser");
 twitterBaseImpl.fillInIDAndScreenName();
 Mockito.verify(listener).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
 Mockito.verifyNoMoreInteractions(listener);
 Mockito.verifyNoMoreInteractions(httpClient);
 RateLimitStatus rateLimitStatus = rateLimitStatusEventCaptor.getValue().getRateLimitStatus();
 assertEquals("testUser", twitterBaseImpl.getScreenName());
 assertEquals(RateLimitStatus.LIMIT_NONE, rateLimitStatus);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScreenNameWithoutAuth() {
 twitterBaseImpl.getScreenName();
 }

 @Test
 public void testGetId() {
 Mockito.when(auth.getInstance()).thenReturn(new OAuthAuthorization(conf));
 Mockito.when(auth.getType()).thenReturn(AuthType.OAuth);
 Mockito.when(auth.getOAuthAccessToken()).thenReturn(null);
 Mockito.when(auth.getUserId()).thenReturn("testUser");
 twitterBaseImpl.fillInIDAndScreenName();
 Mockito.verify(listener).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
 Mockito.verifyNoMoreInteractions(listener);
 Mockito.verifyNoMoreInteractions(httpClient);
 RateLimitStatus rateLimitStatus = rateLimitStatusEventCaptor.getValue().getRateLimitStatus();
 assertEquals("testUser", twitterBaseImpl.getScreenName());
 assertEquals(RateLimitStatus.LIMIT_NONE, rateLimitStatus);
 assertEquals(1L, twitterBaseImpl.getId());
 }

 @Test
 public void testGetIdWithOAuthAccessToken() {
 Mockito.when(auth.getInstance()).thenReturn(new OAuthAuthorization(conf));
 Mockito.when(auth.getType()).thenReturn(AuthType.OAuth);
 Mockito.when(auth.getOAuthAccessToken()).thenReturn(new AccessToken("accessToken", "accessTokenSecret"));
 Mockito.when(auth.getUserId()).thenReturn("testUser");
 twitterBaseImpl.fillInIDAndScreenName();
 Mockito.verify(listener).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
 Mockito.verifyNoMoreInteractions(listener);
 Mockito.verifyNoMoreInteractions(httpClient);
 RateLimitStatus rateLimitStatus = rateLimitStatusEventCaptor.getValue().getRateLimitStatus();
 assertEquals("testUser", twitterBaseImpl.getScreenName());
 assertEquals(RateLimitStatus.LIMIT_NONE, rateLimitStatus);
 assertEquals(1L, twitterBaseImpl.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdWithoutAuth() {
 twitterBaseImpl.getId();
 }

 @Test
 public void testAddRateLimitStatusListener() {
 twitterBaseImpl.addRateLimitStatusListener(listener);
 twitterBaseImpl.onRateLimitStatus(new RateLimitStatusEvent(twitterBaseImpl, new RateLimitStatus(1, 5, 60)));
 Mockito.verify(listener).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
 Mockito.verify(listener).onRateLimitReached(rateLimitStatusEventCaptor.capture());
 RateLimitStatus rateLimitStatus = rateLimitStatusEventCaptor.getValue().getRateLimitStatus();
 assertEquals(1, rateLimitStatus.getLimit());
 assertEquals(5, rateLimitStatus.getRemaining());
 assertEquals(60, rateLimitStatus.getResetTime());
 }

 @Test
 public void testOnRateLimitStatus() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 5, 60);
 RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
 twitterBaseImpl.onRateLimitStatus(rateLimitStatusEvent);
 Mockito.verify(listener).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
 Mockito.verify(listener).onRateLimitReached(rateLimitStatusEventCaptor.capture());
 RateLimitStatus rateLimitStatus2 = rateLimitStatusEventCaptor.getValue().getRateLimitStatus();
 assertEquals(1, rateLimitStatus2.getLimit());
 assertEquals(5, rateLimitStatus2.getRemaining());
 assertEquals(60, rateLimitStatus2.getResetTime());
 }

 @Test
 public void testOnRateLimitReached() {
 RateLimitStatus rateLimitStatus = new RateLimitStatus(1, 0, 60);
 RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);
 twitterBaseImpl.onRateLimitReached(rateLimitStatusEvent);
 Mockito.verify(listener).onRateLimitStatus(rateLimitStatusEventCaptor.capture());
 Mockito.verify(listener).onRateLimitReached(rateLimitStatusEventCaptor.capture());
 RateLimitStatus rateLimitStatus2 = rateLimitStatusEventCaptor.getValue().getRateLimitStatus();
 assertEquals(1, rateLimitStatus2.getLimit());
 assertEquals(0, rateLimitStatus2.getRemaining());
 assertEquals(60, rateLimitStatus2.getResetTime());
 }
}