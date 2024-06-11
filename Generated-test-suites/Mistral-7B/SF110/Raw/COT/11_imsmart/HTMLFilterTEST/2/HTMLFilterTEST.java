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
		String message = "";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterSimpleMessage() {
		String expected = "&lt;html&gt;&amp;some&quot;text&quot;&gt;";
		String message = "<html&amp;some\"text\">";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullCharacterArray() {
		HTMLFilter.filter(null);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testFilterEmptyCharacterArray() {
		char[] content = {};
		StringBuffer result = new StringBuffer(50);
		HTMLFilter.filter(content, result);
	}

	@Test
	public void testFilterWithSpecialCharacters() {
		String expected = "&lt;script src=&quot;javascript:alert('XSS')&quot; type=&quot;text/javascript&quot;&gt;";
		String message = "<script src=\"javascript:alert('XSS')\" type=\"text/javascript\"></script>";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterWithTags() {
		String expected = "&lt;tag1&gt;text1&lt;/tag1&gt;&lt;tag2&gt;text2&lt;/tag2&gt;";
		String message = "<tag1>text1</tag1><tag2>text2</tag2>";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterWithNestedTags() {
		String expected = "&lt;tag1&gt;text1&lt;tag2&gt;text2&lt;/tag2&gt;&lt;/tag1&gt;";
		String message = "<tag1>text1<tag2>text2</tag2></tag1>";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterWithAttributes() {
		String expected = "&lt;tag attr=&quot;value&quot;&gt;text&lt;/tag&gt;";
		String message = "<tag attr=\"value\">text</tag>";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterWithMultipleAttributes() {
		String expected = "&lt;tag attr1=&quot;value1&quot; attr2=&quot;value2&quot;&gt;text&lt;/tag&gt;";
		String message = "<tag attr1=\"value1\" attr2=\"value2\">text</tag>";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterWithSelfClosingTag() {
		String expected = "&lt;img src=&quot;image.jpg&quot; alt=&quot;Image&quot;/&gt;";
		String message = "<img src=\"image.jpg\" alt=\"Image\" />";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

	@Test
	public void testFilterWithEntities() {
		String expected = "&lt;tag&gt;Hello &lt;world&gt;&lt;/tag&gt;";
		String message = "<tag>Hello <world></world></tag>";
		String actual = HTMLFilter.filter(message);
		assertEquals(expected, actual);
	}

}