package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

 private OEmbedRequest createOEmbedRequest() {
 return new OEmbedRequest(1L, "http://example.com");
 }

 @Test
 public void testOEmbedRequestConstructor() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 assertEquals(1L, oEmbedRequest.getStatusId());
 assertEquals("http://example.com", oEmbedRequest.getUrl());
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
 oEmbedRequest.setMaxWidth(50);
 assertEquals(50, oEmbedRequest.getMaxWidth());
 oEmbedRequest.HideMedia();
 assertFalse(oEmbedRequest.isHideMedia());
 oEmbedRequest.HideThread();
 assertFalse(oEmbedRequest.isHideThread());
 oEmbedRequest.omitScript();
 assertTrue(oEmbedRequest.isOmitScript());
 oEmbedRequest.align(Align.CENTER);
 assertEquals(Align.CENTER, oEmbedRequest.getAlign());
 String[] related = new String[]{"related1", "related2"};
 oEmbedRequest.related(related);
 assertArrayEquals(related, oEmbedRequest.getRelated());
 oEmbedRequest.lang("en");
 assertEquals("en", oEmbedRequest.getLang());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidArguments() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 oEmbedRequest.setMaxWidth(-1);
 oEmbedRequest.setHideMedia(-1);
 oEmbedRequest.setHideThread(-1);
 oEmbedRequest.setRelated(null);
 oEmbedRequest.setLang(null);
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
 assertEquals(12, params.length);
 assertEquals("id", params[0].getName());
 assertEquals(Long.toString(oEmbedRequest.getStatusId()), params[0].getValue());
 // Other assertions for other parameters
 }

 @Test
 public void testEquals() {
 OEmbedRequest oEmbedRequest1 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest2 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
 assertFalse(oEmbedRequest1.equals(null));
 assertFalse(oEmbedRequest1.equals(new Object()));
 assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
 }

 @Test
 public void testHashCode() {
 OEmbedRequest oEmbedRequest1 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest2 = createOEmbedRequest();
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
 int hashCode1 = oEmbedRequest1.hashCode();
 int hashCode2 = oEmbedRequest2.hashCode();
 int hashCode3 = oEmbedRequest3.hashCode();
 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 }

 @Test
 public void testToString() {
 OEmbedRequest oEmbedRequest = createOEmbedRequest();
 String toString = oEmbedRequest.toString();
 String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang=null}";
 assertTrue(toString.equals(expected));
 }
}