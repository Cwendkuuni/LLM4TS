package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
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
    public void testSettersAndGettersForAllProperties() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(123);
        oEmbedRequest.MaxWidth(124);
        assertEquals(123, oEmbedRequest.getMaxWidth());
        assertEquals(124, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.HideMedia(true);
        assertFalse(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(false);
        oEmbedRequest.HideThread(true);
        assertFalse(oEmbedRequest.isHideThread());
        assertTrue(oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.omitScript(false);
        assertTrue(oEmbedRequest.isOmitScript());
        assertFalse(oEmbedRequest.isOmitScript());

        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.LEFT, oEmbedRequest.getAlign());
        assertEquals(Align.CENTER, oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(related, oEmbedRequest.getRelated());
        assertArrayEquals(new String[]{"related3", "related4"}, oEmbedRequest.getRelated());

        String lang = "en";
        oEmbedRequest.setLang(lang);
        oEmbedRequest.lang("fr");
        assertEquals(lang, oEmbedRequest.getLang());
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "https://example.com";
        long statusId2 = 2L;
        String url2 = "https://example.com";
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);

        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId1, url1);

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(statusId1, url2);
        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(statusId2, url1);

        assertTrue(oEmbedRequest1.equals(oEmbedRequest3));
        assertTrue(oEmbedRequest3.equals(oEmbedRequest4));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest4));

        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest2.equals(oEmbedRequest1));

        assertFalse(oEmbedRequest1.equals(null));
        assertFalse(null.equals(oEmbedRequest1));

        int hashcode1 = oEmbedRequest1.hashCode();
        int hashcode2 = oEmbedRequest2.hashCode();
        int hashcode3 = oEmbedRequest3.hashCode();
        int hashcode4 = oEmbedRequest4.hashCode();
        int hashcode5 = oEmbedRequest5.hashCode();
        int hashcode6 = oEmbedRequest6.hashCode();

        assertEquals(hashcode1, hashcode3);
        assertEquals(hashcode3, hashcode4);
        assertNotEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode2, hashcode3);
        assertNotEquals(hashcode1, hashcode5);
        assertNotEquals(hashcode2, hashcode6);
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        String expected = "OEmbedRequest{statusId=1, url='" + url + "', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        String actual = oEmbedRequest.toString();
        assertEquals(expected, actual);

        oEmbedRequest.setMaxWidth(123);
        oEmbedRequest.MaxWidth(124);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.HideMedia(true);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.HideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.omitScript(false);
        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        oEmbedRequest.align(Align.CENTER);
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"related3", "related4"});
        String lang = "en";
        oEmbedRequest.setLang(lang);
        oEmbedRequest.lang("fr");

        expected = "OEmbedRequest{statusId=1, url='" + url + "', maxWidth=123, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        String actual2 = oEmbedRequest.toString();
        assertEquals(expected, actual2);

        expected = "OEmbedRequest{statusId=1, url='" + url + "', maxWidth=124, hideMedia=true, hideThread=false, omitScript=false, align=CENTER, related=[related3, related4], lang='fr'}";
        String actual3 = oEmbedRequest.toString();
        assertEquals(expected, actual3);
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        HttpParameter[] httpParameterArray = oEmbedRequest.asHttpParameterArray();

        List<String> expectedKeys = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        List<String> actualKeys = new ArrayList<>();
        for (HttpParameter httpParameter : httpParameterArray) {
            actualKeys.add(httpParameter.getName());
        }
        assertEquals(expectedKeys, actualKeys);

        if (oEmbedRequest.getMaxWidth() > 0) {
            assertEquals("maxwidth=0", httpParameterArray[2].getName() + "=" + httpParameterArray[2].getValue());
        } else {
            int index = -1;
            for (int i = 0; i < httpParameterArray.length; i++) {
                if (httpParameterArray[i].getName().equals("maxwidth")) {
                    index = i;
                    break;
                }
            }
            assertNotNull(index);
            assertNull(httpParameterArray[index].getValue());
        }

        if (oEmbedRequest.isHideMedia()) {
            assertEquals("hide_media=true", httpParameterArray[5].getName() + "=" + httpParameterArray[5].getValue());
        } else {
            assertEquals("hide_media=false", httpParameterArray[5].getName() + "=" + httpParameterArray[5].getValue());
        }

        if (oEmbedRequest.isHideThread()) {
            assertEquals("hide_thread=true", httpParameterArray[6].getName() + "=" + httpParameterArray[6].getValue());
        } else {
            assertEquals("hide_thread=false", httpParameterArray[6].getName() + "=" + httpParameterArray[6].getValue());
        }

        if (oEmbedRequest.isOmitScript()) {
            assertEquals("omit_script=true", httpParameterArray[7].getName() + "=" + httpParameterArray[7].getValue());
        } else {
            assertEquals("omit_script=false", httpParameterArray[7].getName() + "=" + httpParameterArray[7].getValue());
        }

        if (oEmbedRequest.getAlign() != null) {
            assertEquals(oEmbedRequest.getAlign().name().toLowerCase(), httpParameterArray[8].getValue());
        } else {
            assertEquals("align=none", httpParameterArray[8].getName() + "=" + httpParameterArray[8].getValue());
        }

        if (oEmbedRequest.getRelated() != null) {
            String relatedValues = StringUtil.join(oEmbedRequest.getRelated(), ",");
            assertEquals("related=" + relatedValues, httpParameterArray[9].getValue());
        } else {
            assertEquals("related=", httpParameterArray[9].getName() + "=" + httpParameterArray[9].getValue());
        }

        if (oEmbedRequest.getLang() != null) {
            assertEquals("lang=" + oEmbedRequest.getLang(), httpParameterArray[10].getValue());
        } else {
            assertEquals("lang=", httpParameterArray[10].getName() + "=" + httpParameterArray[10].getValue());
        }
    }
}