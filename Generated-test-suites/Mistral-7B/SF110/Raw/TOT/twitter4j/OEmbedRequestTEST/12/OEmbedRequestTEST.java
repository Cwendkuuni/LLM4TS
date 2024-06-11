
package twitter4j;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(100);
        int expectedMaxWidth = 100;
        int actualMaxWidth = request.getMaxWidth();
        assertEquals(expectedMaxWidth, actualMaxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setHideMedia(true);
        boolean expectedHideMedia = true;
        boolean actualHideMedia = request.isHideMedia();
        assertEquals(expectedHideMedia, actualHideMedia);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(100);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setOmitScript(false);
        request.setAlign(Align.LEFT);
        String[] expectedRelated = {"related1", "related2"};
        request.setRelated(expectedRelated);
        request.setLang("en");
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("id", "1"),
                new HttpParameter("url", "http://example.com"),
                new HttpParameter("maxwidth", "100"),
                new HttpParameter("hide_media", "true"),
                new HttpParameter("hide_thread", "true"),
                new HttpParameter("omit_script", "false"),
                new HttpParameter("align", "left"),
                new HttpParameter("related", "related1,related2"),
                new HttpParameter("lang", "en")
        };
        HttpParameter[] actualParams = request.asHttpParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setHideThread(true);
        boolean expectedHideThread = true;
        boolean actualHideThread = request.isHideThread();
        assertEquals(expectedHideThread, actualHideThread);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setOmitScript(true);
        boolean expectedOmitScript = true;
        boolean actualOmitScript = request.isOmitScript();
        assertEquals(expectedOmitScript, actualOmitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        Align expectedAlign = Align.CENTER;
        request.setAlign(expectedAlign);
        Align actualAlign = request.getAlign();
        assertEquals(expectedAlign, actualAlign);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String[] expectedRelated = {"related1", "related2"};
        request.setRelated(expectedRelated);
        String[] actualRelated = request.getRelated();
        assertArrayEquals(expectedRelated, actualRelated);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expectedLang = "fr";
        request.setLang(expectedLang);
        String actualLang = request.getLang();
        assertEquals(expectedLang, actualLang);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        OEmbedRequest request = new OEmbedRequest(1L, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setLang(null);
    }
}