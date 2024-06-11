import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Element("child1").setText("Child 1"));
 root.addContent(new Element("child2").setText("Child 2"));

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";

 String actualXml = xmlOutputter.outputString(doc);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocType("root", new QName("http://example.com", "root"), null);

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<!DOCTYPE root SYSTEM 'http://example.com/root.dtd'>\n";

 String actualXml = xmlOutputter.outputString(docType);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("element");
 element.setText("Element Text");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<element>Element Text</element>\n";

 String actualXml = xmlOutputter.outputString(element);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElementWithContent() throws IOException {
 Element element = new Element("element");
 element.addContent(new Element("child").setText("Child"));

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<element>\n<child>Child</child>\n</element>\n";

 String actualXml = xmlOutputter.outputString(element);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputListOfContents() throws IOException {
 List<Content> list = new ArrayList<>();
 list.add(new Element("element1").setText("Element 1"));
 list.add(new Element("element2").setText("Element 2"));

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<element1>Element 1</element1>\n<element2>Element 2</element2>\n";

 String actualXml = xmlOutputter.outputString(list);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[This is some CDATA text]]>");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<![CDATA[This is some CDATA text]]>\n";

 String actualXml = xmlOutputter.outputString(cdata);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Text Content");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "Text Content\n";

 String actualXml = xmlOutputter.outputString(text);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("This is a comment");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<!--This is a comment-->\n";

 String actualXml = xmlOutputter.outputString(comment);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0'");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<?xml version='1.0'?>\n";

 String actualXml = xmlOutputter.outputString(pi);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entityName");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "&entityName;\n";

 String actualXml = xmlOutputter.outputString(entityRef);
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNullDocument() throws IOException {
 XMLOutputter xmlOutputter = new XMLOutputter();

 try {
 xmlOutputter.output(null, new StringWriter());
 } catch (NullPointerException e) {
 throw new IllegalArgumentException("XMLOutputter should not accept null Document", e);
 }
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testMakeWriterWithUnsupportedEncoding() throws IOException {
 try {
 XMLOutputter.makeWriter(new ByteArrayOutputStream(), new Format().setEncoding("UTF-16BE"));
 } catch (UnsupportedEncodingException e) {
 // Expected
 }
 }
}