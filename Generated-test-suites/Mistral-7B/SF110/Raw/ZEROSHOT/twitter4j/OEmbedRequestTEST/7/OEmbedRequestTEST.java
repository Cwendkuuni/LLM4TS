package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestCreation() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertNull(oEmbedRequest.getMaxWidth());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testOEmbedRequestSetters() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.MaxWidth(200);
        assertEquals(200, oEmbedRequest.getMaxWidth());

        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.HideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());

        oEmbedRequest.setHideThread(false);
        oEmbedRequest.HideThread(true);
        assertTrue(oEmbedRequest.isHideThread());

        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.isOmitScript());

        oEmbedRequest.setAlign(Align.LEFT);
        oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.CENTER, oEmbedRequest.getAlign());

        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, oEmbedRequest.getRelated());

        oEmbedRequest.setLang("en");
        oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.getLang());
    }

    @Test
    public void testOEmbedRequestAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
        List<String> parameterNames = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        for (int i = 0; i < httpParameters.length; i++) {
            String parameterName = parameterNames.get(i);
            HttpParameter parameter = httpParameters[i];
            assertEquals(parameterName, parameter.getName());
            if (parameter instanceof StringHttpParameter) {
                StringHttpParameter stringParameter = (StringHttpParameter)parameter;
                String expectedValue = i < 7 ? String.valueOf(i < 6 ? getExpectedValue(i) : oEmbedRequest.getLang()) :
                        (i == 7 && oEmbedRequest.getRelated() != null) ? StringUtil.join(oEmbedRequest.getRelated()) : null;
                assertEquals(expectedValue, stringParameter.getValue());
            } else if (parameter instanceof LongHttpParameter) {
                LongHttpParameter longParameter = (LongHttpParameter)parameter;
                long expectedValue = i < 5 ? getExpectedValue(i) : oEmbedRequest.getStatusId();
                assertEquals(expectedValue, longParameter.getValue());
            }
        }
    }

    private long getExpectedValue(int index) {
        switch (index) {
            case 0: return 1L;
            case 1: return 1L;
            case 2: return 100;
            case 3: return false;
            case 4: return false;
            default: return 0L;
        }
    }

}