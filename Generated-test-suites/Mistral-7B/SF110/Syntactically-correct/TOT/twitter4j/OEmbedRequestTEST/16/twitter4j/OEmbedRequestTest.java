package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;
import static twitter4j.TestUtil.assertArrayEquals;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        // Test other fields with default values
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        int newMaxWidth = 200;
        oEmbedRequest.setMaxWidth(newMaxWidth);

        assertEquals(newMaxWidth, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");

        oEmbedRequest.setMaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expectedParams = {
                new HttpParameter("id", "1"),
                new HttpParameter("url", "https://example.com/status/1"),
                new HttpParameter("maxwidth", "200"),
                new HttpParameter("hide_media", "false"),
                new HttpParameter("hide_thread", "true"),
                new HttpParameter("omit_script", "true"),
                new HttpParameter("align", "right"),
                new HttpParameter("related", "related1,related2"),
                new HttpParameter("lang", "en")
        };

        HttpParameter[] actualParams = oEmbedRequest.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }
}