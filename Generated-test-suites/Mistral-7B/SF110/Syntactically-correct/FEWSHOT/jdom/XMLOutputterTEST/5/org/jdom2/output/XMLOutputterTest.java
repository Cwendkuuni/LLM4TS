package org.jdom2.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

    @Test
    public void testXMLOutputter() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter);

        Format format = new Format();
        XMLOutputter outputterWithFormat = new XMLOutputter(format);
        assertNotNull(outputterWithFormat);

        XMLOutputProcessor processor = new XMLOutputProcessor();
        XMLOutputter outputterWithProcessor = new XMLOutputter(format, processor);
        assertNotNull(outputterWithProcessor);
    }

    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = new Format();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        Document document = new Document();
        String expectedOutput = "<root/>";

        StringWriter writer = new StringWriter();
        outputter.output(document, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputDocType() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        DocType docType = new DocType("root", null, null);
        String expectedOutput = "<!DOCTYPE root [ ]>\n<root />";

        StringWriter writer = new StringWriter();
        outputter.output(docType, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputElement() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new Element("root");
        String expectedOutput = "<root/>";

        StringWriter writer = new StringWriter();
        outputter.output(element, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputElementContent() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new Element("root");
        element.addContent(new Text("Hello World"));
        String expectedOutput = "<root><text>Hello World</text></root>";

        StringWriter writer = new StringWriter();
        outputter.outputElementContent(element, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputList() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        List<Content> list = new ArrayList<>();
        list.add(new Element("element1"));
        list.add(new Element("element2"));

        String expectedOutput = "<element1 />\n<element2 />";

        StringWriter writer = new StringWriter();
        outputter.output(list, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputCDATA() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
        String expectedOutput = "<![CDATA[Hello World]]>";

        StringWriter writer = new StringWriter();
        outputter.output(cdata, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputText() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        Text text = new Text("Hello World");
        String expectedOutput = "Hello World";

        StringWriter writer = new StringWriter();
        outputter.output(text, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputComment() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        Comment comment = new Comment("Hello World");
        String expectedOutput = "<!--Hello World-->";

        StringWriter writer = new StringWriter();
        outputter.output(comment, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputProcessingInstruction() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
        String expectedOutput = "<?xml version=\"1.0\"?>";

        StringWriter writer = new StringWriter();
        outputter.output(pi, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputEntityRef() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        EntityRef entityRef = new EntityRef("entity1");
        String expectedOutput = "&entity1;";

        StringWriter writer = new StringWriter();
        outputter.output(entityRef, writer);
        String actualOutput = writer.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputElementContentString() throws JDOMException, IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new Element("root");
        element.addContent(new Text("Hello World"));

        String expectedOutput = "<root><text>Hello World</text></root>";
        String actualOutput = outputter.outputElementContentString(element);

        assertEquals(expectedOutput, actualOutput);
    }
}