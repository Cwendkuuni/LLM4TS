package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Tokeniser tokeniser;
 private String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";

 @Before
 public void setup() {
 initialiseTest();
 }

 @Test
 public void testParseValidInput() {
 Document doc = treeBuilder.parse(input, "http://example.com");
 assertNotNull(doc);
 assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParseWithErrors() {
 ParseErrorList errors = new ParseErrorList();
 Document doc = treeBuilder.parse(input, "http://example.com", errors);
 assertNotNull(doc);
 assertEquals(0, errors.size());
 }

 @Test
 public void testParseInvalidInput() {
 ParseErrorList errors = new ParseErrorList();
 Document doc = treeBuilder.parse("invalid input", "http://example.com", errors);
 assertNull(doc);
 assertFalse(errors.isEmpty());
 }

 @Test
 public void testProcessStartTag() {
 tokeniser = new Tokeniser(new CharacterReader(" <html>"), null);
 treeBuilder = new TagNameTreeBuilder();
 Token token = tokeniser.read();
 assertTrue(treeBuilder.processStartTag("html"));
 assertEquals("html", treeBuilder.currentElement().tagName());
 }

 @Test
 public void testProcessEndTag() {
 tokeniser = new Tokeniser(new CharacterReader(" <html><body></html>"), null);
 treeBuilder = new TagNameTreeBuilder();
 tokeniser.read(); // <html>
 tokeniser.read(); // <body>
 tokeniser.read(); // </body>
 assertTrue(treeBuilder.processEndTag("body"));
 assertNull(treeBuilder.currentElement());
 }

 private void initialiseTest() {
 treeBuilder = new TagNameTreeBuilder();
 tokeniser = new Tokeniser(new CharacterReader(input), null);
 }
}

class TagNameTreeBuilder extends TreeBuilder {
 @Override
 protected boolean process(Token token) {
 // Your implementation here
 return super.process(token);
 }
}