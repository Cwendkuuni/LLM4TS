package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(doc, out);
        String expected = "..."; // expected XML string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputDocType() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        DocType docType = doc.getRootElement().getDocument().getDocType();
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(docType, out);
        String expected = "..."; // expected DOCTYPE string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        Element root = doc.getRootElement();
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(root, out);
        String expected = "..."; // expected XML element string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        Element root = doc.getRootElement();
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.outputElementContent(root, out);
        String expected = "..."; // expected XML element content string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputList() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        List<Element> elements = doc.getRootElement().getChildren();
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(elements, out);
        String expected = "..."; // expected XML list string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        Element cdataElement = doc.getRootElement().getChild("cdataElement", Namespace.getNamespace("http://example.com"));
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(cdataElement.getContent(), out);
        String expected = "..."; // expected CDATA string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputText() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        Element textElement = doc.getRootElement().getChild("textElement");
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(textElement.getText(), out);
        String expected = "..."; // expected text string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputComment() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        Element commentElement = doc.getRootElement().getChild("commentElement");
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(commentElement, out);
        String expected = "..."; // expected comment string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        Element piElement = doc.getRootElement().getChild("piElement");
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(piElement, out);
        String expected = "..."; // expected PI string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        EntityRef entityRef = doc.getRootElement().getChild("entityRefElement").getChild("entityRef");
        XMLOutputter xmlOut = new XMLOutputter();
        Writer out = new StringWriter();
        xmlOut.output(entityRef, out);
        String expected = "..."; // expected entity reference string
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputElementContentString() throws IOException {
        Document doc = new JDOMParser().parse("classpath:test.xml");
        Element root = doc.getRootElement();
        XMLOutputter xmlOut = new XMLOutputter();
        String expected = "..."; // expected XML element content string
        String actual = xmlOut.outputElementContentString(root);
        assertEquals(expected, actual);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter xmlOut = new XMLOutputter();
        String input = "&lt;test&gt;<xml&gt;";
        String expected = "&amp;lt;test&amp;gt;&amp;amp;lt;xml&amp;amp;gt;";
        String output = xmlOut.escapeAttributeEntities(input);
        assertEquals(expected, output);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter xmlOut = new XMLOutputter();
        String input = "<test>&lt;xml&gt;</test>";
        String expected = "&lt;test>&amp;lt;xml&amp;gt;</test>";
        String output = xmlOut.escapeElementEntities(input);
        assertEquals(expected, output);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        XMLOutputter xmlOut = new XMLOutputter();
        XMLOutputter cloned = xmlOut.clone();
        assertEquals(xmlOut, cloned);
    }

    @Test
    public void testToString() {
        XMLOutputter xmlOut = new XMLOutputter();
        String expected = "XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = [\r,\n], mode = XML]";
        String actual = xmlOut.toString();
        assertEquals(expected, actual);
    }
}