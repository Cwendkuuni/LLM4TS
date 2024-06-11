package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

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

public class W3CDomTest {

 private Document inJsoupDocument;
 private Document outW3CDocument;

 @Before
 public void setup() {
 inJsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
 }

 @Test
 public void testFromJsoup() {
 outW3CDocument = new W3CDom().fromJsoup(inJsoupDocument);
 assertNotNull(outW3CDocument);
 }

 @Test
 public void testAsString() {
 String w3cString = new W3CDom().asString(outW3CDocument);
 String jsoupString = inJsoupDocument.body().text();
 assertEquals(jsoupString, w3cString.substring(w3cString.indexOf("<p>Hello World</p>")));
 }

 @Test
 public void testConvert() {
 outW3CDocument = new Document();
 new W3CDom().convert(inJsoupDocument, outW3CDocument);
 assertNotNull(outW3CDocument.getDocumentElement());
 }

 @Test
 public void testConvertWithNamespaces() {
 Document inJsoupDocumentWithNamespaces = org.jsoup.Jsoup.parse("<html xmlns:xhtml='http://www.w3.org/1999/xhtml' xml:lang='en'><head><title>Test</title></head><body><p xml:id='p1' xmlns:my='http://example.com'>Hello World</p></body></html>");
 Document outW3CDocumentWithNamespaces = new Document();
 new W3CDom().convert(inJsoupDocumentWithNamespaces, outW3CDocumentWithNamespaces);
 Element rootElement = outW3CDocumentWithNamespaces.getDocumentElement();
 assertNotNull(rootElement);
 assertEquals("html", rootElement.getTagName());
 Map<String, String> expectedNamespaces = new HashMap<>();
 expectedNamespaces.put("xmlns:xhtml", "http://www.w3.org/1999/xhtml");
 expectedNamespaces.put("xml:lang", "en");
 expectedNamespaces.put("xmlns:my", "http://example.com");
 assertEquals(expectedNamespaces, getNamespaces(rootElement));
 }

 @Test
 public void testCreateComment() {
 Document commentDocument = new Document();
 Element commentParent = commentDocument.createElement("commentParent");
 commentDocument.appendChild(commentParent);
 Comment comment = new W3CDom().createComment("A Test Comment");
 commentParent.appendChild(comment);
 assertEquals("A Test Comment", comment.getData());
 }

 @Test
 public void testCreateTextNode() {
 Document textDocument = new Document();
 Element textParent = textDocument.createElement("textParent");
 textDocument.appendChild(textParent);
 Text text = new W3CDom().createTextNode("A Test Text Node");
 textParent.appendChild(text);
 assertEquals("A Test Text Node", text.getTextContent());
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidDocument() {
 Document invalidDocument = null;
 new W3CDom().fromJsoup(invalidDocument);
 }

 private Map<String, String> getNamespaces(Element element) {
 Map<String, String> namespaces = new HashMap<>();
 for (String key : element.getAttributes().keySet()) {
 if (key.startsWith("xmlns:")) {
 namespaces.put(key.substring(xmlnsPrefix.length()), element.getAttribute(key));
 }
 }
 return namespaces;
}

}