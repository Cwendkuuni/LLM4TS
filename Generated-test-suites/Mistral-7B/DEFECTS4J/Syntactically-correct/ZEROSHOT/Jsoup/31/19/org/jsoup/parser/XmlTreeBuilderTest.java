package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private String input;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 input = "<root><element attr='value'><subelement/></element></root>";
 }

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse(input, null, null);
 assertEquals("document", xmlTreeBuilder.currentData().nodeName());
 assertEquals(doc, stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("root", null);
 xmlTreeBuilder.process(startTag);
 Element root = (Element) stack.peek();
 assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.process(endTag);
 assertNull(stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token comment = new Token.Comment("This is a comment");
 xmlTreeBuilder.process(comment);
 Comment commentNode = (Comment) xmlTreeBuilder.currentData().childNodes().get(0);
 assertEquals("This is a comment", commentNode.data());
 }

 @Test
 public void testProcessCharacter() {
 Token character = new Token.Character('a');
 xmlTreeBuilder.process(character);
 Text textNode = (Text) xmlTreeBuilder.currentData().childNodes().get(0);
 assertEquals("a", textNode.data());
 }

 @Test
 public void testProcessDoctype() {
 Token doctype = new Token.Doctype("xml", null, null);
 xmlTreeBuilder.process(doctype);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.currentData().childNodes().get(0);
 assertEquals("xml", documentType.nodeName());
 }

 @Test
 public void testProcessSelfClosingTag() {
 Token startTag = new Token.StartTag("selfClosing", null);
 xmlTreeBuilder.process(startTag);
 Element selfClosingElement = (Element) stack.peek();
 assertEquals("selfClosing", selfClosingElement.nodeName());
 xmlTreeBuilder.process(startTag); // should not add to the stack
 assertNull(stack.peek());
 }

 @Test
 public void testProcessUnknownTag() {
 Token startTag = new Token.StartTag("unknown", null);
 xmlTreeBuilder.process(startTag);
 Element unknownElement = (Element) stack.peek();
 assertFalse(unknownElement.isSelfClosing());
 }

 @Test
 public void testPopStackToClose() {
 Token startTag = new Token.StartTag("root", null);
 xmlTreeBuilder.process(startTag);
 Element root = (Element) stack.peek();
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(stack.peek());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token token = new Token(Token.Type.UNKNOWN, null);
 xmlTreeBuilder.process(token);
 }
}