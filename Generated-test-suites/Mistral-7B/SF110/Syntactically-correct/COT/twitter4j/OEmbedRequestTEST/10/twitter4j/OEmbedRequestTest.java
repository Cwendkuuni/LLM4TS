import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.MaxWidth(200);
        assertEquals(200, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.HideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(false);
        oEmbedRequest.HideThread(true);
        assertTrue(oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.isOmitScript());

        oEmbedRequest.setAlign(Align.LEFT);
        oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.CENTER, oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"related3"});
        assertArrayEquals(new String[]{"related3"}, oEmbedRequest.getRelated());

        oEmbedRequest.setLang("en");
        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.MaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.HideMedia(true);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.HideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.omitScript(false);
        oEmbedRequest.setAlign(Align.LEFT);
        oEmbedRequest.align(Align.CENTER);
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"related3"});
        oEmbedRequest.setLang("en");
        oEmbedRequest.lang("fr");

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(12, httpParameters.length);
        assertEquals("id", httpParameters[0].getName());
        assertEquals("1", httpParameters[0].getValue());
        assertEquals("url", httpParameters[1].getName());
        assertEquals("https://example.com", httpParameters[1].getValue());
        assertEquals("maxwidth", httpParameters[2].getName());
        assertEquals("200", httpParameters[2].getValue());
        assertEquals("hide_media", httpParameters[3].getName());
        assertEquals("true", httpParameters[3].getValue());
        assertEquals("hide_thread", httpParameters[4].getName());
        assertEquals("true", httpParameters[4].getValue());
        assertEquals("omit_script", httpParameters[5].getName());
        assertEquals("false", httpParameters[5].getValue());
        assertEquals("align", httpParameters[6].getName());
        assertEquals("center", httpParameters[6].getValue());
        if (oEmbedRequest.getRelated().length > 0) {
            assertEquals("related", httpParameters[7].getName());
            assertEquals("related1,related2", httpParameters[7].getValue());
        } else {
            assertEquals(7, httpParameters.length);
        }
        assertEquals("lang", httpParameters[8].getName());
        assertEquals("fr", httpParameters[8].getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(-1);
        oEmbedRequest.MaxWidth(200);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, null);
    }
}