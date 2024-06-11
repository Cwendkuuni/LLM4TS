import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private ByteArrayOutputStream out;

 @Before
 public void setup() {
 xmlo = new XMLOutputter();
 out = new ByteArrayOutputStream();
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Text("Hello World"));
 xmlo.output(doc, new OutputStreamWriter(out, "UTF-8"));
 String expected = "<root><text>Hello World</text></root>";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("element");
 xmlo.output(element, new OutputStreamWriter(out, "UTF-8"));
 String expected = "<element/>";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputElementWithContent() throws IOException {
 Element element = new Element("element");
 Text text = new Text("Hello World");
 element.addContent(text);
 xmlo.output(element, new OutputStreamWriter(out, "UTF-8"));
 String expected = "<element><text>Hello World</text></element>";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 List<Object> list = new ArrayList<>();
 list.add(new Text("Item 1"));
 list.add(new Text("Item 2"));
 xmlo.output(list, new OutputStreamWriter(out, "UTF-8"));
 String expected = "<text>Item 1</text><text>Item 2</text>";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 xmlo.output(cdata, new OutputStreamWriter(out, "UTF-8"));
 String expected = "<![CDATA[Hello World]]>";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("This is a comment");
 xmlo.output(comment, new OutputStreamWriter(out, "UTF-8"));
 String expected = "<!--This is a comment-->";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 xmlo.output(pi, new OutputStreamWriter(out, "UTF-8"));
 String expected = "<?xml version=\"1.0\"?>";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("#test");
 xmlo.output(entityRef, new OutputStreamWriter(out, "UTF-8"));
 String expected = "&test;";
 Assert.assertEquals(expected, out.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("element");
 Text text = new Text("Hello World");
 element.addContent(text);
 xmlo.outputElementContent(element, new OutputStreamWriter(out, "UTF-8"));
 String expected = "Hello World";
 Assert.assertEquals(expected, out.toString());
 }
}