package org.jdom2.output;

import org.jdom2.*;
import org.xml.sax.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

public class SAXOutputterTest {

    private SAXOutputter saxOutputter;
    private ByteArrayOutputStream outputStream;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        saxOutputter = new SAXOutputter();
        outputStream = new ByteArrayOutputStream();
        saxOutputter.setOutput(outputStream);
        saxOutputter.setFormat(Format.getPrettyFormat());
        stringWriter = new StringWriter();
        saxOutputter.setOutput(stringWriter);
    }

    @Test
    public void testSAXOutputterEmpty() throws JDOMException {
        Document document = new Document();
        saxOutputter.output(document);
        String xml = stringWriter.toString();
        assertEquals("", xml);
    }

    @Test
    public void testSAXOutputterDocument() throws JDOMException {
        Document document = new Document(new Element("root"));
        saxOutputter.output(document);
        String xml = stringWriter.toString();
        String expected = "<root/>";
        assertEquals(expected, xml);
    }

    @Test
    public void testSAXOutputterList() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        Text text1 = new Text("Text 1");
        Text text2 = new Text("Text 2");
        root.addContent(text1);
        root.addContent(text2);
        document.setRootElement(root);
        saxOutputter.output(document);
        String xml = stringWriter.toString();
        String expected = "<root><text>Text 1</text><text>Text 2</text></root>";
        assertEquals(expected, xml);
    }

    @Test
    public void testSAXOutputterElement() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        Text text1 = new Text("Text 1");
        root.addContent(text1);
        document.setRootElement(root);
        saxOutputter.output(root);
        String xml = stringWriter.toString();
        String expected = "<root><text>Text 1</text></root>";
        assertEquals(expected, xml);
    }

    @Test
    public void testSAXOutputterFragmentList() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        Text text1 = new Text("Text 1");
        Text text2 = new Text("Text 2");
        root.addContent(text1);
        root.addContent(text2);
        document.setRootElement(root);
        saxOutputter.outputFragment(document.getRootElement().getChildren());
        String xml = outputStream.toString();
        String expected = "<root><text>Text 1</text><text>Text 2</text></root>";
        assertEquals(expected, xml);
    }

    @Test
    public void testSAXOutputterFragmentElement() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        Text text = new Text("Text");
        root.addContent(text);
        document.setRootElement(root);
        saxOutputter.outputFragment(root);
        String xml = outputStream.toString();
        String expected = "<root><text>Text</text></root>";
        assertEquals(expected, xml);
    }

    @Test(expected = JDOMException.class)
    public void testSAXOutputterInvalidContent() throws JDOMException {
        Document document = new Document();
        saxOutputter.outputFragment(document.getRootElement());
    }
}