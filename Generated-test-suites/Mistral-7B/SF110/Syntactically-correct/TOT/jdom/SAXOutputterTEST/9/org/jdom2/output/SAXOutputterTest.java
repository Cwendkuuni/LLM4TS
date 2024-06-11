package org.jdom2.output;

import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.*;
import static org.junit.Assert.*;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private StringReader reader;

 @Before
 public void setup() {
 saxOutputter = new SAXOutputter();
 }

 @Test
 public void testOutputDocument() throws JDOMException, IOException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Text("Hello World"));
 saxOutputter.output(document);
 reader = new StringReader(saxOutputter.getFormat().asString(document));
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>Hello World</root>", reader.readLine());
 }

 @Test
 public void testOutputListOfContent() throws JDOMException, IOException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Text("Hello"));
 root.addContent(new Comment("This is a comment"));
 List<Content> nodes = new JDOMSerializer().serialize(document.getRootElement());
 saxOutputter.output(nodes);
 reader = new StringReader(saxOutputter.getFormat().asString(document));
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n <![CDATA[Hello]]>\n<!--This is a comment-->\n</root>", reader.readLine());
 }

 @Test
 public void testOutputElement() throws JDOMException, IOException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Element("child").setText("Hello"));
 saxOutputter.output(root);
 reader = new StringReader(saxOutputter.getFormat().asString(document));
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n <child>Hello</child>\n</root>", reader.readLine());
 }

 @Test
 public void testOutputFragmentListOfContent() throws JDOMException, IOException {
 Document document = new Document();
 List<Content> nodes = new JDOMSerializer().serialize(document.getRootElement());
 saxOutputter.outputFragment(nodes);
 reader = new StringReader(saxOutputter.getFormat().asString(document));
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>", reader.readLine());
 }

 @Test
 public void testOutputFragmentElement() throws JDOMException, IOException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Element("child").setText("Hello"));
 saxOutputter.outputFragment(root);
 reader = new StringReader(saxOutputter.getFormat().asString(document));
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<child>Hello</child>", reader.readLine());
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNull() throws JDOMException, IOException {
 saxOutputter.outputFragment(null);
 }

 @Test
 public void testSetFeatures() throws SAXException, SAXNotRecognizedException, SAXNotSupportedException {
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
 assertTrue(saxOutputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));
 }

 @Test(expected = SAXNotRecognizedException.class)
 public void testSetFeatureUnsupported() throws SAXException, SAXNotRecognizedException, SAXNotSupportedException {
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setFeature("http://xml.org/sax/features/foo", true);
 }

 @Test(expected = SAXNotSupportedException.class)
 public void testSetFeatureNotSupported() throws SAXException, SAXNotRecognizedException, SAXNotSupportedException {
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
 }

 @Test
 public void testSetProperty() throws SAXException, SAXNotRecognizedException, SAXNotSupportedException {
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", new MyLexicalHandler());
 assertEquals(saxOutputter.getLexicalHandler(), new MyLexicalHandler());
 }

 @Test(expected = SAXNotRecognizedException.class)
 public void testSetPropertyUnrecognized() throws SAXException, SAXNotRecognizedException, SAXNotSupportedException {
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.setProperty("http://xml.org/sax/properties/foo", new MyLexicalHandler());
 }
}

class MyLexicalHandler implements LexicalHandler {
 @Override
 public void startElement(String name, String namespaceURI, String qualifiedName, Attributes attrs) throws SAXException {
 System.out.println("Start element: " + qualifiedName);
 }

 @Override
 public void endElement(String name, String namespaceURI, String qualifiedName) throws SAXException {
 System.out.println("End element: " + qualifiedName);
 }

 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 System.out.println("Characters: " + new String(ch, start, length));
 }
}