package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Parser class.
 */
public class ParserTest {

 @Test
 public void testParseHTML() {
 String html = "<html><head><title>Test Title</title></head>" +
 "<body><h1>Test Heading</h1><p>Test Paragraph</p>" +
 "<textarea>Test Textarea</textarea></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element htmlNode = doc.body(); // Check if the root element is "html"
 assertEquals(htmlTag, htmlNode.tag());

 Element headNode = doc.head(); // Check if "head" element exists
 assertNotNull(headNode);

 Element titleNode = doc.head().child(0); // Check if "title" element exists and its text
 assertEquals("Test Title", titleNode.text());

 Element h1Node = htmlNode.child(0); // Check if "h1" element exists and its text
 assertEquals("Test Heading", h1Node.text());

 Element pNode = htmlNode.child(1); // Check if "p" element exists and its text
 assertEquals("Test Paragraph", pNode.text());

 Element textareaNode = htmlNode.child(2); // Check if "textarea" element exists
 assertEquals(textareaTag, textareaNode.tag());

 // Test for empty elements
 String emptyHTML = "<img src=\"image.jpg\" />";
 Document emptyDoc = Parser.parse(emptyHTML, "http://example.com");
 Element imgNode = emptyDoc.body();
 assertTrue(imgNode.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 String html = null;
 try {
 Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Pass, since IllegalArgumentException is expected as per test description
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyHtml() {
 String html = "";
 try {
 Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Pass, since IllegalArgumentException is expected as per test description
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidHtml() {
 String html = "<html<title>Test Title</html>"; // missing end tag for "html"
 try {
 Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Pass, since IllegalArgumentException is expected as per test description
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 String html = "<invalidTag name=\"Test Title\">Test</invalidTag>";
 try {
 Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Pass, since IllegalArgumentException is expected as per test description
 }
 }

 @Test
 public void testParseBodyFragment() {
 String bodyFragment = "<p>Test Paragraph</p>";
 Document doc = Parser.parseBodyFragment(bodyFragment, "http://example.com");

 assertEquals(htmlTag, doc.tagName());
 Element bodyNode = doc.body();
 assertEquals(bodyTag, bodyNode.tag());
 Element pNode = bodyNode.child(0);
 assertEquals("Test Paragraph", pNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 String html = "<html><head><title>Test Title</title></head>" +
 "<body><h1>Test Heading</h1></body></html>";
 try {
 Parser.parse(html, null);
 } catch (IllegalArgumentException e) {
 // Pass, since IllegalArgumentException is expected as per test description
 }
 }
}