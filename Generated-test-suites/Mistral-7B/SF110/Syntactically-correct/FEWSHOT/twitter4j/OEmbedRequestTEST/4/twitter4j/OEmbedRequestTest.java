package twitter4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testNewInstance() {
        final long statusId = 12345;
        final String url = "https://example.com/status/12345";
        final OEmbedRequest request = new OEmbedRequest(statusId, url);

        assertEquals(12345, request.getStatusId());
        assertEquals("https://example.com/status/12345", request.getUrl());
        assertFalse(request.isHideMedia());
        assertFalse(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertSame(Align.NONE, request.getAlign());
        assertNull(request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        final long statusId = 12345;
        final String url = "https://example.com/status/12345";
        final int maxWidth = 42;
        final boolean hideMedia = false;
        final boolean hideThread = true;
        final boolean omitScript = true;
        final Align align = Align.LEFT;
        final String[] related = {"related1", "related2"};
        final String lang = "en";

        final OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.MaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.setHideMedia(hideMedia);
        assertEquals(hideMedia, request.isHideMedia());

        request.HideMedia(hideMedia);
        assertEquals(hideMedia, request.isHideMedia());

        request.setHideThread(hideThread);
        assertEquals(hideThread, request.isHideThread());

        request.HideThread(hideThread);
        assertEquals(hideThread, request.isHideThread());

        request.setOmitScript(omitScript);
        assertEquals(omitScript, request.isOmitScript());

        request.omitScript(omitScript);
        assertEquals(omitScript, request.isOmitScript());

        request.setAlign(align);
        assertEquals(align, request.getAlign());

        request.align(align);
        assertEquals(align, request.getAlign());

        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(related);
        assertArrayEquals(related, request.getRelated());

        request.setLang(lang);
        assertEquals(lang, request.getLang());

        request.lang(lang);
        assertEquals(lang, request.getLang());
    }

    @Test
    public void testEqualsAndHashCode() {
        final long statusId = 12345;
        final String url = "https://example.com/status/12345";
        final int maxWidth = 42;
        final boolean hideMedia = false;
        final boolean hideThread = true;
        final boolean omitScript = true;
        final Align align = Align.LEFT;
        final String[] related = {"related1", "related2"};
        final String lang = "en";

        final OEmbedRequest request1 = new OEmbedRequest(statusId, url);
        final OEmbedRequest request2 = new OEmbedRequest(statusId, url);

        final OEmbedRequest request3 = new OEmbedRequest(statusId + 1, url);
        final OEmbedRequest request4 = new OEmbedRequest(statusId, url + "x");

        final OEmbedRequest request5 = new OEmbedRequest(statusId, url);
        request5.setMaxWidth(maxWidth);
        final OEmbedRequest request6 = new OEmbedRequest(statusId, url);
        request6.setMaxWidth(maxWidth + 1);

        final OEmbedRequest request7 = new OEmbedRequest(statusId, url);
        request7.setHideMedia(hideMedia);
        final OEmbedRequest request8 = new OEmbedRequest(statusId, url);
        request8.setHideMedia(false);

        final OEmbedRequest request9 = new OEmbedRequest(statusId, url);
        request9.setRelated(related);
        final OEmbedRequest request10 = new OEmbedRequest(statusId, url);
        request10.setRelated(new String[0]);

        final OEmbedRequest request11 = new OEmbedRequest(statusId, url);
        request11.setLang(lang);
        final OEmbedRequest request12 = new OEmbedRequest(statusId, url);
        request12.setLang(null);

        final OEmbedRequest request13 = new OEmbedRequest(statusId, url);
        request13.setAlign(align);
        final OEmbedRequest request14 = new OEmbedRequest(statusId, url);
        request14.setAlign(Align.CENTER);

        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request5);
        assertNotEquals(request1, request6);
        assertNotEquals(request1, request7);
        assertNotEquals(request1, request8);
        assertNotEquals(request1, request9);
        assertNotEquals(request1, request10);
        assertNotEquals(request1, request11);
        assertNotEquals(request1, request12);
        assertNotEquals(request1, request13);

        assertTrue(request1.equals(request2));
        assertTrue(request1.equals(request5));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
        assertFalse(request1.equals(request8));
        assertFalse(request1.equals(request9));
        assertFalse(request1.equals(request10));
        assertFalse(request1.equals(request12));
        assertFalse(request1.equals(request13));

        int hashcode1 = request1.hashCode();
        int hashcode2 = request2.hashCode();
        assertEquals(hashcode1, hashcode2);

        int hashcode3 = request3.hashCode();
        assertNotEquals(hashcode1, hashcode3);

        int hashcode4 = request4.hashCode();
        assertNotEquals(hashcode1, hashcode4);

        int hashcode5 = request5.hashCode();
        assertEquals(hashcode1, hashcode5);

        int hashcode6 = request6.hashCode();
        assertNotEquals(hashcode1, hashcode6);

        int hashcode7 = request7.hashCode();
        assertNotEquals(hashcode1, hashcode7);

        int hashcode8 = request8.hashCode();
        assertNotEquals(hashcode1, hashcode8);

        int hashcode9 = request9.hashCode();
        assertNotEquals(hashcode1, hashcode9);

        int hashcode10 = request10.hashCode();
        assertNotEquals(hashcode1, hashcode10);

        int hashcode11 = request11.hashCode();
        assertNotEquals(hashcode1, hashcode11);

        int hashcode12 = request12.hashCode();
        assertNotEquals(hashcode1, hashcode12);

        int hashcode13 = request13.hashCode();
        assertNotEquals(hashcode1, hashcode13);
    }

    @Test
    public void testToString() {
        final long statusId = 12345;
        final String url = "https://example.com/status/12345";
        final int maxWidth = 42;
        final boolean hideMedia = false;
        final boolean hideThread = true;
        final boolean omitScript = true;
        final Align align = Align.LEFT;
        final String[] related = {"related1", "related2"};
        final String lang = "en";

        final OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com/status/12345', maxWidth=42, hideMedia=false, hideThread=true, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}", request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        final long statusId = 12345;
        final String url = "https://example.com/status/12345";
        final int maxWidth = 42;
        final boolean hideMedia = false;
        final boolean hideThread = true;
        final boolean omitScript = true;
        final Align align = Align.LEFT;
        final String[] related = {"related1", "related2"};
        final String lang = "en";

        final OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        HttpParameter[] params = request.asHttpParameterArray();

        assertEquals(13, params.length);
        assertEquals("id", params[0].getName());
        assertEquals(Long.toString(statusId), params[0].getValue());
        assertEquals("url", params[1].getName());
        assertEquals(url, params[1].getValue());
        assertEquals("maxwidth", params[2].getName());
        assertEquals(Integer.toString(maxWidth), params[2].getValue());
        assertEquals("hide_media", params[3].getName());
        assertEquals(Boolean.toString(hideMedia), params[3].getValue());
        assertEquals("hide_thread", params[4].getName());
        assertEquals(Boolean.toString(hideThread), params[4].getValue());
        assertEquals("omit_script", params[5].getName());
        assertEquals(Boolean.toString(omitScript), params[5].getValue());
        assertEquals("align", params[6].getName());
        assertEquals(align.name().toLowerCase(), params[6].getValue());
        assertEquals("related", params[7].getName());
        assertEquals(StringUtil.join(related), params[7].getValue());
        assertEquals("lang", params[8].getName());
        assertEquals(lang, params[8].getValue());
    }

    @Test
    public void testAppendParameter() {
        final List<HttpParameter> list = new ArrayList<HttpParameter>();

        request.appendParameter("id", 12345, list);
        request.appendParameter("url", "https://example.com/status/12345", list);

        assertEquals(2, list.size());
        assertEquals("id", list.get(0).getName());
        assertEquals(Long.toString(12345), list.get(0).getValue());
        assertEquals("url", list.get(1).getName());
        assertEquals("https://example.com/status/12345", list.get(1).getValue());

        request.appendParameter("maxwidth", 42, list);

        assertEquals(3, list.size());
        assertEquals("maxwidth", list.get(1).getName());
        assertEquals(Integer.toString(42), list.get(1).getValue());
    }
}