package org.jdom2.output.test;

import junit.framework.TestCase;
import org.jdom2.*;
import org.jdom2.output.*;
import java.io.*;
import java.util.*;

public class XMLOutputterTest extends TestCase {

 private XMLOutputter xmlOutputter;
 private Format format;
 private OutputStream outputStream;
 private Writer writer;

 @Before
 public void setUp() {
 format = new Format();
 xmlOutputter = new XMLOutputter(format);
 try {
 outputStream = new ByteArrayOutputStream();
 writer = XMLOutputter.makeWriter(outputStream, format);
 } catch (UnsupportedEncodingException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 xmlOutputter = new XMLOutputter(format, null);
 assertNotNull(xmlOutputter.getFormat());
 assertNull(xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithNullFormatAndProcessor() {
 xmlOutputter = new XMLOutputter(null, null);
 assertNull(xmlOutputter.getFormat());
 assertNull(xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 xmlOutputter = new XMLOutputter(format);
 assertNotNull(xmlOutputter.getFormat());
 assertNull(xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 xmlOutputter = new XMLOutputter(null, new XMLOutputProcessor());
 assertNull(xmlOutputter.getFormat());
 assertNotNull(xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 xmlOutputter = new XMLOutputter(null, null);
 assertNull(xmlOutputter.getFormat());
 xmlOutputter.setFormat(format);
 assertSame(format, xmlOutputter.getFormat());
}

 @Test
 public void testGetFormat() {
 xmlOutputter = new XMLOutputter(format);
 assertSame(format, xmlOutputter.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 xmlOutputter = new XMLOutputter(null, null);
 assertNull(xmlOutputter.getXMLOutputProcessor());
 xmlOutputter.setXMLOutputProcessor(new XMLOutputProcessor());
 assertNotNull(xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 xmlOutputter = new XMLOutputter(null, new XMLOutputProcessor());
 assertSame(xmlOutputter.getXMLOutputProcessor(), xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new Document();
 doc.setRootElement(new Element("root"));
 Element root = doc.getRootElement();
 root.addContent(new Element("child", "value"));
 xmlOutputter.output(doc, writer);
 String expectedXml = "<root><child>value</child></root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = new DocType();
 doctype.setName("doctype");
 doctype.setPublicId("-//W3C//DTD XHTML 1.0 Strict//EN");
 doctype.setSystemId("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
 xmlOutputter.output(doctype, writer);
 String expectedXml = "<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("element");
 xmlOutputter.output(element, writer);
 String expectedXml = "<element/>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputElementWithContent() throws IOException {
 Element element = new Element("element");
 Text text = new Text("content");
 element.addContent(text);
 xmlOutputter.output(element, writer);
 String expectedXml = "<element>content</element>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputListOfContents() throws IOException {
 List<Element> list = new ArrayList<>();
 Element element1 = new Element("element1");
 Text text1 = new Text("content1");
 element1.addContent(text1);
 list.add(element1);
 Element element2 = new Element("element2");
 Text text2 = new Text("content2");
 element2.addContent(text2);
 list.add(element2);
 xmlOutputter.output(list, writer);
 String expectedXml = "<element1><text>content1</text></element1><element2><text>content2</text></element2>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputCDATASource() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[content]]>");
 xmlOutputter.output(cdata, writer);
 String expectedXml = "<![CDATA[content]]>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("text");
 xmlOutputter.output(text, writer);
 String expectedXml = "text";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("comment");
 xmlOutputter.output(comment, writer);
 String expectedXml = "<!--comment-->";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("processing-instruction", "data");
 xmlOutputter.output(pi, writer);
 String expectedXml = "<?processing-instruction data?>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity");
 xmlOutputter.output(entityRef, writer);
 String expectedXml = "&entity;";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("element");
 Text text = new Text("content");
 element.addContent(text);
 xmlOutputter.outputElementContent(element, writer);
 String expectedXml = "content";
 assertEquals(expectedXml, writer.toString());
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws IOException {
 xmlOutputter.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws IOException {
 xmlOutputter.output(new Document(), null);
 }

 @Test(expected = IOException.class)
 public void testOutputNullWriter() throws IOException {
 xmlOutputter.output(new Document(), new StringWriter());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullContent() throws IOException {
 xmlOutputter.output(null, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullFormat() {
 xmlOutputter = new XMLOutputter(null, null);
 assertNull(xmlOutputter.getFormat());
 try {
 xmlOutputter.output(new Document(), writer);
 } catch (IOException e) {
}
 xmlOutputter.setFormat(format);
}

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullXMLOutputProcessor() {
 xmlOutputter = new XMLOutputter(format, null);
 assertNotNull(xmlOutputter.getXMLOutputProcessor());
 try {
 xmlOutputter.output(new Document(), writer);
 } catch (IOException e) {
}
 xmlOutputter.setXMLOutputProcessor(new XMLOutputProcessor());
}

 @Test
 public void testEscapeAttributeEntities() {
 String str = "&<>";
 String expected = "&amp;&lt;&gt;";
 assertEquals(expected, XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(str, format));
 }

 @Test
 public void testEscapeElementEntities() {
 String str = "&<>";
 String expected = "&amp;<>&gt;";
 assertEquals(expected, XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(str, format));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 XMLOutputter original = new XMLOutputter(format, null);
 XMLOutputter cloned = original.clone();
 assertNotNull(cloned);
 assertNotSame(original, cloned);
 assertEquals(original.getFormat(), cloned.getFormat());
 assertEquals(original.getXMLOutputProcessor(), cloned.getXMLOutputProcessor());
 }

 @After
 public void tearDown() {
 try {
 writer.close();
 outputStream.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
}