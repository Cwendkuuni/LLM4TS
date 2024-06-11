package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
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
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

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

        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        request.setAlign(align);
        assertEquals(align, request.getAlign());

        String[] related = new String[]{"foo", "bar"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        String lang = "en";
        request.setLang(lang);
        assertEquals(lang, request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = new String[]{"foo", "bar"};
        request.setRelated(related);
        String lang = "en";
        request.setLang(lang);

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);
        assertEquals("id", params[0].getName());
        assertEquals(String.valueOf(statusId), params[0].getValue());
        assertEquals("url", params[1].getName());
        assertEquals(url, params[1].getValue());
        assertEquals("maxwidth", params[2].getName());
        assertEquals(String.valueOf(100), params[2].getValue());
        assertEquals("hide_media", params[3].getName());
        assertEquals(String.valueOf(false), params[3].getValue());
        assertEquals("hide_thread", params[4].getName());
        assertEquals(String.valueOf(true), params[4].getValue());
        assertEquals("omit_script", params[5].getName());
        assertEquals(String.valueOf(true), params[5].getValue());
        assertEquals("align", params[6].getName());
        assertEquals(OEmbedRequest.Align.CENTER.name().toLowerCase(), params[6].getValue());
        assertEquals("related", params[7].getName());
        assertEquals(StringUtil.join(related), params[7].getValue());
        assertEquals("lang", params[8].getName());
        assertEquals(lang, params[8].getValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        long statusId1 = 1234567890L;
        String url1 = "https://example.com/status/1234567890";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        request1.setMaxWidth(100);
        request1.setHideMedia(false);
        request1.setHideThread(true);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.CENTER);
        String[] related1 = new String[]{"foo", "bar"};
        request1.setRelated(related1);
        String lang1 = "en";
        request1.setLang(lang1);

        long statusId2 = 1234567890L;
        String url2 = "https://example.com/status/1234567890";
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        request2.setMaxWidth(100);
        request2.setHideMedia(false);
        request2.setHideThread(true);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.CENTER);
        String[] related2 = new String[]{"foo", "bar"};
        request2.setRelated(related2);
        String lang2 = "en";
        request2.setLang(lang2);

        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);
        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request3));
        assertTrue(request3.equals(request1));
        assertTrue(request1.equals(request2));
        assertTrue(request2.equals(request1));

        request2.setLang("fr");
        assertFalse(request1.equals(request2));

        request1.setStatusId(1234567891L);
        assertFalse(request1.equals(request2));

        request2.setStatusId(1234567891L);
        assertFalse(request1.equals(request2));

        request1.setUrl("https://example.com/status/1234567891");
        assertFalse(request1.equals(request2));

        request2.setUrl("https://example.com/status/1234567891");
        assertFalse(request1.equals(request2));

        String[] related3 = new String[]{"baz", "qux"};
        request1.setRelated(related3);
        assertFalse(request1.equals(request2));

        request2.setRelated(related3);
        assertFalse(request1.equals(request2));

        request1.setRelated(related1);
        request2.setRelated(related1);
        assertTrue(request1.equals(request2));

        assertEquals(request1.hashCode(), request1.hashCode());
        assertEquals(request1.hashCode(), request3.hashCode());
        assertEquals(request1.hashCode(), request2.hashCode());

        request2.setStatusId(1234567891L);
        assertNotEquals(request1.hashCode(), request2.hashCode());

        request1.setUrl("https://example.com/status/1234567891");
        assertNotEquals(request1.hashCode(), request2.hashCode());

        request2.setRelated(related2);
        assertNotEquals(request1.hashCode(), request2.hashCode());

        request1.setRelated(related3);
        assertNotEquals(request1.hashCode(), request2.hashCode());

        request2.setRelated(related1);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        long statusId = 1234567890L;
        String url = "https://example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = new String[]{"foo", "bar"};
        request.setRelated(related);
        String lang = "en";
        request.setLang(lang);

        String expected = "OEmbedRequest{statusId=1234567890, url='" + url + "', maxWidth=100, hideMedia=false, hideThread=true, omitScript=true, align=CENTER, related=[foo, bar], lang='en'}";
        assertEquals(expected, request.toString());
    }
}