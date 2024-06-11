package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version='1.0'><root></root></xml>";
 Stack<Node> stack = new Stack<>();
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, null, errors);
 assertEquals(1, stack.size());
 assertInstanceOf(Document.class, stack.peek());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullInput() {
 String input = null;
 Stack<Node> stack = new Stack<>();
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, null, errors);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml version='1.0'><root><child></child></root></xml>";
 Token startTag = new Token.StartTag("root", null);
 Element rootElement = xmlTreeBuilder.insert(startTag);
 assertEquals("root", rootElement.nodeName());
 assertEquals("", rootElement.baseUri());
 Stack<Element> stack = new Stack<>();
 stack.push(rootElement);
 Token childStartTag = new Token.StartTag("child", null);
 xmlTreeBuilder.process(childStartTag);
 assertEquals(2, stack.size());
 assertInstanceOf(Element.class, stack.peek());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml version='1.0'><root><child></child></root>";
 Token startTag = new Token.StartTag("root", null);
 Element rootElement = xmlTreeBuilder.insert(startTag);
 assertEquals("root", rootElement.nodeName());
 Stack<Element> stack = new Stack<>();
 stack.push(rootElement);
 Token childEndTag = new Token.EndTag("child");
 xmlTreeBuilder.process(childEndTag);
 assertEquals(1, stack.size());
 assertInstanceOf(Element.class, stack.peek());
 }

 @Test
 public void testProcessEndTagNotPresent() {
 String input = "<xml version='1.0'><root></root>";
 Token startTag = new Token.StartTag("root", null);
 Element rootElement = xmlTreeBuilder.insert(startTag);
 assertEquals("root", rootElement.nodeName());
 Stack<Element> stack = new Stack<>();
 stack.push(rootElement);
 Token endTag = new Token.EndTag("child");
 xmlTreeBuilder.process(endTag);
 assertEquals(1, stack.size());
 assertInstanceOf(Element.class, stack.peek());
 }

 @Test
 public void testProcessComment() {
 String input = "<xml version='1.0'><!--comment--></xml>";
 Token commentToken = new Token.Comment("comment");
 Comment comment = xmlTreeBuilder.insert(commentToken);
 assertInstanceOf(Comment.class, comment);
 Stack<Node> stack = new Stack<>();
 stack.push(comment);
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml version='1.0'>abc</xml>";
 Token characterToken = new Token.Character('a');
 TextNode characterNode = xmlTreeBuilder.insert(characterToken);
 assertInstanceOf(TextNode.class, characterNode);
 Stack<Node> stack = new Stack<>();
 stack.push(characterNode);
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">";
 Token doctypeToken = new Token.Doctype("xml", null, null, null);
 DocumentType doctypeNode = xmlTreeBuilder.insert(doctypeToken);
 assertInstanceOf(DocumentType.class, doctypeNode);
 Stack<Node> stack = new Stack<>();
 stack.push(doctypeNode);
 }

 @Test
 public void testPopStackToClose() {
 String input = "<xml version='1.0'><root><child><grandchild></grandchild></child></root>";
 Stack<Element> stack = new Stack<>();
 Element rootElement = new Element("root", null);
 stack.push(rootElement);
 Element childElement = new Element("child", null);
 rootElement.appendChild(childElement);
 stack.push(childElement);
 Element grandchildElement = new Element("grandchild", null);
 childElement.appendChild(grandchildElement);
 stack.push(grandchildElement);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("grandchild"));
 assertEquals(2, stack.size());
 assertInstanceOf(Element.class, stack.peek());
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 String input = "<xml version='1.0'><root><unknown></unknown></root>";
 Stack<Element> stack = new Stack<>();
 Element rootElement = new Element("root", null);
 stack.push(rootElement);
 Element unknownElement = new Element("unknown", null);
 rootElement.appendChild(unknownElement);
 stack.push(unknownElement);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknown"));
 assertEquals(1, stack.size());
 assertInstanceOf(Element.class, stack.peek());
 }

 @Test
 public void testPopStackToCloseNoMatchingTag() {
 String input = "<xml version='1.0'><root><child></child></root>";
 Stack<Element> stack = new Stack<>();
 Element rootElement = new Element("root", null);
 stack.push(rootElement);
 Element childElement = new Element("child", null);
 rootElement.appendChild(childElement);
 stack.push(childElement);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("grandchild"));
 assertEquals(1, stack.size());
 assertInstanceOf(Element.class, stack.peek());
 }
}