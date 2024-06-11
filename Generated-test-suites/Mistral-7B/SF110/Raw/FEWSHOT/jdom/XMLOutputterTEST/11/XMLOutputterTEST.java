package org.jdom2.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testXMLOutputterConstruction() {
        // Test default constructor
        XMLOutputter outputter = new XMLOutputter();
        assertNull(outputter.getFormat());
        assertNull(outputter.getXMLOutputProcessor());

        // Test constructor with Format and XMLOutputProcessor
        Format format = new Format();
        XMLOutputProcessor processor = new StaxOutputter();
        XMLOutputter outputter1 = new XMLOutputter(format, processor);
        assertEquals(format, outputter1.getFormat());
        assertEquals(processor, outputter1.getXMLOutputProcessor());

        // Test constructor with XMLOutputProcessor
        XMLOutputter outputter2 = new XMLOutputter(processor);
        assertEquals(Format.getRawFormat(), outputter2.getFormat());
        assertEquals(processor, outputter2.getXMLOutputProcessor());

        // Test constructor with Format
        Format format1 = new Format();
        XMLOutputter outputter3 = new XMLOutputter(format1);
        assertEquals(format1, outputter3.getFormat());
        assertNull(outputter3.getXMLOutputProcessor());

        // Test constructor with XMLOutputter
        XMLOutputter outputter4 = new XMLOutputter(outputter1);
        assertEquals(outputter1.getFormat(), outputter4.getFormat());
        assertEquals(outputter1.getXMLOutputProcessor(), outputter4.getXMLOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = new Format();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = new StaxOutputter();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        assertNull(outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new JDOMFactory().createDocument();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(doc, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.length() > 0);
    }

    @Test
    public void testOutputDocType() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        DocType docType = new JDOMFactory().createDocType("html", null, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(docType, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.length() > 0);
    }

    @Test
    public void testOutputElement() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(element, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.length() > 0);
    }

    @Test
    public void testOutputElementContent() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        Text text = new JDOMFactory().createText("Hello World!");
        element.addContent(text);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.outputElementContent(element, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.contains("Hello World!"));
    }

    @Test
    public void testOutputList() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        Text text1 = new JDOMFactory().createText("Hello");
        Text text2 = new JDOMFactory().createText("World!");
        List<Content> list = new ArrayList<>();
        list.add(text1);
        list.add(text2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(list, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.length() > 0);
        assertTrue(xmlString.contains("Hello"));
        assertTrue(xmlString.contains("World!"));
    }

    @Test
    public void testOutputCDATA() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        CDATA cdata = new JDOMFactory().createCDATA("Hello World!");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(cdata, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.contains("<![CDATA[Hello World!]]>"));
    }

    @Test
    public void testOutputText() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Text text = new JDOMFactory().createText("Hello World!");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(text, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.contains("Hello World!"));
    }

    @Test
    public void testOutputComment() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Comment comment = new JDOMFactory().createComment("Hello World!");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(comment, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.contains("<!--Hello World!-->"));
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        ProcessingInstruction pi = new JDOMFactory().createProcessingInstruction("xml", "version=\"1.0\"");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(pi, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.contains("<?xml version=\"1.0\"?>"));
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        EntityRef entityRef = new JDOMFactory().createEntityRef("nbsp");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(entityRef, byteArrayOutputStream);
        String xmlString = byteArrayOutputStream.toString("UTF-8");
        assertTrue(xmlString.contains("&#160;"));
    }

    @Test
    public void testOutputStringDocument() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new JDOMFactory().createDocument();
        String xmlString = outputter.outputString(doc);
        assertTrue(xmlString.length() > 0);
    }

    @Test
    public void testOutputStringDocType() {
        XMLOutputter outputter = new XMLOutputter();
        DocType doctype = new JDOMFactory().createDocType("html", null, null);
        String xmlString = outputter.outputString(doctype);
        assertTrue(xmlString.length() > 0);
    }

    @Test
    public void testOutputStringElement() {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        String xmlString = outputter.outputString(element);
        assertTrue(xmlString.length() > 0);
    }

    @Test
    public void testOutputStringList() {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        Text text1 = new JDOMFactory().createText("Hello");
        Text text2 = new JDOMFactory().createText("World!");
        List<Content> list = new ArrayList<>();
        list.add(text1);
        list.add(text2);
        element.addContent(list);
        String xmlString = outputter.outputString(element);
        assertTrue(xmlString.length() > 0);
        assertTrue(xmlString.contains("Hello"));
        assertTrue(xmlString.contains("World!"));
    }

    @Test
    public void testOutputStringCDATA() {
        XMLOutputter outputter = new XMLOutputter();
        CDATA cdata = new JDOMFactory().createCDATA("Hello World!");
        String xmlString = outputter.outputString(cdata);
        assertTrue(xmlString.contains("<![CDATA[Hello World!]]>"));
    }

    @Test
    public void testOutputStringText() {
        XMLOutputter outputter = new XMLOutputter();
        Text text = new JDOMFactory().createText("Hello World!");
        String xmlString = outputter.outputString(text);
        assertTrue(xmlString.contains("Hello World!"));
    }

    @Test
    public void testOutputStringComment() {
        XMLOutputter outputter = new XMLOutputter();
        Comment comment = new JDOMFactory().createComment("Hello World!");
        String xmlString = outputter.outputString(comment);
        assertTrue(xmlString.contains("<!--Hello World!-->"));
    }

    @Test
    public void testOutputStringProcessingInstruction() {
        XMLOutputter outputter = new XMLOutputter();
        ProcessingInstruction pi = new JDOMFactory().createProcessingInstruction("xml", "version=\"1.0\"");
        String xmlString = outputter.outputString(pi);
        assertTrue(xmlString.contains("<?xml version=\"1.0\"?>"));
    }

    @Test
    public void testOutputStringEntityRef() {
        XMLOutputter outputter = new XMLOutputter();
        EntityRef entityRef = new JDOMFactory().createEntityRef("nbsp");
        String xmlString = outputter.outputString(entityRef);
        assertTrue(xmlString.contains("&#160;"));
    }

    @Test
    public void testOutputElementContentString() {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        Text text = new JDOMFactory().createText("Hello World!");
        element.addContent(text);
        String xmlString = outputter.outputElementContentString(element);
        assertTrue(xmlString.contains("Hello World!"));
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String input = "Hello <world>!";
        String expectedOutput = "Hello &lt;world&gt;!";
        String output = outputter.escapeAttributeEntities(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String input = "<h1>Hello <world>!</h1>";
        String expectedOutput = "<h1>Hello &lt;world&gt;!</h1>";
        String output = outputter.escapeElementEntities(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputter clone = outputter.clone();
        assertSame(outputter.getFormat(), clone.getFormat());
        assertSame(outputter.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
    }

    @Test
    public void testToString() {
        XMLOutputter outputter = new XMLOutputter();
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = [\r\n], mode = TEXT]");
        String output = outputter.toString();
        assertEquals(expectedOutput.toString(), output);
    }
}