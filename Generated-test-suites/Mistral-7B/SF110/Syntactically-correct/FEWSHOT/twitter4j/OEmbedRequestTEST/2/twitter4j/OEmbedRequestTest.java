package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
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
        assertEquals(related, request.getRelated());

        request.lang("en");
        assertEquals("en", request.getLang());

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);
        for (HttpParameter param : params) {
            if (param.getName().equals("id")) {
                assertEquals(1L, Long.parseLong(param.getValue()));
            } else if (param.getName().equals("url")) {
                assertEquals("http://example.com", param.getValue());
            }
        }
    }

    @Test
    public void testEqualsAndHashcode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");

        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");

        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(200);
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setMaxWidth(200);

        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setRelated(new String[]{"related1", "related2"});
        OEmbedRequest request8 = new OEmbedRequest(1L, "http://example.com");
        request8.setRelated(new String[]{"related1", "related2"});

        OEmbedRequest request9 = new OEmbedRequest(1L, "http://example.com");
        request9.setLang("en");
        OEmbedRequest request10 = new OEmbedRequest(1L, "http://example.com");
        request10.setLang("en");

        OEmbedRequest request11 = new OEmbedRequest(1L, "http://example.com");
        request11.setHideMedia(false);
        OEmbedRequest request12 = new OEmbedRequest(1L, "http://example.com");
        request12.setHideMedia(false);

        assertTrue(request1.equals(request2));
        assertTrue(request1.hashCode() == request2.hashCode());
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
        assertFalse(request1.equals(request8));
        assertFalse(request1.equals(request9));
        assertFalse(request1.equals(request10));
        assertFalse(request1.equals(request11));
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAlignEnumValues() {
        List<Align> values = Arrays.asList(Align.LEFT, Align.CENTER, Align.RIGHT, Align.NONE);
        for (Align value : values) {
            assertEquals(value.name(), value.toString());
        }
    }
}