import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new Format(), null);
 Assert.assertNotNull(xmlo.getFormat());
 Assert.assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlo = new XMLOutputter(new Format());
 Assert.assertNotNull(xmlo.getFormat());
 Assert.assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlo = new XMLOutputter(null, new XMLOutputProcessor());
 Assert.assertNull(xmlo.getFormat());
 Assert.assertNotNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithThat() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(xmlo1);
 Assert.assertSame(xmlo1.getFormat(), xmlo2.getFormat());
 Assert.assertNull(xmlo2.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 Assert.assertSame(format, xmlo.getFormat());
 }

 @Test
 public void testOutputDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 StringWriter writer = new StringWriter();
 xmlo.output(doc, writer);
 String result = writer.toString();
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 xmlo.output(doc, null);
 }

 @Test
 public void testOutputDocType() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 DocType docType = new DocType("root", null, null, null, null, null, null, null);
 StringWriter writer = new StringWriter();
 xmlo.output(docType, writer);
 String result = writer.toString();
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testOutputDocTypeWithNullDocType() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputDocTypeWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 DocType docType = new DocType("root", null, null, null, null, null, null, null);
 xmlo.output(docType, null);
 }

 @Test
 public void testOutputElement() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Element element = doc.getRootElement().getChild("child", "test");
 StringWriter writer = new StringWriter();
 xmlo.output(element, writer);
 String result = writer.toString();
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testOutputElementWithNullElement() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputElementWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Element element = doc.getRootElement().getChild("child", "test");
 xmlo.output(element, null);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Element element = doc.getRootElement().getChild("child", "test");
 StringWriter writer = new StringWriter();
 xmlo.outputElementContent(element, writer);
 String result = writer.toString();
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testOutputElementContentWithNullElement() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.outputElementContent(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputElementContentWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Element element = doc.getRootElement().getChild("child", "test");
 xmlo.outputElementContent(element, null);
 }

 @Test
 public void testOutputList() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 List<Content> list = new ArrayList<>();
 list.add(doc.getRootElement().getChild("child1", "test1"));
 list.add(doc.getRootElement().getChild("child2", "test2"));
 StringWriter writer = new StringWriter();
 xmlo.output(list, writer);
 String result = writer.toString();
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testOutputListWithNullList() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputListWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 List<Content> list = new ArrayList<>();
 list.add(doc.getRootElement().getChild("child1", "test1"));
 list.add(doc.getRootElement().getChild("child2", "test2"));
 xmlo.output(list, null);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 CDATA cdata = new CDATA("<![CDATA[Test CDATA]]>");
 StringWriter writer = new StringWriter();
 xmlo.output(cdata, writer);
 String result = writer.toString();
 Assert.assertEquals("<![CDATA[Test CDATA]]>", result);
 }

 @Test(expected = IOException.class)
 public void testOutputCDATAWithNullCDATA() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputCDATAWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 CDATA cdata = new CDATA("<![CDATA[Test CDATA]]>");
 xmlo.output(cdata, null);
 }

 @Test
 public void testOutputText() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Text text = doc.getRootElement().getTextTrim();
 StringWriter writer = new StringWriter();
 xmlo.output(text, writer);
 String result = writer.toString();
 Assert.assertEquals(text.getText(), result);
 }

 @Test(expected = IOException.class)
 public void testOutputTextWithNullText() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputTextWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Text text = doc.getRootElement().getTextTrim();
 xmlo.output(text, null);
 }

 @Test
 public void testOutputComment() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Comment comment = new Comment("Test Comment");
 StringWriter writer = new StringWriter();
 xmlo.output(comment, writer);
 String result = writer.toString();
 Assert.assertEquals("<!---Test Comment--->", result);
 }

 @Test(expected = IOException.class)
 public void testOutputCommentWithNullComment() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputCommentWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Comment comment = new Comment("Test Comment");
 xmlo.output(comment, null);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 ProcessingInstruction pi = new ProcessingInstruction("xml-mapping", "Test PI");
 StringWriter writer = new StringWriter();
 xmlo.output(pi, writer);
 String result = writer.toString();
 Assert.assertEquals("<?xml-mapping PI?>", result);
 }

 @Test(expected = IOException.class)
 public void testOutputProcessingInstructionWithNullPI() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputProcessingInstructionWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 ProcessingInstruction pi = new ProcessingInstruction("xml-mapping", "Test PI");
 xmlo.output(pi, null);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 EntityRef entityRef = new EntityRef("testEntity");
 StringWriter writer = new StringWriter();
 xmlo.output(entityRef, writer);
 String result = writer.toString();
 Assert.assertEquals("&testEntity;", result);
 }

 @Test(expected = IOException.class)
 public void testOutputEntityRefWithNullEntityRef() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 StringWriter writer = new StringWriter();
 xmlo.output(null, writer);
 }

 @Test(expected = IOException.class)
 public void testOutputEntityRefWithNullWriter() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 EntityRef entityRef = new EntityRef("testEntity");
 xmlo.output(entityRef, null);
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = JDOMFactory.createRootElement("root");
 Element element = doc.getRootElement().getChild("child", "test");
 String result = xmlo.outputElementContentString(element);
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testOutputElementContentStringWithNullElement() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 String result = xmlo.outputElementContentString(null);
 Assert.assertEquals("", result);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "<abc&def>123&456&'&\"&> & < & & dos' s & 's & \"s\" & &quot; & &amp; & &gt;";
 String expected = "&amp;lt;abc&amp;amp;amp;def&gt;123&amp;amp;amp;#38;456&amp;amp;amp;#39;&amp;amp;amp;#39;s&amp;amp;amp;#39;s&amp;amp;amp;#39;s&amp;amp;amp;#34;s&amp;amp;amp;#32; & & & lt; & gt;";
 String result = XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(input, null);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "<abc&def>123&456&'&\"&> & < & & dos' s & 's & \"s\" & &quot; & &amp; & &gt;";
 String expected = "&lt;abc&gt;123&amp;#38;456&amp;#39;s&amp;#39;s&amp;#39;s&amp;#34;s&amp;#32; & & & lt; & gt;";
 String result = XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(input, null);
 Assert.assertEquals(expected, result);
 }

}