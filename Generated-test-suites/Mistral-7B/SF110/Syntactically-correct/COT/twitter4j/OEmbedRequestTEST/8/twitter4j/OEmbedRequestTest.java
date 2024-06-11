import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.Align;
import java.util.Arrays;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
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

        request.setAlign(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());

        request.align(Align.NONE);
        assertEquals(Align.NONE, request.getAlign());

        String[] related = new String[]{"rel1", "rel2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(new String[]{"rel3", "rel4"});
        assertArrayEquals(new String[]{"rel3", "rel4"}, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);
        for (int i = 0; i < 12; i++) {
            if (i < 10) {
                assertEquals(Integer.toString(i + 1), params[i].getName());
                if (i == 1) {
                    assertEquals("1", params[i].getValue());
                } else {
                    assertNull(params[i].getValue());
                }
            } else {
                assertEquals("align", params[i].getName());
                assertEquals(Align.NONE.name().toLowerCase(), params[i].getValue());
            }
        }

        request.setMaxWidth(100);
        params = request.asHttpParameterArray();
        assertEquals(13, params.length);
        assertHttpParameterEquals("maxwidth", "100", params[11]);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");

        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        request4.setMaxWidth(100);
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(100);

        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setAlign(Align.LEFT);
        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setAlign(Align.LEFT);

        OEmbedRequest request8 = new OEmbedRequest(1L, "http://example.com");
        request8.setRelated(new String[]{"rel1"});
        OEmbedRequest request9 = new OEmbedRequest(1L, "http://example.com");
        request9.setRelated(new String[]{"rel1"});

        OEmbedRequest request10 = new OEmbedRequest(1L, "http://example.com");
        request10.setLang("en");
        OEmbedRequest request11 = new OEmbedRequest(1L, "http://example.com");
        request11.setLang("en");

        OEmbedRequest request12 = new OEmbedRequest(1L, "http://example.com");
        request12.setUrl("http://example2.com");

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request4));
        assertTrue(request1.equals(request5));
        assertFalse(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
        assertFalse(request1.equals(request8));
        assertFalse(request1.equals(request9));
        assertFalse(request1.equals(request10));
        assertFalse(request1.equals(request11));
        assertFalse(request1.equals(request12));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");

        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        request4.setMaxWidth(100);

        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(100);

        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setAlign(Align.LEFT);

        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setAlign(Align.LEFT);

        OEmbedRequest request8 = new OEmbedRequest(1L, "http://example.com");
        request8.setRelated(new String[]{"rel1"});

        OEmbedRequest request9 = new OEmbedRequest(1L, "http://example.com");
        request9.setRelated(new String[]{"rel1"});

        OEmbedRequest request10 = new OEmbedRequest(1L, "http://example.com");
        request10.setLang("en");

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();
        int hashCode6 = request6.hashCode();
        int hashCode7 = request7.hashCode();
        int hashCode8 = request8.hashCode();
        int hashCode9 = request9.hashCode();
        int hashCode10 = request10.hashCode();

        assertEquals(hashCode1, hashCode4);
        assertEquals(hashCode1, hashCode5);
        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode6);
        assertNotSame(hashCode1, hashCode7);
        assertNotSame(hashCode1, hashCode8);
        assertNotSame(hashCode1, hashCode9);
        assertNotSame(hashCode1, hashCode10);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, request.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setAlign(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setRelated(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setLang(null);
    }

    private void assertHttpParameterEquals(String name, String value, HttpParameter param) {
        assertEquals(name, param.getName());
        assertEquals(value, param.getValue());
    }
}