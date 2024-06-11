package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import static org.junit.Assert.*;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 W3CDom w3CDom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello world!</p></body></html>");
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() {
 W3CDom w3CDom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello world!</p></body></html>");
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);

 assertEquals("<html><head/><body><p>Hello world!</p></body></html>", w3CDom.asString(w3cDocument));
 }

 @Test
 public void testConvertWithNamespaces() {
 W3CDom w3CDom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head/><body><p xml:id=\"p\">Hello world!</p></body></html>");
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);

 Element root = w3cDocument.getDocumentElement();
 assertEquals("http://www.w3.org/XML/1998/namespace", root.getNamespaceURI());
 assertEquals("xml", root.getPrefix("xml"));
 Element p = (Element) root.getElementsByTagName("p").item(0);
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", p.getNamespaceURI());
 assertEquals("xsi", p.getPrefix("xsi"));
 assertEquals("p", p.getLocalName());
 assertEquals("Hello world!", p.getTextContent());
 }

 @Test
 public void testComment() {
 W3CDom w3CDom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><!-- This is a comment --></html>");
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);

 assertEquals(1, w3cDocument.getChildNodes().getLength());
 Node firstChild = w3cDocument.getChildNodes().item(0);
 assertTrue(firstChild instanceof Comment);
 assertEquals("This is a comment", ((Comment) firstChild).getNodeValue());
 }

 @Test
 public void testDataNode() {
 W3CDom w3CDom = new W3CDom();
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><script type=\"text/javascript\">function test() { return 123; }</script></body></html>");
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);

 Node scriptNode = w3cDocument.getDocumentElement().getElementsByTagName("script").item(0);
 Node scriptDataNode = scriptNode.getFirstChild();
 assertEquals(Text.NODE_TYPE, scriptDataNode.getNodeType());
 assertEquals("function test() { return 123; }", scriptDataNode.getNodeValue());
 }

}