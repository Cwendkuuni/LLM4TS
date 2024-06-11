package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

    @Test
    public void testXMLOutputter() {
        final XMLOutputter outputter = new XMLOutputter();
        assertNull(outputter.getFormat());
        assertNull(outputter.getXMLOutputProcessor());

        final XMLOutputter formatOutputter = new XMLOutputter(Format.getRawFormat());
        assertEquals(Format.getRawFormat(), formatOutputter.getFormat());
        assertNull(formatOutputter.getXMLOutputProcessor());

        final XMLOutputter processorOutputter = new XMLOutputter(null, new XMLOutputProcessor() {});
        assertNull(processorOutputter.getFormat());
        assertNotNull(processorOutputter.getXMLOutputProcessor());

        final XMLOutputter formatAndProcessorOutputter = new XMLOutputter(Format.getRawFormat(), new XMLOutputProcessor());
        assertEquals(Format.getRawFormat(), formatAndProcessorOutputter.getFormat());
        assertNotNull(formatAndProcessorOutputter.getXMLOutputProcessor());
    }

    @Test
    public void testSetterMethods() {
        final XMLOutputter outputter = new XMLOutputter();
        final Format format = new Format();
        final XMLOutputProcessor processor = new XMLOutputProcessor();

        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());

        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(document, outputStream);

        final String xml = outputStream.toString();
        assertTrue(xml.isEmpty() || xml.matches("^<\\?xml[^>]*>.*$"));
    }

    @Test
    public void testOutputDocType() throws IOException {
        final DocType docType = new JDOMFactory().getDocumentBuilder().getDocType();
        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(docType, outputStream);

        final String xml = outputStream.toString();
        assertTrue(xml.isEmpty() || xml.matches("^<!DOCTYPE[^>]*>$"));
    }

    @Test
    public void testOutputElement() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final Element root = document.getRootElement();
        root.addContent(new Element("child"));

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(root, outputStream);

        final String xml = outputStream.toString();
        assertTrue(xml.contains("<child></child>"));
    }

    @Test
    public void testOutputElementContent() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final Element root = document.getRootElement();
        root.setText("content");

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.outputElementContent(root, outputStream);

        final String xml = outputStream.toString();
        assertEquals("content", xml);
    }

    @Test
    public void testOutputList() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final Element root = document.getRootElement();
        root.addContent(new Element("child1"));
        root.addContent(new Element("child2"));

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(root, outputStream);

        final String xml = outputStream.toString();
        assertTrue(xml.contains("<child1></child1>"));
        assertTrue(xml.contains("<child2></child2>"));
    }

    @Test
    public void testOutputCDATA() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final Element root = document.getRootElement();
        root.addContent(new CDATA("<![CDATA[content]]]>"));

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(root, outputStream);

        final String xml = outputStream.toString();
        assertEquals("<![CDATA[content]]>", xml);
    }

    @Test
    public void testOutputText() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final Element root = document.getRootElement();
        root.setText("content");

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(root, outputStream);

        final String xml = outputStream.toString();
        assertEquals("content", xml);
    }

    @Test
    public void testOutputComment() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final Element root = document.getRootElement();
        root.addContent(new Comment("comment"));

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(root, outputStream);

        final String xml = outputStream.toString();
        assertTrue(xml.contains("<!--comment-->"));
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final ProcessingInstruction pi = new ProcessingInstruction("xml-default", "data");
        document.addContent(pi);

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(document, outputStream);

        final String xml = outputStream.toString();
        assertTrue(xml.contains("<?xml-default data?>"));
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        final Document document = new JDOMFactory().createDocument();
        final EntityRef entityRef = new EntityRef("entity");
        document.addContent(entityRef);

        final XMLOutputter outputter = new XMLOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(document, outputStream);

        final String xml = outputStream.toString();
        assertTrue(xml.contains("&entity;"));
    }

    @Test
    public void testOutputElementContentString() {
        final Element element = new Element("element");
        element.setText("content");

        final XMLOutputter outputter = new XMLOutputter();

        final String xml = outputter.outputElementContentString(element);
        assertEquals("content", xml);
    }
}