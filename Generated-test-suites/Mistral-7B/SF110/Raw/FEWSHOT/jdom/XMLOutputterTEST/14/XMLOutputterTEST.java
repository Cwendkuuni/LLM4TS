package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

 XMLOutputter xmlOutputter;
 Format format;
 Writer writer;
 OutputStream outputStream;

 @Before
 public void setUp() {
 xmlOutputter = new XMLOutputter();
 format = new Format();
 outputStream = new MemoryOutputStream();
 writer = XMLOutputter.makeWriter(outputStream, format);
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new JDOMFactory().newDocument("Root");
 Element root = doc.getRootElement();
 root.addContent(new Element("Child1"));
 root.addContent(new Element("Child2").setText("Test"));
 xmlOutputter.output(doc, writer);
 String expectedXml = "<Root><Child1></Child1><Child2>Test</Child2></Root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new JDOMFactory().newDocType("Root", null, "root", null);
 xmlOutputter.output(docType, writer);
 String expectedDocType = "<!DOCTYPE Root [ <!ATTLIST root att1 CDATA #IMPLIED>\n";
 assertEquals(expectedDocType, writer.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("Test");
 xmlOutputter.output(element, writer);
 String expectedElement = "<Test/>";
 assertEquals(expectedElement, writer.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("Test");
 element.addContent(new Text("Test Content"));
 xmlOutputter.outputElementContent(element, writer);
 String expectedElementContent = "Test Content";
 assertEquals(expectedElementContent, writer.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = new JDOMFactory().newElement("List").addContent(new Element("Item1")).addContent(new Element("Item2"));
 xmlOutputter.output(list, writer);
 String expectedList = "<List><Item1></Item1><Item2></Item2></List>";
 assertEquals(expectedList, writer.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("Test CDATA");
 xmlOutputter.output(cdata, writer);
 String expectedCDATA = "<![CDATA[Test CDATA]]>";
 assertEquals(expectedCDATA, writer.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Test Text");
 xmlOutputter.output(text, writer);
 String expectedText = "Test Text";
 assertEquals(expectedText, writer.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("Test Comment");
 xmlOutputter.output(comment, writer);
 String expectedComment = "<!--Test Comment-->";
 assertEquals(expectedComment, writer.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("test", "Test Data");
 xmlOutputter.output(pi, writer);
 String expectedPI = "<?test Test Data?>";
 assertEquals(expectedPI, writer.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("TestEntity");
 xmlOutputter.output(entityRef, writer);
 String expectedEntityRef = "&TestEntity;";
 assertEquals(expectedEntityRef, writer.toString());
 }

 @Test
 public void testOutputStringDocument() {
 String xmlString = xmlOutputter.outputString(new JDOMFactory().newDocument("Root"));
 String expectedXml = "<Root></Root>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringDocType() {
 String xmlString = xmlOutputter.outputString(new JDOMFactory().newDocType("Root", null, "root", null));
 String expectedXml = "<!DOCTYPE Root [ <!ATTLIST root att1 CDATA #IMPLIED>\n";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringElement() {
 Element element = new Element("Test");
 String xmlString = xmlOutputter.outputString(element);
 String expectedElement = "<Test/>";
 assertEquals(expectedElement, xmlString);
 }

 @Test
 public void testOutputStringElementContent() {
 Element element = new Element("Test");
 element.addContent(new Text("Test Content"));
 String xmlString = xmlOutputter.outputString(element);
 String expectedElementContent = "Test Content";
 assertEquals(expectedElementContent, xmlString);
 }

 @Test
 public void testOutputStringList() {
 List<Element> list = new JDOMFactory().newElement("List").addContent(new Element("Item1")).addContent(new Element("Item2"));
 String xmlString = xmlOutputter.outputString(list);
 String expectedList = "<List><Item1></Item1><Item2></Item2></List>";
 assertEquals(expectedList, xmlString);
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("Test CDATA");
 String xmlString = xmlOutputter.outputString(cdata);
 String expectedCDATA = "<![CDATA[Test CDATA]]>";
 assertEquals(expectedCDATA, xmlString);
 }

 @Test
 public void testOutputStringText() {
 Text text = new Text("Test Text");
 String xmlString = xmlOutputter.outputString(text);
 String expectedText = "Test Text";
 assertEquals(expectedText, xmlString);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("Test Comment");
 String xmlString = xmlOutputter.outputString(comment);
 String expectedComment = "<!--Test Comment-->";
 assertEquals(expectedComment, xmlString);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("test", "Test Data");
 String xmlString = xmlOutputter.outputString(pi);
 String expectedPI = "<?test Test Data?>";
 assertEquals(expectedPI, xmlString);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("TestEntity");
 String xmlString = xmlOutputter.outputString(entityRef);
 String expectedEntityRef = "&TestEntity;";
 assertEquals(expectedEntityRef, xmlString);
 }

 @Test
 public void testOutputStringElementContentString() {
 Element element = new Element("Test");
 element.addContent(new Text("Test Content"));
 String xmlString = xmlOutputter.outputElementContentString(element);
 String expectedElementContent = "Test Content";
 assertEquals(expectedElementContent, xmlString);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String expected = xmlOutputter.escapeAttributeEntities("&<>'\" Test");
 String input = "&amp;&lt;&gt;&apos; &amp;lt; &amp;gt; &amp;apos; Test";
 assertEquals(expected, input);
 }

 @Test
 public void testEscapeElementEntities() {
 String expected = xmlOutputter.escapeElementEntities("&<>'\" Test");
 String input = "&amp;<amp;>&amp;lt;>&amp;gt;>&amp;apos; Test";
 assertEquals(expected, input);
 }

 @After
 public void cleanUp() throws IOException {
 writer.close();
 outputStream.close();
 }
}