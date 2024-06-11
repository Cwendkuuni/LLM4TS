import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private Document document;
 private XMLOutputter xmlOutputter;

 @Before
 public void setUp() {
 JDOMHelper.init(); // Initialize JDOM
 document = new JDOMHelper().createDocument();
 xmlOutputter = new XMLOutputter();
 }

 @Test
 public void testOutputDocument() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(document, outputStream);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>"; // Replace with the expected XML
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputDocType() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 DocType docType = new DocType("html", null, null, null);
 xmlOutputter.output(docType, outputStream);
 String expectedXml = "<!DOCTYPE html [ ]>";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("root");
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(element, outputStream);
 String expectedXml = "<root/>";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("root");
 element.addContent(new Text("Hello World"));
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.outputElementContent(element, outputStream);
 String expectedXml = "Hello World";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Content> list = new JDOMHelper().createList("item1", "item2", "item3");
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(list, outputStream);
 String expectedXml = "<item1/><item2/><item3/>";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("Hello World");
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(cdata, outputStream);
 String expectedXml = "<![CDATA[Hello World]]>";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Hello World");
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(text, outputStream);
 String expectedXml = "Hello World";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("Hello World");
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(comment, outputStream);
 String expectedXml = "<!--Hello World-->";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\"");
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(pi, outputStream);
 String expectedXml = "<xml-declaration version=\"1.0\"/>";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("externalEntity");
 OutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(entityRef, outputStream);
 String expectedXml = "&externalEntity;";
 String actualXml = new String(outputStream.toByteArray());
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringDocument() {
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>";
 String actualXml = xmlOutputter.outputString(document);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringDocType() {
 String expectedXml = "<!DOCTYPE html [ ]>";
 String actualXml = xmlOutputter.outputString(new DocType("html", null, null, null));
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testoutputStringElement() {
 String expectedXml = "<root/>";
 String actualXml = xmlOutputter.outputString(new Element("root"));
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringList() {
 List<Content> list = new JDOMHelper().createList("item1", "item2", "item3");
 String expectedXml = "<item1/><item2/><item3/>";
 String actualXml = xmlOutputter.outputString(list);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("Hello World");
 String expectedXml = "<![CDATA[Hello World]]>";
 String actualXml = xmlOutputter.outputString(cdata);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringText() {
 Text text = new Text("Hello World");
 String expectedXml = "Hello World";
 String actualXml = xmlOutputter.outputString(text);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("Hello World");
 String expectedXml = "<! --Hello World-->";
 String actualXml = xmlOutputter.outputString(comment);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\"");
 String expectedXml = "<xml-declaration version=\"1.0\"/>";
 String actualXml = xmlOutputter.outputString(pi);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("externalEntity");
 String expectedXml = "&externalEntity;";
 String actualXml = xmlOutputter.outputString(entityRef);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringElementContent() {
 Element element = new Element("root");
 element.addContent(new Text("Hello World"));
 String expectedXml = "Hello World";
 String actualXml = xmlOutputter.outputStringElementContent(element);
 assertEquals(expectedXml, actualXml);
 }
}