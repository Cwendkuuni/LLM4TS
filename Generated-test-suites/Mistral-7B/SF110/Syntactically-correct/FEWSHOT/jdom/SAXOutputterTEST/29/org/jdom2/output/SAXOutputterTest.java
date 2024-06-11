package org.jdom2.output;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.xml.sax.*;

import java.util.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterConstruction() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerSetter() {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler handler = mock(ContentHandler.class);
        outputter.setContentHandler(handler);
        assertEquals(handler, outputter.getContentHandler());
    }

    @Test
    public void testSAXOutputterErrorHandlerSetter() {
        SAXOutputter outputter = new SAXOutputter();
        ErrorHandler handler = mock(ErrorHandler.class);
        outputter.setErrorHandler(handler);
        assertEquals(handler, outputter.getErrorHandler());
    }

    @Test
    public void testSAXOutputterDTDHandlerSetter() {
        SAXOutputter outputter = new SAXOutputter();
        DTDHandler handler = mock(DTDHandler.class);
        outputter.setDTDHandler(handler);
        assertEquals(handler, outputter.getDTDHandler());
    }

    @Test
    public void testSAXOutputterEntityResolverSetter() {
        SAXOutputter outputter = new SAXOutputter();
        EntityResolver resolver = mock(EntityResolver.class);
        outputter.setEntityResolver(resolver);
        assertEquals(resolver, outputter.getEntityResolver());
    }

    @Test
    public void testSAXOutputterLexicalHandlerSetter() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler handler = mock(LexicalHandler.class);
        outputter.setLexicalHandler(handler);
        assertEquals(handler, outputter.getLexicalHandler());
    }

    @Test
    public void testSAXOutputterDeclHandlerSetter() {
        SAXOutputter outputter = new SAXOutputter();
        DeclHandler handler = mock(DeclHandler.class);
        outputter.setDeclHandler(handler);
        assertEquals(handler, outputter.getDeclHandler());
    }

    @Test
    public void testSAXOutputterReportNamespaceDeclarationsSetter() {
        SAXOutputter outputter = new SAXOutputter();
        assertTrue(outputter.getReportNamespaceDeclarations());
        outputter.setReportNamespaceDeclarations(false);
        assertFalse(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSAXOutputterReportDTDEventsSetter() {
        SAXOutputter outputter = new SAXOutputter();
        assertTrue(outputter.getReportDTDEvents());
        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterSAXOutputProcessorSetter() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        SAXOutputter outputter = new SAXOutputter();
        outputter.setSAXOutputProcessor(processor);
        assertEquals(processor, outputter.getSAXOutputProcessor());
    }

    @Test
    public void testSAXOutputterFormatSetter() {
        Format format = new DefaultFormat();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSAXOutputterOutputDocument() throws SAXException, JDOMException {
        Document document = new JAXBDocument();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
        SAXTarget target = outputter.buildTarget(document);
        verify(outputter.getContentHandler()).startDocument();
        verify(outputter.getContentHandler()).startElement("document", null);
        document.save(new StringWriter(), outputter);
        verify(outputter.getContentHandler()).endElement("document");
        verify(outputter.getContentHandler()).endDocument();
    }

    @Test
    public void testSAXOutputterOutputList() throws SAXException, JDOMException {
        Document document = new JAXBDocument();
        List<Content> nodes = document.getContent();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(nodes);
        SAXTarget target = outputter.buildTarget(null);
        verify(outputter.getContentHandler()).startDocument();
        verify(outputter.getContentHandler()).startElement("document", null);
        for (Content node : nodes) {
            outputter.outputFragment(node);
        }
        verify(outputter.getContentHandler()).endElement("document");
        verify(outputter.getContentHandler()).endDocument();
    }

    @Test
    public void testSAXOutputterOutputElement() throws SAXException, JDOMException {
        Document document = new JAXBDocument();
        Element element = document.getRootElement();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(element);
        SAXTarget target = outputter.buildTarget(null);
        verify(outputter.getContentHandler()).startDocument();
        verify(outputter.getContentHandler()).startElement("document", null);
        outputter.outputFragment(element);
        verify(outputter.getContentHandler()).endElement("document");
        verify(outputter.getContentHandler()).endDocument();
    }

    @Test
    public void testSAXOutputterOutputFragmentList() throws SAXException, JDOMException {
        Document document = new JAXBDocument();
        List<Content> nodes = document.getContent();
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(nodes);
        SAXTarget target = outputter.buildTarget(null);
        for (Content node : nodes) {
            outputter.outputFragment(node);
        }
        verify(outputter.getContentHandler()).startDocument();
        verify(outputter.getContentHandler()).startElement("document", null);
        for (Content node : nodes) {
            switch (node.getCType()) {
                case CDATA:
                    verify(outputter.getContentHandler()).characters(node.getContent().toCharArray(), 0, node.getContent().length());
                    break;
                case Comment:
                    verify(outputter.getContentHandler()).comment(node.getContent().getText());
                    break;
                case Element:
                    verify(outputter.getContentHandler()).startElement(node.getName().getLocalName(), null);
                    Element element = (Element) node;
                    for (Attribute attribute : element.getAttributes()) {
                        verify(outputter.getContentHandler()).attribute(attribute.getQualifiedName().getLocalName(), attribute.getText());
                    }
                    verify(outputter.getContentHandler()).endElement(node.getName().getLocalName());
                    break;
                case EntityRef:
                    verify(outputter.getContentHandler()).processingInstruction("xml-declaration", node.getText());
                    break;
                case ProcessingInstruction:
                    verify(outputter.getContentHandler()).processingInstruction(node.getName().getLocalName(), node.getText());
                    break;
                case Text:
                    verify(outputter.getContentHandler()).characters(node.getContent().toCharArray(), 0, node.getContent().length());
                    break;
            }
        }
        verify(outputter.getContentHandler()).endElement("document");
        verify(outputter.getContentHandler()).endDocument();
    }

    @Test
    public void testSAXOutputterOutputFragmentElement() throws SAXException, JDOMException {
        Document document = new JAXBDocument();
        Element element = document.getRootElement();
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(element);
        SAXTarget target = outputter.buildTarget(null);
        verify(outputter.getContentHandler()).startDocument();
        verify(outputter.getContentHandler()).startElement("document", null);
        outputter.outputFragment(element);
        verify(outputter.getContentHandler()).endElement("document");
        verify(outputter.getContentHandler()).endDocument();
    }
}