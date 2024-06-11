package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1234567890L;
        String url = "https://www.example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://www.example.com/status/1234567890");

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

        Align align = Align.LEFT;
        request.setAlign(align);
        assertEquals(align, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        String lang = "en";
        request.setLang(lang);
        assertEquals(lang, request.getLang());
    }

    @Test
    public void testEqualAndHashCode() {
        long statusId = 1234567890L;
        String url = "https://www.example.com/status/1234567890";

        OEmbedRequest request1 = new OEmbedRequest(statusId, url);
        OEmbedRequest request2 = new OEmbedRequest(statusId, url);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        request1.setMaxWidth(100);
        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());

        request2.setMaxWidth(100);
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1234567890L;
        String url = "https://www.example.com/status/1234567890";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.MaxWidth(200);
        request.setHideMedia(false);
        request.HideMedia(true);
        request.setHideThread(false);
        request.HideThread(true);
        request.setOmitScript(true);
        request.omitScript(false);
        Align align = Align.LEFT;
        request.setAlign(align);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        String lang = "en";
        request.setLang(lang);

        HttpParameter[] params = request.asHttpParameterArray();
        List<String> paramsList = Arrays.asList(params);

        List<String> expectedParams = Arrays.asList(
                "id=" + statusId,
                "url=" + url,
                "maxwidth=" + 200,
                "hide_media=" + !request.isHideMedia(),
                "hide_thread=" + request.isHideThread(),
                "omit_script=" + request.isOmitScript(),
                "align=" + align.name().toLowerCase(),
                "related=" + StringUtil.join(related),
                "lang=" + lang
        );

        assertEquals(expectedParams, paramsList);
    }
}