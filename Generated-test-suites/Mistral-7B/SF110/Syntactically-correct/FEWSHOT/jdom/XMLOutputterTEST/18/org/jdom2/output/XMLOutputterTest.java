package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(doc, writer);
        String expected = new String(getClass().getResource("/expected_output.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputDocType() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        DocType doctype = doc.getRootElement().getDocument().getDocType();
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(doctype, writer);
        String expected = new String(getClass().getResource("/expected_output_doctype.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("myElement", "namespace:myPrefix");
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(element, writer);
        String expected = new String(getClass().getResource("/expected_output_element.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("myElement", "namespace:myPrefix");
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.outputElementContent(element, writer);
        String expected = new String(getClass().getResource("/expected_output_element_content.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputList() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element listElement = doc.getRootElement().getChild("myList");
        List<Content> list = listElement.getContent();
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(list, writer);
        String expected = new String(getClass().getResource("/expected_output_list.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element cdataElement = doc.getRootElement().getChild("myCDATA");
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(cdataElement.getContent()[0], writer);
        String expected = new String(getClass().getResource("/expected_output_cdata.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputText() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element textElement = doc.getRootElement().getChild("myText");
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(textElement.getContent()[0], writer);
        String expected = new String(getClass().getResource("/expected_output_text.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputComment() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element commentElement = doc.getRootElement().getChild("myComment");
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(commentElement.getContent()[0], writer);
        String expected = new String(getClass().getResource("/expected_output_comment.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element piElement = doc.getRootElement().getChild("myPI");
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(piElement.getContent()[0], writer);
        String expected = new String(getClass().getResource("/expected_output_processing_instruction.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        EntityRef entityRef = doc.getRootElement().getChild("myEntityRef");
        XMLOutputter xmlo = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlo.output(entityRef, writer);
        String expected = new String(getClass().getResource("/expected_output_entity_ref.xml").getText(), "UTF-8");
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testOutputElementContentString() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml")).getRootElement();
        Element element = doc.getRootElement().getChild("myElement", "namespace:myPrefix");
        XMLOutputter xmlo = new XMLOutputter();
        String output = xmlo.outputElementContentString(element);
        String expected = new String(getClass().getResource("/expected_output_element_content_string.xml").getText(), "UTF-8");
        assertEquals(expected, output);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter xmlo = new XMLOutputter();
        String input = "<root xmlns:myPrefix=\"http://example.com/\"><myAttribute>foo &amp; bar &lt;baz&gt;</myAttribute></root>";
        String expected = "<root xmlns:myPrefix=\"http://example.com/\"><myAttribute>foo &amp;amp; bar &lt;baz&gt;</myAttribute></root>";
        String output = xmlo.escapeAttributeEntities(input);
        assertEquals(expected, output);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter xmlo = new XMLOutputter();
        String input = "<root><myElement>&lt;test&gt;</myElement></root>";
        String expected = "<root><myElement>&lt;test&gt;</myElement></root>";
        String output = xmlo.escapeElementEntities(input, null);
        assertEquals(expected, output);
    }
}