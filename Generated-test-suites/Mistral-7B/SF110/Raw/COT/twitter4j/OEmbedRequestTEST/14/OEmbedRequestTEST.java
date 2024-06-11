package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.Align;
import twitter4j.OEmbedRequest;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        long statusId = 1L;
        String url = "https://twitter.com/statuses/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        Assert.assertEquals(statusId, oEmbedRequest.getStatusId());
        Assert.assertEquals(url, oEmbedRequest.getUrl());
        Assert.assertFalse(oEmbedRequest.isHideMedia());
        Assert.assertFalse(oEmbedRequest.isHideThread());
        Assert.assertFalse(oEmbedRequest.isOmitScript());
        Assert.assertNull(oEmbedRequest.getAlign());
        Assert.assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        Assert.assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        oEmbedRequest.setMaxWidth(10);
        Assert.assertEquals(10, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testMaxWidthMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        OEmbedRequest maxWidthRequest = oEmbedRequest.MaxWidth(10);
        Assert.assertEquals(10, maxWidthRequest.getMaxWidth());
        Assert.assertEquals(oEmbedRequest, maxWidthRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        oEmbedRequest.setMaxWidth(-1);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        oEmbedRequest.setHideMedia(true);
        Assert.assertTrue(oEmbedRequest.isHideMedia());
    }

    @Test
    public void testHideMediaMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        OEmbedRequest hideMediaRequest = oEmbedRequest.HideMedia(true);
        Assert.assertTrue(hideMediaRequest.isHideMedia());
        Assert.assertEquals(oEmbedRequest, hideMediaRequest);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        oEmbedRequest.setHideThread(true);
        Assert.assertTrue(oEmbedRequest.isHideThread());
    }

    @Test
    public void testHideThreadMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        OEmbedRequest hideThreadRequest = oEmbedRequest.HideThread(true);
        Assert.assertTrue(hideThreadRequest.isHideThread());
        Assert.assertEquals(oEmbedRequest, hideThreadRequest);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        oEmbedRequest.setOmitScript(true);
        Assert.assertTrue(oEmbedRequest.isOmitScript());
    }

    @Test
    public void testOmitScriptMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        OEmbedRequest omitScriptRequest = oEmbedRequest.omitScript(true);
        Assert.assertTrue(omitScriptRequest.isOmitScript());
        Assert.assertEquals(oEmbedRequest, omitScriptRequest);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        Align align = Align.LEFT;
        oEmbedRequest.setAlign(align);
        Assert.assertEquals(align, oEmbedRequest.getAlign());
    }

    @Test
    public void testAlignMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        OEmbedRequest alignRequest = oEmbedRequest.align(Align.LEFT);
        Assert.assertEquals(Align.LEFT, alignRequest.getAlign());
        Assert.assertEquals(oEmbedRequest, alignRequest);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        String[] related = new String[]{"test1", "test2"};
        oEmbedRequest.setRelated(related);
        Assert.assertArrayEquals(related, oEmbedRequest.getRelated());
    }

    @Test
    public void testRelatedMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        String[] related = new String[]{"test1", "test2"};
        OEmbedRequest relatedRequest = oEmbedRequest.related(related);
        Assert.assertArrayEquals(related, relatedRequest.getRelated());
        Assert.assertEquals(oEmbedRequest, relatedRequest);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        String lang = "en";
        oEmbedRequest.setLang(lang);
        Assert.assertEquals(lang, oEmbedRequest.getLang());
    }

    @Test
    public void testLangMethod() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        String lang = "en";
        OEmbedRequest langRequest = oEmbedRequest.lang(lang);
        Assert.assertEquals(lang, langRequest.getLang());
        Assert.assertEquals(oEmbedRequest, langRequest);
    }

    @Test
    public void testAsHttpParameterArray() {
        long statusId = 1L;
        String url = "https://twitter.com/statuses/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        HttpParameter[] expectedParams = {
                new HttpParameter("id", String.valueOf(statusId)),
                new HttpParameter("url", url),
                new HttpParameter("maxwidth", String.valueOf(oEmbedRequest.getMaxWidth()))
        };
        HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParams, params);
    }

    @Test
    public void testAppendParameterWithNullName() {
        List<HttpParameter> params = new ArrayList<>();
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        oEmbedRequest.appendParameter(null, null, params);
        Assert.assertTrue(params.isEmpty());
    }

    @Test
    public void testAppendParameterWithNullValue() {
        List<HttpParameter> params = new ArrayList<>();
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "https://twitter.com/statuses/1");
        oEmbedRequest.appendParameter("id", null, params);
        Assert.assertTrue(params.isEmpty());
    }

    @Test
    public void testEquals() {
        long statusId1 = 1L;
        long statusId2 = 2L;
        String url1 = "https://twitter.com/statuses/1";
        String url2 = "https://twitter.com/statuses/2";
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId1, url1);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId2, url2);
        OEmbedRequest equalRequest = new OEmbedRequest(statusId1, url1);
        OEmbedRequest unequalRequest = new OEmbedRequest(statusId1, url2);
        Assert.assertTrue(oEmbedRequest1.equals(equalRequest));
        Assert.assertFalse(oEmbedRequest1.equals(unequalRequest));
        Assert.assertFalse(oEmbedRequest1.equals(null));
        Assert.assertFalse(oEmbedRequest1.equals("test"));
    }

    @Test
    public void testHashCode() {
        long statusId = 1L;
        String url = "https://twitter.com/statuses/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        long hashCode = oEmbedRequest.hashCode();
        OEmbedRequest equalRequest = new OEmbedRequest(statusId, url);
        Assert.assertEquals(hashCode, equalRequest.hashCode());
    }

    @Test
    public void testToString() {
        long statusId = 1L;
        String url = "https://twitter.com/statuses/1";
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        String expectedString = "OEmbedRequest{statusId=1, url='https://twitter.com/statuses/1', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang=''}";
        String string = oEmbedRequest.toString();
        Assert.assertEquals(expectedString, string);
    }
}