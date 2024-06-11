package org.jdom2.output;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.OutputFormat;
import org.jdom2.processor.DefaultXMLProcessor;
import org.jdom2.processor.XMLOutputProcessor;
import org.jdom2.test.JDOMTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private OutputStream outputStream;
 private Writer writer;
 private Document document;
 private Element element;
 private XMLOutputter xmlOutputter;

 @Before
 public void setUp() throws IOException, JDOMException {
 outputStream = new MemoryOutputStream();
 writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
 document = new JDOMTest().createDocument();
 element = document.getRootElement().getChild("test");
 xmlOutputter = new XMLOutputter();
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlOutputterWithFormatAndProcessor = new XMLOutputter(new OutputFormat(), new XMLOutputProcessor());
 assertEquals(new OutputFormat(), xmlOutputterWithFormatAndProcessor.getFormat());
 assertNull(xmlOutputterWithFormatAndProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithNullFormatAndNullProcessor() {
 XMLOutputter xmlOutputterWithNullFormatAndNullProcessor = new XMLOutputter();
 assertNull(xmlOutputterWithNullFormatAndNullProcessor.getFormat());
 assertNull(xmlOutputterWithNullFormatAndNullProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlOutputterWithFormat = new XMLOutputter(new OutputFormat());
 assertEquals(new OutputFormat(), xmlOutputterWithFormat.getFormat());
 assertNull(xmlOutputterWithFormat.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlOutputterWithProcessor = new XMLOutputter(new XMLOutputProcessor());
 assertNull(xmlOutputterWithProcessor.getFormat());
 assertEquals(new XMLOutputProcessor(), xmlOutputterWithProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterSetFormat() {
 OutputFormat format = new OutputFormat();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.setFormat(format);
 assertEquals(format, xmlOutputter.getFormat());
 }

 @Test
 public void testXMLOutputterSetXMLOutputProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 xmlOutputter.output(document, writer);
 String expectedOutput = "<document>\n<test>\n</test>\n</document>";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 xmlOutputter.output(document.getDoctype(), writer);
 String expectedOutput = "<!DOCTYPE document [ ]>";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 xmlOutputter.output(element, writer);
 String expectedOutput = "<test/>";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 xmlOutputter.outputElementContent(element, writer);
 String expectedOutput = "<test/>";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 List<Content> elementsList = new JDOMTest().getTestList();
 xmlOutputter.output(elementsList, writer);
 String expectedOutput = "<test>\n<element1/>\n<element2/>\n<element3/>\n</test>";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[test]]>");
 xmlOutputter.output(cdata, writer);
 String expectedOutput = "<![CDATA[test]]>";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("test");
 xmlOutputter.output(text, writer);
 String expectedOutput = "test";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("test comment");
 xmlOutputter.output(comment, writer);
 String expectedOutput = "<!--test comment-->";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "test");
 xmlOutputter.output(pi, writer);
 String expectedOutput = "<xml PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<!-- test -->";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entityRef");
 xmlOutputter.output(entityRef, writer);
 String expectedOutput = "&entityRef;";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 xmlOutputter.outputElementContentString(element);
 String expectedOutput = "<test/>";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test(expected = IOException.class)
 public void testOutputWithNullOutputStream() {
 xmlOutputter.output(document, null);
 }

 @Test(expected = IOException.class)
 public void testOutputWithNullDocument() {
 xmlOutputter.output(null, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputWithNullWriter() {
 xmlOutputter.output(document, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputWithNullFormat() {
 XMLOutputter xmlOutputterWithNullFormat = new XMLOutputter();
 xmlOutputterWithNullFormat.setFormat(null);
 try {
 xmlOutputterWithNullFormat.output(document, writer);
 } catch (IOException e) {
 fail("IOException expected but got: " + e.getMessage());
 }
 assertEquals("Format cannot be null", e.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputWithNullProcessor() {
 XMLOutputter xmlOutputterWithNullProcessor = new XMLOutputter();
 xmlOutputterWithNullProcessor.setXMLOutputProcessor(null);
 try {
 xmlOutputterWithNullProcessor.output(document, writer);
 } catch (IOException e) {
 fail("IOException expected but got: " + e.getMessage());
 }
 assertEquals("XMLOutputProcessor cannot be null", e.getMessage());
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "<test attribute=\"<tag>test</tag>\" />";
 String expectedOutput = "<test attribute=\"&lt;tag&gt;test&lt;/tag&gt;\"/>";
 assertEquals(expectedOutput, XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(input, new OutputFormat()));
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "<test><tag>&lt;tag&gt;</tag></test>";
 String expectedOutput = "<test><tag>&amp;lt;tag&gt;</tag></test>";
 assertEquals(expectedOutput, XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(input, new OutputFormat()));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testMakeWriterWithInvalidEncoding() throws UnsupportedEncodingException {
 XMLOutputter.makeWriter(new ByteArrayOutputStream(), null);
 }
}