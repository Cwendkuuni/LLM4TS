import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

 private Document createDocument() {
 final Element root = new Element("root");
 root.addContent(new Element("element1").setText("Element 1"));
 root.addContent(new Element("element2").setText("Element 2"));
 return new Document(new Element("document"), root);
 }

 @Test
 public void testOutputDocument() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Document document = createDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(document, outputStream);
 final String expectedXml = "<document>\n" +
 " <root>\n" +
 "  <element1>Element 1</element1>\n" +
 "  <element2>Element 2</element2>\n" +
 " </root>\n" +
 "</document>";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputDocumentToString() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Document document = createDocument();
 final String expectedXml = "<document>\n" +
 " <root>\n" +
 "  <element1>Element 1</element1>\n" +
 "  <element2>Element 2</element2>\n" +
 " </root>\n" +
 "</document>";
 final String actualXml = outputter.outputString(document);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElement() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Element element = new Element("element");
 element.setText("Element");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(element, outputStream);
 final String expectedXml = "<element>Element</element>";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElementToString() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Element element = new Element("element");
 element.setText("Element");
 final String expectedXml = "<element>Element</element>";
 final String actualXml = outputter.outputString(element);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputText() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Text text = new Text("Text");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(text, outputStream);
 final String expectedXml = "Text";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputTextToString() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Text text = new Text("Text");
 final String expectedXml = "Text";
 final String actualXml = outputter.outputString(text);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputEmptyDocument() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Document document = new Document();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(document, outputStream);
 final String expectedXml = "<document />";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputEmptyElement() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Element element = new Element("element");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(element, outputStream);
 final String expectedXml = "<element />";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputList() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final List<Element> elements = new java.util.ArrayList<>();
 elements.add(new Element("element1").setText("Element 1"));
 elements.add(new Element("element2").setText("Element 2"));
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(elements, outputStream);
 final String expectedXml = "<element>\n" +
 "  <element1>Element 1</element1>\n" +
 "  <element2>Element 2</element2>\n" +
 " </element>";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputCDATA() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final CDATA cdata = new CDATA("CDATA");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(cdata, outputStream);
 final String expectedXml = "<![CDATA[CDATA]]>";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputComment() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final Comment comment = new Comment("Comment");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(comment, outputStream);
 final String expectedXml = "<!--Comment-->";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputProcessingInstruction() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"style.css\" type=\"text/css\"");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(pi, outputStream);
 final String expectedXml = "<xml-stylesheet href=\"style.css\" type=\"text/css\" />";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputEntityRef() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final EntityRef entityRef = new EntityRef("entity");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(entityRef, outputStream);
 final String expectedXml = "&entity;";
 final String actualXml = new String(outputStream.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testEscapeAttributeEntities() {
 final XMLOutputter outputter = new XMLOutputter();
 final String input = "&lt;tag&gt;<script>alert('XSS')</script>&amp;";
 final String expectedOutput = "&lt;tag&gt;&amp;amp;<script>alert(&#39;XSS&#39;)</script>&amp;";
 final String actualOutput = outputter.escapeAttributeEntities(input);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEscapeElementEntities() {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final String input = "&lt;script&gt;alert('XSS')&lt;/script&gt;";
 final String expectedOutput = "&lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt;";
 final String actualOutput = outputter.escapeElementEntities(input);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testClone() throws Exception {
 final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 final XMLOutputter clonedOutputter = outputter.clone();
 final Document document = createDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(document, outputStream);
 clonedOutputter.output(document, outputStream);
 final String expectedXml = "<document>\n" +
 " <root>\n" +
 "  <element1>Element 1</element1>\n" +
 "  <element2>Element 2</element2>\n" +
 " </root>\n" +
 "</document>";
 final byte[] outputterXml = outputStream.toByteArray();
 final byte[] clonedOutputterXml = outputStream.toByteArray();
 Assert.assertArrayEquals(expectedXml.getBytes(), outputterXml);
 Assert.assertArrayEquals(expectedXml.getBytes(), clonedOutputterXml);
 }
}