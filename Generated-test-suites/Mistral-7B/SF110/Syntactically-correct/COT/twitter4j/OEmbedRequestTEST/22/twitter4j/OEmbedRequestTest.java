package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

 private OEmbedRequest createOEmbedRequest() {
 return new OEmbedRequest(1L, "http://example.com");
 }

 @Test
 public void testOEmbedRequestConstructor() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 assertEquals(1L, oEmbedRequest.getStatusId());
 assertEquals("http://example.com", oEmbedRequest.getUrl());
 assertEquals(0, oEmbedRequest.getMaxWidth());
 assertTrue(oEmbedRequest.isHideMedia());
 assertTrue(oEmbedRequest.isHideThread());
 assertFalse(oEmbedRequest.isOmitScript());
 assertNull(oEmbedRequest.getAlign());
 assertArrayEquals(new String[0], oEmbedRequest.getRelated());
 assertNull(oEmbedRequest.getLang());
 }

 @Test
 public void testSettersAndGetters() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();

 oEmbedRequest.setMaxWidth(100);
 assertEquals(100, oEmbedRequest.getMaxWidth());

 oEmbedRequest.MaxWidth(200);
 assertEquals(200, oEmbedRequest.getMaxWidth());

 oEmbedRequest.setHideMedia(false);
 assertFalse(oEmbedRequest.isHideMedia());

 oEmbedRequest.HideMedia(true);
 assertTrue(oEmbedRequest.isHideMedia());

 oEmbedRequest.setHideThread(false);
 assertFalse(oEmbedRequest.isHideThread());

 oEmbedRequest.HideThread(true);
 assertTrue(oEmbedRequest.isHideThread());

 oEmbedRequest.setOmitScript(true);
 assertTrue(oEmbedRequest.isOmitScript());

 oEmbedRequest.omitScript(false);
 assertFalse(oEmbedRequest.isOmitScript());

 oEmbedRequest.setAlign(Align.LEFT);
 assertEquals(Align.LEFT, oEmbedRequest.getAlign());

 oEmbedRequest.align(Align.CENTER);
 assertEquals(Align.CENTER, oEmbedRequest.getAlign());

 oEmbedRequest.setAlign(Align.RIGHT);
 assertEquals(Align.RIGHT, oEmbedRequest.getAlign());

 oEmbedRequest.align(Align.NONE);
 assertEquals(Align.NONE, oEmbedRequest.getAlign());

 String[] related = {"test1", "test2"};
 oEmbedRequest.setRelated(related);
 assertArrayEquals(related, oEmbedRequest.getRelated());

 oEmbedRequest.related(new String[]{"test3", "test4"});
 assertArrayEquals(new String[]{"test3", "test4"}, oEmbedRequest.getRelated());

 oEmbedRequest.setLang("en");
 assertEquals("en", oEmbedRequest.getLang());

 oEmbedRequest.lang("fr");
 assertEquals("fr", oEmbedRequest.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();

 HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
 assertEquals(12, httpParameters.length);

 assertEquals("id", httpParameters[0].getName());
 assertEquals(String.valueOf(1L), httpParameters[0].getValue());

 assertEquals("url", httpParameters[1].getName());
 assertEquals("http://example.com", httpParameters[1].getValue());

 assertEquals("maxwidth", httpParameters[2].getName());
 assertEquals(String.valueOf(0), httpParameters[2].getValue());

 assertEquals("hide_media", httpParameters[3].getName());
 assertEquals("true", httpParameters[3].getValue());

 assertEquals("hide_thread", httpParameters[4].getName());
 assertEquals("true", httpParameters[4].getValue());

 assertEquals("omit_script", httpParameters[5].getName());
 assertEquals("false", httpParameters[5].getValue());

 assertEquals("align", httpParameters[6].getName());
 assertEquals(Align.NONE.name().toLowerCase(), httpParameters[6].getValue());

 if (oEmbedRequest.getRelated().length > 0) {
 assertEquals("related", httpParameters[7].getName());
 assertEquals(StringUtil.join(oEmbedRequest.getRelated()), httpParameters[7].getValue());
 } else {
 assertTrue(httpParameters[7].getValue() == null);
 }

 assertEquals("lang", httpParameters[8].getName());
 assertEquals("null", httpParameters[8].getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullName() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter(null, "value", params);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullValue() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter("name", null, params);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNegativeValue() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter("name", -1L, params);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testEquals() {
 OEmbedRequest oEmbedRequest1 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest2 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");

 OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest5.setMaxWidth(100);

 OEmbedRequest oEmbedRequest6 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest6.setMaxWidth(200);

 OEmbedRequest oEmbedRequest7 = new OEmbedRequest(2L, "http://example.com");

 OEmbedRequest oEmbedRequest8 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest8.setAlign(Align.LEFT);

 OEmbedRequest oEmbedRequest9 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest9.setRelated(new String[]{"test1", "test2"});

 OEmbedRequest oEmbedRequest10 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest10.setRelated(new String[0]);

 OEmbedRequest oEmbedRequest11 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest11.setLang("en");

 assertTrue(oEmbedRequest1.equals(oEmbedRequest1));
 assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest4));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest5));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest6));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest7));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest8));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest9));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest10));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest11));
 }

 @Test
 public void testHashCode() {
 OEmbedRequest oEmbedRequest1 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest2 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");

 OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest5.setMaxWidth(100);

 OEmbedRequest oEmbedRequest6 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest6.setMaxWidth(200);

 OEmbedRequest oEmbedRequest7 = new OEmbedRequest(2L, "http://example.com");

 OEmbedRequest oEmbedRequest8 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest8.setAlign(Align.LEFT);

 OEmbedRequest oEmbedRequest9 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest9.setRelated(new String[]{"test1", "test2"});

 OEmbedRequest oEmbedRequest10 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest10.setRelated(new String[0]);

 OEmbedRequest oEmbedRequest11 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest11.setLang("en");

 int hashCode1 = oEmbedRequest1.hashCode();
 int hashCode2 = oEmbedRequest2.hashCode();
 int hashCode3 = oEmbedRequest3.hashCode();
 int hashCode4 = oEmbedRequest4.hashCode();
 int hashCode5 = oEmbedRequest5.hashCode();
 int hashCode6 = oEmbedRequest6.hashCode();
 int hashCode7 = oEmbedRequest7.hashCode();
 int hashCode8 = oEmbedRequest8.hashCode();
 int hashCode9 = oEmbedRequest9.hashCode();
 int hashCode10 = oEmbedRequest10.hashCode();
 int hashCode11 = oEmbedRequest11.hashCode();

 assertTrue(oEmbedRequest1.hashCode() == oEmbedRequest1.hashCode());
 assertTrue(oEmbedRequest1.hashCode() == oEmbedRequest2.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest3.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest4.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest5.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest6.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest7.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest8.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest9.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest10.hashCode());
 assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest11.hashCode());

 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 assertFalse(hashCode1 == hashCode4);
 assertFalse(hashCode1 == hashCode5);
 assertFalse(hashCode1 == hashCode6);
 assertFalse(hashCode1 == hashCode7);
 assertFalse(hashCode1 == hashCode8);
 assertFalse(hashCode1 == hashCode9);
 assertFalse(hashCode1 == hashCode10);
 assertFalse(hashCode1 == hashCode11);
 }

 @Test
 public void testToString() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}", oEmbedRequest.toString());
 }
}