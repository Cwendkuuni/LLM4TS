import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.HttpParameter;
import java.util.Arrays;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        assertEquals(1L, request.statusId);
        assertEquals("http://example.com", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertNull(request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        request.setMaxWidth(100);
        assertEquals(100, request.getMaxWidth());

        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        assertFalse(request.hideMedia);

        request.HideMedia(true);
        assertTrue(request.hideMedia);

        request.setHideThread(false);
        assertFalse(request.hideThread);

        request.HideThread(true);
        assertTrue(request.hideThread);

        request.setOmitScript(true);
        assertTrue(request.omitScript);

        request.omitScript(false);
        assertFalse(request.omitScript);

        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.getAlign());

        request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.getAlign());

        request.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.getAlign());

        request.align(OEmbedRequest.Align.NONE);
        assertEquals(OEmbedRequest.Align.NONE, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);

        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.related);

        request.setLang("en");
        assertEquals("en", request.lang);

        request.lang("fr");
        assertEquals("fr", request.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);

        HttpParameter idParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("id")) {
                idParam = param;
                break;
            }
        }

        assertEquals("1", idParam.getValue());

        HttpParameter urlParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("url")) {
                urlParam = param;
                break;
            }
        }

        assertEquals("http://example.com", urlParam.getValue());

        HttpParameter maxWidthParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("maxwidth")) {
                maxWidthParam = param;
                break;
            }
        }

        assertEquals("0", maxWidthParam.getValue());

        HttpParameter hideMediaParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("hide_media")) {
                hideMediaParam = param;
                break;
            }
        }

        assertTrue(hideMediaParam.getValue().equals("true") || hideMediaParam.getValue().equals("1"));

        HttpParameter hideThreadParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("hide_thread")) {
                hideThreadParam = param;
                break;
            }
        }

        assertTrue(hideThreadParam.getValue().equals("true") || hideThreadParam.getValue().equals("1"));

        HttpParameter omitScriptParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("omit_script")) {
                omitScriptParam = param;
                break;
            }
        }

        assertFalse(omitScriptParam.getValue().equals("true") || omitScriptParam.getValue().equals("1"));

        HttpParameter alignParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("align")) {
                alignParam = param;
                break;
            }
        }

        assertEquals(OEmbedRequest.Align.NONE.name().toLowerCase(), alignParam.getValue());

        if (request.related.length > 0) {
            HttpParameter relatedParam = null;
            for (HttpParameter param : params) {
                if (param.getName().equals("related")) {
                    relatedParam = param;
                    break;
                }
            }

            String expectedRelated = StringUtil.join(request.related);
            assertEquals(expectedRelated, relatedParam.getValue());
        }

        HttpParameter langParam = null;
        for (HttpParameter param : params) {
            if (param.getName().equals("lang")) {
                langParam = param;
                break;
            }
        }

        assertEquals(request.lang, langParam.getValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
        OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request5 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request6 = new OEmbedRequest(1L, "http://example.com");
        OEmbedRequest request7 = new OEmbedRequest(1L, "http://example2.com");

        request1.setMaxWidth(100);
        request2.setMaxWidth(100);
        request3.setMaxWidth(200);
        request4.setMaxWidth(100);
        request5.setMaxWidth(100);
        request6.setMaxWidth(100);
        request7.setMaxWidth(100);

        request1.setHideMedia(false);
        request2.setHideMedia(false);
        request3.setHideMedia(true);
        request4.setHideMedia(false);
        request5.setHideMedia(false);
        request6.setHideMedia(false);
        request7.setHideMedia(false);

        request1.setHideThread(false);
        request2.setHideThread(false);
        request3.setHideThread(true);
        request4.setHideThread(false);
        request5.setHideThread(false);
        request6.setHideThread(false);
        request7.setHideThread(false);

        request1.setOmitScript(true);
        request2.setOmitScript(false);
        request3.setOmitScript(false);
        request4.setOmitScript(true);
        request5.setOmitScript(false);
        request6.setOmitScript(false);
        request7.setOmitScript(false);

        request1.setAlign(OEmbedRequest.Align.LEFT);
        request2.setAlign(OEmbedRequest.Align.CENTER);
        request3.setAlign(OEmbedRequest.Align.RIGHT);
        request4.setAlign(OEmbedRequest.Align.NONE);
        request5.setAlign(OEmbedRequest.Align.LEFT);
        request6.setAlign(OEmbedRequest.Align.LEFT);
        request7.setAlign(OEmbedRequest.Align.NONE);

        String[] related1 = new String[]{"related1", "related2"};
        String[] related2 = new String[]{"related1", "related3"};
        request1.setRelated(related1);
        request2.setRelated(related2);
        request3.setRelated(related2);
        request4.setRelated(related1);
        request5.setRelated(related1);
        request6.setRelated(related1);
        request7.setRelated(related1);

        String lang1 = "en";
        String lang2 = "fr";
        request1.setLang(lang1);
        request2.setLang(lang2);
        request3.setLang(lang1);
        request4.setLang(lang1);
        request5.setLang(lang1);
        request6.setLang(lang1);
        request7.setLang(lang1);

        assertFalse(request1.equals(null));
        assertFalse(request1.equals(request3));
        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request4));
        assertTrue(request1.equals(request5));
        assertTrue(request1.equals(request6));
        assertFalse(request1.equals(request2));
        assertFalse(request1.equals(request7));

        int hashCode1 = request1.hashCode();
        int hashCode2 = request2.hashCode();
        int hashCode3 = request3.hashCode();
        int hashCode4 = request4.hashCode();
        int hashCode5 = request5.hashCode();
        int hashCode6 = request6.hashCode();
        int hashCode7 = request7.hashCode();

        assertEquals(hashCode1, hashCode4);
        assertEquals(hashCode1, hashCode5);
        assertEquals(hashCode1, hashCode6);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode7);
    }
}