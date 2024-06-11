package org.jdom2.output;

import org.jdom2.*;
import org.jdom2.output.support.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;

public class SAXOutputterTest {

 private SAXOutputter outputter;
 private StringWriter writer;
 private Document document;

 @Before
 public void setup() {
 writer = new StringWriter();
 outputter = new SAXOutputter(new DefaultHandler(), new DefaultErrorHandler(), null, null, new DefaultLexicalHandler(), null, null);
 document = new Document(new Element("root"));
 document.getRootElement().addContent(new Element("child1").setText("Child 1"));
 document.getRootElement().addContent(new Element("child2").setText("Child 2"));
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.setContentHandler(new DefaultHandler());
 outputter.output(document);
 String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputNullDocument() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.output(null);
 // Empty string as a result is an expected behavior
 assertEquals("");
 writer.getBuffer().reset();
 }

 @Test
 public void testOutputEmptyDocument() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.output(new Document(new Element("root")));
 String expectedXml = "<root/>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputList() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.setContentHandler(new DefaultHandler());
 outputter.output(Collections.singletonList(document.getRootElement()));
 String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputElement() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.setContentHandler(new DefaultHandler());
 outputter.output(document.getRootElement());
 String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputFragment() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.setContentHandler(new DefaultHandler());
 outputter.outputFragment(document.getRootElement().getChildren().get(0));
 String expectedXml = "<child1>Child 1</child1>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputEmptyFragment() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.setContentHandler(new DefaultHandler());
 outputter.outputFragment(new Text(""));
 // Empty string as a result is an expected behavior
 assertEquals("");
 writer.getBuffer().reset();
 }

 @Test(expected = JDOMException.class)
 public void testOutputNullFragment() throws JDOMException {
 outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 outputter.setFormat(Format.getPrettyFormat());
 outputter.setContentHandler(new DefaultHandler());
 outputter.outputFragment(null);
 }
}