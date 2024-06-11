import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Token token;
 private Tokenizer tokenizer;

 @Before
 public void setup() {
 Validate.notNull(treeBuilder = new ElementTreeBuilder());
 Validate.notNull(tokenizer = new Tokenizer());
 }

 @Test
 public void testParseWithValidInput() {
 treeBuilder.initialiseParse("<html><body><h1>Hello World</h1></body></html>", "http://example.com", ParseErrorList.noTracking());
 treeBuilder.parse(tokenizer.tokenize("<html><body><h1>Hello World</h1></body></html>"), "http://example.com");

 Document doc = treeBuilder.doc;
 assertEquals("Hello World", doc.body().text());
 assertEquals("html", doc.tagName());
 }

 @Test
 public void testParseWithBaseUri() {
 treeBuilder.initialiseParse("<a href='/about'><h1>About Us</h1></a>", "http://example.com", ParseErrorList.noTracking());
 Document doc = treeBuilder.parse("<a href='/about'><h1>About Us</h1></a>", "http://example.com");
 assertEquals("/about", doc.select("a[href]").first().attr("href"));
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.initialiseParse("<html><body>", "http://example.com", ParseErrorList.noTracking());
 Element body = treeBuilder.currentElement();
 assertTrue(body.tagName().equals("body"));

 treeBuilder.processStartTag("html");
 Element html = treeBuilder.currentElement();
 assertEquals("html", html.tagName());
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder.initialiseParse("<html><body><h1>Hello World</h1></body></html>", "http://example.com", ParseErrorList.noTracking());
 Element h1 = treeBuilder.currentElement();
 assertTrue(h1 != null);

 treeBuilder.processEndTag("h1");
 assertNull(treeBuilder.currentElement());
 }

 @Test
 public void testProcessInvalidStartTag() {
 token = tokenizer.tokenize("<html><badtag>Hello World</badtag></html>")[0];
 treeBuilder.initialiseParse("", "", ParseErrorList.noTracking());
 treeBuilder.process(token);

 assertEquals(Token.TokenType.START_TAG_MISMATCH, token.type);
 assertEquals("badtag", token.data());
 }

 @Test
 public void testProcessErrorTracking() {
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.initialiseParse("", "", errors);
 token = tokenizer.tokenize("<html><body><h1>Hello World</h1></body></html>")[0];
 treeBuilder.process(token);

 assertEquals(1, errors.size());
 assertEquals(1, errors.get(0).getLine());
 assertEquals(1, errors.get(0).getColumn());
 assertEquals("Start tag expected, '<html>' found", errors.get(0).getMessage());
 }

 @Test
 public void testProcessEOF() {
 treeBuilder.initialiseParse("", "", ParseErrorList.noTracking());
 treeBuilder.runParser();
 assertFalse(treeBuilder.doc.body() != null);
 }
}