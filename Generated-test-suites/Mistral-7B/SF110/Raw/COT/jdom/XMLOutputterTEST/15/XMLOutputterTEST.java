package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private XMLOutputter xmlOutputter;
 private Document doc;
 private StringBuilder expectedString;

 @Before
 public void setUp() {
 xmlOutputter = new XMLOutputter();
 doc = new JDOMFactory().newDocument();
 expectedString = new StringBuilder();
 }

 // Test constructor with Format and XMLOutputProcessor
 @Test
 public void testXMLOutputterWithFormatAndProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 Format format = new Format();
 xmlOutputter = new XMLOutputter(format, processor);
 assertNotNull(xmlOutputter.getFormat());
 assertNotNull(xmlOutputter.getXMLOutputProcessor());
 }

 // Test constructor with only Format
 @Test
 public void testXMLOutputterWithFormat() {
 Format format = new Format();
 xmlOutputter = new XMLOutputter(format);
 assertNotNull(xmlOutputter.getFormat());
 assertNull(xmlOutputter.getXMLOutputProcessor());
 }

 // Test constructor with only XMLOutputProcessor
 @Test
 public void testXMLOutputterWithProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlOutputter = new XMLOutputter(processor);
 assertNull(xmlOutputter.getFormat());
 assertNotNull(xmlOutputter.getXMLOutputProcessor());
 }

 // Test constructor with XMLOutputter that
 @Test
 public void testXMLOutputterWithThat() {
 XMLOutputter that = new XMLOutputter();
 xmlOutputter = new XMLOutputter(that);
 assertNull(xmlOutputter.getFormat());
 assertNull(xmlOutputter.getXMLOutputProcessor());
 }

 // Test constructor with Format only
 @Test
 public void testXMLOutputterWithFormatOnly() {
 Format format = new Format();
 xmlOutputter = new XMLOutputter(format);
 assertNotNull(xmlOutputter.getFormat());
 assertNull(xmlOutputter.getXMLOutputProcessor());
 }

 // Test constructor with XMLOutputProcessor only
 @Test
 public void testXMLOutputterWithProcessorOnly() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlOutputter = new XMLOutputter(processor);
 assertNull(xmlOutputter.getFormat());
 assertNotNull(xmlOutputter.getXMLOutputProcessor());
 }

 // Test output Document to OutputStream
 @Test
 public void testOutputDocument() throws IOException {
 xmlOutputter.output(doc, new ByteArrayOutputStream());
 xmlOutputter.output(doc, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), doc.getRootElement().toString(xmlOutputter));
 }

 // Test output DocType to OutputStream
 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = doc.getRootElement().getDocument().getDoctype();
 xmlOutputter.output(docType, new ByteArrayOutputStream());
 xmlOutputter.output(docType, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), docType.toString(xmlOutputter));
 }

 // Test output Element to OutputStream
 @Test
 public void testOutputElement() throws IOException {
 Element element = doc.getRootElement().getChild("test");
 xmlOutputter.output(element, new ByteArrayOutputStream());
 xmlOutputter.output(element, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), element.toString(xmlOutputter));
 }

 // Test output Element content to OutputStream
 @Test
 public void testOutputElementContent() throws IOException {
 Element element = doc.getRootElement().getChild("test");
 xmlOutputter.outputElementContent(element, new ByteArrayOutputStream());
 xmlOutputter.outputElementContent(element, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), element.getContent().toString(xmlOutputter));
 }

 // Test output List of Content to OutputStream
 @Test
 public void testOutputListOfContent() throws IOException {
 List<Content> list = doc.getRootElement().getChildren();
 xmlOutputter.output(list, new ByteArrayOutputStream());
 xmlOutputter.output(list, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), list.toString(xmlOutputter));
 }

 // Test output CDATA to OutputStream
 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("Test CDATA");
 xmlOutputter.output(cdata, new ByteArrayOutputStream());
 xmlOutputter.output(cdata, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), cdata.toString(xmlOutputter));
 }

 // Test output Text to OutputStream
 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Test Text");
 xmlOutputter.output(text, new ByteArrayOutputStream());
 xmlOutputter.output(text, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), text.toString(xmlOutputter));
 }

 // Test output Comment to OutputStream
 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("Test Comment");
 xmlOutputter.output(comment, new ByteArrayOutputStream());
 xmlOutputter.output(comment, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), comment.toString(xmlOutputter));
 }

 // Test output ProcessingInstruction to OutputStream
 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 xmlOutputter.output(pi, new ByteArrayOutputStream());
 xmlOutputter.output(pi, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), pi.toString(xmlOutputter));
 }

 // Test output EntityRef to OutputStream
 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("testEntity");
 xmlOutputter.output(entityRef, new ByteArrayOutputStream());
 xmlOutputter.output(entityRef, new StringWriter(expectedString));
 assertEquals(expectedString.toString(), entityRef.toString(xmlOutputter));
 }

 // Test outputElementContentString
 @Test
 public void testOutputElementContentString() throws IOException {
 Element element = doc.getRootElement().getChild("test");
 xmlOutputter.outputElementContentString(element);
 assertEquals(expectedString.toString(), element.getContent().toString(xmlOutputter));
 }

 // Test escapeAttributeEntities
 @Test
 public void testEscapeAttributeEntities() {
 String str = "&<>'\"";
 String escapedStr = xmlOutputter.escapeAttributeEntities(str);
 assertEquals("&amp;&lt;&gt;&amp;quot;", escapedStr);
 }

 // Test escapeElementEntities
 @Test
 public void testEscapeElementEntities() {
 String str = "&<>'\"";
 String escapedStr = xmlOutputter.escapeElementEntities(str);
 assertEquals("&amp;&lt;&gt;&amp;quot;", escapedStr);
 }

 // Test clone method
 @Test
 public void testClone() {
 XMLOutputter that = new XMLOutputter();
 XMLOutputter cloned = that.clone();
 assertNotSame(that, cloned);
 }
}