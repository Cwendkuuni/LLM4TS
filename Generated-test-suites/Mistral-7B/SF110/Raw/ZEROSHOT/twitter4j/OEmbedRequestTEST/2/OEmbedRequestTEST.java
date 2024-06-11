package twitter4j.test;

import org.junit.Before;
import org.junit.Test;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;
import twitter4j.HttpParameter;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OEmbedRequestTest {

 private OEmbedRequest oEmbedRequest;

 @Before
 public void setup() {
 oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
 }

 @Test
 public void testOEmbedRequestWithDefaultValues() {
 // Test default values
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
 // Test setters and getters
 oEmbedRequest.setMaxWidth(10);
 assertEquals(10, oEmbedRequest.getMaxWidth());
 oEmbedRequest = oEmbedRequest.MaxWidth(20);
 assertEquals(20, oEmbedRequest.getMaxWidth());

 oEmbedRequest.setHideMedia(false);
 assertFalse(oEmbedRequest.isHideMedia());
 oEmbedRequest = oEmbedRequest.HideMedia(true);
 assertTrue(oEmbedRequest.isHideMedia());

 oEmbedRequest.setHideThread(false);
 assertFalse(oEmbedRequest.isHideThread());
 oEmbedRequest = oEmbedRequest.HideThread(true);
 assertTrue(oEmbedRequest.isHideThread());

 oEmbedRequest.setOmitScript(true);
 assertTrue(oEmbedRequest.isOmitScript());
 oEmbedRequest = oEmbedRequest.omitScript(false);
 assertFalse(oEmbedRequest.isOmitScript());

 oEmbedRequest.setAlign(Align.LEFT);
 assertEquals(Align.LEFT, oEmbedRequest.getAlign());
 oEmbedRequest = oEmbedRequest.align(Align.CENTER);
 assertEquals(Align.CENTER, oEmbedRequest.getAlign());

 String[] related = new String[]{"related1", "related2"};
 oEmbedRequest.setRelated(related);
 assertArrayEquals(related, oEmbedRequest.getRelated());

 oEmbedRequest.setLang("en");
 assertEquals("en", oEmbedRequest.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 // Test asHttpParameterArray
 OEmbedRequest oEmbedRequestWithRelated = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequestWithRelated.setRelated(new String[]{"related1", "related2"});

 List<HttpParameter> params = Arrays.asList(oEmbedRequest.asHttpParameterArray());
 List<HttpParameter> expectedParams = Arrays.asList(
 new HttpParameter("id", "1"),
 new HttpParameter("url", "http://example.com"),
 new HttpParameter("maxwidth", "0"),
 new HttpParameter("hide_media", "true"),
 new HttpParameter("hide_thread", "true"),
 new HttpParameter("omit_script", "false"),
 new HttpParameter("align", "none"),
 new HttpParameter("related", "related1,related2"),
 new HttpParameter("lang", null)
 );
 assertEquals(expectedParams, params);
 }

 @Test
 public void testEquals() {
 OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");

 assertTrue(oEmbedRequest1.equals(oEmbedRequest1));
 assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
 }

 @Test
 public void testHashCode() {
 OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
 OEmbedRequest oEmbedRequestWithRelated = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequestWithRelated.setRelated(new String[]{"related1", "related2"});

 assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
 assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest3.hashCode());
 assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequestWithRelated.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", oEmbedRequest.toString());
 }
}