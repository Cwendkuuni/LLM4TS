package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.setMaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.HideMedia();
        assertFalse(request.isHideMedia());

        request.HideThread();
        assertFalse(request.isHideThread());

        request.omitScript();
        assertTrue(request.isOmitScript());

        request.align(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.related(related);
        assertArrayEquals(related, request.getRelated());

        request.lang("en");
        assertEquals("en", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(13, params.length);

        HttpParameter idParam = params[0];
        assertEquals("id", idParam.getName());
        assertEquals(String.valueOf(1L), idParam.getValue());

        HttpParameter urlParam = params[1];
        assertEquals("url", urlParam.getName());
        assertEquals("http://example.com", urlParam.getValue());

        HttpParameter maxwidthParam = params[2];
        assertEquals("maxwidth", maxwidthParam.getName());
        assertEquals("0", maxwidthParam.getValue()); // default value

        HttpParameter hideMediaParam = params[3];
        assertEquals("hide_media", hideMediaParam.getName());
        assertEquals("true", hideMediaParam.getValue()); // default value

        HttpParameter hideThreadParam = params[4];
        assertEquals("hide_thread", hideThreadParam.getName());
        assertEquals("true", hideThreadParam.getValue()); // default value

        HttpParameter omitScriptParam = params[5];
        assertEquals("omit_script", omitScriptParam.getName());
        assertEquals("false", omitScriptParam.getValue()); // default value

        HttpParameter alignParam = params[6];
        assertEquals("align", alignParam.getName());
        assertEquals(Align.NONE.name().toLowerCase(), alignParam.getValue()); // default value

        if (request.getRelated() != null) {
            HttpParameter relatedParam = params[7];
            assertEquals("related", relatedParam.getName());
            assertEquals("", relatedParam.getValue()); // default value
            String[] relatedValues = request.getRelated();
            String joinedValues = StringUtil.join(relatedValues);
            assertEquals(joinedValues, relatedParam.getValue());
        } else {
            assertNull(params[7]);
        }

        HttpParameter langParam = params[8];
        assertEquals("lang", langParam.getName());
        assertNull(langParam.getValue()); // default value

        HttpParameter[] otherParams = new HttpParameter[5];
        assertEquals(otherParams, params.subArray(9, params.length));
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");

        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com/other");

        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.HideMedia();
        request5.HideThread();
        request5.omitScript();
        request5.align(Align.LEFT);
        request5.related(new String[]{"related1", "related2"});
        request5.lang("en");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertTrue(request1.equals(request5));

        HttpParameter[] params1 = request1.asHttpParameterArray();
        HttpParameter[] params5 = request5.asHttpParameterArray();
        assertArrayEquals(params1, params5);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com/other");

        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.HideMedia();
        request5.HideThread();
        request5.omitScript();
        request5.align(Align.LEFT);
        request5.related(new String[]{"related1", "related2"});
        request5.lang("en");

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();

        assertEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang=''}";
        assertEquals(expected, request.toString());

        request.setMaxWidth(200);
        request.HideMedia();
        request.HideThread();
        request.omitScript();
        request.align(Align.LEFT);
        request.related(new String[]{"related1", "related2"});
        request.lang("en");

        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expected, request.toString());
    }

}