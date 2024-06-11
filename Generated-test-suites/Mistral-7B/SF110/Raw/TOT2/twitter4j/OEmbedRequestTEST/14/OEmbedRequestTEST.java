import org.junit.Assert;
import org.junit.Test;
import twitter4j.Align;
import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertNotNull(oEmbedRequest);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOMitScript());
        assertNull(oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSetMaxWidthAndMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.getMaxWidth());
        oEmbedRequest.MaxWidth(200);
        assertEquals(200, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "https://example.com");

        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));

        oEmbedRequest2.setMaxWidth(200);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));

        oEmbedRequest2.setMaxWidth(100);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));

        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "https://example.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));

        oEmbedRequest3.setStatusId(1L);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest3));
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://example.com");
        OEmbedRequest oEmbedRequestWithParams = new OEmbedRequest(1L, "https://example.com")
                .setMaxWidth(100)
                .hideMedia()
                .hideThread()
                .omitScript()
                .align(Align.LEFT)
                .related(new String[]{"related1", "related2"})
                .lang("en");

        HttpParameter[] oEmbedRequestParams = oEmbedRequest.asHttpParameterArray();
        HttpParameter[] oEmbedRequestWithParamsParams = oEmbedRequestWithParams.asHttpParameterArray();

        assertArrayEquals(oEmbedRequestParams, new HttpParameter[12]);
        assertArrayEquals(oEmbedRequestWithParamsParams, new HttpParameter[]{
                new HttpParameter("id", "1"),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", "100"),
                new HttpParameter("hide_media", "true"),
                new HttpParameter("hide_thread", "true"),
                new HttpParameter("omit_script", "true"),
                new HttpParameter("align", "left"),
                new HttpParameter("related", "related1,related2"),
                new HttpParameter("lang", "en")
        });
    }
}