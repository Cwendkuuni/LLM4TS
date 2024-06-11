package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<child1 attr2=\"val2\"/>\n<child2/>\n</root>", null, null);
 assertEquals("root", xmlTreeBuilder.getCurrentElement().nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", null, null);
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<child1 attr2=\"val2\"/>\n<child2/>\n</root>", null, null);
 Token startTag = xmlTreeBuilder.tokeniser.nextToken();
 assertEquals(XmlTreeBuilder.StartTag.class, startTag.getClass());
 assertEquals("root", startTag.asStartTag().name());
 xmlTreeBuilder.process(startTag);
 Element root = xmlTreeBuilder.getCurrentElement();
 assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<child1 attr2=\"val2\"><child3/>\n</child1>\n</root>", null, null);
 Token endTag = xmlTreeBuilder.tokeniser.nextToken();
 assertEquals(XmlTreeBuilder.EndTag.class, endTag.getClass());
 assertEquals("child3", endTag.asEndTag().name());
 xmlTreeBuilder.popStackToClose(endTag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<child1 attr2=\"val2\"><unknown/>\n</child1>\n</root>", null, null);
 Token endTag = xmlTreeBuilder.tokeniser.nextToken();
 xmlTreeBuilder.popStackToClose(endTag);
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<!-- This is a comment -->\n<child1 attr2=\"val2\"/>\n<child2/>\n</root>", null, null);
 Token comment = xmlTreeBuilder.tokeniser.nextToken();
 assertEquals(XmlTreeBuilder.Comment.class, comment.getClass());
 xmlTreeBuilder.process(comment);
 Comment commentNode = (Comment) xmlTreeBuilder.getCurrentElement().child(0);
 assertEquals("This is a comment", commentNode.data());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<child1 attr2=\"val2\">Hello world!</child1>\n<child2/>\n</root>", null, null);
 Token character = xmlTreeBuilder.tokeniser.nextToken();
 assertEquals(XmlTreeBuilder.Character.class, character.getClass());
 xmlTreeBuilder.process(character);
 TextNode textNode = (TextNode) xmlTreeBuilder.getCurrentElement().child(1);
 assertEquals("Hello world!", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]>\n<root attr=\"val\"></root>", null, null);
 Token doctype = xmlTreeBuilder.tokeniser.nextToken();
 assertEquals(XmlTreeBuilder.Doctype.class, doctype.getClass());
 xmlTreeBuilder.process(doctype);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.getCurrentElement();
 assertEquals("root", doctypeNode.name());
 }

 @Test
 public void testProcessSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\"><selfClosing/></root>", null, null);
 Token startSelfClosing = xmlTreeBuilder.tokeniser.nextToken();
 assertEquals(XmlTreeBuilder.StartTag.class, startSelfClosing.getClass());
 assertEquals("selfClosing", startSelfClosing.asStartTag().name());
 assertEquals(true, startSelfClosing.asStartTag().isSelfClosing());
 xmlTreeBuilder.process(startSelfClosing);
 Element selfClosingElement = xmlTreeBuilder.getCurrentElement().child(0);
 assertEquals("selfClosing", selfClosingElement.nodeName());
 }

 @Test
 public void testProcessUnknownTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\"><unknownTag attr2=\"val2\"/></root>", null, null);
 Token startUnknownTag = xmlTreeBuilder.tokeniser.nextToken();
 assertEquals(XmlTreeBuilder.StartTag.class, startUnknownTag.getClass());
 assertEquals("unknownTag", startUnknownTag.asStartTag().name());
 assertNotNull(startUnknownTag.asStartTag().tag());
 xmlTreeBuilder.process(startUnknownTag);
 Element unknownTagElement = xmlTreeBuilder.getCurrentElement().child(0);
 assertEquals("unknownTag", unknownTagElement.nodeName());
 }

 @Test
 public void testPushPopStack() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<child1 attr2=\"val2\"><child3/><child4/>\n</child1>\n<child5/>\n</root>", null, null);
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <root>
 Element root = xmlTreeBuilder.getCurrentElement();
 assertEquals("root", root.nodeName());
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <child1>
 Element child1 = xmlTreeBuilder.getCurrentElement();
 assertEquals("child1", child1.nodeName());
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <child3>
 Element child3 = xmlTreeBuilder.getCurrentElement();
 assertEquals("child3", child3.nodeName());
 xmlTreeBuilder.popStackToClose(xmlTreeBuilder.tokeniser.nextToken()); // </child3>
 Element child3Parent = xmlTreeBuilder.getCurrentElement();
 assertEquals(child1, child3Parent);
 xmlTreeBuilder.popStackToClose(xmlTreeBuilder.tokeniser.nextToken()); // </child1>
 Element rootParent = xmlTreeBuilder.getCurrentElement();
 assertEquals(root, rootParent);
 }

 @Test
 public void testProcessEof() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root attr1=\"val1\">\n<child1 attr2=\"val2\"><child3/><child4/>\n</child1>\n<child5/>\n</root>", null, null);
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <root>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <child1>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <child3>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <child4>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // </child4>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // </child3>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // </child1>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // <child5>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // </child5>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.nextToken()); // </root>
 Document doc = xmlTreeBuilder.getDocument();
 assertEquals(5, doc.childNodes().size());
 }
}