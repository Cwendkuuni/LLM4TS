package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 @Test
 public void testXMLOutputterConstructionWithFormatAndProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new Format(), new XMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructionWithFormatOnly() {
 XMLOutputter xmlo = new XMLOutputter(new Format());
 }

 @Test
 public void testXMLOutputterConstructionWithProcessorOnly() {
 XMLOutputter xmlo = new XMLOutputter(new XMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructionWithThat() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(xmlo1);
 }

 @Test
 public void testXMLOutputterConstructionWithFormatAndNullProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new Format(), null);
 Assert.assertNotNull(xmlo.getXMLOutputProcessor());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXMLOutputterConstructionWithNullFormat() {
 XMLOutputter xmlo = new XMLOutputter(null, new XMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 Assert.assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 Assert.assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new JDOMFactory().newDocument("root", new Element("root"));
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(doc, out);
 String expected = "<root></root>";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocumentType();
 docType.setName("test");
 docType.setPublic("-//test//DTD Test//EN");
 docType.setSystem("http://test.com/test.dtd");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(docType, out);
 String expected = "<!DOCTYPE test PUBLIC \"-//test//DTD Test//EN\" \"http://test.com/test.dtd\">";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("root");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(element, out);
 String expected = "<root></root>";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("root");
 element.addContent(new Text("Hello World"));
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.outputElementContent(element, out);
 String expected = "Hello World";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Content> list = new ArrayList<Content>();
 list.add(new Text("Text1"));
 list.add(new Text("Text2"));
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(list, out);
 String expected = "Text1Text2";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(cdata, out);
 String expected = "<![CDATA[Hello World]]>";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Hello World");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(text, out);
 String expected = "Hello World";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("Hello World");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(comment, out);
 String expected = "<!--Hello World-->";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(pi, out);
 String expected = "<?xml version=\"1.0\"?>";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("#comment");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Comment("Comment"));
 xmlo.output(root, out);
 xmlo.output(entityRef, out);
 String expected = "<root><!--Comment--></root>&#x23;comment;";
 String actual = new String(out.toByteArray());
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws IOException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(null, out);
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws IOException {
 Document doc = new JDOMFactory().newDocument("root", new Element("root"));
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(doc, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullContent() throws IOException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(null, out);
 }

 @Test
 public void testOutputStringDocument() {
 Document doc = new JDOMFactory().newDocument("root", new Element("root"));
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(doc, out);
 String expected = "<root></root>";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringDocType() {
 DocType docType = new DocumentType();
 docType.setName("test");
 docType.setPublic("-//test//DTD Test//EN");
 docType.setSystem("http://test.com/test.dtd");
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(docType, out);
 String expected = "<!DOCTYPE test PUBLIC \"-//test//DTD Test//EN\" \"http://test.com/test.dtd\">";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringElement() {
 Element element = new Element("root");
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(element, out);
 String expected = "<root></root>";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringElementContent() {
 Element element = new Element("root");
 element.addContent(new Text("Hello World"));
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.outputElementContent(element, out);
 String expected = "Hello World";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringList() {
 List<Content> list = new ArrayList<Content>();
 list.add(new Text("Text1"));
 list.add(new Text("Text2"));
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(list, out);
 String expected = "Text1Text2";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(cdata, out);
 String expected = "<![CDATA[Hello World]]>";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringText() throws IOException {
 Text text = new Text("Hello World");
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(text, out);
 String expected = "Hello World";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringComment() throws IOException {
 Comment comment = new Comment("Hello World");
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(comment, out);
 String expected = "<!--Hello World-->";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(pi, out);
 String expected = "<?xml version=\"1.0\"?>";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("#comment");
 Element root = new Element("root");
 root.addContent(new Comment("Comment"));
 StringWriter out = new StringWriter();
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(root, out);
 xmlo.output(entityRef, out);
 String expected = "<root><!--Comment--></root>&#x23;comment;";
 String actual = out.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String str = "<test attr=\"<tag>&foo&bar&amp;&quot; />";
 String escaped = XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(str, null);
 String expected = "<test attr=\"&lt;tag&gt;&amp;amp;&amp;quot;&#x26;amp;&#x26;quot; />";
 Assert.assertEquals(expected, escaped);
 }

 @Test
 public void testEscapeElementEntities() {
 String str = "<test><foo>&bar;</foo></test>";
 String escaped = XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(str, null);
 String expected = "<test><foo>&amp;bar;</foo></test>";
 Assert.assertEquals(expected, escaped);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = xmlo1.clone();
 Assert.assertEquals(xmlo1, xmlo2);
 }
}