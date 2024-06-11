package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;

/**
 * @author JohnDoe
 */
public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 String input = "<html><head><title>Test</title></head><body>" +
         "<p id='p1'>Hello World</p>" +
         "</body></html>";
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return super.process(token);
 }

 @Override
 protected boolean processStartTag(Token token) {
 System.out.println("Start Tag: " + token.name());
 return true;
 }

 @Override
 protected boolean processEndTag(Token token) {
 System.out.println("End Tag: " + token.name());
 return true;
 }
 };
 treeBuilder.initialiseParse(input, "", errors);
 }

 @Test
 public void testParse() {
 doc = treeBuilder.parse("<html><head><title>Test</title></head><body>" +
         "<p id='p1'>Hello World</p>" +
         "</body></html>", "", ParseErrorList.noTracking());
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.select("#p1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 treeBuilder.initialiseParse(null, "", errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 treeBuilder.initialiseParse("", null, errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullErrors() {
 treeBuilder.initialiseParse("", "", null);
 }

 @Test
 public void testProcessStartTag() {
 tokeniser = treeBuilder.tokeniser;
 tokeniser.reset();
 tokeniser.next(); // <html>
 treeBuilder.processStartTag("html");
 assertEquals("html", treeBuilder.currentElement().tagName());
 }

 @Test
 public void testProcessEndTag() {
 tokeniser = treeBuilder.tokeniser;
 tokeniser.reset();
 tokeniser.read(); // <html>
 tokeniser.read(); // <head>
 tokeniser.read(); // <title>
 treeBuilder.processEndTag("title");
 assertEquals("title", treeBuilder.currentElement().tagName());
 }

 @Test
 public void testCurrentElement() {
 tokeniser = treeBuilder.tokeniser;
 tokeniser.reset();
 tokeniser.read(); // <html>
 treeBuilder.processStartTag("html");
 tokeniser.read(); // <head>
 treeBuilder.processStartTag("title");
 assertEquals("title", treeBuilder.currentElement().tagName());
 }
}