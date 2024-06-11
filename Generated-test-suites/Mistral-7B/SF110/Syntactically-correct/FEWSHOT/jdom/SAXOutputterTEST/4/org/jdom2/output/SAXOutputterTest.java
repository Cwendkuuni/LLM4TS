package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.output.support.SAXTarget;
import org.jdom2.output.support.SAXUtil;
import org.junit.Test;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefault() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text></root>", result);
    }

    @Test
    public void testSAXOutputterContentHandler() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter(new MyContentHandler());
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text></root>", result);
    }

    @Test
    public void testSAXOutputterErrorHandler() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);

        saxOutputter.setErrorHandler(new MyErrorHandler());

        try {
            saxOutputter.output(document);
            fail("Expected an error.");
        } catch (SAXException e) {
            // Expected
        }

        String result = writer.toString();
        assertEquals("", result);
    }

    @Test
    public void testSAXOutputterDTDHandler() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.setDTDHandler(new MyDTDHandler());
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("", result);
    }

    @Test
    public void testSAXOutputterEntityResolver() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.setEntityResolver(new MyEntityResolver());
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("", result);
    }

    @Test
    public void testSAXOutputterLexicalHandler() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.setLexicalHandler(new MyLexicalHandler());
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("", result);
    }

    @Test
    public void testSAXOutputterDeclHandler() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.setDeclHandler(new MyDeclHandler());
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("", result);
    }

    @Test
    public void testSAXOutputterFormat() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        Format format = new Format("test.xml");
        saxOutputter.setFormat(format);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text></root>", result);

        // Verify the output file is created with the specified format
        List<String> lines = new ArrayList<>();
        String line;
        try (Scanner scanner = new Scanner(new File("test.xml"))) {
            while ((line = scanner.nextLine())) {
                lines.add(line);
            }
        }
        String xmlResult = String.join("\n", lines);
        assertEquals("<root><Text>Hello, JDOM!</Text></root>", xmlResult);
    }

    @Test
    public void testSAXOutputterProcessList() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));
        document.setRootElement(root);

        List<Element> list = new ArrayList<>();
        list.add(new Element("item1").setText("Item 1"));
        list.add(new Element("item2").setText("Item 2"));
        root.addContent(new Element("items").addContent(list));

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text><items><item1>Item 1</item1><item2>Item 2</item2></items></root>", result);
    }

    @Test
    public void testSAXOutputterProcessElement() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));

        Element item = new Element("item").setText("Test");
        root.addContent(item);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text><item>Test</item></root>", result);
    }

    @Test
    public void testSAXOutputterProcessText() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));

        Element item = new Element("item").setText("Test");
        root.addContent(item);

        Text text = new Text(" JDOM text");
        item.addContent(text);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text><item>Test JDOM text</item></root>", result);
    }

    @Test
    public void testSAXOutputterProcessCDATA() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));

        Element item = new Element("item");
        root.addContent(item);

        CDATA cdata = new CDATA("<![CDATA[Test JDOM text]]]>");
        item.addContent(cdata);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text><item><![CDATA[Test JDOM text]]></item></root>", result);
    }

    @Test
    public void testSAXOutputterProcessComment() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));

        Element item = new Element("item");
        root.addContent(item);

        Comment comment = new Comment("Test comment");
        item.addContent(comment);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text><item><![CDATA[--Test comment--]]]></item></root>", result);
    }

    @Test
    public void testSAXOutputterProcessProcessingInstruction() throws JDOMException, SAXException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, JDOM!"));

        Element item = new Element("item");
        root.addContent(item);

        ProcessingInstruction pi = new ProcessingInstruction("xml-version", "1.0");
        item.addContent(pi);

        StringWriter writer = new StringWriter();
        SAXOutputter.SAXTarget target = saxOutputter.buildTarget(document);
        saxOutputter.output(document);

        String result = writer.toString();
        assertEquals("<root><Text>Hello, JDOM!</Text><item><?xml version='1.0'?></item></root>", result);
    }

    static class MyContentHandler implements ContentHandler {
        @Override
        public void startDocument() throws SAXException {}

        @Override
        public void endDocument() throws SAXException {}

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {}

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
    }

    static class MyErrorHandler implements ErrorHandler {
        @Override
        public void warning(SAXParseException exception) throws SAXException {}

        @Override
        public void error(SAXParseException exception) throws SAXException {}

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    }

    static class MyDTDHandler implements DTDHandler {
        @Override
        public void setDocument(Document doc) {}

        @Override
        public void notationDecl(String name, String publicId, String systemId) {}

        @Override
        public void elementDecl(String name, String model) {}

        @Override
        public void attributeDecl(String eName, String aName, String type, String defValue, String value) {}

        @Override
        public void internalSubset(String name, String systemId, String publicId) {}

        @Override
        public void externalSubset(String systemId, String publicId) {}

        @Override
        public void setEntity(String name, String pubid, String systemid) {}

        @Override
        public void processingInstructionDecl(String target, String data) {}

        @Override
        public void setDTD(Document doc, InputSource input) {}
    }

    static class MyEntityResolver implements EntityResolver {
        @Override
        public InputSource resolveEntity(String name, String publicId, String systemId) throws SAXException, IOException {
            return null;
        }
    }

    static class MyLexicalHandler implements LexicalHandler {
        @Override
        public void startDTD(String name, String publicId, String systemId) throws SAXException {}

        @Override
        public void endDTD() throws SAXException {}

        @Override
        public void startEntity(String name) throws SAXException {}

        @Override
        public void endEntity(String name) throws SAXException {}

        @Override
        public void startCDATA() throws SAXException {}

        @Override
        public void endCDATA() throws SAXException {}

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {}

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}

        @Override
        public void startProcessingInstruction(String target) throws SAXException {}

        @Override
        public void endProcessingInstruction(String target) throws SAXException {}

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void startDocument() throws SAXException {}

        @Override
        public void endDocument() throws SAXException {}

        @Override
        public void setDocumentLocator(Locator locator) {}
    }

    static class MyDeclHandler implements DeclHandler {
        @Override
        public void declaration(String name, String publicId, String systemId, int type, String value) throws SAXException {
            throw new SAXNotRecognizedException("Unsupported declaration: " + name);
        }

        @Override
        public void entityDeclaration(String name, String publicId, String systemId) {}

        @Override
        public void attributeDeclaration(String eName, String aName, String type, String value) {}
    }
}