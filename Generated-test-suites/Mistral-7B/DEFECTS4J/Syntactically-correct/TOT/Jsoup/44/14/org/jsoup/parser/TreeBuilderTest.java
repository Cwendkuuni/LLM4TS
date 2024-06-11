package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Tokeniser tokeniser;
 private String input;

 @Before
 public void setUp() {
 input = "<html><head></head><body><div><p>Test</p></div></body></html>";
 treeBuilder = new HTMLTreeBuilder(); // assuming HTMLTreeBuilder is the concrete implementation
 tokeniser = new Tokeniser(new CharacterReader(new StringReader(input)), null);
 }

 @Test
 public void testParseValidHTML() {
 TreeBuilder treeBuilderSpy = new SpyTreeBuilder(treeBuilder); // SpyTreeBuilder is a TestSpy to check method calls
 treeBuilderSpy.initialiseParse(input, null, null);
 treeBuilderSpy.parse(input, null);

 assertEquals("<html><head></head><body><div><p>Test</p></div></body></html>", treeBuilderSpy.doc.body().html());
 assertNotNull(treeBuilderSpy.doc.head());
 assertNotNull(treeBuilderSpy.doc.body());
 assertNotNull(treeBuilderSpy.doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
 treeBuilder.initialiseParse(null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 treeBuilder.initialiseParse("", null, null);
 }

 @Test
 public void testParseEmptyInput() {
 TreeBuilder treeBuilderSpy = new SpyTreeBuilder(treeBuilder);
 treeBuilderSpy.initialiseParse("", null, null);
 treeBuilderSpy.parse("", null);

 assertEquals("", treeBuilderSpy.doc.body().html());
 assertNull(treeBuilderSpy.doc.head());
 assertNull(treeBuilderSpy.doc.body());
 }

 @Test
 public void testProcessStartTag() {
 tokeniser = new Tokeniser(new CharacterReader(new StringReader("<html")), null);
 TreeBuilder treeBuilderSpy = new SpyTreeBuilder(treeBuilder);

 treeBuilderSpy.initialiseParse("", null, null);
 treeBuilderSpy.runParser();

 assertTrue(treeBuilderSpy.processStartTag("html"));
 assertEquals("<html>", treeBuilderSpy.currentElement().tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 tokeniser = new Tokeniser(new CharacterReader(new StringReader("<div id='test' class='testClass'>")), null);
 TreeBuilder treeBuilderSpy = new SpyTreeBuilder(treeBuilder);

 treeBuilderSpy.initialiseParse("", null, null);
 treeBuilderSpy.runParser();

 assertTrue(treeBuilderSpy.processStartTag("div"));
 assertTrue(treeBuilderSpy.currentElement().tagName().equals("div"));
 assertEquals("test", treeBuilderSpy.currentElement().attr("id"));
 assertEquals("testClass", treeBuilderSpy.currentElement().attr("class"));
 }

 @Test
 public void testProcessEndTag() {
 tokeniser = new Tokeniser(new CharacterReader(new StringReader("</div>")), null);
 TreeBuilder treeBuilderSpy = new SpyTreeBuilder(treeBuilder);

 treeBuilderSpy.initialiseParse("", null, null);
 treeBuilderSpy.parse("<html><body><div></div></body></html>", null);
 treeBuilderSpy.runParser();

 assertTrue(treeBuilderSpy.processEndTag("div"));
 assertNull(treeBuilderSpy.currentElement());
 }

 @Test
 public void testProcessInvalidToken() {
 tokeniser = new Tokeniser(new CharacterReader(new StringReader("</div>abc")), null);
 TreeBuilder treeBuilderSpy = new SpyTreeBuilder(treeBuilder);

 treeBuilderSpy.initialiseParse("", null, null);
 treeBuilderSpy.parse("<html><body><div></div></body></html>", null);
 treeBuilderSpy.runParser();

 assertFalse(treeBuilderSpy.process(new Token("abc", Token.TokenType.TEXT)));
 }

 @Test
 public void testProcessEOF() {
 tokeniser = new Tokeniser(new CharacterReader(new StringReader("</div>")), null);
 TreeBuilder treeBuilderSpy = new SpyTreeBuilder(treeBuilder);

 treeBuilderSpy.initialiseParse("", null, null);
 treeBuilderSpy.parse("<html><body><div></div></body></html>", null);
 treeBuilderSpy.runParser();

 treeBuilderSpy.tokeniser.read(); // read the EOF token
 assertTrue(treeBuilderSpy.process(tokeniser.token));
 assertNull(treeBuilderSpy.currentElement());
 }
}

class SpyTreeBuilder extends TreeBuilder {
 private TreeBuilder treeBuilder;

 public SpyTreeBuilder(TreeBuilder treeBuilder) {
 this.treeBuilder = treeBuilder;
 }

 @Override
 protected boolean process(Token token) {
 treeBuilder.process(token);
 return super.process(token);
 }
}