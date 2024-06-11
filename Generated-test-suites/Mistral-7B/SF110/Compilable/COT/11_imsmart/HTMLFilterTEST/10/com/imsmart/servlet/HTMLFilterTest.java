package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.imsmart.servlet.HTMLFilter;

public class HTMLFilterTest {

	/**
	 * Test for HTMLFilter.filter(String) method with valid input
	 * @author John Doe
	 */
	@Test
	public void testFilterValidInput() {
		assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", HTMLFilter.filter("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>"));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with null input
	 * @author John Doe
	 */
	@Test
	public void testFilterNullInput() {
		assertNull(HTMLFilter.filter(null));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with empty string input
	 * @author John Doe
	 */
	@Test
	public void testFilterEmptyInput() {
		assertEquals("", HTMLFilter.filter(""));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with input containing '<' character
	 * @author John Doe
	 */
	@Test
	public void testFilterLT() {
		assertEquals("&lt;", HTMLFilter.filter("<"));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with input containing '>' character
	 * @author John Doe
	 */
	@Test
	public void testFilterGT() {
		assertEquals("&gt;", HTMLFilter.filter(">"));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with input containing '&' character
	 * @author John Doe
	 */
	@Test
	public void testFilterAMP() {
		assertEquals("&amp;", HTMLFilter.filter("&"));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with input containing '\"' character
	 * @author John Doe
	 */
	@Test
	public void testFilterQuote() {
		assertEquals("&quot;", HTMLFilter.filter("\""));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with input containing special characters sequence
	 * @author John Doe
	 */
	@Test
	public void testFilterSpecialCharacters() {
		assertEquals("&lt;p&gt;Hello &amp; World!&lt;/p&gt;", HTMLFilter.filter("<p>Hello & World!</p>"));
	}

	/**
	 * Test for HTMLFilter.filter(String) method with input containing '<' character at the beginning
	 * @author John Doe
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterLTAtBeginning() {
		HTMLFilter.filter("<html");
	}

	/**
	 * Test for HTMLFilter.filter(String) method with input containing '<' character at the end
	 * @author John Doe
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterLTAtEnd() {
		HTMLFilter.filter("html>");
	}
}