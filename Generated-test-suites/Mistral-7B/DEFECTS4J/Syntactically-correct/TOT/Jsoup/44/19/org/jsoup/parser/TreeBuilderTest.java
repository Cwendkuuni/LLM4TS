import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Before
 public void setup() {
 treeBuilder = new MyTreeBuilder(); // replace MyTreeBuilder with the actual subclass name
 doc = treeBuilder.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://localhost:8080/");
 }

 @Test
 public void testParse() {
 assertNotNull(doc);
 }

 @Test
 public void testProcessStartTagSingleAttribute() {
 treeBuilder = new MyTreeBuilder();
 doc = treeBuilder.parse("<p class='test'>Test</p>", "http://localhost:8080/");
 Element pElement = doc.select("p").first();
 assertNotNull(pElement);
 assertEquals("test", pElement.classes().get(0));
 }

 @Test
 public void testProcessStartTagMultipleAttributes() {
 treeBuilder = new MyTreeBuilder();
 doc = treeBuilder.parse("<p id='test' class='test' title='Test'>Test</p>", "http://localhost:8080/");
 Element pElement = doc.select("p").first();
 assertNotNull(pElement);
 assertEquals("test", pElement.id());
 assertEquals("Test", pElement.attr("title"));
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder = new MyTreeBuilder();
 String input = "<p><h1>Hello World</h1></p>";
 doc = treeBuilder.parse(input, "http://localhost:8080/");
 Element pElement = doc.select("p").first();
 assertNotNull(pElement);
 treeBuilder.processEndTag("h1");
 assertEquals(0, pElement.childSize());
 }

 @Test
 public void testProcessInvalidStartTag() {
 treeBuilder = new MyTreeBuilder();
 String input = "<p><x></x>";
 doc = treeBuilder.parse(input, "http://localhost:8080/");
 assertNull(doc.select("x").first());
 }

 @Test(expected = IllegalStateException.class)
 public void testRunParserWithErrors() {
 treeBuilder = new MyTreeBuilder();
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, "http://localhost:8080/", errors);
 assertNotNull(doc);
 assertEquals(1, errors.size());
 }

 @Test
 public void testProcessStartTagEmptyName() {
 treeBuilder = new MyTreeBuilder() {
 @Override
 protected boolean process(Token token) {
 if (token.type == Token.TokenType.StartTag) {
 if (token.startTag().name().isEmpty()) {
 throw new RuntimeException("Empty tag name is not allowed.");
 }
 }
 return super.process(token);
 }
 };
 String input = "<p></p>";
 doc = treeBuilder.parse(input, "http://localhost:8080/");
 Element pElement = doc.select("p").first();
 assertNotNull(pElement);
 }
}