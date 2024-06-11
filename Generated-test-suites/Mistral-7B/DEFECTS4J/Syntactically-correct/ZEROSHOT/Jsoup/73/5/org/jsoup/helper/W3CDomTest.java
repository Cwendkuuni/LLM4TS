package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

 private W3CDom w3CDom;
 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setup() {
 w3CDom = new W3CDom();

 jsoupDocument = neworg.jsoup.nodes.Document();
 jsoupDocument.setTitle("Test Title");
 Element titleElement = jsoupDocument.getElementsByTagName("title").getItem(0);
 titleElement.text("Test Title - Modified");
 titleElement.attributes().put("data-test", "true");

 Element bodyElement = jsoupDocument.getElementsByTagName("body").getItem(0);
 bodyElement.text("Hello World!");

 Text textElement = jsoupDocument.createTextNode("This is a test text node.");
 bodyElement.appendChild(textElement);

 Comment commentElement = jsoupDocument.createComment("This is a test comment.");
 bodyElement.appendChild(commentElement);
 }

 @Test
 public void testFromJsoup() {
 w3cDocument = w3CDom.fromJsoup(jsoupDocument);

 assertEquals("Test Title - Modified", w3cDocument.getDocumentElement().getTagName());
 Element titleElement = (Element) w3cDocument.getElementsByTagName("title").getItem(0);
 assertEquals("Test Title - Modified", titleElement.getTextContent());
 assertEquals("data-test", titleElement.getAttribute("data-test"));

 Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").getItem(0);
 assertEquals("Hello World!", bodyElement.getTextContent());

 Text textElement = (Text) w3cDocument.getElementsByTagName("textNode").ITEM(0); // assuming a getElementsByTagName call with "textNode" exists for testing
 assertEquals("This is a test text node.", textElement.getNodeValue());

 Comment commentElement = (Comment) w3cDocument.getElementsByTagName("comment").ITEM(0); // assuming a getElementsByTagName call with "comment" exists for testing
 assertEquals("This is a test comment.", commentElement.getData());
 }

 @Test
 public void testConvert() {
 Document convertedDocument = Document.newDocument();
 w3CDom.convert(jsoupDocument, convertedDocument);

 assertEquals(jsoupDocument.getDocumentElement().getTagName(), convertedDocument.getDocumentElement().getTagName());
 Element titleElement = (Element) convertedDocument.getElementsByTagName("title").getItem(0);
 assertEquals("Test Title - Modified", titleElement.getTextContent());
 assertEquals("data-test", titleElement.getAttribute("data-test"));

 Element bodyElement = (Element) convertedDocument.getElementsByTagName("body").getItem(0);
 assertEquals("Hello World!", bodyElement.getTextContent());

 Text textElement = (Text) convertedDocument.getElementsByTagName("textNode").ITEM(0); // assuming a getElementsByTagName call with "textNode" exists for testing
 assertEquals("This is a test text node.", textElement.getNodeValue());

 Comment commentElement = (Comment) convertedDocument.getElementsByTagName("comment").ITEM(0); // assuming a getElementsByTagName call with "comment" exists for testing
 assertEquals("This is a test comment.", commentElement.getData());
 }

 @Test
 public void testAsString() {
 String w3cString = w3CDom.asString(w3cDocument);
 String jsoupString = jsoupDocument.body().html();

 assertEquals(w3cString, jsoupString);
 }

}