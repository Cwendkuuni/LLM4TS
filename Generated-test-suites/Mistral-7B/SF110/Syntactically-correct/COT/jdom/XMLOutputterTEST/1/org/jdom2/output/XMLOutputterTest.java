package org.jdom2.output;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.OutputFormat;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.XMLOutputter;
import org.jdom2.output.support.DefaultXMLProcessor;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private OutputFormat format;
 private ByteArrayOutputStream outStream;

 @Before
 public void setUp() {
 xmlo = new XMLOutputter();
 format = new OutputFormat();
 outStream = new ByteArrayOutputStream();
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = new XMLOutputter(format, null);
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlo = new XMLOutputter(format);
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlo = new XMLOutputter(null, new DefaultXMLProcessor());
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterConstructorWithNoArguments() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterSetFormat() {
 OutputFormat format = new OutputFormat();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.setFormat(format);
 assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterGetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNull(xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.setXMLOutputProcessor(new DefaultXMLProcessor());
 assertNotNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterGetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException, JDOMException {
 Document doc = new Document();
 doc.setRootElement(new Element("root"));
 doc.getRootElement().addContent(new Element("child"));
 xmlo.output(doc, new OutputStreamWriter(outStream));
 String expected = "<root><child/></root>";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocType("test", null, null, null);
 xmlo.output(docType, new OutputStreamWriter(outStream));
 String expected = "<!DOCTYPE test [ ]>";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputElement() throws IOException, JDOMException {
 Element element = new Element("root");
 xmlo.output(element, new OutputStreamWriter(outStream));
 String expected = "<root/>";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException, JDOMException {
 Element element = new Element("root");
 element.setText("content");
 xmlo.outputElementContent(element, new OutputStreamWriter(outStream));
 String expected = "content";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputListOfContents() throws IOException, JDOMException {
 List<Content> list = new ArrayList<>();
 list.add(new Text("item1"));
 list.add(new Text("item2"));
 xmlo.output(list, new OutputStreamWriter(outStream));
 String expected = "item1item2";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("content");
 xmlo.output(cdata, new OutputStreamWriter(outStream));
 String expected = "<![CDATA[content]]>";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("content");
 xmlo.output(text, new OutputStreamWriter(outStream));
 String expected = "content";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("comment");
 xmlo.output(comment, new OutputStreamWriter(outStream));
 String expected = "<!--comment-->";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("target", "data");
 xmlo.output(pi, new OutputStreamWriter(outStream));
 String expected = "<processing-instruction name=\"target\" data=\"data\"/>";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity");
 xmlo.output(entityRef, new OutputStreamWriter(outStream));
 String expected = "&entity;";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testOutputElementContentString() throws IOException, JDOMException {
 Element element = new Element("root");
 element.setText("content");
 String expected = "content";
 assertEquals(expected, xmlo.outputElementContentString(element));
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "&lt;test&gt;<&>&'\"";
 String expected = "&amp;lt;test&gt;&amp;lt;amp;&amp;gt;&amp;lt;&amp;gt;&amp;apos;&amp;quot;";
 assertEquals(expected, xmlo.escapeAttributeEntities(input));
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "&lt;test&gt;&amp;lt;amp;&amp;gt;<&>&'\"";
 String expected = "&lt;test&gt;&amp;lt;amp&gt;&amp;lt;amp&gt;&amp;gt;<&amp;amp;gt;<&amp;amp;quot;>";
 assertEquals(expected, xmlo.escapeElementEntities(input));
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullDocument() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputDocument should throw IOException with null document");
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullOutputStream() throws IOException {
 xmlo.output(new Document(), null);
 fail("OutputDocument should throw IOException with null output stream");
 }

 @Test(expected = IOException.class)
 public void testOutputElementWithNullElement() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputElement should throw IOException with null element");
 }

 @Test(expected = IOException.class)
 public void testOutputElementContentWithNullElement() throws IOException {
 xmlo.outputElementContent(null, new OutputStreamWriter(outStream));
 fail("OutputElementContent should throw IOException with null element");
 }

 @Test(expected = IOException.class)
 public void testOutputListOfContentsWithNullList() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputListOfContents should throw IOException with null list");
 }

 @Test(expected = IOException.class)
 public void testOutputCDATAWithNullCDATA() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputCDATA should throw IOException with null CDATA");
 }

 @Test(expected = IOException.class)
 public void testOutputTextWithNullText() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputText should throw IOException with null text");
 }

 @Test(expected = IOException.class)
 public void testOutputCommentWithNullComment() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputComment should throw IOException with null comment");
 }

 @Test(expected = IOException.class)
 public void testOutputProcessingInstructionWithNullPi() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputProcessingInstruction should throw IOException with null PI");
 }

 @Test(expected = IOException.class)
 public void testOutputEntityRefWithNullEntityRef() throws IOException {
 xmlo.output(null, new OutputStreamWriter(outStream));
 fail("OutputEntityRef should throw IOException with null EntityRef");
 }
}