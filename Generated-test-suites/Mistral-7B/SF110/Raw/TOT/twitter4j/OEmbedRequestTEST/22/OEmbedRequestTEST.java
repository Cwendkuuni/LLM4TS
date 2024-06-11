
package twitter4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructionAndMaxWidth() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(100);

        HttpParameter[] params = request.asHttpParameterArray();
        Assert.assertEquals(12, params.length);
        Assert.assertEquals("1", params[0].getName());
        Assert.assertEquals("1", params[0].getValue());
        Assert.assertEquals("url", params[1].getName());
        Assert.assertEquals("https://example.com", params[1].getValue());
        Assert.assertEquals("maxwidth", params[2].getName());
        Assert.assertEquals("100", params[2].getValue());
        // Add more assertions for other fields if needed
    }

    @Test
    public void testOEmbedRequestHideMediaAndEquals() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId, url);
        OEmbedRequest request2 = new OEmbedRequest(statusId, url);

        request1.setHideMedia(false);
        request2.setHideMedia(true);

        Assert.assertNotEquals(request1, request2);

        request1 = new OEmbedRequest(statusId, url);
        request1.setHideMedia(true);

        Assert.assertEquals(request1, request2);

        // Test other fields and check equals method for those as well
    }

    @Test
    public void testOEmbedRequestAlignAndAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setAlign(Align.LEFT);
        Assert.assertEquals(Align.LEFT, request.getAlign());

        HttpParameter[] params = request.asHttpParameterArray();
        Assert.assertEquals(12, params.length);
        Assert.assertEquals("align", params[8].getName());
        Assert.assertEquals("left", params[8].getValue());

        request.setAlign(Align.CENTER);
        params = request.asHttpParameterArray();
        Assert.assertEquals("align", params[8].getName());
        Assert.assertEquals("center", params[8].getValue());

        // Test other fields and check asHttpParameterArray method for those as well
    }

    @Test
    public void testOEmbedRequestSetters() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        Assert.assertEquals(100, request.getMaxWidth());

        request.setHideMedia(false);
        Assert.assertFalse(request.isHideMedia());

        request.setHideThread(true);
        Assert.assertTrue(request.isHideThread());

        request.setOmitScript(true);
        Assert.assertTrue(request.isOmitScript());

        request.setAlign(Align.RIGHT);
        Assert.assertEquals(Align.RIGHT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        Assert.assertArrayEquals(related, request.getRelated());

        request.setLang("en");
        Assert.assertEquals("en", request.getLang());
    }

    @Test
    public void testOEmbedRequestAppendParameterMethods() {
        long statusId = 1L;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.appendParameter("id", "1");
        HttpParameter[] params = request.asHttpParameterArray();
        Assert.assertEquals("1", params[0].getValue());

        request.appendParameter("maxwidth", "200");
        params = request.asHttpParameterArray();
        Assert.assertEquals("200", params[2].getValue());

        request.appendParameter("hide_media", "true");
        params = request.asHttpParameterArray();
        Assert.assertEquals("true", params[9].getValue());

        request.appendParameter("align", "right");
        params = request.asHttpParameterArray();
        Assert.assertEquals("right", params[8].getValue());

        request.appendParameter("related", "related1,related2");
        params = request.asHttpParameterArray();
        Assert.assertEquals("related1,related2", params[10].getValue());

        request.appendParameter("lang", "fr");
        params = request.asHttpParameterArray();
        Assert.assertEquals("fr", params[11].getValue());
    }

    @Test
    public void testOEmbedRequestEqualsAndHashCode() {
        long statusId1 = 1L;
        String url1 = "https://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);

        request1.setMaxWidth(100);
        request1.setHideMedia(false);
        request1.setHideThread(true);
        request1.setOmitScript(true);
        request1.setAlign(Align.LEFT);
        String[] related1 = new String[]{"related1", "related2"};
        request1.setRelated(related1);
        request1.setLang("en");

        long statusId2 = 1L;
        String url2 = "https://example.com";
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url2);
        request2.setMaxWidth(100);
        request2.setHideMedia(false);
        request2.setHideThread(true);
        request2.setOmitScript(true);
        request2.setAlign(Align.LEFT);
        String[] related2 = new String[]{"related1", "related2"};
        request2.setRelated(related2);
        request2.setLang("en");

        Assert.assertEquals(request1, request2);
        Assert.assertEquals(request1.hashCode(), request2.hashCode());

        request1.setLang("fr");
        Assert.assertNotEquals(request1, request2);
        Assert.assertNotEquals(request1.hashCode(), request2.hashCode());

        request1.setLang("en");
        request2.setLang("es");
        Assert.assertNotEquals(request1, request2);
        Assert.assertNotEquals(request1.hashCode(), request2.hashCode());

        request1.setRelated(new String[]{"related1"});
        Assert.assertNotEquals(request1, request2);
        Assert.assertNotEquals(request1.hashCode(), request2.hashCode());
    }
}