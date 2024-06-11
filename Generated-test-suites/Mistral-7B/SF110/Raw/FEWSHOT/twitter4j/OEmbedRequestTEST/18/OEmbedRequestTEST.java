package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertFalse(request.isMaxWidthSet());
        assertTrue(request.isHideMediaDefault());
        assertTrue(request.isHideThreadDefault());
        assertFalse(request.isOmitScriptDefault());
        assertEquals(Align.NONE, request.getAlign());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.setMaxWidth(123);
        assertEquals(123, request.getMaxWidth());

        request.MaxWidth(456);
        assertEquals(456, request.getMaxWidth());

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

        request.align(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");

        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(456);
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setMaxWidth(456);

        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setRelated(new String[]{"related1"});
        OEmbedRequest request8 = new OEmbedRequest(1L, "http://example.com");
        request8.setRelated(new String[]{"related1"});

        OEmbedRequest request9 = new OEmbedRequest(1L, "http://example.com");
        request9.setLang("en");
        OEmbedRequest request10 = new OEmbedRequest(1L, "http://example.com");
        request10.setLang("en");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(null));

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertNotEquals(request1.hashCode(), request4.hashCode());
        assertNotEquals(request1.hashCode(), null.hashCode());

        assertTrue(request1.equals(request5));
        assertTrue(request5.equals(request1));
        assertFalse(request1.equals(request6));
        assertFalse(request6.equals(request1));
        assertFalse(request1.equals(request7));
        assertFalse(request7.equals(request1));
        assertTrue(request1.equals(request9));
        assertTrue(request9.equals(request1));
        assertFalse(request1.equals(request10));
        assertFalse(request10.equals(request1));
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}", request.toString());

        request.setMaxWidth(123);
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}", request.toString());

        request.HideMedia(false);
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=false, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}", request.toString());

        request.HideThread(false);
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=false, hideThread=false, omitScript=false, align=NONE, related=null, lang='null'}", request.toString());

        request.omitScript(true);
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=false, hideThread=false, omitScript=true, align=NONE, related=null, lang='null'}", request.toString());

        request.align(Align.LEFT);
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=null, lang='null'}", request.toString());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='null'}", request.toString());

        request.setLang("en");
        assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}", request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);

        assertEquals("id", params[0].getName());
        assertEquals(String.valueOf(request.getStatusId()), params[0].getValue());

        assertEquals("url", params[1].getName());
        assertEquals(request.getUrl(), params[1].getValue());

        assertEquals("maxwidth", params[2].getName());
        assertNull(params[2].getValue());

        assertEquals("hide_media", params[3].getName());
        assertEquals(Boolean.toString(request.isHideMedia()), params[3].getValue());

        assertEquals("hide_thread", params[4].getName());
        assertEquals(Boolean.toString(request.isHideThread()), params[4].getValue());

        assertEquals("omit_script", params[5].getName());
        assertEquals(Boolean.toString(request.isOmitScript()), params[5].getValue());

        assertEquals("align", params[6].getName());
        assertEquals(request.getAlign().name().toLowerCase(), params[6].getValue());

        assertNull(params[7].getValue());

        if (request.getRelated() != null) {
            assertEquals("related", params[8].getName());
            assertEquals(StringUtil.join(request.getRelated()), params[8].getValue());
        } else {
            assertNull(params[8].getValue());
        }

        assertEquals("lang", params[9].getName());
        assertNull(params[9].getValue());

        assertEquals(0, params[10].getName().length());
        assertNull(params[10].getValue());
    }

    @Test
    public void testAppendParameter() {
        List<HttpParameter> params = new ArrayList<>();

        request1: {
            request1: {
                OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
                request1.appendParameter("id", request1.getStatusId(), params);
                request1.appendParameter("url", request1.getUrl(), params);

                assertEquals(2, params.size());
                HttpParameter param1 = params.get(0);
                assertEquals("id", param1.getName());
                assertEquals(String.valueOf(request1.getStatusId()), param1.getValue());

                HttpParameter param2 = params.get(1);
                assertEquals("url", param2.getName());
                assertEquals(request1.getUrl(), param2.getValue());

                request1.appendParameter("maxwidth", request1.getMaxWidth(), params);
                assertEquals(3, params.size());
                HttpParameter param3 = params.get(2);
                assertEquals("maxwidth", param3.getName());
                assertNull(param3.getValue());
            }

            request2: {
                String value = "value";
                request1.appendParameter("name", value, params);
                assertEquals(4, params.size());
                HttpParameter param = params.get(3);
                assertEquals("name", param.getName());
                assertEquals(value, param.getValue());

                request1.appendParameter("name", value, params);
                assertEquals(4, params.size());

                request1.appendParameter("name", null, params);
                assertEquals(4, params.size());

                request1.appendParameter("name", "", params);
                assertEquals(4, params.size());

                request1.appendParameter("name", "", params);
                assertEquals(4, params.size());

                request1.appendParameter("name", null, params);
                assertEquals(4, params.size());

                request1.appendParameter("name", null, params);
                assertEquals(4, params.size());

                request1.appendParameter("name", null, params);
                assertEquals(4, params.size());

                try {
                    request1.appendParameter("name", null, params);
                    fail();
                } catch (NullPointerException e) {
                    // expected
                }

                request1.appendParameter("name", "", params);
                assertEquals(5, params.size());

                request2: {
                    long value = 123;
                    request1.appendParameter("name", value, params);
                    assertEquals(6, params.size());
                    HttpParameter param = params.get(5);
                    assertEquals("name", param.getName());
                    assertEquals(String.valueOf(value), param.getValue());

                    request1.appendParameter("name", value, params);
                    assertEquals(6, params.size());

                    request1.appendParameter("name", value + 1, params);
                    assertEquals(7, params.size());
                    HttpParameter param2 = params.get(6);
                    assertEquals("name", param2.getName());
                    assertEquals(String.valueOf(value + 1), param2.getValue());

                    request1.appendParameter("name", value, params);
                    assertEquals(7, params.size());

                    request1.appendParameter("name", value, params);
                    assertEquals(7, params.size());

                    break request2;
                }
            }
        }

        assertEquals(7, params.size());
    }
}