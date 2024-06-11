package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.conf.*;
import twitter4j.util.function.*;
import java.util.*;
import java.io.*;
import twitter4j.auth.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        String screenName = twitterBaseImpl.getScreenName();
        assertNull(screenName);

        twitterBaseImpl.fillInIDAndScreenName();
        screenName = twitterBaseImpl.getScreenName();
        assertNotNull(screenName);
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        long id = twitterBaseImpl.getId();
        assertEquals(0, id);

        twitterBaseImpl.fillInIDAndScreenName();
        id = twitterBaseImpl.getId();
        assertNotEquals(0, id);
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.fillInIDAndScreenName();
        assertNotNull(twitterBaseImpl.getScreenName());
        assertNotEquals(0, twitterBaseImpl.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameWhenAuthorizationNotEnabled() throws TwitterException, IllegalStateException {
        Configuration conf = new Configuration();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, null);
        twitterBaseImpl.getScreenName();
    }

    @Test
    public void testGetConfiguration() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        assertEquals(conf, twitterBaseImpl.getConfiguration());
    }

    @Test
    public void testSetOAuthConsumer() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        String consumerKey = "consumerKey";
        String consumerSecret = "consumerSecret";
        twitterBaseImpl.setOAuthConsumer(consumerKey, consumerSecret);
        assertNotNull(twitterBaseImpl.getAuthorization());
        assertEquals(consumerKey, twitterBaseImpl.getAuthorization().getConsumerKey());
        assertEquals(consumerSecret, twitterBaseImpl.getAuthorization().getConsumerSecret());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOAuthConsumerWithNullConsumerKey() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setOAuthConsumer(null, "consumerSecret");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOAuthConsumerWithNullConsumerSecret() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        twitterBaseImpl.setOAuthConsumer("consumerKey", null);
    }

    @Test
    public void testGetOAuthRequestToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        assertNotNull(requestToken);
    }

    @Test
    public void testGetOAuthAccessToken() throws TwitterException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        RequestToken requestToken = twitterBaseImpl.getOAuthRequestToken();
        AccessToken accessToken = twitterBaseImpl.getOAuthAccessToken(requestToken);
        assertNotNull(accessToken);
    }

    @Test
    public void testSetOAuthAccessToken() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        AccessToken accessToken = new AccessToken("accessToken", "accessTokenSecret");
        twitterBaseImpl.setOAuthAccessToken(accessToken);
        assertEquals(accessToken, twitterBaseImpl.getOAuthAccessToken());
    }

    @Test
    public void testEquals() {
        Configuration conf1 = new Configuration();
        Authorization auth1 = new OAuthAuthorization(conf1);
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf1, auth1);

        Configuration conf2 = new Configuration();
        Authorization auth2 = new OAuthAuthorization(conf2);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf2, auth2);

        assertEquals(twitterBaseImpl1, twitterBaseImpl1);
        assertEquals(twitterBaseImpl1, twitterBaseImpl1);

        assertEquals(twitterBaseImpl1, twitterBaseImpl2);
        assertEquals(twitterBaseImpl2, twitterBaseImpl1);

        twitterBaseImpl1.fillInIDAndScreenName();
        twitterBaseImpl2.fillInIDAndScreenName();
        assertNotEquals(twitterBaseImpl1, twitterBaseImpl2);

        Configuration conf3 = new Configuration();
        Authorization auth3 = new OAuthAuthorization(conf3);
        TwitterBaseImpl twitterBaseImpl3 = new TwitterBaseImpl(conf3, auth3);
        assertNotEquals(twitterBaseImpl1, twitterBaseImpl3);

        assertNotEquals(twitterBaseImpl1, new Object());
    }

    @Test
    public void testHashCode() {
        Configuration conf1 = new Configuration();
        Authorization auth1 = new OAuthAuthorization(conf1);
        TwitterBaseImpl twitterBaseImpl1 = new TwitterBaseImpl(conf1, auth1);

        Configuration conf2 = new Configuration();
        Authorization auth2 = new OAuthAuthorization(conf2);
        TwitterBaseImpl twitterBaseImpl2 = new TwitterBaseImpl(conf2, auth2);

        int hashCode1 = twitterBaseImpl1.hashCode();
        int hashCode2 = twitterBaseImpl1.hashCode();

        int hashCode3 = twitterBaseImpl1.hashCode();
        int hashCode4 = twitterBaseImpl2.hashCode();

        twitterBaseImpl1.fillInIDAndScreenName();
        int hashCode5 = twitterBaseImpl1.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);
        String expected = "TwitterBase{conf=Configuration{}, http=null, rateLimitStatusListeners=[], auth=OAuthAuthorization{oAuthConsumer={consumerKey='', consumerSecret=''}, oAuthAccessToken=null, token='', tokenSecret=''}}";
        String actual = twitterBaseImpl.toString();
        assertEquals(expected, actual);

        twitterBaseImpl.fillInIDAndScreenName();
        expected = "TwitterBase{conf=Configuration{}, http=null, rateLimitStatusListeners=[], auth=OAuthAuthorization{oAuthConsumer={consumerKey='consumerKey', consumerSecret='consumerSecret'}, oAuthAccessToken=AccessToken{screenName='screenName', id=1}, token='', tokenSecret=''}}";
        twitterBaseImpl.setId(1);
        twitterBaseImpl.setScreenName("screenName");
        assertEquals(expected, twitterBaseImpl.toString());
    }

    @Test
    public void testWriteObject() throws IOException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        twitterBaseImpl.writeObject(objectOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)objectInputStream.readObject();

        assertEquals(twitterBaseImpl, twitterBaseImpl2);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        twitterBaseImpl.writeObject(objectOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TwitterBaseImpl twitterBaseImpl2 = (TwitterBaseImpl)objectInputStream.readObject();

        assertEquals(twitterBaseImpl, twitterBaseImpl2);
    }
}