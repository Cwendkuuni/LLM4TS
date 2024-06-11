package twitter4j;

import static org.junit.Assert.*;
import java.util.Arrays;

import org.junit.Test;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());
        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());
    }
    
    @Test
    public void testEquals() {
        long statusId1 = 1L;
        long statusId2 = 2L;
        String url1 = "https://example.com/status/1";
        String url2 = "https://example.com/status/2";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        assertFalse(request1.equals(request2));
        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);
        assertTrue(request1.equals(request3));
        request3.setMaxWidth(100);
        assertFalse(request1.equals(request3));
    }
    
    @Test
    public void testAlignment() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        assertEquals(Align.NONE, request.getAlign());
        request.align(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());
        request.align(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());
        request.align(Align.CENTER);
        assertEquals(Align.CENTER, request.getAlign());
    }
    
    @Test
    public void testRelated() {
        String[] related = {"related1", "related2"};
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());
        String related1 = "related3";
        request.related(new String[]{related1});
        assertArrayEquals(new String[]{related1}, request.getRelated());
    }
    
    @Test
    public void testLang() {
        String lang = "lang1";
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        request.setLang(lang);
        assertEquals(lang, request.getLang());
        request.lang("lang2");
        assertEquals("lang2", request.getLang());
    }
    
    @Test
    public void testSerialization() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(100);
        request.setHideMedia(true);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("lang1");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(request);
            out.close();
            byte[] bytes = baos.toByteArray();
            OEmbedRequest request2 = (OEmbedRequest) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
            assertEquals(statusId, request2.getStatusId());
            assertEquals(url, request2.getUrl());
            assertEquals(request.getMaxWidth(), request2.getMaxWidth());
            assertEquals(request.isHideMedia(), request2.isHideMedia());
            assertEquals(request.isHideThread(), request2.isHideThread());
            assertEquals(request.isOmitScript(), request2.isOmitScript());
            assertEquals(request.getAlign(), request2.getAlign());
            assertArrayEquals(request.getRelated(), request2.getRelated());
            assertEquals(request.getLang(), request2.getLang());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testEmptyConstructor() {
        OEmbedRequest request = new OEmbedRequest();
        assertEquals(0L, request.getStatusId());
        assertNull(request.getUrl());
        assertFalse(request.isHideMedia());
        assertFalse(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }
}