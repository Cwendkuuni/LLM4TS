package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.junit.Assert;

import java.util.List;

public class XmlTreeBuilderTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<root><a><b/><c/></a></root>", "", new XmlTreeBuilder());
 }

 @After
 public void tearDown() {
 doc = null;
 }

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root><a><b/><c/></a></root>";
 String baseUri = "";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 Assert.assertNotNull(xmlTreeBuilder.doc);
 Assert.assertEquals("xml", xmlTreeBuilder.doc.outputSettings().syntax().name());
 Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Element rootElement = doc.rootElement();
 Element aElement = rootElement.child(0);
 Element bElement = aElement.child(0);
 Token startTag = new Token.StartTag("a");
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.process(startTag);
 Element xmlRootElement = xmlTreeBuilder.doc.rootElement();
 Element xmlAAelement = xmlRootElement.child(0);
 Assert.assertEquals(aElement, xmlAAelement);
 }

 @Test
 public void testProcessEndTag() {
 Element rootElement = doc.rootElement();
 Element aElement = rootElement.child(0);
 Element bElement = aElement.child(0);
 Token endTag = new Token.EndTag("a");
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.process(endTag);
 Stack<Element> stack = xmlTreeBuilder.stack;
 Assert.assertEquals(stack.size(), 0);
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("# This is a comment");
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.process(commentToken);
 List<Node> commentNodes = xmlTreeBuilder.doc.childNodes();
 Assert.assertEquals(commentNodes.size(), 1);
 Assert.assertTrue(commentNodes.get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.process(characterToken);
 List<Node> textNodes = xmlTreeBuilder.doc.childNodes();
 Assert.assertEquals(textNodes.size(), 1);
 Assert.assertTrue(textNodes.get(0) instanceof TextNode);
 Assert.assertEquals(textNodes.get(0).text(), "A");
 }

 @Test
 public void testProcessDoctype() {
 String doctype = "<!DOCTYPE html SYSTEM \"JSPS.dtd\">";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(doctype, "", new ParseErrorList());
 List<Node> doctypeNodes = xmlTreeBuilder.doc.childNodes();
 Assert.assertEquals(doctypeNodes.size(), 1);
 Assert.assertTrue(doctypeNodes.get(0) instanceof DocumentType);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 Token unexpectedToken = new Token(1, "Unexpected token", "");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<root><a><b/><c/></a></root>";
 List<Node> expectedNodes = doc.childNodes();
 List<Node> parsedNodes = XmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList()).toArray();
 Assert.assertEquals(expectedNodes.size(), parsedNodes.size());
 for (int i = 0; i < expectedNodes.size(); i++) {
 Assert.assertEquals(expectedNodes.get(i).getClass(), parsedNodes.get(i).getClass());
 }
 }
}