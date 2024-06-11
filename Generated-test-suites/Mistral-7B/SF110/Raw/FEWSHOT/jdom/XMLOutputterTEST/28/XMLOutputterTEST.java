package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import java.io.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XMLOutputterTest {

 XMLOutputter outputter;
 Document doc;
 OutputStream out;
 Writer writer;

 @Before
 public void setUp() throws Exception {
 outputter = new XMLOutputter();
 doc = new JDOMFactory().newDocument("root");
 out = new ByteArrayOutputStream();
 writer = new StringWriter();
 }

 @Test
 public void testOutputDocument() throws IOException {
 outputter.output(doc, out);
 assertEquals("<root/>\n", outputter.outputString(doc));
 }

 @Test
 public void testOutputDocType() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 DocType doctype = new JDOMFactory().newDocType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "xhtml1-strict.dtd");
 outputter.output(doctype, out);
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"xhtml1-strict.dtd\">\n", outputter.outputString(doctype));
 }

 @Test
 public void testOutputElement() throws IOException {
 Element root = doc.getRootElement();
 root.addContent(new Element("child"));
 outputter.output(root.getChild(0), out);
 assertEquals("<child/>\n", outputter.outputString(root.getChild(0)));
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element root = doc.getRootElement();
 root.addContent(new Element("child").setText("test"));
 outputter.outputElementContent(root.getChild(0), out);
 assertEquals("test\n", outputter.outputString(root.getChild(0)));
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(new Element("item1"));
 list.add(new Element("item2"));
 outputter.output(list, out);
 assertEquals("<item1/>\n<item2/>\n", outputter.outputString(list));
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("test");
 outputter.output(cdata, out);
 assertEquals("<!--CDATA[_>test<_]-->\n", outputter.outputString(cdata));
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("test");
 outputter.output(text, out);
 assertEquals("test\n", outputter.outputString(text));
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = doc.createComment("test comment");
 outputter.output(comment, out);
 assertEquals("<!--test comment-->\n", outputter.outputString(comment));
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "<?xml version=\"1.0\"?>\n");
 outputter.output(pi, out);
 assertEquals(pi.getData(), outputter.outputString(pi));
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("myEntity");
 outputter.output(entityRef, out);
 assertEquals("&myEntity;\n", outputter.outputString(entityRef));
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 Element root = doc.getRootElement();
 root.addContent(new Element("child").setText("test"));
 assertEquals("test", outputter.outputElementContentString(root.getChild(0)));
 }

 @Test
 public void testSetFormat() {
 Format format = new Format();
 outputter.setFormat(format);
 assertEquals(format, outputter.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor processor = new XMLOutputter.DefaultXMLProcessor();
 outputter.setXMLOutputProcessor(processor);
 assertEquals(processor, outputter.getXMLOutputProcessor());
 }

 @Test
 public void testToString() {
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", outputter.toString());
 }
}