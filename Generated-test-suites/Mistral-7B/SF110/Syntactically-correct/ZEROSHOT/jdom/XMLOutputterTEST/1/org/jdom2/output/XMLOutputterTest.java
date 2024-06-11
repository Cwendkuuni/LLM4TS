import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 @Test
 public void testXMLOutputterWithDefaultFormatAndNullProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new Document();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try {
 xmlo.output(doc, outputStream);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputStream.toString();
 String expectedXml = "<document/>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithCustomFormatAndNullProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 Format customFormat = new Format();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try {
 xmlo.output(new Document(), outputStream, customFormat);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputStream.toString();
 String expectedXml = "<document/>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithDefaultFormatAndCustomProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 XMLOutputProcessor customProcessor = new DefaultXMLProcessor();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try {
 xmlo.setXMLOutputProcessor(customProcessor);
 Document doc = new Document();
 xmlo.output(doc, outputStream);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputStream.toString();
 String expectedXml = "<document/>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithCustomFormatAndCustomProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 Format customFormat = new Format();
 XMLOutputProcessor customProcessor = new DefaultXMLProcessor();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try {
 xmlo.setFormat(customFormat);
 xmlo.setXMLOutputProcessor(customProcessor);
 Document doc = new Document();
 xmlo.output(doc, outputStream);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputStream.toString();
 String expectedXml = "<document/>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputDocument() {
 XMLOutputter xmlo = createXMLOutputter();
 Document inputDoc = new Document(new JDOMParser().parse(getClass().getResource("/input.xml")));
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(inputDoc, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "<root><child>test</child></root>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputDocType() {
 XMLOutputter xmlo = createXMLOutputter();
 DocType docType = new DocType("root", null, null, null, null);
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(docType, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "<!DOCTYPE root [ ]>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputElement() {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("root").addContent(new Element("child").setText("test"));
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(element, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "<root><child>test</child></root>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputElementContent() {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("root").addContent(new Text("test"));
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.outputElementContent(element, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "test";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputList() {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 List<Content> elements = new ArrayList<>();
 elements.add(new Element("child1").setText("test1"));
 elements.add(new Element("child2").setText("test2"));
 root.addContent(elements);
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(elements, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "<root><child1>test1</child1><child2>test2</child2></root>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputCDATA() {
 XMLOutputter xmlo = createXMLOutputter();
 CDATA cdata = new CDATA("<![CDATA[test]]>");
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(cdata, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "<![CDATA[test]]>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputText() {
 XMLOutputter xmlo = createXMLOutputter();
 Text text = new Text("test");
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(text, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "test";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputComment() {
 XMLOutputter xmlo = createXMLOutputter();
 Comment comment = new Comment("test");
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(comment, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "<!--test-->";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputProcessingInstruction() {
 XMLOutputter xmlo = createXMLOutputter();
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version='1.0'");
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(pi, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "<?xml version='1.0'?>";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputEntityRef() {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entityRef = new EntityRef(" entities.xml#entity1");
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.output(entityRef, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "&entity1;";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithInputEntityRefWithCustomProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entityRef = new EntityRef(" entities.xml#entity1");
 XMLOutputProcessor customProcessor = new DefaultXMLProcessor();
 StringWriter outputWriter = new StringWriter();
 try {
 xmlo.setXMLOutputProcessor(customProcessor);
 xmlo.output(entityRef, outputWriter);
 } catch (IOException e) {
 // IOException should not be thrown
 e.printStackTrace();
 Assert.fail("IOException should not be thrown");
 }
 String actualXml = outputWriter.toString();
 String expectedXml = "&entity1;";
 Assert.assertEquals("XML generated is not as expected", expectedXml, actualXml);
 }

 @Test
 public void testEscapeAttributeEntities() {
 XMLOutputter xmlo = createXMLOutputter();
 String inputString = "<test attr='<tag>value' />";
 String expectedOutputString = "<test attr='&lt;tag&gt;value' />";
 String actualOutputString = xmlo.escapeAttributeEntities(inputString);
 Assert.assertEquals("Attribute entities escaping is not as expected", expectedOutputString, actualOutputString);
 }

 @Test
 public void testEscapeElementEntities() {
 XMLOutputter xmlo = createXMLOutputter();
 String inputString = "<test>&lt;tag&gt;</test>";
 String expectedOutputString = "<test>&lt;tag&gt;</test>";
 String actualOutputString = xmlo.escapeElementEntities(inputString, null);
 Assert.assertEquals("Element entities escaping is not as expected", expectedOutputString, actualOutputString);

 String inputStringWithFormat = "<test>&lt;tag&gt;</test>";
 String format = "<root><test>&amp;lt;tag&amp;gt;</test></root>";
 String expectedOutputStringWithFormat = format;
 String actualOutputStringWithFormat = xmlo.escapeElementEntities(inputString, new Format(format));
 Assert.assertEquals("Element entities escaping with format is not as expected", expectedOutputStringWithFormat, actualOutputStringWithFormat);
 }

 @Test
 public void testClone() {
 XMLOutputter original = createXMLOutputter();
 XMLOutputter cloned = original.clone();
 Format format = new Format();
 Assert.assertNotSame(original, cloned);
 Assert.assertEquals(original.getFormat(), cloned.getFormat());
 Assert.assertEquals(original.getXMLOutputProcessor(), cloned.getXMLOutputProcessor());
 cloned.setFormat(new Format());
 cloned.setXMLOutputProcessor(new DefaultXMLProcessor());
 Assert.assertNotEquals(original.getFormat(), cloned.getFormat());
 Assert.assertNotEquals(original.getXMLOutputProcessor(), cloned.getXMLOutputProcessor());
 }

}