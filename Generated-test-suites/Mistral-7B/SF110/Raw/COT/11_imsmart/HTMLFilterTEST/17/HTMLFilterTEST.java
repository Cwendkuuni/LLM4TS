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
	public void testFilterSingleCharacter() {
		String message = "A";
		String result = HTMLFilter.filter(message);
		assertEquals("A", result);
	}

	@Test
	public void testFilterSpecialCharacters() {
		String message = "<>&\"";
		String result = HTMLFilter.filter(message);
		assertEquals("&lt;&gt;&amp;&quot;", result);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullContent() {
		char[] content = null;
		StringBuffer result = new StringBuffer(content.length + 50);
		HTMLFilter.filter(content, result); // Using method overload for testing
		fail("NullPointerException not thrown");
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testFilterInvalidIndex() {
		String message = "abc";
		char[] content = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		content[message.length()] = 'x';
		StringBuffer result = new StringBuffer(content.length + 50);
		HTMLFilter.filter(content, result); // Using method overload for testing
		fail("ArrayIndexOutOfBoundsException not thrown");
	}

	@Test
	public void testFilterLongMessage() {
		String message = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-={}[]|\\:;\",<.>?/";
		String result = HTMLFilter.filter(message);
		assertEquals(message.length() + 50, result.length());
	}

	@Test
	public void testFilterMessageWithHTMLTag() {
		String message = "<html><body><h1>Hello World!</h1></body></html>";
		String result = HTMLFilter.filter(message);
		assertEquals("&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;", result);
	}

	@Test
	public void testFilterMessageWithMultipleHTMLTags() {
		String message = "<html><body><h1>Hello World!</h1><p>This is a paragraph.</p><a href=\"http://example.com\">Link</a></body></html>";
		String result = HTMLFilter.filter(message);
		assertEquals("&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;p&gt;This is a paragraph.&lt;/p&gt;&lt;a href=&quot;http://example.com&quot;&gt;Link&lt;/a&gt;&lt;/body&gt;&lt;/html&gt;", result);
	}
}