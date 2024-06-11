package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 123456L;
        String url = "https://example.com/status/123456";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSetters() {
        long statusId = 123456L;
        String url = "https://example.com/status/123456";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(420);
        request.MaxWidth(480);
        assertEquals(480, request.getMaxWidth());

        request.setHideMedia(false);
        request.HideMedia(true);
        assertTrue(request.hideMedia);

        request.setHideThread(false);
        request.HideThread(true);
        assertTrue(request.hideThread);

        request.setOmitScript(true);
        request.omitScript(false);
        assertFalse(request.omitScript);

        Align align = Align.CENTER;
        request.setAlign(align);
        request.align(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        String[] related = new String[]{"related1", "related2", "related3"};
        request.setRelated(related);
        request.related(new String[]{"related4", "related5"});
        assertArrayEquals(new String[]{"related4", "related5"}, request.getRelated());

        request.setLang("en");
        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        long statusId = 123456L;
        String url = "https://example.com/status/123456";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(420);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.CENTER);
        String[] related = new String[]{"related1", "related2", "related3"};
        request.setRelated(related);
        request.setLang("en");

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
        outStream.writeObject(request);
        outStream.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream inStream = new ObjectInputStream(byteInput);
        OEmbedRequest requestCopy = (OEmbedRequest)inStream.readObject();
        inStream.close();

        assertEquals(request.getStatusId(), requestCopy.getStatusId());
        assertEquals(request.getUrl(), requestCopy.getUrl());
        assertEquals(request.getMaxWidth(), requestCopy.getMaxWidth());
        assertEquals(request.hideMedia, requestCopy.hideMedia);
        assertEquals(request.hideThread, requestCopy.hideThread);
        assertEquals(request.omitScript, requestCopy.omitScript);
        assertEquals(request.getAlign(), requestCopy.getAlign());
        assertArrayEquals(request.getRelated(), requestCopy.getRelated());
        assertEquals(request.getLang(), requestCopy.getLang());
    }

    @Test
    public void testEquals() {
        long statusId1 = 123456L;
        long statusId2 = 123457L;
        String url1 = "https://example.com/status/123456";
        String url2 = "https://example.com/status/123457";

        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request3));
        assertFalse(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        long statusId1 = 123456L;
        long statusId2 = 123457L;
        String url1 = "https://example.com/status/123456";
        String url2 = "https://example.com/status/123457";

        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();

        assertTrue(hashCode1 == hashCode3);
        assertFalse(hashCode1 == hashCode2);
    }

    @Test
    public void testToString() {
        long statusId = 123456L;
        String url = "https://example.com/status/123456";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(420);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.CENTER);
        String[] related = new String[]{"related1", "related2", "related3"};
        request.setRelated(related);
        request.setLang("en");

        String expected = "OEmbedRequest{statusId=" + statusId + ", url='" + url + "', maxWidth=420, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[related1, related2, related3], lang='en'}";
        assertEquals(expected, request.toString());
    }

    @Test
    public void testAlignValues() {
        for (Align align : Align.values()) {
            assertEquals(align.name().toLowerCase(), align.toString());
        }
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 123456L;
        String url = "https://example.com/status/123456";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(420);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.CENTER);
        String[] related = new String[]{"related1", "related2", "related3"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        List<String> expectedParams = Arrays.asList(
                "id=" + statusId,
                "url=" + url,
                "maxwidth=420",
                "hide_media=" + request.hideMedia,
                "hide_thread=" + request.hideThread,
                "omit_script=" + request.omitScript,
                "align=" + Align.CENTER.name().toLowerCase(),
                "related=" + StringUtil.join(related),
                "lang=en"
        );

        List<String> actualParams = Arrays.asList(Arrays.stream(params).map(p -> p.getName() + "=" + p.getValue()).collect(Collectors.toList()));
        assertEquals(expectedParams, actualParams);
    }
}