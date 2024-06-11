package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Format format;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 xmlo = new XMLOutputter();
 format = new Format();
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testXMLOutputterWithFormatAndProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo = new XMLOutputter(format, processor);
 assertNotNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterWithoutFormatAndProcessor() {
 xmlo = new XMLOutputter();
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterWithFormatOnly() {
 xmlo = new XMLOutputter(format);
 assertNotNull(xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterWithProcessorOnly() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo = new XMLOutputter(processor);
 assertNotNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 xmlo = new XMLOutputter();
 Format newFormat = new Format();
 xmlo.setFormat(newFormat);
 assertEquals(newFormat, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor newProcessor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(newProcessor);
 assertEquals(newProcessor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = JDOMFactory.createRootElement("Root");
 xmlo.output(doc, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<Root/>\n", outputStream.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocType("testDocType", new Element[]{}, new Attribute[]{}, new String[]{"public", "test.dtd"});
 xmlo.output(docType, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<!DOCTYPE testDocType []\n", outputStream.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = JDOMFactory.createRootElement("Root").addContent(JDOMFactory.createElement("Child"));
 xmlo.output(element, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<Root>\n <Child/>\n</Root>\n", outputStream.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = JDOMFactory.createRootElement("Root").addContent(JDOMFactory.createText("Hello World"));
 xmlo.outputElementContent(element, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<Root>\nHello World\n</Root>\n", outputStream.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 List<Content> list = new ArrayList<>();
 list.add(JDOMFactory.createText("Text 1"));
 list.add(JDOMFactory.createText("Text 2"));
 xmlo.output(list, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n< Root>\n Text 1\nText 2\n</Root>\n", outputStream.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("Hello World");
 xmlo.output(cdata, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<![CDATA[Hello World]]>\n", outputStream.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = JDOMFactory.createText("Hello World");
 xmlo.output(text, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<Root>\n<text xml:space='preserve'>Hello World</text>\n</Root>\n", outputStream.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = JDOMFactory.createComment("This is a comment");
 xmlo.output(comment, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<Root>\n<!--This is a comment-->\n</Root>\n", outputStream.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-data", "data");
 xmlo.output(pi, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<Root>\n<?xml-data data?>\n</Root>\n", outputStream.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity1");
 xmlo.output(entityRef, outputStream);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<Root>\n&entity1;\n</Root>\n", outputStream.toString());
 }

 @Test
 public void testOutputStringDocument() {
 String xmlString = xmlo.outputString(JDOMFactory.createRootElement("Root"));
 assertEquals("<Root/>\n", xmlString);
 }

 @Test
 public void testOutputStringDocType() {
 String xmlString = xmlo.outputString(new DocType("testDocType", new Element[]{}, new Attribute[]{}, new String[]{"public", "test.dtd"}));
 assertEquals("<!DOCTYPE testDocType []\n", xmlString);
 }

 @Test
 public void testOutputStringElement() {
 String xmlString = xmlo.outputString(JDOMFactory.createRootElement("Root").addContent(JDOMFactory.createElement("Child")));
 assertEquals("<Root>\n <Child/>\n</Root>\n", xmlString);
 }

 @Test
 public void testOutputStringElementContent() {
 String xmlString = xmlo.outputString(JDOMFactory.createRootElement("Root").addContent(JDOMFactory.createText("Hello World")));
 assertEquals("<Root>\nHello World\n</Root>\n", xmlString);
 }

 @Test
 public void testOutputStringList() {
 List<Content> list = new ArrayList<>();
 list.add(JDOMFactory.createText("Text 1"));
 list.add(JDOMFactory.createText("Text 2"));
 String xmlString = xmlo.outputString(list);
 assertEquals("< Root>\n Text 1\nText 2\n</Root>\n", xmlString);
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("Hello World");
 String xmlString = xmlo.outputString(cdata);
 assertEquals("<![CDATA[Hello World]]>\n", xmlString);
 }

 @Test
 public void testOutputStringText() {
 Text text = JDOMFactory.createText("Hello World");
 String xmlString = xmlo.outputString(text);
 assertEquals("<Root>\n<text xml:space='preserve'>Hello World</text>\n</Root>\n", xmlString);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = JDOMFactory.createComment("This is a comment");
 String xmlString = xmlo.outputString(comment);
 assertEquals("<Root>\n<!--This is a comment-->\n</Root>\n", xmlString);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml-data", "data");
 String xmlString = xmlo.outputString(pi);
 assertEquals("<Root>\n<?xml-data data?>\n</Root>\n", xmlString);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("entity1");
 String xmlString = xmlo.outputString(entityRef);
 assertEquals("<Root>\n&entity1;\n</Root>\n", xmlString);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String escapedString = xmlo.escapeAttributeEntities("&< >'\" test=\"1 2 3\" xml:space='preserve'");
 assertEquals("&amp;&lt;&gt;&apos;&quot; test=&quot;1&nbsp;2&nbsp;3&quot; xml:space=&quot;preserve&quot;", escapedString);
 }

 @Test
 public void testEscapeElementEntities() {
 String escapedString = xmlo.escapeElementEntities("&< >'\" test=\"1 2 3\" xml:space='preserve'");
 assertEquals("&amp;&lt;&gt;&apos;&quot; test=&quot;1&nbsp;2&nbsp;3&quot;", escapedString);
 }

 @Test
 public void testClone() {
 XMLOutputter original = new XMLOutputter();
 XMLOutputter cloned = original.clone();
 assertNotEquals(original, cloned);
 }

}