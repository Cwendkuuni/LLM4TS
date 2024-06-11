package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput_ValidHtml() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, "http://example.com");

 Assert.assertNotNull("Document should not be null", document);
 Assert.assertEquals("Title should be Test Page", "Test Page", document.title().text());
 Assert.assertEquals("Number of elements should be 3", 3, document.numChildren());
 }

 @Test
 public void testParseInput_InvalidHtmlWithTrackingErrors() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello <script>World</script></h1></body></html>";
 parser.setTrackErrors(1);
 Document document = parser.parseInput(html, "http://example.com");

 List<ParseError> errors = parser.getErrors();
 Assert.assertFalse("No errors expected", errors.isEmpty());
 Assert.assertEquals("Number of errors should be 1", 1, errors.size());
 Assert.assertEquals("Error message should contain 'script'", "Encountered unexpected character '<' in script tag: script", errors.get(0).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInput_NullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test
 public void testParseInput_EmptyHtml() {
 String html = "";
 Document document = parser.parseInput(html, "http://example.com");

 Assert.assertNotNull("Document should not be null", document);
 Assert.assertFalse("Document should not have any children", document.numChildren() > 0);
 }

 @Test
 public void testParseInput_BaseUri() {
 String html = "<a href='test.html'><img src='image.jpg'></a>";
 Document document = parser.parseInput(html, "http://example.com");
 Element link = document.selectFirst("a[href=test.html]");
 String imageSrc = link.selectFirst("img").attr("src");
 String expectedImageSrc = "http://example.com/image.jpg";

 Assert.assertNotNull("Link element should not be null", link);
 Assert.assertEquals("Image source should be 'http://example.com/image.jpg'", expectedImageSrc, imageSrc);
 }

 @Test
 public void testParseInput_TrackErrorsAndMaxErrorsReached() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello <script>World</script></h1></body></html>";
 parser.setTrackErrors(2);
 Document document = parser.parseInput(html, "http://example.com");

 List<ParseError> errors = parser.getErrors();
 Assert.assertFalse("No errors expected", errors.isEmpty());
 Assert.assertEquals("Number of errors should be 2", 2, errors.size());
 }

 @Test
 public void testParseInput_NoTrackErrors() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello <script>World</script></h1></body></html>";
 parser.setTrackErrors(0);
 Document document = parser.parseInput(html, "http://example.com");

 List<ParseError> errors = parser.getErrors();
 Assert.assertTrue("No errors expected", errors.isEmpty());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals("TreeBuilder should match", this.treeBuilder, treeBuilder);
 }
}