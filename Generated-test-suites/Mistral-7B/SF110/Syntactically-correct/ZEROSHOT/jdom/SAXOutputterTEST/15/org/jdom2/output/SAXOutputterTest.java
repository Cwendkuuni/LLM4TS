package org.jdom2.output;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.output.support.SAXTarget;
import org.xml.sax.*;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SAXOutputterTest {

    private SAXOutputter saxOutputter;

    @org.junit.Before
    public void setUp() {
        saxOutputter = new SAXOutputter();
    }

    @org.junit.Test
    public void testSAXOutputterDefaultConstructor() {
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNotNull(saxOutputter.getEntityResolver());
        assertNotNull(saxOutputter.getLexicalHandler());
        assertNotNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterContentHandlerConstructor() {
        final ContentHandler contentHandler = new DefaultHandler2();
        saxOutputter = new SAXOutputter(contentHandler);
        assertNotNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
        final ContentHandler contentHandler = new DefaultHandler2();
        final ErrorHandler errorHandler = new DefaultHandler2();
        saxOutputter = new SAXOutputter(contentHandler, errorHandler);
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
        final ContentHandler contentHandler = new DefaultHandler2();
        final ErrorHandler errorHandler = new DefaultHandler2();
        final DTDHandler dtdHandler = new DefaultHandler2();
        saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
        final ContentHandler contentHandler = new DefaultHandler2();
        final ErrorHandler errorHandler = new DefaultHandler2();
        final DTDHandler dtdHandler = new DefaultHandler2();
        final EntityResolver entityResolver = new DefaultHandler2();
        saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNotNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerLexicalHandlerConstructor() {
        final ContentHandler contentHandler = new DefaultHandler2();
        final ErrorHandler errorHandler = new DefaultHandler2();
        final DTDHandler dtdHandler = new DefaultHandler2();
        final LexicalHandler lexicalHandler = new DefaultHandler2();
        saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, lexicalHandler);
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNotNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterSAXOutputProcessorFormatConstructor() {
        final SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        final Format format = new DocumentFormat();
        saxOutputter = new SAXOutputter(processor, format);
        assertNotNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
        assertEquals(format, saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterSAXOutputProcessorFormatContentHandlerConstructor() {
        final SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        final Format format = new DocumentFormat();
        final ContentHandler contentHandler = new DefaultHandler2();
        saxOutputter = new SAXOutputter(processor, format, contentHandler);
        assertNotNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
        assertEquals(format, saxOutputter.getFormat());
    }

    @org.junit.Test
    public void testSAXOutputterDocumentOutput() throws JDOMException {
        final Document document = new Document();
        saxOutputter.output(document);
    }

    @org.junit.Test
    public void testSAXOutputterListOutput() throws JDOMException {
        final List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("testElement"));
        saxOutputter.output(contentList);
    }

    @org.junit.Test
    public void testSAXOutputterElementOutput() throws JDOMException {
        final Element element = new Element("testElement");
        saxOutputter.output(element);
    }

    @org.junit.Test
    public void testSAXOutputterFragmentListOutput() throws JDOMException {
        final List<Content> contentList = new ArrayList<>();
        contentList.add(new Text("text content"));
        saxOutputter.outputFragment(contentList);
    }

    @org.junit.Test
    public void testSAXOutputterFragmentTextOutput() throws JDOMException {
        final Text text = new Text("text content");
        saxOutputter.outputFragment(text);
    }

    @org.junit.Test(expected = JDOMException.class)
    public void testSAXOutputterInvalidFragmentOutput() throws JDOMException {
        final Element element = new Element("testElement");
        saxOutputter.outputFragment(element);
    }
}