package twitter4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.statusId);
        assertEquals("http://example.com", request.url);
        assertEquals(0, request.maxWidth);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertNull(request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 2L;
        String url = "http://example.com/post/2";
        int maxWidth = 420;
        boolean hideMedia = false;
        boolean hideThread = false;
        boolean omitScript = true;
        Align align = Align.LEFT;
        String lang = "en";
        String[] related = {"related1", "related2"};

        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.MaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.setHideMedia(hideMedia);
        assertEquals(hideMedia, request.isHideMedia());

        request.HideMedia(hideMedia);
        assertEquals(hideMedia, request.isHideMedia());

        request.setHideThread(hideThread);
        assertEquals(hideThread, request.isHideThread());

        request.HideThread(hideThread);
        assertEquals(hideThread, request.isHideThread());

        request.setOmitScript(omitScript);
        assertEquals(omitScript, request.isOmitScript());

        request.omitScript(omitScript);
        assertEquals(omitScript, request.isOmitScript());

        request.setAlign(align);
        assertEquals(align, request.getAlign());

        request.align(align);
        assertEquals(align, request.getAlign());

        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(related);
        assertArrayEquals(related, request.getRelated());

        request.setLang(lang);
        assertEquals(lang, request.getLang());

        request.lang(lang);
        assertEquals(lang, request.getLang());
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");

        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");

        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(420);

        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setMaxWidth(421);

        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setMaxWidth(420);
        request7.setLang("fr");

        Map<OEmbedRequest, Integer> map = new HashMap<>();
        map.put(request1, 1);
        map.put(request2, 1);
        map.put(request3, 2);
        map.put(request4, 1);
        map.put(request5, 5);
        map.put(request6, 6);
        map.put(request7, 7);

        List<OEmbedRequest> list = Arrays.asList(request1, request2, request3, request4, request5, request6, request7);

        for (OEmbedRequest o : list) {
            for (OEmbedRequest other : list) {
                if (!o.equals(other)) {
                    int result = o.hashCode();
                    int otherHashCode = other.hashCode();
                    assertEquals("Unexpected hash code for equals objects: " + o + " and " + other, result, otherHashCode);
                }
            }
        }
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = String.format(
                "OEmbedRequest{statusId=%d, url='%s', maxWidth=%d, hideMedia=%b, hideThread=%b, omitScript=%b, align=%s, related=%s, lang='%s'}",
                1, "http://example.com", 0, true, true, false, null, new String[0], null);
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(420);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");

        HttpParameter[] expected = new HttpParameter[]{
            new HttpParameter("id", "1"),
            new HttpParameter("url", "http://example.com"),
            new HttpParameter("maxwidth", "420"),
            new HttpParameter("hide_media", "false"),
            new HttpParameter("hide_thread", "false"),
            new HttpParameter("omit_script", "true"),
            new HttpParameter("align", "left"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        };

        HttpParameter[] actual = request.asHttpParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAppendParameter() {
        List<HttpParameter> params = new ArrayList<>();
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.appendParameter("id", "1");
        params.add(new HttpParameter("id", "1"));

        request.appendParameter("url", "http://example.com");
        params.add(new HttpParameter("url", "http://example.com"));

        request.appendParameter("maxwidth", "420");
        params.add(new HttpParameter("maxwidth", "420"));

        request.appendParameter("hide_media", "false");
        params.add(new HttpParameter("hide_media", "false"));

        request.appendParameter("hide_thread", "true");
        params.add(new HttpParameter("hide_thread", "true"));

        request.appendParameter("omit_script", "true");
        params.add(new HttpParameter("omit_script", "true"));

        request.appendParameter("align", "left");
        params.add(new HttpParameter("align", "left"));

        request.appendParameter("related", "related1,related2");
        params.add(new HttpParameter("related", "related1,related2"));

        request.appendParameter("lang", "en");
        params.add(new HttpParameter("lang", "en"));

        HttpParameter[] expected = params.toArray(new HttpParameter[params.size()]);
        HttpParameter[] actual = request.asHttpParameterArray();
        assertArrayEquals(expected, actual);
    }
}