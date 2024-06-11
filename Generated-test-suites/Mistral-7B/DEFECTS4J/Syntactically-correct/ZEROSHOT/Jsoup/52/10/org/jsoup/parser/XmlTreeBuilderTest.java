package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class XmlTreeBuilderTest {

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", new ParseErrorList());
 assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.tokeniser = new TagTokenParser(); // assume we provide a tokenizer for testing
 Token startTag = new Token.StartTag("root", null);
 builder.process(startTag);
 Element root = builder.currentElement();
 assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element root = new Element("root", "", new Attributes());
 builder.stack.add(root);
 Token endTag = new Token.EndTag("root");
 builder.process(endTag);
 assertFalse(builder.stack.isEmpty());
 assertNull(builder.stack.get(builder.stack.size() - 1));
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token commentToken = new Token.Comment("<!-- comment -->");
 builder.process(commentToken);
 Comment comment = (Comment) builder.currentNode();
 assertEquals("comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token characterToken = new Token.Character('A');
 builder.process(characterToken);
 TextNode textNode = (TextNode) builder.currentNode();
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token doctypeToken = new Token.Doctype("doctype", "", "", "");
 builder.process(doctypeToken);
 DocumentType documentType = (DocumentType) builder.currentNode();
 assertEquals("doctype", documentType.nodeName());
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element root = new Element("root", "", new Attributes());
 Element child1 = new Element("child1", "", new Attributes());
 Element child2 = new Element("child2", "", new Attributes());
 builder.stack.add(root);
 builder.stack.add(child1);
 builder.stack.add(child2);
 builder.popStackToClose(new Token.EndTag("child1"));
 assertEquals(child1, builder.stack.get(builder.stack.size() - 1));
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String input = "<root><child1></child1><child2></child2></root>";
 List<Node> nodes = builder.parseFragment(input, "", new ParseErrorList());
 assertEquals(3, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 assertEquals("child1", nodes.get(1).nodeName());
 assertEquals("child2", nodes.get(2).nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testValidateFail() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.Validate.fail("test failure");
 }
}