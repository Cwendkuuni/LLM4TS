package twitter4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import twitter4j.HttpParameter;
import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

	@Test
	public void testOEmbedRequestConstruction() {
		final long statusId = 1L;
		final String url = "http://example.com";
		final OEmbedRequest request = new OEmbedRequest(statusId, url);
		assertNotNull(request);
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
	public void testSettersAndGetters() {
		final long statusId = 1L;
		final String url = "http://example.com";
		final int maxWidth = 120;
		final boolean hideMedia = false;
		final boolean hideThread = false;
		final boolean omitScript = true;
		final Align align = Align.RIGHT;
		final String[] related = new String[] {"related1", "related2"};
		final String lang = "en";

		final OEmbedRequest request = new OEmbedRequest(statusId, url);
		request.setMaxWidth(maxWidth);
		assertEquals(maxWidth, request.getMaxWidth());
		request.MaxWidth(maxWidth);
		assertEquals(maxWidth, request.getMaxWidth());

		request.setHideMedia(hideMedia);
		assertEquals(hideMedia, request.isHideMedia());
		request.HideMedia(hideMedia);
		assertEquals(hideMedia, request.isHideMedia());

		request.setHideThread(hideThread);
		assertEquals(hideThread, request.isHideThread());
		request.HideThread(hideThread);
		assertEquals(hideThread, request.isHideThread());

		request.setOmitScript(omitScript);
		assertEquals(omitScript, request.isOmitScript());
		request.omitScript(omitScript);
		assertEquals(omitScript, request.isOmitScript());

		request.setAlign(align);
		assertEquals(align, request.getAlign());
		request.align(align);
		assertEquals(align, request.getAlign());

		request.setRelated(related);
		assertArrayEquals(related, request.getRelated());
		request.related(related);
		assertArrayEquals(related, request.getRelated());

		request.setLang(lang);
		assertEquals(lang, request.getLang());
		request.lang(lang);
		assertEquals(lang, request.getLang());
	}

	@Test
	public void testEqualsAndHashcode() {
		final long statusId = 1L;
		final String url = "http://example.com";
		final int maxWidth = 120;
		final boolean hideMedia = false;
		final boolean hideThread = false;
		final boolean omitScript = true;
		final Align align = Align.RIGHT;
		final String[] related = new String[] {"related1", "related2"};
		final String lang = "en";

		final OEmbedRequest request1 = new OEmbedRequest(statusId, url);
		final OEmbedRequest request2 = new OEmbedRequest(statusId, url);
		final OEmbedRequest request3 = new OEmbedRequest(statusId + 1, url);
		final OEmbedRequest request4 = new OEmbedRequest(statusId, url + "x");
		final OEmbedRequest request5 = new OEmbedRequest(statusId, url);
		request5.setMaxWidth(maxWidth);
		final OEmbedRequest request6 = new OEmbedRequest(statusId, url);
		request6.setMaxWidth(maxWidth + 1);
		final OEmbedRequest request7 = new OEmbedRequest(statusId, url);
		request7.setHideMedia(hideMedia);
		final OEmbedRequest request8 = new OEmbedRequest(statusId, url);
		request8.setHideMedia(hideMedia);
		request8.setHideThread(hideThread);
		final OEmbedRequest request9 = new OEmbedRequest(statusId, url);
		request9.setHideMedia(hideMedia);
		request9.setHideThread(hideThread);
		request9.setOmitScript(omitScript);
		final OEmbedRequest request10 = new OEmbedRequest(statusId, url);
		request10.setAlign(align);
		final OEmbedRequest request11 = new OEmbedRequest(statusId, url);
		request11.setAlign(align);
		request11.setRelated(related);
		final OEmbedRequest request12 = new OEmbedRequest(statusId, url);
		request12.setRelated(related);
		request12.setLang(lang);
		final OEmbedRequest request13 = new OEmbedRequest(statusId, url);
		request13.setRelated(related);
		request13.setLang(lang);
		final OEmbedRequest request14 = new OEmbedRequest(statusId, url);
		request14.setRelated(related);
		request14.setLang(lang);
		request14.setMaxWidth(maxWidth);

		assertTrue(request1.equals(request2));
		assertTrue(request1.hashCode() == request2.hashCode());
		assertFalse(request1.equals(request3));
		assertFalse(request1.equals(request4));
		assertFalse(request1.equals(request5));
		assertFalse(request1.equals(request6));
		assertFalse(request1.equals(request7));
		assertFalse(request1.equals(request8));
		assertFalse(request1.equals(request9));
		assertFalse(request1.equals(request10));
		assertTrue(request1.equals(request11));
		assertTrue(request11.equals(request12));
		assertTrue(request11.equals(request13));
		assertTrue(request11.equals(request14));
		assertFalse(request1.equals(null));
	}

	@Test
	public void testAsHttpParameterArray() {
		final long statusId = 1L;
		final String url = "http://example.com";
		final int maxWidth = 120;
		final boolean hideMedia = false;
		final boolean hideThread = false;
		final boolean omitScript = true;
		final Align align = Align.RIGHT;
		final String[] related = new String[] {"related1", "related2"};
		final String lang = "en";

		final OEmbedRequest request = new OEmbedRequest(statusId, url);
		final HttpParameter[] params = request.asHttpParameterArray();
		assertNotNull(params);
		assertEquals(12, params.length);
		assertEquals("id", params[0].getName());
		assertEquals(Long.toString(statusId), params[0].getValue());
		assertEquals("url", params[1].getName());
		assertEquals(url, params[1].getValue());
		assertEquals("maxwidth", params[2].getName());
		assertEquals(Integer.toString(maxWidth), params[2].getValue());
		assertEquals("hide_media", params[3].getName());
		assertEquals(Boolean.toString(hideMedia), params[3].getValue());
		assertEquals("hide_thread", params[4].getName());
		assertEquals(Boolean.toString(hideThread), params[4].getValue());
		assertEquals("omit_script", params[5].getName());
		assertEquals(Boolean.toString(omitScript), params[5].getValue());
		assertEquals("align", params[6].getName());
		assertEquals(align.name().toLowerCase(), params[6].getValue());
		if (related.length > 0) {
			assertEquals("related", params[7].getName());
			assertEquals(StringUtil.join(related), params[7].getValue());
		} else {
			assertNull(params[7].getValue());
		}
		assertEquals("lang", params[8].getName());
		assertEquals(lang, params[8].getValue());
	}

	@Test
	public void testAlignEnum() {
		final Align alignLeft = Align.LEFT;
		final Align alignCenter = Align.CENTER;
		final Align alignRight = Align.RIGHT;
		final Align alignNone = Align.NONE;

		assertNotNull(alignLeft);
		assertEquals(Align.LEFT, alignLeft);
		assertNotNull(alignCenter);
		assertEquals(Align.CENTER, alignCenter);
		assertNotNull(alignRight);
		assertEquals(Align.RIGHT, alignRight);
		assertNotNull(alignNone);
		assertEquals(Align.NONE, alignNone);

		List<Align> aligns = Arrays.asList(alignLeft, alignCenter, alignRight, alignNone);
		assertEquals(4, aligns.size());
		assertTrue(aligns.contains(alignLeft));
		assertTrue(aligns.contains(alignCenter));
		assertTrue(aligns.contains(alignRight));
		assertTrue(aligns.contains(alignNone));
	}
}