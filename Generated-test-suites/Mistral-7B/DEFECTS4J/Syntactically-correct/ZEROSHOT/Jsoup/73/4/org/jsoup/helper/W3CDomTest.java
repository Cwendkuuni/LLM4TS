package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.apis.views.WritableXMLStream;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

 private W3CDom w3CDom = new W3CDom();

 @Test
 public void testFromJsoup() throws Exception {
 // Given a jsoup Document
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");

 // When converting the jsoup Document to a W3C Document
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

 // Then the W3C Document should not be null
 assertNotNull(w3cDocument);

 // And the Document should contain the expected elements and text
 Element htmlElement = w3cDocument.getDocumentElement();
 assertEquals("html", htmlElement.getTagName());
 Element headElement = getChildElement(htmlElement, "head");
 assertEquals("head", headElement.getTagName());
 Element bodyElement = getChildElement(htmlElement, "body");
 assertEquals("body", bodyElement.getTagName());
 Element pElement = getChildElement(bodyElement, "p");
 assertEquals("p", pElement.getTagName());
 assertEquals("Hello World", pElement.getTextContent());
 }

 @Test
 public void testConvert() throws Exception {
 // Given a jsoup Document
 org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");

 // When converting the jsoup Document into an existing W3C Document
 Document originalW3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Document w3cDocument = Document.createDocument();
 w3CDom.convert(jsoupDocument, w3cDocument);

 // Then the W3C Document should not be null and should be equal to the original W3C Document
 assertNotNull(w3cDocument);
 assertEquals(originalW3cDocument.getDocumentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
 }

 @Test
 public void testAsString() throws Exception {
 // Given a W3C Document
 Document w3cDocument = Document.createDocument();
 Element htmlElement = w3cDocument.createElement("html");
 w3cDocument.appendChild(htmlElement);
 Element headElement = w3cDocument.createElement("head");
 htmlElement.appendChild(headElement);
 Element bodyElement = w3cDocument.createElement("body");
 htmlElement.appendChild(bodyElement);
 Element pElement = w3cDocument.createElement("p");
 bodyElement.appendChild(pElement);
 pElement.setTextContent("Hello World");

 // When converting the W3C Document to a String
 String w3cString = w3CDom.asString(w3cDocument);

 // Then the String should not be null and should represent the W3C Document
 assertNotNull(w3cString);
 assertEquals("<html><head/><body><p>Hello World</p></body></html>", w3cString);
 }

 private Element getChildElement(Element parent, String tagName) {
 return (Element) parent.getElementsByTagName(tagName).item(0);
 }
}