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
import org.junit.Assert;
import org.junit.Test;

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

public class W3CDomTest {

 private W3CDom w3CDom = new W3CDom();

 @Test
 public void testFromJsoup() throws Exception {
 Document jsoupDoc =Jsoup.parse("<html><body><h1 id='test'>Test</h1><p>Hello World</p></body></html>");
 Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

 Assert.assertNotNull(w3cDoc);
 Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());

 Element html = w3cDoc.getDocumentElement();
 Assert.assertEquals(1, html.getChildNodes().getLength());
 Element body = (Element) html.getFirstChild();
 Assert.assertEquals("body", body.getTagName());
 Assert.assertEquals(2, body.getChildNodes().getLength());

 Element h1 = (Element) body.getChildNodes().item(0);
 Assert.assertEquals("h1", h1.getTagName());
 Assert.assertEquals("1", h1.getAttribute("id"));
 Assert.assertEquals("Test", h1.getTextContent());

 Element p = (Element) body.getChildNodes().item(1);
 Assert.assertEquals("p", p.getTagName());
 Assert.assertEquals("Hello World", p.getTextContent());
 }

 @Test
 public void testConvert() throws Exception {
 Document jsoupDoc = Jsoup.parse("<html><body><h1 id='test'>Test</h1><p>Hello World</p><script src=\"script.js\"></script></body></html>");
 Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
 w3CDom.convert(jsoupDoc, w3cDoc);

 Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());

 Element html = w3cDoc.getDocumentElement();
 Assert.assertEquals(1, html.getChildNodes().getLength());
 Element body = (Element) html.getFirstChild();
 Assert.assertEquals("body", body.getTagName());
 Assert.assertEquals(3, body.getChildNodes().getLength());

 Element h1 = (Element) body.getChildNodes().item(0);
 Assert.assertEquals("h1", h1.getTagName());
 Assert.assertEquals("1", h1.getAttribute("id"));
 Assert.assertEquals("Test", h1.getTextContent());

 Element p = (Element) body.getChildNodes().item(1);
 Assert.assertEquals("p", p.getTagName());
 Assert.assertEquals("Hello World", p.getTextContent());

 Element script = (Element) body.getChildNodes().item(2);
 Assert.assertEquals("script", script.getTagName());
 }

 @Test
 public void testAsString() throws Exception {
 Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
 w3cDoc.appendChild(w3cDoc.createProcessingInstruction("xml", "version=\"1.0\""));
 w3cDoc.appendChild(w3cDoc.createElement("html"));
 w3cDoc.getElementsByTagName("html").item(0).appendChild(w3cDoc.createElement("body"));
 w3cDoc.getElementsByTagName("body").item(0).appendChild(w3cDoc.createTextNode("Hello World"));

 String result = w3CDom.asString(w3cDoc);
 String expected = "<processing-instruction xml-version=\"1.0\">\n<html>\n<body>\nHello World\n</body>\n</html>";
 Assert.assertEquals(expected, result);
 }

}