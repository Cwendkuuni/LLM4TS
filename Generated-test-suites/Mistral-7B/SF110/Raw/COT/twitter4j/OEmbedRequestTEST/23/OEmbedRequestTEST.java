package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, oEmbedRequest.getStatusId());
        assertEquals("http://example.com", oEmbedRequest.getUrl());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        oEmbedRequest.setMaxWidth(200);
        oEmbedRequest.MaxWidth(300);
        assertEquals(300, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.HideMedia(true);
        assertEquals(true, oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(false);
        oEmbedRequest.HideThread(true);
        assertEquals(true, oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.omitScript(false);
        assertEquals(false, oEmbedRequest.isOmitScript());

        oEmbedRequest.setAlign(Align.LEFT);
        oEmbedRequest.align(Align.RIGHT);
        assertEquals(Align.RIGHT, oEmbedRequest.getAlign());

        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"related3"});
        assertArrayEquals(new String[]{"related3"}, oEmbedRequest.getRelated());

        oEmbedRequest.setLang("en");
        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
        List<String> names = Collections.nCopies(params.length, "name");
        List<String> values = Arrays.asList("value1", "value2");
        for (int i = 0; i < params.length; i++) {
            assertEquals(names.get(i), params[i].getName());
            assertEquals(values.get(i), params[i].getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNegativeStatusId() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("id", -1L, params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNullUrl() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("url", null, params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNullName() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter(null, "value", params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNullValue() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("name", null, params);
    }

}