import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Align;
import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;
import twitter4j.TwitterException;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        long statusId = 1L;
        String url = "https://example.com";
        this.oEmbedRequest = new OEmbedRequest(statusId, url);
    }

    @After
    public void tearDown() {
        this.oEmbedRequest = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.oEmbedRequest);
        assertEquals(1L, this.oEmbedRequest.getStatusId());
        assertEquals(url, this.oEmbedRequest.getUrl());
        assertTrue(this.oEmbedRequest.isHideMedia());
        assertTrue(this.oEmbedRequest.isHideThread());
        assertFalse(this.oEmbedRequest.isOmitScript());
        assertNull(this.oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], this.oEmbedRequest.getRelated());
        assertNull(this.oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        this.oEmbedRequest.setMaxWidth(300);
        assertEquals(300, this.oEmbedRequest.getMaxWidth());

        this.oEmbedRequest.HideMedia(false);
        assertFalse(this.oEmbedRequest.isHideMedia());

        this.oEmbedRequest.HideThread(false);
        assertFalse(this.oEmbedRequest.isHideThread());

        this.oEmbedRequest.omitScript(true);
        assertTrue(this.oEmbedRequest.isOmitScript());

        this.oEmbedRequest.align(Align.LEFT);
        assertEquals(Align.LEFT, this.oEmbedRequest.getAlign());

        String[] related = new String[]{"first", "second", "third"};
        this.oEmbedRequest.setRelated(related);
        assertArrayEquals(related, this.oEmbedRequest.getRelated());

        this.oEmbedRequest.setLang("en");
        assertEquals("en", this.oEmbedRequest.getLang());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        this.oEmbedRequest.setMaxWidth(-1);
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParameters = new HttpParameter[]{
                new HttpParameter("id", String.valueOf(1L)),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", "300"),
                new HttpParameter("hide_media", "false"),
                new HttpParameter("hide_thread", "false"),
                new HttpParameter("omit_script", "true"),
                new HttpParameter("align", Align.LEFT.name().toLowerCase()),
                new HttpParameter("related", "first,second,third"),
                new HttpParameter("lang", "en")
        };

        this.oEmbedRequest.setMaxWidth(300);
        this.oEmbedRequest.HideMedia(false);
        this.oEmbedRequest.HideThread(false);
        this.oEmbedRequest.omitScript(true);
        this.oEmbedRequest.align(Align.LEFT);
        this.oEmbedRequest.setRelated(new String[]{"first", "second", "third"});
        this.oEmbedRequest.setLang("en");

        HttpParameter[] actualParameters = this.oEmbedRequest.asHttpParameterArray();

        assertArrayEquals(expectedParameters, actualParameters);
    }

    @Test(expected = TwitterException.class)
    public void testEquals() throws TwitterException {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example1.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example2.com");

        assertNotSame(oEmbedRequest1, oEmbedRequest2);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));

        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(1L, "https://example1.com");
        assertTrue(oEmbedRequest1.equals(oEmbedRequest3));

        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(2L, "https://example1.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest4));

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest5.setMaxWidth(300);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest5));

        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest6.setMaxWidth(300);
        oEmbedRequest6.setHideMedia(false);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest6));

        OEmbedRequest oEmbedRequest7 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest7.setMaxWidth(300);
        oEmbedRequest7.setHideMedia(false);
        oEmbedRequest7.setHideThread(false);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest7));

        OEmbedRequest oEmbedRequest8 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest8.setMaxWidth(300);
        oEmbedRequest8.setHideMedia(false);
        oEmbedRequest8.setHideThread(false);
        oEmbedRequest8.setOmitScript(true);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest8));

        OEmbedRequest oEmbedRequest9 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest9.setMaxWidth(300);
        oEmbedRequest9.setHideMedia(false);
        oEmbedRequest9.setHideThread(false);
        oEmbedRequest9.setOmitScript(true);
        oEmbedRequest9.setAlign(Align.LEFT);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest9));

        OEmbedRequest oEmbedRequest10 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest10.setMaxWidth(300);
        oEmbedRequest10.setHideMedia(false);
        oEmbedRequest10.setHideThread(false);
        oEmbedRequest10.setOmitScript(true);
        oEmbedRequest10.setAlign(Align.LEFT);
        oEmbedRequest10.setRelated(new String[]{"first", "second"});
        assertFalse(oEmbedRequest1.equals(oEmbedRequest10));

        OEmbedRequest oEmbedRequest11 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest11.setMaxWidth(300);
        oEmbedRequest11.setHideMedia(false);
        oEmbedRequest11.setHideThread(false);
        oEmbedRequest11.setOmitScript(true);
        oEmbedRequest11.setAlign(Align.LEFT);
        oEmbedRequest11.setRelated(new String[]{"first", "second"});
        oEmbedRequest11.setLang("en");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest11));

        OEmbedRequest oEmbedRequest12 = new OEmbedRequest(1L, "https://example1.com");
        oEmbedRequest12.setMaxWidth(300);
        oEmbedRequest12.setHideMedia(false);
        oEmbedRequest12.setHideThread(false);
        oEmbedRequest12.setOmitScript(true);
        oEmbedRequest12.setAlign(Align.LEFT);
        oEmbedRequest12.setRelated(new String[]{"first", "second"});
        oEmbedRequest12.setLang("en");
        oEmbedRequest12.setUrl("https://example2.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest12));
    }

    @Test
    public void testHashCode() {
        long statusId = 1L;
        String url = "https://example.com";
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = {"first", "second", "third"};
        String lang = "en";

        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        int hashCode1 = oEmbedRequest1.hashCode();

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
        oEmbedRequest2.setMaxWidth(maxWidth);
        int hashCode2 = oEmbedRequest2.hashCode();
        assertEquals(hashCode1, hashCode2);

        oEmbedRequest2.setHideMedia(hideMedia);
        int hashCode3 = oEmbedRequest2.hashCode();
        assertEquals(hashCode2, hashCode3);

        oEmbedRequest2.setHideThread(hideThread);
        int hashCode4 = oEmbedRequest2.hashCode();
        assertEquals(hashCode3, hashCode4);

        oEmbedRequest2.setOmitScript(omitScript);
        int hashCode5 = oEmbedRequest2.hashCode();
        assertEquals(hashCode4, hashCode5);

        oEmbedRequest2.setAlign(align);
        int hashCode6 = oEmbedRequest2.hashCode();
        assertEquals(hashCode5, hashCode6);

        oEmbedRequest2.setRelated(related);
        int hashCode7 = oEmbedRequest2.hashCode();
        assertEquals(hashCode6, hashCode7);

        oEmbedRequest2.setLang(lang);
        int hashCode8 = oEmbedRequest2.hashCode();
        assertEquals(hashCode7, hashCode8);
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com";
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String[] related = {"first", "second", "third"};
        String lang = "en";

        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(maxWidth);
        oEmbedRequest.HideMedia(hideMedia);
        oEmbedRequest.HideThread(hideThread);
        oEmbedRequest.omitScript(omitScript);
        oEmbedRequest.align(align);
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang(lang);

        String expectedString = "OEmbedRequest{statusId=1, url='https://example.com', maxWidth=300, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[first, second, third], lang='en'}";
        String actualString = oEmbedRequest.toString();

        assertEquals(expectedString, actualString);
    }
}