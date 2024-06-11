import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import twitter4j.Align;
import twitter4j.OEmbedRequest;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

 @Test
 public void testOEmbedRequestConstruction() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com/statuses/1");
 assertEquals(1L, request.getStatusId());
 assertEquals("http://example.com/statuses/1", request.getUrl());
 }

 @Test
 public void testSettersAndGetters() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com/statuses/1");
 request.setMaxWidth(100);
 request.MaxWidth(200);
 assertEquals(200, request.getMaxWidth());
 request.setHideMedia(false);
 request.HideMedia(true);
 assertEquals(true, request.isHideMedia());
 request.setHideThread(false);
 request.HideThread(true);
 assertEquals(true, request.isHideThread());
 request.setOmitScript(true);
 request.omitScript(false);
 assertEquals(false, request.isOmitScript());
 request.setAlign(Align.LEFT);
 request.align(Align.CENTER);
 assertEquals(Align.CENTER, request.getAlign());
 String[] related = new String[]{"related1", "related2"};
 request.setRelated(related);
 request.related(new String[]{"related3"});
 assertArrayEquals(new String[]{"related3"}, request.getRelated());
 request.setLang("en");
 request.lang("fr");
 assertEquals("fr", request.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com/statuses/1");
 HttpParameter[] params = request.asHttpParameterArray();
 assertEquals(12, params.length);
 assertEquals("1", params[0].getName());
 assertEquals("1", params[0].getValue());
 // ... check other parameters
 }

 @Test
 public void testEqualsAndHashCode() {
 OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com/statuses/1");
 OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com/statuses/1");
 assertEquals(request1, request2);
 OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com/statuses/1");
 assertFalse(request1.equals(request3));

 String url1 = "http://example.com/statuses/1";
 String url2 = "http://example.com/statuses/2";
 OEmbedRequest request4 = new OEmbedRequest(1L, url1);
 OEmbedRequest request5 = new OEmbedRequest(1L, url2);
 assertFalse(request4.equals(request5));
 }

 @Test
 public void testToString() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com/statuses/1");
 String toString = request.toString();
 String expected = "OEmbedRequest{statusId=1, url='http://example.com/statuses/1', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
 assertEquals(expected, toString);
 }

 @Test
 public void testAlignEnum() {
 List<Align> aligns = Arrays.asList(Align.LEFT, Align.CENTER, Align.RIGHT, Align.NONE);
 for (Align align : aligns) {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com/statuses/1");
 request.setAlign(align);
 assertEquals(align, request.getAlign());
 }
 }

 @Test
 public void testNullValues() {
 OEmbedRequest request = new OEmbedRequest(1L, null);
 assertNull(request.getUrl());
 request = new OEmbedRequest(1L, "http://example.com/statuses/1");
 request.setRelated(null);
 assertNull(request.getRelated());
 request.setLang(null);
 assertNull(request.getLang());
 }
}