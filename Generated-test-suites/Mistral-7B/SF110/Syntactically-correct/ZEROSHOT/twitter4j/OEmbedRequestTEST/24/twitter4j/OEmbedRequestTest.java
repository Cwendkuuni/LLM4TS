package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import java.util.Arrays;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

 @Test
 public void testOEmbedRequestInstance() {
  long statusId = 123;
  String url = "https://example.com/status/123";
  OEmbedRequest request = new OEmbedRequest(statusId, url);
  assertEquals(statusId, request.getStatusId());
  assertEquals(url, request.getUrl());
  assertNull(request.getMaxWidth());
  assertTrue(request.isHideMedia());
  assertTrue(request.isHideThread());
  assertFalse(request.isOmitScript());
  assertNull(request.getAlign());
  assertArrayEquals(new String[0], request.getRelated());
  assertNull(request.getLang());
 }

 @Test
 public void testSetters() {
  long statusId = 123;
  String url = "https://example.com/status/123";
  OEmbedRequest request = new OEmbedRequest(statusId, url);

  request.setMaxWidth(42);
  assertEquals(42, request.getMaxWidth());
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

  String[] related = new String[]{"foo", "bar"};
  request.setRelated(related);
  assertArrayEquals(related, request.getRelated());

  request.setLang("en");
  assertEquals("en", request.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
  long statusId = 123;
  String url = "https://example.com/status/123";
  OEmbedRequest request = new OEmbedRequest(statusId, url);

  request.setMaxWidth(42);
  request.setHideMedia(false);
  request.setHideThread(false);
  request.setOmitScript(true);
  request.setAlign(Align.LEFT);
  String[] related = new String[]{"foo", "bar"};
  request.setRelated(related);
  request.setLang("en");

  HttpParameter[] params = request.asHttpParameterArray();
  assertEquals(12, params.length);
  assertEquals("id", params[0].getName());
  assertEquals(Long.toString(statusId), params[0].getValue());
  assertEquals("url", params[1].getName());
  assertEquals(url, params[1].getValue());
  assertEquals("maxwidth", params[2].getName());
  assertEquals(Integer.toString(request.getMaxWidth()), params[2].getValue());
  assertEquals("hide_media", params[3].getName());
  assertEquals(Boolean.toString(request.isHideMedia()), params[3].getValue());
  assertEquals("hide_thread", params[4].getName());
  assertEquals(Boolean.toString(request.isHideThread()), params[4].getValue());
  assertEquals("omit_script", params[5].getName());
  assertEquals(Boolean.toString(request.isOmitScript()), params[5].getValue());
  assertEquals("align", params[6].getName());
  assertEquals(Align.LEFT.name().toLowerCase(), params[6].getValue());
  if (request.getRelated().length > 0) {
       assertEquals("related", params[7].getName());
       assertEquals(StringUtil.join(request.getRelated()), params[7].getValue());
  }
  else {
       assertNull(params[7]);
  }
  assertEquals("lang", params[8].getName());
  assertEquals(request.getLang(), params[8].getValue());
 }

}