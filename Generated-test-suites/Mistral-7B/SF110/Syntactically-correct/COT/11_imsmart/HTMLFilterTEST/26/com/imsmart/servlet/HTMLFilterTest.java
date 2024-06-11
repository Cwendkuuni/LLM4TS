package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.imsmart.servlet.HTMLFilter;

public class HTMLFilterTest {

	@Test
	public void testFilterNullMessage() {
		String result = HTMLFilter.filter(null);
		assertNull(result);
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
	}

	@Test
	public void testFilterMessageWithSpecialCharacters() {
		String message = "<>&\"'";
		String result = HTMLFilter.filter(message);
		assertEquals("&lt;>&amp;&gt; &quot; &apos;", result);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullContent() {
		char[] content = null;
		StringBuffer result = new StringBuffer(content.length + 50);
		HTMLFilter.filter(content, result); // The filter method is not designed to accept char[] and StringBuffer as arguments
	}

	@Test
	public void testFilterLongMessage() {
		String message = "a" + new String(new char[2000]).replace("\0", "x"); // Create a long message with 2000 characters
		String result = HTMLFilter.filter(message);
		assertEquals(message, result); // Verify that the filtering does not change the message's length
	}
}