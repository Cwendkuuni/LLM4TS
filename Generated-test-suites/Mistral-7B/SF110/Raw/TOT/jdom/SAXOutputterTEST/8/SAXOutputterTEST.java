package org.jdom2.output.test;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class SAXOutputterTest {

    @org.junit.Test
    public void testDefaultSAXOutputter() throws JDOMException, SAXException {
        // Test default constructor
        SAXOutputter outputter = new SAXOutputter();

        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello World"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        outputter.setFormat(Format.getPrettyFormat());
        outputter.setContentHandler(new PrettyPrintingContentHandler(writer));
        outputter.output(document);

        String expectedXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<root>\n<text>Hello World</text>\n</root>";
        assertEquals(expectedXml, writer.toString());

        // Test constructor with ContentHandler
        outputter = new SAXOutputter(new PrettyPrintingContentHandler(new StringWriter()));
        outputter.output(document);
        assertEquals(expectedXml, outputter.getContentHandler().getOutputString());

        // Test constructor with multiple handlers
        outputter = new SAXOutputter(new PrettyPrintingContentHandler(new StringWriter()), new MyErrorHandler(), null, null);
        document = new Document();
        document.setErrorHandler(new MyErrorHandler());
        outputter.output(document);
        assertTrue(outputter.getErrorHandler().getErrorCount() == 0);

        // Test constructor with all handlers
        outputter = new SAXOutputter(new PrettyPrintingContentHandler(new StringWriter()), new MyErrorHandler(), new MyDTDHandler(), new MyEntityResolver(), new MyLexicalHandler(), new MyDeclHandler(), null, false);
        outputter.output(document);
        assertEquals(expectedXml, outputter.getContentHandler().getOutputString());
        assertTrue(outputter.getErrorHandler().getErrorCount() == 0);
        assertTrue(outputter.getDTDHandler().isIgnoringElement("test"));
        assertTrue(outputter.getEntityResolver().getEntity("http://example.com/test").getText().contains("Resolved entity"));
        assertTrue(outputter.getLexicalHandler().isProcessingInstruction("xml", "version"));
        assertEquals(1, outputter.getDeclHandler().getDeclarations().size());
        assertEquals("namespace:test", ((Map.Entry)outputter.getDeclHandler().getDeclarations().entrySet().iterator().next()).getKey().toString());
        assertEquals("URI:http://example.com/test", ((Map.Entry)outputter.getDeclHandler().getDeclarations().entrySet().iterator().next()).getValue().toString());

        // Test setters and getters
        SAXOutputter outputterWithProperties = new SAXOutputter();
        outputterWithProperties.setDeclareNamespaces(true);
        outputterWithProperties.setReportDTDEvents(false);
        outputterWithProperties.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
        outputterWithProperties.setFormat(Format.getRawFormat());
        outputterWithProperties.output(document);
        assertTrue(outputterWithProperties.getReportNamespaceDeclarations());
        assertFalse(outputterWithProperties.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithProperties.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithProperties.getFormat());

        // Test output methods
        List<Element> nodeList = new JDOMNamespaceTest().createNodeList();
        outputterWithProperties.output(nodeList);
        assertEquals(nodeList.size(), outputterWithProperties.getContentHandler().getEventCount());

        Content node = nodeList.get(0);
        outputterWithProperties.outputFragment(node);
        assertEquals(node.getCType(), outputterWithProperties.getContentHandler().getEventType());

        outputterWithProperties.handleError(new JDOMException("Test error"));
        assertFalse(outputterWithProperties.getErrorHandler().getErrorCount() == 0);
    }

    private static class MyErrorHandler implements ErrorHandler {
        private int errorCount = 0;

        @Override
        public void error(SAXParseException exception) throws SAXException {
            errorCount++;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            errorCount++;
        }

        public int getErrorCount() {
            return errorCount;
        }
    }

    private static class MyDTDHandler implements DTDHandler {
        private boolean ignoringElement = false;

        @Override
        public void startElement(String name, String publicId, String systemId) throws SAXException {
            if ("test".equals(name)) {
                ignoringElement = true;
            }
        }

        public boolean isIgnoringElement(String elementName) {
            return ignoringElement;
        }
    }

    private static class MyEntityResolver implements EntityResolver {
        private String entityText = "Resolved entity";

        @Override
        public InputSource getEntity(String name, String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new StringReader(entityText));
        }
    }

    private static class MyLexicalHandler implements LexicalHandler {
        private boolean processingInstruction = false;

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            if ("xml".equals(target)) {
                processingInstruction = "version".equals(data);
            }
        }

        public boolean isProcessingInstruction(String piTarget, String piData) {
            return processingInstruction;
        }
    }

    private static class MyDeclHandler implements DeclHandler {
        private Map<String, String> declarations = new HashMap<>();

        @Override
        public void declaration(String name, String publicId, String systemId, String type, String value) throws SAXException {
            declarations.put(name, value);
        }

        public Map<String, String> getDeclarations() {
            return declarations;
        }
    }
}