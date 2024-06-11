package org.jdom2.output;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jdom2.*;
import org.jdom2.output.support.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(entityResolver, outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(entityResolver, outputter.getEntityResolver());
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterProcessorFormatConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        SAXOutputter outputter = new SAXOutputter(processor, format);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterProcessorFormatContentHandlerConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        ContentHandler contentHandler = mock(ContentHandler.class);
        SAXOutputter outputter = new SAXOutputter(processor, format, contentHandler);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertEquals(contentHandler, outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSettersAndGetters() throws SAXException {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        DeclHandler declHandler = mock(DeclHandler.class);

        outputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());

        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());

        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());

        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());

        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());

        outputter.setDeclHandler(declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());

        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());

        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        outputter.setSAXOutputProcessor(processor);
        assertEquals(processor, outputter.getSAXOutputProcessor());

        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());

        String publicID = "publicID";
        String systemID = "systemID";
        Document doc = new Document();
        when(doc.getDocType()).thenReturn(new DocType("docType", null, null, null, null, null, null, null, null, null, null, null, null, null));
        when(doc.getDocType().getPublicID()).thenReturn(publicID);
        when(doc.getDocType().getSystemID()).thenReturn(systemID);
        SAXTarget target = outputter.buildTarget(doc);
        assertEquals(outputter.getContentHandler(), target.getContentHandler());
        assertEquals(outputter.getErrorHandler(), target.getErrorHandler());
        assertEquals(outputter.getDTDHandler(), target.getDTDHandler());
        assertEquals(outputter.getEntityResolver(), target.getEntityResolver());
        assertEquals(outputter.getLexicalHandler(), target.getLexicalHandler());
        assertEquals(outputter.getDeclHandler(), target.getDeclHandler());
        assertEquals(outputter.getReportNamespaceDeclarations(), target.getReportNamespaceDeclarations());
        assertEquals(outputter.getReportDTDEvents(), target.getReportDTDEvents());
        assertEquals(publicID, target.getPublicID());
        assertEquals(systemID, target.getSystemID());
    }

    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        SAXOutputter outputter = new SAXOutputter();
        Document document = new Document();
        outputter.output(document);
    }

    @Test
    public void testOutputList() throws JDOMException, SAXException {
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        outputter.output(nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException, SAXException {
        SAXOutputter outputter = new SAXOutputter();
        Element node = new Element("node");
        outputter.output(node);
    }

    @Test
    public void testOutputFragmentList() throws JDOMException, SAXException {
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        outputter.outputFragment(nodes);
    }

    @Test
    public void testOutputFragmentContent() throws JDOMException, SAXException {
        SAXOutputter outputter = new SAXOutputter();
        Content node = new Text("node");
        outputter.outputFragment(node);
    }
}