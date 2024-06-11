package org.jdom2.output;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.jdom2.output.support.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        ContentHandler targetContentHandler = mock(ContentHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(targetContentHandler);
        assertEquals(targetContentHandler, saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
        ContentHandler targetContentHandler = mock(ContentHandler.class);
        ErrorHandler targetErrorHandler = mock(ErrorHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(targetContentHandler, targetErrorHandler);
        assertEquals(targetContentHandler, saxOutputter.getContentHandler());
        assertEquals(targetErrorHandler, saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
        ContentHandler targetContentHandler = mock(ContentHandler.class);
        ErrorHandler targetErrorHandler = mock(ErrorHandler.class);
        DTDHandler targetDTDHandler = mock(DTDHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(targetContentHandler, targetErrorHandler, targetDTDHandler);
        assertEquals(targetContentHandler, saxOutputter.getContentHandler());
        assertEquals(targetErrorHandler, saxOutputter.getErrorHandler());
        assertEquals(targetDTDHandler, saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
        ContentHandler targetContentHandler = mock(ContentHandler.class);
        ErrorHandler targetErrorHandler = mock(ErrorHandler.class);
        DTDHandler targetDTDHandler = mock(DTDHandler.class);
        EntityResolver targetEntityResolver = mock(EntityResolver.class);
        SAXOutputter saxOutputter = new SAXOutputter(targetContentHandler, targetErrorHandler, targetDTDHandler, targetEntityResolver);
        assertEquals(targetContentHandler, saxOutputter.getContentHandler());
        assertEquals(targetErrorHandler, saxOutputter.getErrorHandler());
        assertEquals(targetDTDHandler, saxOutputter.getDTDHandler());
        assertEquals(targetEntityResolver, saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterSAXOutputProcessorFormatConstructor() {
        SAXOutputProcessor targetSAXOutputProcessor = mock(SAXOutputProcessor.class);
        Format targetFormat = mock(Format.class);
        SAXOutputter saxOutputter = new SAXOutputter(targetSAXOutputProcessor, targetFormat);
        assertEquals(targetSAXOutputProcessor, saxOutputter.getSAXOutputProcessor());
        assertEquals(targetFormat, saxOutputter.getFormat());
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterSetContentHandler() {
        ContentHandler targetContentHandler = mock(ContentHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(targetContentHandler);
        assertEquals(targetContentHandler, saxOutputter.getContentHandler());
    }

    @Test
    public void testSAXOutputterGetContentHandler() {
        ContentHandler targetContentHandler = mock(ContentHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(targetContentHandler);
        assertEquals(targetContentHandler, saxOutputter.getContentHandler());
    }

    @Test
    public void testSAXOutputterSetErrorHandler() {
        ErrorHandler targetErrorHandler = mock(ErrorHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setErrorHandler(targetErrorHandler);
        assertEquals(targetErrorHandler, saxOutputter.getErrorHandler());
    }

    @Test
    public void testSAXOutputterGetErrorHandler() {
        ErrorHandler targetErrorHandler = mock(ErrorHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(null, targetErrorHandler);
        assertEquals(targetErrorHandler, saxOutputter.getErrorHandler());
    }

    @Test
    public void testSAXOutputterSetDTDHandler() {
        DTDHandler targetDTDHandler = mock(DTDHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setDTDHandler(targetDTDHandler);
        assertEquals(targetDTDHandler, saxOutputter.getDTDHandler());
    }

    @Test
    public void testSAXOutputterGetDTDHandler() {
        DTDHandler targetDTDHandler = mock(DTDHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(null, null, targetDTDHandler);
        assertEquals(targetDTDHandler, saxOutputter.getDTDHandler());
    }

    @Test
    public void testSAXOutputterSetEntityResolver() {
        EntityResolver targetEntityResolver = mock(EntityResolver.class);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setEntityResolver(targetEntityResolver);
        assertEquals(targetEntityResolver, saxOutputter.getEntityResolver());
    }

    @Test
    public void testSAXOutputterGetEntityResolver() {
        EntityResolver targetEntityResolver = mock(EntityResolver.class);
        SAXOutputter saxOutputter = new SAXOutputter(null, null, null, targetEntityResolver);
        assertEquals(targetEntityResolver, saxOutputter.getEntityResolver());
    }

    @Test
    public void testSAXOutputterSetLexicalHandler() {
        LexicalHandler targetLexicalHandler = mock(LexicalHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setLexicalHandler(targetLexicalHandler);
        assertEquals(targetLexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testSAXOutputterGetLexicalHandler() {
        LexicalHandler targetLexicalHandler = mock(LexicalHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(null, null, null, null, targetLexicalHandler);
        assertEquals(targetLexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testSAXOutputterSetDeclHandler() {
        DeclHandler targetDeclHandler = mock(DeclHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setDeclHandler(targetDeclHandler);
        assertEquals(targetDeclHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void testSAXOutputterGetDeclHandler() {
        DeclHandler targetDeclHandler = mock(DeclHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(null, null, null, null, null, targetDeclHandler);
        assertEquals(targetDeclHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void testSAXOutputterGetReportNamespaceDeclarations() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSAXOutputterSetReportNamespaceDeclarations() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportNamespaceDeclarations(true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSAXOutputterGetReportDTDEvents() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterSetReportDTDEvents() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportDTDEvents(false);
        assertFalse(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterSetFeature() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testSAXOutputterSetFeatureUnsupported() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/validation", true);
    }

    @Test(expected = SAXNotSupportedException.class)
    public void testSAXOutputterSetFeatureNotSupported() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
    }

    @Test
    public void testSAXOutputterGetFeature() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertTrue(saxOutputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));
        assertFalse(saxOutputter.getFeature("http://xml.org/sax/features/namespaces"));
        assertFalse(saxOutputter.getFeature("http://xml.org/sax/features/validation"));
    }

    @Test
    public void testSAXOutputterSetProperty() {
        SAXOutputter saxOutputter = new SAXOutputter();
        LexicalHandler targetLexicalHandler = mock(LexicalHandler.class);
        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", targetLexicalHandler);
        assertEquals(targetLexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testSAXOutputterGetProperty() {
        SAXOutputter saxOutputter = new SAXOutputter();
        DeclHandler targetDeclHandler = mock(DeclHandler.class);
        saxOutputter.setDeclHandler(targetDeclHandler);
        assertEquals(targetDeclHandler, saxOutputter.getProperty("http://xml.org/sax/handlers/DeclHandler"));
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testSAXOutputterSetPropertyUnrecognized() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", mock(DeclHandler.class));
    }

    @Test(expected = SAXNotSupportedException.class)
    public void testSAXOutputterSetPropertyNotSupported() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setProperty("http://xml.org/sax/properties/input-source", mock(InputSource.class));
    }

    @Test
    public void testSAXOutputterGetSAXOutputProcessor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testSAXOutputterSetSAXOutputProcessor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        SAXOutputProcessor targetSAXOutputProcessor = mock(SAXOutputProcessor.class);
        saxOutputter.setSAXOutputProcessor(targetSAXOutputProcessor);
        assertEquals(targetSAXOutputProcessor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testSAXOutputterGetFormat() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterSetFormat() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Format targetFormat = mock(Format.class);
        saxOutputter.setFormat(targetFormat);
        assertEquals(targetFormat, saxOutputter.getFormat());
    }
}