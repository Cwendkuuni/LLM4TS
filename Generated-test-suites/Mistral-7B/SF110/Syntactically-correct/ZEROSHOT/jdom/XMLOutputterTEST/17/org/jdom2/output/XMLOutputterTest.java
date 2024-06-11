package org.jdom2.output;

import org.jdom2.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

 private XMLOutputter xmloutputter;
 private Document document;

 @Before
 public void setUp() {
 xmloutputter = new XMLOutputter();

 DocumentBuilder documentBuilder = new JDOMBuilder();
 document = documentBuilder.build(
 "<root>\n" +
 "  <element attribute=\"value\">Content</element>\n" +
 "  <element>\n" +
 "    <subElement>\n" +
 "      <subSubElement/>\n" +
 "    </subElement>\n" +
 "  </element>\n" +
 "</root>");
 }

 @Test
 public void testOutputDocument() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(document, outputStream);
 String expectedXML = "<root>\n" +
 "  <element attribute=\"value\">Content</element>\n" +
 "  <element>\n" +
 "    <subElement>\n" +
 "      <subSubElement/>\n" +
 "    </subElement>\n" +
 "  </element>\n" +
 "</root>";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DocType docType = new DefaultDocType("root", null, null);
 xmloutputter.output(docType, outputStream);
 String expectedXML = "<!DOCTYPE root [ ]>\n";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = document.getRootElement().getChild(0);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(element, outputStream);
 String expectedXML = "<element attribute=\"value\">Content</element>\n";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = document.getRootElement().getChild(1);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.outputElementContent(element, outputStream);
 String expectedXML = "<subElement/>\n";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 List<Content> list = document.getRootElement().getChildren();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(list, outputStream);
 String expectedXML = "<root>\n" +
 "  <element attribute=\"value\">Content</element>\n" +
 "  <element>\n" +
 "    <subElement>\n" +
 "      <subSubElement/>\n" +
 "    </subElement>\n" +
 "  </element>\n" +
 "</root>";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("Test CDATA");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(cdata, outputStream);
 String expectedXML = "<![CDATA[Test CDATA]]>";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = document.getRootElement().getTextTrim();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(text, outputStream);
 String expectedXML = "root\n";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("Test Comment");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(comment, outputStream);
 String expectedXML = "<!--Test Comment-->";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(pi, outputStream);
 String expectedXML = "<?xml version=\"1.0\"?>\n";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entityRef");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmloutputter.output(entityRef, outputStream);
 String expectedXML = "&entityRef;\n";
 assertEquals(expectedXML, outputStream.toString());
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 Element element = document.getRootElement().getChild(1);
 String expectedXML = "<subElement/>\n";
 String actualXML = xmloutputter.outputElementContentString(element);
 assertEquals(expectedXML, actualXML);
 }
}