package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

	@Test
	public void testOEmbedRequestWithDefaultValues() {
		OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
		assertEquals(1L, request.getStatusId());
		assertEquals("http://example.com", request.getUrl());
		assertEquals(0, request.getMaxWidth());
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
		request.setMaxWidth(100);
		request.MaxWidth(200);
		assertEquals(200, request.getMaxWidth());

		request.setHideMedia(false);
		request.HideMedia(true);
		assertTrue(request.isHideMedia());

		request.setHideThread(false);
		request.HideThread(true);
		assertTrue(request.isHideThread());

		request.setOmitScript(true);
		request.omitScript(false);
		assertFalse(request.isOmitScript());

		request.setAlign(Align.LEFT);
		request.align(Align.RIGHT);
		assertEquals(Align.RIGHT, request.getAlign());

		String[] related = new String[] {"related1", "related2"};
		request.setRelated(related);
		request.related(new String[] {"related3"});
		assertArrayEquals(new String[] {"related3"}, request.getRelated());

		request.setLang("en");
		request.lang("fr");
		assertEquals("fr", request.getLang());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeMaxWidth() {
		OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
		request.setMaxWidth(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAlign() {
		OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
		request.setAlign(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullRelated() {
		OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
		request.setRelated(null);
	}

	@Test
	public void testToString() {
		OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
		request.setMaxWidth(100);
		request.setHideMedia(false);
		request.setHideThread(false);
		request.setOmitScript(false);
		request.setAlign(Align.LEFT);
		String[] related = new String[] {"related1", "related2"};
		request.setRelated(related);
		request.setLang("en");
		String expected = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=false, align=LEFT, related=[related1, related2], lang='en'}";
		assertEquals(expected, request.toString());
	}

	@Test
	public void testEquals() {
		OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
		OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
		assertTrue(request1.equals(request2));

		request2.setMaxWidth(200);
		assertFalse(request1.equals(request2));

		request2.setUrl("http://example2.com");
		assertFalse(request1.equals(request2));

		request2.setStatusId(2L);
		assertFalse(request1.equals(request2));

		request2.setAlign(Align.LEFT);
		assertFalse(request1.equals(request2));

		String[] related1 = new String[] {"related1"};
		request1.setRelated(related1);
		String[] related2 = new String[] {"related2"};
		request2.setRelated(related2);
		assertFalse(request1.equals(request2));

		request2.setRelated(related1);
		assertTrue(request1.equals(request2));

		request2.setLang("en");
		assertFalse(request1.equals(request2));

		request2.setLang(null);
		assertTrue(request1.equals(request2));
	}

	@Test
	public void testHashCode() {
		OEmbedRequest request1 = new OEmbedRequest(1L, "http://example.com");
		OEmbedRequest request2 = new OEmbedRequest(1L, "http://example.com");
		assertEquals(request1.hashCode(), request2.hashCode());

		request2.setMaxWidth(200);
		assertNotEquals(request1.hashCode(), request2.hashCode());

		request2.setUrl("http://example2.com");
		assertNotEquals(request1.hashCode(), request2.hashCode());

		request2.setStatusId(2L);
		assertNotEquals(request1.hashCode(), request2.hashCode());

		request2.setAlign(Align.LEFT);
		assertNotEquals(request1.hashCode(), request2.hashCode());

		String[] related1 = new String[] {"related1"};
		request1.setRelated(related1);
		String[] related2 = new String[] {"related2"};
		request2.setRelated(related2);
		assertNotEquals(request1.hashCode(), request2.hashCode());

		request2.setRelated(related1);
		assertEquals(request1.hashCode(), request2.hashCode());

		request2.setLang("en");
		assertNotEquals(request1.hashCode(), request2.hashCode());

		request2.setLang(null);
		assertEquals(request1.hashCode(), request2.hashCode());
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
		assertNull(params[2].getValue());
		assertEquals("maxwidth", params[3].getName());
		assertEquals("0", params[3].getValue());
		assertEquals("hide_media", params[4].getName());
		assertEquals("true", params[4].getValue());
		assertEquals("hide_thread", params[5].getName());
		assertEquals("true", params[5].getValue());
		assertEquals("omit_script", params[6].getName());
		assertEquals("false", params[6].getValue());
		assertEquals("align", params[7].getName());
		assertEquals(Align.NONE.name().toLowerCase(), params[7].getValue());
		assertArrayEquals(new String[0], params[8].getValue()? new String[0] : params[8].getValue().split(","));
		assertEquals("lang", params[9].getName());
		assertEquals(null, params[9].getValue());
		assertEquals("id", params[10].getName());
		assertEquals("1", params[10].getValue());
		assertEquals("_method_", params[11].getName());
		assertEquals("GET", params[11].getValue());
	}

	@Test
	public void testAppendParameter() {
		List<HttpParameter> params = new ArrayList<>();
		OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
		request.appendParameter("name1", "value1", params);
		request.appendParameter("name2", 1, params);
		assertEquals(14, params.size());
		assertEquals("name1", params.get(0).getName());
		assertEquals("value1", params.get(0).getValue());
		assertEquals("name2", params.get(1).getName());
		assertEquals("1", params.get(1).getValue());
	}
}