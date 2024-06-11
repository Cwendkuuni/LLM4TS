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
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());

        SAXOutputter outputterWithContentHandler = new SAXOutputter(new DocumentContentHandler());
        assertSame(outputterWithContentHandler.getContentHandler(), new DocumentContentHandler());
        assertNull(outputterWithContentHandler.getErrorHandler());
        assertNull(outputterWithContentHandler.getDTDHandler());
        assertNull(outputterWithContentHandler.getEntityResolver());
        assertNull(outputterWithContentHandler.getLexicalHandler());
        assertNull(outputterWithContentHandler.getDeclHandler());
        assertFalse(outputterWithContentHandler.getReportNamespaceDeclarations());
        assertTrue(outputterWithContentHandler.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithContentHandler.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithContentHandler.getFormat());

        SAXOutputter outputterWithContentHandlerErrorHandler = new SAXOutputter(new DocumentContentHandler(), new SAXErrorHandler());
        assertSame(outputterWithContentHandlerErrorHandler.getContentHandler(), new DocumentContentHandler());
        assertSame(outputterWithContentHandlerErrorHandler.getErrorHandler(), new SAXErrorHandler());
        assertNull(outputterWithContentHandlerErrorHandler.getDTDHandler());
        assertNull(outputterWithContentHandlerErrorHandler.getEntityResolver());
        assertNull(outputterWithContentHandlerErrorHandler.getLexicalHandler());
        assertNull(outputterWithContentHandlerErrorHandler.getDeclHandler());
        assertFalse(outputterWithContentHandlerErrorHandler.getReportNamespaceDeclarations());
        assertTrue(outputterWithContentHandlerErrorHandler.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithContentHandlerErrorHandler.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithContentHandlerErrorHandler.getFormat());

        SAXOutputter outputterWithAllHandlers = new SAXOutputter(new DocumentContentHandler(), new SAXErrorHandler(), new DefaultDTDHandler(), new IdentityEntityResolver(), new DefaultLexicalHandler(), new DefaultDeclHandler());
        assertSame(outputterWithAllHandlers.getContentHandler(), new DocumentContentHandler());
        assertSame(outputterWithAllHandlers.getErrorHandler(), new SAXErrorHandler());
        assertSame(outputterWithAllHandlers.getDTDHandler(), new DefaultDTDHandler());
        assertSame(outputterWithAllHandlers.getEntityResolver(), new IdentityEntityResolver());
        assertSame(outputterWithAllHandlers.getLexicalHandler(), new DefaultLexicalHandler());
        assertSame(outputterWithAllHandlers.getDeclHandler(), new DefaultDeclHandler());
        assertFalse(outputterWithAllHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithAllHandlers.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithAllHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithAllHandlers.getFormat());

        SAXOutputter outputterWithCustomProcessor = new SAXOutputter(null, null, null, null, null, null, null, null, null, null, new CustomSAXOutputProcessor());
        assertSame(outputterWithCustomProcessor.getSAXOutputProcessor(), new CustomSAXOutputProcessor());
        assertNull(outputterWithCustomProcessor.getContentHandler());
        assertNull(outputterWithCustomProcessor.getErrorHandler());
        assertNull(outputterWithCustomProcessor.getDTDHandler());
        assertNull(outputterWithCustomProcessor.getEntityResolver());
        assertNull(outputterWithCustomProcessor.getLexicalHandler());
        assertNull(outputterWithCustomProcessor.getDeclHandler());
        assertFalse(outputterWithCustomProcessor.getReportNamespaceDeclarations());
        assertTrue(outputterWithCustomProcessor.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputterWithCustomProcessor.getFormat());
    }

    @Test
    public void testSettersAndGetters() {
        SAXOutputter outputter = new SAXOutputter();

        outputter.setContentHandler(new DocumentContentHandler());
        assertSame(outputter.getContentHandler(), new DocumentContentHandler());

        outputter.setErrorHandler(new SAXErrorHandler());
        assertSame(outputter.getErrorHandler(), new SAXErrorHandler());

        outputter.setDTDHandler(new DefaultDTDHandler());
        assertSame(outputter.getDTDHandler(), new DefaultDTDHandler());

        outputter.setEntityResolver(new IdentityEntityResolver());
        assertSame(outputter.getEntityResolver(), new IdentityEntityResolver());

        outputter.setLexicalHandler(new DefaultLexicalHandler());
        assertSame(outputter.getLexicalHandler(), new DefaultLexicalHandler());

        outputter.setDeclHandler(new DefaultDeclHandler());
        assertSame(outputter.getDeclHandler(), new DefaultDeclHandler());

        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());

        SAXOutputProcessor processor = new CustomSAXOutputProcessor();
        outputter.setSAXOutputProcessor(processor);
        assertSame(outputter.getSAXOutputProcessor(), processor);

        Format format = new Format();
        outputter.setFormat(format);
        assertSame(outputter.getFormat(), format);
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new Document();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
    }

    @Test
    public void testOutputListOfNodes() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        nodes.add(root);
        outputter.output(nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(root);
    }

    @Test
    public void testOutputFragmentListOfNodes() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        nodes.add(new Text("text"));
        outputter.outputFragment(nodes);
    }

    @Test
    public void testOutputFragmentElement() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(root);
    }

    @Test
    public void testOutputFragmentCDATA() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        CDATA cdata = new CDATA("cdata");
        root.addContent(cdata);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(cdata);
    }

    @Test
    public void testOutputFragmentComment() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        Comment comment = new Comment("comment");
        root.addContent(comment);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(comment);
    }

    @Test
    public void testOutputFragmentEntityRef() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        EntityRef entityRef = new EntityRef("entity");
        root.addContent(entityRef);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(entityRef);
    }

    @Test
    public void testOutputFragmentProcessingInstruction() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        ProcessingInstruction processingInstruction = new ProcessingInstruction("target", "data");
        root.addContent(processingInstruction);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(processingInstruction);
    }

    @Test
    public void testOutputFragmentText() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        Text text = new Text("text");
        root.addContent(text);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(text);
    }
}

class CustomSAXOutputProcessor extends AbstractSAXOutputProcessor {
    // Add any custom logic here if needed.
}

class DocumentContentHandler extends ContentHandler {
    // Add any custom logic here if needed.
}

class SAXErrorHandler extends ErrorHandler {
    // Add any custom logic here if needed.
}

class DefaultDTDHandler implements DTDHandler {
    // Add any custom logic here if needed.
}

class IdentityEntityResolver implements EntityResolver {
    // Add any custom logic here if needed.
}

class DefaultLexicalHandler implements LexicalHandler {
    // Add any custom logic here if needed.
}

class DefaultDeclHandler implements DeclHandler {
    // Add any custom logic here if needed.
}