package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SAXOutputterTest {

 private SAXOutputter outputter;
 private ContentHandler handler;
 private Document document;

 @Before
 public void setUp() {
 handler = new MyContentHandler();
 outputter = new SAXOutputter();
 document = new JDOMParser().parse(getClass().getResourceAsStream("/test.xml")).getDocument();
 }

 @Test
 public void testDefaultSAXOutputter() {
 outputter = new SAXOutputter();
 outputter.setFormat(Format.getRawFormat());
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertNotNull(target.getDocument());
 }

 @Test
 public void testSAXOutputterWithContentHandler() {
 outputter = new SAXOutputter(handler);
 outputter.setFormat(Format.getRawFormat());
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertEquals("Expected content handled", "Test Content", handler.getContent());
 }

 @Test
 public void testSAXOutputterWithErrorHandler() {
 ErrorHandler errorHandler = new MyErrorHandler();
 outputter = new SAXOutputter(handler, errorHandler);
 outputter.setFormat(Format.getRawFormat());
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertTrue("Expected error not thrown", errorHandler.isErrorThrown());
 }

 @Test
 public void testSAXOutputterWithDTDHandler() {
 DTDHandler dtdHandler = new MyDTDHandler();
 outputter = new SAXOutputter(handler, null, dtdHandler);
 outputter.setFormat(Format.getRawFormat());
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertNotNull("Expected DTD events handled", dtdHandler.getDTDEvent());
 }

 @Test
 public void testSAXOutputterWithEntityResolver() {
 EntityResolver entityResolver = new MyEntityResolver();
 outputter = new SAXOutputter(handler, null, null, entityResolver);
 outputter.setFormat(Format.getRawFormat());
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertEquals("Expected entity resolved", "Test Entity", entityResolver.getEntity());
 }

 @Test
 public void testSAXOutputterWithLexicalHandler() {
 LexicalHandler lexicalHandler = new MyLexicalHandler();
 outputter = new SAXOutputter(handler, null, null, null, lexicalHandler);
 outputter.setFormat(Format.getRawFormat());
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertEquals("Expected lexical info handled", "Test Lexical Info", lexicalHandler.getLexicalInfo());
 }

 @Test
 public void testSAXOutputterWithDeclHandler() {
 DeclHandler declHandler = new MyDeclHandler();
 outputter = new SAXOutputter(handler, null, null, null, null, declHandler);
 outputter.setFormat(Format.getRawFormat());
 outputter.setReportNamespaceDeclarations(true);
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertEquals("Expected decl events handled", "Test Declaration", declHandler.getDeclEvent());
 }

 @Test
 public void testSAXOutputterWithSAXOutputProcessor() {
 SAXOutputProcessor processor = new MySAXOutputProcessor();
 outputter = new SAXOutputter(processor, Format.getRawFormat(), handler, null, null, null);
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertEquals("Expected SAXOutputProcessor processed", "Test Processor Output", processor.getOutput());
 }

 @Test
 public void testSAXOutputterWithFormat() {
 Format format = new DefaultFormat();
 outputter = new SAXOutputter();
 outputter.setFormat(format);
 SAXOutputTarget target = outputter.buildTarget(document);
 outputter.output(document);
 assertEquals("Expected format applied", format.asString(), target.toString());
 }

}

 class MyContentHandler implements ContentHandler {
 String content;

 @Override
 public void startDocument() throws SAXException {
 }

 @Override
 public void endDocument() throws SAXException {
 }

 @Override
 public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
 }

 @Override
 public void endElement(String uri, String localName, String qName) throws SAXException {
 }

 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 content = new String(ch, start, length);
 }

 @Override
 public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
 }

 @Override
 public void processingInstruction(String target, String data) throws SAXException {
 }

 @Override
 public void skippedEntity(String name) throws SAXException {
 }

 @Override
 public void startEntity(String name) throws SAXException {
 }

 @Override
 public void endEntity(String name) throws SAXException {
 }

 public String getContent() {
 return content;
 }
}

 class MyErrorHandler implements ErrorHandler {
 private boolean errorThrown = false;

 @Override
 public void warning(SAXParseException exception) throws SAXException {
 }

 @Override
 public void error(SAXParseException exception) throws SAXException {
 errorThrown = true;
 }

 @Override
 public void fatalError(SAXParseException exception) throws SAXException {
 errorThrown = true;
 throw exception;
 }

 public boolean isErrorThrown() {
 return errorThrown;
 }
}

 class MyDTDHandler implements DTDHandler {
 private String dtdEvent;

 @Override
 public void startDTD(String name, String publicId, String systemId) throws SAXException {
 dtdEvent = "DTD Start: " + name;
 }

 @Override
 public void endDTD() throws SAXException {
 }

 @Override
 public void notationDecl(String name, String publicId, String systemId) throws SAXException {
 }

 @Override
 public void unparsedEntityDecl(String name, String publicId, String systemId) throws SAXException {
 }

 @Override
 public void entityDecl(String name, String publicId, String systemId) throws SAXException {
 }

 @Override
 public void attributeDecl(String eName, String aName, String type, String defaultValue) throws SAXException {
 }

 @Override
 public void internalEntityDecl(String name, String systemId, String declaration) throws SAXException {
 }

 @Override
 public void externalEntityDecl(String name, String systemId, String declaration) throws SAXException {
 }

 public String getDTDEvent() {
 return dtdEvent;
 }
}

 class MyEntityResolver implements EntityResolver {
 private String entity;

 @Override
 public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
 InputSource is = new InputSource(new StringReader("Test Entity"));
 is.setSystemId(systemId);
 entity = systemId;
 return is;
 }

 public String getEntity() {
 return entity;
 }
}

 class MyLexicalHandler implements LexicalHandler {
 private String lexicalInfo;

 @Override
 public void startCDATA() throws SAXException {
 }

 @Override
 public void endCDATA() throws SAXException {
 }

 @Override
 public void startDTD(String name, String publicId, String systemId) throws SAXException {
 }

 @Override
 public void endDTD() throws SAXException {
 }

 @Override
 public void startEntity(String name) throws SAXException {
 }

 @Override
 public void endEntity(String name) throws SAXException {
 }

 @Override
 public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
 lexicalInfo = "Test Lexical Info";
 }

 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 }

 @Override
 public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
 }

 @Override
 public void processingInstruction(String target, String data) throws SAXException {
 }

 @Override
 public void skippedEntity(String name) throws SAXException {
 }

 public String getLexicalInfo() {
 return lexicalInfo;
 }
}

 class MyDeclHandler implements DeclHandler {
 private String declEvent;

 @Override
 public void declaration(String name, String publicId, String systemId, String type, String value) throws SAXException {
 declEvent = name + " Declaration: " + value;
 }

 public String getDeclEvent() {
 return declEvent;
 }
}

 class MySAXOutputProcessor implements SAXOutputProcessor {
 private String output;

 @Override
 public void process(SAXOutputTarget target, Format format, Document document) throws SAXException {
 output = target.toString();
 }

 @Override
 public void processAsDocument(SAXOutputTarget target, Format format, List<? extends Content> content) throws SAXException {
 output = target.toString();
 }

 @Override
 public void processAsDocument(SAXOutputTarget target, Format format, Element element) throws SAXException {
 output = target.toString();
 }

 public String getOutput() {
 return output;
 }
}