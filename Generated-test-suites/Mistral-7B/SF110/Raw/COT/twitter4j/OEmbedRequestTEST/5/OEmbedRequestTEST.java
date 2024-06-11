import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        OEmbedRequest request = new OEmbedRequest(123L, "http://example.com");
        assertEquals(123L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        // default values
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(123L, "http://example.com");
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
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123L, "http://example.com");
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        List<String> paramNames = Arrays.asList("id", "url", "maxwidth", "hide_media", "hide_thread", "omit_script", "align", "related", "lang");
        assertEquals(paramNames.size(), params.length);
        for (int i = 0; i < params.length; i++) {
            String name = paramNames.get(i);
            String value = null;
            if (i < 7) { // parameters with values
                value = name.equals("maxwidth") ? String.valueOf(request.getMaxWidth()) : String.valueOf(request.getBooleanValue(name));
            }
            HttpParameter param = params[i];
            assertEquals(name, param.getName());
            assertEquals(value, param.getValue());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(124L, "http://example.com");

        OEmbedRequest request4 = new OEmbedRequest(123L, "http://example.com");
        request4.setMaxWidth(100);
        OEmbedRequest request5 = new OEmbedRequest(123L, "http://example.com");
        request5.setMaxWidth(200);
        OEmbedRequest request6 = new OEmbedRequest(123L, "http://example.com");
        request6.setRelated(new String[]{"related1"});
        OEmbedRequest request7 = new OEmbedRequest(123L, "http://example.com");
        request7.setRelated(new String[0]);
        OEmbedRequest request8 = new OEmbedRequest(123L, "http://example.com");
        request8.setLang("en");

        // equals test
        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertTrue(request1.equals(request4));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request5));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
        assertFalse(request1.equals(request8));

        // hashcode test
        int hashcode1 = request1.hashCode();
        int hashcode2 = request2.hashCode();
        assertEquals(hashcode1, hashcode2);
        int hashcode3 = request3.hashCode();
        assertNotSame(hashcode1, hashcode3);
        int hashcode4 = request4.hashCode();
        assertEquals(hashcode1, hashcode4);
        int hashcode5 = request5.hashCode();
        assertNotSame(hashcode1, hashcode5);
        int hashcode6 = request6.hashCode();
        assertNotSame(hashcode1, hashcode6);
        int hashcode7 = request7.hashCode();
        assertNotSame(hashcode1, hashcode7);
        int hashcode8 = request8.hashCode();
        assertNotSame(hashcode1, hashcode8);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123L, "http://example.com");
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        String toString = request.toString();
        String expected = "OEmbedRequest{statusId=123, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expected, toString);
    }

    @Test
    public void testAlignEnum() {
        List<Align> aligns = Collections.unmodifiableList(Arrays.asList(OEmbedRequest.Align.LEFT, OEmbedRequest.Align.CENTER, OEmbedRequest.Align.RIGHT, OEmbedRequest.Align.NONE));

        // test values
        assertEquals(OEmbedRequest.Align.LEFT, OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.CENTER, OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.RIGHT, OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.NONE, OEmbedRequest.Align.NONE);

        // test isXXX method
        for (Align align : aligns) {
            assertTrue(align.isLeft());
            assertTrue(align.isCenter());
            assertTrue(align.isRight());
            assertFalse(align.isNone());
        }

        // test valuesOf method
        Align alignLeft = Align.LEFT;
        Align alignCenter = Align.CENTER;
        Align alignRight = Align.RIGHT;
        Align alignNone = Align.NONE;

        assertEquals(alignLeft, Align.valueOf("LEFT"));
        assertEquals(alignCenter, Align.valueOf("CENTER"));
        assertEquals(alignRight, Align.valueOf("RIGHT"));
        assertTrue(Align.valueOf("NONE").equals(alignNone));
        try {
            Align.valueOf("UNKNOWN");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAppendedParameters() {
        OEmbedRequest request = new OEmbedRequest(123L, "http://example.com");

        // set null value
        request.appendParameter("name", null);
        List<HttpParameter> params = request.asHttpParameterArray();
        int index = Arrays.asList("name").indexOf(p -> p.getName().equals("name"));
        assertNull(params[index].getValue());

        // set empty value
        request.appendParameter("name", "");
        params = request.asHttpParameterArray();
        index = Arrays.asList("name").indexOf(p -> p.getName().equals("name"));
        assertEquals("", params[index].getValue());

        // set long value
        request.appendParameter("id", Long.MAX_VALUE);
        params = request.asHttpParameterArray();
        index = Arrays.asList("id").indexOf(p -> p.getName().equals("id"));
        String longValue = String.valueOf(Long.MAX_VALUE);
        assertEquals(longValue, params[index].getValue());

        // set boolean value
        request.appendParameter("hide_media", true);
        params = request.asHttpParameterArray();
        index = Arrays.asList("hide_media").indexOf(p -> p.getName().equals("hide_media"));
        assertTrue(params[index].getValue().equals("true"));

        // set Align enum value
        request.appendParameter("align", Align.LEFT);
        params = request.asHttpParameterArray();
        index = Arrays.asList("align").indexOf(p -> p.getName().equals("align"));
        assertEquals(Align.LEFT.name().toLowerCase(), params[index].getValue());

        // set String array value
        String[] related = new String[]{"related1", "related2"};
        request.appendParameter("related", StringUtil.join(related));
        params = request.asHttpParameterArray();
        index = Arrays.asList("related").indexOf(p -> p.getName().equals("related"));
        String joinedRelated = StringUtil.join(related, ",");
        assertEquals(joinedRelated, params[index].getValue());

        // set empty String array value
        request.appendParameter("related", "");
        params = request.asHttpParameterArray();
        index = Arrays.asList("related").indexOf(p -> p.getName().equals("related"));
        assertEquals("", params[index].getValue());

        // set null String array value
        request.appendParameter("related", null);
        params = request.asHttpParameterArray();
        index = Arrays.asList("related").indexOf(p -> p.getName().equals("related"));
        assertNull(params[index].getValue());
    }
}