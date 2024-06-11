import org.junit.Before;
import org.junit.Test;
import twitter4j.Align;
import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

 private OEmbedRequest oEmbedRequest;

 @Before
 public void setUp() {
 oEmbedRequest = new OEmbedRequest(1L, "http://example.com");
 }

 @Test
 public void testConstructor() {
 assertEquals(1L, oEmbedRequest.getStatusId());
 assertEquals("http://example.com", oEmbedRequest.getUrl());
 assertNull(oEmbedRequest.getMaxWidth());
 assertTrue(oEmbedRequest.isHideMedia());
 assertTrue(oEmbedRequest.isHideThread());
 assertFalse(oEmbedRequest.isOmitScript());
 assertNull(oEmbedRequest.getAlign());
 assertArrayEquals(new String[0], oEmbedRequest.getRelated());
 assertNull(oEmbedRequest.getLang());
 }

 @Test
 public void testSettersAndGetters() {
 oEmbedRequest.setMaxWidth(100);
 assertEquals(100, oEmbedRequest.getMaxWidth());

 oEmbedRequest.HideMedia(false);
 assertFalse(oEmbedRequest.isHideMedia());

 oEmbedRequest.HideThread(false);
 assertFalse(oEmbedRequest.isHideThread());

 oEmbedRequest.omitScript(true);
 assertTrue(oEmbedRequest.isOmitScript());

 oEmbedRequest.align(Align.LEFT);
 assertEquals(Align.LEFT, oEmbedRequest.getAlign());

 String[] related = new String[]{"related1", "related2"};
 oEmbedRequest.related(related);
 assertArrayEquals(related, oEmbedRequest.getRelated());

 oEmbedRequest.lang("en");
 assertEquals("en", oEmbedRequest.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
 assertEquals(12, params.length);

 assertEquals("id", params[0].getName());
 assertEquals("1", params[0].getValue());

 assertEquals("url", params[1].getName());
 assertEquals("http://example.com", params[1].getValue());

 assertEquals("maxwidth", params[2].getName());
 assertEquals("100", params[2].getValue());

 assertTrue(params[3].getName().equals("hide_media"));
 assertEquals(Boolean.toString(oEmbedRequest.isHideMedia()), params[3].getValue());

 assertTrue(params[4].getName().equals("hide_thread"));
 assertEquals(Boolean.toString(oEmbedRequest.isHideThread()), params[4].getValue());

 assertTrue(params[5].getName().equals("omit_script"));
 assertEquals(Boolean.toString(oEmbedRequest.isOmitScript()), params[5].getValue());

 assertEquals("align", params[6].getName());
 assertEquals(Align.LEFT.name().toLowerCase(), params[6].getValue());

 if (oEmbedRequest.getRelated().length > 0) {
 assertEquals("related", params[7].getName());
 assertEquals(StringUtil.join(oEmbedRequest.getRelated()), params[7].getValue());
 } else {
 assertNull(params[7]);
 }

 assertEquals("lang", params[8].getName());
 assertEquals("en", params[8].getValue());
 }

 @Test
 public void testEquals() {
 OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest4 = new OEmbedRequest(2L, "http://example.com");
 OEmbedRequest oEmbedRequest5 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest6 = new OEmbedRequest(1L, "http://example.com");
 OEmbedRequest oEmbedRequest7 = new OEmbedRequest(1L, "http://example.com");
 oEmbedRequest7.setMaxWidth(100);

 assertTrue(oEmbedRequest.equals(oEmbedRequest5));
 assertTrue(oEmbedRequest.equals(oEmbedRequest6));
 assertTrue(oEmbedRequest.equals(oEmbedRequest7));

 assertFalse(oEmbedRequest.equals(oEmbedRequest2));
 assertFalse(oEmbedRequest.equals(oEmbedRequest3));
 assertFalse(oEmbedRequest.equals(oEmbedRequest4));
 }

 @Test
 public void testHashCode() {
 Set<OEmbedRequest> set = new HashSet<>();
 set.add(oEmbedRequest);
 set.add(new OEmbedRequest(1L, "http://example.com"));
 set.add(new OEmbedRequest(1L, "http://example.com"));
 set.add(new OEmbedRequest(1L, "http://example.com"));
 set.add(new OEmbedRequest(1L, "http://example.com") {
 @Override
 public int hashCode() {
 return 1;
 }
 });
 assertEquals(4, set.size());
 }

 @Test
 public void testToString() {
 assertEquals("OEmbedRequest{statusId=1, url='http://example.com/', maxWidth=100, hideMedia=false, hideThread=false, omitScript=false, align=LEFT, related=[], lang='en''}", oEmbedRequest.toString());
 }
}