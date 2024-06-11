package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @SuppressWarnings("deprecation")
 @BeforeClass
 public static void setUpClass() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Before
 public void setUp() {
 parser.settings(parser.settings());
 }

 /**
 * Test parsing HTML with a valid input.
 */
 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, "http://test.com");

 Assert.assertEquals("Test Page", document.title().text());
 Assert.assertEquals("Hello World", document.body().children().get(0).text());
 Assert.assertEquals(0, parser.getErrors().size());
 }

 /**
 * Test parsing HTML with an invalid input (missing closing tag).
 */
 @Test
 public void testParseInvalidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1>";
 Document document = parser.parseInput(html, "http://test.com");

 Assert.assertEquals(1, parser.getErrors().size());
 ParseError parseError = parser.getErrors().get(0);
 Assert.assertEquals("org.jsoup.parser.ParseError", parseError.getClass().getName());
 Assert.assertEquals(ParseError.TagMismatch, parseError.getExceptionType());
 Assert.assertEquals(ParseError.MISMATCH_TAG_END, parseError.getLocation().line());
 Assert.assertEquals(ParseError.MISMATCH_TAG_END, parseError.getLocation().column());
 Assert.assertEquals("unclosed tag: body", parseError.getMessage());
 }

 /**
 * Test setting and getting the TreeBuilder.
 */
 @Test
 public void testSetAndGetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);

 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 /**
 * Test enabling and disabling parse error tracking.
 */
 @Test
 public void testTrackErrors() {
 // Enable error tracking
 parser.setTrackErrors(1);
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1><script>throw new Error('Test Error')</script></body></html>";
 Document document = parser.parseInput(html, "http://test.com");
 Assert.assertEquals(2, parser.getErrors().size());

 // Disable error tracking
 parser.setTrackErrors(0);
 Document document2 = parser.parseInput(html, "http://test.com");
 Assert.assertEquals(0, parser.getErrors().size());
 }
}