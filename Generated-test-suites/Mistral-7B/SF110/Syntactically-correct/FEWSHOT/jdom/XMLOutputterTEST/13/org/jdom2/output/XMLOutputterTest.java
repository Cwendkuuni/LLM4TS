package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws Exception {
        Document doc = new JDOMFactory().newDocument("root", new Element("element"));
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(doc, writer);
        String expected = "<root><element/></root>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputDocType() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        doc.setDocType(new DocType("doctype", null, new Element("root"), new Attribute[]{}));
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(doc.getDocType(), writer);
        String expected = "<!DOCTYPE doctype [ <!ATTLIST root attr CDATA #IMPLIED> ]>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputElement() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("root text");
        doc.setRootElement(root);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(root, writer);
        String expected = "<root>root text</root>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputElementContent() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("root text");
        Element child = new Element("child");
        child.setText("child text");
        root.addContent(child);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.outputElementContent(root, writer);
        String expected = "<child>child text</child>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputList() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Element child1 = new Element("child1");
        child1.setText("child1 text");
        Element child2 = new Element("child2");
        child2.setText("child2 text");
        root.addContent(child1);
        root.addContent(child2);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(new JDOMFactory().getDocument(root.getContent()), writer);
        String expected = "<root><child1>child1 text</child1><child2>child2 text</child2></root>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputCDATA() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        CDATA cdata = new CDATA("CDATA text");
        root.addContent(cdata);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(cdata, writer);
        String expected = "<![CDATA[CDATA text]]>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputText() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Text text = new Text("text");
        root.addContent(text);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(text, writer);
        String expected = "<text>text</text>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputComment() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Comment comment = new Comment("comment text");
        root.addContent(comment);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(comment, writer);
        String expected = "<!--comment text-->";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        ProcessingInstruction pi = new ProcessingInstruction("xml-pi", "PI text");
        root.addContent(pi);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(pi, writer);
        String expected = "<xml-pi PI text>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputEntityRef() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        EntityRef entityRef = new EntityRef("entity");
        root.addContent(entityRef);
        XMLOutputter out = new XMLOutputter();
        Writer writer = new StringWriter();
        out.output(entityRef, writer);
        String expected = "&entity;";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputStringWithFormat() throws Exception {
        XMLOutputter out = new XMLOutputter(new Format());
        String output = out.outputString(new Element("root"));
        String expected = "<root/>";
        assertEquals(expected, output);
    }

    @Test
    public void testOutputStringWithProcessor() throws Exception {
        XMLOutputter out = new XMLOutputter(new XMLOutputProcessor() {
            @Override
            public void process(Writer writer, Format format, Content content) throws IOException {
                writer.write(content.getValue());
            }
        });
        String output = out.outputString(new Element("root"));
        String expected = "<root/>";
        assertEquals(expected, output);
    }
}