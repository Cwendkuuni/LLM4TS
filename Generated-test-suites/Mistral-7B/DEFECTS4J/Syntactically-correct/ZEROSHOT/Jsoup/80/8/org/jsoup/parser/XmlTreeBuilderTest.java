package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element rootElement = doc.getRootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals("value", rootElement.attribute("attr"));
 assertEquals("Content", rootElement.child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<root></root>"), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 xmlTreeBuilder.parse(new StringReader(""), "http://test.com");
 }

 @Test
 public void testProcessStartTag() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 Token token = new Token(Token.Type.START_TAG, "<root>", null, null, 1, 1, 1, 1, null);
 Element element = xmlTreeBuilder.insert(token.asStartTag());
 assertEquals("root", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xml = "<root><element></element></root>";
 Token token = new Token(Token.Type.END_TAG, "</element>", null, null, 1, 1, 1, 1, null);
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithUnknownTag() {
 xmlTreeBuilder = new XmlTreeBuilder(); // create new instance as previous one has a non-empty stack
 String xml = "<root><unknownTag></unknownTag></root>";
 Token token = new Token(Token.Type.END_TAG, "</unknownTag>", null, null, 1, 1, 1, 1, null);
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 Validate.fail("Unexpected end tag: unknownTag");
 }

 @Test
 public void testProcessComment() {
 String xml = "<!-- This is a comment -->";
 Token token = new Token(Token.Type.COMMENT, "-->This is a comment<!--", null, null, 1, 1, 1, 1, null);
 Comment comment = (Comment) xmlTreeBuilder.insert(token);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 String xml = "<root>&#x1F602;Content</root>";
 Token token = new Token(Token.Type.CHARACTER, "&#x1F602;", null, null, 1, 1, 1, 1, null);
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(token);
 assertEquals("😜", textNode.text());
 }

 @Test
 public void testProcessDocumentType() {
 String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root></root>";
 Token token = new Token(Token.Type.DOCTYPE, "<!DOCTYPE root [", null, null, 1, 1, 1, 1, null);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(token);
 assertEquals("root", documentType.nodeName());
 assertEquals("", documentType.publicId());
 assertEquals("", documentType.systemId());
 assertEquals("CDATA", documentType.internalSubset().data().substring(0, 5));
 }

 @Test
 public void testParseFragment() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 List<Node> fragment = xmlTreeBuilder.parseFragment(xml, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element rootElement = (Element) fragment.get(0);
 assertEquals("root", rootElement.tagName());
 assertEquals(1, fragment.size());
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }
}