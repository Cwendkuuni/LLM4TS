import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

 private static final String XML_FILE_CONTENT = "<root><element1>Element 1 Content</element1><element2><subElement1>Sub Element 1 Content</subElement1></element2></root>";

 @Test
 public void testXMLOutputter_ValidXML_OutputString() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 XMLOutputter outputter = new XMLOutputter();
 String expectedOutput = XML_FILE_CONTENT;
 String actualOutput = outputter.outputString(doc);
 Assert.assertEquals("Output string should be equal to the input XML", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputOutputStream() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(doc, outWriter);
 String expectedOutput = XML_FILE_CONTENT;
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the input XML", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputElement() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 Element root = doc.getRootElement();
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(root, outWriter);
 String expectedOutput = "<root><element1>Element 1 Content</element1><element2><subElement1>Sub Element 1 Content</subElement1></element2></root>";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the input XML", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputElementContent() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 Element root = doc.getRootElement();
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.outputElementContent(root, outWriter);
 String expectedOutput = "<element1>Element 1 Content</element1><element2><subElement1>Sub Element 1 Content</subElement1></element2>";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputList() throws Exception {
 List<Element> elements = new ArrayList<>();
 elements.add(new Element("element1", "Element 1 Content"));
 elements.add(new Element("element2", new Element("subElement1", "Sub Element 1 Content")));
 Document doc = new Document(elements);
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(elements, outWriter);
 String expectedOutput = "<element1>Element 1 Content</element1><element2><subElement1>Sub Element 1 Content</subElement1></element2>";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputCDATA() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 CDATA cdata = new CDATA("<![CDATA[Content of the CDATA section]]>");
 doc.getRootElement().addContent(cdata);
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(cdata, outWriter);
 String expectedOutput = "<![CDATA[Content of the CDATA section]]>";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputText() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 Text text = doc.getRootElement().getText();
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(text, outWriter);
 String expectedOutput = XML_FILE_CONTENT;
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the input XML", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputComment() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 Comment comment = new Comment("This is a comment.");
 doc.getRootElement().addContent(comment);
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(comment, outWriter);
 String expectedOutput = "<!--This is a comment.-->";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputProcessingInstruction() throws Exception {
 Document doc = JDOMFactory.stringParse(XML_FILE_CONTENT);
 ProcessingInstruction pi = new ProcessingInstruction("xml-mapping", "External DTD file: dtd.xml");
 doc.getRootElement().addContent(pi);
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(pi, outWriter);
 String expectedOutput = "<xml-mapping SYSTEM=\"dtd.xml\">";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputEntityRef() throws Exception {
 Document doc = new Document(new Element("root", new Element("entityRef", new EntityRef("MyEntity"))));
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(doc, outWriter);
 String expectedOutput = "<root><entityRef entity=\"MyEntity\"/></root>";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_ValidXML_OutputEntityRefContent() throws Exception {
 Document doc = new Document(new Element("root", new Element("entityRef", new EntityRef("MyEntity"))));
 EntityRef entityRef = doc.getRootElement().getChild("entityRef").getContent().getValue();
 XMLOutputter outputter = new XMLOutputter();
 StringWriter outWriter = new StringWriter();
 outputter.output(entityRef, outWriter);
 String expectedOutput = "MyEntity";
 String actualOutput = outWriter.toString();
 Assert.assertEquals("Output string should be equal to the expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testXMLOutputter_Format_DefaultValues() throws Exception {
 XMLOutputter outputter = new XMLOutputter();
 Format format = outputter.getFormat();
 Assert.assertFalse("omitDeclaration should be false by default", format.omitDeclaration);
 Assert.assertEquals("encoding should be null by default", null, format.encoding);
 Assert.assertFalse("omitEncoding should be false by default", format.omitEncoding);
 Assert.assertEquals("indent should be empty by default", "", format.indent);
 Assert.assertTrue("expandEmptyElements should be true by default", format.expandEmptyElements);
 Map<String, String> lineSeparatorMap = new HashMap<>();
 lineSeparatorMap.put("line.separator", "\r\n");
 Assert.assertEquals("lineSeparator should be a System property by default", lineSeparatorMap, format.getLineSeparatorMap());
 }

 @Test
 public void testXMLOutputter_Format_CustomValues() throws Exception {
 XMLOutputter outputter = new XMLOutputter(new Format("MyFormat"));
 outputter.setFormat(new Format("MyFormat", true, "UTF-8", true, "MyIndent", false, null));
 Format format = outputter.getFormat();
 Assert.assertEquals("Format name should be 'MyFormat'", "MyFormat", format.getName());
 Assert.assertTrue("omitDeclaration should be true", format.omitDeclaration);
 Assert.assertEquals("encoding should be 'UTF-8'", "UTF-8", format.encoding);
 Assert.assertTrue("omitEncoding should be true", format.omitEncoding);
 Assert.assertEquals("indent should be 'MyIndent'", "MyIndent", format.indent);
 Assert.assertFalse("expandEmptyElements should be false", format.expandEmptyElements);
 }

 @Test
 public void testXMLOutputter_Format_Clone() throws Exception {
 XMLOutputter outputter = new XMLOutputter(new Format("MyFormat"));
 outputter.setFormat(new Format("MyFormat", true, "UTF-8", true, "MyIndent", false, null));
 XMLOutputter clonedOutputter = outputter.clone();
 Format originalFormat = outputter.getFormat();
 Format clonedFormat = clonedOutputter.getFormat();
 Assert.assertNotSame("Cloned Format should not be the same as the original Format", originalFormat, clonedFormat);
 Assert.assertEquals("Format names should be equal", originalFormat.getName(), clonedFormat.getName());
 Assert.assertEquals("Format properties should be equal", originalFormat.clone(), clonedFormat.clone());
 }

 @Test
 public void testXMLOutputter_Format_ToString() throws Exception {
 XMLOutputter outputter = new XMLOutputter();
 Format format = outputter.getFormat();
 String expectedOutput = "XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = true]";
 String actualOutput = outputter.toString();
 Assert.assertEquals("ToString method should return the expected output", expectedOutput, actualOutput);
 }
}