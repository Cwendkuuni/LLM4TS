
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    @Test
    public void testConstructor() {
        // ... Previous test method ...
    }

    @Test
    public void testConstructorWithNullUrl() {
        // ... Previous test method ...
    }

    @Test
    public void testSetMaxWidth() {
        // ... Previous test method ...
    }

    @Test
    public void testMaxWidthMethod() {
        // ... Previous test method ...
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());
    }

    @Test
    public void testHideMediaMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest hideMediaRequest = oEmbedRequest.HideMedia(false);
        assertFalse(hideMediaRequest.isHideMedia());
        assertEquals(oEmbedRequest, hideMediaRequest);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());
    }

    @Test
    public void testHideThreadMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest hideThreadRequest = oEmbedRequest.HideThread(false);
        assertFalse(hideThreadRequest.isHideThread());
        assertEquals(oEmbedRequest, hideThreadRequest);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
    }

    @Test
    public void testOmitScriptMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest omitScriptRequest = oEmbedRequest.omitScript(true);
        assertTrue(omitScriptRequest.isOmitScript());
        assertEquals(oEmbedRequest, omitScriptRequest);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest alignRequest = oEmbedRequest.align(Align.LEFT);
        assertEquals(Align.LEFT, alignRequest.getAlign());
        assertEquals(oEmbedRequest, alignRequest);
    }

    @Test
    public void testAlignMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        Align align = Align.LEFT;
        OEmbedRequest alignRequest = oEmbedRequest.align(align);
        assertEquals(align, alignRequest.getAlign());
        assertEquals(oEmbedRequest, alignRequest);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
    }

    @Test
    public void testRelatedMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        String[] related = new String[]{"related1", "related2"};
        OEmbedRequest relatedRequest = oEmbedRequest.related(related);
        assertArrayEquals(related, relatedRequest.getRelated());
        assertEquals(oEmbedRequest, relatedRequest);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());
    }

    @Test
    public void testLangMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        String lang = "en";
        OEmbedRequest langRequest = oEmbedRequest.lang(lang);
        assertEquals(lang, langRequest.getLang());
        assertEquals(oEmbedRequest, langRequest);
    }

    @Test
    public void testNegativeMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com/status/1");
        Assertions.assertThrows(IllegalArgumentException.class, () -> oEmbedRequest.setMaxWidth(-1));
    }

    @Test
    public void testAsHttpParameterArray() {
        // ... Previous test method ...
    }
}
