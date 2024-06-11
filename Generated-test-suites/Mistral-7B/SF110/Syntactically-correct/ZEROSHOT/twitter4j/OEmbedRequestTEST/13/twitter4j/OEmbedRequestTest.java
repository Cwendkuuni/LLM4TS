package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertFalse(oEmbedRequest.isHideMedia());
        assertFalse(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        int maxWidth = 42;
        oEmbedRequest.setMaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(56);
        assertEquals(56, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());

        oEmbedRequest.HideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());

        oEmbedRequest.HideThread(true);
        assertTrue(oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());

        oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.isOmitScript());

        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.getAlign());

        oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.CENTER, oEmbedRequest.getAlign());

        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, oEmbedRequest.getRelated());

        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        // Default parameters
        HttpParameter[] parameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(12, parameters.length);
        assertEquals(statusId, parameters[0].getName().equals("id") ? Long.parseLong(parameters[0].getValue()) : -1);
        assertEquals(url, parameters[1].getName().equals("url") ? parameters[1].getValue() : null);
        assertEquals(Integer.valueOf(0), parameters[2].getName().equals("maxwidth") ? parameters[2] : null);
        assertEquals("true", parameters[3].getName().equals("hide_media") ? parameters[3].getValue() : null);
        assertEquals("true", parameters[4].getName().equals("hide_thread") ? parameters[4].getValue() : null);
        assertEquals("false", parameters[5].getName().equals("omit_script") ? parameters[5].getValue() : null);
        assertEquals("none", parameters[6].getName().equals("align") ? parameters[6].getValue() : null);
        assertNull(parameters[7]);
        assertNull(parameters[8]);
        assertNull(parameters[9]);
        assertNull(parameters[10]);
        assertNull(parameters[11]);

        // Custom parameters
        oEmbedRequest.setMaxWidth(42);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.LEFT);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        String lang = "en";
        oEmbedRequest.setLang(lang);

        parameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(12, parameters.length);
        assertEquals(Long.valueOf(statusId), parameters[0]);
        assertEquals(url, parameters[1].getValue());
        assertEquals(Integer.valueOf(42), parameters[2]);
        assertEquals("false", parameters[3]);
        assertEquals("true", parameters[4]);
        assertEquals("true", parameters[5]);
        assertEquals("left", parameters[6]);
        assertEquals(Arrays.toString(related), parameters[7].getValue());
        assertEquals(lang, parameters[11].getValue());

        // Null parameters
        oEmbedRequest.setMaxWidth(null);
        oEmbedRequest.setRelated(null);
        oEmbedRequest.setLang(null);

        parameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(11, parameters.length);
        assertEquals(Long.valueOf(statusId), parameters[0]);
        assertEquals(url, parameters[1].getValue());
        assertEquals(null, parameters[2]);
        assertEquals("false", parameters[3]);
        assertEquals("true", parameters[4]);
        assertEquals("none", parameters[5]);
        assertNull(parameters[6]);
        assertNull(parameters[7]);
        assertNull(parameters[8]);
        assertNull(parameters[9]);
        assertNull(parameters[10]);
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "https://example.com";
        long statusId2 = 2L;
        String url2 = "https://example.com";
        String url3 = "https://example.org";
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url1);
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId1, url2);
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId1, url3);
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(statusId1, url1);

        assertTrue(oEmbedRequest1.equals(oEmbedRequest5));
        assertTrue(oEmbedRequest1.hashCode() == oEmbedRequest5.hashCode());

        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest2.hashCode());

        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest3.hashCode());

        assertFalse(oEmbedRequest1.equals(oEmbedRequest4));
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest4.hashCode());
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        String expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[]}", statusId, url);
        assertEquals(expected, oEmbedRequest.toString());

        oEmbedRequest.setMaxWidth(42);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=42, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang=''}", statusId, url);
        assertEquals(expected, oEmbedRequest.toString());

        oEmbedRequest.setHideMedia(false);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=42, hideMedia=false, hideThread=true, omitScript=false, align=NONE, related=[], lang=''}", statusId, url);
        assertEquals(expected, oEmbedRequest.toString());

        oEmbedRequest.setHideThread(false);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=42, hideMedia=false, hideThread=false, omitScript=false, align=NONE, related=[], lang=''}", statusId, url);
        assertEquals(expected, oEmbedRequest.toString());

        oEmbedRequest.setOmitScript(true);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=42, hideMedia=false, hideThread=false, omitScript=true, align=NONE, related=[], lang=''}", statusId, url);
        assertEquals(expected, oEmbedRequest.toString());

        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=42, hideMedia=false, hideThread=false, omitScript=true, align=%s, related=[], lang=''}", statusId, url, align);
        assertEquals(expected, oEmbedRequest.toString());

        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=42, hideMedia=false, hideThread=false, omitScript=true, align=%s, related=[%s, %s], lang=''}", statusId, url, align, related[0], related[1]);
        assertEquals(expected, oEmbedRequest.toString());

        String lang = "en";
        oEmbedRequest.setLang(lang);
        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=42, hideMedia=false, hideThread=false, omitScript=true, align=%s, related=[%s, %s], lang='%s'}", statusId, url, align, related[0], related[1], lang);
        assertEquals(expected, oEmbedRequest.toString());
    }

    @Test
    public void testAlignEnum() {
        List<Align> aligns = EnumSet.allOf(Align.class);

        for (Align align : aligns) {
            OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
            oEmbedRequest.align(align);
            assertEquals(align, oEmbedRequest.getAlign());
        }
    }
}