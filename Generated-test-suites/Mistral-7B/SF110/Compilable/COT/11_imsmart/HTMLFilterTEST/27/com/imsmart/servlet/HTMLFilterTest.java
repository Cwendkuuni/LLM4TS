package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.imsmart.servlet.HTMLFilter;

public class HTMLFilterTest {

	@Test
	public void testFilterNullMessage() {
		assertNull(HTMLFilter.filter(null));
	}

	@Test
	public void testFilterEmptyMessage() {
		assertEquals("", HTMLFilter.filter(""));
	}

	@Test
	public void testFilterSingleCharacterMessage() {
		assertEquals("a", HTMLFilter.filter("a"));
	}

	@Test
	public void testFilterMessageWithSpecialCharacters() {
		assertEquals("&lt;p&gt;Hello &amp; World&quot;!&gt;", HTMLFilter.filter("<p>Hello & World</p>"));
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullCharacterArray() {
		HTMLFilter.filter(null);
	}

	@Test
	public void testFilterLongMessage() {
		final String longMessage = "This is a long message that needs to be filtered, it has more than 1000 characters and it includes all possible characters including special characters.";
		assertEquals(longMessage, HTMLFilter.filter(longMessage));
	}

	@Test
	public void testFilterMessageWithMalformedHTML() {
		assertEquals("&lt;&lt; &gt;&gt; &amp;&amp; &quot;&quot;", HTMLFilter.filter("<esc> & gt; & amp; \"\" "));
	}
}