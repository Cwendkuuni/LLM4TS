package twitter4j;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OEmbedRequestTest {

    private static final long STATUS_ID_SAME = 1234567890L;
    private static final long STATUS_ID_DIFFERENT = 1234567891L;
    private static final String URL_SAME = "https://twitter.com/example/status/123";
    private static final String URL_DIFFERENT = "https://twitter.com/example/status/124";
    private static final int MAX_WIDTH = 300;
    private static final int MAX_WIDTH_NEGATIVE = -10;
    private static final String LANG_SAME = "en";
    private static final String LANG_DIFFERENT = "fr";
    private static final String[] RELATED_SAME = new String[0];
    private static final String[] RELATED_DIFFERENT = {"https://example.com/related1", "https://example.com/related2"};

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1234567890L;
        String url = "https://twitter.com/example/status/123";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertEquals(Align.NONE, request.getAlign());
        assertEquals(new String[0], request.getRelated());
        assertEquals("none", request.getAlign().name());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(STATUS_ID_SAME, URL_SAME);
        request.setMaxWidth(MAX_WIDTH);
        request.MaxWidth(MAX_WIDTH);
        assertEquals(MAX_WIDTH, request.getMaxWidth());

        request.setMaxWidth(MAX_WIDTH_NEGATIVE);
        request.MaxWidth(MAX_WIDTH_NEGATIVE);
        assertEquals(0, request.getMaxWidth());
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request1 = new OEmbedRequest(STATUS_ID_SAME, URL_SAME);
        request1.setLang(LANG_SAME);
        request1.lang(LANG_SAME);
        assertEquals(LANG_SAME, request1.getLang());

        OEmbedRequest request2 = new OEmbedRequest(STATUS_ID_SAME, URL_SAME);
        request2.setLang(LANG_SAME);
        request2.lang(LANG_SAME);
        assertTrue(request1.equals(request2));

        OEmbedRequest request3 = new OEmbedRequest(STATUS_ID_SAME, URL_SAME);
        request3.setLang(LANG_DIFFERENT);
        request3.lang(LANG_DIFFERENT);
        assertFalse(request1.equals(request3));

        OEmbedRequest request4 = new OEmbedRequest(STATUS_ID_DIFFERENT, URL_SAME);
        request4.setLang(LANG_SAME);
        request4.lang(LANG_SAME);
        assertFalse(request1.equals(request4));
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request1 = new OEmbedRequest(STATUS_ID_SAME, URL_SAME);
        String[] related1 = new String[0];
        request1.setRelated(related1);
        request1.related(related1);
        assertEquals(related1, request1.getRelated());

        String[] related2 = {"https://example.com/related1", "https://example.com/related2"};
        request1.setRelated(related2);
        request1.related(related2);
        assertEquals(related2, request1.getRelated());
    }
}