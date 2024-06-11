package twitter4j;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;
    private Configuration configuration;
    private Authorization authorization;
    private HttpClient httpClientMock;
    private OAuthSupport oAuthSupportMock;

    @Before
    public void setUp() {
        configuration = new Configuration();
        authorization = Mockito.mock(Authorization.class);
        httpClientMock = Mockito.mock(HttpClient.class);
        oAuthSupportMock = Mockito.mock(OAuthSupport.class);

        twitterBaseImpl = new TwitterBaseImpl(configuration, authorization);
        twitterBaseImpl.init();
        twitterBaseImpl.setFactory();
        twitterBaseImpl.setHttpClient(httpClientMock);
        twitterBaseImpl.setOAuthSupport(oAuthSupportMock);
    }

    @Test
    public void testGetScreenName() {
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(authorization.getInstanceType()).thenReturn(InstanceType.AUTHENTICATED);
        Mockito.when(oAuthSupportMock.getOAuthAccessToken()).thenReturn(new AccessToken("1234567890", "abcdefghijklmnopqrstuvwxyz1234567890"));

        String screenName = twitterBaseImpl.getScreenName();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz1234567890", screenName);

        Mockito.verify(oAuthSupportMock, Mockito.times(1)).getOAuthAccessToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWithoutAuthorization() {
        Mockito.when(authorization.isEnabled()).thenReturn(false);

        twitterBaseImpl.getScreenName();

        Mockito.verifyNoInteractions(oAuthSupportMock);
        Assert.assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getScreenName());
    }

    @Test
    public void testGetId() {
        Mockito.when(authorization.isEnabled()).thenReturn(true);
        Mockito.when(oAuthSupportMock.getOAuthAccessToken()).thenReturn(new AccessToken("1234567890", "abcdefghijklmnopqrstuvwxyz1234567890"));

        long id = twitterBaseImpl.getId();
        Assert.assertEquals(1234567890L, id);

        Mockito.verify(oAuthSupportMock, Mockito.times(1)).getOAuthAccessToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIdWithoutAuthorization() {
        Mockito.when(authorization.isEnabled()).thenReturn(false);

        twitterBaseImpl.getId();

        Mockito.verifyNoInteractions(oAuthSupportMock);
        Assert.assertThrows(IllegalStateException.class, () -> twitterBaseImpl.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();

        twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));

        ArgumentCaptor<RateLimitStatusListener> listenerArgumentCaptor = ArgumentCaptor.forClass(RateLimitStatusListener.class);
        Mockito.verify(twitterBaseImpl).addListener(listenerArgumentCaptor.capture());
        rateLimitStatusListeners.add(listenerArgumentCaptor.getValue());

        Assert.assertEquals(1, twitterBaseImpl.getRateLimitStatusListeners().size());
        Assert.assertEquals(rateLimitStatusListeners.get(0), twitterBaseImpl.getRateLimitStatusListeners().get(0));
    }

    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(httpClientMock, 200, null);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(5, 10, 15);

        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);

        twitterBaseImpl.onRateLimitStatus(Mockito.mock(Consumer.class));
        twitterBaseImpl.onRateLimitReached(Mockito.mock(Consumer.class));

        Mockito.verify(twitterBaseImpl, Mockito.times(2)).onRateLimitStatus(rateLimitStatusEvent);

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        Mockito.verify(twitterBaseImpl, Mockito.times(1)).onRateLimitStatus(rateLimitStatusEvent);
    }

    @Test
    public void testHttpResponseReceivedWithRateLimitReached() {
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(httpClientMock, 429, null);
        RateLimitStatus rateLimitStatus = new RateLimitStatus(5, 10, 15);

        RateLimitStatusEvent rateLimitStatusEvent = new RateLimitStatusEvent(twitterBaseImpl, rateLimitStatus, true);

        List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<>();

        twitterBaseImpl.onRateLimitStatus(Mockito.mock(Consumer.class));
        twitterBaseImpl.onRateLimitReached(Mockito.mock(Consumer.class));

        Mockito.verify(twitterBaseImpl, Mockito.times(2)).onRateLimitStatus(rateLimitStatusEvent);

        twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));
        twitterBaseImpl.addRateLimitStatusListener(Mockito.mock(RateLimitStatusListener.class));

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        Mockito.verify(twitterBaseImpl, Mockito.times(1)).onRateLimitReached(rateLimitStatusEvent);
        Mockito.verify(twitterBaseImpl.getRateLimitStatusListeners(), Mockito.times(2)).onRateLimitStatus(rateLimitStatusEvent);
    }

    @Test
    public void testHttpResponseReceivedWithNoRateLimitStatus() {
        HttpResponseEvent httpResponseEvent = new HttpResponseEvent(httpClientMock, 200, null);

        twitterBaseImpl.httpResponseReceived(httpResponseEvent);

        Mockito.verifyNoInteractions(twitterBaseImpl.getRateLimitStatusListeners());
    }
}