import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 @Test
 public void testParseSimpleDocument() {
 TreeBuilder treeBuilder = new SimpleTreeBuilder(); // assuming SimpleTreeBuilder extends TreeBuilder

 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = treeBuilder.parse(input, baseUri);

 Element html = doc.body();
 Element title = html.head().title();
 Element h1 = html.selectFirst("h1");

 assertEquals("Test", title.text());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseDocumentWithErrors() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 if (token.type == Token.TokenType.ERROR) {
 errors.add(token.errorMessage());
 return false;
 }
 return super.process(token);
 }
 };

 String input = "<html><head><title>Test</title>"; // incomplete tag
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 Document doc = treeBuilder.parse(input, baseUri, errors);

 assertTrue(errors.size() > 0);
 }

 @Test
 public void testParseDocumentWithEndTagMismatch() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 if (token.type == Token.TokenType.END_TAG && !token.name().equals("body")) {
 errors.add("End tag <" + token.name() + "> does not match the start tag <body>");
 return false;
 }
 return super.process(token);
 }
 };

 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></p>"; // body tag should be closed with </body>
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 Document doc = treeBuilder.parse(input, baseUri, errors);

 assertTrue(errors.size() > 0);
 }
}