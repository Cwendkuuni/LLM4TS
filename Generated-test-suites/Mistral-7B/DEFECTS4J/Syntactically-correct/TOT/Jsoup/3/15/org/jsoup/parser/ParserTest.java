package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Welcome to the Test Page</h1>" +
 "<p>This is a <b>test</b> paragraph.</p>" +
 "<a href=\"http://example.com\">Link to Example</a>" +
 "<img src=\"image.jpg\" alt=\"Image\">" +
 "<form action=\"submit.html\">" +
 "<input type=\"text\" name=\"username\">" +
 "<input type=\"submit\" value=\"Submit\">" +
 "</form>" +
 "</body></html>";

 parser = new Parser(html, "http://test.com/", false);
 doc = parser.parse();
 }

 @Test
 public void testParseHtml() {
 // Test that the root element of the parsed document is an HTML tag
 assertEquals(htmlTag, doc.tagName());

 // Test that all expected tags are present
 Equals ignoreCase = Equals.ignoreCase();
 assertTrue(doc.body().tagName().equals(bodyTag, ignoreCase));
 assertTrue(doc.head().tagName().equals(headTag, ignoreCase));
 assertTrue(doc.title().tagName().equals(titleTag, ignoreCase));

 // Test text nodes
 assertEquals("Welcome to the Test Page", doc.select("h1").text());
 assertEquals("This is a test paragraph.", doc.select("p").text());

 // Test link
 assertEquals("Link to Example", doc.select("a").text());
 assertEquals("http://example.com", doc.select("a").attr("href"));

 // Test image
 assertTrue(doc.select("img").isNull()); // No image data provided

 // Test form
 assertTrue(doc.body().select("form").size() > 0);
 Element form = doc.select("form").first();
 assertTrue(form.select("input[type=text]").size() > 0);
 assertTrue(form.select("input[type=submit]").size() > 0);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>This is a test paragraph.</p>";
 Document fragment = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
 assertEquals("", fragment.head().text());
 assertEquals("This is a test paragraph.", fragment.body().text());
 }

 @Test
 public void testParseWithBaseURI() {
 String htmlWithRelativeUrl = "<img src=\"image.jpg\">";
 Document docWithBase = Parser.parse(htmlWithRelativeUrl, "http://test.com/img/");
 assertEquals("image.jpg", docWithBase.select("img").attr("src"));
 }

 @Test
 public void testParseEmptyHtml() {
 String emptyHtml = "<html></html>";
 Document emptyDoc = Parser.parse(emptyHtml, "http://test.com/");
 assertTrue(emptyDoc.text().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 String nullHtml = null;
 try {
 Parser.parse(nullHtml, "http://test.com/");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseWithInvalidTag() {
 String invalidTagHtml = "<html><invalidTag/>";
 try {
 Parser.parse(invalidTagHtml, "http://test.com/");
 fail();
 } catch (RuntimeException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseWithNoClosingTag() {
 String noClosingTagHtml = "<html><p>Test<script";
 try {
 Parser.parse(noClosingTagHtml, "http://test.com/");
 fail();
 } catch (RuntimeException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseWithNestedTags() {
 String nestedTagsHtml = "<html><head><title>Nested</title>" +
 "<body><p>Nested <b>Text</b></p></body></html>";
 Document nestedDoc = Parser.parse(nestedTagsHtml, "http://test.com/");
 assertEquals("Nested Text", nestedDoc.select("p b").text());
 }

 @Test
 public void testParseWithImplicitParents() {
 String implicitParentsHtml = "<img src=\"image.jpg\" alt=\"Image\">";
 Document implicitParentsDoc = Parser.parse(implicitParentsHtml, "http://test.com/");
 Element img = implicitParentsDoc.select("img").first();
 assertEquals("image.jpg", img.attr("src"));
 assertTrue(img.parent().tagName().equals(bodyTag, ignoreCase));
 }

 @Test
 public void testParseWithInvalidBaseURI() {
 String invalidBaseHtml = "<base href=\"/\">";
 String htmlWithRelativeUrl = "<img src=\"image.jpg\">";
 Document docWithInvalidBase = Parser.parse(htmlWithRelativeUrl, "http://test.com/" + StringUtils.repeat("/", 100)); // Extremely long base uri to test invalid base handling
 assertEquals("", docWithInvalidBase.select("img").attr("src"));
 }
}