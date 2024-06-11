import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Document doc;

 @Before
 public void setup() {
 DocumentBuilder builder = new JDOMBuilder();
 doc = builder.build(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element>Test</element></root>"));
 }

 @Test
 public void testXMLOutputter_OutputDocumentToOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo = new XMLOutputter();
 xmlo.output(doc, outputStream);
 String expectedXml = "<root>\n<element>Test</element>\n</root>";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputDocumentToString() {
 String expectedXml = "<root>\n<element>Test</element>\n</root>";
 String actualXml = xmlo.outputString(doc);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputElementToOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo = new XMLOutputter();
 Element element = doc.getRootElement().getChild(0);
 xmlo.output(element, outputStream);
 String expectedXml = "<element>Test</element>";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputElementToString() {
 String expectedXml = "<element>Test</element>";
 String actualXml = xmlo.outputString(doc.getRootElement().getChild(0));
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputListToOutputStream() throws IOException {
 List<Element> list = doc.getRootElement().getChildren();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo = new XMLOutputter();
 xmlo.output(list, outputStream);
 String expectedXml = "<root>\n<element>Test</element>\n</root>";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputListToString() {
 String expectedXml = "<root>\n<element>Test</element>\n</root>";
 String actualXml = xmlo.outputString(doc.getRootElement().getChildren());
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputCDATAToOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 CDATA cdata = new CDATA("<![CDATA[Test]]>");
 xmlo = new XMLOutputter();
 xmlo.output(cdata, outputStream);
 String expectedXml = "<![CDATA[Test]]>";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputCDATAToString() {
 String expectedXml = "<![CDATA[Test]]>";
 String actualXml = xmlo.outputString(cdata);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputTextToOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Text text = doc.getRootElement().getChildText(0);
 xmlo = new XMLOutputter();
 xmlo.output(text, outputStream);
 String expectedXml = "Test";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputTextToString() {
 String expectedXml = "Test";
 String actualXml = xmlo.outputString(doc.getRootElement().getChildText(0));
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputCommentToOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Comment comment = new Comment("Test Comment");
 xmlo = new XMLOutputter();
 xmlo.output(comment, outputStream);
 String expectedXml = "<!--Test Comment-->";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputCommentToString() {
 String expectedXml = "<!--Test Comment-->";
 String actualXml = xmlo.outputString(comment);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputProcessingInstructionToOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ProcessingInstruction pi = new ProcessingInstruction("xml-version", "1.0");
 xmlo = new XMLOutputter();
 xmlo.output(pi, outputStream);
 String expectedXml = "<?xml-version 1.0?>";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputProcessingInstructionToString() {
 String expectedXml = "<?xml-version 1.0?>";
 String actualXml = xmlo.outputString(pi);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputEntityRefToOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 EntityRef entityRef = new EntityRef("testEntity");
 xmlo = new XMLOutputter();
 xmlo.output(entityRef, outputStream);
 String expectedXml = "&testEntity;";
 Assert.assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testXMLOutputter_OutputEntityRefToString() {
 String expectedXml = "&testEntity;";
 String actualXml = xmlo.outputString(entityRef);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputter_OutputElementContentToString() {
 String expectedXml = "<element>Test</element>";
 String actualXml = xmlo.outputElementContentString(doc.getRootElement());
 Assert.assertEquals(expectedXml, actualXml);
 }
}