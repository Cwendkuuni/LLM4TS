import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 @Test
 public void testXMLOutputterConstructor() {
 XMLOutputter xmlo = new XMLOutputter();
 Assert.assertNull(xmlo.getFormat());
 Assert.assertNull(xmlo.getXMLOutputProcessor());

 XMLOutputter xmloFormat = new XMLOutputter(new Format());
 Assert.assertNotNull(xmloFormat.getFormat());
 Assert.assertNull(xmloFormat.getXMLOutputProcessor());

 XMLOutputter xmloProcessor = new XMLOutputter(new XMLOutputProcessor());
 Assert.assertNull(xmloProcessor.getFormat());
 Assert.assertNotNull(xmloProcessor.getXMLOutputProcessor());

 XMLOutputter xmloFormatAndProcessor = new XMLOutputter(new Format(), new XMLOutputProcessor());
 Assert.assertNotNull(xmloFormatAndProcessor.getFormat());
 Assert.assertNotNull(xmloFormatAndProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterSetters() {
 XMLOutputter xmlo = new XMLOutputter();

 Format format = new Format();
 XMLOutputProcessor processor = new XMLOutputProcessor();

 xmlo.setFormat(format);
 Assert.assertEquals(format, xmlo.getFormat());

 xmlo.setXMLOutputProcessor(processor);
 Assert.assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(doc, outStream);

 String expectedXml = "<root/>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputDocType() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 DocType docType = new DocType("root", new QualifiedName("root-element"), null);

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(docType, outStream);

 String expectedXml = "<!DOCTYPE root [ ]>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElement() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 root.addContent(jdomHelper.createElement("child", "value"));

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(root, outStream);

 String expectedXml = "<root><child>value</child></root>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputString() {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 root.addContent(jdomHelper.createElement("child", "value"));

 StringWriter writer = new StringWriter();
 xmlo.output(root, writer);

 String expectedXml = "<root><child>value</child></root>";
 String actualXml = writer.toString();
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputList() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 root.addContent(jdomHelper.createElement("child1", "value1"));
 root.addContent(jdomHelper.createElement("child2", "value2"));

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(root.getContent(), outStream);

 String expectedXml = "<root><child1>value1</child1><child2>value2</child2></root>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 root.addContent(jdomHelper.createCDATA("value"));

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(root.getContent().get(0), outStream);

 String expectedXml = "<![CDATA[value]]>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputText() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 Text text = doc.getText();
 text.setValue("value");

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(text, outStream);

 String expectedXml = "<root>value</root>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputComment() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 Comment comment = new Comment("comment");

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(comment, outStream);

 String expectedXml = "<!--comment-->";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(pi, outStream);

 String expectedXml = "<?xml version=\"1.0\"?>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 EntityRef entityRef = new EntityRef("entity");

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(entityRef, outStream);

 String expectedXml = "<!ENTITY entity SYSTEM \"entity.xml\">";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 JDOMHelper jdomHelper = new JDOMHelper();
 Document doc = jdomHelper.createDocument();
 Element root = doc.getRootElement();
 root.setName("root");
 root.addContent(jdomHelper.createElement("child", "value"));

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.outputElementContent(root, outStream);

 String expectedXml = "<child>value</child>";
 String actualXml = new String(outStream.toByteArray());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "<root attr=\"<tag>&<script>script&script/><script>script\" />";
 String expectedOutput = "<root attr=\"&lt;tag&gt;&amp;script&amp;script;&#x2F;script&gt;script\" />";
 String output = new XMLOutputter().escapeAttributeEntities(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "<root>&lt;tag&gt;&amp;script&amp;script/&gt;</root>";
 String expectedOutput = "<root>&lt;tag&gt;&lt;tag&gt;&amp;script&amp;script;&#x2F;script&gt;</root>";
 String output = new XMLOutputter().escapeElementEntities(input, new Format());
 Assert.assertEquals(expectedOutput, output);
 }
}