package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new Format(), new XMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithFormatOnly() {
 XMLOutputter xmlo = new XMLOutputter(new Format());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessorOnly() {
 XMLOutputter xmlo = new XMLOutputter(new XMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithAnotherXMLOutputter() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(xmlo1);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndNullProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new Format(), null);
 }

 @Test
 public void testXMLOutputterConstructorWithNullFormatAndNullProcessor() {
 XMLOutputter xmlo = new XMLOutputter(null, null);
 }

 @Test
 public void testXMLOutputterSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 Assert.assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterGetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 Assert.assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 Assert.assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterGetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 Assert.assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new Document();
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(doc, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputDocType() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 DocType doctype = new DocType();
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(doctype, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputElement() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("test");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(element, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("test");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.outputElementContent(element, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputList() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 List<Content> list = new ArrayList<>();
 list.add(new Text("text1"));
 list.add(new Text("text2"));
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(list, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 CDATA cdata = new CDATA("test");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(cdata, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputText() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Text text = new Text("test");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(text, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputComment() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Comment comment = new Comment("test");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(comment, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version='1.0'");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(pi, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entity = new EntityRef("#test");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(entity, out);
 String result = new String((byte[]) out.toByteArray());
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringDocument() {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new Document();
 String result = xmlo.outputString(doc);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringDocType() {
 XMLOutputter xmlo = createXMLOutputter();
 DocType doctype = new DocType();
 String result = xmlo.outputString(doctype);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringElement() {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("test");
 String result = xmlo.outputString(element);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringList() {
 XMLOutputter xmlo = createXMLOutputter();
 List<Content> list = new ArrayList<>();
 list.add(new Text("text1"));
 list.add(new Text("text2"));
 String result = xmlo.outputString(list);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringCDATA() {
 XMLOutputter xmlo = createXMLOutputter();
 CDATA cdata = new CDATA("test");
 String result = xmlo.outputString(cdata);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringText() {
 XMLOutputter xmlo = createXMLOutputter();
 Text text = new Text("test");
 String result = xmlo.outputString(text);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringComment() {
 XMLOutputter xmlo = createXMLOutputter();
 Comment comment = new Comment("test");
 String result = xmlo.outputString(comment);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 XMLOutputter xmlo = createXMLOutputter();
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version='1.0'");
 String result = xmlo.outputString(pi);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputStringEntityRef() {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entity = new EntityRef("#test");
 String result = xmlo.outputString(entity);
 Assert.assertNotNull(result);
 }

 @Test
 public void testOutputElementContentString() {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("test");
 String result = xmlo.outputElementContentString(element);
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(null, out);
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new Document();
 xmlo.output(doc, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullContent() {
 XMLOutputter xmlo = createXMLOutputter();
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(null, out);
 }

}