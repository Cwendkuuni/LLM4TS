import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private Document document;
 private OutputStream outputStream;
 private String expectedOutput;

 @Before
 public void setup() {
 JDOMUtils.init(); // Initialize JDOM2

 DocumentBuilder builder = new DocumentBuilder();
 document = builder.buildDocument();
 Element root = new Element("root");
 document.setRootElement(root);
 Element child1 = new Element("child1");
 child1.setText("Child 1");
 root.addContent(child1);
 Element child2 = new Element("child2");
 child2.setText("Child 2");
 root.addContent(child2);
 }

 @Test
 public void testOutputDocument() throws IOException {
 XMLOutputter xmlOutputter = new XMLOutputter();
 outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(document, outputStream);
 expectedOutput = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = new DocType("test", null, null, null, null, null);
 String expectedDoctypeOutput = "<!DOCTYPE test [ ]>";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(doctype, outputStream);
 assertEquals(expectedDoctypeOutput, outputStream.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = document.getRootElement().getChild("child1");
 String expectedElementOutput = "<child1>Child 1</child1>";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(element, outputStream);
 assertEquals(expectedElementOutput, outputStream.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = document.getRootElement().getChild("child1");
 String expectedContentOutput = "Child 1";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.outputElementContent(element, outputStream);
 assertEquals(expectedContentOutput, outputStream.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> elements = document.getRootElement().getChildren();
 String expectedListOutput = "<child1>Child 1</child1><child2>Child 2</child2>";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(elements, outputStream);
 assertEquals(expectedListOutput, outputStream.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("This is CDATA");
 String expectedCDATAOutput = "<![CDATA[This is CDATA]]>";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(cdata, outputStream);
 assertEquals(expectedCDATAOutput, outputStream.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = document.getRootElement().getTextTrim();
 String expectedTextOutput = "root<child1>Child 1</child1><child2>Child 2</child2>";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(document, outputStream);
 assertEquals(expectedTextOutput, outputStream.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("This is a comment");
 String expectedCommentOutput = "<!--This is a comment-->";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(comment, outputStream);
 assertEquals(expectedCommentOutput, outputStream.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-default", "");
 String expectedPIOutput = "<?xml-default?>";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(pi, outputStream);
 assertEquals(expectedPIOutput, outputStream.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity1");
 String expectedEntityRefOutput = "&entity1;";
 outputStream = new ByteArrayOutputStream();
 XMLOutputter xmlOutputter = new XMLOutputter();
 xmlOutputter.output(entityRef, outputStream);
 assertEquals(expectedEntityRefOutput, outputStream.toString());
 }

 @Test
 public void testOutputStringDocument() {
 String expectedOutput = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(document);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringDocType() {
 String expectedOutput = "<!DOCTYPE test [ ]>";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(document.getRootElement().getDocumentType());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringElement() {
 Element element = document.getRootElement().getChild("child1");
 String expectedOutput = "<child1>Child 1</child1>";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(element);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringList() {
 List<Element> elements = document.getRootElement().getChildren();
 String expectedOutput = "<child1>Child 1</child1><child2>Child 2</child2>";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(elements);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("This is CDATA");
 String expectedOutput = "<![CDATA[This is CDATA]]>";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(cdata);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringText() {
 Text text = document.getRootElement().getTextTrim();
 String expectedOutput = "root<child1>Child 1</child1><child2>Child 2</child2>";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(document);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("This is a comment");
 String expectedOutput = "<!--This is a comment-->";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(comment);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringPI() {
 ProcessingInstruction pi = new ProcessingInstruction("xml-default", "");
 String expectedOutput = "<?xml-default?>";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(pi);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("entity1");
 String expectedOutput = "&entity1;";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputString(entityRef);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringElementContent() {
 Element element = document.getRootElement().getChild("child1");
 String expectedOutput = "Child 1";
 XMLOutputter xmlOutputter = new XMLOutputter();
 String actualOutput = xmlOutputter.outputElementContentString(element);
 assertEquals(expectedOutput, actualOutput);
 }
}