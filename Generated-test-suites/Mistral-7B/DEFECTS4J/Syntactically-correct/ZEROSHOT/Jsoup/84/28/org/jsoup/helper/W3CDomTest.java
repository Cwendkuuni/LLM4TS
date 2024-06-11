package org.jsoup.helper;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");
 Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

 assertEquals("html", w3cDoc.getDocumentElement().getTagName());
 Element body = w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
 assertEquals("p", body.getElementsByTagName("p").item(0).getTagName());
 assertEquals("Hello World", body.getElementsByTagName("p").item(0).getTextContent());
 }

 @Test
 public void testConvert() {
 Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");
 Document w3cDoc = new Document();
 new W3CDom().convert(jsoupDoc, w3cDoc);

 assertEquals("html", w3cDoc.getDocumentElement().getTagName());
 Element body = w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
 assertEquals("p", body.getElementsByTagName("p").item(0).getTagName());
 assertEquals("Hello World", body.getElementsByTagName("p").item(0).getTextContent());
 }

 @Test
 public void testAsString() {
 Document doc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>"));
 String str = new W3CDom().asString(doc);
 assertEquals("<html><head/><body><p>Hello World</p></body></html>", str);
 }

 @Test
 public void testNamespaces() {
 Document doc = org.jsoup.Jsoup.parse("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head/><body><p xml:id=\"p1\" xmlns:my=\"http://example.com\"><my:foo xmlns:my2=\"http://example2.com\"/></p></body></html>");
 Document w3cDoc = new W3CDom().fromJsoup(doc);
 Element p = w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getElementsByTagName("p").item(0);

 assertEquals("http://example.com", p.getNamespaceURI());
 assertEquals("my", p.getPrefix());
 assertEquals("foo", p.getElementsByTagName("my:foo").item(0).getTagName());
 assertEquals("http://example2.com", p.getElementsByTagName("my:foo").item(0).getNamespaceURI());
 assertEquals("", p.getElementsByTagName("my:foo").item(0).getPrefix());
 }

 @Test
 public void testDataNode() {
 Document doc = org.jsoup.Jsoup.parse("<html><head/><body><p><data xml:id=\"data\"/></p></body></html>");
 Document w3cDoc = new W3CDom().fromJsoup(doc);
 Element p = w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getElementsByTagName("p").item(0);
 Text data = p.getElementsByTagName("data").item(0);
 assertEquals(DataNode.class, data.getClass());
 assertEquals("", data.getNodeValue());
 }

 @Test
 public void testComment() {
 Document doc = org.jsoup.Jsoup.parse("<html><head/><body><p><!-- This is a comment --></p></body></html>");
 Document w3cDoc = new W3CDom().fromJsoup(doc);
 Element p = w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getElementsByTagName("p").item(0);
 Comment comment = p.getElementsByTagName("!--").item(0);
 assertEquals(Comment.class, comment.getClass());
 assertEquals("This is a comment ", comment.getTextContent());
 }
}