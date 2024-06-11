import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

 private final static String XML_SCHEMA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element1 attr1=\"value1\">Content1</element1>\n" +
         "<element2 attr2=\"value2\">Content2</element2>\n" +
         "<element3>\n" +
         "<subElement attr3=\"value3\">SubContent</subElement>\n" +
         "</element3>\n" +
         "<element4>\n" +
         "<text>TextContent</text>\n" +
         "</element4>\n" +
         "<comment><![CDATA[This is a comment]]></comment>\n" +
         "</root>";

 @Test
 public void testOutputDocument() throws Exception {
 Document doc = JDOMFactory.stringToDocument(XML_SCHEMA);
 XMLOutputter xmlo = new XMLOutputter();
 StringWriter sw = new StringWriter();
 xmlo.output(doc, sw);
 String expected = XML_SCHEMA;
 String actual = sw.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElement() throws Exception {
 Element root = new Element("root");
 Element element1 = new Element("element1").addAttribute("attr1", "value1");
 Element element2 = new Element("element2").addAttribute("attr2", "value2");
 Element element3 = new Element("element3");
 Element subElement = new Element("subElement").addAttribute("attr3", "value3");
 element3.addContent(subElement);
 root.addContent(element1);
 root.addContent(element2);
 root.addContent(element3);
 xmlo.output(root, new ByteArrayOutputStream(512));
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentNull() throws Exception {
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output((Document) null, new StringWriter());
 }

 @Test(expected = IOException.class)
 public void testOutputWriterNull() throws Exception {
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(new Document(), null);
 }

 @Test
 public void testOutputFormat() throws Exception {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(Format.getPrettyFormat());
 StringWriter sw1 = new StringWriter();
 StringWriter sw2 = new StringWriter();
 Document doc = JDOMFactory.stringToDocument(XML_SCHEMA);
 xmlo1.output(doc, sw1);
 xmlo2.output(doc, sw2);
 String expected = XML_SCHEMA;
 String actual1 = sw1.toString();
 String actual2 = sw2.toString();
 Assert.assertFalse(actual1.equals(actual2));
 Assert.assertEquals(expected, actual1);
 Assert.assertEquals(expected, actual2);
 }

 @Test
 public void testOutputProcessor() throws Exception {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(new MyCustomXMLOutputProcessor());
 StringWriter sw1 = new StringWriter();
 StringWriter sw2 = new StringWriter();
 Document doc = JDOMFactory.stringToDocument(XML_SCHEMA);
 xmlo1.output(doc, sw1);
 xmlo2.output(doc, sw2);
 String expected = XML_SCHEMA;
 String actual1 = sw1.toString();
 String actual2 = sw2.toString();
 Assert.assertFalse(actual1.equals(actual2));
 Assert.assertEquals(expected, actual1);
 Assert.assertEquals(expected, actual2);
 }

 @Test
 public void testOutputList() throws Exception {
 List<Text> list = new ArrayList<>();
 list.add(new Text("Text1"));
 list.add(new Text("Text2"));
 xmlo.output(list, new StringWriter());
 }

 @Test
 public void testOutputEntityRef() throws Exception {
 EntityRef entityRef = new EntityRef("entity1");
 xmlo.output(entityRef, new StringWriter());
 }

 @Test
 public void testOutputComment() throws Exception {
 Comment comment = new Comment("This is a comment");
 xmlo.output(comment, new StringWriter());
 }

 @Test
 public void testOutputCDATA() throws Exception {
 CDATA cdata = new CDATA("<![CDATA[This is CDATA]]>");
 xmlo.output(cdata, new StringWriter());
 }

 @Test
 public void testOutputDocType() throws Exception {
 DocType docType = new DocType("doctype", null, new Element("root"));
 xmlo.output(docType, new StringWriter());
 }

 @Test
 public void testOutputProcessingInstruction() throws Exception {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\"");
 xmlo.output(pi, new StringWriter());
 }

 private static class MyCustomXMLOutputProcessor extends XMLOutputProcessor {
 @Override
 public void process(Writer out, Format format, Content content) throws IOException {
 // Custom processing logic here
 }
 }

}