import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Collections;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Document doc;

 @Before
 public void setup() {
 JDOMHelper.init();
 doc = new JDOMHelper().createElement("root");
 doc.getRootElement().addContent(new JDOMHelper().createElement("element1", "content1"));
 doc.getRootElement().addContent(new JDOMHelper().createElement("element2", "content2"));
 }

 @Test
 public void testOutputDocument() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo = new XMLOutputter();
 xmlo.output(doc, outStream);
 String expectedXml = "<root><element1>content1</element1><element2>content2</element2></root>";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputDocType() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 DocType docType = new DefaultDocumentType("root", "-//W3C//DTD XHTML 1.0 Strict//EN", "xhtml1-strict.dtd");
 xmlo = new XMLOutputter();
 xmlo.output(docType, outStream);
 String expectedXml = "<!DOCTYPE root [ ... ]>";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputElement() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo = new XMLOutputter();
 xmlo.output(doc.getRootElement(), outStream);
 String expectedXml = "<root><element1>content1</element1><element2>content2</element2></root>";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputElementContent() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo = new XMLOutputter();
 xmlo.outputElementContent(doc.getRootElement(), outStream);
 String expectedXml = "<element1>content1</element1><element2>content2</element2>";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputList() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 List<Content> list = Collections.singletonList(doc.getRootElement());
 xmlo = new XMLOutputter();
 xmlo.output(list, outStream);
 String expectedXml = "<root><element1>content1</element1><element2>content2</element2></root>";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputCDATA() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 CDATA cdata = new CDATA("This is a CDATA section");
 xmlo = new XMLOutputter();
 xmlo.output(cdata, outStream);
 String expectedXml = "<![CDATA[This is a CDATA section]]>";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputText() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 Text text = new Text("This is a Text node");
 xmlo = new XMLOutputter();
 xmlo.output(text, outStream);
 String expectedXml = "This is a Text node";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputComment() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 Comment comment = new Comment("This is a comment");
 xmlo = new XMLOutputter();
 xmlo.output(comment, outStream);
 String expectedXml = "<!--This is a comment-->";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputProcessingInstruction() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 ProcessingInstruction pi = new ProcessingInstruction("xml-app", "data");
 xmlo = new XMLOutputter();
 xmlo.output(pi, outStream);
 String expectedXml = "<?xml-app data?>";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputEntityRef() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 EntityRef entityRef = new EntityRef("#pcdata");
 xmlo = new XMLOutputter();
 xmlo.output(entityRef, outStream);
 String expectedXml = "&pcdata;";
 assertEquals(expectedXml, new String(outStream.toByteArray()));
 }

 @Test
 public void testOutputStringDocument() {
 String expectedXml = "<root><element1>content1</element1><element2>content2</element2></root>";
 assertEquals(expectedXml, xmlo.outputString(doc));
 }

 @Test
 public void testOutputStringDocType() {
 String expectedXml = "<!DOCTYPE root [ ... ]>";
 assertEquals(expectedXml, xmlo.outputString(new DefaultDocumentType("root", "-//W3C//DTD XHTML 1.0 Strict//EN", "xhtml1-strict.dtd")));
 }

 @Test
 public void testOutputStringElement() {
 String expectedXml = "<root><element1>content1</element1><element2>content2</element2></root>";
 assertEquals(expectedXml, xmlo.outputString(doc.getRootElement()));
 }

 @Test
 public void testOutputStringElementContent() {
 String expectedXml = "<element1>content1</element1><element2>content2</element2>";
 assertEquals(expectedXml, xmlo.outputElementContentString(doc.getRootElement()));
 }
}