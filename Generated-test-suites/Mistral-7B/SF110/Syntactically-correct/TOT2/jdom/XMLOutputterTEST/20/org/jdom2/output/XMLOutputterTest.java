
package org.junit.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.*;
import static org.junit.Assert.*;

public class XMLOutputterTest {
    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMParser().parse(new StringReader("<root><child>Hello</child></root>"));
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outputStream);
        String expectedXml = "<root><child>Hello</child></root>";
        assertEquals(expectedXml, outputStream.toString());
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new JDOMParser().parse(new StringReader("<root><child><text>Hello</text></child></root>"));
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.outputElementContent(doc.getRootElement().getChild(0), outputStream);
        String expectedXml = "<text>Hello</text>";
        assertEquals(expectedXml, outputStream.toString());
    }

    @Test
    public void testOutputList() throws IOException {
        Document doc = new JDOMParser().parse(new StringReader("<root><child><text>Hello</text></child><child><text>World</text></child></root>"));
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc.getRootElement().getChildren(), outputStream);
        String expectedXml = "<child><text>Hello</text></child><child><text>World</text></child>";
        assertEquals(expectedXml, outputStream.toString());
    }

    @Test
    public void testOutputStringDocument() {
        Document doc = new JDOMParser().parse(new StringReader("<root><child><text>Hello</text></child></root>"));
        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedXml = "<root><child><text>Hello</text></child></root>";
        String actualXml = xmlOutputter.outputString(doc);
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testEmptyDocumentOutput() throws IOException {
        Document doc = new Document();
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outputStream);
        String expectedXml = "";
        assertEquals(expectedXml, outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testOutputNullDocument() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        Document doc = null;
        xmlOutputter.output(doc, outputStream);
    }

    @Test(expected = IOException.class)
    public void testOutputNullOutputStream() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        Document doc = new JDOMParser().parse(new StringReader("<root><child><text>Hello</text></child></root>"));
        OutputStream outputStream = null;
        xmlOutputter.output(doc, outputStream);
    }

    @Test
    public void testOutputEmptyList() throws IOException {
        Document doc = new JDOMParser().parse(new StringReader("<root></root>"));
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(new ArrayList<Content>(), outputStream);
        String expectedXml = "";
        assertEquals(expectedXml, outputStream.toString());
    }

    @Test
    public void testOutputNullElement() throws IOException {
        Document doc = new JDOMParser().parse(new StringReader("<root><child><text>Hello</text></child></root>"));
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputNullFormat() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        Format format = null;
        xmlOutputter.output(new Document(), outputStream, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputNullProcessor() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputProcessor processor = null;
        xmlOutputter.output(new Document(), outputStream, processor);
    }

    @Test
    public void testSetFormat() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        Format format = new Format();
        xmlOutputter.setFormat(format);
        assertEquals(format, xmlOutputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        Format format = new Format();
        xmlOutputter.setFormat(format);
        assertEquals(format, xmlOutputter.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);
        assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }

    @Test
    public void testToString() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        StringBuilder buffer = new StringBuilder();
        buffer.append("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = false]");
        assertEquals(buffer.toString(), xmlOutputter.toString());
    }
}