import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

 @Test
 public void testOEmbedRequestConstruction() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 assertEquals(1L, request.getStatusId());
 assertEquals("http://example.com", request.getUrl());
 assertFalse(request.isHideMedia());
 assertFalse(request.isHideThread());
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
 request.MaxWidth(300);
 assertEquals(300, request.getMaxWidth());
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
 String[] related = new String[]{"Related1", "Related2"};
 request.setRelated(related);
 assertArrayEquals(related, request.getRelated());
 request.related(new String[]{"Related3", "Related4"});
 assertArrayEquals(new String[]{"Related3", "Related4"}, request.getRelated());
 request.setLang("en");
 assertEquals("en", request.getLang());
 request.lang("fr");
 assertEquals("fr", request.getLang());
 }

 @Test
 public void testAppendParameter() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 List<HttpParameter> params = new ArrayList<>();
 request.appendParameter("id", "1", params);
 assertEquals("id=1", params.get(0).getName());
 assertEquals("1", params.get(0).getValue());
 request.appendParameter("maxwidth", "200", params);
 assertEquals("maxwidth=200", params.get(1).getName());
 assertEquals("200", params.get(1).getValue());
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
 HttpParameter[] httpParams = request.asHttpParameterArray();
 assertEquals(13, httpParams.length);
 assertEquals("id=1", httpParams[0].getName());
 assertEquals("1", httpParams[0].getValue());
 assertEquals("url=http://example.com", httpParams[1].getName());
 // other assertions for other parameters
 }

}