package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import java.util.Arrays;
import org.junit.Test;

public class OEmbedRequestTest {

 @Test
 public void testNewInstance() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 assertEquals(1L, request.getStatusId());
 assertEquals("http://example.com", request.getUrl());
 assertTrue(request.isHideMedia());
 assertTrue(request.isHideThread());
 assertFalse(request.isOmitScript());
 assertNull(request.getAlign());
 assertArrayEquals(new String[0], request.getRelated());
 assertNull(request.getLang());
 }

 @Test
 public void testSettersAndGetters() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 request.setMaxWidth(120);
 assertEquals(120, request.getMaxWidth());
 request.MaxWidth(200);
 assertEquals(200, request.getMaxWidth());
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
 request.setRelated(new String[]{"related1", "related2"});
 assertArrayEquals(new String[]{"related1", "related2"}, request.getRelated());
 request.related(new String[]{"related3", "related4"});
 assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());
 request.setLang("en");
 assertEquals("en", request.getLang());
 request.lang("es");
 assertEquals("es", request.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 HttpParameter[] params = request.asHttpParameterArray();
 assertEquals(12, params.length);
 assertEquals("id", params[0].getName());
 assertEquals("1", params[0].getValue());
 assertEquals("url", params[1].getName());
 assertEquals("http://example.com", params[1].getValue());
 assertNull(params[2]);
 assertNull(params[3]);
 assertNull(params[4]);
 assertNull(params[5]);
 assertNull(params[6]);
 assertNull(params[7]);
 assertNull(params[8]);
 assertNull(params[9]);
 assertNull(params[10]);
 HttpParameter relatedParam = params[11];
 assertEquals("related", relatedParam.getName());
 assertEquals("", relatedParam.getValue());
 }

 @Test
 public void testEquals() {
 OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
 assertTrue(request1.equals(request2));

 request2.setMaxWidth(200);
 assertFalse(request1.equals(request2));

 request2.setMaxWidth(120);
 assertTrue(request1.equals(request2));

 request2.setHideMedia(false);
 assertFalse(request1.equals(request2));

 request2.setLang("en");
 request1.setLang("es");
 assertFalse(request1.equals(request2));

 request1.setLang("en");
 assertTrue(request1.equals(request2));
 }

 @Test
 public void testHashCode() {
 OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
 int hashCode1 = request1.hashCode();
 int hashCode2 = request2.hashCode();
 assertEquals(hashCode1, hashCode2);

 request2.setMaxWidth(200);
 int hashCode3 = request2.hashCode();
 assertNotEquals(hashCode1, hashCode3);

 request2.setMaxWidth(120);
 assertEquals(hashCode1, hashCode2);

 request2.setHideMedia(false);
 int hashCode4 = request2.hashCode();
 assertNotEquals(hashCode1, hashCode4);

 request2.setLang("es");
 int hashCode5 = request2.hashCode();
 assertNotEquals(hashCode1, hashCode5);
 }
}