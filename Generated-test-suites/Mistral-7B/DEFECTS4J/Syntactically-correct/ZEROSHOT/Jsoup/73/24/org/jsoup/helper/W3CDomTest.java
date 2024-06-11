package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.apis.commons.encoding.EncodingUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

 private DocumentBuilderFactory factory;
 private DocumentBuilder builder;
 private W3CDom w3cdom;

 @Before
 public void setUp() throws Exception {
 factory = DocumentBuilderFactory.newInstance();
 builder = factory.newDocumentBuilder();
 w3cdom = new W3CDom();
 }

 @Test
 public void testFromJsoup() throws ParserConfigurationException {
 // Test empty jsoup document
 org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parseEmpty();
 Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
 assertNotNull(w3cDoc);
 assertEquals(0, w3cDoc.getDocumentElement().getChildNodes().getLength());

 // Test jsoup document with root element and text child
 org.jsoup.nodes.Document jsoupDocWithText = org.jsoup.parser.Parser.parse("<html><body><p>Hello World</p></body></html>");
 Document w3cDocWithText = w3cdom.fromJsoup(jsoupDocWithText);
 assertNotNull(w3cDocWithText);
 assertEquals("html", w3cDocWithText.getDocumentElement().getNodeName());
 Element html = (Element) w3cDocWithText.getDocumentElement();
 assertEquals(1, html.getChildNodes().getLength());
 Element body = (Element) html.getFirstChild();
 assertEquals(1, body.getChildNodes().getLength());
 Element p = (Element) body.getFirstChild();
 assertEquals("p", p.getNodeName());
 assertEquals("Hello World", p.getTextContent());

 // Test jsoup document with root element, text child and comment child
 org.jsoup.nodes.Document jsoupDocWithTextAndComment = org.jsoup.parser.Parser.parse("<html><body><p>Hello World</p><!-- Comment --></body></html>");
 Document w3cDocWithTextAndComment = w3cdom.fromJsoup(jsoupDocWithTextAndComment);
 assertNotNull(w3cDocWithTextAndComment);
 assertEquals("html", w3cDocWithTextAndComment.getDocumentElement().getNodeName());
 Element htmlWithTextAndComment = (Element) w3cDocWithTextAndComment.getDocumentElement();
 assertEquals(1, htmlWithTextAndComment.getChildNodes().getLength());
 Element bodyWithTextAndComment = (Element) htmlWithTextAndComment.getFirstChild();
 assertEquals(2, bodyWithTextAndComment.getChildNodes().getLength());
 Element pWithText = (Element) bodyWithTextAndComment.getFirstChild();
 assertEquals("p", pWithText.getNodeName());
 assertEquals("Hello World", pWithText.getTextContent());
 Element comment = (Element) bodyWithTextAndComment.getLastChild();
 assertEquals(Comment.COMMENT_NODE, comment.getNodeType());
 assertEquals("Comment", comment.getNodeName());
 assertNull(comment.getTextContent());
 }

 @Test
 public void testConvert() throws ParserConfigurationException {
 // Test empty jsoup document
 org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parseEmpty();
 Document w3cDoc = builder.newDocument();
 w3cdom.convert(jsoupDoc, w3cDoc);
 assertNotNull(w3cDoc);
 assertEquals(0, w3cDoc.getDocumentElement().getChildNodes().getLength());

 // Test jsoup document with root element and text child
 org.jsoup.nodes.Document jsoupDocWithText = org.jsoup.parser.Parser.parse("<html><body><p>Hello World</p></body></html>");
 Document w3cDocWithText = builder.newDocument();
 w3cdom.convert(jsoupDocWithText, w3cDocWithText);
 assertNotNull(w3cDocWithText);
 assertEquals("html", w3cDocWithText.getDocumentElement().getNodeName());
 Element htmlWithText = (Element) w3cDocWithText.getDocumentElement();
 assertEquals(1, htmlWithText.getChildNodes().getLength());
 Element bodyWithText = (Element) htmlWithText.getFirstChild();
 assertEquals(1, bodyWithText.getChildNodes().getLength());
 Element pWithText = (Element) bodyWithText.getFirstChild();
 assertEquals("p", pWithText.getNodeName());
 assertEquals("Hello World", pWithText.getTextContent());

 // Test jsoup document with root element, text child and comment child
 org.jsoup.nodes.Document jsoupDocWithTextAndComment = org.jsoup.parser.Parser.parse("<html><body><p>Hello World</p><!-- Comment --></body></html>");
 Document w3cDocWithTextAndComment = builder.newDocument();
 w3cdom.convert(jsoupDocWithTextAndComment, w3cDocWithTextAndComment);
 assertNotNull(w3cDocWithTextAndComment);
 assertEquals("html", w3cDocWithTextAndComment.getDocumentElement().getNodeName());
 Element htmlWithTextAndComment = (Element) w3cDocWithTextAndComment.getDocumentElement();
 assertEquals(1, htmlWithTextAndComment.getChildNodes().getLength());
 Element bodyWithTextAndComment = (Element) htmlWithTextAndComment.getFirstChild();
 assertEquals(2, bodyWithTextAndComment.getChildNodes().getLength());
 Element pWithText = (Element) bodyWithTextAndComment.getFirstChild();
 assertEquals("p", pWithText.getNodeName());
 assertEquals("Hello World", pWithText.getTextContent());
 Element comment = (Element) bodyWithTextAndComment.getLastChild();
 assertEquals(Comment.COMMENT_NODE, comment.getNodeType());
 assertEquals("Comment", comment.getNodeName());
 assertNull(comment.getTextContent());
 }

 @Test
 public void testAsString() throws ParserConfigurationException, TransformerException {
 // Test empty w3c document
 Document w3cDocEmpty = builder.newDocument();
 String w3cStringEmpty = w3cdom.asString(w3cDocEmpty);
 assertTrue(w3cStringEmpty.isEmpty());

 // Test w3c document with root element and text child
 Document w3cDocWithText = builder.newDocument();
 Element root = w3cDocWithText.createElement("html");
 Text text = w3cDocWithText.createTextNode("Hello World");
 root.appendChild(text);
 w3cDocWithText.appendChild(root);
 String w3cStringWithText = w3cdom.asString(w3cDocWithText);
 assertEquals("<?xml version='1.0'?>\n<html>\n<![CDATA[\n Hello World\n]]>\n</html>\n", w3cStringWithText);

 // Test w3c document with root element, text child and comment child
 Document w3cDocWithTextAndComment = builder.newDocument();
 Element rootWithTextAndComment = w3cDocWithTextAndComment.createElement("html");
 Text textWithComment = w3cDocWithTextAndComment.createTextNode("Hello World");
 rootWithTextAndComment.appendChild(textWithComment);
 Element comment = w3cDocWithTextAndComment.createComment("Comment");
 rootWithTextAndComment.appendChild(comment);
 w3cDocWithTextAndComment.appendChild(rootWithTextAndComment);
 String w3cStringWithTextAndComment = w3cdom.asString(w3cDocWithTextAndComment);
 assertEquals("<?xml version='1.0'?>\n<html>\n<![CDATA[\n Hello World\n]]>\n<!--Comment-->\n</html>\n", w3cStringWithTextAndComment);
 }
}