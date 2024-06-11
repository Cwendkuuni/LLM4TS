package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Before
 public void setup() {
 Validate.notNull(treeBuilder = new ParserTreeBuilder());
 Validate.notNull(doc = treeBuilder.parse("", ""));
 }

 @Test
 public void testParseEmptyInput() {
 assertNotNull(doc);
 }

 @Test
 public void testParseInvalidInput() {
 treeBuilder.initialiseParse("Invalid Input", "", ParseErrorList.noTracking());
 treeBuilder.runParser();
 // Since the input is invalid, the test is expected to fail
 // with ParseError exceptions
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag().name("html");
 treeBuilder.process(startTag);
 Element htmlElement = treeBuilder.currentElement();
 assertNotNull(htmlElement);
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 tokeniser = new Tokenizer(new StringReader("<html lang=\"en\"></html>"), treeBuilder.errors);
 start.reset();
 start.nameAttr("html", new Attributes() {{
 attributes().put("lang", "en");
 }});
 treeBuilder.process(start);
 Element htmlElement = treeBuilder.currentElement();
 assertNotNull(htmlElement);
 assertNotNull(htmlElement.attr("lang"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag().name("html");
 treeBuilder.process(endTag);
 Element htmlElement = treeBuilder.currentElement();
 assertNotNull(htmlElement);
 assertNull(treeBuilder.currentElement());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessInvalidToken() {
 Token invalidToken = new Token(Token.TokenType.EOF, "");
 treeBuilder.process(invalidToken);
 }

 @Test
 public void testStack() {
 // Add elements to the stack
 treeBuilder.processStartTag("html");
 treeBuilder.processStartTag("head");
 treeBuilder.processStartTag("title");
 treeBuilder.processEndTag("title");
 treeBuilder.processEndTag("head");
 treeBuilder.processEndTag("html");

 // Check the stack size and the last element
 assertNotNull(treeBuilder.currentElement());
 assertNotNull(treeBuilder.currentElement().tagName());
 assertNull(treeBuilder.currentElement());
 List<Element> stack = treeBuilder.stack;
 assertNotNull(stack);
 assertFalse(stack.isEmpty());
 assertTrue(stack.size() <= 32);
 }
}

class ParserTreeBuilder extends TreeBuilder {
 // Empty implementation, all methods override the protected ones
 @Override
 protected boolean process(Token token) {
 return false;
 }

 @Override
 protected boolean processStartTag(String name) {
 return super.processStartTag(name);
 }

 @Override
 protected boolean processEndTag(String name) {
 return super.processEndTag(name);
 }

 @Override
 protected void initialiseParse(String input, String baseUri, ParseErrorList errors) {
 super.initialiseParse(input, baseUri, errors);
 }

 @Override
 protected Document parse(String input, String baseUri) {
 return super.parse(input, baseUri);
 }

 @Override
 protected void runParser() {
 super.runParser();
 }
}