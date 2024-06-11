import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.CharacterReader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseErrorList;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 private String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 private String baseUri = "http://example.com/";

 @Test
 public void testParseValidInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
 Document doc = treeBuilder.parse(input, baseUri);
 assertEquals("HTML", doc.tagName());
 assertNotNull(doc.head());
 assertNotNull(doc.title());
 assertNotNull(doc.body());
 assertNotNull(doc.getElementsByTag("h1").first());
 }

 @Test
 public void testParseInvalidInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 TreeBuilder.ParseErrorList errors = new TreeBuilder.ParseErrorList();
 treeBuilder.initialiseParse("", baseUri, errors);
 Document doc = treeBuilder.parse("", baseUri);
 assertEquals(null, doc);
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testProcessStartTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return processStartTag(token);
 }

 protected boolean processStartTag(Token token) {
 Validate.notNull(token, "Token must not be null");
 return true;
 }
 };
 TreeBuilder.ParseErrorList errors = new TreeBuilder.ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);
 Document doc = treeBuilder.parse(input, baseUri);
 Element title = doc.title();
 assertEquals("Test", title.text());
 }

 @Test
 public void testProcessEndTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return processEndTag(token.name());
 }

 protected boolean processEndTag(String name) {
 return "body".equals(name);
 }
 };
 TreeBuilder.ParseErrorList errors = new TreeBuilder.ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);
 Document doc = treeBuilder.parse(input, baseUri);
 assertNotNull(doc.getElementsByTag("h1").first());
 assertNull(doc.body().getElementsByTag("body").first());
 }
}