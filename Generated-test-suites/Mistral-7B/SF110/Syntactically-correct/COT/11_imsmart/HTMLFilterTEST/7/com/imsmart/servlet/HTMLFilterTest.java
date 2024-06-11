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
	public void testFilterSimpleMessage() {
		String message = "Hello World!";
		String result = HTMLFilter.filter(message);
		String expected = "Hello World!";
		expected = expected.replaceAll("<", "&lt;");
		expected = expected.replaceAll(">", "&gt;");
		expected = expected.replaceAll("&", "&amp;");
		expected = expected.replaceAll("\"", "&quot;");
		assertEquals(expected, result);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullCharArray() {
		char[] content = null;
		String result = HTMLFilter.filter(new String(content));
	}

	@Test
	public void testFilterMessageWithSpecialCharacters() {
		String message = "<html><head><title>Test & Quotes: \"'&</title></head><body><p>Test &lt; &gt; &amp; &quot;</p></body></html>";
		String result = HTMLFilter.filter(message);
		String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test &amp; Quotes: &quot;&#39;&amp;&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;p&gt;Test &lt; &gt; &amp; &quot;&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
		assertEquals(expected, result);
	}

	@Test
	public void testFilterLargeMessage() {
		String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		String result = HTMLFilter.filter(message);
		assertEquals(message, result); // No changes expected for valid input
	}

	@Test
	public void testFilterMessageWithMalformedInput() {
		String message = "Hello <script>alert('XSS');</script> World!";
		String result = HTMLFilter.filter(message);
		String expected = "Hello &lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt; World!";
		assertEquals(expected, result);
	}
}