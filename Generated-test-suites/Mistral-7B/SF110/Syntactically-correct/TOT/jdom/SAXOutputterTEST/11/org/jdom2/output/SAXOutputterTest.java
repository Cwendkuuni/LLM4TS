package org.jdom2.output;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;

public class SAXOutputterTest {

 private SAXOutputter outputter;
 private String expectedXml;

 @Before
 public void setUp() {
 // Initialize the outputter with a ContentHandler that captures the generated XML
 outputter = new SAXOutputter(new MyContentHandler());
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
 // Test outputting a Document
 Document doc = new Document("root");
 doc.getRootElement().addContent(new Element("child"));
 outputter.output(doc);
 expectedXml = "<root><child/></root>";
 assertEquals(expectedXml, getGeneratedXml());
 }

 @Test
 public void testOutputList() throws JDOMException, SAXException {
 // Test outputting a list of nodes
 Element root = new Element("root");
 Element child1 = new Element("child1");
 Element child2 = new Element("child2");
 root.addContent(child1);
 root.addContent(child2);
 outputter.output(root.getChildren());
 expectedXml = "<root><child1/><child2/></root>";
 assertEquals(expectedXml, getGeneratedXml());
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
 // Test outputting an Element
 Element element = new Element("element");
 outputter.output(element);
 expectedXml = "<element/>";
 assertEquals(expectedXml, getGeneratedXml());
 }

 @Test
 public void testOutputFragmentList() throws JDOMException, SAXException {
 // Test outputting a list of fragments
 Element fragment1 = new Element("fragment1");
 fragment1.setText("fragment text 1");
 Element fragment2 = new Element("fragment2");
 fragment2.setText("fragment text 2");
 outputter.outputFragment(new java.util.ArrayList<>(Arrays.asList(fragment1, fragment2)));
 expectedXml = "<fragment1>fragment text 1</fragment1><fragment2>fragment text 2</fragment2>";
 assertEquals(expectedXml, getGeneratedXml());
 }

 @Test
 public void testOutputFragmentElement() throws JDOMException, SAXException {
 // Test outputting an Element fragment
 Element element = new Element("element");
 outputter.outputFragment(element);
 expectedXml = "<element/>";
 assertEquals(expectedXml, getGeneratedXml());
 }

 @Test(expected = JDOMException.class)
 public void testErrorOutput() throws JDOMException, SAXException {
 // Test error handling when outputting an invalid node
 Element invalidNode = new Element("invalidNode");
 invalidNode.setName("root"); // Set name to be the root element name
 outputter.outputFragment(invalidNode);
 }

 private String getGeneratedXml() {
 // Get the XML produced by the ContentHandler
 return myContentHandler.getXml();
 }

 private static class MyContentHandler extends org.jdom2.output.DefaultHandler {
 private StringBuilder xml = new StringBuilder();

 @Override
 public void startDocument() throws SAXException {
 super.startDocument();
 }

 @Override
 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 super.startElement(uri, localName, qName, attributes);
 xml.append("<").append(qName);
 if (attributes != null) {
 xml.append(" ");
 for (int i = 0; i < attributes.getLength(); ++i) {
 xml.append(attributes.getName(i)).append("=\"").append(attributes.getValue(i)).append("\" ");
 }
 }
 xml.append(">");
 }

 @Override
 public void endElement(String uri, String localName, String qName) throws SAXException {
 super.endElement(uri, localName, qName);
 xml.append("</").append(qName).append(">");
 }

 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 super.characters(ch, start, length);
 xml.append(new String(ch, start, length));
 }

 @Override
 public void endDocument() throws SAXException {
 super.endDocument();
 }

 public String getXml() {
 return xml.toString();
 }
 }

}