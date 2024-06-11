package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class SAXOutputterTest {

 private Document createDocument() {
 final Element root = new Element("root");
 root.addContent(new Text("Hello World"));
 return new Document(new JDOMFactory().getDocumentBuilder().getDocumentBuilderHelper().buildDocument(new JDOMFactory().getDocumentBuilder(), root));
 }

 @org.junit.Before
 public void setUp() {
 SAXOutputter.DEFAULT_PROCESSOR = new TestSAXOutputProcessor();
 }

 @org.junit.After
 public void tearDown() {
 SAXOutputter.DEFAULT_PROCESSOR = SAXOutputter.DEFAULT_PROCESSOR;
 }

 @org.junit.Test
 public void testSAXOutputterDefaultConstructor() {
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
 assertNotNull(outputter.getFormat());
 }

 @org.junit.Test
 public void testSAXOutputterContentHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(new DefaultHandler());
 assertSame(outputter.getContentHandler(), new DefaultHandler());
 assertNull(outputter.getErrorHandler());
 assertNull(outputter.getDTDHandler());
 assertNull(outputter.getEntityResolver());
 assertNull(outputter.getLexicalHandler());
 assertNull(outputter.getDeclHandler());
 assertFalse(outputter.getReportNamespaceDeclarations());
 assertTrue(outputter.getReportDTDEvents());
 assertNull(outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @org.junit.Test
 public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(new DefaultHandler(), new DefaultHandler());
 assertSame(outputter.getContentHandler(), new DefaultHandler());
 assertSame(outputter.getErrorHandler(), new DefaultHandler());
 assertNull(outputter.getDTDHandler());
 assertNull(outputter.getEntityResolver());
 assertNull(outputter.getLexicalHandler());
 assertNull(outputter.getDeclHandler());
 assertFalse(outputter.getReportNamespaceDeclarations());
 assertTrue(outputter.getReportDTDEvents());
 assertNull(outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @org.junit.Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(new DefaultHandler(), new DefaultHandler(), new DefaultHandler());
 assertSame(outputter.getContentHandler(), new DefaultHandler());
 assertSame(outputter.getErrorHandler(), new DefaultHandler());
 assertSame(outputter.getDTDHandler(), new DefaultHandler());
 assertNull(outputter.getEntityResolver());
 assertNull(outputter.getLexicalHandler());
 assertNull(outputter.getDeclHandler());
 assertFalse(outputter.getReportNamespaceDeclarations());
 assertTrue(outputter.getReportDTDEvents());
 assertNull(outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @org.junit.Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerLexicalHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(new DefaultHandler(), new DefaultHandler(), new DefaultHandler(), new DefaultHandler());
 assertSame(outputter.getContentHandler(), new DefaultHandler());
 assertSame(outputter.getErrorHandler(), new DefaultHandler());
 assertSame(outputter.getDTDHandler(), new DefaultHandler());
 assertSame(outputter.getLexicalHandler(), new DefaultHandler());
 assertNull(outputter.getEntityResolver());
 assertNull(outputter.getDeclHandler());
 assertFalse(outputter.getReportNamespaceDeclarations());
 assertTrue(outputter.getReportDTDEvents());
 assertNull(outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @org.junit.Test
 public void testSAXOutputterSAXOutputProcessorFormatConstructor() {
 SAXOutputter outputter = new SAXOutputter(new TestSAXOutputProcessor(), Format.getPrettyFormat(), null, null, null);
 assertSame(outputter.getContentHandler(), null);
 assertSame(outputter.getErrorHandler(), null);
 assertSame(outputter.getDTDHandler(), null);
 assertSame(outputter.getEntityResolver(), null);
 assertSame(outputter.getLexicalHandler(), null);
 assertSame(outputter.getDeclHandler(), null);
 assertFalse(outputter.getReportNamespaceDeclarations());
 assertTrue(outputter.getReportDTDEvents());
 assertSame(outputter.getSAXOutputProcessor(), new TestSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 assertNotNull(outputter.getFormat().getOutputStream());
 }

 @org.junit.Test
 public void testSAXOutputterSetters() {
 SAXOutputter outputter = new SAXOutputter();
 ContentHandler contentHandler = new DefaultHandler();
 ErrorHandler errorHandler = new DefaultHandler();
 DTDHandler dtdHandler = new DefaultHandler();
 EntityResolver entityResolver = new DefaultHandler();
 LexicalHandler lexicalHandler = new DefaultHandler();
 DeclHandler declHandler = new DefaultHandler();
 outputter.setContentHandler(contentHandler);
 assertSame(outputter.getContentHandler(), contentHandler);
 outputter.setErrorHandler(errorHandler);
 assertSame(outputter.getErrorHandler(), errorHandler);
 outputter.setDTDHandler(dtdHandler);
 assertSame(outputter.getDTDHandler(), dtdHandler);
 outputter.setEntityResolver(entityResolver);
 assertSame(outputter.getEntityResolver(), entityResolver);
 outputter.setLexicalHandler(lexicalHandler);
 assertSame(outputter.getLexicalHandler(), lexicalHandler);
 outputter.setDeclHandler(declHandler);
 assertSame(outputter.getDeclHandler(), declHandler);
 outputter.setReportNamespaceDeclarations(true);
 assertTrue(outputter.getReportNamespaceDeclarations());
 outputter.setReportDTDEvents(false);
 assertFalse(outputter.getReportDTDEvents());
 SAXOutputProcessor processor = new TestSAXOutputProcessor();
 outputter.setSAXOutputProcessor(processor);
 assertSame(outputter.getSAXOutputProcessor(), processor);
 Format format = Format.getPrettyFormat();
 outputter.setFormat(format);
 assertSame(outputter.getFormat(), format);
 }

 @org.junit.Test
 public void testOutputDocument() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Document document = createDocument();
 outputter.output(document);
 StringWriter writer = new StringWriter();
 String xml = writer.toString();
 assertTrue(xml.contains("<root>Hello World</root>"));
 }

 @org.junit.Test
 public void testOutputList() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Document document = createDocument();
 Element root = document.getRootElement();
 root.addContent(new Element("element"));
 List<Content> nodes = new ArrayList<Content>(root.getContent().size());
 nodes.addAll(root.getContent());
 outputter.output(nodes);
 StringWriter writer = new StringWriter();
 String xml = writer.toString();
 String expected = "<root><Hello World></root><element/>";
 assertTrue(xml.contains(expected));
 }

 @org.junit.Test
 public void testOutputElement() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Document document = createDocument();
 Element root = document.getRootElement();
 Element element = new Element("element");
 outputter.output(element);
 StringWriter writer = new StringWriter();
 String xml = writer.toString();
 String expected = "<element></element>";
 assertTrue(xml.contains(expected));
 }

 @org.junit.Test
 public void testOutputFragmentList() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Document document = createDocument();
 Element root = document.getRootElement();
 root.addContent(new Text("Hello World"));
 List<Content> fragments = new ArrayList<Content>();
 fragments.add(root.getContent().get(0));
 outputter.outputFragment(fragments);
 StringWriter writer = new StringWriter();
 String xml = writer.toString();
 assertTrue(xml.contains("Hello World"));
 }

 @org.junit.Test
 public void testOutputFragmentElement() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Document document = createDocument();
 Element root = document.getRootElement();
 Element element = new Element("element");
 outputter.outputFragment(element);
 StringWriter writer = new StringWriter();
 String xml = writer.toString();
 String expected = "<element></element>";
 assertTrue(xml.contains(expected));
 }

 @org.junit.Test(expected = JDOMException.class)
 public void testOutputFragmentInvalidContent() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Document document = createDocument();
 outputter.outputFragment(document);
 }

 @org.junit.Test
 public void testSetProperty() {
 SAXOutputter outputter = new SAXOutputter();
 outputter.setProperty("http://xml.org/sax/properties/lexical-handler", new DefaultHandler());
 DeclHandler declHandler = new DefaultHandler();
 outputter.setProperty("http://xml.org/sax/properties/declaration-handler", declHandler);
 assertSame(outputter.getLexicalHandler(), new DefaultHandler());
 assertSame(outputter.getDeclHandler(), declHandler);
 }

 @org.junit.Test(expected = SAXNotRecognizedException.class)
 public void testSetPropertyUnrecognizedProperty() {
 SAXOutputter outputter = new SAXOutputter();
 outputter.setProperty("http://xml.org/sax/properties/unknown", new DefaultHandler());
 }

 @org.junit.Test
 public void testSetFeature() {
 SAXOutputter outputter = new SAXOutputter();
 outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
 outputter.setFeature("http://xml.org/sax/features/validation", false);
 assertTrue(outputter.getReportNamespaceDeclarations());
 assertFalse(outputter.getReportDTDEvents());
 }

 @org.junit.Test(expected = SAXNotRecognizedException.class)
 public void testSetFeatureUnknownFeature() {
 SAXOutputter outputter = new SAXOutputter();
 outputter.setFeature("http://xml.org/sax/features/unknown", true);
 }

 @org.junit.Test
 public void testSetFeatureAlreadySet() {
 SAXOutputter outputter = new SAXOutputter();
 outputter.setFeature("http://xml.org/sax/features/validation", true);
 outputter.setFeature("http://xml.org/sax/features/validation", false);
 assertFalse(outputter.getReportDTDEvents());
 }

 @org.junit.Test
 public void testGetFeature() {
 SAXOutputter outputter = new SAXOutputter();
 assertFalse(outputter.getReportNamespaceDeclarations());
 assertTrue(outputter.getReportDTDEvents());
 }

 @org.junit.Test
 public void testGetProperty() {
 SAXOutputter outputter = new SAXOutputter();
 outputter.setProperty("http://xml.org/sax/properties/lexical-handler", new DefaultHandler());
 assertSame(outputter.getProperty("http://xml.org/sax/properties/lexical-handler"), new DefaultHandler());
 outputter.setProperty("http://xml.org/sax/properties/declaration-handler", new DefaultHandler());
 assertSame(outputter.getProperty("http://xml.org/sax/properties/declaration-handler"), new DefaultHandler());
 }

 @org.junit.Test(expected = SAXNotRecognizedException.class)
 public void testGetPropertyUnrecognizedProperty() {
 SAXOutputter outputter = new SAXOutputter();
 outputter.setProperty("http://xml.org/sax/properties/unknown", new DefaultHandler());
 outputter.getProperty("http://xml.org/sax/properties/unknown");
 }

}

 class TestSAXOutputProcessor extends AbstractSAXOutputProcessor {
 @Override
 public void startDocument() throws SAXException {
 super.startDocument();
 }

 @Override
 public void endDocument() throws SAXException {
 super.endDocument();
 }

 @Override
 public void startPrefixMapping(String prefix, String uri) throws SAXException {
 super.startPrefixMapping(prefix, uri);
 }

 @Override
 public void endPrefixMapping(String prefix) throws SAXException {
 super.endPrefixMapping(prefix);
 }

 @Override
 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 super.startElement(uri, localName, qName, attributes);
 }

 @Override
 public void endElement(String uri, String localName, String qName) throws SAXException {
 super.endElement(uri, localName, qName);
 }

 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 super.characters(ch, start, length);
 }

 @Override
 public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
 super.ignorableWhitespace(ch, start, length);
 }

 @Override
 public void processingInstruction(String target, String data) throws SAXException {
 super.processingInstruction(target, data);
 }

 @Override
 public void skippedEntity(String name) throws SAXException {
 super.skippedEntity(name);
 }

 @Override
 public void error(SAXParseException exception) throws SAXException {
 super.error(exception);
 }

 @Override
 public void fatalError(SAXParseException exception) throws SAXException {
 super.fatalError(exception);
 }
 }