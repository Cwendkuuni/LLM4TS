package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 @Mock
 private Parser parser;
 @Mock
 private Document document;
 @Mock
 private Element element;
 @Mock
 private TextNode textNode;
 @Mock
 private Comment comment;
 @Mock
 private CDataNode cDataNode;
 @Mock
 private XmlDeclaration xmlDeclaration;
 @Mock
 private DocumentType documentType;
 @Mock
 private Node node;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.PRESERVE_CASE, settings);
 }

 @Test
 public void testInitialiseParse() {
 Reader input = new StringReader("");
 String baseUri = "baseUri";
 xmlTreeBuilder.initialiseParse(input, baseUri, parser);
 assertEquals(baseUri, xmlTreeBuilder.getDocument().baseUri());
 Mockito.verify(parser).setDocument(ArgumentMatchers.eq(xmlTreeBuilder.getDocument()));
 }

 @Test
 public void testParseDocument() {
 String input = "<root></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(input), "baseUri");
 assertEquals("<root/>", document.html());
 assertEquals("<root></root>", document.outerHtml());
 assertEquals("root", document.tagName());
 assertEquals(1, document.childNodes().size());
 assertNull(document.body());
 }

 @Test
 public void testParseDocumentWithRootElementName() {
 String input = "<myRoot><child></child></myRoot>";
 Document document = xmlTreeBuilder.parse(new StringReader(input), "baseUri");
 assertEquals("<myRoot><child></child></myRoot>", document.outerHtml());
 assertEquals("myRoot", document.tagName());
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testParseDocumentWithComment() {
 String input = "<!-- This is a comment -->";
 Document document = xmlTreeBuilder.parse(new StringReader(input), "baseUri");
 List<Node> commentNodes = document.body().childNodes();
 assertEquals(1, commentNodes.size());
 assertSame(comment, commentNodes.get(0));
 }

 @Test
 public void testParseDocumentWithCDataSection() {
 String input = "<![CDATA[<script type=\"text/javascript\">function test() { alert(\"Hello World\"); }</script>]]>";
 Document document = xmlTreeBuilder.parse(new StringReader(input), "baseUri");
 List<Node> cdataNodes = document.body().childNodes();
 assertEquals(1, cdataNodes.size());
 assertSame(cDataNode, cdataNodes.get(0));
 }

 @Test
 public void testParseDocumentWithDoctypeDeclaration() {
 String input = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document document = xmlTreeBuilder.parse(new StringReader(input), "baseUri");
 Element doctype = document.childNodes().get(0);
 assertEquals("html", doctype.nodeName());
 assertEquals("about:legacy-compat", doctype.attributes().get("system"));
 }

 @Test
 public void testParseDocumentWithDoctypeDeclarationAndRootElement() {
 String input = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><root></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(input), "baseUri");
 Element doctype = document.childNodes().get(0);
 assertEquals("html", doctype.nodeName());
 assertEquals("about:legacy-compat", doctype.attributes().get("system"));
 assertEquals("root", document.tagName());
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testParseDocumentWithMultipleRootElements() {
 String input = "<root></root><root></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(input), "baseUri");
 assertEquals(2, document.childNodes().size());
 }

 @Test
 public void testProcessTokenStartTag() {
 xmlTreeBuilder.element = element;
 xmlTreeBuilder.settings = ParseSettings.preserveCase;
 Token token = new Token(Token.Type.START_TAG, "<myTag attr=\"value\" />");
 xmlTreeBuilder.process(token);
 assertSame(element, xmlTreeBuilder.stack.peek());
 assertEquals("myTag", element.tagName());
 assertTrue(element.hasAttr("attr"));
 assertEquals("value", element.attr("attr"));
 assertFalse(element.hasAttr("anotherAttr"));
 }

 @Test
 public void testProcessTokenStartTagWithSelfClosingTag() {
 xmlTreeBuilder.element = element;
 xmlTreeBuilder.settings = ParseSettings.preserveCase;
 Token token = new Token(Token.Type.START_TAG, "<myTag attr=\"value\" />");
 xmlTreeBuilder.process(token);
 assertSame(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 assertFalse(element.hasParent());
 }

 @Test
 public void testProcessTokenComment() {
 xmlTreeBuilder.comment = comment;
 Token token = new Token(Token.Type.COMMENT, "-- This is a comment -->");
 xmlTreeBuilder.process(token);
 assertSame(comment, xmlTreeBuilder.stack.peek());
 assertEquals("-- This is a comment -->", comment.data());
 }

 @Test
 public void testProcessTokenCharacter() {
 xmlTreeBuilder.textNode = textNode;
 Token token = new Token(Token.Type.CHARACTER, "A");
 xmlTreeBuilder.process(token);
 assertSame(textNode, xmlTreeBuilder.stack.peek());
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessTokenCharacterCDATA() {
 xmlTreeBuilder.cDataNode = cDataNode;
 Token token = new Token(Token.Type.CHARACTER, "<![CDATA[A]]>");
 xmlTreeBuilder.process(token);
 assertSame(cDataNode, xmlTreeBuilder.stack.peek());
 assertEquals("A", cDataNode.text());
 }

 @Test
 public void testProcessTokenEndTag() {
 xmlTreeBuilder.element = element;
 Token token = new Token(Token.Type.END_TAG, "</myTag>");
 xmlTreeBuilder.process(token);
 assertSame(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 assertNull(element);
 }

 @Test
 public void testProcessTokenEndTagWithWrongTagName() {
 xmlTreeBuilder.element = element;
 Token token = new Token(Token.Type.END_TAG, "</wrongTag>");
 xmlTreeBuilder.process(token);
 assertSame(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 assertNull(element);
 }

 @Test
 public void testProcessTokenDoctype() {
 xmlTreeBuilder.documentType = documentType;
 Token token = new Token(Token.Type.DOCTYPE, "<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
 xmlTreeBuilder.process(token);
 assertSame(documentType, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testParseFragment() {
 String input = "<root><child></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, null, "baseUri", parser);
 assertEquals(2, nodes.size());
 assertSame(element, nodes.get(0));
 assertSame(element, nodes.get(1));
 }

 @Test
 public void testParseFragmentWithElementInsideAnotherElement() {
 String input = "<root><child><grandChild></grandChild></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, null, "baseUri", parser);
 assertEquals(4, nodes.size());
 assertSame(element, nodes.get(0));
 Element child = (Element) nodes.get(1);
 assertEquals("child", child.tagName());
 Element grandChild = (Element) nodes.get(3);
 assertEquals("grandChild", grandChild.tagName());
 }
}