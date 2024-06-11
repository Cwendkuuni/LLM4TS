
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Arrays;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 123456L;
        String url = "https://example.com/status/123456";
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
        OEmbedRequest request = new OEmbedRequest(123456L, "https://example.com/status/123456");
        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());
    }

    @Test
    public void testSetMaxWidthNegative() {
        OEmbedRequest request = new OEmbedRequest(123456L, "https://example.com/status/123456");
        request.setMaxWidth(-1);
        assertEquals(-1, request.getMaxWidth());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456L, "https://example.com/status/123456");
        request.setMaxWidth(100);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.RIGHT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");

        List<HttpParameter> params = Arrays.asList(request.asHttpParameterArray());

        assertEquals(12, params.size());
        assertEquals("id", params.get(0).getName());
        assertEquals("123456", params.get(0).getValue());
        assertEquals("maxwidth", params.get(2).getName());
        assertEquals("100", params.get(2).getValue());
        // Add other assertions for other properties
    }
}
