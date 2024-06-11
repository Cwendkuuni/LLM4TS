import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private XMLOutputter xmlOutputter;
 private Document doc;
 private StringWriter out;

 @Before
 public void setup() {
 xmlOutputter = new XMLOutputter(); // default constructor
 doc = new JDOMFactory().newDocument();
 out = new StringWriter();
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlOutputterWithFormatAndProcessor = new XMLOutputter(new Format(), new XMLOutputProcessor());
 assertEquals(new Format(), xmlOutputterWithFormatAndProcessor.getFormat());
 assertEquals(new XMLOutputProcessor(), xmlOutputterWithFormatAndProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlOutputterWithFormat = new XMLOutputter(new Format());
 assertEquals(new Format(), xmlOutputterWithFormat.getFormat());
 assertEquals(XMLOutputter.DEFAULTPROCESSOR, xmlOutputterWithFormat.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlOutputterWithProcessor = new XMLOutputter(new XMLOutputProcessor());
 assertEquals(Format.getRawFormat(), xmlOutputterWithProcessor.getFormat());
 assertEquals(new XMLOutputProcessor(), xmlOutputterWithProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithThat() {
 XMLOutputter that = new XMLOutputter();
 XMLOutputter xmlOutputterWithThat = new XMLOutputter(that);
 assertEquals(that.getFormat(), xmlOutputterWithThat.getFormat());
 assertEquals(that.getXMLOutputProcessor(), xmlOutputterWithThat.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlOutputter = new XMLOutputter();
 Format format = new Format();
 xmlOutputter.setFormat(format);
 assertEquals(format, xmlOutputter.getFormat());
 }

 @Test
 public void testGetFormat() {
 XMLOutputter xmlOutputter = new XMLOutputter();
 assertEquals(Format.getRawFormat(), xmlOutputter.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlOutputter = new XMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlOutputter.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputter xmlOutputter = new XMLOutputter();
 assertEquals(XMLOutputter.DEFAULTPROCESSOR, xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 xmlOutputter.output(doc, out);
 assertEquals(XMLUtil.convertDocumentToString(doc), xmlOutputter.outputString(doc));
 }

 @Test
 public void testOutputDocType() throws IOException {
 // create a docType
 DocType docType = new JDOMFactory().newDocType("doctype", new QName("namespace", "localName"), new AttributesImpl());
 xmlOutputter.output(docType, out);
 assertEquals("<!DOCTYPE doctype PUBLIC \"namespace public\" \"localName.dtd\">\n", out.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new JDOMFactory().newElement("root", new QName("namespace", "localName"));
 xmlOutputter.output(element, out);
 assertEquals("<root/>\n", out.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new JDOMFactory().newElement("root", new QName("namespace", "localName"));
 element.addContent(new Text("content"));
 xmlOutputter.outputElementContent(element, out);
 assertEquals("content", out.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 Element root = new JDOMFactory().newElement("root", new QName("namespace", "localName"));
 root.addContent(new Text("first"));
 root.addContent(new Text("second"));
 xmlOutputter.output(root, out);
 assertEquals("<root>\n<text>first</text>\n<text>second</text>\n</root>\n", out.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[content]]>");
 xmlOutputter.output(cdata, out);
 assertEquals("<![CDATA[content]]>\n", out.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("text");
 xmlOutputter.output(text, out);
 assertEquals("text\n", out.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("comment");
 xmlOutputter.output(comment, out);
 assertEquals("<!--comment-->\n", out.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("target", "data");
 xmlOutputter.output(pi, out);
 assertEquals("<?target data?>\n", out.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity");
 xmlOutputter.output(entityRef, out);
 assertEquals("&entity;\n", out.toString());
 }

 @Test
 public void testOutputElementContentString() {
 Element element = new JDOMFactory().newElement("root", new QName("namespace", "localName"));
 element.addContent(new Text("content"));
 assertEquals("content", xmlOutputter.outputElementContentString(element));
 }

 @Test(expected = IOException.class)
 public void testOutputWithNullOutputStream() throws IOException {
 xmlOutputter.output(doc, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputWithNullDocument() {
 xmlOutputter.output(null, out);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputWithNullWriter() {
 xmlOutputter.output(doc, null);
 }

 @Test
 public void testOutputStringDocumentWithNullDocument() {
 assertEquals("", xmlOutputter.outputString(null));
 }

 @Test(expected = IOException.class)
 public void testOutputStringWithNullWriter() throws IOException {
 xmlOutputter.outputString(doc);
 }
}