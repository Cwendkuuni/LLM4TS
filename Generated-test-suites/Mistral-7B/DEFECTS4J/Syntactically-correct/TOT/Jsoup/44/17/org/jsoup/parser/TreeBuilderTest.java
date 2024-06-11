import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 doc = treeBuilder.parse("", "", ParseErrorList.noTracking());
 }

 @Test
 public void testParseWithEmptyInput() {
 assertEquals(0, doc.body().childNodes().size());
 }

 @Test
 public void testParseWithValidInput() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 if (token.type == Token.TokenType.START_TAG) {
 if (token.name().equalsIgnoreCase("html")) {
 assertEquals("", token.attr("xmlns:html"));
 stack.add(doc.createElement("html"));
 return true;
 } else if (token.name().equalsIgnoreCase("body")) {
 stack.add(doc.createElement("body"));
 return true;
 }
 }
 return false;
 }
 };
 treeBuilder.parse("<html xmlns:html=\"\"> <body>", "", ParseErrorList.noTracking());
 Element html = currentElement();
 Element body = currentElement();
 assertNotNull(html);
 assertNotNull(body);
 assertEquals("html", html.tagName());
 assertEquals("body", body.tagName());
 }

 @Test
 public void testParseWithErrorScenario() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 if (token.type == Token.TokenType.EOF) {
 errors.add(new ParseError("Unexpected EOF", token.line(), token.column()));
 return false;
 }
 return true;
 }
 };
 String input = "<html><body><h1<";
 doc = treeBuilder.parse(input, "", ParseErrorList.trackingErrors());
 assertEquals(1, doc.body().childNodes().size());
 Element error = doc.body().childNodes().get(0);
 assertTrue(error instanceof Element);
 assertTrue(error.tagName().equals("parse-error"));
 }
}