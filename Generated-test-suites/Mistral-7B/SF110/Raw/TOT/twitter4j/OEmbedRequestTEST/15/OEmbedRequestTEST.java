import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertNull(oEmbedRequest.getMaxWidth());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");

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
        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, oEmbedRequest.getRelated());

        oEmbedRequest.setLang("en");
        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");

        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.LEFT);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();

        assertEquals(12, httpParameters.length);
        assertEquals("id", httpParameters[0].getName());
        assertEquals("1", httpParameters[0].getValue());
        assertEquals("url", httpParameters[1].getName());
        assertEquals("https://example.com/status/1", httpParameters[1].getValue());
        assertEquals("maxwidth", httpParameters[2].getName());
        assertEquals("100", httpParameters[2].getValue());
        assertEquals("hide_media", httpParameters[3].getName());
        assertEquals("false", httpParameters[3].getValue());
        assertEquals("hide_thread", httpParameters[4].getName());
        assertEquals("true", httpParameters[4].getValue());
        assertEquals("omit_script", httpParameters[5].getName());
        assertEquals("true", httpParameters[5].getValue());
        assertEquals("align", httpParameters[6].getName());
        assertEquals("left", httpParameters[6].getValue());
        assertEquals("related", httpParameters[7].getName());
        assertEquals("related1,related2", httpParameters[7].getValue());
        assertEquals("lang", httpParameters[8].getName());
        assertEquals("en", httpParameters[8].getValue());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "https://example.com/status/2");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "https://example.com/status/1");

        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest4));
    }
}