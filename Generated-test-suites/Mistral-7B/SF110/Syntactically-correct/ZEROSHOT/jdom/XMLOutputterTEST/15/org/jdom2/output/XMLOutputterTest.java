package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 @BeforeClass
 static public void setup() {
 // Create test document
 Document doc = new Document();
 Element root = new Element("root");
 root.addContent(new Element("element1").setText("Element 1"));
 root.addContent(new Element("element2").setText("Element 2"));
 doc.setRootElement(root);
 }

 @Test
 public void testOutputDocument() throws IOException {
 // Test outputting a Document to an OutputStream
 XMLOutputter xmlOutputter = new XMLOutputter();
 OutputStream out = new ByteArrayOutputStream();
 xmlOutputter.output(doc, out);
 String xmlString = out.toString();
 assertEquals("<root><element1>Element 1</element1><element2>Element 2</element2></root>", xmlString);
 }

 @Test
 public void testOutputDocType() throws IOException {
 // Test outputting a DocType to an OutputStream
 DocType doctype = new DocType("html", null, null, null);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(doctype, out);
 String xmlString = out.toString();
 assertEquals("<!DOCTYPE html SYSTEM>\n", xmlString);
 }

 @Test
 public void testOutputElement() throws IOException {
 // Test outputting an Element to an OutputStream
 Element element = new Element("testElement");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(element, out);
 String xmlString = out.toString();
 assertEquals("<testElement/>\n", xmlString);
 }

 @Test
 public void testOutputElementWithContent() throws IOException {
 // Test outputting an Element with content to an OutputStream
 Element element = new Element("testElement");
 element.addContent(new Text("Test content"));
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(element, out);
 String xmlString = out.toString();
 assertEquals("<testElement>Test content</testElement>\n", xmlString);
 }

 @Test
 public void testOutputList() throws IOException {
 // Test outputting a List of Content to an OutputStream
 List<Content> list = new ArrayList<>();
 list.add(new Text("Test Content 1"));
 list.add(new Element("Test Element 1").setText("Test Element 1 Content"));
 XMLOutputter xmlOutputter = new XMLOutputter();
 OutputStream out = new ByteArrayOutputStream();
 xmlOutputter.output(list, out);
 String xmlString = out.toString();
 assertEquals("<testContent>\n <Text>Test Content 1</Text>\n <TestElement1>Test Element 1 Content</TestElement1>\n</testContent>\n", xmlString);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 // Test outputting a CDATA to an OutputStream
 CDATA cdata = new CDATA("Test CDATA");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(cdata, out);
 String xmlString = out.toString();
 assertEquals("<![CDATA[Test CDATA]]>\n", xmlString);
 }

 @Test
 public void testOutputText() throws IOException {
 // Test outputting a Text node to an OutputStream
 Text text = new Text("Test Text");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(text, out);
 String xmlString = out.toString();
 assertEquals("<Text>Test Text</Text>\n", xmlString);
 }

 @Test
 public void testOutputComment() throws IOException {
 // Test outputting a Comment to an OutputStream
 Comment comment = new Comment("Test Comment");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(comment, out);
 String xmlString = out.toString();
 assertEquals("<!--Test Comment-->\n", xmlString);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 // Test outputting a ProcessingInstruction to an OutputStream
 ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"test.css\"");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(pi, out);
 String xmlString = out.toString();
 assertEquals("<?xml-stylesheet type=\"text/css\" href=\"test.css\"?>\n", xmlString);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 // Test outputting an EntityRef to an OutputStream
 EntityRef entityRef = new EntityRef("testEntity");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(entityRef, out);
 String xmlString = out.toString();
 assertEquals("<![ENTITY testEntity SYSTEM \"testEntity.ent\"]>\n", xmlString);
 }

 @Test
 public void testOutputStringDocument() {
 // Test outputting a Document to a StringWriter
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(doc, writer);
 String xmlString = writer.toString();
 assertEquals("<root><element1>Element 1</element1><element2>Element 2</element2></root>", xmlString);
 }

 @Test
 public void testOutputStringDocType() {
 // Test outputting a DocType to a StringWriter
 DocType doctype = new DocType("html", null, null, null);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(doctype, writer);
 String xmlString = writer.toString();
 assertEquals("<!DOCTYPE html SYSTEM>\n", xmlString);
 }

 @Test
 public void testOutputStringElement() {
 // Test outputting an Element to a StringWriter
 Element element = new Element("testElement");
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(element, writer);
 String xmlString = writer.toString();
 assertEquals("<testElement/>\n", xmlString);
 }

 @Test
 public void testOutputStringElementWithContent() {
 // Test outputting an Element with content to a StringWriter
 Element element = new Element("testElement");
 element.addContent(new Text("Test content"));
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(element, writer);
 String xmlString = writer.toString();
 assertEquals("<testElement>Test content</testElement>\n", xmlString);
 }

 @Test
 public void testOutputStringList() {
 // Test outputting a List of Content to a StringWriter
 List<Content> list = new ArrayList<>();
 list.add(new Text("Test Content 1"));
 list.add(new Element("Test Element 1").setText("Test Element 1 Content"));
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(list, writer);
 String xmlString = writer.toString();
 assertEquals("<testContent>\n <Text>Test Content 1</Text>\n <TestElement1>Test Element 1 Content</TestElement1>\n</testContent>\n", xmlString);
 }

 @Test
 public void testOutputStringCDATA() throws IOException {
 // Test outputting a CDATA to a StringWriter
 CDATA cdata = new CDATA("Test CDATA");
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(cdata, writer);
 String xmlString = writer.toString();
 assertEquals("<![CDATA[Test CDATA]]>\n", xmlString);
 }

 @Test
 public void testOutputStringText() throws IOException {
 // Test outputting a Text node to a StringWriter
 Text text = new Text("Test Text");
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(text, writer);
 String xmlString = writer.toString();
 assertEquals("<Text>Test Text</Text>\n", xmlString);
 }

 @Test
 public void testOutputStringComment() throws IOException {
 // Test outputting a Comment to a StringWriter
 Comment comment = new Comment("Test Comment");
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(comment, writer);
 String xmlString = writer.toString();
 assertEquals("<!--Test Comment-->\n", xmlString);
 }

 @Test
 public void testOutputStringProcessingInstruction() throws IOException {
 // Test outputting a ProcessingInstruction to a StringWriter
 ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"test.css\"");
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(pi, writer);
 String xmlString = writer.toString();
 assertEquals("<?xml-stylesheet type=\"text/css\" href=\"test.css\"?>\n", xmlString);
 }

 @Test
 public void testOutputStringEntityRef() throws IOException {
 // Test outputting an EntityRef to a StringWriter
 EntityRef entityRef = new EntityRef("testEntity");
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 xmlOutputter.output(entityRef, writer);
 String xmlString = writer.toString();
 assertEquals("<![ENTITY testEntity SYSTEM \"testEntity.ent\"]>\n", xmlString);
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 // Test outputting an Element's content as a String
 Element element = new Element("testElement");
 element.addContent(new Text("Test content"));
 XMLOutputter xmlOutputter = new XMLOutputter();
 String xmlString = xmlOutputter.outputElementContentString(element);
 assertEquals("Test content", xmlString);
 }
}