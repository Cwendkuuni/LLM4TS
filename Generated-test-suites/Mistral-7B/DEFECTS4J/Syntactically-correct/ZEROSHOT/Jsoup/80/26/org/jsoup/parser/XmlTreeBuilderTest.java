package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 @Mock
 private ParseErrorList errorList;
 @Mock
 private ParseSettings parseSettings;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.defaultSettings = () -> parseSettings;
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element>Hello</element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUrl");
 Element rootElement = document.getRootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals("baseUrl", rootElement.baseUri());
 List<Node> childNodes = rootElement.childNodes();
 assertEquals(1, childNodes.size());
 Element element = (Element) childNodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("Hello", element.text());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element>Hello</element>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUrl", errorList, parseSettings);
 Element element = (Element) parsedNodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("baseUrl", element.baseUri());
 assertEquals("Hello", element.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "baseUrl");
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = mock(Token.StartTag.class);
 when(startTagToken.getName()).thenReturn("element");
 when(startTagToken.isSelfClosing()).thenReturn(false);
 xmlTreeBuilder.insert(startTagToken);
 Element element = xmlTreeBuilder.currentElement();
 assertEquals("element", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = mock(Token.EndTag.class);
 when(endTagToken.getName()).thenReturn("element");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = mock(Token.Character.class);
 when(characterToken.getData()).thenReturn("Hello");
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNodes().get(0);
 assertEquals("Hello", textNode.text());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = mock(Token.Comment.class);
 when(commentToken.getData()).thenReturn("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Comment commentNode = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testProcessDocumentType() {
 Token documentTypeToken = mock(Token.Doctype.class);
 when(documentTypeToken.getName()).thenReturn("xml");
 xmlTreeBuilder.insert(documentTypeToken);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
 assertEquals("xml", documentTypeNode.tagName());
 }
}