package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(doc, sw);
        String expected = "<root/>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType docType = new DocType("test", null, null);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(docType, sw);
        String expected = "<!DOCTYPE test [ ]>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        Element child = new Element("child");
        root.addContent(child);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(child, sw);
        String expected = "<child/>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputElementWithContent() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        Element child = new Element("child");
        child.setText("Hello World");
        root.addContent(child);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(child, sw);
        String expected = "<child>Hello World</child>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputListOfContent() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        root.addContent(child1);
        root.addContent(child2);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(new JList<Content>(new Content[]{child1, child2}), sw);
        String expected = "<root><child1/><child2/></root>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
        root.addContent(cdata);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(cdata, sw);
        String expected = "<![CDATA[Hello World]]>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputText() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        Text text = new Text("Hello World");
        root.addContent(text);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(text, sw);
        String expected = "<text>Hello World</text>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputComment() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        Comment comment = new Comment("This is a comment");
        root.addContent(comment);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(comment, sw);
        String expected = "<!--This is a comment-->";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0' encoding='UTF-8'");
        root.addContent(pi);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(pi, sw);
        String expected = "<?xml-declaration version='1.0' encoding='UTF-8'?>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        EntityRef entityRef = new EntityRef("entity1");
        root.addContent(entityRef);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.output(entityRef, sw);
        String expected = "<entity1/>";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        Element child = new Element("child");
        child.setText("Hello World");
        root.addContent(child);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter sw = new StringWriter();
        xmlo.outputElementContent(child, sw);
        String expected = "Hello World";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testOutputStringFromDocument() {
        Document doc = new Document();
        Element root = new Element("root");
        Element child = new Element("child");
        child.setText("Hello World");
        root.addContent(child);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(doc);
        String expected = "<root><child>Hello World</child></root>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromDocType() {
        DocType docType = new DocType("test", null, null);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(docType);
        String expected = "<!DOCTYPE test [ ]>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromElement() {
        Document doc = new Document();
        Element root = new Element("root");
        Element child = new Element("child");
        child.setText("Hello World");
        root.addContent(child);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(child);
        String expected = "<child>Hello World</child>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromListOfContent() {
        Document doc = new Document();
        Element root = new Element("root");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        root.addContent(child1);
        root.addContent(child2);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(new JList<Content>(new Content[]{child1, child2}));
        String expected = "<root><child1/><child2/></root>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromCDATA() {
        Document doc = new Document();
        Element root = new Element("root");
        CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
        root.addContent(cdata);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(cdata);
        String expected = "<![CDATA[Hello World]]>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromText() {
        Document doc = new Document();
        Element root = new Element("root");
        Text text = new Text("Hello World");
        root.addContent(text);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(text);
        String expected = "<text>Hello World</text>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromComment() {
        Document doc = new Document();
        Element root = new Element("root");
        Comment comment = new Comment("This is a comment");
        root.addContent(comment);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(comment);
        String expected = "<!--This is a comment-->";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromProcessingInstruction() {
        Document doc = new Document();
        Element root = new Element("root");
        ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0' encoding='UTF-8'");
        root.addContent(pi);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(pi);
        String expected = "<?xml-declaration version='1.0' encoding='UTF-8'?>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromEntityRef() {
        Document doc = new Document();
        Element root = new Element("root");
        EntityRef entityRef = new EntityRef("entity1");
        root.addContent(entityRef);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputString(entityRef);
        String expected = "<entity1/>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringFromElementContent() {
        Document doc = new Document();
        Element root = new Element("root");
        Element child = new Element("child");
        child.setText("Hello World");
        root.addContent(child);
        doc.setRootElement(root);
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputElementContentString(child);
        String expected = "Hello World";
        assertEquals(expected, output);
    }
}