package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("https://example.com", request.getUrl());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.isHideMedia());
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.isHideThread());
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.isOmitScript());
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        String[] related = new String[]{"test1", "test2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.setRelated(null);
        assertNull(request.getRelated());

        request.setRelated(new String[0]);
        assertEquals(0, request.getRelated().length);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.getLang());

        request.setLang(null);
        assertNull(request.getLang());
    }
}