package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.support.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterConstruction() {
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

        SAXOutputter outputterWithContentHandler = new SAXOutputter(new SAXContentHandler());
        assertSame(outputterWithContentHandler.getContentHandler(), new SAXContentHandler());
        assertNull(outputterWithContentHandler.getErrorHandler());
        assertNull(outputterWithContentHandler.getDTDHandler());
        assertNull(outputterWithContentHandler.getEntityResolver());
        assertNull(outputterWithContentHandler.getLexicalHandler());
        assertNull(outputterWithContentHandler.getDeclHandler());
        assertFalse(outputterWithContentHandler.getReportNamespaceDeclarations());
        assertTrue(outputterWithContentHandler.getReportDTDEvents());
        assertNull(outputterWithContentHandler.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithContentHandler.getFormat());

        SAXOutputter outputterWithHandlers = new SAXOutputter(new SAXContentHandler(), new SAXErrorHandler(), new SAXDTDHandler(), new SAXEntityResolver());
        assertSame(outputterWithHandlers.getContentHandler(), new SAXContentHandler());
        assertSame(outputterWithHandlers.getErrorHandler(), new SAXErrorHandler());
        assertSame(outputterWithHandlers.getDTDHandler(), new SAXDTDHandler());
        assertSame(outputterWithHandlers.getEntityResolver(), new SAXEntityResolver());
        assertNull(outputterWithHandlers.getLexicalHandler());
        assertNull(outputterWithHandlers.getDeclHandler());
        assertFalse(outputterWithHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithHandlers.getReportDTDEvents());
        assertNull(outputterWithHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithHandlers.getFormat());

        SAXOutputter outputterWithAllHandlers = new SAXOutputter(new SAXContentHandler(), new SAXErrorHandler(), new SAXDTDHandler(), new SAXEntityResolver(), new SAXLexicalHandler(), new SAXDeclHandler());
        assertSame(outputterWithAllHandlers.getContentHandler(), new SAXContentHandler());
        assertSame(outputterWithAllHandlers.getErrorHandler(), new SAXErrorHandler());
        assertSame(outputterWithAllHandlers.getDTDHandler(), new SAXDTDHandler());
        assertSame(outputterWithAllHandlers.getEntityResolver(), new SAXEntityResolver());
        assertSame(outputterWithAllHandlers.getLexicalHandler(), new SAXLexicalHandler());
        assertSame(outputterWithAllHandlers.getDeclHandler(), new SAXDeclHandler());
        assertFalse(outputterWithAllHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithAllHandlers.getReportDTDEvents());
        assertNull(outputterWithAllHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithAllHandlers.getFormat());

        SAXOutputter outputterWithProcessor = new SAXOutputter(new DefaultSAXOutputProcessor(), Format.getRawFormat(), new SAXContentHandler());
        assertSame(outputterWithProcessor.getSAXOutputProcessor(), new DefaultSAXOutputProcessor());
        assertSame(outputterWithProcessor.getFormat(), Format.getRawFormat());
        assertSame(outputterWithProcessor.getContentHandler(), new SAXContentHandler());
        assertNull(outputterWithProcessor.getErrorHandler());
        assertNull(outputterWithProcessor.getDTDHandler());
        assertNull(outputterWithProcessor.getEntityResolver());
        assertNull(outputterWithProcessor.getLexicalHandler());
        assertNull(outputterWithProcessor.getDeclHandler());
        assertFalse(outputterWithProcessor.getReportNamespaceDeclarations());
        assertTrue(outputterWithProcessor.getReportDTDEvents());

        SAXOutputter outputterWithFormat = new SAXOutputter(Format.getPrettyFormat());
        assertNull(outputterWithFormat.getSAXOutputProcessor());
        assertSame(outputterWithFormat.getFormat(), Format.getPrettyFormat());
        assertNull(outputterWithFormat.getContentHandler());
        assertNull(outputterWithFormat.getErrorHandler());
        assertNull(outputterWithFormat.getDTDHandler());
        assertNull(outputterWithFormat.getEntityResolver());
        assertNull(outputterWithFormat.getLexicalHandler());
        assertNull(outputterWithFormat.getDeclHandler());
        assertFalse(outputterWithFormat.getReportNamespaceDeclarations());
        assertTrue(outputterWithFormat.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterSetters() {
        SAXOutputter outputter = new SAXOutputter();

        outputter.setContentHandler(new SAXContentHandler());
        assertSame(outputter.getContentHandler(), new SAXContentHandler());

        outputter.setErrorHandler(new SAXErrorHandler());
        assertSame(outputter.getErrorHandler(), new SAXErrorHandler());

        outputter.setDTDHandler(new SAXDTDHandler());
        assertSame(outputter.getDTDHandler(), new SAXDTDHandler());

        outputter.setEntityResolver(new SAXEntityResolver());
        assertSame(outputter.getEntityResolver(), new SAXEntityResolver());

        outputter.setLexicalHandler(new SAXLexicalHandler());
        assertSame(outputter.getLexicalHandler(), new SAXLexicalHandler());

        outputter.setDeclHandler(new SAXDeclHandler());
        assertSame(outputter.getDeclHandler(), new SAXDeclHandler());

        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());

        outputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
        assertSame(outputter.getSAXOutputProcessor(), new DefaultSAXOutputProcessor());

        outputter.setFormat(Format.getPrettyFormat());
        assertSame(outputter.getFormat(), Format.getPrettyFormat());

        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setReportDTDEvents(true);
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterFeatures() {
        SAXOutputter outputter = new SAXOutputter();

        try {
            outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            fail("SAX not recognized feature: namespace-prefixes");
        } catch (SAXNotRecognizedException e) {
            // expected
        }

        try {
            outputter.setFeature("http://xml.org/sax/features/namespaces", false);
            throw new AssertionError("SAX not supported feature: namespaces");
        } catch (SAXNotSupportedException e) {
            // expected
        }

        try {
            outputter.setFeature("http://xml.org/sax/features/validation", false);
            outputter.setReportDTDEvents(false);
            fail("SAX not supported feature: validation without disabling reportDTDEvents");
        } catch (SAXNotSupportedException e) {
            // expected
        }

        outputter.setFeature("http://xml.org/sax/features/validation", true);
        assertTrue(outputter.getReportDTDEvents());

        try {
            outputter.setFeature("http://xml.org/sax/features/validation", true);
            outputter.setReportDTDEvents(false);
            fail("SAX not recognized feature: disabling reportDTDEvents while validation is enabled");
        } catch (SAXNotRecognizedException e) {
            // expected
        }

        outputter.setFeature("http://xml.org/sax/features/validation", false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterProperties() {
        SAXOutputter outputter = new SAXOutputter();

        outputter.setProperty("http://xml.org/sax/properties/lexical-handler", new SAXLexicalHandler());
        assertSame(outputter.getLexicalHandler(), new SAXLexicalHandler());

        outputter.setProperty("http://xml.org/sax/properties/declaration-handler", new SAXDeclHandler());
        assertSame(outputter.getDeclHandler(), new SAXDeclHandler());

        try {
            outputter.setProperty("http://xml.org/sax/properties/declaration-handler", "InvalidType");
            fail("Invalid property value type: 'InvalidType'");
        } catch (SAXNotRecognizedException e) {
            // expected
        }

        try {
            outputter.setProperty("http://xml.org/sax/properties/declaration-handler", null);
            fail("Null property value not allowed");
        } catch (SAXNotRecognizedException e) {
            // expected
        }
    }

    @Test
    public void testSAXOutputter() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        doc.setRootElement(root);

        SAXOutputter outputter = new SAXOutputter();
        outputter.output(doc);

        // Test output methods
        outputter.output(root);
        outputter.outputFragment(root);
        outputter.outputFragment(new Element("child"));

        outputter = new SAXOutputter(new DefaultSAXOutputProcessor(), Format.getPrettyFormat());
        outputter.output(doc);
        outputter.output(root);
        outputter.outputFragment(root);
        outputter.outputFragment(new Element("child"));

        outputter = new SAXOutputter(new DefaultSAXOutputProcessor(), Format.getPrettyFormat(), new SAXContentHandler());
        outputter.output(doc);
        outputter.output(root);
        outputter.outputFragment(root);
        outputter.outputFragment(new Element("child"));

        outputter = new SAXOutputter(new DefaultSAXOutputProcessor(), Format.getPrettyFormat(), new SAXContentHandler(), new SAXErrorHandler(), new SAXDTDHandler(), new SAXEntityResolver(), new SAXLexicalHandler(), new SAXDeclHandler());
        outputter.output(doc);
        outputter.output(root);
        outputter.outputFragment(root);
        outputter.outputFragment(new Element("child"));
    }
}