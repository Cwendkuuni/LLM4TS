import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 @Test
 public void testOutputDocument() throws Exception {
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Element("child1").setText("Child 1"));
 root.addContent(new Element("child2").setText("Child 2"));
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(doc, stringWriter);
 String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputDocType() throws Exception {
 DocType docType = new DocType("html", null, new Attribute[]{new Attribute("xmlns", "http://www.w3.org/1999/xhtml")}, null);
 XMLOutputter outputter = new XMLOutputter(Format.getRawFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(docType, stringWriter);
 String expectedXml = "<!DOCTYPE html SYSTEM ''>";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputElement() throws Exception {
 Element element = new Element("element");
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(element, stringWriter);
 String expectedXml = "<element/>";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputElementWithContent() throws Exception {
 Element element = new Element("element");
 element.addContent(new Text("Content"));
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(element, stringWriter);
 String expectedXml = "<element>Content</element>";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputListOfContents() throws Exception {
 List<Content> contentList = new ArrayList<>();
 contentList.add(new Element("element1").setText("Element 1"));
 contentList.add(new Element("element2").setText("Element 2"));
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(contentList, stringWriter);
 String expectedXml = "<element1>Element 1</element1>\n<element2>Element 2</element2>";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputCDATA() throws Exception {
 CDATA cdata = new CDATA("<![CDATA[This is some CDATA content]]>");
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(cdata, stringWriter);
 String expectedXml = "<![CDATA[This is some CDATA content]]>";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputText() throws Exception {
 Text text = new Text("Some text content");
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(text, stringWriter);
 String expectedXml = "Some text content";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputComment() throws Exception {
 Comment comment = new Comment("This is a comment");
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(comment, stringWriter);
 String expectedXml = "<!--This is a comment-->";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws Exception {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0'?");
 XMLOutputter outputter = new XMLOutputter(Format.getRawFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(pi, stringWriter);
 String expectedXml = "<?xml-declaration version='1.0'?>";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputEntityRef() throws Exception {
 EntityRef entityRef = new EntityRef("external-ref");
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.output(entityRef, stringWriter);
 String expectedXml = "&external-ref;";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputElementContent() throws Exception {
 Element element = new Element("element");
 element.addContent(new Text("Content"));
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 StringWriter stringWriter = new StringWriter();
 outputter.outputElementContent(element, stringWriter);
 String expectedXml = "Content";
 assertEquals(expectedXml, stringWriter.toString());
 }

 @Test
 public void testOutputStringDocument() throws Exception {
 Document doc = new Document();
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 String actualXml = outputter.outputString(doc);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringDocType() throws Exception {
 DocType docType = new DocType("html", null, null, null);
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<!DOCTYPE html SYSTEM ''>";
 String actualXml = outputter.outputString(docType);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringElement() throws Exception {
 Element element = new Element("element");
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<element/>";
 String actualXml = outputter.outputString(element);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringElementWithContent() throws Exception {
 Element element = new Element("element");
 element.addContent(new Text("Content"));
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<element>Content</element>";
 String actualXml = outputter.outputString(element);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringListOfContents() throws Exception {
 List<Content> contentList = new ArrayList<>();
 contentList.add(new Element("element1").setText("Element 1"));
 contentList.add(new Element("element2").setText("Element 2"));
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<element1>Element 1</element1>\n<element2>Element 2</element2>";
 String actualXml = outputter.outputString(contentList);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringCDATA() throws Exception {
 CDATA cdata = new CDATA("<![CDATA[This is some CDATA content]]>");
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<![CDATA[This is some CDATA content]]>";
 String actualXml = outputter.outputString(cdata);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringText() throws Exception {
 Text text = new Text("Some text content");
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "Some text content";
 String actualXml = outputter.outputString(text);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringComment() throws Exception {
 Comment comment = new Comment("This is a comment");
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<!--This is a comment-->";
 String actualXml = outputter.outputString(comment);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringProcessingInstruction() throws Exception {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0'?");
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "<?xml-declaration version='1.0'?>";
 String actualXml = outputter.outputString(pi);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringEntityRef() throws Exception {
 EntityRef entityRef = new EntityRef("external-ref");
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "&external-ref;";
 String actualXml = outputter.outputString(entityRef);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringElementContent() throws Exception {
 Element element = new Element("element");
 element.addContent(new Text("Content"));
 XMLOutputter outputter = new XMLOutputter();
 String expectedXml = "Content";
 String actualXml = outputter.outputElementContentString(element);
 assertEquals(expectedXml, actualXml);
 }
}