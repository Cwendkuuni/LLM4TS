package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.List;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 String html = StringUtils.EMPTY;
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);

 // Check if the document is empty
 Assert.assertEquals(0, doc.getDocumentElement().getChildNodes().getLength());
 }

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to Test Page</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);

 // Check if the document contains the expected elements
 Element head = doc.getDocumentElement().getElementsByTagName("head").item(0);
 Element title = head.getElementsByTagName("title").item(0);
 Element body = doc.getDocumentElement().getElementsByTagName("body").item(0);
 Element h1 = body.getElementsByTagName("h1").item(0);

 Assert.assertEquals("Test Page", title.getTextContent());
 Assert.assertEquals("Welcome to Test Page", h1.getTextContent());
 }

 @Test
 public void testParseHtmlWithComments() {
 String html = "<html><!-- This is a comment -->" +
 "<head><title>Test Page</title></head>" +
 "<body><h1>Welcome to Test Page</h1>" +
 "<!-- Another comment -->" +
 "<!-- <script src=\"script.js\"></script> --></body>" +
 "</html>";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);

 // Check if the document contains the expected elements and comments
 List<Node> comments = doc.getDocumentElement().getElementsByTagName("comment");
 Assert.assertEquals(2, comments.size());

 Element head = doc.getDocumentElement().getElementsByTagName("head").item(0);
 Element title = head.getElementsByTagName("title").item(0);
 Element body = doc.getDocumentElement().getElementsByTagName("body").item(0);
 Element h1 = body.getElementsByTagName("h1").item(0);

 Assert.assertEquals("Test Page", title.getTextContent());
 Assert.assertEquals("Welcome to Test Page", h1.getTextContent());
 }

 @Test
 public void testParseHtmlWithXmlDeclaration() {
 String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
 "<html><head><title>Test Page</title></head><body><h1>Welcome to Test Page</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);

 // Check if the document contains the expected elements and xml declaration
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.getDocumentElement().getFirstChild();
 Assert.assertNotNull(xmlDeclaration);
 Assert.assertEquals("1.0", xmlDeclaration.getVersion());
 Assert.assertEquals("UTF-8", xmlDeclaration.getEncoding());

 Element head = doc.getDocumentElement().getElementsByTagName("head").item(0);
 Element title = head.getElementsByTagName("title").item(0);
 Element body = doc.getDocumentElement().getElementsByTagName("body").item(0);
 Element h1 = body.getElementsByTagName("h1").item(0);

 Assert.assertEquals("Test Page", title.getTextContent());
 Assert.assertEquals("Welcome to Test Page", h1.getTextContent());
 }

 @Test
 public void testParseHtmlWithBaseTag() {
 String html = "<html><base href=\"http://sub.example.com\"><head><title>Test Page</title></head><body><h1>Welcome to Test Page</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);

 // Check if the base URI has been updated
 Assert.assertEquals("http://sub.example.com", doc.getBaseURI());

 Element head = doc.getDocumentElement().getElementsByTagName("head").item(0);
 Element title = head.getElementsByTagName("title").item(0);
 Element body = doc.getDocumentElement().getElementsByTagName("body").item(0);
 Element h1 = body.getElementsByTagName("h1").item(0);

 Assert.assertEquals("Test Page", title.getTextContent());
 Assert.assertEquals("Welcome to Test Page", h1.getTextContent());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Welcome to Test Page</h1></body>";
 String baseUri = "http://example.com";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

 // Check if the document contains the expected element
 Element body = doc.getDocumentElement().getElementsByTagName("body").item(0);
 Element h1 = body.getElementsByTagName("h1").item(0);

 Assert.assertEquals("Welcome to Test Page", h1.getTextContent());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 String html = null;
 String baseUri = "http://example.com";

 try {
 Parser.parse(html, baseUri);
 } catch (IllegalArgumentException e) {
 // Expected exception, test message
 Assert.assertEquals("HTML is null", e.getMessage());
 }
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseInvalidBaseUri() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to Test Page</h1></body></html>";
 String baseUri = null;

 try {
 Parser.parse(html, baseUri);
 } catch (IllegalArgumentException e) {
 // Expected exception, test message
 Assert.assertEquals("baseUri cannot be null", e.getMessage());
 }
 }
}