package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

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

        String[] relatedArray = new String[]{"related1", "related2"};
        request.setRelated(relatedArray);
        assertEquals(relatedArray, request.getRelated());
        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());
        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] relatedArray = new String[]{"related1", "related2"};
        request.setRelated(relatedArray);
        request.setLang("en");

        List<HttpParameter> params = Arrays.asList(request.asHttpParameterArray());
        List<String> paramNames = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");

        for (int i = 0; i < params.size(); i++) {
            assertEquals(paramNames.get(i), params.get(i).getName());
            if (i < 7) {
                if (params.get(i) instanceof HttpBasicNameValueParameter) {
                    HttpBasicNameValueParameter param = (HttpBasicNameValueParameter) params.get(i);
                    if (i == 1) {
                        assertEquals("http://example.com", param.getValue());
                    } else if (i == 5) {
                        assertTrue(params.get(i).getValue().equals("true") || params.get(i).getValue().equals("false"));
                    } else {
                        assertEquals(i + 1, Integer.parseInt(params.get(i).getValue()));
                    }
                } else if (i == 6) {
                    assertEquals("en", ((HttpStringNameValueParameter) params.get(i)).getValue());
                } else {
                    if (i < 3) {
                        assertNull(params.get(i).getValue());
                    } else {
                        HttpArrayParameter param = (HttpArrayParameter) params.get(i);
                        String[] paramValues = param.getValue();
                        assertEquals(relatedArray.length, paramValues.length);
                        for (int j = 0; j < relatedArray.length; j++) {
                            assertEquals(relatedArray[j], paramValues[j]);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");

        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        request4.setMaxWidth(100);
        request4.setHideMedia(false);
        request4.setHideThread(true);
        request4.setOmitScript(true);
        request4.setAlign(Align.LEFT);
        String[] relatedArray = new String[]{"related1", "related2"};
        request4.setRelated(relatedArray);
        request4.setLang("en");

        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(200);
        request5.setHideMedia(false);
        request5.setHideThread(true);
        request5.setOmitScript(true);
        request5.setAlign(Align.LEFT);
        request5.setRelated(new String[0]);
        request5.setLang(null);

        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setMaxWidth(100);
        request6.setHideMedia(false);
        request6.setHideThread(true);
        request6.setOmitScript(true);
        request6.setAlign(Align.LEFT);
        request6.setRelated(new String[0]);
        request6.setLang(null);

        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setMaxWidth(100);
        request7.setHideMedia(false);
        request7.setHideThread(true);
        request7.setOmitScript(true);
        request7.setAlign(Align.LEFT);
        request7.setRelated(new String[]{"related1"});
        request7.setLang("en");

        assertTrue(request1.equals(request4));
        assertTrue(request1.equals(request6));
        assertTrue(request1.equals(request7));
        assertFalse(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(null));

        int hashCode1 = request1.hashCode();
        int hashCode2 = request4.hashCode();
        int hashCode3 = request6.hashCode();
        int hashCode4 = request7.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, hashCode4);
    }
}