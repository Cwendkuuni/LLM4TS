import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 @Test
 public void testOutputDocument() throws JDOMException, IOException {
 // Create sample XML document
 Document doc = new Document();
 Element root = new Element("root");
 root.addContent(new Element("element1").setText("Element 1"));
 root.addContent(new Element("element2").setText("Element 2"));
 doc.setRootElement(root);

 // Test XMLOutputter with default settings
 XMLOutputter outputter = new XMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(doc, outputStream);
 String xmlString = outputStream.toString();
 assertEquals("<root><element1>Element 1</element1><element2>Element 2</element2></root>", xmlString);

 // Test XMLOutputter with custom Format
 Format customFormat = new Format();
 customFormat.setEncoding("UTF-8");
 customFormat.setOmitDeclaration(true);
 customFormat.setLineSeparator("\n");
 XMLOutputter customOutputter = new XMLOutputter(customFormat);
 ByteArrayOutputStream customOutputStream = new ByteArrayOutputStream();
 customOutputter.output(doc, customOutputStream);
 String customXmlString = customOutputStream.toString();
 assertEquals("<root><element1>Element 1</element1><element2>Element 2</element2></root>", customXmlString);
 }

 @Test
 public void testOutputElement() throws JDOMException, IOException {
 // Create sample XML document
 Document doc = new Document();
 Element root = new Element("root");
 Element element = new Element("element");
 element.setText("Element");
 root.addContent(element);
 doc.setRootElement(root);

 // Test XMLOutputter with default settings
 XMLOutputter outputter = new XMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(element, outputStream);
 String xmlString = outputStream.toString();
 assertEquals("<element>Element</element>", xmlString);

 // Test XMLOutputter with custom Format
 Format customFormat = new Format();
 customFormat.setEncoding("UTF-8");
 customFormat.setOmitDeclaration(true);
 customFormat.setLineSeparator("\n");
 XMLOutputter customOutputter = new XMLOutputter(customFormat);
 ByteArrayOutputStream customOutputStream = new ByteArrayOutputStream();
 customOutputter.output(element, customOutputStream);
 String customXmlString = customOutputStream.toString();
 assertEquals("<element>Element</element>", customXmlString);
 }

 @Test
 public void testOutputEmptyDocument() throws IOException {
 // Test XMLOutputter with empty document
 XMLOutputter outputter = new XMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(new Document(), outputStream);
 String xmlString = outputStream.toString();
 assertEquals("", xmlString);
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws IOException {
 // Test XMLOutputter with null document
 XMLOutputter outputter = new XMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputter.output(null, outputStream);
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws IOException {
 // Test XMLOutputter with null OutputStream
 XMLOutputter outputter = new XMLOutputter();
 outputter.output(new Document(), null);
 }
}