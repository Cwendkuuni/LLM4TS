import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private String baseUri;
 private ParseErrorList errors;
 private CharacterReader characterReader;
 private Tokeniser tokeniser;
 private ArrayList<Element> stack;

 @Before
 public void setUp() {
 input = "some input";
 baseUri = "https://example.com";
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, errors);
 doc = treeBuilder.parse(input, baseUri);
 characterReader = treeBuilder.reader;
 tokeniser = treeBuilder.tokeniser;
 stack = treeBuilder.stack;
 }

 @Test
 public void testInitialiseParse() {
 Validate.notNull(treeBuilder.reader, "reader must not be null");
 Validate.notNull(treeBuilder.tokeniser, "tokeniser must not be null");
 Validate.notNull(treeBuilder.doc, "doc must not be null");
 Validate.notNull(treeBuilder.stack, "stack must not be null");
 Validate.assertEquals(baseUri, treeBuilder.baseUri, "baseUri must be set correctly");
 }

 @Test
 public void testParse() {
 assertSame(doc, treeBuilder.parse(input, baseUri), "parse should return the same Document instance");
 }

 @Test
 public void testRunParser() {
 // This test is not comprehensive as it's hard to assert the internal state of the parser
 // but it's a good starting point to check if the parser runs without errors
 treeBuilder.runParser();
 }

 @Test
 public void testProcessStartTag() {
 String tagName = "div";
 Element element = new Element("div");
 treeBuilder.processStartTag(tagName);
 assertSame(element, currentElement(), "currentElement should be set to the newly created Element");
 }

 @Test
 public void testProcessEndTag() {
 String tagName = "div";
 treeBuilder.processEndTag(tagName);
 assertNull(currentElement(), "currentElement should be null after processing an end tag");
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String tagName = "p";
 Attributes attributes = new Attributes();
 attributes.put("id", "someId");
 attributes.put("class", "someClass");
 treeBuilder.processStartTag(tagName, attributes);
 Element element = currentElement();
 assertEquals("p", element.tagName(), "tagName should be set correctly");
 assertEquals("someId", element.id(), "id should be set correctly");
 assertEquals("someClass", element.classes().toString(), "classes should be set correctly");
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessUnknownTokenType() {
 Token token = new Token("someTokenType", "someValue");
 treeBuilder.process(token);
 }

 @Test
 public void testProcessEOF() {
 treeBuilder.runParser();
 Token eofToken = tokeniser.read();
 treeBuilder.process(eofToken);
 assertNull(currentElement(), "currentElement should be null after processing EOF");
 }

 private String input;
}