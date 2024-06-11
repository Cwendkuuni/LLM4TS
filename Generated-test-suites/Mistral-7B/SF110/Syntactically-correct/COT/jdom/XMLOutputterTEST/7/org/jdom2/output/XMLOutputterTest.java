import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private XMLOutputter xmlOutputter;
 private Document doc;
 private String expectedXml;

 @Before
 public void setUp() throws JDOMException, IOException {
 DocumentBuilder builder = new DOMParser();
 doc = builder.build(new File("input.xml"));
 xmlOutputter = new XMLOutputter();
 }

 @Test
 public void testOutputDocument() throws IOException {
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(doc, target);
 String actualXml = target.toString();
 expectedXml = "expected XML output for document";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = doc.getRootElement().getDocument().getDoctype();
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(doctype, target);
 String actualXml = target.toString();
 expectedXml = "expected XML output for docType";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = doc.getRootElement().getChild("childElement");
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(element, target);
 String actualXml = target.toString();
 expectedXml = "expected XML output for childElement";
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = doc.getRootElement().getChildren("listElement");
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(list, target);
 String actualXml = target.toString();
 expectedXml = "expected XML output for listElement";
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() {
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(null, target);
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() {
 Document doc = this.doc;
 xmlOutputter.output(doc, null);
 }

 @Test
 public void testOutputStringDocument() {
 String xmlOutput = xmlOutputter.outputString(doc);
 assertEquals(expectedXml, xmlOutput);
 }

 @Test
 public void testOutputStringDocType() {
 String xmlOutput = xmlOutputter.outputString(doc.getRootElement().getDocument().getDoctype());
 assertEquals(expectedXml, xmlOutput);
 }

 @Test
 public void testOutputStringElement() {
 String xmlOutput = xmlOutputter.outputString(doc.getRootElement().getChild("childElement"));
 assertEquals(expectedXml, xmlOutput);
 }

 @Test
 public void testOutputStringList() {
 String xmlOutput = xmlOutputter.outputString(doc.getRootElement().getChildren("listElement"));
 assertEquals(expectedXml, xmlOutput);
 }

 @Test(expected = IOException.class)
 public void testOutputNullElement() {
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(null, target);
 }

 @Test
 public void testOutputNullOutputStreamWithFormat() throws IOException {
 Format format = new Format();
 OutputStream target = null;
 xmlOutputter.output(doc, target, format);
 }

 @Test
 public void testOutputFormatWithNullDoc() {
 Format format = new Format();
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(null, target, format);
 }

 @Test
 public void testOutputFormatWithNullOutputStream() {
 Format format = new Format();
 xmlOutputter.output(doc, null, format);
 }

 @Test
 public void testOutputFormat() throws IOException {
 Format format = new Format();
 OutputStream target = new ByteArrayOutputStream();
 xmlOutputter.output(doc, target, format);
 String actualXml = target.toString();
 expectedXml = "expected XML output with custom format";
 assertEquals(expectedXml, actualXml);
 }
}