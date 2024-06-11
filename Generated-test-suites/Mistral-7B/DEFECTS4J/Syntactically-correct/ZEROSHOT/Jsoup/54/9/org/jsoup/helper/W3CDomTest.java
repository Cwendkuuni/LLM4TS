package org.jsoup.helper;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Attribute;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 Document jsoupDocument = org.jsoup.parser.Parser.parseFromString("<html><head></head><body><h1>Hello World</h1></body></html>", null);
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:x=\"\">" +
 " <head xmlns:x=\"\"></head>" +
 " <body xmlns:x=\"\">" +
 " <h1 xmlns:x=\"\">Hello World</h1>" +
 " </body>" +
 "</html>", new W3CDom().asString(w3cDocument));
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = org.jsoup.parser.Parser.parseFromString("<html><head></head><body><h1>Hello World</h1></body></html>", null);
 Document w3cDocument = Document.newDocument();
 new W3CDom().convert(jsoupDocument, w3cDocument);
 assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:x=\"\">" +
 " <head xmlns:x=\"\"></head>" +
 " <body xmlns:x=\"\">" +
 " <h1 xmlns:x=\"\">Hello World</h1>" +
 " </body>" +
 "</html>", new W3CDom().asString(w3cDocument));
 }

 @Test
 public void testConvertWithRootElement() {
 Document jsoupDocument = org.jsoup.parser.Parser.parseFromString("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", null);
 Document w3cDocument = Document.newDocument();
 new W3CDom().convert(jsoupDocument, w3cDocument, "html");
 assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:x=\"\">" +
 " <head xmlns:x=\"\">" +
 " <title xmlns:x=\"\">Test</title>" +
 " </head>" +
 " <body xmlns:x=\"\">" +
 " <h1 xmlns:x=\"\">Hello World</h1>" +
 " </body>" +
 "</html>", new W3CDom().asString(w3cDocument));
 }

 @Test
 public void testAsString() {
 Document w3cDocument = Document.newDocument();
 w3cDocument.appendChild(w3cDocument.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\""));
 w3cDocument.appendChild(w3cDocument.createElementNS("http://www.w3.org/1999/xhtml", "html"));
 w3cDocument.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "html").item(0).appendChild(createElement("head"));
 w3cDocument.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "html").item(0).appendChild(createElement("body"));
 w3cDocument.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "html").item(0).getElementsByTagNameNS("*", "head").item(0).appendChild(createElement("title", "Test"));
 w3cDocument.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "html").item(0).getElementsByTagNameNS("*", "body").item(0).appendChild(createElement("h1", "Hello World"));
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:x=\"\">" +
 " <head xmlns:x=\"\"><title xmlns:x=\"\">Test</title></head>" +
 " <body xmlns:x=\"\"><h1 xmlns:x=\"\">Hello World</h1></body>" +
 " </html>", new W3CDom().asString(w3cDocument));
 }

 private Element createElement(String tagName, String textContent) {
 Element element = Document.newDocument().createElement(tagName);
 if (textContent != null) {
 Text textNode = Document.newDocument().createTextNode(textContent);
 element.appendChild(textNode);
 }
 return element;
 }

}