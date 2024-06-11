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
    public void testOEmbedRequestConstructor() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(0L, null);

        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());

        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        assertFalse(request.isHideMedia());

        request.HideMedia(true);
        assertTrue(request.isHideMedia());

        request.setHideThread(false);
        assertFalse(request.isHideThread());

        request.HideThread(true);
        assertTrue(request.isHideThread());

        request.setOmitScript(true);
        assertTrue(request.isOmitScript());

        request.omitScript(false);
        assertFalse(request.isOmitScript());

        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        request.setRelated(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, request.getRelated());

        request.related(new String[]{});
        assertArrayEquals(new String[0], request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());

        request.lang(null);
        assertNull(request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(0L, null);

        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        List<String> expectedKeys = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        List<String> actualKeys = Arrays.stream(params).map(HttpParameter::getName).collect(Collectors.toList());
        assertEquals(expectedKeys, actualKeys);

        List<String> expectedValues = Arrays.asList(String.valueOf(0L), null, String.valueOf(100), String.valueOf(false), String.valueOf(true), String.valueOf(true), Align.LEFT.name().toLowerCase(), StringUtil.join(new String[]{"related1", "related2"}, ","), "en");
        List<String> actualValues = Arrays.stream(params).map(HttpParameter::getValue).collect(Collectors.toList());
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testEqualsAndHashcode() {
        OEmbedRequest request1 = new OEmbedRequest(0L, null);
        OEmbedRequest request2 = new OEmbedRequest(0L, null);

        request1.setMaxWidth(100);
        request2.setMaxWidth(100);
        assertEquals(request1, request2);

        request1.setUrl("https://example.com/status/1");
        assertNotEquals(request1, request2);

        request2.setUrl("https://example.com/status/1");
        assertEquals(request1, request2);

        request1.setRelated(new String[]{"related1", "related2"});
        request2.setRelated(new String[]{"related2", "related1"});
        assertNotEquals(request1, request2);

        request2.setRelated(new String[]{"related1", "related2"});
        assertEquals(request1, request2);

        request1.setLang("en");
        request2.setLang("fr");
        assertNotEquals(request1, request2);

        request2.setLang("en");
        assertEquals(request1, request2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterWithNullName() {
        OEmbedRequest request = new OEmbedRequest(0L, null);
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter(null, "value", params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterWithNullValue() {
        OEmbedRequest request = new OEmbedRequest(0L, null);
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("name", null, params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterWithNegativeValue() {
        OEmbedRequest request = new OEmbedRequest(0L, null);
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("name", -1, params);
    }

}