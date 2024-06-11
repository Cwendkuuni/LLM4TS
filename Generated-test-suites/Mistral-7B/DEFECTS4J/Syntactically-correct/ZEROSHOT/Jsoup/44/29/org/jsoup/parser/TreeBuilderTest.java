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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private CharacterReader characterReader;
 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private Document doc;

 @Before
 public void setUp() {
 characterReader = mock(CharacterReader.class);
 tokeniser = mock(Tokeniser.class);
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected Document parse(String input, String baseUri) {
 return parse(input, baseUri, errors);
 }

 @Override
 protected Document parse(String input, String baseUri, ParseErrorList errors) {
 initialiseParse(input, baseUri, errors);
 runParser();
 return doc;
 }

 @Override
 protected void processStartTag(String name) {
 // No-op
 }

 @Override
 protected void processStartTag(String name, Attributes attrs) {
 // No-op
 }

 @Override
 protected void processEndTag(String name) {
 // No-op
 }

 @Override
 protected Element currentElement() {
 return null;
 }
 };

 when(characterReader.getText()).thenReturn("some input");
 when(tokeniser.read()).thenAnswer(invocation -> {
 Token token = new Token();
 token.type = Token.TokenType.START_TAG;
 return token;
 });
 }

 @Test
 public void testParse() {
 treeBuilder.initialiseParse("some input", "baseUri", errors);
 treeBuilder.parse("some input", "baseUri");
 assertNotNull(treeBuilder.doc);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
 treeBuilder.parse(null, "baseUri");
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 treeBuilder.parse("some input", null);
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.doc = new Document("baseUri");
 treeBuilder.processStartTag("tagName");
 assertNull(treeBuilder.currentElement());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 treeBuilder.doc = new Document("baseUri");
 Attributes attrs = new Attributes();
 attrs.put("attrName", "attrValue");
 treeBuilder.processStartTag("tagName", attrs);
 assertNotNull(treeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTag() {
 Element currentElement = new Element("tagName");
 treeBuilder.stack.add(currentElement);
 treeBuilder.processEndTag("tagName");
 assertNull(treeBuilder.currentElement());
 }
}