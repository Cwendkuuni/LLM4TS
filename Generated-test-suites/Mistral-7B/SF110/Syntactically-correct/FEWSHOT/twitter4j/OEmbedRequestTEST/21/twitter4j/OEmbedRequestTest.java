package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.getStatusId());
        assertEquals("http://example.com", request.getUrl());
        assertEquals(Align.NONE, request.getAlign());
        assertNull(request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSetters() {
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

        Align align = Align.LEFT;
        request.setAlign(align);
        assertSame(align, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(123);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("id", String.valueOf(request.getStatusId())));
        expectedParams.add(new HttpParameter("url", request.getUrl()));
        expectedParams.add(new HttpParameter("maxwidth", String.valueOf(request.getMaxWidth())));
        expectedParams.add(new HttpParameter("hide_media", String.valueOf(request.isHideMedia())));
        expectedParams.add(new HttpParameter("hide_thread", String.valueOf(request.isHideThread())));
        expectedParams.add(new HttpParameter("omit_script", String.valueOf(request.isOmitScript())));
        expectedParams.add(new HttpParameter("align", Align.LEFT.name().toLowerCase()));
        if (request.getRelated() != null) {
            expectedParams.add(new HttpParameter("related", StringUtil.join(request.getRelated())));
        }
        expectedParams.add(new HttpParameter("lang", request.getLang()));

        assertArrayEquals(expectedParams.toArray(new HttpParameter[expectedParams.size()]), params);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(123);
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setMaxWidth(456);
        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setUrl("http://another.com");

        List<OEmbedRequest> requests = Arrays.asList(request1, request2, request3, request4, request5, request6, request7);

        for (int i = 0; i < requests.size(); i++) {
            for (int j = i + 1; j < requests.size(); j++) {
                if (requests.get(i) == requests.get(j)) {
                    continue;
                }
                assertFalse(requests.get(i).equals(requests.get(j)));
            }
        }

        assertFalse(request1.equals(null));
        assertFalse(request1.equals(new Object()));
        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request4));
        assertFalse(request1.equals(request5));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        request5.setMaxWidth(123);
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        request6.setMaxWidth(456);
        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example.com");
        request7.setUrl("http://another.com");

        List<OEmbedRequest> requests = Arrays.asList(request1, request2, request3, request4, request5, request6, request7);

        for (int i = 0; i < requests.size(); i++) {
            for (int j = i + 1; j < requests.size(); j++) {
                if (requests.get(i).hashCode() == requests.get(j).hashCode()) {
                    continue;
                }
                assertFalse(requests.get(i).equals(requests.get(j)));
                assertFalse(requests.get(i).hashCode() == requests.get(j).hashCode());
            }
        }

        int request1HashCode = request1.hashCode();
        assertEquals(request1.hashCode(), request1HashCode);
        assertEquals(request1HashCode, request4.hashCode());
        assertNotEquals(request1HashCode, request5.hashCode());
        assertNotEquals(request1HashCode, request6.hashCode());
        assertNotEquals(request1HashCode, request7.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setMaxWidth(123);
        request.setHideMedia(false);
        request.setHideThread(true);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        String expectedString = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=123, hideMedia=false, hideThread=true, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        assertEquals(expectedString, request.toString());
    }
}