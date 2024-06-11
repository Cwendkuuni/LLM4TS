package org.jdom2.output;

import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.xml.sax.helpers.DefaultHandler;

public class SAXOutputterTest {

 private SAXOutputter outputter;
 private StringWriter writer;

 @Before
 public void setup() {
 writer = new StringWriter();
 outputter = new SAXOutputter(new DefaultHandler(), new DefaultHandler(), new DefaultHandler(), null, null, null, null);
 outputter.setSAXOutputProcessor(new SAXOutputter.DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
 Document doc = new Document();
 doc.setRootElement(new Element("root"));
 doc.getRootElement().addContent(new Text("Hello World"));
 outputter.output(doc);
 String expectedXml = "<root><text>Hello World</text></root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputEmptyDocument() throws JDOMException, SAXException {
 Document doc = new Document();
 outputter.output(doc);
 String expectedXml = "<root/>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputNullDocument() throws JDOMException, SAXException {
 outputter.output(null);
 assertEquals("", writer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputDocumentWithNullWriter() throws JDOMException, SAXException {
 outputter.output(new Document(), null);
 }

 @Test
 public void testOutputListOfContents() throws JDOMException, SAXException {
 List<Content> contents = new JDOMNameSpaceList();
 contents.add(new Comment("This is a comment"));
 contents.add(new Element("element"));
 contents.add(new Text("This is some text"));
 outputter.output(contents);
 String expectedXml = "<!--This is a comment-->\n<element>\n<text>This is some text</text>\n";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
 Element element = new Element("element");
 element.addContent(new Text("This is some text"));
 outputter.output(element);
 String expectedXml = "<element><text>This is some text</text></element>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputNullElement() throws JDOMException, SAXException {
 outputter.output(null);
 assertEquals("", writer.toString());
 }

 @Test
 public void testOutputEmptyElement() throws JDOMException, SAXException {
 Element element = new Element("element");
 outputter.output(element);
 String expectedXml = "<element/>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputCDATA() throws JDOMException, SAXException {
 CDATA cdata = new CDATA("<![CDATA[This is some CDATA]]>");
 outputter.outputFragment(cdata);
 String expectedXml = "<![CDATA[This is some CDATA]]>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws JDOMException, SAXException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-version", "1.0");
 outputter.outputFragment(pi);
 String expectedXml = "<?xml version='1.0'?>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputEntityRef() throws JDOMException, SAXException {
 EntityRef entityRef = new EntityRef("ampersand");
 outputter.outputFragment(entityRef);
 String expectedXml = "&amp;";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputText() throws JDOMException, SAXException {
 Text text = new Text("This is some text");
 outputter.outputFragment(text);
 String expectedXml = "This is some text";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputNullText() throws JDOMException, SAXException {
 outputter.outputFragment(null);
 assertEquals("", writer.toString());
 }

 @Test
 public void testOutputEmptyText() throws JDOMException, SAXException {
 Text text = new Text("");
 outputter.outputFragment(text);
 assertEquals("", writer.toString());
 }
}