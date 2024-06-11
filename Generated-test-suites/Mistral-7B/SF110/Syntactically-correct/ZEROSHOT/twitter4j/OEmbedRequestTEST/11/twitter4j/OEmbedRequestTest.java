package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        int maxWidth = 42;
        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.getMaxWidth());

        request.MaxWidth(56);
        assertEquals(56, request.getMaxWidth());

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
        assertEquals(align, request.getAlign());

        request.align(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());

        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        String lang = "en";
        request.setLang(lang);
        assertEquals(lang, request.getLang());

        request.lang("fr");
        assertEquals("fr", request.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(42);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        String lang = "en";
        request.setLang(lang);

        List<HttpParameter> params = Arrays.asList(request.asHttpParameterArray());
        assertEquals(12, params.size());

        HttpParameter idParam = findParameter(params, "id");
        assertEquals(Long.toString(statusId), idParam.getValue());

        HttpParameter urlParam = findParameter(params, "url");
        assertEquals(url, urlParam.getValue());

        HttpParameter maxWidthParam = findParameter(params, "maxwidth");
        assertEquals("42", maxWidthParam.getValue());

        HttpParameter hideMediaParam = findParameter(params, "hide_media");
        assertFalse(hideMediaParam.getValue().equals("true"));

        HttpParameter hideThreadParam = findParameter(params, "hide_thread");
        assertFalse(hideThreadParam.getValue().equals("true"));

        HttpParameter omitScriptParam = findParameter(params, "omit_script");
        assertTrue(omitScriptParam.getValue().equals("true"));

        HttpParameter alignParam = findParameter(params, "align");
        assertEquals(Align.LEFT.name().toLowerCase(), alignParam.getName());

        HttpParameter relatedParam = findParameter(params, "related");
        assertEquals(StringUtil.join(related, ","), relatedParam.getValue());

        HttpParameter langParam = findParameter(params, "lang");
        assertEquals(lang, langParam.getValue());
    }

    private HttpParameter findParameter(List<HttpParameter> params, String name) {
        for (HttpParameter param : params) {
            if (param.getName().equals(name)) {
                return param;
            }
        }
        fail("Parameter not found: " + name);
        return null;
    }

    @Test
    public void testEquality() {
        long statusId1 = 1L;
        String url1 = "http://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);
        request1.setMaxWidth(42);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(Align.LEFT);
        String[] related1 = new String[]{"related1", "related2"};
        request1.setRelated(related1);
        String lang1 = "en";
        request1.setLang(lang1);

        long statusId2 = 1L;
        String url2 = "http://example.com";
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        request2.setMaxWidth(42);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(Align.LEFT);
        String[] related2 = new String[]{"related1", "related2"};
        request2.setRelated(related2);
        String lang2 = "en";
        request2.setLang(lang2);

        OEmbedRequest request3 = new OEmbedRequest(statusId1, url1);
        request3.setMaxWidth(56);
        request3.setHideMedia(false);
        request3.setHideThread(false);
        request3.setOmitScript(true);
        request3.setAlign(Align.LEFT);
        String[] related3 = new String[]{"related1", "related2", "related3"};
        request3.setRelated(related3);
        String lang3 = "en";
        request3.setLang(lang3);

        OEmbedRequest request4 = new OEmbedRequest(statusId1, url1);
        request4.setMaxWidth(42);
        request4.setHideMedia(false);
        request4.setHideThread(false);
        request4.setOmitScript(false);
        request4.setAlign(Align.LEFT);
        String[] related4 = new String[]{"related1", "related2"};
        request4.setRelated(related4);
        String lang4 = "en";
        request4.setLang(lang4);

        OEmbedRequest request5 = new OEmbedRequest(statusId1, url1);
        request5.setMaxWidth(42);
        request5.setHideMedia(false);
        request5.setHideThread(false);
        request5.setOmitScript(true);
        request5.setAlign(Align.LEFT);
        String[] related5 = new String[]{"related1", "related2"};
        request5.setRelated(related5);
        String lang5 = "fr";
        request5.setLang(lang5);

        OEmbedRequest request6 = new OEmbedRequest(statusId2, url2);
        request6.setMaxWidth(42);
        request6.setHideMedia(false);
        request6.setHideThread(false);
        request6.setOmitScript(true);
        request6.setAlign(Align.LEFT);
        String[] related6 = new String[]{"related1", "related2"};
        request6.setRelated(related6);
        String lang6 = "en";
        request6.setLang(lang6);

        OEmbedRequest request7 = new OEmbedRequest(statusId1, url1);
        request7.setMaxWidth(42);
        request7.setHideMedia(false);
        request7.setHideThread(false);
        request7.setOmitScript(true);
        request7.setAlign(Align.LEFT);
        String[] related7 = new String[]{"related1", "related2"};
        request7.setRelated(related7);
        String lang7 = null;
        request7.setLang(lang7);

        OEmbedRequest request8 = new OEmbedRequest(statusId1, url1);
        request8.setMaxWidth(42);
        request8.setHideMedia(false);
        request8.setHideThread(false);
        request8.setOmitScript(true);
        request8.setAlign(Align.RIGHT);
        String[] related8 = new String[]{"related1", "related2"};
        request8.setRelated(related8);
        String lang8 = "en";
        request8.setLang(lang8);

        OEmbedRequest request9 = new OEmbedRequest(statusId1, url1);
        request9.setMaxWidth(42);
        request9.setHideMedia(false);
        request9.setHideThread(false);
        request9.setOmitScript(true);
        request9.setAlign(Align.LEFT);
        String[] related9 = new String[]{"related1"};
        request9.setRelated(related9);
        String lang9 = "en";
        request9.setLang(lang9);

        OEmbedRequest request10 = new OEmbedRequest(statusId1, url1);
        request10.setMaxWidth(42);
        request10.setHideMedia(false);
        request10.setHideThread(false);
        request10.setOmitScript(true);
        request10.setAlign(Align.LEFT);
        String[] related10 = new String[0];
        request10.setRelated(related10);
        String lang10 = "en";
        request10.setLang(lang10);

        OEmbedRequest request11 = new OEmbedRequest(statusId1, url1);
        request11.setMaxWidth(42);
        request11.setHideMedia(false);
        request11.setHideThread(false);
        request11.setOmitScript(true);
        request11.setAlign(Align.LEFT);
        String[] related11 = new String[0];
        request11.setRelated(related11);
        String lang11 = null;
        request11.setLang(lang11);

        OEmbedRequest request12 = new OEmbedRequest(statusId1, url1);
        request12.setMaxWidth(42);
        request12.setHideMedia(false);
        request12.setHideThread(false);
        request12.setOmitScript(true);
        request12.setAlign(Align.LEFT);
        String[] related12 = new String[0];
        request12.setRelated(related12);
        String lang12 = "";
        request12.setLang(lang12);

        assertTrue(request1.equals(request1));
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
        assertFalse(request1.equals(request6));
        assertFalse(request1.equals(request7));
        assertFalse(request1.equals(request8));
        assertFalse(request1.equals(request9));
        assertFalse(request1.equals(request10));
        assertFalse(request1.equals(request11));
        assertFalse(request1.equals(request12));
    }
}