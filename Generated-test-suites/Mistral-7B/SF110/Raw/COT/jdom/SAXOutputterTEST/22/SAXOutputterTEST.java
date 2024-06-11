package org.jdom2.output; // Add your test package here

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SAXOutputterTest {

    private SAXOutputter saxOutputter;
    private final Document document = new Document();
    private final Element element = new Element("Root");
    private final String xmlString = "<Root></Root>";

    @Before
    public void setUp() {
        saxOutputter = new SAXOutputter();
    }

    @Test
    public void testSAXOutputterConstruction() {
        Assert.assertNotNull(saxOutputter);
    }

    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        saxOutputter.output(document);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter saxOutputterWithOutputStream = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), Format.getPrettyFormat(), null, null, null, null, null, null, null, null);
        saxOutputterWithOutputStream.setOutput(outputStream);
        saxOutputterWithOutputStream.output(document);
        final String output = outputStream.toString();
        Assert.assertEquals(xmlString, output);
    }

    @Test
    public void testOutputListOfContent() throws JDOMException, SAXException {
        document.addContent(element);
        List<Content> nodes = new ArrayList<>();
        nodes.add(element);
        saxOutputter.output(nodes);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter saxOutputterWithOutputStream = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), Format.getPrettyFormat(), null, null, null, null, null, null, null, null);
        saxOutputterWithOutputStream.setOutput(outputStream);
        saxOutputterWithOutputStream.output(nodes);
        final String output = outputStream.toString();
        Assert.assertEquals(xmlString, output);
    }

    @Test
    public void testOutputElement() throws JDOMException, SAXException {
        document.setRootElement(element);
        saxOutputter.output(document);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter saxOutputterWithOutputStream = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), Format.getPrettyFormat(), null, null, null, null, null, null, null, null);
        saxOutputterWithOutputStream.setOutput(outputStream);
        saxOutputterWithOutputStream.output(document);
        final String output = outputStream.toString();
        Assert.assertEquals(xmlString, output);
    }

    @Test
    public void testOutputFragmentListOfContent() throws JDOMException, SAXException {
        document.addContent(element);
        List<Content> nodes = new ArrayList<>();
        nodes.add(element);
        saxOutputter.outputFragment(nodes);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter saxOutputterWithOutputStream = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), null, null, null, null, null, null, null, null, null);
        saxOutputterWithOutputStream.setOutput(outputStream);
        saxOutputterWithOutputStream.outputFragment(nodes);
        final String output = outputStream.toString();
        Assert.assertEquals("<Root></Root>", output);
    }

    @Test
    public void testOutputFragmentElement() throws JDOMException, SAXException {
        document.setRootElement(element);
        saxOutputter.outputFragment(document);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter saxOutputterWithOutputStream = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), null, null, null, null, null, null, null, null, null);
        saxOutputterWithOutputStream.setOutput(outputStream);
        saxOutputterWithOutputStream.outputFragment(document);
        final String output = outputStream.toString();
        Assert.assertEquals(xmlString, output);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullDocument() throws JDOMException, SAXException {
        saxOutputter.output(null);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullListOfContent() throws JDOMException, SAXException {
        saxOutputter.output(null);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullElement() throws JDOMException, SAXException {
        saxOutputter.output(null);
    }

    @Test
    public void testErrorHandler() throws JDOMException, SAXException {
        final ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                Assert.assertEquals("Error message", exception.getMessage());
            }
        };
        saxOutputter.setErrorHandler(errorHandler);
        try {
            saxOutputter.output(document);
            Assert.fail();
        } catch (JDOMException e) {
            // Expected
        }
    }
}