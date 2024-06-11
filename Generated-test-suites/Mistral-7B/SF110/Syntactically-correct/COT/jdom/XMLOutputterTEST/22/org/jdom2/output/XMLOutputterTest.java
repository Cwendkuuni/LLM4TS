package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private final static Format FORMAT = new Format();
 private final static XMLOutputter OUTPUTTER = new XMLOutputter(FORMAT);

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter outputter = new XMLOutputter(FORMAT);
 assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter outputter = new XMLOutputter(FORMAT, null);
 assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter outputter = new XMLOutputter(null, null);
 assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithNullArguments() {
 XMLOutputter outputter = new XMLOutputter();
 assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithThat() {
 XMLOutputter that = new XMLOutputter(null, null);
 XMLOutputter outputter = new XMLOutputter(that);
 assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessorThat() {
 XMLOutputter that = new XMLOutputter(FORMAT, null);
 XMLOutputter outputter = new XMLOutputter(FORMAT, that);
 assertNotNull(outputter);
 }

 @Test
 public void testSetFormat() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 Format newFormat = new Format();
 outputter.setFormat(newFormat);
 assertEquals(newFormat, outputter.getFormat());
 }

 @Test
 public void testGetFormat() {
 XMLOutputter outputter = new XMLOutputter();
 Format format = outputter.getFormat();
 assertNotNull(format);
 }

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputter outputter = new XMLOutputter();
 XMLOutputProcessor processor = outputter.getXMLOutputProcessor();
 assertNull(processor);
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter outputter = new XMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 outputter.setXMLOutputProcessor(processor);
 assertEquals(processor, outputter.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new Document();
 doc.setRootElement(new Element("root"));
 doc.getRootElement().addContent(new Text("test"));
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(doc, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<root><text>test</text></root>";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocType();
 docType.setName("doctype");
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(docType, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<!DOCTYPE doctype []>\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("element");
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(element, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<element/>\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputElementWithTextChild() throws IOException {
 Element element = new Element("element");
 Text text = new Text("test");
 element.addContent(text);
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(element, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<element>test</element>\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Content> list = new ArrayList<>();
 list.add(new Text("test1"));
 list.add(new Text("test2"));
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(list, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<test1/><test2/>\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[test]]>");
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(cdata, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<![CDATA[test]]>\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("test");
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(text, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "test\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("test");
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(comment, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<!--test-->\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("target", "data");
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(pi, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "<xml-pi:target data=\"data\"/>\n";
 assertEquals(expected, result);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity");
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(entityRef, outputStream);
 String result = new String(outputStream.toByteArray());
 String expected = "&entity;\n";
 assertEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(null, outputStream);
 fail("IOException expected");
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws IOException {
 Document doc = new Document();
 OUTPUTTER.output(doc, null);
 fail("IOException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullContent() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 OUTPUTTER.output(null, outputStream);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullFormat() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 try {
 outputter.output(new Document(), null);
 }
 catch (IllegalArgumentException e) {
 }
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullProcessor() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 try {
 outputter.output(new Document(), outputStream, null);
 }
 catch (IllegalArgumentException e) {
 }
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFormatNullFormat() {
 XMLOutputter outputter = new XMLOutputter();
 try {
 outputter.setFormat(null);
 }
 catch (IllegalArgumentException e) {
 }
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetProcessorNullProcessor() {
 XMLOutputter outputter = new XMLOutputter();
 try {
 outputter.setXMLOutputProcessor(null);
 }
 catch (IllegalArgumentException e) {
 }
 fail("IllegalArgumentException expected");
 }
}