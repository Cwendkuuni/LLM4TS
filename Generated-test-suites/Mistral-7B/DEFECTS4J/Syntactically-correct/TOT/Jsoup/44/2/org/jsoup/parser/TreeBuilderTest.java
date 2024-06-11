import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder builder;
 private Document doc;

 @Before
 public void setup() {
 this.builder = new SimpleTreeBuilder();
 this.doc = null;
 }

 @Test
 public void testParseValidDocument() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 this.doc = this.builder.parse(input, "http://example.com");
 assertNotNull(this.doc);
 assertEquals("http://example.com", this.doc.baseUri());
 }

 @Test
 public void testParseDocumentWithErrors() {
 String input = "<html><head><title>Test</title>"; // missing end tag
 this.builder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 if (token.type == Token.TokenType.ERROR) {
 this.errors.add(token.error);
 return false;
 }
 return super.process(token);
 }
 };
 this.doc = this.builder.parse(input, "http://example.com", TreeBuilder.ParseErrorList.withTracking());
 assertNotNull(this.doc);
 assertFalse(this.doc.bodyElements().isEmpty());
 // check that an error was reported
 assertEquals(1, this.builder.errors().size());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 this.builder = new SimpleTreeBuilder();
 this.doc = this.builder.parse(input, "http://example.com");
 Element current = this.builder.currentElement();
 Token startTagToken = new Token.StartTag("h1");
 assertTrue(this.builder.processStartTag(startTagToken.name()));
 assertEquals("h1", current.tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String input = "<div id='test' class='testClass'><p>Test</p></div>";
 this.builder = new SimpleTreeBuilder();
 this.doc = this.builder.parse(input, "http://example.com");
 Element current = this.builder.currentElement();
 Token startTagToken = new Token.StartTag("div").name("div").attr("id", "test").attr("class", "testClass");
 assertTrue(this.builder.processStartTag(startTagToken));
 assertEquals("div", current.tagName());
 assertEquals("test", current.id());
 assertEquals("testClass", current.classes().get(0));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></p></body></html>";
 this.builder = new SimpleTreeBuilder();
 this.doc = this.builder.parse(input, "http://example.com");
 Element current = this.builder.currentElement();
 Token endTagToken = new Token.EndTag("p");
 assertTrue(this.builder.processEndTag(endTagToken.name()));
 assertNull(current);
 }
}

class SimpleTreeBuilder extends TreeBuilder {
 @Override
 protected boolean process(Token token) {
 return super.process(token);
 }
}