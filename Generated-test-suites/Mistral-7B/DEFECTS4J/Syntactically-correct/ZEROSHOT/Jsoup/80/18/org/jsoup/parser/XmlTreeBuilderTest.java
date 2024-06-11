package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @After
 public void tearDown() {
 xmlTreeBuilder = null;
 doc = null;
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child1><child2>Hello</child2></child1></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");

 Element root = doc.rootElement();
 Element child1 = root.child(0);
 Element child2 = child1.child(0);

 Assert.assertEquals("root", root.tagName());
 Assert.assertEquals("child1", child1.tagName());
 Assert.assertEquals("child2", child2.tagName());
 Assert.assertEquals("Hello", child2.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child></child></root>"), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnknownTokenType() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><unknown>Hello</unknown></root>"), "http://example.com");
 Validate.fail("Unexpected test to pass");
 }

 @Test
 public void testInsertStartTag() {
 Token startTag = new Token.StartTag("root");
 xmlTreeBuilder.insert(startTag);

 Element root = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);

 Assert.assertEquals("root", root.tagName());
 }

 @Test
 public void testInsertEndTag() {
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTag);

 Assert.assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("<!-- This is a comment -->");
 xmlTreeBuilder.insert(commentToken);

 Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);

 Assert.assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('&');
 xmlTreeBuilder.insert(characterToken);

 TextNode characterNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);

 Assert.assertEquals("&", characterNode.data());
 }

 @Test
 public void testInsertDoctype() {
 Token doctypeToken = new Token.Doctype("xml", null, null, null, null);
 xmlTreeBuilder.insert(doctypeToken);

 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);

 Assert.assertEquals("xml", doctypeNode.name());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child><grandchild>Hello</grandchild></child>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

 Element child = (Element) nodes.get(0);
 Element grandchild = (Element) child.child(0);

 Assert.assertEquals("child", child.tagName());
 Assert.assertEquals("grandchild", grandchild.tagName());
 Assert.assertEquals("Hello", grandchild.text());
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

 Assert.assertEquals(0, nodes.size());
 }
}