package com.imsmart.servlet.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.immart.servlet.HTMLFilter;

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
	public void testFilterSimpleMessage() {
		String message = "Hello World!";
		String result = HTMLFilter.filter(message);
		assertEquals("Hello World!", result);
		assertEquals("Hello World!", HTMLFilter.filter(message.intern()));
	}

	@Test
	public void testFilterMessageWithSpecialCharacters() {
		String message = "<Hello & World!>";
		String result = HTMLFilter.filter(message);
		assertEquals("&lt;Hello &amp; &amp; World!&gt;", result);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterWithNullCharArray() {
		char[] content = null;
		StringBuffer result = new StringBuffer(50);
		HTMLFilter.filter(content, result); // This method doesn't exist in the given class.
	}

	@Test(expected = NullPointerException.class)
	public void testFilterWithNullResultStringBuffer() {
		String message = "Test Message";
		char[] content = message.toCharArray();
		StringBuffer result = null;
		HTMLFilter.filter(content, result); // This method doesn't exist in the given class.
	}

	@Test
	public void testFilterMessageWithSpecialCharactersAtBeginningAndEnd() {
		String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
		String result = HTMLFilter.filter(message);
		assertEquals("&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&lt;!DOCTYPE html PUBLIC -//W3C//DTD XHTML 1.0 Strict//EN &quot;http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd&gt;", result);
	}
}