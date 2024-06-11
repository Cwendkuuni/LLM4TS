package twitter4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        assertEquals(1L, oEmbedRequest.getStatusId());
        assertEquals("https://example.com", oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertNull(oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testMaxWidthAndAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(120);
        oEmbedRequest.setAlign(Align.LEFT);
        assertEquals(120, oEmbedRequest.getMaxWidth());
        assertEquals(Align.LEFT, oEmbedRequest.getAlign());
        OEmbedRequest oEmbedRequest2 = oEmbedRequest.MaxWidth(200).align(Align.CENTER);
        assertEquals(200, oEmbedRequest2.getMaxWidth());
        assertEquals(Align.CENTER, oEmbedRequest2.getAlign());
        assertNotSame(oEmbedRequest, oEmbedRequest2);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());
        oEmbedRequest.HideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.isHideThread());
        oEmbedRequest.HideThread(false);
        assertFalse(oEmbedRequest.isHideThread());
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
        oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.isOmitScript());
    }

    @Test
    public void testSetRelated() {
        String[] related = {"Test1", "Test2", "Test3"};
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
        oEmbedRequest.related(new String[]{"Test4", "Test5"});
        String[] expectedRelated = { "Test1", "Test2", "Test3", "Test4", "Test5" };
        assertArrayEquals(expectedRelated, oEmbedRequest.getRelated());
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());
        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com");
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "https://example.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "https://example.com/test");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest4));
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest5.setHideMedia(false);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest5));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com");
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "https://example.com");
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest3.hashCode());
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "https://example.com/test");
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest4.hashCode());
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest5.setHideMedia(false);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest5.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        assertEquals("OEmbedRequest{statusId=1, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", oEmbedRequest.toString());
    }

    @Test
    public void testAlignEnum() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        assertEquals(Align.LEFT, oEmbedRequest.getAlign());
        assertEquals("left", oEmbedRequest.getParameter("align").getValue());
    }

    @Test
    public void testEmptyRelatedArrayParameter() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        assertArrayEquals(new HttpParameter[12], httpParameters);
        assertNull(httpParameters[10]);
        assertNull(httpParameters[11]);

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest2.setRelated(new String[0]);
        HttpParameter[] httpParameters2 = oEmbedRequest2.asHttpParameterArray();
        assertArrayEquals(new HttpParameter[12], httpParameters2);
        assertNull(httpParameters2[10]);
        assertNull(httpParameters2[11]);
    }

    @Test
    public void testRelatedArrayParameter() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        String[] related = {"Test1", "Test2"};
        oEmbedRequest.setRelated(related);
        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        List<String> relatedList = Arrays.asList(related);
        assertArrayEquals(new HttpParameter[13], httpParameters);
        assertEquals("related", httpParameters[10].getName());
        assertEquals(StringUtil.join(relatedList, ","), httpParameters[10].getValue());
    }

    @Test
    public void testSetStatusIdNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> new OEmbedRequest(-1L, "https://example.com"));
    }

    @Test
    public void testSetStatusIdNull() {
        assertThrows(NullPointerException.class, () -> new OEmbedRequest(null, "https://example.com"));
    }

    @Test
    public void testSetUrlNull() {
        assertThrows(NullPointerException.class, () -> new OEmbedRequest(1L, null));
    }

    @Test
    public void testSetUrlEmptyString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "");
        assertEquals("", oEmbedRequest.getUrl());
    }

    @Test
    public void testSetMaxWidthNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> new OEmbedRequest(1L, "https://example.com").setMaxWidth(-1));
    }

    @Test
    public void testSetMaxWidthZero() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(0);
        assertEquals(0, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testSetMaxWidthNull() {
        assertThrows(NullPointerException.class, () -> new OEmbedRequest(1L, "https://example.com").setMaxWidth(null));
    }

    @Test
    public void testSetHideMediaNull() {
        assertThrows(NullPointerException.class, () -> new OEmbedRequest(1L, "https://example.com").setHideMedia(null));
    }

    @Test
    public void testSetHideThreadNull() {
        assertThrows(NullPointerException.class, () -> new OEmbedRequest(1L, "https://example.com").setHideThread(null));
    }

    @Test
    public void testSetOmitScriptNull() {
        assertThrows(NullPointerException.class, () -> new OEmbedRequest(1L, "https://example.com").setOmitScript(null));
    }

    @Test
    public void testSetLangNull() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        assertNull(oEmbedRequest.getLang());
        oEmbedRequest.setLang(null);
        assertEquals(null, oEmbedRequest.getLang());
    }

    @Test
    public void testSetRelatedNull() {
        assertThrows(NullPointerException.class, () -> new OEmbedRequest(1L, "https://example.com").setRelated(null));
    }
}