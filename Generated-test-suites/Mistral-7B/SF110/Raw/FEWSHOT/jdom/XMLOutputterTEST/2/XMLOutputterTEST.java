package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() {
        Document doc = new JDOMFactory().newDocument();
        doc.setRootElementName("root");
        Element root = doc.getRootElement();
        root.addContent(new Element("child1").setText("Hello World"));
        root.addContent(new Element("child2").setText("Test"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "<root><child1>Hello World</child1><child2>Test</child2></root>";
        String result = xmlOutputter.outputString(doc);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputDocType() {
        DocType doctype = new JDOMFactory().newDocType("html", null, null, null, null);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "<!DOCTYPE html SYSTEM \"\">";
        String result = xmlOutputter.outputString(doctype);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputElement() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        Element child1 = new Element("child1");
        child1.setText("Hello World");
        root.addContent(child1);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "<child1>Hello World</child1>";
        String result = xmlOutputter.outputString(child1);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputList() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        Element child = new Element("child");
        List<Element> list = new JDOMFactory().newList(new Element[] { child, child, child });

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "<child/><child/><child/>";
        String result = xmlOutputter.outputString(list);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputCDATA() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
        root.addContent(cdata);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "<![CDATA[Hello World]]>";
        String result = xmlOutputter.outputString(cdata);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputText() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        Text text = new Text("Hello World");
        root.addContent(text);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "Hello World";
        String result = xmlOutputter.outputString(text);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputComment() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        Comment comment = new Comment("This is a comment");
        root.addContent(comment);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "<!--This is a comment-->";
        String result = xmlOutputter.outputString(comment);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputProcessingInstruction() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
        root.addContent(pi);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "<?xml version=\"1.0\"?>";
        String result = xmlOutputter.outputString(pi);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputEntityRef() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        EntityRef entity = new EntityRef("ampersand");
        root.addContent(entity);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "&amp;";
        String result = xmlOutputter.outputString(entity);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOutputElementContent() {
        Document doc = new JDOMFactory().newDocument();
        Element element = new Element("root");
        Text text = new Text("Hello World");
        element.addContent(text);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedResult = "Hello World";
        String result = xmlOutputter.outputElementContentString(element);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        String input = "<root test=\"<script>alert('Hello World')</script>\" />";
        String expectedResult = "<root test=\"&lt;script&gt;alert('Hello World')&lt;/script&gt;\"/>";
        String result = xmlOutputter.escapeAttributeEntities(input);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        String input = "<root><script>alert('Hello World')</script></root>";
        String expectedResult = "<root><script>alert(&#x27;Hello World&#x27;)</script></root>";
        String result = xmlOutputter.escapeElementEntities(input);

        assertEquals(expectedResult, result);
    }
}