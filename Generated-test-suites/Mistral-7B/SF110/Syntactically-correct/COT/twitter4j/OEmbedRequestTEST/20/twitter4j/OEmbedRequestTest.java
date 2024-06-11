package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

 private final long statusId = 1234567890L;
 private final String url = "https://example.com/status/1";

 @Test
 public void testOEmbedRequestConstruction() {
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
 OEmbedRequest request = new OEmbedRequest(statusId, url);
 request.setMaxWidth(10);
 assertEquals(10, request.getMaxWidth());
 request.MaxWidth(20);
 assertEquals(20, request.getMaxWidth());
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
 request.setAlign(Align.LEFT);
 assertEquals(Align.LEFT, request.getAlign());
 request.align(Align.CENTER);
 assertEquals(Align.CENTER, request.getAlign());
 String[] related = new String[]{"related1", "related2"};
 request.setRelated(related);
 assertArrayEquals(related, request.getRelated());
 request.related(new String[]{"related3", "related4"});
 assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());
 request.setLang("en");
 assertEquals("en", request.getLang());
 request.lang("fr");
 assertEquals("fr", request.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest request = new OEmbedRequest(statusId, url);
 HttpParameter[] params = request.asHttpParameterArray();
 assertEquals(12, params.length);
 assertEquals("id", params[0].getName());
 assertEquals("1234567890", params[0].getValue());
 // Other assertions for other parameters
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxWidth() {
 OEmbedRequest request = new OEmbedRequest(statusId, url);
 request.setMaxWidth(-1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullUrl() {
 OEmbedRequest request = new OEmbedRequest(statusId, null);
 }

}