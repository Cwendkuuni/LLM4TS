import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private XMLOutputter xmlOutputter;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 xmlOutputter = new XMLOutputter();
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testOutputDocument() throws Exception {
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Element("child1").setText("child1 text"));
 root.addContent(new Element("child2").setText("child2 text"));

 xmlOutputter.output(doc, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedXml = "<root><child1>child1 text</child1><child2>child2 text</child2></root>";
 assertEquals(expectedXml, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputDocType() throws Exception {
 DocType docType = new DocType("html", null, new Element[]{}, new Attribute[]{}, null);

 xmlOutputter.output(docType, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedDoctype = "<!DOCTYPE html SYSTEM>\n";
 assertEquals(expectedDoctype, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputElement() throws Exception {
 Element element = new Element("element");
 xmlOutputter.output(element, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedElement = "<element/>\n";
 assertEquals(expectedElement, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputElementContent() throws Exception {
 Element element = new Element("element");
 element.addContent(new Text("content"));
 xmlOutputter.outputElementContent(element, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedContent = "<element>\ncontent\n</element>\n";
 assertEquals(expectedContent, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputList() throws Exception {
 List<Element> elements = new ArrayList<>();
 elements.add(new Element("element1"));
 elements.add(new Element("element2"));
 xmlOutputter.output(elements, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedList = "<element1/><element2/>\n";
 assertEquals(expectedList, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputCDATA() throws Exception {
 CDATA cdata = new CDATA("<![CDATA[This is CDATA]]>");
 xmlOutputter.output(cdata, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedCdata = "<![CDATA[\nThis is CDATA\n]]>\n";
 assertEquals(expectedCdata, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputText() throws Exception {
 Text text = new Text("text");
 xmlOutputter.output(text, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedText = "<text>text</text>\n";
 assertEquals(expectedText, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputComment() throws Exception {
 Comment comment = new Comment("This is a comment");
 xmlOutputter.output(comment, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedComment = "<!--This is a comment-->\n";
 assertEquals(expectedComment, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputProcessingInstruction() throws Exception {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "<?xml version='1.0'?>\n");
 xmlOutputter.output(pi, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedPI = "<?xml version='1.0'?>\n";
 assertEquals(expectedPI, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputEntityRef() throws Exception {
 EntityRef entityRef = new EntityRef("entity");
 xmlOutputter.output(entityRef, outputStream);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String expectedEntity = "&entity;\n";
 assertEquals(expectedEntity, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringDocument() throws Exception {
 Document doc = new Document();
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputXml = "<root><child1>child1 text</child1><child2>child2 text</child2></root>";
 xmlOutputter.outputString(doc);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(new Document(new StringReader(inputXml)), outputStream);
 String expectedXml = "<root><child1>child1 text</child1><child2>child2 text</child2></root>";
 assertEquals(expectedXml, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringDocType() throws Exception {
 DocType docType = new DocType("html", null, new Element[]{}, new Attribute[]{}, null);
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputDoctype = "<!DOCTYPE html SYSTEM>\n";
 xmlOutputter.outputString(docType);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(new DocType(new StringReader(inputDoctype)), outputStream);
 String expectedDoctype = "<!DOCTYPE html SYSTEM>\n";
 assertEquals(expectedDoctype, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringElement() throws Exception {
 Element element = new Element("element");
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputElement = "<element/>\n";
 xmlOutputter.outputString(element);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(new Element("element"), outputStream);
 String expectedElement = "<element/>\n";
 assertEquals(expectedElement, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringElementContent() throws Exception {
 Element element = new Element("element");
 element.addContent(new Text("content"));
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputContent = "<element>\ncontent\n</element>\n";
 xmlOutputter.outputString(element);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.outputElementContent(new Element("element"), outputStream);
 String expectedContent = "<element>\ncontent\n</element>\n";
 assertEquals(expectedContent, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringList() throws Exception {
 List<Element> elements = new ArrayList<>();
 elements.add(new Element("element1"));
 elements.add(new Element("element2"));
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputList = "<element1/><element2/>\n";
 xmlOutputter.outputString(elements);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(new List<Element>(elements.toArray()), outputStream);
 String expectedList = "<element1/><element2/>\n";
 assertEquals(expectedList, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringCDATA() throws Exception {
 CDATA cdata = new CDATA("<![CDATA[This is CDATA]]>");
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputCdata = "<![CDATA[\nThis is CDATA\n]]>\n";
 xmlOutputter.outputString(cdata);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(cdata, outputStream);
 String expectedCdata = "<![CDATA[\nThis is CDATA\n]]>\n";
 assertEquals(expectedCdata, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringText() throws Exception {
 Text text = new Text("text");
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputText = "<text>text</text>\n";
 xmlOutputter.outputString(text);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(text, outputStream);
 String expectedText = "<text>text</text>\n";
 assertEquals(expectedText, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringComment() throws Exception {
 Comment comment = new Comment("This is a comment");
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputComment = "<!--This is a comment-->\n";
 xmlOutputter.outputString(comment);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(comment, outputStream);
 String expectedComment = "<!--This is a comment-->\n";
 assertEquals(expectedComment, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringProcessingInstruction() throws Exception {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "<?xml version='1.0'?>\n");
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputPI = "<?xml version='1.0'?>\n";
 xmlOutputter.outputString(pi);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(pi, outputStream);
 String expectedPI = "<?xml version='1.0'?>\n";
 assertEquals(expectedPI, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringEntityRef() throws Exception {
 EntityRef entityRef = new EntityRef("entity");
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputEntity = "&entity;\n";
 xmlOutputter.outputString(entityRef);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.output(entityRef, outputStream);
 String expectedEntity = "&entity;\n";
 assertEquals(expectedEntity, new String(outputStream.toByteArray()));
 }

 @Test
 public void testOutputStringElementContentString() throws Exception {
 Element element = new Element("element");
 element.addContent(new Text("content"));
 xmlOutputter.setFormat(new Format("UTF-8"));
 String inputContentString = "<element>\ncontent\n</element>\n";
 xmlOutputter.outputString(element);
 xmlOutputter.setOutputstream(outputStream);
 xmlOutputter.outputElementContent(element, outputStream);
 String expectedContentString = "<element>\ncontent\n</element>\n";
 assertEquals(expectedContentString, new String(outputStream.toByteArray()));
 }
}