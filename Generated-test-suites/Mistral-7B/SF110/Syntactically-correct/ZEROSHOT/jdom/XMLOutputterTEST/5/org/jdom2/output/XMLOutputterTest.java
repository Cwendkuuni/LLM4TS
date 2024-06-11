import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Format format;
 private ByteArrayOutputStream outputStream;
 private StringWriter stringWriter;

 @Before
 public void setUp() {
 format = new Format();
 xmlo = new XMLOutputter(format);
 outputStream = new ByteArrayOutputStream();
 stringWriter = new StringWriter();
 }

 @Test
 public void testXMLOutputter() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(format);
 XMLOutputter xmlo3 = new XMLOutputter(format, null);
 XMLOutputter xmlo4 = new XMLOutputter(null, null);
 XMLOutputter xmlo5 = new XMLOutputter(null, new XMLOutputProcessor());

 assertEquals(null, xmlo1.getFormat());
 assertEquals(null, xmlo1.getXMLOutputProcessor());
 assertEquals(format, xmlo2.getFormat());
 assertEquals(null, xmlo2.getXMLOutputProcessor());
 assertEquals(format, xmlo3.getFormat());
 assertEquals(null, xmlo3.getXMLOutputProcessor());
 assertEquals(null, xmlo4.getFormat());
 assertEquals(null, xmlo4.getXMLOutputProcessor());
 assertEquals(null, xmlo5.getFormat());
 assertEquals(new XMLOutputProcessor(), xmlo5.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format newFormat = new Format();
 xmlo.setFormat(newFormat);
 assertEquals(newFormat, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor newProcessor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(newProcessor);
 assertEquals(newProcessor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 xmlo.output(doc, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "<root/>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocumentString() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 xmlo.outputString(doc);
 String actual = xmlo.outputString(doc);
 String expected = "<root/>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = new JDOMFactory().newDocumentType("root", null, null);
 xmlo.output(doctype, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "<!DOCTYPE root [ ]>\n";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocTypeString() throws IOException {
 DocType doctype = new JDOMFactory().newDocumentType("root", null, null);
 xmlo.outputString(doctype);
 String actual = xmlo.outputString(doctype);
 String expected = "<!DOCTYPE root [ ]>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new JDOMFactory().newElement("root");
 xmlo.output(element, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "<root/>\n";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementString() throws IOException {
 Element element = new JDOMFactory().newElement("root");
 xmlo.outputString(element);
 String actual = xmlo.outputString(element);
 String expected = "<root/>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new JDOMFactory().newElement("root");
 Text text = new JDOMFactory().newText("Hello World");
 element.addContent(text);
 xmlo.outputElementContent(element, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "Hello World";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 Element element = new JDOMFactory().newElement("root");
 Text text = new JDOMFactory().newText("Hello World");
 element.addContent(text);
 xmlo.outputElementContentString(element);
 String actual = xmlo.outputElementContentString(element);
 String expected = "Hello World";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputList() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = doc.getRootElement();
 List<Element> elements = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 Element element = new JDOMFactory().newElement("element" + i);
 root.addContent(element);
 elements.add(element);
 }
 xmlo.output(elements, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "<root>\n<element0/>\n<element1/>\n<element2/>\n<element3/>\n<element4/>\n";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 xmlo.output(cdata, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "<![CDATA[Hello World]]>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputCDATAString() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 xmlo.outputString(cdata);
 String actual = xmlo.outputString(cdata);
 String expected = "<![CDATA[Hello World]]>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Hello World");
 xmlo.output(text, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "Hello World";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputTextString() throws IOException {
 Text text = new Text("Hello World");
 xmlo.outputString(text);
 String actual = xmlo.outputString(text);
 String expected = "Hello World";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("This is a comment.");
 xmlo.output(comment, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "<!--This is a comment.-->\n";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputCommentString() throws IOException {
 Comment comment = new Comment("This is a comment.");
 xmlo.outputString(comment);
 String actual = xmlo.outputString(comment);
 String expected = "<!--This is a comment.-->";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-data", "data");
 xmlo.output(pi, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "<?xml-data data?>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputProcessingInstructionString() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-data", "data");
 xmlo.outputString(pi);
 String actual = xmlo.outputString(pi);
 String expected = "<?xml-data data?>";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity1");
 xmlo.output(entityRef, outputStream);
 String actual = new String(outputStream.toByteArray(), "UTF-8");
 String expected = "&entity1;";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputEntityRefString() throws IOException {
 EntityRef entityRef = new EntityRef("entity1");
 xmlo.outputString(entityRef);
 String actual = xmlo.outputString(entityRef);
 String expected = "&entity1;";
 assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContentStringWithElements() throws IOException {
 Element root = new JDOMFactory().newElement("root");
 Element element1 = new JDOMFactory().newElement("element1");
 Text text1 = new Text("Hello");
 Element element2 = new JDOMFactory().newElement("element2");
 Text text2 = new Text("World");
 root.addContent(element1);
 root.addContent(text1);
 root.addContent(element2);
 root.addContent(text2);
 xmlo.outputElementContentString(root);
 String actual = xmlo.outputElementContentString(root);
 String expected = "<element1/><text>Hello</text><element2/><text>World</text>";
 assertEquals(expected, actual);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "&lt;&gt;&amp;&quot;&apos;";
 String expected = "&amp;lt;&amp;gt;&amp;amp;&amp;quot;&amp;apos;";
 String actual = xmlo.escapeAttributeEntities(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "&lt;script&gt;alert('Hello World')&lt;/script&gt;";
 String expected = "&lt;?xml version=&quot;1.0&quot;&gt; &lt;script&gt;alert(&apos;Hello World&apos;)&lt;/script&gt;";
 String actual = xmlo.escapeElementEntities(input, format);
 assertEquals(expected, actual);
 }

 @Test
 public void testClone() {
 XMLOutputter original = new XMLOutputter();
 XMLOutputter cloned = original.clone();
 assertEquals(original, cloned);
 }

 @Test
 public void testToString() {
 XMLOutputter xmlo = new XMLOutputter();
 Map<String, String> expected = new HashMap<>();
 expected.put("omitDeclaration", "false");
 expected.put("encoding", "UTF-8");
 expected.put("omitEncoding", "false");
 expected.put("indent", " ");
 expected.put("expandEmptyElements", "true");
 expected.put("lineSeparator", "\r\n");
 expected.put("mode", "xml");
 String actual = xmlo.toString();
 String expectedToString = "XMLOutputter[omitDeclaration=false, encoding=UTF-8, omitEncoding=false, indent = , expandEmptyElements=true, lineSeparator=\r\n, mode=xml]";
 assertEquals(expectedToString, actual);
 }
}