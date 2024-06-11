package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() {
        Document doc = new JDOMFactory().newDocument("root");
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root/>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputDocumentWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'/>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputDocType() {
        DocType docType = new JDOMFactory().getDocumentBuilder().getDocType();
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<!DOCTYPE root []\n<root/>";
        String actual = outputter.outputString(docType);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputElement() {
        Document doc = new JDOMFactory().newDocument("root");
        Element element = new Element("child", "value");
        doc.getRootElement().addContent(element);
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root><child>value</child></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputElementWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        Element element = new Element("child", "value");
        doc.getRootElement().addContent(element);
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'><child>value</child></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputListOfContents() {
        Document doc = new JDOMFactory().newDocument("root");
        Element root = doc.getRootElement();
        root.addContent(new Element("child1").setText("value1"));
        root.addContent(new Element("child2").setText("value2"));
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root><child1>value1</child1><child2>value2</child2></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputListOfContentsWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        Element root = doc.getRootElement();
        root.addContent(new Element("child1").setText("value1"));
        root.addContent(new Element("child2").setText("value2"));
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'><child1>value1</child1><child2>value2</child2></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputCDATA() {
        Document doc = new JDOMFactory().newDocument("root");
        CDATA cdata = new CDATA("value");
        doc.getRootElement().addContent(cdata);
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root><![CDATA[value]]></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputCDATAWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        CDATA cdata = new CDATA("value");
        doc.getRootElement().addContent(cdata);
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'><![CDATA[value]]></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputText() {
        Document doc = new JDOMFactory().newDocument("root");
        Element element = new Element("child", "value");
        doc.getRootElement().addContent(element.getText());
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root>value</root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputTextWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        Element element = new Element("child", "value");
        doc.getRootElement().addContent(element.getText());
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'>value</root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputComment() {
        Document doc = new JDOMFactory().newDocument("root");
        Element root = doc.getRootElement();
        Comment comment = new Comment("This is a comment");
        root.addContent(comment);
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root><!--This is a comment--></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputCommentWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        Element root = doc.getRootElement();
        Comment comment = new Comment("This is a comment");
        root.addContent(comment);
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'><!--This is a comment--></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputProcessingInstruction() {
        Document doc = new JDOMFactory().newDocument("root");
        ProcessingInstruction pi = new ProcessingInstruction("xml-map", "PI value");
        doc.getRootElement().addContent(pi);
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root<?xml-map PI value?>></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputProcessingInstructionWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        ProcessingInstruction pi = new ProcessingInstruction("xml-map", "PI value");
        doc.getRootElement().addContent(pi);
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'<?xml-map PI value?>></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputEntityRef() {
        Document doc = new JDOMFactory().newDocument("root");
        EntityRef entityRef = new EntityRef("entity");
        doc.getRootElement().addContent(entityRef);
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root>&entity;</root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputEntityRefWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        EntityRef entityRef = new EntityRef("entity");
        doc.getRootElement().addContent(entityRef);
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'>&entity;</root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputElementContent() {
        Document doc = new JDOMFactory().newDocument("root");
        Element element = new Element("child", null);
        element.addContent(new Text("value"));
        doc.getRootElement().addContent(element);
        XMLOutputter outputter = new XMLOutputter();
        String expected = "<root><child>value</child></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputElementContentWithFormat() {
        Document doc = new JDOMFactory().newDocument("root");
        Element element = new Element("child", null);
        element.addContent(new Text("value"));
        doc.getRootElement().addContent(element);
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        String expected = "<root xmlns='http://www.w3.org/2000/xmlns/' xml:space='preserve'><child>value</child></root>";
        String actual = outputter.outputString(doc);
        assertEquals(expected, actual);
    }
}