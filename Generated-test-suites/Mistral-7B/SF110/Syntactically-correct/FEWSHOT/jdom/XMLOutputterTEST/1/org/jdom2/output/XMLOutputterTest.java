package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(doc);
        assertEquals("<root><child><subChild/><anotherChild>Text</anotherChild></child></root>", output);
    }

    @Test
    public void testOutputDocType() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        DocType docType = doc.getDocumentType();
        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(docType);
        assertEquals("<!DOCTYPE root SYSTEM \"test.dtd\">", output);
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("child");
        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(element);
        assertEquals("<child/>", output);

        element.addContent(new Text("Text"));
        output = xmlOutputter.outputString(element);
        assertEquals("<child>Text</child>", output);
    }

    @Test
    public void testOutputList() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element root = doc.getRootElement();
        List<Element> list = root.getChildren("child");

        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(list);
        assertEquals("<child></child><child></child>", output);
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("child");
        CDATA cdata = new CDATA("<![CDATA[Text]]>");
        element.addContent(cdata);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(element);
        assertEquals("<child><![CDATA[Text]]></child>", output);
    }

    @Test
    public void testOutputText() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("child");
        Text text = new Text("Text");
        element.addContent(text);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(element);
        assertEquals("<child>Text</child>", output);
    }

    @Test
    public void testOutputComment() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("child");
        Comment comment = new Comment("Text");
        element.addContent(comment);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(element);
        assertEquals("<child><!--Text--></child>", output);
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element root = doc.getRootElement();
        root.addProcessingInstruction("xml", "version='1.0' encoding='UTF-8'");

        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(doc);
        assertEquals("<!-- xml version='1.0' encoding='UTF-8' --><root></root>", output);
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element root = doc.getRootElement();
        EntityRef entityRef = new EntityRef("entity1");
        root.addContent(entityRef);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String output = xmlOutputter.outputString(doc);
        assertEquals("<root><![ENTITY entity1 SYSTEM \"entity1.ent\"]></root>", output);
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("child");
        Text text = new Text("Text");
        element.addContent(text);

        XMLOutputter xmlOutputter = new XMLOutputter();String output = xmlOutputter.outputElementContentString(element);
        assertEquals("Text", output);
    }
}