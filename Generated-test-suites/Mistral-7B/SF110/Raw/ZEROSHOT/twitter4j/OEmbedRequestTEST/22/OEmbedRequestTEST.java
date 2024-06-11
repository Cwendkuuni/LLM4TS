package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setup() {
        this.oEmbedRequest = new OEmbedRequest(1L, "http://test.com");
    }

    @Test
    public void testOEmbedRequestConstruction() {
        assertEquals(1L, this.oEmbedRequest.getStatusId());
        assertEquals("http://test.com", this.oEmbedRequest.getUrl());
    }

    @Test
    public void testSettersAndGetters() {
        this.oEmbedRequest.setMaxWidth(200);
        assertEquals(200, this.oEmbedRequest.getMaxWidth());

        this.oEmbedRequest.HideMedia(false);
        assertFalse(this.oEmbedRequest.isHideMedia());

        this.oEmbedRequest.HideThread(false);
        assertFalse(this.oEmbedRequest.isHideThread());

        this.oEmbedRequest.omitScript(true);
        assertTrue(this.oEmbedRequest.isOmitScript());

        this.oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, this.oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        this.oEmbedRequest.related(related);
        assertArrayEquals(related, this.oEmbedRequest.getRelated());

        this.oEmbedRequest.setLang("en");
        assertEquals("en", this.oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = {
                new HttpParameter("id", "1"),
                new HttpParameter("url", "http://test.com"),
                new HttpParameter("maxwidth", "200"),
                new HttpParameter("hide_media", "false"),
                new HttpParameter("hide_thread", "false"),
                new HttpParameter("omit_script", "true"),
                new HttpParameter("align", "right"),
                new HttpParameter("related", "related1,related2"),
                new HttpParameter("lang", "en")
        };

        this.oEmbedRequest.setMaxWidth(200);
        this.oEmbedRequest.HideMedia(false);
        this.oEmbedRequest.HideThread(false);
        this.oEmbedRequest.omitScript(true);
        this.oEmbedRequest.align(Align.RIGHT);
        this.oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        this.oEmbedRequest.setLang("en");

        HttpParameter[] actualParams = this.oEmbedRequest.asHttpParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://test.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://test.com");

        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://test.com");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://test2.com");

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://test.com");
        this.oEmbedRequest.setMaxWidth(200);

        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        assertTrue(oEmbedRequest1.hashCode() == oEmbedRequest2.hashCode());

        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest3.hashCode());

        assertFalse(oEmbedRequest1.equals(oEmbedRequest4));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest4.hashCode());

        assertFalse(oEmbedRequest1.equals(oEmbedRequest5));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest5.hashCode());

        assertTrue(this.oEmbedRequest.equals(this.oEmbedRequest));
        assertTrue(this.oEmbedRequest.hashCode() == this.oEmbedRequest.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("OEmbedRequest{statusId=1, url='http://test.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}", this.oEmbedRequest.toString());

        this.oEmbedRequest.setMaxWidth(200);
        this.oEmbedRequest.HideMedia(false);
        this.oEmbedRequest.HideThread(false);
        this.oEmbedRequest.omitScript(true);
        this.oEmbedRequest.align(Align.RIGHT);
        this.oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        this.oEmbedRequest.setLang("en");

        List<String> relatedList = Arrays.asList(this.oEmbedRequest.getRelated());
        assertEquals("OEmbedRequest{statusId=1, url='http://test.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=RIGHT, related=[related1, related2], lang='en'}", this.oEmbedRequest.toString());
    }
}