package org.jdom2.output;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
import org.jdom2.test.JDOMTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 @Test
 public void testOutputDocument() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 xmloutputter.output(document, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputDocument.xml");
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputDocType() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 xmloutputter.output(document.getRootElement().getDocument().getDoctype(), outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputDocType.xml");
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputElement() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final Element rootElement = document.getRootElement();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 xmloutputter.output(rootElement, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputElement.xml");
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputElementContent() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final Element rootElement = document.getRootElement();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 xmloutputter.outputElementContent(rootElement, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputElementContent.xml");
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputList() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final Element rootElement = document.getRootElement();
 final List<Element> list = new ArrayList<>();
 list.add(new Element("item1"));
 list.add(new Element("item2"));
 rootElement.addContent(list);
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 xmloutputter.output(list, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputList.xml");
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputCDATA() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 final CDATA cdata = new CDATA("testCDATA");
 xmloutputter.output(cdata, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = "<![CDATA[testCDATA]]>";
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputText() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 final Text text = document.getRootElement().getTextTrim();
 xmloutputter.output(text, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = document.getRootElement().getTextTrim();
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputComment() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 final Comment comment = new Comment("testComment");
 xmloutputter.output(comment, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = "<!--testComment-->";
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 final ProcessingInstruction pi = new ProcessingInstruction("xml", "version='1.0'");
 xmloutputter.output(pi, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = "<?xml version='1.0'?>";
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputEntityRef() throws IOException, JDOMException {
 final Document document = JDOMTestUtils.createTestDocument();
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final XMLOutputter xmloutputter = createXMLOutputter();
 final EntityRef entityRef = new EntityRef("testEntity");
 xmloutputter.output(entityRef, outputStream);
 final String actualOutput = outputStream.toString();
 final String expectedOutput = "&testEntity;";
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringDocument() throws IOException {
 final Document document = JDOMTestUtils.createTestDocument();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputDocument.xml");
 final XMLOutputter xmloutputter = createXMLOutputter();
 final String actualOutput = xmloutputter.outputString(document);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringDocType() throws IOException {
 final Document document = JDOMTestUtils.createTestDocument();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputDocType.xml");
 final XMLOutputter xmloutputter = createXMLOutputter();
 final String actualOutput = xmloutputter.outputString(document.getRootElement().getDocument().getDoctype());
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringElement() throws IOException {
 final Document document = JDOMTestUtils.createTestDocument();
 final Element rootElement = document.getRootElement();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputElement.xml");
 final XMLOutputter xmloutputter = createXMLOutputter();
 final String actualOutput = xmloutputter.outputString(rootElement);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputStringElementContent() throws IOException {
 final Document document = JDOMTestUtils.createTestDocument();
 final Element rootElement = document.getRootElement();
 final String expectedOutput = JDOMTestUtils.getExpectedXML("outputElementContent.xml");
 final XMLOutputter xmloutputter = createXMLOutputter();
 final String actualOutput = xmloutputter.outputElementContentString(rootElement);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEscapeAttributeEntities() {
 final XMLOutputter xmloutputter = createXMLOutputter();
 final Map<String, String> inputValues = new HashMap<>();
 inputValues.put("test1", "<test>");
 inputValues.put("test2", "&test;");
 inputValues.put("test3", "\"test\"");
 inputValues.put("test4", "'test'");
 inputValues.put("test5", "&lt;test&gt;");
 inputValues.put("test6", "&amp;test;");
 for (Map.Entry<String, String> entry : inputValues.entrySet()) {
 final String input = entry.getKey();
 final String expectedOutput = entry.getValue();
 final String actualOutput = xmloutputter.escapeAttributeEntities(input);
 Assert.assertEquals(expectedOutput, actualOutput);
 }
 }

 @Test
 public void testEscapeElementEntities() {
 final XMLOutputter xmloutputter = createXMLOutputter();
 final Map<String, String> inputValues = new HashMap<>();
 inputValues.put("test1", "<test1>");
 inputValues.put("test2", "&lt;test2&gt;");
 inputValues.put("test3", "&amp;test3;");
 inputValues.put("test4", "&quot;test4&quot;");
 inputValues.put("test5", "&apos;test5&apos;");
 inputValues.put("test6", "&gt;test6&gt;");
 for (Map.Entry<String, String> entry : inputValues.entrySet()) {
 final String input = entry.getKey();
 final String expectedOutput = entry.getValue();
 final String actualOutput = xmloutputter.escapeElementEntities(input, null);
 Assert.assertEquals(expectedOutput, actualOutput);
 }
 }
}