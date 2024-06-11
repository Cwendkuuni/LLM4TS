package twitter4j;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
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
 request.setMaxWidth(200);
 assertEquals(200, request.getMaxWidth());
 request.setHideMedia(false);
 assertFalse(request.isHideMedia());
 request.setHideThread(false);
 assertFalse(request.isHideThread());
 request.setOmitScript(true);
 assertTrue(request.isOmitScript());
 request.setAlign(Align.RIGHT);
 assertEquals(Align.RIGHT, request.getAlign());
 String[] related = new String[]{"related1", "related2"};
 request.setRelated(related);
 assertArrayEquals(related, request.getRelated());
 request.setLang("en");
 assertEquals("en", request.getLang());
 }

 @Test
 public void testEqualsAndHashCode() {
 OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest request3 = new OEmbedRequest(2L, "http://example.com");
 OEmbedRequest request4 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest request5 = new OEmbedRequest(1L, "http://different-example.com");

 assertTrue(request1.equals(request1));
 assertTrue(request1.equals(request2));
 assertFalse(request1.equals(request3));
 assertFalse(request1.equals(request4));
 assertFalse(request1.equals(request5));

 int hashCode1 = request1.hashCode();
 int hashCode2 = request2.hashCode();
 int hashCode3 = request3.hashCode();
 int hashCode4 = request4.hashCode();
 int hashCode5 = request5.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 assertNotEquals(hashCode1, hashCode5);
 }

 @Test
 public void testToString() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 assertEquals(
 "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}",
 request.toString());
 }

 @Test
 public void testSettersAndGettersWithNullValues() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 request.setLang(null);
 assertNull(request.getLang());
 request.setRelated(null);
 assertArrayEquals(new String[0], request.getRelated());
 }

 @Test
 public void testAttributeList() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 HttpParameter[] params = request.asHttpParameterArray();
 List<HttpParameter> expectedParams = Arrays.asList(
 new HttpParameter("id", "1"),
 new HttpParameter("url", "http://example.com"),
 new HttpParameter("maxwidth", "0"),
 new HttpParameter("hide_media", "true"),
 new HttpParameter("hide_thread", "true"),
 new HttpParameter("omit_script", "false"),
 new HttpParameter("align", "none"),
 new HttpParameter("related", "[\"\"]"),
 new HttpParameter("lang", null)
 );
 assertArrayEquals(expectedParams.toArray(new HttpParameter[0]), params);
 }

}