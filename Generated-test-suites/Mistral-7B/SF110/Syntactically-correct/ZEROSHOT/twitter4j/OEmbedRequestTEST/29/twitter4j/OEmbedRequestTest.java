package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, oEmbedRequest.getStatusId());
        assertEquals("http://example.com", oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");

        oEmbedRequest.setMaxWidth(120);
        assertEquals(120, oEmbedRequest.getMaxWidth());

        oEmbedRequest.HideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());

        oEmbedRequest.HideThread(false);
        assertFalse(oEmbedRequest.isHideThread());

        oEmbedRequest.omitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());

        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());

        String[] related = {"related1", "related2"};
        oEmbedRequest.related(related);
        assertEquals(related, oEmbedRequest.getRelated());

        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        List<String> expectedParameterNames = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        List<String> actualParameterNames = Arrays.stream(httpParameters).map(HttpParameter::getName).collect(Collectors.toList());
        assertEquals(expectedParameterNames, actualParameterNames);

        OEmbedRequest oEmbedRequestWithMaxWidth = oEmbedRequest.MaxWidth(120);
        HttpParameter[] httpParametersWithMaxWidth = oEmbedRequestWithMaxWidth.asHttpParameterArray();
        HttpParameter maxWidthParameter = Arrays.stream(httpParametersWithMaxWidth).filter(param -> "maxwidth".equals(param.getName())).findFirst().get();
        assertEquals(120, Integer.parseInt(maxWidthParameter.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.MaxWidth(-1);
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");

        assertEquals(oEmbedRequest1, oEmbedRequest1);
        assertEquals(oEmbedRequest1, oEmbedRequest2);
        assertNotEquals(oEmbedRequest1, oEmbedRequest3);

        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest4.setMaxWidth(120);
        assertNotEquals(oEmbedRequest1, oEmbedRequest4);

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest5.setLang("en");
        oEmbedRequest5.setRelated(new String[]{"related1"});
        assertNotEquals(oEmbedRequest1, oEmbedRequest5);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");

        int hashCode1 = oEmbedRequest1.hashCode();
        int hashCode2 = oEmbedRequest2.hashCode();
        int hashCode3 = oEmbedRequest3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);

        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest4.setMaxWidth(120);
        int hashCode4 = oEmbedRequest4.hashCode();
        assertNotEquals(hashCode1, hashCode4);

        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest5.setLang("en");
        oEmbedRequest5.setRelated(new String[]{"related1"});
        int hashCode5 = oEmbedRequest5.hashCode();
        assertNotEquals(hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        String expectedToString = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedToString, oEmbedRequest.toString());
    }

    @Test
    public void testAlignEnum() {
        List<String> expectedAlignValues = Arrays.asList("LEFT", "CENTER", "RIGHT", "NONE");
        List<Align> actualAlignValues = Arrays.asList(Align.LEFT, Align.CENTER, Align.RIGHT, Align.NONE);
        List<Align> actualAlignValuesReverse = Arrays.asList(Align.NONE, Align.RIGHT, Align.CENTER, Align.LEFT);

        assertEquals(expectedAlignValues, Arrays.asList(Align.values()));
        assertEquals(actualAlignValues, Arrays.asList(Align.values()));
        assertEquals(actualAlignValuesReverse, Arrays.asList(Align.values()));
    }
}