package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseReaderWithBaseUri() {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element attr1=\"value1\">content</element>\n" +
 " </root>\n";
 String baseUri = "http://www.example.com/";

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

 assertEquals("http://www.example.com/", xmlDoc.baseUri());
 Element rootElement = xmlDoc.select("root").first();
 Element element = xmlDoc.select("root > element").first();

 assertNotNull(rootElement);
 assertEquals("root", rootElement.tagName());
 assertNotNull(element);
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("content", element.text());
 }

 @Test
 public void testParseStringWithBaseUri() {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element attr1=\"value1\">content</element>\n" +
 " </root>\n";
 String baseUri = "http://www.example.com/";

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, baseUri);

 assertEquals("http://www.example.com/", xmlDoc.baseUri());
 Element rootElement = xmlDoc.select("root").first();
 Element element = xmlDoc.select("root > element").first();

 assertNotNull(rootElement);
 assertEquals("root", rootElement.tagName());
 assertNotNull(element);
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("content", element.text());
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element rootElement = new Element("root");
 xmlTreeBuilder.stack.add(rootElement);

 Token startTagToken = new Token.StartTag("element", null, new AttributeSet());
 xmlTreeBuilder.insert(startTagToken);

 Element element = (Element) xmlTreeBuilder.stack.peek();
 assertEquals("element", element.tagName());
 assertEquals(0, element.childNodes().size());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element rootElement = new Element("root");
 Element element = new Element("element");
 xmlTreeBuilder.stack.add(rootElement);
 xmlTreeBuilder.stack.add(element);

 Token endTagToken = new Token.EndTag("element");
 xmlTreeBuilder.popStackToClose(endTagToken);

 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("root", xmlTreeBuilder.stack.peek().tagName());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 Token commentToken = new Token.Comment("This is a comment.");
 xmlTreeBuilder.insert(commentToken);

 Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("This is a comment.", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 Token characterToken1 = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken1);
 Token characterToken2 = new Token.Character('b');
 xmlTreeBuilder.insert(characterToken2);

 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(1);
 assertEquals("ab", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 Token doctypeToken = new Token.Doctype("xml", null, null, null, null, null);
 xmlTreeBuilder.insert(doctypeToken);

 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", documentTypeNode.name());
 assertNull(documentTypeNode.publicIdentifier());
 assertNull(documentTypeNode.systemIdentifier());
 }

 @Test
 public void testParseFragment() {
 String xmlInputFragment = "<root><element attr1=\"value1\">content</element></root>";
 String baseUri = "http://www.example.com/";

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

 Element rootElement = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);

 assertEquals(2, nodes.size());
 assertEquals("root", rootElement.tagName());
 assertEquals(1, rootElement.childNodes().size());
 assertEquals("element", element.tagName());
 assertEquals(1, element.childNodes().size());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("content", element.childNodes().get(0).text());
 }
}