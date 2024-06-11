package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long statusId = 1L;
        String url = "https://example.com/status/1";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        Assert.assertEquals(statusId, request.getStatusId());
        Assert.assertEquals(url, request.getUrl());
        Assert.assertTrue(request.isHideMedia());
        Assert.assertTrue(request.isHideThread());
        Assert.assertFalse(request.isOmitScript());
        Assert.assertEquals(Align.NONE, request.getAlign());
        Assert.assertNull(request.getRelated());
        Assert.assertNull(request.getLang());

        HttpParameter[] params = request.asHttpParameterArray();
        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("id", String.valueOf(statusId)),
                new HttpParameter("url", url),
                new HttpParameter("hide_media", "true"),
                new HttpParameter("hide_thread", "true"),
                new HttpParameter("omit_script", "false")
        );
        Assert.assertArrayEquals(expectedParams.toArray(new HttpParameter[0]), params);
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");

        request.setMaxWidth(120);
        Assert.assertEquals(120, request.getMaxWidth());
        request.MaxWidth(200);
        Assert.assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        Assert.assertFalse(request.isHideMedia());
        request.HideMedia(true);
        Assert.assertTrue(request.isHideMedia());

        request.setHideThread(false);
        Assert.assertFalse(request.isHideThread());
        request.HideThread(true);
        Assert.assertTrue(request.isHideThread());

        request.setOmitScript(true);
        Assert.assertTrue(request.isOmitScript());
        request.omitScript(false);
        Assert.assertFalse(request.isOmitScript());

        request.setAlign(Align.LEFT);
        Assert.assertEquals(Align.LEFT, request.getAlign());
        request.align(Align.CENTER);
        Assert.assertEquals(Align.CENTER, request.getAlign());

        String[] related = new String[]{"rel1", "rel2"};
        request.setRelated(related);
        Assert.assertArrayEquals(related, request.getRelated());
        request.related(new String[]{"rel3", "rel4"});
        Assert.assertArrayEquals(new String[]{"rel3", "rel4"}, request.getRelated());

        request.setLang("en");
        Assert.assertEquals("en", request.getLang());
        request.lang("fr");
        Assert.assertEquals("fr", request.getLang());

        HttpParameter[] params = request.asHttpParameterArray();
        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("id", "1"),
                new HttpParameter("url", "https://example.com/status/1"),
                new HttpParameter("maxwidth", "200"),
                new HttpParameter("hide_media", "true"),
                new HttpParameter("hide_thread", "true"),
                new HttpParameter("omit_script", "false"),
                new HttpParameter("align", "center"),
                new HttpParameter("related", "rel3,rel4"),
                new HttpParameter("lang", "fr")
        );
        Assert.assertArrayEquals(expectedParams.toArray(new HttpParameter[0]), params);
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1L, "https://example.com/status/1");
        OEmbedRequest request2 = new OEmbedRequest(1L, "https://example.com/status/1");

        OEmbedRequest request3 = new OEmbedRequest(2L, "https://example.com/status/1");
        OEmbedRequest request4 = new OEmbedRequest(1L, "https://example.com/status/2");

        OEmbedRequest request5 = new OEmbedRequest(1L, "https://example.com/status/1");
        request5.setRelated(new String[]{"rel5"});

        OEmbedRequest request6 = new OEmbedRequest(1L, "https://example.com/status/1");
        request6.setRelated(new String[0]);

        OEmbedRequest request7 = new OEmbedRequest(1L, "https://example.com/status/1");
        request7.setLang("en");

        Assert.assertTrue(request1.equals(request2));
        Assert.assertTrue(request1.hashCode() == request2.hashCode());

        Assert.assertFalse(request1.equals(request3));
        Assert.assertFalse(request1.equals(request4));
        Assert.assertFalse(request1.equals(request5));
        Assert.assertFalse(request1.equals(request6));
        Assert.assertFalse(request1.equals(null));
        Assert.assertFalse(request1.equals(""));

        Assert.assertFalse(request1.equals(new Object()));

        OEmbedRequest request8 = request1;
        Assert.assertTrue(request1.equals(request8));
        Assert.assertTrue(request1.hashCode() == request8.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        request.setMaxWidth(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        OEmbedRequest request = new OEmbedRequest(1L, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        request.setRelated(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        request.setRelated(Collections.emptyArray(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com/status/1");
        request.setLang(null);
    }
}