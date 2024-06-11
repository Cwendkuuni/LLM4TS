package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, oEmbedRequest.getStatusId());
        assertEquals("http://example.com", oEmbedRequest.getUrl());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.MaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.HideMedia(true);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.HideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.omitScript(false);
        oEmbedRequest.setAlign(Align.LEFT);
        oEmbedRequest.align(Align.RIGHT);
        String[] related = new String[]{"one", "two", "three"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"four", "five", "six"});
        assertEquals(200, oEmbedRequest.getMaxWidth());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());
        assertArrayEquals(new String[]{"four", "five", "six"}, oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        assertEquals(12, httpParameters.length);
        List<String> expectedNames = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (int i = 0; i < httpParameters.length; i++) {
            assertEquals(expectedNames.get(i), httpParameters[i].getName());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest oEmbedRequest6 = new OEmbedRequest(1L, null);
        OEmbedRequest oEmbedRequest7 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest7.setLang("en");
        OEmbedRequest oEmbedRequest8 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest8.setRelated(new String[]{"one", "two"});
        OEmbedRequest oEmbedRequest9 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest9.setMaxWidth(100);
        OEmbedRequest oEmbedRequest10 = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest10.setMaxWidth(100);
        oEmbedRequest10.setRelated(new String[]{"one", "two"});
        assertTrue(oEmbedRequest1.equals(oEmbedRequest4));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest5));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest8));
        assertTrue(oEmbedRequest1.equals(oEmbedRequest10));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest6));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest7));
        assertFalse(oEmbedRequest1.equals(null));
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest4.hashCode());
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest5.hashCode());
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest8.hashCode());
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest10.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }

    @Test
    public void testAlignValues() {
        assertTrue(Align.LEFT.equals(Align.valueOf("LEFT")));
        assertTrue(Align.CENTER.equals(Align.valueOf("CENTER")));
        assertTrue(Align.RIGHT.equals(Align.valueOf("RIGHT")));
        assertNull(Align.valueOf(null));
        assertThrows(IllegalArgumentException.class, () -> Align.valueOf("INVALID"));
    }

    @Test
    public void testAppendParameterNullValue() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("name", null, params);
        assertTrue(params.isEmpty());
    }

    @Test
    public void testAppendParameterLongValue() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("name", 1L, params);
        List<HttpParameter> expectedParams = Arrays.asList(new HttpParameter("name", "1"));
        assertEquals(expectedParams, params);
    }

    @Test
    public void testAppendParameterStringValue() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("name", "value", params);
        List<HttpParameter> expectedParams = Arrays.asList(new HttpParameter("name", "value"));
        assertEquals(expectedParams, params);
    }

    @Test
    public void testAppendParameterEmptyStringArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("name", (String[]) null, params);
        assertTrue(params.isEmpty());
    }

    @Test
    public void testAppendParameterNonEmptyStringArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        String[] related = new String[]{"one", "two", "three"};
        oEmbedRequest.appendParameter("name", related, params);
        List<HttpParameter> expectedParams = Arrays.asList(new HttpParameter("name", StringUtil.join(related)));
        assertEquals(expectedParams, params);
    }
}