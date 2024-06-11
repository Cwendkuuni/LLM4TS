package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

 private final long statusId = 1234567890L;
 private final String url = "https://www.example.com/status/123";

 @Test
 public void testOEmbedRequestConstruction() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
 assertEquals(statusId, oEmbedRequest.getStatusId());
 assertEquals(url, oEmbedRequest.getUrl());
 }

 @Test
 public void testSettersAndGetters() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);

 oEmbedRequest.setMaxWidth(120);
 assertEquals(120, oEmbedRequest.getMaxWidth());

 oEmbedRequest.HideMedia(false);
 assertFalse(oEmbedRequest.isHideMedia());

 oEmbedRequest.HideThread(false);
 assertFalse(oEmbedRequest.isHideThread());

 oEmbedRequest.setOmitScript(true);
 assertTrue(oEmbedRequest.isOmitScript());

 oEmbedRequest.setAlign(Align.LEFT);
 assertEquals(Align.LEFT, oEmbedRequest.getAlign());

 String[] related = new String[]{"related1", "related2"};
 oEmbedRequest.setRelated(related);
 assertArrayEquals(related, oEmbedRequest.getRelated());

 oEmbedRequest.setLang("en");
 assertEquals("en", oEmbedRequest.getLang());
 }

 @Test
 public void testAppendParameter() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter("name1", "value1", params);
 oEmbedRequest.appendParameter("name2", 123456L, params);

 HttpParameter[] expectedParams = new HttpParameter[]{
 new HttpParameter("name1", "value1"),
 new HttpParameter("name2", "123456")
 };
 assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendNullParameterName() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter(null, "value", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendNullParameterValue() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter("name", null, params);
 }

 @Test
 public void testEquals() {
 OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
 OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
 assertTrue(oEmbedRequest1.equals(oEmbedRequest2));

 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId + 1, url);
 assertFalse(oEmbedRequest1.equals(oEmbedRequest3));

 OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId, "https://www.example.com/status/234");
 assertFalse(oEmbedRequest1.equals(oEmbedRequest4));
 }

 @Test
 public void testHashCode() {
 OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
 OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
 assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());

 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(statusId + 1, url);
 int differentHashCode = oEmbedRequest1.hashCode() ^ oEmbedRequest3.hashCode();
 assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest3.hashCode());

 OEmbedRequest oEmbedRequest4 = new OEmbedRequest(statusId, "https://www.example.com/status/234");
 int differentHashCode2 = oEmbedRequest1.hashCode() ^ oEmbedRequest4.hashCode();
 assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest4.hashCode());
 }

 @Test
 public void testToString() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
 String expectedToString = "OEmbedRequest{statusId=1234567890, url='https://www.example.com/status/123', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
 assertEquals(expectedToString, oEmbedRequest.toString());
 }

}