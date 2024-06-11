package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 123L;
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
        long statusId = 123L;
        String url = "https://example.com/status/123";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        request.HideMedia(true);
        assertTrue(request.isHideMedia());

        request.setHideThread(false);
        request.HideThread(true);
        assertTrue(request.isHideThread());

        request.setOmitScript(true);
        request.omitScript(false);
        assertFalse(request.isOmitScript());

        Align align = Align.LEFT;
        request.setAlign(align);
        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.related(new String[]{"related3"});
        assertArrayEquals(new String[]{"related3"}, request.getRelated());

        request.setLang("en");
        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 123L;
        String url = "https://example.com/status/123";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.MaxWidth(200);
        request.setHideMedia(false);
        request.HideMedia(true);
        request.setHideThread(false);
        request.HideThread(true);
        request.setOmitScript(true);
        request.omitScript(false);
        request.setAlign(Align.LEFT);
        request.align(Align.CENTER);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.related(new String[0]);
        request.setLang("en");
        request.lang("fr");

        List<HttpParameter> params = Arrays.asList(request.asHttpParameterArray());

        List<String> names = Collections.nCopies(params.size(), "paramName");
        List<String> values = Arrays.asList(
                "123",
                "https://example.com/status/123",
                "200",
                "false",
                "true",
                "true",
                "center",
                "",
                "en",
                "related1",
                "related2"
        );

        for (int i = 0; i < params.size(); i++) {
            assertEquals(names.get(i), params.get(i).getName());
            assertEquals(values.get(i), params.get(i).getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        long statusId = 123L;
        String url = "https://example.com/status/123";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        long statusId = 123L;
        OEmbedRequest request = new OEmbedRequest(statusId, null);
    }
}