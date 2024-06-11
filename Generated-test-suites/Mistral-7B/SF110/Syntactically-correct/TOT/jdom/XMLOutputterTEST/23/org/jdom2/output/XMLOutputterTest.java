import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Test;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter(Format.getPrettyFormat());
 }

 @Test
 public void testOutputDocument() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 root.addContent(new Text("Hello World"));
 document.setRootElement(root);

 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<root><Text>Hello World</Text></root>";
 assertEquals(expectedXml, out.toString());

 String inputXml = "<root><Text>Hello World</Text></root>";
 Document inputDocument = JDOM.load(new StringReader(inputXml));
 StringWriter output = new StringWriter();
 xmlo.output(inputDocument, output);
 assertEquals(expectedXml, output.toString());
 }

 @Test
 public void testOutputElement() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 Element child = new Element("child");
 root.addContent(child);
 child.addContent(new Text("Hello World"));
 document.setRootElement(root);

 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(child, out);
 String expectedXml = "<child><Text>Hello World</Text></child>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputEmptyElement() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 Element emptyElement = new Element("empty");
 document.setRootElement(root);
 root.addContent(emptyElement);

 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(emptyElement, out);
 ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
 Document inputDocument = JDOM.load(input);
 String expectedXml = "<empty/>";
 assertEquals(expectedXml, inputDocument.getRootElement().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(null, new StringWriter());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOutputStream() {
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(new Document(), null);
 }

 @Test
 public void testOutputDocumentWithProcessingInstruction() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 root.addContent(pi);
 root.addContent(new Text("Hello World"));
 document.setRootElement(root);

 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<?xml version=\"1.0\"?><root><Text>Hello World</Text></root>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputDocumentWithComment() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 Comment comment = new Comment("This is a comment");
 root.addContent(comment);
 root.addContent(new Text("Hello World"));
 document.setRootElement(root);

 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<root><!--This is a comment--><Text>Hello World</Text></root>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputDocumentWithEntity() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 EntityRef entity = new EntityRef("ampersand");
 root.addContent(entity);
 root.addContent(new Text("This is a & test."));
 document.setRootElement(root);
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<root>&amp;This is a & test.</root>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputDocumentWithCDATA() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 CDATA cdata = new CDATA("<![CDATA[This is CDATA]]>");
 root.addContent(cdata);
 document.setRootElement(root);
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<root><![CDATA[This is CDATA]]></root>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputDocumentWithAttributes() throws Exception {
 Document document = new Document();
 Element root = new Element("root");
 root.setAttribute("attr1", "value1");
 root.setAttribute("attr2", "value2");
 Element child = new Element("child");
 child.setText("Hello World");
 root.addContent(child);
 document.setRootElement(root);
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<root attr1=\"value1\" attr2=\"value2\"><child>Hello World</child></root>";
 assertEquals(expectedXml, out.toString());
 }

 @Test
 public void testOutputDocumentWithFormat() throws Exception {
 XMLOutputter xmlo = createXMLOutputter();
 Format format = Format.getRawFormat();
 xmlo.setFormat(format);
 Document document = new Document();
 Element root = new Element("root");
 root.addContent(new Text("Hello World"));
 document.setRootElement(root);
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<root><Text>Hello World</Text></root>";
 assertEquals(expectedXml, out.toString());

 String inputXml = "<root><Text>Hello World</Text></root>";
 Document inputDocument = JDOM.load(new StringReader(inputXml));
 StringWriter output = new StringWriter();
 xmlo.output(inputDocument, output);
 assertEquals(expectedXml, output.toString());
 }

 @Test
 public void testOutputDocumentWithDefaultProcessor() throws Exception {
 Format format = Format.getPrettyFormat();
 format.setProcessInstructionDtd(false);
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.setFormat(format);
 xmlo.setXMLOutputProcessor(DefaultXMLProcessor.getInstance());
 Document document = new Document();
 Element root = new Element("root");
 root.addContent(new Text("Hello World"));
 document.setRootElement(root);
 StringWriter out = new StringWriter();
 xmlo.output(document, out);
 String expectedXml = "<root><Text>Hello World</Text></root>";
 assertEquals(expectedXml, out.toString());

 String inputXml = "<root><Text>Hello World</Text></root>";
 Document inputDocument = JDOM.load(new StringReader(inputXml));
 StringWriter output = new StringWriter();
 xmlo.output(inputDocument, output);
 assertEquals(expectedXml, output.toString());
 }
}