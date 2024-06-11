import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

 @Test
 public void testXMLOutputterConstruction() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputter xmloFormat = new XMLOutputter(Format.getRawFormat());
 XMLOutputter xmloProcessor = new XMLOutputter(null, new XMLOutputProcessor());
 XMLOutputter xmloFormatAndProcessor = new XMLOutputter(Format.getRawFormat(), new XMLOutputProcessor());
 XMLOutputter xmloClone = xmloFormatAndProcessor.clone();

 assertNotNull(xmlo);
 assertNotNull(xmloFormat);
 assertNotNull(xmloProcessor);
 assertNotNull(xmloFormatAndProcessor);
 assertNotNull(xmloClone);
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document document = new JDOMFactory().newDocument("Root", new Element("Child"));
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(document, os);
 String expected = "<Root><Child/></Root>";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new JDOMFactory().newDocType("Root", null, null, null, null);
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(docType, os);
 String expected = "<!DOCTYPE Root [ ]>\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new JDOMFactory().newElement("Root", new Attribute("attr", "value"));
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(element, os);
 String expected = "<Root attr=\"value\"/>\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new JDOMFactory().newElement("Root", new Text("Hello World!"));
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.outputElementContent(element, os);
 String expected = "Hello World!\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = new ArrayList<>();
 Element element1 = new JDOMFactory().newElement("Element1");
 Element element2 = new JDOMFactory().newElement("Element2");
 list.add(element1);
 list.add(element2);
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(list, os);
 String expected = "<Element1/>\n<Element2/>\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new JDOMFactory().newCDATA("Hello World!");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(cdata, os);
 String expected = "<![CDATA[Hello World!]]>\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new JDOMFactory().newText("Hello World!");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(text, os);
 String expected = "Hello World!\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new JDOMFactory().newComment("Hello World!");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(comment, os);
 String expected = "<!--HelloWorld-->\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new JDOMFactory().newProcessingInstruction("xml", "version=\"1.0\"");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(pi, os);
 String expected = "<?xml version=\"1.0\"?>\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(entityRef, os);
 String expected = "&entity;\n";
 String actual = new String(os.toByteArray());
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 Element element = new JDOMFactory().newElement("Root", new Text("Hello World!"));
 StringWriter sw = new StringWriter();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.outputElementContent(element, sw);
 String expected = "Hello World!\n";
 String actual = sw.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "&lt;&gt;&amp;&apos;&quot;&amp;entityRef;";
 String format = new Format();
 String expected = "&amp;lt;&amp;gt;&amp;amp;&amp;apos;&amp;quot;&amp;amp;entityRef;";
 String actual = XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(input, format);
 assertEquals(expected, actual);
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "&lt;script&gt;alert(&quot;Hello World!&quot;)&lt;/script&gt;";
 String format = new Format();
 String expected = "&lt;script&gt;alert(&quot;Hello World!&quot;)&lt;/script&gt;";
 String actual = XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(input, format);
 assertEquals(expected, actual);
 }
}