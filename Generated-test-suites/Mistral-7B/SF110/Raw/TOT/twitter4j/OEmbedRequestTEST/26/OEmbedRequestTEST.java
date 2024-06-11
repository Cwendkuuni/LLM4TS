import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;
import twitter4j.HttpParameter;
import twitter4j.Align;

public class OEmbedRequestTest {

 @Test
 public void testOEmbedRequestConstruction() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
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
 OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest.setMaxWidth(100);
 assertEquals(100, oEmbedRequest.getMaxWidth());
 oEmbedRequest.HideMedia();
 assertFalse(oEmbedRequest.isHideMedia());
 oEmbedRequest.align(Align.LEFT);
 assertEquals(Align.LEFT, oEmbedRequest.getAlign());
 oEmbedRequest.related(new String[]{"related1", "related2"});
 assertArrayEquals(new String[]{"related1", "related2"}, oEmbedRequest.getRelated());
 oEmbedRequest.lang("en");
 assertEquals("en", oEmbedRequest.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
 HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
 assertEquals(12, params.length);
 assertEquals("id", params[0].getName());
 assertEquals(Long.toString(oEmbedRequest.getStatusId()), params[0].getValue());
 // Add other assertions for the other parameters
 }

 @Test
 public void testEqualsAndHashCode() {
 OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(2L, "http://example.com");
 OEmbedRequest oEmbedRequest4 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, null);

 assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
 assertTrue(oEmbedRequest1.equals(oEmbedRequest4));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
 assertFalse(oEmbedRequest1.equals(oEmbedRequest5));

 assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
 assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest4.hashCode());
 assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest3.hashCode());
 assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest5.hashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullName() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter(null, "value", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullValue() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter("name", null, params);
 }

 @Test(expected = NumberFormatException.class)
 public void testAppendParameterWithNegativeValue() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
 List<HttpParameter> params = new ArrayList<>();
 oEmbedRequest.appendParameter("name", -1, params);
 }
}