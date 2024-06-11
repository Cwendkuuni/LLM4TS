package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Test class for Parser class.
 */
public class ParserTest {

 private static final String HTML_WITH_HEAD_AND_BODY = "<html><head><title>Test Title</title></head>" +
         "<body><h1>Test Heading</h1></body></html>";
 private static final String BODY_FRAGMENT = "<div class=\"test\">Test Content</div>";
 private static final String RELAXED_BODY_FRAGMENT = "<div class=\"test\" data-attr=\"test\">Test Content</div>";
 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
     parser = new Parser(HTML_WITH_HEAD_AND_Body, null, false);
     doc = parser.parse();
 }

 @Test
 public void testParseHtmlWithHeadAndBody() {
     assertEquals("Test Title", doc.title());
     assertEquals("Test Heading", doc.getElementsByTag("h1").first().text());
 }

 @Test
 public void testParseBodyFragment() {
     parser = new Parser(BODY_FRAGMENT, null, true);
     doc = parser.parse();
     Element body = doc.body();
     assertEquals("div", body.tagName());
     Element testDiv = body.getElementsByClass("test").first();
     assertEquals("Test Content", testDiv.text());
 }

 @Test
 public void testParseRelaxedBodyFragment() {
     parser = new Parser(RELAXED_BODY_FRAGMENT, null, true);
     parser.relaxed = true;
     doc = parser.parse();
     Element body = doc.body();
     assertEquals("div", body.tagName());
     Element testDiv = body.getElementsByClass("test").first();
     assertEquals("Test Content", testDiv.text());
     assertTrue(testDiv.tag().isData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
     try {
         parser = new Parser(null, null, false);
         parser.parse();
         fail();
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtml() {
     try {
         parser = new Parser("", null, false);
         parser.parse();
         fail();
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidTagName() {
     try {
         parser = new Parser("<invalidTag>Test</invalidTag>", null, false);
         parser.parse();
         fail();
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testParseHtmlWithBaseTag() {
     parser = new Parser(HTML_WITH_HEAD_AND_BODY, "http://example.com/", false);
     doc = parser.parse();
     assertEquals("http://example.com/", doc.baseUri());
 }

 @Test
 public void testParseBodyFragmentWithBaseTag() {
     parser = new Parser(BODY_FRAGMENT, "http://example.com/", true);
     doc = parser.parse();
     assertEquals("http://example.com/", doc.baseUri());
 }
}