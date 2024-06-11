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
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());

        ContentHandler handler = new HandlerMock();
        ErrorHandler errorHandler = new ErrorHandlerMock();
        DTDHandler dtdHandler = new DTDHandlerMock();
        EntityResolver entityResolver = new EntityResolverMock();
        LexicalHandler lexicalHandler = new LexicalHandlerMock();
        DeclHandler declHandler = new DeclHandlerMock();
        SAXOutputter outputterWithHandlers = new SAXOutputter(handler, errorHandler, dtdHandler, entityResolver, lexicalHandler, declHandler);
        assertEquals(handler, outputterWithHandlers.getContentHandler());
        assertEquals(errorHandler, outputterWithHandlers.getErrorHandler());
        assertEquals(dtdHandler, outputterWithHandlers.getDTDHandler());
        assertEquals(entityResolver, outputterWithHandlers.getEntityResolver());
        assertEquals(lexicalHandler, outputterWithHandlers.getLexicalHandler());
        assertEquals(declHandler, outputterWithHandlers.getDeclHandler());
        assertFalse(outputterWithHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithHandlers.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithHandlers.getFormat());

        SAXOutputter outputterWithProcessor = new SAXOutputter(null, null, null, null, null, null, new DefaultSAXOutputProcessor());
        assertEquals(new DefaultSAXOutputProcessor(), outputterWithProcessor.getSAXOutputProcessor());

        Format format = new DefaultFormat();
        SAXOutputter outputterWithFormat = new SAXOutputter(null, format, null, null, null, null);
        assertEquals(format, outputterWithFormat.getFormat());

        SAXOutputter outputterWithAll = new SAXOutputter(new DefaultSAXOutputProcessor(), format, handler, errorHandler, dtdHandler, entityResolver, lexicalHandler, declHandler);
        assertEquals(handler, outputterWithAll.getContentHandler());
        assertEquals(errorHandler, outputterWithAll.getErrorHandler());
        assertEquals(dtdHandler, outputterWithAll.getDTDHandler());
        assertEquals(entityResolver, outputterWithAll.getEntityResolver());
        assertEquals(lexicalHandler, outputterWithAll.getLexicalHandler());
        assertEquals(declHandler, outputterWithAll.getDeclHandler());
        assertFalse(outputterWithAll.getReportNamespaceDeclarations());
        assertTrue(outputterWithAll.getReportDTDEvents());
        assertEquals(new DefaultSAXOutputProcessor(), outputterWithAll.getSAXOutputProcessor());
        assertEquals(format, outputterWithAll.getFormat());
    }

    @Test
    public void testSetters() {
        SAXOutputter outputter = new SAXOutputter();

        ContentHandler handler = new HandlerMock();
        outputter.setContentHandler(handler);
        assertEquals(handler, outputter.getContentHandler());

        ErrorHandler errorHandler = new ErrorHandlerMock();
        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());

        DTDHandler dtdHandler = new DTDHandlerMock();
        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());

        EntityResolver entityResolver = new EntityResolverMock();
        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());

        LexicalHandler lexicalHandler = new LexicalHandlerMock();
        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());

        DeclHandler declHandler = new DeclHandlerMock();
        outputter.setDeclHandler(declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());

        boolean declareNamespaces = true;
        outputter.setReportNamespaceDeclarations(declareNamespaces);
        assertTrue(outputter.getReportNamespaceDeclarations());

        boolean reportDtdEvents = false;
        outputter.setReportDTDEvents(reportDtdEvents);
        assertFalse(outputter.getReportDTDEvents());

        SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        outputter.setSAXOutputProcessor(processor);
        assertEquals(processor, outputter.getSAXOutputProcessor());

        Format format = new DefaultFormat();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(doc);

        SAXOutputter outputterWithHandlers = new SAXOutputter(null, null, null, null, null, null);
        outputterWithHandlers.setContentHandler(new ContentHandlerMock());
        outputterWithHandlers.output(doc);

        SAXOutputter outputterWithFormat = new SAXOutputter(null, new DefaultFormat(), null, null, null, null);
        outputterWithFormat.setFormat(Format.getPrettyFormat());
        outputterWithFormat.output(doc);

        SAXOutputter outputterWithHandlersAndFormat = new SAXOutputter(null, Format.getPrettyFormat(), null, null, null, null);
        outputterWithHandlersAndFormat.setContentHandler(new ContentHandlerMock());
        outputterWithHandlersAndFormat.output(doc);
    }

    @Test
    public void testOutputNodes() throws JDOMException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Text("Text1"));
        root.addContent(new Comment("Comment1"));
        root.addContent(new Element("Child1").setText("Child1Text"));
        root.addContent(new Element("Child2").setText("Child2Text"));
        List<Content> nodes = doc.getContent();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(nodes);

        SAXOutputter outputterWithHandlers = new SAXOutputter(null, null, null, null, null, null);
        outputterWithHandlers.setContentHandler(new ContentHandlerMock());
        outputterWithHandlers.output(nodes);

        SAXOutputter outputterWithFormat = new SAXOutputter(null, new DefaultFormat(), null, null, null, null);
        outputterWithFormat.setFormat(Format.getPrettyFormat());
        outputterWithFormat.output(nodes);

        SAXOutputter outputterWithHandlersAndFormat = new SAXOutputter(null, Format.getPrettyFormat(), null, null, null, null);
        outputterWithHandlersAndFormat.setContentHandler(new ContentHandlerMock());
        outputterWithHandlersAndFormat.output(nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(root);

        SAXOutputter outputterWithHandlers = new SAXOutputter(null, null, null, null, null, null);
        outputterWithHandlers.setContentHandler(new ContentHandlerMock());
        outputterWithHandlers.output(root);

        SAXOutputter outputterWithFormat = new SAXOutputter(null, new DefaultFormat(), null, null, null, null);
        outputterWithFormat.setFormat(Format.getPrettyFormat());
        outputterWithFormat.output(root);

        SAXOutputter outputterWithHandlersAndFormat = new SAXOutputter(null, Format.getPrettyFormat(), null, null, null, null);
        outputterWithHandlersAndFormat.setContentHandler(new ContentHandlerMock());
        outputterWithHandlersAndFormat.output(root);
    }

    @Test
    public void testOutputFragment() throws JDOMException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        Element child = new Element("child");
        Text text = new Text("Text1");
        child.addContent(text);
        doc.addContent(child);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(doc.getContent());

        SAXOutputter outputterWithHandlers = new SAXOutputter(null, null, null, null, null, null);
        outputterWithHandlers.setContentHandler(new ContentHandlerMock());
        outputterWithHandlers.outputFragment(doc.getContent());

        SAXOutputter outputterWithFormat = new SAXOutputter(null, new DefaultFormat(), null, null, null, null);
        outputterWithFormat.setFormat(Format.getPrettyFormat());
        outputterWithFormat.outputFragment(doc.getContent());

        SAXOutputter outputterWithHandlersAndFormat = new SAXOutputter(null, Format.getPrettyFormat(), null, null, null, null);
        outputterWithHandlersAndFormat.setContentHandler(new ContentHandlerMock());
        outputterWithHandlersAndFormat.outputFragment(doc.getContent());
    }

    @Test
    public void testHandleError() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        JDOMException exception = new JDOMException("Test exception");
        outputter.handleError(exception);

        SAXOutputter outputterWithErrorHandler = new SAXOutputter(null, null, null, null, null, null);
        outputterWithErrorHandler.setErrorHandler(new ErrorHandlerMock());
        JDOMException exceptionWithHandler = new JDOMException("Test exception");
        outputterWithErrorHandler.handleError(exceptionWithHandler);
    }
}

class HandlerMock implements ContentHandler, DeclHandler {
    @Override
    public void startDocument() throws SAXException {}

    @Override
    public void endDocument() throws SAXException {}

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {}

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {}

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {}

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

    @Override
    public void processingInstruction(String target, String data) throws SAXException {}

    @Override
    public void skippedEntity(String name) throws SAXException {}

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {}

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {}

    @Override
    public void setDocumentLocator(Locator locator) {}

    @Override
    public void declaration(String name, String publicId, String systemId, String type) throws SAXException {}

    @Override
    public void externalSubset(String systemId, String baseURI) throws SAXException {}
}

class ErrorHandlerMock implements ErrorHandler {
    @Override
    public void warning(SAXParseException exception) throws SAXException {}

    @Override
    public void error(SAXParseException exception) throws SAXException {}

    @Override
    public void fatalError(SAXParseException exception) throws SAXException, SAXException {
        throw exception;
    }
}

class DTDHandlerMock implements DTDHandler {
    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {}

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {}

    @Override
    public void elementDecl(String name, String contentModel) throws SAXException {}

    @Override
    public void attributeDecl(String eName, String name, String type, String defaultValue) throws SAXException {}

    @Override
    public void internalSubset(String systemId, String baseURI) throws SAXException {}

    @Override
    public void externalSubset(String systemId, String baseURI) throws SAXException {}
}

class EntityResolverMock implements EntityResolver {
    @Override
    public InputSource getInputSource(String name) throws SAXException, IOException {
        return new InputSource(new StringReader(""));
    }
}

class LexicalHandlerMock implements LexicalHandler {
    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {}

    @Override
    public void endDTD() throws SAXException {}

    @Override
    public void startEntity(String name) throws SAXException {}

    @Override
    public void endEntity(String name) throws SAXException {}

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        attributes.addAttribute("", "test", "", "CDATA", "Test");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {}

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {}

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

    @Override
    public void processingInstruction(String target, String data) throws SAXException {}

    @Override
    public void skippedEntity(String name) throws SAXException {}
}

class DefaultFormat extends Format {
}

class DefaultSAXOutputProcessor extends AbstractSAXOutputProcessor {
}