package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 XMLOutputter xmlo = null;
 Format format = null;
 XMLOutputProcessor processor = null;

 @Before
 public void setUp() {
 xmlo = new XMLOutputter();
 format = new Format();
 processor = new DefaultXMLProcessor();
 xmlo.setXMLOutputProcessor(processor);
 }

 @Test
 public void testXMLOutputterConstructor() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(format);
 XMLOutputter xmlo3 = new XMLOutputter(processor);
 XMLOutputter xmlo4 = new XMLOutputter(format, processor);
 }

 @Test
 public void testXMLOutputterClone() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = xmlo1.clone();
 assertNotSame(xmlo1, xmlo2);
 assertSame(xmlo1.getFormat(), xmlo2.getFormat());
 assertSame(xmlo1.getXMLOutputProcessor(), xmlo2.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new Document();
 doc.setRootElement(new Element("root"));
 Element root = doc.getRootElement();
 root.addContent(new Element("child"));
 xmlo.output(doc, new StringWriter());
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = new DocType("root", new QualifiedName("http://www.w3.org/1999/xhtml", "html"), null);
 xmlo.output(doctype, new StringWriter());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("element");
 xmlo.output(element, new StringWriter());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("element");
 element.setText("content");
 xmlo.outputElementContent(element, new StringWriter());
 }

 @Test
 public void testOutputList() throws IOException {
 Element element = new Element("element");
 List<Content> list = new ArrayList<>();
 list.add(new Text("item1"));
 list.add(new Text("item2"));
 xmlo.output(list, new StringWriter());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[content]]>");
 xmlo.output(cdata, new StringWriter());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("text");
 xmlo.output(text, new StringWriter());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("comment");
 xmlo.output(comment, new StringWriter());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
 xmlo.output(pi, new StringWriter());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("#external-xml-decl-1");
 xmlo.output(entityRef, new StringWriter());
 }

 @Test
 public void testOutputStringDocument() {
 String xml = xmlo.outputString(new Document());
 assertTrue(xml.contains("<root>"));
 }

 @Test
 public void testOutputStringDocType() {
 String xml = xmlo.outputString(new DocType("root", new QualifiedName("http://www.w3.org/1999/xhtml", "html"), null));
 assertTrue(xml.contains("<!DOCTYPE root PUBLIC \"http://www.w3.org/1999/xhtml html"));
 }

 @Test
 public void testOutputStringElement() {
 String xml = xmlo.outputString(new Element("element"));
 assertTrue(xml.contains("<element/>"));
 }

 @Test
 public void testOutputStringElementWithContent() {
 String xml = xmlo.outputString(new Element("element").setText("content"));
 assertTrue(xml.contains("<element>content</element>"));
 }

 @Test
 public void testOutputStringList() {
 String xml = xmlo.outputString(new ArrayList<>());
 assertTrue(xml.isEmpty());
 String xmlList = xmlo.outputString(Arrays.asList("item1", "item2"));
 assertTrue(xmlList.contains("<item1/>"));
 assertTrue(xmlList.contains("<item2/>"));
 }

 @Test
 public void testOutputStringCDATA() {
 String xml = xmlo.outputString("<![CDATA[content]]>");
 assertTrue(xml.contains("<![CDATA[content]]>"));
 }

 @Test
 public void testOutputStringText() {
 String xml = xmlo.outputString("text");
 assertTrue(xml.contains("text"));
 }

 @Test
 public void testOutputStringComment() {
 String xml = xmlo.outputString(new Comment("comment"));
 assertTrue(xml.contains("<!--comment-->"));
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 String xml = xmlo.outputString(new ProcessingInstruction("xml", " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"));
 assertTrue(xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
 }

 @Test
 public void testOutputStringEntityRef() {
 String xml = xmlo.outputString(new EntityRef("#external-xml-decl-1"));
 assertTrue(xml.contains("&external-xml-decl-1;"));
 }

 @Test
 public void testOutputStringElementContent() {
 String xml = xmlo.outputString(new Element("element").setText("content"));
 assertTrue(xml.contains("<element>content</element>"));
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "&amp;<>&apos;\"";
 String expected = "&amp;amp; &lt; &gt; &apos; &quot;";
 String output = xmlo.escapeAttributeEntities(input, format);
 assertEquals(expected, output);
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "&amp;<>&apos;\"";
 String expected = "&amp;lt;lt;&gt;gt;&apos;&apos;";
 String output = xmlo.escapeElementEntities(input, format);
 assertEquals(expected, output);
 }

}