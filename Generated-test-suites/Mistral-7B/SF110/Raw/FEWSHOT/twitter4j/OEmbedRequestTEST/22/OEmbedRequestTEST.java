package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 123;
        String url = "https://example.com/status/123";
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
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com/status/123");

        request.setMaxWidth(42);
        assertEquals(42, request.getMaxWidth());

        request.HideMedia();
        assertFalse(request.isHideMedia());

        request.HideThread();
        assertFalse(request.isHideThread());

        request.omitScript();
        assertTrue(request.isOmitScript());

        Align align = Align.CENTER;
        request.align(align);
        assertEquals(Align.CENTER, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.related(related);
        assertArrayEquals(related, request.getRelated());

        String lang = "en";
        request.lang(lang);
        assertEquals(lang, request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com/status/123");
        request.setMaxWidth(42);
        request.HideMedia();
        request.HideThread();
        request.omitScript();
        request.align(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.related(related);
        String lang = "en";
        request.lang(lang);

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);

        HttpParameter idParam = params[0];
        assertEquals("id", idParam.getName());
        assertEquals(String.valueOf(request.getStatusId()), idParam.getValue());

        HttpParameter urlParam = params[1];
        assertEquals("url", urlParam.getName());
        assertEquals(request.getUrl(), urlParam.getValue());

        HttpParameter maxWidthParam = params[2];
        assertEquals("maxwidth", maxWidthParam.getName());
        assertEquals(String.valueOf(request.getMaxWidth()), maxWidthParam.getValue());

        HttpParameter hideMediaParam = params[3];
        assertEquals("hide_media", hideMediaParam.getName());
        assertEquals(String.valueOf(request.isHideMedia()), hideMediaParam.getValue());

        HttpParameter hideThreadParam = params[4];
        assertEquals("hide_thread", hideThreadParam.getName());
        assertEquals(String.valueOf(request.isHideThread()), hideThreadParam.getValue());

        HttpParameter omitScriptParam = params[5];
        assertEquals("omit_script", omitScriptParam.getName());
        assertEquals(String.valueOf(request.isOmitScript()), omitScriptParam.getValue());

        HttpParameter alignParam = params[6];
        assertEquals("align", alignParam.getName());
        assertEquals(Align.LEFT.name().toLowerCase(), alignParam.getValue());

        HttpParameter relatedParam = params[7];
        assertEquals("related", relatedParam.getName());
        assertEquals(StringUtil.join(request.getRelated()), relatedParam.getValue());

        HttpParameter langParam = params[8];
        assertEquals("lang", langParam.getName());
        assertEquals(request.getLang(), langParam.getValue());

        HttpParameter id2Param = params[9];
        assertNull(id2Param);

        HttpParameter url2Param = params[10];
        assertNull(url2Param);
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com/status/123");
        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com/status/123");

        OEmbedRequest request3 = new OEmbedRequest(124, "https://example.com/status/123");
        OEmbedRequest request4 = new OEmbedRequest(123, "https://example.com/status/124");

        OEmbedRequest request5 = new OEmbedRequest(123, "https://example.com/status/123");
        request5.HideMedia();
        OEmbedRequest request6 = new OEmbedRequest(123, "https://example.com/status/123");

        OEmbedRequest request7 = new OEmbedRequest(123, "https://example.com/status/123");
        request7.setMaxWidth(42);
        OEmbedRequest request8 = new OEmbedRequest(123, "https://example.com/status/123");
        request8.setMaxWidth(43);

        assertTrue(request1.equals(request2));
        assertTrue(request1.hashCode() == request2.hashCode());

        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));

        assertTrue(request1.equals(request5));
        assertFalse(request1.equals(request6));

        assertTrue(request7.equals(request8));
        assertFalse(request7.equals(request1));
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com/status/123");
        request.setMaxWidth(42);
        request.HideMedia();
        request.HideThread();
        request.omitScript();
        request.align(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.related(related);
        String lang = "en";
        request.lang(lang);

        String expected = "OEmbedRequest{statusId=123, url='https://example.com/status/123', maxWidth=42, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expected, request.toString());
    }
}