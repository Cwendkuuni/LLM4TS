package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.imsmart.servlet.HTMLFilter;

public class HTMLFilterTest {

	@Test
	public void testFilterNullMessage() {
		String result = HTMLFilter.filter(null);
		assertEquals(null, result);
	}

	@Test
	public void testFilterEmptyMessage() {
		String message = "";
		String result = HTMLFilter.filter(message);
		assertEquals("", result);
	}

	@Test
	public void testFilterSingleCharacters() {
		String message = "abcd";
		String result = HTMLFilter.filter(message);
		assertEquals("abcd", result);
	}

	@Test
	public void testFilterSpecialCharacters() {
		String message = "<>&\"";
		String result = HTMLFilter.filter(message);
		assertEquals("&lt;&gt;&amp;&quot;", result);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullCharacterArray() {
		char[] content = null;
		String result = HTMLFilter.filter(new String(content));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterNegativeLengthMessage() {
		String message = "abcdefg";
		HTMLFilter.filter(message.subString(-3, message.length()));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testFilterArrayIndexOutOfBounds() {
		String message = "abc";
		char[] content = new char[message.length()];
		message.getChars(message.length(), 0, content, 0);
		String result = HTMLFilter.filter(new String(content));
	}
}