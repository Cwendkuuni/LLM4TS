package org.junit.tests.saxoutputter;

import org.junit.Before;
import org.junit.Test;
import org.jdom2.DocumentBuilderFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXOutputTarget;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SAXOutputterTest {
    private SAXOutputter saxOutputter;
    private SAXOutputTarget saxOutputTarget;

    @Before
    public void setup() {
        saxOutputter = new SAXOutputter();
        saxOutputTarget = new SAXOutputTarget();
    }

    

    @Test
    public void testSAXOutputterDefaultConstructor() {
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertFalse(saxOutputter.getReportDTDEvents());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNull(saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        saxOutputter = new SAXOutputter(new ContentHandler() {});
        assertNull(saxOutputter.getDeclareNamespaces());
        assertNull(saxOutputter.getReportDtdEvents());
        assertNull(saxOutputter.getProcessor());
        assertNull(saxOutputter.getFormat());
        assertNotNull(saxOutputter.getContentHandler());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
        saxOutputter = new SAXOutputter(new ContentHandler() {}, new ErrorHandler() {}, new DTDHandler() {}, new EntityResolver() {});
        assertNull(saxOutputter.getDeclareNamespaces());
        assertNull(saxOutputter.getReportDtdEvents());
        assertNull(saxOutputter.getProcessor());
        assertNull(saxOutputter.getFormat());
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNotNull(saxOutputter.getEntityResolver());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
        saxOutputter = new SAXOutputter(new ContentHandler() {}, new ErrorHandler() {}, new DTDHandler() {}, new EntityResolver() {}, new LexicalHandler() {});
        assertNull(saxOutputter.getDeclareNamespaces());
        assertNull(saxOutputter.getReportDtdEvents());
        assertNull(saxOutputter.getProcessor());
        assertNull(saxOutputter.getFormat());
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNotNull(saxOutputter.getEntityResolver());
        assertNotNull(saxOutputter.getLexicalHandler());
    }

    @Test
    public void testSAXOutputterProcessorFormatContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = new Element("root");
        document.setDocumentElement(root);
        saxOutputter = new SAXOutputter(new SAXOutputProcessor() {}, Format.getRawFormat(), new ContentHandler() {}, new ErrorHandler() {}, new DTDHandler() {}, new EntityResolver() {}, new LexicalHandler() {});
        saxOutputter.output(document);
        assertNull(saxOutputter.getDeclareNamespaces());
        assertNull(saxOutputter.getReportDtdEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNotNull(saxOutputter.getEntityResolver());
        assertNotNull(saxOutputter.getLexicalHandler());
    }

    @Test
    public void testSetContentHandler() {
        saxOutputter = new SAXOutputter();
        ContentHandler contentHandler = new ContentHandler() {};
        saxOutputter.setContentHandler(contentHandler);
        assertNotNull(saxOutputter.getContentHandler());
    }

    @Test
    public void testGetContentHandler() {
        saxOutputter = new SAXOutputter();
        ContentHandler contentHandler = new ContentHandler() {};
        saxOutputter.setContentHandler(contentHandler);
        assertSame(contentHandler, saxOutputter.getContentHandler());
    }

    @Test
    public void testSetErrorHandler() {
        saxOutputter = new SAXOutputter();
        ErrorHandler errorHandler = new ErrorHandler() {};
        saxOutputter.setErrorHandler(errorHandler);
        assertNotNull(saxOutputter.getErrorHandler());
    }

    @Test
    public void testGetErrorHandler() {
        saxOutputter = new SAXOutputter();
        ErrorHandler errorHandler = new ErrorHandler() {};
        saxOutputter.setErrorHandler(errorHandler);
        assertSame(errorHandler, saxOutputter.getErrorHandler());
    }

    @Test
    public void testSetDTDHandler() {
        saxOutputter = new SAXOutputter();
        DTDHandler dtdHandler = new DTDHandler() {};
        saxOutputter.setDTDHandler(dtdHandler);
        assertNotNull(saxOutputter.getDTDHandler());
    }

    @Test
    public void testGetDTDHandler() {
        saxOutputter = new SAXOutputter();
        DTDHandler dtdHandler = new DTDHandler() {};
        saxOutputter.setDTDHandler(dtdHandler);
        assertSame(dtdHandler, saxOutputter.getDTDHandler());
    }

    @Test
    public void testSetEntityResolver() {
        saxOutputter = new SAXOutputter();
        EntityResolver entityResolver = new EntityResolver() {};
        saxOutputter.setEntityResolver(entityResolver);
        assertNotNull(saxOutputter.getEntityResolver());
    }

    @Test
    public void testGetEntityResolver() {
        saxOutputter = new SAXOutputter();
        EntityResolver entityResolver = new EntityResolver() {};
        saxOutputter.setEntityResolver(entityResolver);
        assertSame(entityResolver, saxOutputter.getEntityResolver());
    }

    @Test
    public void testSetLexicalHandler() {
        saxOutputter = new SAXOutputter();
        LexicalHandler lexicalHandler = new LexicalHandler() {};
        saxOutputter.setLexicalHandler(lexicalHandler);
        assertNotNull(saxOutputter.getLexicalHandler());
    }

    @Test
    public void testGetLexicalHandler() {
        saxOutputter = new SAXOutputter();
        LexicalHandler lexicalHandler = new LexicalHandler() {};
        saxOutputter.setLexicalHandler(lexicalHandler);
        assertSame(lexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testSetDeclHandler() {
        saxOutputter = new SAXOutputter();
        DeclHandler declHandler = new DeclHandler() {};
        saxOutputter.setDeclHandler(declHandler);
        assertNotNull(saxOutputter.getDeclHandler());
    }

    @Test
    public void testGetDeclHandler() {
        saxOutputter = new SAXOutputter();
        DeclHandler declHandler = new DeclHandler() {};
        saxOutputter.setDeclHandler(declHandler);
        assertSame(declHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void testGetReportNamespaceDeclarations() {
        saxOutputter = new SAXOutputter();
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSetReportNamespaceDeclarations() {
        saxOutputter = new SAXOutputter();
        saxOutputter.setReportNamespaceDeclarations(true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testGetReportDTDEvents() {
        saxOutputter = new SAXOutputter();
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSetReportDTDEvents() {
        saxOutputter = new SAXOutputter();
        saxOutputter.setReportDTDEvents(false);
        assertFalse(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSetSAXOutputProcessor() {
        SAXOutputProcessor saxOutputProcessor = new SAXOutputProcessor() {};
        saxOutputter = new SAXOutputter();
        saxOutputter.setSAXOutputProcessor(saxOutputProcessor);
        assertSame(saxOutputProcessor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testGetSAXOutputProcessor() {
        SAXOutputProcessor saxOutputProcessor = new SAXOutputProcessor() {};
        saxOutputter = new SAXOutputter();
        saxOutputter.setSAXOutputProcessor(saxOutputProcessor);
        assertSame(saxOutputProcessor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        Format format = new Format() {};
        saxOutputter = new SAXOutputter();
        saxOutputter.setFormat(format);
        assertSame(format, saxOutputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        Format format = new Format() {};
        saxOutputter = new SAXOutputter();
        saxOutputter.setFormat(format);
        assertSame(format, saxOutputter.getFormat());
    }

    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = new Element("root");
        document.setDocumentElement(root);
        saxOutputter.output(document);
        assertNull(saxOutputTarget.getDocument());
    }

    @Test
    public void testOutputList() throws JDOMException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = new Element("root");
        document.setDocumentElement(root);
        List<Content> nodes = new ArrayList<>();
        nodes.add(new Element("element1"));
        nodes.add(new Element("element2"));
        saxOutputter.output(nodes);
        assertNull(saxOutputTarget.getDocument());
    }

    @Test
    public void testOutputElement() throws JDOMException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = new Element("root");
        document.setDocumentElement(root);
        Element element = new Element("element");
        saxOutputter.output(element);
        assertNull(saxOutputTarget.getDocument());
    }

    @Test
    public void testOutputFragmentList() throws JDOMException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = new Element("root");
        document.setDocumentElement(root);
        List<Content> nodes = new ArrayList<>();
        nodes.add(new Element("element1"));
        nodes.add(new Element("element2"));
        saxOutputter.outputFragment(nodes);
        assertNull(saxOutputTarget.getDocument());
    }

    @Test
    public void testOutputFragmentElement() throws JDOMException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = new Element("root");
        document.setDocumentElement(root);
        Element element = new Element("element");
        saxOutputter.outputFragment(element);
        assertNull(saxOutputTarget.getDocument());
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullElement() throws JDOMException {
        saxOutputter.outputFragment(null);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullList() throws JDOMException {
        saxOutputter.outputFragment(null);
    }

    
}