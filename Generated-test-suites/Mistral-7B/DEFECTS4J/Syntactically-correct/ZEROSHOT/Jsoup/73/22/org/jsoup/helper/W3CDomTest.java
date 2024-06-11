package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setUp() {
 jsoupDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
 }

 @Test
 public void testFromJsoup() {
 w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 assertNotNull("Expected a non-null W3C Document", w3cDocument);
 }

 @Test
 public void testConvert() {
 Document newW3cDocument = new Document();
 new W3CDom().convert(jsoupDocument, newW3cDocument);
 assertEquals("Expected the same Document objects", w3cDocument.getDocumentURI(), newW3cDocument.getDocumentURI());
 }

 @Test
 public void testAsString() {
 String jsoupString = new W3CDom().asString(w3cDocument);
 String jsoupExpected = "<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
 assertEquals("Expected the same String representation", jsoupExpected, jsoupString);
 }

 @Test
 public void testConvertNodeVisitor() {
 Element jsoupRoot = jsoupDocument.select("html").first();
 Element w3cRoot = w3cDocument.getDocumentElement();

 assertEquals("Expected the same number of children", jsoupRoot.childNodes().size(), w3cRoot.getChildNodes().getLength());

 for (int i = 0; i < jsoupRoot.childNodes().size(); i++) {
 Element jsoupChild = (Element) jsoupRoot.childNodes().get(i);
 Element w3cChild = (Element) w3cRoot.getChildNodes().item(i);

 assertEquals("Expected the same tag name", jsoupChild.tagName(), w3cChild.getTagName());

 if (jsoupChild.childNodes().size() > 0) {
 testConvertNodeVisitor(jsoupChild.childNodes().get(0), w3cChild.getChildNodes().item(0));
 }
 }
 }

 @Test
 public void testConvertNodeVisitorComment() {
 Comment jsoupComment = (Comment) jsoupDocument.select("html > body > p > comment()").first();
 Comment w3cComment = (Comment) w3cDocument.getElementsByTagName("comment").item(0);

 assertEquals("Expected the same data", jsoupComment.data(), w3cComment.getData());
 }

 @Test
 public void testConvertNodeVisitorText() {
 Text jsoupText = (Text) jsoupDocument.select("html > head > title > text()").first();
 Text w3cText = (Text) w3cDocument.getElementsByTagName("title").item(0).getTextContent();

 assertEquals("Expected the same text", jsoupText.text(), w3cText);
 }

 @Test
 public void testConvertNodeVisitorData() {
 Element jsoupData = jsoupDocument.select("html > body > script[src]").first();
 String jsoupDataSrc = jsoupData.attr("src");
 String w3cDataSrc = jsoupDocument.getElementsByTagName("script").item(0).getAttributeNS("http://www.w3.org/1999/xlink", "href");

 assertEquals("Expected the same data source", jsoupDataSrc, w3cDataSrc);
 }

 private void testConvertNodeVisitor(org.jsoup.nodes.Node jsoupNode, Node w3cNode) {
 if (jsoupNode instanceof Element) {
 testConvertElement((Element) jsoupNode, (Element) w3cNode);
 } else if (jsoupNode instanceof TextNode) {
 testConvertTextNode((TextNode) jsoupNode, (Text) w3cNode);
 } else if (jsoupNode instanceof Comment) {
 testConvertComment((Comment) jsoupNode, (Comment) w3cNode);
 } else if (jsoupNode instanceof org.jsoup.nodes.DataNode) {
 testConvertDataNode((org.jsoup.nodes.DataNode) jsoupNode, (Text) w3cNode);
 } else {
 throw new RuntimeException("Unexpected node type: " + jsoupNode.getClass().getName());
 }
 }

 private void testConvertElement(Element jsoupElement, Element w3cElement) {
 assertEquals("Expected the same tag name", jsoupElement.tagName(), w3cElement.getTagName());
 assertEquals("Expected the same number of attributes", jsoupElement.attributeCount(), w3cElement.getAttributes().getLength());
 Attributes jsoupAttributes = jsoupElement.attributes();
 for (String jsoupAttributeName : jsoupAttributes.keySet()) {
 String jsoupAttributeValue = jsoupAttributes.get(jsoupAttributeName).toString();
 String w3cAttributeName = jsoupAttributeName;
 String w3cAttributeNamespace = "";
 if (jsoupAttributeName.startsWith("xmlns:")) {
 w3cAttributeName = jsoupAttributeName.substring(6);
 w3cAttributeNamespace = jsoupAttributeName.substring(0, 6);
 }
 String w3cAttributeValue = w3cElement.getAttributeNS(w3cAttributeNamespace, w3cAttributeName);
 assertEquals("Expected the same attribute value", jsoupAttributeValue, w3cAttributeValue);
 }
 }

 private void testConvertTextNode(TextNode jsoupTextNode, Text w3cTextNode) {
 assertEquals("Expected the same text value", jsoupTextNode.getWholeText(), w3cTextNode.getData());
 }

 private void testConvertComment(Comment jsoupComment, Comment w3cComment) {
 assertEquals("Expected the same comment data", jsoupComment.data(), w3cComment.getData());
 }

 private void testConvertDataNode(org.jsoup.nodes.DataNode jsoupDataNode, Text w3cTextNode) {
 assertEquals("Expected the same data value", jsoupDataNode.getWholeData(), w3cTextNode.getData());
 }

}