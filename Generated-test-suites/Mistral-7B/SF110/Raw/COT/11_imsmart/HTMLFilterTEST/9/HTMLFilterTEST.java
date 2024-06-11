package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.immart.servlet.HTMLFilter;

public class HTMLFilterTest {

	@Test
	public void testFilterNullMessage() {
		String expected = null;
		String actual = HTMLFilter.filter(null);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterEmptyMessage() {
		String expected = "";
		String message = "";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterSingleCharacterMessage() {
		String expected = "";
		String message = "A";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected + actual.charAt(0), expected + actual.charAt(0));
	}

	@Test
	public void testFilterMessageWithSpecialCharacters() {
		String expected = "&lt;Hello&gt;World&amp;This&quot;is&quot;a&quot;test&lt;!&gt;";
		String message = "<Hello>World&This is a test<!>";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullContentArray() {
		String message = "";
		HTMLFilter.filter(null);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testFilterEmptyContentArray() {
		String message = "";
		char[] content = new char[0];
		HTMLFilter.filter(content);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterNegativeLengthMessage() {
		String message = "";
		HTMLFilter.filter(new String(new char[0]), -1);
	}
}