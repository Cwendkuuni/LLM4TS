package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private String baseUri = "http://test.com";
 private ParseErrorList errors = new ParseErrorList();
 private StringBuilder input;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 input = new StringBuilder();
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse(input.toString(), baseUri, errors);
 assertEquals("org.jsoup.nodes.Document", xmlTreeBuilder.stack.peek().getClass().getName());
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("root", null, null);
 xmlTreeBuilder.process(startTag);
 Element root = (Element) xmlTreeBuilder.stack.peek();
 assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.stack.push(new Element("root", baseUri, null));
 xmlTreeBuilder.process(endTag);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Token comment = new Token.Comment("this is a comment");
 xmlTreeBuilder.process(comment);
 assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
 assertTrue(xmlTreeBuilder.getDocument().childNodes().get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 Token character = new Token.Character('A');
 xmlTreeBuilder.process(character);
 assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
 assertTrue(xmlTreeBuilder.getDocument().childNodes().get(0) instanceof TextNode);
 }

 @Test
 public void testProcessDoctype() {
 Token doctype = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.process(doctype);
 assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
 assertTrue(xmlTreeBuilder.getDocument().childNodes().get(0) instanceof DocumentType);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, null, null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.stack.push(new Element("parent", baseUri, null));
 xmlTreeBuilder.stack.push(new Element("child", baseUri, null));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testPopStackToCloseNotFound() {
 xmlTreeBuilder.stack.push(new Element("parent", baseUri, null));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknownTag"));
 assertEquals(1, xmlTreeBuilder.stack.size());
 }
}