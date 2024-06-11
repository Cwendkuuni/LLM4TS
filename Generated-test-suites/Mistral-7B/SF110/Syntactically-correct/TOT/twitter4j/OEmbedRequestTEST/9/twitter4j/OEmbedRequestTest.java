package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;
import twitter4j.Align;
import static twitter4j.util.StringUtil.join;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertEquals(new String[0], oEmbedRequest.getRelated());
        assertEquals(null, oEmbedRequest.getLang());
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com/status/1");
        int maxWidth = 300;
        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());
        request.MaxWidth(maxWidth + 50);
        assertEquals(maxWidth + 50, request.getMaxWidth());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com/status/1");
        request.setMaxWidth(300);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        String lang = "en";
        request.setLang(lang);

        HttpParameter[] expectedParams = {
                new HttpParameter("id", String.valueOf(request.getStatusId())),
                new HttpParameter("url", request.getUrl()),
                new HttpParameter("maxwidth", String.valueOf(request.getMaxWidth())),
                new HttpParameter("hide_media", String.valueOf(request.isHideMedia())),
                new HttpParameter("hide_thread", String.valueOf(request.isHideThread())),
                new HttpParameter("omit_script", String.valueOf(request.isOmitScript())),
                new HttpParameter("align", Align.LEFT.name().toLowerCase()),
                new HttpParameter("related", join(related)),
                new HttpParameter("lang", lang)
        };

        HttpParameter[] actualParams = request.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }
}