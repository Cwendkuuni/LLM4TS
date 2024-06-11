package com.imsmart.servlet.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.immart.servlet.HTMLFilter;

public class HTMLFilterTest {

	@Test
	public void testFilterNullMessage() {
		assertEquals(null, HTMLFilter.filter(null));
	}

	@Test
	public void testFilterEmptyMessage() {
		assertEquals("", HTMLFilter.filter(""));
	}

	@Test
	public void testFilterSimpleMessage() {
		String message = "This is a simple message.";
		String expected = "This is a simple message.";
		String result = HTMLFilter.filter(message);
		assertEquals(expected, result);
	}

	@Test
	public void testFilterMessageWithSpecialCharacters() {
		String message = "This <message> has &special& characters!";
		String expected = "This &lt;message&gt; has &amp;special&amp; characters!";
		String result = HTMLFilter.filter(message);
		assertEquals(expected, result);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterNullCharArray() {
		char[] content = null;
		String message = new String(content);
		HTMLFilter.filter(message);
	}

	@Test(expected = StringIndexOutOfBoundsException.class)
	public void testFilterLongMessage() {
		String message = new String(new char[Integer.MAX_VALUE]);
		HTMLFilter.filter(message);
	}

	@Test
	public void testFilterMessageWithOnlySpecialCharacters() {
		String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
		                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
		                "<head>" +
		                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
		                "</head>" +
		                "<body>" +
		                "<script type=\"text/javascript\">" +
		                "</script>" +
		                "</body>" +
		                "</html>";
		String expected = "&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;" +
		                "&lt;!DOCTYPE html PUBLIC -//W3C//DTD XHTML 1.0 Strict//EN &quot;http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd&gt;" +
		                "&lt;html xmlns=http://www.w3.org/1999/xhtml&gt;" +
		                "&lt;head&gt;" +
		                "&lt;meta http-equiv=&quot;Content-Type&quot; content=&quot;text/html; charset=UTF-8&quot; /&gt;" +
		                "&lt;/head&gt;" +
		                "&lt;body&gt;" +
		                "&lt;script type=&quot;text/javascript&quot;&gt;" +
		                "&lt;/script&gt;" +
		                "&lt;/body&gt;" +
		                "&lt;/html&gt;";
		String result = HTMLFilter.filter(message);
		assertEquals(expected, result);
	}

	@Test
	public void testFilterMessageWithHtmlTagsAndText() {
		String message = "<h1>This is a <b>test</b> message.</h1>";
		String expected = "&lt;h1&gt;This is a &lt;b&gt;test&lt;/b&gt; message.&lt;/h1&gt;";
		String result = HTMLFilter.filter(message);
		assertEquals(expected, result);
	}

	@Test
	public void testFilterMessageWithSpecialCharactersInsideHtmlTags() {
		String message = "<h1>&lt;script&gt;alert(&quot;Hello, World!&quot;)&lt;/script&gt;</h1>";
		String expected = "<h1>&lt;script&gt;&amp;lt;script&gt;alert(&quot;Hello, World!&quot;)&amp;lt;/script&gt;&lt;/script&gt;</h1>";
		String result = HTMLFilter.filter(message);
		assertEquals(expected, result);
	}
}