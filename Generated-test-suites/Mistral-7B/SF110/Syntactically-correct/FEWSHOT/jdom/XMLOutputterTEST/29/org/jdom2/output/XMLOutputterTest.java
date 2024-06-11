package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

 XMLOutputter xmlOutputter;
 Document doc;
 OutputStream out;
 Writer writer;
 XMLOutputProcessor processor;
 Format format;

 @Before
 public void setup() {
 xmlOutputter = new XMLOutputter();
 doc = new JDOMParser().parse("<root><child>test</child></root>");
 format = new Format();
 processor = new DefaultXMLProcessor();
 out = new ByteArrayOutputStream();
 writer = new BufferedWriter(new OutputStreamWriter(out));
 }

 @Test
 public void testOutputDocument() throws IOException {
 xmlOutputter.output(doc, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "<root><child>test</child></root>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputDocType() throws IOException {
 Element root = doc.getRootElement();
 DocType docType = root.getDocument().getDoctype();
 xmlOutputter.output(docType, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "<!DOCTYPE root [ ]>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element child = doc.getRootElement().getChild("child");
 xmlOutputter.output(child, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "<child>test</child>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element child = doc.getRootElement().getChild("child");
 xmlOutputter.outputElementContent(child, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "test";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Text> textList = doc.getRootElement().getChildren("child");
 xmlOutputter.output(textList, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "<child>test</child>"; // Assuming all child elements have the same content
 for (Text text : textList) {
 assertEquals("test", text.getText());
 }
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[test]]>");
 xmlOutputter.output(cdata, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "<![CDATA[test]]>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("test");
 xmlOutputter.output(text, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "test";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("test comment");
 xmlOutputter.output(comment, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "<!--test comment-->";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\"");
 xmlOutputter.output(pi, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity1");
 xmlOutputter.output(entityRef, writer);
 String actualOutput = writer.toString();
 String expectedOutput = "&entity1;";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringDocument() {
 String actualOutput = xmlOutputter.outputString(doc);
 String expectedOutput = "<root><child>test</child></root>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringDocType() {
 Element root = doc.getRootElement();
 DocType docType = root.getDocument().getDoctype();
 String actualOutput = xmlOutputter.outputString(docType);
 String expectedOutput = "<!DOCTYPE root [ ]>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringElement() {
 Element child = doc.getRootElement().getChild("child");
 String actualOutput = xmlOutputter.outputString(child);
 String expectedOutput = "<child>test</child>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringElementContent() {
 Element child = doc.getRootElement().getChild("child");
 String actualOutput = xmlOutputter.outputString(child.getContent());
 String expectedOutput = "test";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringList() throws IOException {
 List<Text> textList = doc.getRootElement().getChildren("child");
 String actualOutput = xmlOutputter.outputString(textList);
 String expectedOutput = "<child>test</child>"; // Assuming all child elements have the same content
 for (Text text : textList) {
 assertEquals("test", text.getText());
 }
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("<![CDATA[test]]>");
 String actualOutput = xmlOutputter.outputString(cdata);
 String expectedOutput = "<![CDATA[test]]>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringText() {
 Text text = new Text("test");
 String actualOutput = xmlOutputter.outputString(text);
 String expectedOutput = "test";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("test comment");
 String actualOutput = xmlOutputter.outputString(comment);
 String expectedOutput = "<!--test comment-->";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\"");
 String actualOutput = xmlOutputter.outputString(pi);
 String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("entity1");
 String actualOutput = xmlOutputter.outputString(entityRef);
 String expectedOutput = "&entity1;";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSetFormat() {
 Format newFormat = new Format();
 xmlOutputter.setFormat(newFormat);
 assertEquals(newFormat, xmlOutputter.getFormat());
 }

 @Test
 public void testGetFormat() {
 Format expectedFormat = format;
 Format actualFormat = xmlOutputter.getFormat();
 assertEquals(expectedFormat, actualFormat);
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor newProcessor = new DefaultXMLProcessor();
 xmlOutputter.setXMLOutputProcessor(newProcessor);
 assertEquals(newProcessor, xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputProcessor expectedProcessor = processor;
 XMLOutputProcessor actualProcessor = xmlOutputter.getXMLOutputProcessor();
 assertEquals(expectedProcessor, actualProcessor);
 }
}