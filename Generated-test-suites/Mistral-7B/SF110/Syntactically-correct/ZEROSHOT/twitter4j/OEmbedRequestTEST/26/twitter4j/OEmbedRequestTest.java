package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertNull(oEmbedRequest.getMaxWidth());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
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

        int maxWidth = 120;
        oEmbedRequest.setMaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(200);
        assertEquals(200, oEmbedRequest.getMaxWidth());

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

        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(12, httpParameters.length);

        HttpParameter idParam = findParameterByName(httpParameters, "id");
        assertEquals(Long.toString(statusId), idParam.getValue());

        HttpParameter urlParam = findParameterByName(httpParameters, "url");
        assertEquals(url, urlParam.getValue());

        HttpParameter maxwidthParam = findParameterByName(httpParameters, "maxwidth");
        assertNull(maxwidthParam);

        HttpParameter hide_mediaParam = findParameterByName(httpParameters, "hide_media");
        assertTrue(hide_mediaParam.getValue().equalsIgnoreCase("true"));

        HttpParameter hide_threadParam = findParameterByName(httpParameters, "hide_thread");
        assertTrue(hide_threadParam.getValue().equalsIgnoreCase("true"));

        HttpParameter omit_scriptParam = findParameterByName(httpParameters, "omit_script");
        assertFalse(omit_scriptParam.isSet());

        HttpParameter alignParam = findParameterByName(httpParameters, "align");
        assertNull(alignParam);

        HttpParameter relatedParam = findParameterByName(httpParameters, "related");
        assertNull(relatedParam);

        HttpParameter langParam = findParameterByName(httpParameters, "lang");
        assertNull(langParam);
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "https://example.com/1";
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);

        long statusId2 = 2L;
        String url2 = "https://example.com/2";
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);

        long statusId3 = 1L;
        String url3 = "https://example.com/1";
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId3, url3);

        long statusId4 = 1L;
        String url4 = "https://example.com/1";
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId4, url4);

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(statusId1, url2);

        OEmbedRequest oEmbedRequest7 = new OEmbedRequest(statusId2, url1);
        OEmbedRequest oEmbedRequest8 = new OEmbedRequest(statusId2, url2);

        // equals test
        assertTrue(oEmbedRequest1.equals(oEmbedRequest1));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest5));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(null));
        assertFalse(oEmbedRequest1.equals(new Object()));

        // hashCode test
        int hashCode1 = oEmbedRequest1.hashCode();
        int hashCode2 = oEmbedRequest2.hashCode();
        int hashCode3 = oEmbedRequest3.hashCode();
        int hashCode4 = oEmbedRequest4.hashCode();
        int hashCode5 = oEmbedRequest5.hashCode();
        int hashCode6 = oEmbedRequest6.hashCode();
        int hashCode7 = oEmbedRequest7.hashCode();
        int hashCode8 = oEmbedRequest8.hashCode();

        // equals and hashCode should be consistent
        assertTrue(oEmbedRequest1.equals(oEmbedRequest5) && hashCode1 == hashCode5);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest1) && hashCode1 == hashCode1);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2) && hashCode1 != hashCode2);
        assertFalse(oEmbedRequest1.equals(null) && hashCode1 != 0);
        assertFalse(oEmbedRequest1.equals(new Object()) && hashCode1 != System.identityHashCode(new Object()));

        // different objects with same values should have same hashCode
        assertTrue(oEmbedRequest1.equals(oEmbedRequest3) && hashCode1 == hashCode3);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest4) && hashCode1 == hashCode4);

        // different objects with different values should have different hashCodes
        assertFalse(oEmbedRequest1.equals(oEmbedRequest7) && hashCode1 != hashCode7);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest8) && hashCode1 != hashCode8);

        // null object should not be equal to any non-null object and have hashCode 0
        assertFalse(oEmbedRequest1.equals(null) && hashCode1 == 0);
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        String expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=%d, hideMedia=%b, hideThread=%b, omitScript=%b, align=%s, related=[], lang='%s'}", 
                statusId, url, 0, true, true, false, null, null);
        assertEquals(expected, oEmbedRequest.toString());

        oEmbedRequest.setMaxWidth(120);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");

        expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=%d, hideMedia=%b, hideThread=%b, omitScript=%b, align=%s, related=[related1, related2], lang='en'}", 
                statusId, url, 120, false, false, true, Align.CENTER);
        assertEquals(expected, oEmbedRequest.toString());
    }

    private HttpParameter findParameterByName(HttpParameter[] parameters, String name) {
        for (HttpParameter parameter : parameters) {
            if (name.equals(parameter.getName())) {
                return parameter;
            }
        }
        return null;
    }

    @Test
    public void testAlignEnum() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest.Align align = OEmbedRequest.Align.LEFT;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.getAlign());
        assertEquals(Align.LEFT.name().toLowerCase(), oEmbedRequest.getAlign().name().toLowerCase());

        oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.CENTER, oEmbedRequest.getAlign());
        assertEquals(Align.CENTER.name().toLowerCase(), oEmbedRequest.getAlign().name().toLowerCase());

        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());
        assertEquals(Align.RIGHT.name().toLowerCase(), oEmbedRequest.getAlign().name().toLowerCase());

        oEmbedRequest.align(Align.NONE);
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertEquals(Align.NONE.name().toLowerCase(), oEmbedRequest.getAlign().name().toLowerCase());
    }

    @Test
    public void testToStringWithAlignAndRelated() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(120);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");

        String expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=%d, hideMedia=%b, hideThread=%b, omitScript=%b, align=%s, related=[related1, related2], lang='en'}", 
                statusId, url, 120, false, false, true, Align.CENTER);

        String actual = oEmbedRequest.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithRelatedEmptyArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(120);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.CENTER);
        oEmbedRequest.setRelated(new String[0]);
        oEmbedRequest.setLang("en");

        String expected = String.format("OEmbedRequest{statusId=%d, url='%s', maxWidth=%d, hideMedia=%b, hideThread=%b, omitScript=%b, align=%s, related=[], lang='en'}", 
                statusId, url, 120, false, false, true, Align.CENTER);

        String actual = oEmbedRequest.toString();
        assertEquals(expected, actual);
    }
}