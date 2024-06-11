import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 private XMLOutputter createXMLOutputter(Format format) {
 return new XMLOutputter(format);
 }

 private XMLOutputter createXMLOutputter(XMLOutputProcessor processor) {
 return new XMLOutputter(processor);
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlo = createXMLOutputter(new Format());
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlo = createXMLOutputter(new DefaultXMLProcessor());
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = createXMLOutputter(new Format(), new DefaultXMLProcessor());
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = createXMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
 xmlo.setFormat(null);
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
 xmlo.setFormat(format.clone());
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
}

 @Test
 public void testGetFormat() {
 XMLOutputter xmlo = createXMLOutputter();
 Format format = xmlo.getFormat();
 assertNull(format);
 xmlo.setFormat(new Format());
 format = xmlo.getFormat();
 assertNotNull(format);
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
}

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 XMLOutputProcessor processor = xmlo.getXMLOutputProcessor();
 assertNull(processor);
 xmlo.setXMLOutputProcessor(new DefaultXMLProcessor());
 processor = xmlo.getXMLOutputProcessor();
 assertNotNull(processor);
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
}

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 XMLOutputProcessor processor = new DefaultXMLProcessor();
 xmlo.setXMLOutputProcessor(processor);
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
 xmlo.setXMLOutputProcessor(null);
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
 xmlo.setXMLOutputProcessor(processor.clone());
 assertEquals("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = [\r,\n], textMode = text]", xmlo.toString());
}

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(doc, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<root\\s[^>]*>.*</root>$"));
}

 @Test
 public void testOutputDocumentWithFormat() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Format format = new Format();
 XMLOutputter xmlo = createXMLOutputter(format);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(doc, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<root\\s[^>]*>.*</root>$"));
 assertNotEquals(xmlString, xmlo.outputString(doc));
}

 @Test
 public void testOutputDocumentWithEmptyDocument() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(doc, baos);
 String xmlString = baos.toString();
 assertEquals("", xmlString);
}

 @Test (expected = IOException.class)
 public void testOutputDocumentWithNullDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(null, baos);
 fail("IOException expected");
}

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = new JDOMFactory().newDocType("root", new QName("http://test.com", "root"), null);
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(doctype, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<!DOCTYPE root [^>]*>$"));
}

 @Test
 public void testOutputElement() throws IOException {
 Element element = new JDOMFactory().newElement("root");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(element, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<root[^>]*>.*</root>$"));
}

 @Test
 public void testOutputElementWithAttributes() throws IOException {
 Element element = new JDOMFactory().newElement("root");
 element.setAttribute("attr1", "value1");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(element, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<root[^>]* attr1=[^>]*=\"value1\"[^>]*>.*</root>$"));
}

 @Test
 public void testOutputElementWithEmptyText() throws IOException {
 Element element = new JDOMFactory().newElement("root");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.outputElementContent(element, baos);
 String xmlString = baos.toString();
 assertEquals("", xmlString);
}

 @Test
 public void testOutputElementWithText() throws IOException {
 Element element = new JDOMFactory().newElement("root", new Text("text"));
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.outputElementContent(element, baos);
 String xmlString = baos.toString();
 assertEquals("text", xmlString);
}

 @Test
 public void testOutputElementWithTextAndChildren() throws IOException {
 Element root = new JDOMFactory().newElement("root");
 Element child1 = new JDOMFactory().newElement("child1");
 root.addContent(child1);
 Text text = new Text("text");
 child1.addContent(text);
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(root, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<root[^>]*>.*<child1[^>]*>text.*</child1>.*</root>$"));
}

 @Test
 public void testOutputListOfContent() throws IOException {
 List<Content> list = new ArrayList<>();
 list.add(new Text("text1"));
 list.add(new Text("text2"));
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(list, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<root[^>]*>.*<text>text1</text>.*<text>text2</text>.*</root>$"));
}

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new JDOMFactory().newCDATA("text");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(cdata, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<![CDATA[text]]>$"));
}

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("text");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(text, baos);
 String xmlString = baos.toString();
 assertEquals("text", xmlString);
}

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new JDOMFactory().newComment("comment");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(comment, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<!--comment-->$"));
}

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new JDOMFactory().newProcessingInstruction("target", "data");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(pi, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^<\\?target [^>]*=['\"]data['\"]\\?>$"));
}

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new JDOMFactory().newEntityRef("#external-xml-declaration");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 xmlo.output(entityRef, baos);
 String xmlString = baos.toString();
 assertTrue(xmlString.matches("^&[a-zA-Z]+;$"));
}

 @Test
 public void testOutputElementContentString() throws IOException {
 Element element = new JDOMFactory().newElement("root", new Text("text"));
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputElementContentString(element);
 assertEquals("text", xmlString);
}

 @Test
 public void testEscapeAttributeEntities() {
 String str = "<root attr1=\"<tag>value1&amp;value2&quot;\" attr2='value3\tvalue4'/>";
 String escapedStr = XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(str, new Format());
 assertEquals("<root attr1=\"&lt;tag&gt;value1&amp;value2&quot;\" attr2='value3	value4'/>", escapedStr);
}

 @Test
 public void testEscapeElementEntities() {
 String str = "<root><tag>value1&amp;value2&quot;</tag></root>";
 String escapedStr = XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(str, new Format());
 assertEquals("<root><tag>&lt;tag&gt;value1&amp;value2&quot;</tag></root>", escapedStr);
}

}