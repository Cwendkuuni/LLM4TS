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

        SAXOutputter outputterWithHandlers = new SAXOutputter(new MyContentHandler(), new MyErrorHandler(), new MyDTDHandler(), new MyEntityResolver());
        assertNotNull(outputterWithHandlers.getContentHandler());
        assertEquals(outputterWithHandlers.getContentHandler(), new MyContentHandler());
        assertNotNull(outputterWithHandlers.getErrorHandler());
        assertEquals(outputterWithHandlers.getErrorHandler(), new MyErrorHandler());
        assertNull(outputterWithHandlers.getDTDHandler());
        assertNull(outputterWithHandlers.getEntityResolver());
        assertNull(outputterWithHandlers.getLexicalHandler());
        assertNull(outputterWithHandlers.getDeclHandler());
        assertFalse(outputterWithHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithHandlers.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithHandlers.getFormat());

        SAXOutputter outputterWithAllHandlers = new SAXOutputter(new MyContentHandler(), new MyErrorHandler(), new MyDTDHandler(), new MyEntityResolver(), new MyLexicalHandler(), new MyDeclHandler());
        assertNotNull(outputterWithAllHandlers.getContentHandler());
        assertEquals(outputterWithAllHandlers.getContentHandler(), new MyContentHandler());
        assertNotNull(outputterWithAllHandlers.getErrorHandler());
        assertEquals(outputterWithAllHandlers.getErrorHandler(), new MyErrorHandler());
        assertNotNull(outputterWithAllHandlers.getDTDHandler());
        assertEquals(outputterWithAllHandlers.getDTDHandler(), new MyDTDHandler());
        assertNotNull(outputterWithAllHandlers.getEntityResolver());
        assertEquals(outputterWithAllHandlers.getEntityResolver(), new MyEntityResolver());
        assertNotNull(outputterWithAllHandlers.getLexicalHandler());
        assertEquals(outputterWithAllHandlers.getLexicalHandler(), new MyLexicalHandler());
        assertNotNull(outputterWithAllHandlers.getDeclHandler());
        assertEquals(outputterWithAllHandlers.getDeclHandler(), new MyDeclHandler());
        assertFalse(outputterWithAllHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithAllHandlers.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithAllHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithAllHandlers.getFormat());

        SAXOutputter outputterWithProcessor = new SAXOutputter(null, null, null, null, null, null, null, null, null, null, new MySAXOutputProcessor(), Format.getRawFormat());
        assertNotNull(outputterWithProcessor.getSAXOutputProcessor());
        assertEquals(outputterWithProcessor.getSAXOutputProcessor(), new MySAXOutputProcessor());
        assertNull(outputterWithProcessor.getContentHandler());
        assertNull(outputterWithProcessor.getErrorHandler());
        assertNull(outputterWithProcessor.getDTDHandler());
        assertNull(outputterWithProcessor.getEntityResolver());
        assertNull(outputterWithProcessor.getLexicalHandler());
        assertNull(outputterWithProcessor.getDeclHandler());
        assertFalse(outputterWithProcessor.getReportNamespaceDeclarations());
        assertTrue(outputterWithProcessor.getReportDTDEvents());

        SAXOutputter outputterWithFormat = new SAXOutputter(null, null, null, null, null, null, null, null, null, null, null, Format.createPrettyPrintFormat());
        assertNotNull(outputterWithFormat.getFormat());
        assertEquals(outputterWithFormat.getFormat(), Format.createPrettyPrintFormat());
        assertNull(outputterWithFormat.getContentHandler());
        assertNull(outputterWithFormat.getErrorHandler());
        assertNull(outputterWithFormat.getDTDHandler());
        assertNull(outputterWithFormat.getEntityResolver());
        assertNull(outputterWithFormat.getLexicalHandler());
        assertNull(outputterWithFormat.getDeclHandler());
        assertFalse(outputterWithFormat.getReportNamespaceDeclarations());
        assertTrue(outputterWithFormat.getReportDTDEvents());

        SAXOutputter outputterWithAll = new SAXOutputter(new MyContentHandler(), new MyErrorHandler(), new MyDTDHandler(), new MyEntityResolver(), new MyLexicalHandler(), new MyDeclHandler(), null, null, null, null, new MySAXOutputProcessor(), Format.createPrettyPrintFormat());
        assertNotNull(outputterWithAll.getSAXOutputProcessor());
        assertEquals(outputterWithAll.getSAXOutputProcessor(), new MySAXOutputProcessor());
        assertNotNull(outputterWithAll.getFormat());
        assertEquals(outputterWithAll.getFormat(), Format.createPrettyPrintFormat());
        assertNotNull(outputterWithAll.getContentHandler());
        assertEquals(outputterWithAll.getContentHandler(), new MyContentHandler());
        assertNotNull(outputterWithAll.getErrorHandler());
        assertEquals(outputterWithAll.getErrorHandler(), new MyErrorHandler());
        assertNotNull(outputterWithAll.getDTDHandler());
        assertEquals(outputterWithAll.getDTDHandler(), new MyDTDHandler());
        assertNotNull(outputterWithAll.getEntityResolver());
        assertEquals(outputterWithAll.getEntityResolver(), new MyEntityResolver());
        assertNotNull(outputterWithAll.getLexicalHandler());
        assertEquals(outputterWithAll.getLexicalHandler(), new MyLexicalHandler());
        assertNotNull(outputterWithAll.getDeclHandler());
        assertEquals(outputterWithAll.getDeclHandler(), new MyDeclHandler());
        assertFalse(outputterWithAll.getReportNamespaceDeclarations());
        assertTrue(outputterWithAll.getReportDTDEvents());
    }

    @Test
    public void testSettingHandlers() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setContentHandler(new MyContentHandler());
        assertEquals(outputter.getContentHandler(), new MyContentHandler());
        outputter.setErrorHandler(new MyErrorHandler());
        assertEquals(outputter.getErrorHandler(), new MyErrorHandler());
        outputter.setDTDHandler(new MyDTDHandler());
        assertEquals(outputter.getDTDHandler(), new MyDTDHandler());
        outputter.setEntityResolver(new MyEntityResolver());
        assertEquals(outputter.getEntityResolver(), new MyEntityResolver());
        outputter.setLexicalHandler(new MyLexicalHandler());
        assertEquals(outputter.getLexicalHandler(), new MyLexicalHandler());
        outputter.setDeclHandler(new MyDeclHandler());
        assertEquals(outputter.getDeclHandler(), new MyDeclHandler());
        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());
        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testSettingProcessorAndFormat() {
        SAXOutputter outputter = new SAXOutputter();
        MySAXOutputProcessor processor = new MySAXOutputProcessor();
        Format format = Format.createPrettyPrintFormat();
        outputter.setSAXOutputProcessor(processor);
        assertEquals(outputter.getSAXOutputProcessor(), processor);
        outputter.setFormat(format);
        assertEquals(outputter.getFormat(), format);
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new JDOMFactory().createDocument();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
        // Additional assertions can be added here.
    }

    @Test
    public void testOutputNodes() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Document document = factory.createDocument();
        Element root = document.getRootElement();
        Element element = root.getChild("child1");
        Text text = element.getTextTrim();
        List<Content> nodes = new ArrayList<>();
        nodes.add(text);
        outputter.output(nodes);
        // Additional assertions can be added here.
    }

    @Test
    public void testOutputElement() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Document document = factory.createDocument();
        Element root = document.getRootElement();
        Element element = root.getChild("child1");
        outputter.output(element);
        // Additional assertions can be added here.
    }

    @Test
    public void testOutputFragment() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Document document = factory.createDocument();
        Element root = document.getRootElement();
        Text text = root.getTextTrim();
        List<Content> nodes = new ArrayList<>();
        nodes.add(text);
        outputter.outputFragment(nodes);
        // Additional assertions can be added here.
    }

    @Test
    public void testOutputFragmentElement() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Document document = factory.createDocument();
        Element root = document.getRootElement();
        Element element = root.getChild("child1");
        outputter.outputFragment(element);
        // Additional assertions can be added here.
    }

    private static class MyContentHandler implements ContentHandler {
        // Implement the methods here.
    }

    private static class MyErrorHandler implements ErrorHandler {
        // Implement the methods here.
    }

    private static class MyDTDHandler implements DTDHandler {
        // Implement the methods here.
    }

    private static class MyEntityResolver implements EntityResolver {
        // Implement the methods here.
    }

    private static class MyLexicalHandler implements LexicalHandler {
        // Implement the methods here.
    }

    private static class MyDeclHandler implements DeclHandler {
        // Implement the methods here.
    }

    private static class MySAXOutputProcessor extends AbstractSAXOutputProcessor {
        // Implement the methods here.
    }
}