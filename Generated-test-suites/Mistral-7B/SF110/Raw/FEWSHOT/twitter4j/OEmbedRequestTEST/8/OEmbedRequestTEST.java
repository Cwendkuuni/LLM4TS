package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
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

        request.setAlign(Align.LEFT);
        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());

        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.related(new String[]{"related3"});
        assertArrayEquals(new String[]{"related3"}, request.getRelated());

        request.setLang("en");
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
        request5.setMaxWidth(100);
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setMaxWidth(100);

        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setRelated(new String[]{"related1", "related2"});
        OEmbedRequest request8 = new OEmbedRequest(1L, "http://example.com");
        request8.setRelated(new String[]{"related1", "related2"});

        OEmbedRequest request9 = new OEmbedRequest(1L, "http://example.com");
        request9.setLang("en");
        OEmbedRequest request10 = new OEmbedRequest(1L, "http://example.com");
        request10.setLang("en");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertTrue(request1.equals(request4));

        assertTrue(request1.hashCode() == request2.hashCode());
        assertNotSame(request1.hashCode(), request3.hashCode());
        assertNotSame(request1.hashCode(), request5.hashCode());
        assertNotSame(request1.hashCode(), request6.hashCode());
        assertNotSame(request1.hashCode(), request7.hashCode());
        assertNotSame(request1.hashCode(), request8.hashCode());
        assertNotSame(request1.hashCode(), request9.hashCode());
        assertNotSame(request1.hashCode(), request10.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, request.toString());

        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(false);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=false, align=NONE, related=[related1, related2], lang='en'}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] parameters = request.asHttpParameterArray();
        assertArrayEquals(new HttpParameter[0], parameters);

        request.setMaxWidth(100);
        parameters = request.asHttpParameterArray();
        HttpParameter maxWidthParameter = parameters[0];
        assertEquals("maxwidth", maxWidthParameter.getName());
        assertEquals("100", maxWidthParameter.getValue());

        request.setHideMedia(false);
        parameters = request.asHttpParameterArray();
        HttpParameter hideMediaParameter = parameters[parameters.length - 1];
        assertEquals("hide_media", hideMediaParameter.getName());
        assertEquals("false", hideMediaParameter.getValue());

        request.setRelated(new String[]{"related1", "related2"});
        parameters = request.asHttpParameterArray();
        HttpParameter relatedParameter = parameters[parameters.length - 1];
        assertEquals("related", relatedParameter.getName());
        assertEquals("related1,related2", relatedParameter.getValue());
    }
}