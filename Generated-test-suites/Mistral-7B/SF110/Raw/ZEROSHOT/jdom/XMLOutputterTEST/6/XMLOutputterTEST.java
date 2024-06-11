import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 @Test
 public void testXMLOutputterInstance() {
 XMLOutputter xmlOutputter = new XMLOutputter();
 assertNotNull(xmlOutputter);
 }

 @Test
 public void testXMLOutputterFormat() {
 XMLOutputter xmlOutputter = new XMLOutputter(new Format());
 assertNotNull(xmlOutputter.getFormat());
 }

 @Test
 public void testXMLOutputterProcessor() {
 XMLOutputter xmlOutputter = new XMLOutputter(null, new XMLOutputProcessor());
 assertNotNull(xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlOutputter = new XMLOutputter();
 Format format = new Format();
 xmlOutputter.setFormat(format);
 assertEquals(format, xmlOutputter.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlOutputter = new XMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlOutputter.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(doc, stringWriter);
 String expectedOutput = "<root/>";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 doc.setDocType(new DocType("doctype", null, null));
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(doc.getRootElement().getDocument().getDocType(), stringWriter);
 String expectedOutput = "<!DOCTYPE doctype [ ]>";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 Element element = new Element("child", "content");
 doc.getRootElement().addContent(element);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(element, stringWriter);
 String expectedOutput = "<child>content</child>";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 Element element = new Element("child", "content");
 doc.getRootElement().addContent(element);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.outputElementContent(element, stringWriter);
 String expectedOutput = "content";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 Element element = new Element("child");
 doc.getRootElement().addContent(element);
 List<Content> list = new ArrayList<>();
 list.add(new Text("text1"));
 list.add(new Text("text2"));
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(list, stringWriter);
 String expectedOutput = "<child><text1/><text2/></child>";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 CDATA cdata = new CDATA("cdata text");
 doc.getRootElement().addContent(cdata);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(cdata, stringWriter);
 String expectedOutput = "<![CDATA[cdata text]]>";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 Text text = new Text("text");
 doc.getRootElement().addContent(text);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(text, stringWriter);
 String expectedOutput = "text";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 Comment comment = new Comment("comment text");
 doc.getRootElement().addContent(comment);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(comment, stringWriter);
 String expectedOutput = "<!--comment text-->";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 ProcessingInstruction pi = new ProcessingInstruction("pi", "data");
 doc.getRootElement().addProcessingInstruction(pi);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(pi, stringWriter);
 String expectedOutput = "<?" + pi.getTarget() + " " + pi.getData() + "?";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 EntityRef entityRef = new EntityRef("entityName");
 doc.getRootElement().addContent(entityRef);
 XMLOutputter xmlOutputter = new XMLOutputter();
 StringWriter stringWriter = new StringWriter();
 xmlOutputter.output(entityRef, stringWriter);
 String expectedOutput = "&entityName;";
 assertEquals(expectedOutput, stringWriter.toString());
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 Document doc = JDOMFactory.createRootElement("root");
 Element element = new Element("child", "content");
 doc.getRootElement().addContent(element);
 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedOutput = "<child>content</child>";
 assertEquals(expectedOutput, xmlOutputter.outputElementContentString(element));
 }

}