package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.imsmart.servlet.HTMLFilter;

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
		String actual = HTMLFilter.filter("");
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterSimpleMessage() {
		String message = "Hello World!";
		String expected = "Hello World!";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterMessageWithLessThanSign() {
		String message = "<html>";
		String expected = "&lt;html&gt;";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterMessageWithGreaterThanSign() {
		String message = ">hello< world!";
		String expected = "&gt;hello&lt; world!";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterMessageWithAmpersand() {
		String message = "& hello world!";
		String expected = "&amp; hello world!";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullCharacterArray() {
		char[] content = null;
		String message = new String(content);
		HTMLFilter.filter(message);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testFilterMessageWithLongLength() {
		String message = new String(new char[Integer.MAX_VALUE]);
		HTMLFilter.filter(message);
	}
}