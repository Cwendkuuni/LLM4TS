import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private static final String XML_CONTENT = "<root><element>Text content</element></root>";

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new Document(new JAXBElement<Element>("root", Root.class, new Element("root")) {
 {
 this.addContent(new Element("element").setText(XML_CONTENT));
 }
 });

 XMLOutputter outputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 outputter.output(doc, writer);
 String expectedXml = "<root><element>Text content</element></root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputDocumentWithFormat() throws IOException {
 Document doc = new Document(new JAXBElement<Element>("root", Root.class, new Element("root")) {
 {
 this.addContent(new Element("element").setText(XML_CONTENT));
 }
 });
 Format format = new Format();
 format.setOmitDeclaration(true);
 format.setEncoding("UTF-8");
 XMLOutputter outputter = new XMLOutputter(format);
 StringWriter writer = new StringWriter();
 outputter.output(doc, writer);
 String expectedXml = "<root><element>Text content</element></root>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputStream() throws IOException {
 Document doc = new Document(new JAXBElement<Element>("root", Root.class, new Element("root")) {
 {
 this.addContent(new Element("element").setText(XML_CONTENT));
 }
 });
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter outputter = new XMLOutputter();
 outputter.output(doc, out);
 byte[] expectedXmlBytes = XML_CONTENT.getBytes("UTF-8");
 byte[] actualXmlBytes = ((ByteArrayOutputStream)out).toByteArray();
 assertArrayEquals(expectedXmlBytes, actualXmlBytes);
 }

 @Test
 public void testOutputStreamWithFormat() throws IOException {
 Document doc = new Document(new JAXBElement<Element>("root", Root.class, new Element("root")) {
 {
 this.addContent(new Element("element").setText(XML_CONTENT));
 }
 });
 OutputStream out = new ByteArrayOutputStream();
 Format format = new Format();
 format.setOmitDeclaration(true);
 format.setEncoding("UTF-8");
 XMLOutputter outputter = new XMLOutputter(format);
 outputter.output(doc, out);
 byte[] expectedXmlBytes = XML_CONTENT.getBytes("UTF-8");
 byte[] actualXmlBytes = ((ByteArrayOutputStream)out).toByteArray();
 assertArrayEquals(expectedXmlBytes, actualXmlBytes);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("root").addContent(new Element("element").setText(XML_CONTENT));
 XMLOutputter outputter = new XMLOutputter();
 StringWriter writer = new StringWriter();
 outputter.outputElementContent(element, writer);
 String expectedXml = "<element>Text content</element>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testOutputElementWithFormat() throws IOException {
 Element element = new Element("root").addContent(new Element("element").setText(XML_CONTENT));
 Format format = new Format();
 format.setOmitDeclaration(true);
 format.setEncoding("UTF-8");
 XMLOutputter outputter = new XMLOutputter(format);
 StringWriter writer = new StringWriter();
 outputter.outputElementContent(element, writer);
 String expectedXml = "<element>Text content</element>";
 assertEquals(expectedXml, writer.toString());
 }

 @Test
 public void testThrowsIOExceptionWhenNullDocument() {
 Document doc = null;
 StringWriter writer = new StringWriter();
 XMLOutputter outputter = new XMLOutputter();
 try {
 outputter.output(doc, writer);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testThrowsIOExceptionWhenNullOutputStream() {
 OutputStream out = null;
 Document doc = new Document();
 XMLOutputter outputter = new XMLOutputter();
 try {
 outputter.output(doc, out);
 fail();
 } catch (IOException e) {
 // expected
 }
}

}