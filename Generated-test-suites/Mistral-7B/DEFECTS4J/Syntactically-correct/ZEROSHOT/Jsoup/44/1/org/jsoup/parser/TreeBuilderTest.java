import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private CharacterReader characterReader;
 private Tokeniser tokeniser;
 private Document doc;
 private ParseErrorList errors;

 @Before
 public void setup() {
 input = "some input string";
 baseUri = "baseUri";
 errors = ParseErrorList.noTracking();

 treeBuilder = new MyTreeBuilder(); // assuming MyTreeBuilder is a subclass of TreeBuilder
 characterReader = mock(CharacterReader.class);
 tokeniser = mock(Tokeniser.class);

 when(characterReader.read()).thenReturn(new Token("some token", Token.TokenType.TEXT));
 when(tokeniser.read()).thenCallRealMethod();

 treeBuilder.initialiseParse(input, baseUri, errors);
 treeBuilder.tokeniser = tokeniser;
 treeBuilder.reader = characterReader;
 }

 @Test
 public void testParse() {
 Document doc = treeBuilder.parse(input, baseUri);
 assertEquals(baseUri, doc.baseUri());
 assertNotNull(doc.body());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 treeBuilder = new MyTreeBuilder();
 treeBuilder.parse(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 treeBuilder = new MyTreeBuilder();
 treeBuilder.parse(input, null);
 }

 @Test
 public void testRunParser() {
 treeBuilder.runParser();
 }

 @Test
 public void testProcessStartTag() {
 String name = "div";
 treeBuilder.processStartTag(name);
 assertNotNull(treeBuilder.currentElement());
 assertEquals(name, treeBuilder.currentElement().tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String name = "p";
 Attributes attributes = new Attributes();
 attributes.put("id", "someId");
 attributes.put("class", "someClass");
 treeBuilder.processStartTag(name, attributes);
 assertNotNull(treeBuilder.currentElement());
 assertEquals(name, treeBuilder.currentElement().tagName());
 assertEquals("someId", treeBuilder.currentElement().id());
 assertEquals("someClass", treeBuilder.currentElement().classes());
 }

 @Test
 public void testProcessEndTag() {
 String name = "div";
 treeBuilder.processEndTag(name);
 assertEquals(treeBuilder.stack.size(), 0);
 }

 private String input;
 private String baseUri;
}