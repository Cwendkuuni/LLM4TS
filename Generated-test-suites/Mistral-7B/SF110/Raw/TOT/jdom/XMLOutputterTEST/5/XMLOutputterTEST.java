import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setup() {
 xmlo = new XMLOutputter();
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new Document();
 Element root = new Element("root");
 root.addContent(new Text("Hello World"));
 doc.setRootElement(root);
 xmlo.output(doc, new OutputStreamWriter(outputStream));
 String expected = "<root><Text>Hello World</Text></root>";
 assertEquals(expected, xmlo.outputString(doc));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 Document doc = new Document();
 DocType doctype = new DocType("html", new QName("http://www.w3.org/1999/xhtml"), "");
 doc.setDoctype(doctype);
 xmlo.output(doctype, new OutputStreamWriter(outputStream));
 String expected = "<!DOCTYPE html SYSTEM \"http://www.w3.org/1999/xhtml\">";
 assertEquals(expected, xmlo.outputString(doctype));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("element");
 Text text = new Text("Text content");
 element.addContent(text);
 xmlo.output(element, new OutputStreamWriter(outputStream));
 String expected = "<element><Text>Text content</Text></element>";
 assertEquals(expected, xmlo.outputString(element));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("element");
 Text text = new Text("Text content");
 element.addContent(text);
 xmlo.outputElementContent(element, new OutputStreamWriter(outputStream));
 String expected = "Text content";
 assertEquals(expected, xmlo.outputElementContentString(element));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 Element root = new Element("root");
 Text text1 = new Text("Text 1");
 Text text2 = new Text("Text 2");
 List<Content> list = new JDOMNamespaceList<Content>(new Element("list"));
 list.add(text1);
 list.add(text2);
 root.addContent(list);
 xmlo.output(list, new OutputStreamWriter(outputStream));
 String expected = "<list><Text>Text 1</Text><Text>Text 2</Text></list>";
 assertEquals(expected, xmlo.outputString(list));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello CDATA]]>");
 xmlo.output(cdata, new OutputStreamWriter(outputStream));
 String expected = "<![CDATA[Hello CDATA]]>";
 assertEquals(expected, xmlo.outputString(cdata));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Text content");
 xmlo.output(text, new OutputStreamWriter(outputStream));
 String expected = "Text content";
 assertEquals(expected, xmlo.outputString(text));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("This is a comment");
 xmlo.output(comment, new OutputStreamWriter(outputStream));
 String expected = "<!--This is a comment-->";
 assertEquals(expected, xmlo.outputString(comment));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\"");
 xmlo.output(pi, new OutputStreamWriter(outputStream));
 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 assertEquals(expected, xmlo.outputString(pi));
 assertNotNull(outputStream.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("#external-xml-declaration");
 xmlo.output(entityRef, new OutputStreamWriter(outputStream));
 String expected = "&external-xml-declaration;";
 assertEquals(expected, xmlo.outputString(entityRef));
 assertNotNull(outputStream.toString());
 }
}