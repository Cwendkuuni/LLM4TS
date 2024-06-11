import org.junit.Test;
import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private final XMLOutputter createXMLOutputter() {
 return new XMLOutputter(Format.getPrettyFormat());
 }

 @Test
 public void testOutputDocument() throws Exception {
 // Create a sample XML document
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Element("element1").setText("Element 1"));
 root.addContent(new Element("element2").setText("Element 2"));

 // Test outputting the document to a StringWriter
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(doc, out);
 String expected = "<root><element1>Element 1</element1><element2>Element 2</element2></root>";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputDocType() throws Exception {
 // Create a sample DocType
 DocType docType = new DocType("root", null, null, null);

 // Test outputting the DocType to a StringWriter
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(docType, out);
 String expected = "<!DOCTYPE root [ ]>";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputElement() throws Exception {
 // Create a sample Element
 Element element = new Element("element");
 element.setText("Element");

 // Test outputting the Element to a StringWriter
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(element, out);
 String expected = "<element>Element</element>";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputElementContent() throws Exception {
 // Create a sample Element with content
 Element element = new Element("element");
 element.addContent(new Text("Text"));

 // Test outputting the Element content to a StringWriter
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.outputElementContent(element, out);
 String expected = "Text";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputStringOnce() throws Exception {
 // Create an XMLOutputter instance
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 String input = "Input string";

 // Test outputting the same String multiple times
 xmlo.output(new Text(input), out);
 xmlo.output(new Text(input), out);
 xmlo.output(new Text(input), out);

 // Assert that only one instance of the string is outputted
 String expected = "<text>Input string</text>";
 assertEquals(expected, out.toString());
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws Exception {
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(null, new StringWriter());
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws Exception {
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(new Document(), null);
 }

}