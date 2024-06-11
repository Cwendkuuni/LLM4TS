package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.jdom2.output.support.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputter() {
        SAXOutputter outputter = new SAXOutputter();
        assertNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertNull(outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());

        SAXOutputter outputterWithContentHandler = new SAXOutputter(new DefaultContentHandler());
        assertEquals(outputterWithContentHandler.getContentHandler(), new DefaultContentHandler());
        assertNull(outputterWithContentHandler.getErrorHandler());
        assertNull(outputterWithContentHandler.getDTDHandler());
        assertNull(outputterWithContentHandler.getEntityResolver());
        assertNull(outputterWithContentHandler.getLexicalHandler());
        assertNull(outputterWithContentHandler.getDeclHandler());
        assertFalse(outputterWithContentHandler.getReportNamespaceDeclarations());
        assertTrue(outputterWithContentHandler.getReportDTDEvents());
        assertNull(outputterWithContentHandler.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithContentHandler.getFormat());

        SAXOutputter outputterWithContentHandlerAndErrorHandler = new SAXOutputter(new DefaultContentHandler(), new DefaultErrorHandler());
        assertEquals(outputterWithContentHandlerAndErrorHandler.getContentHandler(), new DefaultContentHandler());
        assertEquals(outputterWithContentHandlerAndErrorHandler.getErrorHandler(), new DefaultErrorHandler());
        assertNull(outputterWithContentHandlerAndErrorHandler.getDTDHandler());
        assertNull(outputterWithContentHandlerAndErrorHandler.getEntityResolver());
        assertNull(outputterWithContentHandlerAndErrorHandler.getLexicalHandler());
        assertNull(outputterWithContentHandlerAndErrorHandler.getDeclHandler());
        assertFalse(outputterWithContentHandlerAndErrorHandler.getReportNamespaceDeclarations());
        assertTrue(outputterWithContentHandlerAndErrorHandler.getReportDTDEvents());
        assertNull(outputterWithContentHandlerAndErrorHandler.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithContentHandlerAndErrorHandler.getFormat());

        SAXOutputter outputterWithAllHandlers = new SAXOutputter(new DefaultContentHandler(), new DefaultErrorHandler(), new DefaultDTDHandler(), new DefaultEntityResolver(), new DefaultLexicalHandler(), new DefaultDeclHandler());
        assertEquals(outputterWithAllHandlers.getContentHandler(), new DefaultContentHandler());
        assertEquals(outputterWithAllHandlers.getErrorHandler(), new DefaultErrorHandler());
        assertEquals(outputterWithAllHandlers.getDTDHandler(), new DefaultDTDHandler());
        assertEquals(outputterWithAllHandlers.getEntityResolver(), new DefaultEntityResolver());
        assertEquals(outputterWithAllHandlers.getLexicalHandler(), new DefaultLexicalHandler());
        assertEquals(outputterWithAllHandlers.getDeclHandler(), new DefaultDeclHandler());
        assertFalse(outputterWithAllHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithAllHandlers.getReportDTDEvents());
        assertNull(outputterWithAllHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithAllHandlers.getFormat());

        SAXOutputter outputterWithCustomProcessor = new SAXOutputter(new CustomSAXOutputProcessor(), Format.getRawFormat());
        assertNotNull(outputterWithCustomProcessor.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithCustomProcessor.getFormat());
        assertNull(outputterWithCustomProcessor.getContentHandler());
        assertNull(outputterWithCustomProcessor.getErrorHandler());
        assertNull(outputterWithCustomProcessor.getDTDHandler());
        assertNull(outputterWithCustomProcessor.getEntityResolver());
        assertNull(outputterWithCustomProcessor.getLexicalHandler());
        assertNull(outputterWithCustomProcessor.getDeclHandler());
        assertFalse(outputterWithCustomProcessor.getReportNamespaceDeclarations());
        assertTrue(outputterWithCustomProcessor.getReportDTDEvents());

        SAXOutputter outputterWithCustomFormat = new SAXOutputter(null, new CustomFormat());
        assertNull(outputterWithCustomFormat.getSAXOutputProcessor());
        assertNotNull(outputterWithCustomFormat.getFormat());
        assertNull(outputterWithCustomFormat.getContentHandler());
        assertNull(outputterWithCustomFormat.getErrorHandler());
        assertNull(outputterWithCustomFormat.getDTDHandler());
        assertNull(outputterWithCustomFormat.getEntityResolver());
        assertNull(outputterWithCustomFormat.getLexicalHandler());
        assertNull(outputterWithCustomFormat.getDeclHandler());
        assertFalse(outputterWithCustomFormat.getReportNamespaceDeclarations());
        assertTrue(outputterWithCustomFormat.getReportDTDEvents());
    }

    @Test
    public void testSetters() {
        SAXOutputter outputter = new SAXOutputter();

        outputter.setContentHandler(new DefaultContentHandler());
        assertEquals(outputter.getContentHandler(), new DefaultContentHandler());

        outputter.setErrorHandler(new DefaultErrorHandler());
        assertEquals(outputter.getErrorHandler(), new DefaultErrorHandler());

        outputter.setDTDHandler(new DefaultDTDHandler());
        assertEquals(outputter.getDTDHandler(), new DefaultDTDHandler());

        outputter.setEntityResolver(new DefaultEntityResolver());
        assertEquals(outputter.getEntityResolver(), new DefaultEntityResolver());

        outputter.setLexicalHandler(new DefaultLexicalHandler());
        assertEquals(outputter.getLexicalHandler(), new DefaultLexicalHandler());

        outputter.setDeclHandler(new DefaultDeclHandler());
        assertEquals(outputter.getDeclHandler(), new DefaultDeclHandler());

        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());

        outputter.setSAXOutputProcessor(new CustomSAXOutputProcessor());
        assertNotNull(outputter.getSAXOutputProcessor());

        outputter.setFormat(new CustomFormat());
        assertNotNull(outputter.getFormat());
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        Document document = new Document();
        outputter.output(document);
    }

    @Test
    public void testOutputNodes() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        nodes.add(new Element("root"));
        outputter.output(nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        Element element = new Element("root");
        outputter.output(element);
    }

    @Test
    public void testOutputFragment() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        List<Content> fragment = new ArrayList<>();
        fragment.add(new Text("fragment"));
        outputter.outputFragment(fragment);
    }

    @Test
    public void testOutputFragmentSingleContent() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        Content fragment = new Text("fragment");
        outputter.outputFragment(fragment);
    }

    @Test(expected = JDOMException.class)
    public void testOutputInvalidContent() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        nodes.add(new ProcessingInstruction("", ""));
        outputter.outputFragment(nodes);
    }
}

class DefaultContentHandler extends DefaultHandler {
}

class DefaultErrorHandler extends DefaultHandler {
}

class DefaultDTDHandler extends DefaultHandler implements DTDHandler {
}

class DefaultEntityResolver implements EntityResolver {
}

class DefaultLexicalHandler extends DefaultHandler implements LexicalHandler {
}

class DefaultDeclHandler implements DeclHandler {
}

class CustomFormat implements Format {
}

class CustomSAXOutputProcessor extends AbstractSAXOutputProcessor {
}