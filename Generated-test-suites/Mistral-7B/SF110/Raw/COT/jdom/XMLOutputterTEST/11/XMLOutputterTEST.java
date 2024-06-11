package org.jdom2.output; // Import necessary packages here

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

    private static Document doc;
    private static Element root;
    private static OutputStream outputStream;

    @BeforeClass
    public static void setUp() throws Exception {
        // Create a sample JDOM document for testing
        DocumentBuilder documentBuilder = new DocumentBuilder();
        doc = documentBuilder.buildDocument();
        root = doc.getRootElement();
        root.addContent(new Element("element1").setText("Test Content 1"));
        root.addContent(new Element("element2").setText("Test Content 2"));

        outputStream = new MemoryOutputStream();
    }

    

    // Test constructor with no arguments
    @Test
    public void testXMLOutputterNoArgs() {
        XMLOutputter xmlo = new XMLOutputter();
        assertNotNull(xmlo);
    }

    // Test constructor with Format argument
    @Test
    public void testXMLOutputterFormat() {
        XMLOutputter xmlo = new XMLOutputter(new Format());
        assertNotNull(xmlo);
    }

    // Test constructor with XMLOutputProcessor argument
    @Test
    public void testXMLOutputterXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter xmlo = new XMLOutputter(processor);
        assertNotNull(xmlo);
    }

    // Test setFormat method
    @Test
    public void testSetFormat() {
        XMLOutputter xmlo = new XMLOutputter();
        Format format = new Format();
        xmlo.setFormat(format);
        assertEquals(format, xmlo.getFormat());
    }

    // Test setXMLOutputProcessor method
    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter xmlo = new XMLOutputter();
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        xmlo.setXMLOutputProcessor(processor);
        assertEquals(processor, xmlo.getXMLOutputProcessor());
    }

    // Test output method with Document argument
    @Test
    public void testOutputDocument() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(doc, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<root>"));
        assertTrue(xmlString.contains("<element1>Test Content 1</element1>"));
        assertTrue(xmlString.contains("<element2>Test Content 2</element2>"));
    }

    // Test output method with DocType argument
    @Test
    public void testOutputDocType() throws IOException {
        Element docType = new Element("docType");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(docType, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<!DOCTYPE docType>"));
    }

    // Test output method with Element argument
    @Test
    public void testOutputElement() throws IOException {
        Element element = root.getChild("element1");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(element, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<element1>Test Content 1</element1>"));
    }

    // Test outputElementContent method with Element argument
    @Test
    public void testOutputElementContent() throws IOException {
        Element element = root.getChild("element1");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.outputElementContent(element, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("Test Content 1"));
    }

    // Test output method with List argument
    @Test
    public void testOutputList() throws IOException {
        List<Element> list = new ArrayList<>();
        Element element1 = new Element("element1").setText("Test Content 1");
        Element element2 = new Element("element2").setText("Test Content 2");
        list.add(element1);
        list.add(element2);

        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(list, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<element1>Test Content 1</element1>"));
        assertTrue(xmlString.contains("<element2>Test Content 2</element2>"));
    }

    // Test output method with CDATA argument
    @Test
    public void testOutputCDATA() throws IOException {
        CDATA cdata = new CDATA("<![CDATA[Test CDATA]]>");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(cdata, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<![CDATA[Test CDATA]]>"));
    }

    // Test output method with Text argument
    @Test
    public void testOutputText() throws IOException {
        Text text = new Text("Test Text");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(text, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("Test Text"));
    }

    // Test output method with Comment argument
    @Test
    public void testOutputComment() throws IOException {
        Comment comment = new Comment("Test Comment");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(comment, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<!--Test Comment-->"));
    }

    // Test output method with ProcessingInstruction argument
    @Test
    public void testOutputProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0' encoding='UTF-8'");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(pi, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<?xml version='1.0' encoding='UTF-8'?>"));
    }

    // Test output method with EntityRef argument
    @Test
    public void testOutputEntityRef() throws IOException {
        EntityRef entityRef = new EntityRef("entity1");
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(entityRef, outputStream);
        String xmlString = new String(outputStream.toByteArray());
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("&entity1;"));
    }

    // Test outputString method with Document argument
    @Test
    public void testOutputStringDocument() {
        XMLOutputter xmlo = new XMLOutputter();
        String xmlString = xmlo.outputString(doc);
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<root>"));
        assertTrue(xmlString.contains("<element1>Test Content 1</element1>"));
        assertTrue(xmlString.contains("<element2>Test Content 2</element2>"));
    }

    // Test outputString method with DocType argument
    @Test
    public void testOutputStringDocType() {
        XMLOutputter xmlo = new XMLOutputter();
        String xmlString = xmlo.outputString(new DocType("doctype", null));
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<!DOCTYPE doctype>"));
    }

    // Test outputString method with Element argument
    @Test
    public void testOutputStringElement() {
        XMLOutputter xmlo = new XMLOutputter();
        String xmlString = xmlo.outputString(root.getChild("element1"));
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<element1>Test Content 1</element1>"));
    }

    // Test outputString method with List argument
    @Test
    public void testOutputStringList() {
        XMLOutputter xmlo = new XMLOutputter();
        List<Element> list = new ArrayList<>();
        Element element1 = new Element("element1").setText("Test Content 1");
        Element element2 = new Element("element2").setText("Test Content 2");
        list.add(element1);
        list.add(element2);
        String xmlString = xmlo.outputString(list);
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<element1>Test Content 1</element1>"));
        assertTrue(xmlString.contains("<element2>Test Content 2</element2>"));
    }

    // Test outputString method with CDATA argument
    @Test
    public void testOutputStringCDATA() {
        XMLOutputter xmlo = new XMLOutputter();
        CDATA cdata = new CDATA("<![CDATA[Test CDATA]]>");
        String xmlString = xmlo.outputString(cdata);
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<![CDATA[Test CDATA]]>"));
    }

    // Test outputString method with Text argument
    @Test
    public void testOutputStringText() {
        XMLOutputter xmlo = new XMLOutputter();
        Text text = new Text("Test Text");
        String xmlString = xmlo.outputString(text);
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("Test Text"));
    }

    // Test outputString method with Comment argument
    @Test
    public void testOutputStringComment() {
        XMLOutputter xmlo = new XMLOutputter();
        Comment comment = new Comment("Test Comment");
        String xmlString = xmlo.outputString(comment);
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<!--Test Comment-->"));
    }

    // Test outputString method with ProcessingInstruction argument
    @Test
    public void testOutputStringProcessingInstruction() {
        XMLOutputter xmlo = new XMLOutputter();
        ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0' encoding='UTF-8'");
        String xmlString = xmlo.outputString(pi);
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<?xml version='1.0' encoding='UTF-8'?>"));
    }

    // Test outputString method with EntityRef argument
    @Test
    public void testOutputStringEntityRef() {
        XMLOutputter xmlo = new XMLOutputter();
        EntityRef entityRef = new EntityRef("entity1");
        String xmlString = xmlo.outputString(entityRef);
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("&entity1;"));
    }

    // Test outputElementContentString method with Element argument
    @Test
    public void testOutputElementContentStringElement() {
        XMLOutputter xmlo = new XMLOutputter();
        String xmlString = xmlo.outputElementContentString(root.getChild("element1"));
        assertNotNull(xmlString);
        assertTrue(xmlString.contains("Test Content 1"));
    }

    // Test escapeAttributeEntities and escapeElementEntities methods
    @Test
    public void testEscapeEntities() {
        XMLOutputter xmlo = new XMLOutputter();
        String input = "<root><element attr=\"< & >\"></element></root>";
        String expectedOutput = "<root><element attr=\"&lt; &amp; &gt;</element></root>";
        String output = xmlo.escapeAttributeEntities(input, new Format());
        assertEquals(expectedOutput, output);

        String inputElement = "<root><element>&< & ></element></root>";
        String expectedOutputElement = "<root><element>&amp; &lt; &gt;</element></root>";
        String outputElement = xmlo.escapeElementEntities(inputElement, new Format());
        assertEquals(expectedOutputElement, outputElement);
    }

    // Test clone method
    @Test
    public void testClone() {
        XMLOutputter xmlo = new XMLOutputter();
        XMLOutputter clonedXmlo = xmlo.clone();
        assertEquals(xmlo, clonedXmlo);
    }

    // Test toString method
    @Test
    public void testToString() {
        XMLOutputter xmlo = new XMLOutputter();
        String expectedOutput = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = , expandEmptyElements = true]";
        String output = xmlo.toString();
        assertEquals(expectedOutput, output);
    }

    // Test Exception handling when OutputStream is null
    @Test(expected = IllegalArgumentException.class)
    public void testOutputNullOutputStream() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(doc, null);
    }

    // Test Exception handling when Document is null
    @Test(expected = IllegalArgumentException.class)
    public void testOutputNullDocument() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.output(null, outputStream);
    }

    // Test Exception handling when XMLOutputProcessor is null
    @Test(expected = IllegalArgumentException.class)
    public void testSetXMLOutputProcessorNullProcessor() {
        XMLOutputter xmlo = new XMLOutputter();
        xmlo.setXMLOutputProcessor(null);
    }

    
}