package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(doc, writer);
        String expectedXml = new String(getClass().getResource("/expected_output.xml").getBytes());
        assertEquals(expectedXml, writer.toString());
    }

    @Test
    public void testOutputDocType() throws IOException {
        // Create a DocType object for the given document
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        DocType docType = doc.getRootElement().getDocument().getDocType();
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(docType, writer);
        String expectedDocType = new String(getClass().getResource("/expected_output_docType.xml").getBytes());
        assertEquals(expectedDocType, writer.toString());
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(rootElement, writer);
        String expectedElement = new String(getClass().getResource("/expected_output_element.xml").getBytes());
        assertEquals(expectedElement, writer.toString());
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.outputElementContent(rootElement, writer);
        String expectedElementContent = new String(getClass().getResource("/expected_output_elementContent.xml").getBytes());
        assertEquals(expectedElementContent, writer.toString());
    }

    @Test
    public void testOutputList() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        List<Element> elements = doc.getRootElement().getChildren();
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(elements, writer);
        String expectedList = new String(getClass().getResource("/expected_output_list.xml").getBytes());
        assertEquals(expectedList, writer.toString());
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        CDATA cdata = (CDATA) rootElement.getChild("data");
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(cdata, writer);
        String expectedCDATA = new String(getClass().getResource("/expected_output_cdata.xml").getBytes());
        assertEquals(expectedCDATA, writer.toString());
    }

    @Test
    public void testOutputText() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        Text text = (Text) rootElement.getChild("text");
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(text, writer);
        String expectedText = new String(getClass().getResource("/expected_output_text.xml").getBytes());
        assertEquals(expectedText, writer.toString());
    }

    @Test
    public void testOutputComment() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        Comment comment = (Comment) rootElement.getChild("comment");
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(comment, writer);
        String expectedComment = new String(getClass().getResource("/expected_output_comment.xml").getBytes());
        assertEquals(expectedComment, writer.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        ProcessingInstruction pi = (ProcessingInstruction) rootElement.getChild("processingInstruction");
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(pi, writer);
        String expectedPI = new String(getClass().getResource("/expected_output_processingInstruction.xml").getBytes());
        assertEquals(expectedPI, writer.toString());
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        EntityRef entityRef = (EntityRef) rootElement.getChild("entityRef");
        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = new StringWriter();
        xmlOutputter.output(entityRef, writer);
        String expectedEntityRef = new String(getClass().getResource("/expected_output_entityRef.xml").getBytes());
        assertEquals(expectedEntityRef, writer.toString());
    }

    @Test
    public void testOutputStringDocument() {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedXml = new String(getClass().getResource("/expected_output.xml").getBytes());
        String actualXml = xmlOutputter.outputString(doc);
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputStringDocType() {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        DocType docType = doc.getRootElement().getDocument().getDocType();
        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedDocType = new String(getClass().getResource("/expected_output_docType.xml").getBytes());
        String actualDocType = xmlOutputter.outputString(docType);
        assertEquals(expectedDocType, actualDocType);
    }

    @Test
    public void testOutputStringElement() {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedElement = new String(getClass().getResource("/expected_output_element.xml").getBytes());
        String actualElement = xmlOutputter.outputString(rootElement);
        assertEquals(expectedElement, actualElement);
    }

    @Test
    public void testOutputStringElementContent() {
        Document doc = new JDOMParser().parse(getClass().getResource("/test.xml")).getDocument();
        Element rootElement = doc.getRootElement();
        XMLOutputter xmlOutputter = new XMLOutputter();
        String expectedElementContent = new String(getClass().getResource("/expected_output_elementContent.xml").getBytes());
        String actualElementContent = xmlOutputter.outputElementContentString(rootElement);
        assertEquals(expectedElementContent, actualElementContent);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        String input = "&amp;<>&apos;\"";
        String expected = "&amp;amp; &lt; &gt; &apos; &apos; &quot;";
        String output = xmlOutputter.escapeAttributeEntities(input);
        assertEquals(expected, output);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        String input = "&amp;<>&apos;\"";
        String expected = "&amp;amp;<&lt;>&gt; &apos; &apos; &quot;";
        String output = xmlOutputter.escapeElementEntities(input);
        assertEquals(expected, output);
    }
}