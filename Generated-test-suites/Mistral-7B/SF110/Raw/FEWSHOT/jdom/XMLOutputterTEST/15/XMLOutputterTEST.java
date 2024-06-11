package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        doc.getRootElement().addContent(new Element("root").setText("Hello World"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root>Hello World</root>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType docType = new DocumentType("html", null, null, null);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<!DOCTYPE html SYSTEM \"\">";
        String result = xmlOutputter.outputString(docType);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Element child = new Element("child");
        root.addContent(child);
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root><child/></root>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Hello World");
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root>Hello World</root>";
        String result = xmlOutputter.outputElementContentString(root);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputList() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Element child1 = new Element("child1").setText("Child 1");
        Element child2 = new Element("child2").setText("Child 2");
        root.addContent(child1, child2);
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        CDATA cdata = new CDATA("<![CDATA[Hello World]]]>");
        root.addContent(cdata);
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root><![CDATA[Hello World]]></root>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputText() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Text text = new Text("Hello World");
        root.addContent(text);
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root>Hello World</root>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputComment() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Comment comment = new Comment("This is a comment");
        root.addContent(comment);
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root><![--This is a comment--]></root>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\"");
        root.addContent(pi);
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root<?xml version=\"1.0\" encoding=\"UTF-8\"?>>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        EntityRef entityRef = new EntityRef("entity");
        Element root = new Element("root");
        root.addContent(entityRef);
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expected = "<root>&entity;</root>";
        String result = xmlOutputter.outputString(doc);
        assertEquals(expected, result);
    }

    @Test
    public void testSetFormat() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        Format format = new Format();
        format.setEncoding("UTF-8");
        xmlOutputter.setFormat(format);

        Format result = xmlOutputter.getFormat();
        assertEquals(format, result);
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);

        XMLOutputProcessor result = xmlOutputter.getXMLOutputProcessor();
        assertEquals(processor, result);
    }
}