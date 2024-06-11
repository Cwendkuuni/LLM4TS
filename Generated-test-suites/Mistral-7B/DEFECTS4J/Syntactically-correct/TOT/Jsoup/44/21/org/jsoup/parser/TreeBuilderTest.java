package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private String input;

 @Before
 public void setUp() {
 input = " <html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 }

 @Test
 public void testParseNormalInput() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse(input, "http://test.com", ParseErrorList.noTracking());
 Document doc = treeBuilder.parse(input, "http://test.com");
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().select("h1").text());
 }

 @Test
 public void testParseWithErrorTracking() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder() {
 @Override
 protected ParseErrorList errors() {
 return new ParseErrorList();
 }
 };
 TreeBuilder spyTreeBuilder = spy(treeBuilder);
 doReturn(input).when(spyTreeBuilder).input;
 doReturn("http://test.com").when(spyTreeBuilder).baseUri;
 doReturn(ParseErrorList.instance()).when(spyTreeBuilder).errors;

 Document doc = spyTreeBuilder.parse(input, "http://test.com");
 verify(spyTreeBuilder, times(1)).runParser();

 Token token = ArgumentCaptor.forClass(Token.class).capture(spyTreeBuilder::process);
 assertEquals(Token.TokenType.START_TAG, token.getType());
 assertEquals("html", token.getName());

 ParseError error = ArgumentCaptor.forClass(ParseError.class).capture(spyTreeBuilder::reportError);
 assertNotNull(error);
 assertEquals("Invalid start tag html", error.getMessage());

 assertEquals(1, spyTreeBuilder.errors().size());
 }

 @Test
 public void testProcessStartTag() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 Token startTag = new Token.StartTag().name("html");
 assertTrue(treeBuilder.processStartTag(startTag));
 }

 @Test(expected = NullPointerException.class)
 public void testProcessStartTagWithNullName() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 assertFalse(treeBuilder.processStartTag(null));
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 String input = "<div id='test' class='class1 class2'><p>Test</p></div>";
 start.reset().name("div").attrs(new Attributes(){{
 setAttribute("id", "test");
 setAttribute("class", "class1 class2");
 }});
 assertTrue(treeBuilder.processStartTag(start));
 Element div = stack.get(stack.size()-1);
 assertEquals("test", div.id());
 assertEquals("class1 class2", div.classes().toString());
 }

 @Test
 public void testProcessEndTag() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 Token endTag = new Token.EndTag().name("div");
 assertTrue(treeBuilder.processEndTag(endTag));
 assertEquals(stack.size(), 1);
 }

 @Test(expected = NullPointerException.class)
 public void testProcessEndTagWithNullName() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 assertFalse(treeBuilder.processEndTag(null));
 }

 @Test
 public void testCurrentElement() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 Element div = new Element("div");
 stack.add(div);
 assertEquals(div, treeBuilder.currentElement());
 }

 @Test
 public void testCurrentElementWhenStackIsEmpty() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 assertEquals(null, treeBuilder.currentElement());
 }
}