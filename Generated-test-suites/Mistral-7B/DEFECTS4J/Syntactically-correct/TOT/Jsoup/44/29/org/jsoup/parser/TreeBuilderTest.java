package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import java.util.ArrayList;
import java.util.List;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private final String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 private final String baseUri = "http://example.com";

 @Before
 public void setup() {
 Validate.notNull(input, "String input must not be null");
 Validate.notNull(baseUri, "BaseURI must not be null");
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // Override process method for testing purposes
 System.out.println("Processing token: " + token);
 return true;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
 }

 @Test
 public void testParse() {
 Document doc = treeBuilder.parse(input, baseUri);
 Element root = doc.rootElement();
 Validate.assertEquals("html", root.tagName());
 Validate.assertEquals(1, doc.body().children().size());
 Validate.assertEquals("h1", doc.body().children().get(0).tagName());
 Validate.assertEquals("Hello World", doc.body().children().get(0).text());
 }

 @Test
 public void testProcessStartTag() {
 start: {
 tokeniser = new Tokeniser(treeBuilder.reader, treeBuilder.errors);
 Token token = tokeniser.read();
 if (token.type != Token.TokenType.STARTTAG) {
 throw new RuntimeException("Expected STARTTAG, got " + token.type);
 }
 start.reset();
 start.name(token.name());
 treeBuilder.processStartTag(start);
 treeBuilder.currentToken = token;

 if (!treeBuilder.process(token.nextSibling())) {
 throw new RuntimeException("Failed to process next sibling");
 }
 token = tokeniser.read();
 if (token.type != Token.TokenType.ENDTAG) {
 throw new RuntimeException("Expected ENDTAG, got " + token.type);
 }
 end.reset();
 end.name(token.name());
 if (!treeBuilder.processEndTag(end.name())) {
 throw new RuntimeException("Failed to process end tag");
 }
 if (treeBuilder.currentElement() != null) {
 throw new RuntimeException("Expected stack to be empty");
 }
 }
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 start: {
 tokeniser = new Tokeniser(treeBuilder.reader, treeBuilder.errors);
 Token token = tokeniser.read();
 if (token.type != Token.TokenType.STARTTAG) {
 throw new RuntimeException("Expected STARTTAG, got " + token.type);
 }
 start.reset();
 start.name(token.name());

 List<String> names = new ArrayList<>();
 List<String> values = new ArrayList<>();

 while (tokeniser.nextAttribute() > 0) {
 Attributes attr = tokeniser.attributes();
 String name = attr.key();
 String value = attr.value();
 names.add(name);
 values.add(value);
 }
 start.nameAttr(names.get(0), values.get(0));
 treeBuilder.processStartTag(start);
 treeBuilder.currentToken = token;

 if (!treeBuilder.process(token.nextSibling())) {
 throw new RuntimeException("Failed to process next sibling");
 }
 token = tokeniser.read();
 if (token.type != Token.TokenType.ENDTAG) {
 throw new RuntimeException("Expected ENDTAG, got " + token.type);
 }
 end.reset();
 end.name(token.name());
 if (!treeBuilder.processEndTag(end.name())) {
 throw new RuntimeException("Failed to process end tag");
 }
 if (treeBuilder.currentElement() != null) {
 throw new RuntimeException("Expected stack to be empty");
 }
 }
 }

 @Test
 public void testErrorHandling() {
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // Override process method for testing purposes
 throw new RuntimeException("Test error");
 }
 };
 treeBuilder.initialiseParse(input, baseUri, errors);
 treeBuilder.parse(input, baseUri);
 Validate.assertEquals(1, errors.size());
 Validate.assertEquals("Test error", errors.get(0).message());
 }
}