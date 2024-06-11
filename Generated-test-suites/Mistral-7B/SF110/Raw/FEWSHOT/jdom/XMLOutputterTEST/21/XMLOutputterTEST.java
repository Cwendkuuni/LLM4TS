package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XMLOutputterTest {

 XMLOutputter xmlo;
 Document doc;
 OutputStream out;
 Writer writer;

 @Before
 public void setup() throws Exception {
 xmlo = new XMLOutputter();
 doc = new JDOMFactory().newDocument();
 out = new ByteArrayOutputStream();
 writer = xmlo.makeWriter(out, xmlo.getFormat());
 }

 @Test
 public void testOutputDocument() throws IOException {
 xmlo.output(doc, writer);
 assertEquals(doc.getRootElement().toString(), xmlo.outputString(doc));
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = new DefaultDocument().getRootElement().getDocument().getDoctype();
 xmlo.output(doctype, writer);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element root = new Element("root");
 doc.setRootElement(root);
 xmlo.output(root, writer);
 assertEquals(root.toString(), xmlo.outputString(root));
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = new JDOMFactory().newElement("list").addContent(new JDOMFactory().newElement("item1")).addContent(new JDOMFactory().newElement("item2"));
 xmlo.output(list, writer);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[hello]]>");
 xmlo.output(cdata, writer);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("text");
 xmlo.output(text, writer);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("comment");
 xmlo.output(comment, writer);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 xmlo.output(pi, writer);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("#comment");
 xmlo.output(entityRef, writer);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element root = new Element("root");
 Text text = new Text("text");
 root.addContent(text);
 xmlo.outputElementContent(root, writer);
 }

 @Test
 public void testSetFormat() {
 Format format = new Format();
 xmlo.setFormat(format);
 assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor processor = new XMLOutputter.DefaultXMLProcessor();
 xmlo.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 XMLOutputter clone = xmlo.clone();
 assertNotSame(xmlo, clone);
 assertEquals(xmlo.getFormat(), clone.getFormat());
 assertEquals(xmlo.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
 }

 @Test
 public void testOutputStringDocument() {
 assertEquals(doc.getRootElement().toString(), xmlo.outputString(doc));
 }

 @Test
 public void testOutputStringDocType() {
 }

 @Test
 public void testOutputStringElement() {
 assertEquals(doc.getRootElement().toString(), xmlo.outputString(doc.getRootElement()));
 }

 @Test
 public void testOutputStringList() {
 List<Element> list = new JDOMFactory().newElement("list").addContent(new JDOMFactory().newElement("item1")).addContent(new JDOMFactory().newElement("item2"));
 assertEquals(list.toString(), xmlo.outputString(list));
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("<![CDATA[hello]]>");
 assertEquals("<![CDATA[hello]]>", xmlo.outputString(cdata));
 }

 @Test
 public void testOutputStringText() {
 Text text = new Text("text");
 assertEquals("text", xmlo.outputString(text));
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("comment");
 assertEquals("", xmlo.outputString(comment));
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 assertEquals("<?xml version=\"1.0\"?>", xmlo.outputString(pi));
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("#comment");
 assertEquals("", xmlo.outputString(entityRef));
 }

 @Test
 public void testOutputStringElementContent() {
 Element root = new Element("root");
 Text text = new Text("text");
 root.addContent(text);
 assertEquals("text", xmlo.outputStringElementContent(root));
 }

 @Test // Test escapeAttributeEntities
 public void testEscapeAttributeEntities() throws IOException {
 String str = "<root attr=\"<test> & '\" ></root>";
 String escapedStr = xmlo.escapeAttributeEntities(str, xmlo.getFormat());
 assertEquals("<root attr=\"&lt;test&gt; &amp; &#39; ></root>", escapedStr);
 }

 @Test // Test escapeElementEntities
 public void testEscapeElementEntities() throws IOException {
 String str = "<root>&lt;test&gt;</root>";
 String escapedStr = xmlo.escapeElementEntities(str, xmlo.getFormat());
 assertEquals("<root>&lt;test&gt;</root>", escapedStr);
 }
}