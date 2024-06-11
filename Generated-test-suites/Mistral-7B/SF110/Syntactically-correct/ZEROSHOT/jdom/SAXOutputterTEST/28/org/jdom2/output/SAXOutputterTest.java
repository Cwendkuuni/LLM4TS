package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.ext.*;
import org.xml.sax.*;
import org.junit.Test;

public class SAXOutputterTest {

 SAXOutputter saxOutputter;
 Document doc;
 ContentHandler handler;

 @Before
 public void setUp() {
 saxOutputter = new SAXOutputter();
 doc = new Document("root");
 handler = new TestContentHandler();
 saxOutputter.setContentHandler(handler);
 }

 @Test
 public void testSAXOutputterDefaultConstructor() {
 SAXOutputter outputter = new SAXOutputter();
 assertEquals(false, outputter.getReportNamespaceDeclarations());
 assertEquals(true, outputter.getReportDTDEvents());
 assertNull(outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(handler);
 assertNull(outputter.getErrorHandler());
 assertNull(outputter.getDTDHandler());
 assertNull(outputter.getEntityResolver());
 assertNull(outputter.getLexicalHandler());
 assertNull(outputter.getDeclHandler());
 assertEquals(false, outputter.getReportNamespaceDeclarations());
 assertEquals(true, outputter.getReportDTDEvents());
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(handler, new TestErrorHandler());
 assertNotNull(outputter.getErrorHandler());
 assertEquals(false, outputter.getReportNamespaceDeclarations());
 assertEquals(true, outputter.getReportDTDEvents());
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerDTDHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(handler, new TestDTDHandler(), new TestEntityResolver());
 assertNotNull(outputter.getDTDHandler());
 assertNotNull(outputter.getEntityResolver());
 assertEquals(false, outputter.getReportNamespaceDeclarations());
 assertEquals(true, outputter.getReportDTDEvents());
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerDTDHandlerLexicalHandlerConstructor() {
 SAXOutputter outputter = new SAXOutputter(handler, new TestErrorHandler(), new TestDTDHandler(), new TestEntityResolver(), new TestLexicalHandler());
 assertNotNull(outputter.getErrorHandler());
 assertNotNull(outputter.getDTDHandler());
 assertNotNull(outputter.getEntityResolver());
 assertNotNull(outputter.getLexicalHandler());
 assertEquals(false, outputter.getReportNamespaceDeclarations());
 assertEquals(true, outputter.getReportDTDEvents());
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @Test
 public void testSAXOutputterProcessorConstructor() {
 SAXOutputter outputter = new SAXOutputter(SAXOutputter.DEFAULT_PROCESSOR, Format.getRawFormat(), handler, new TestErrorHandler(), new TestDTDHandler(), new TestEntityResolver(), new TestLexicalHandler());
 assertEquals(false, outputter.getReportNamespaceDeclarations());
 assertEquals(true, outputter.getReportDTDEvents());
 assertSame(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
 assertNotNull(outputter.getFormat());
 }

 @Test
 public void testSetContentHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getContentHandler());
 outputter.setContentHandler(handler);
 assertSame(handler, outputter.getContentHandler());
 }

 @Test
 public void testGetContentHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getContentHandler());
 outputter.setContentHandler(handler);
 assertSame(handler, outputter.getContentHandler());
 }

 @Test
 public void testSetErrorHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getErrorHandler());
 outputter.setErrorHandler(new TestErrorHandler());
 assertSame(new TestErrorHandler(), outputter.getErrorHandler());
 }

 @Test
 public void testGetErrorHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getErrorHandler());
 outputter.setErrorHandler(new TestErrorHandler());
 assertSame(new TestErrorHandler(), outputter.getErrorHandler());
 }

 @Test
 public void testSetDTDHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getDTDHandler());
 outputter.setDTDHandler(new TestDTDHandler());
 assertSame(new TestDTDHandler(), outputter.getDTDHandler());
 }

 @Test
 public void testGetDTDHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getDTDHandler());
 outputter.setDTDHandler(new TestDTDHandler());
 assertSame(new TestDTDHandler(), outputter.getDTDHandler());
 }

 @Test
 public void testSetEntityResolver() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getEntityResolver());
 outputter.setEntityResolver(new TestEntityResolver());
 assertSame(new TestEntityResolver(), outputter.getEntityResolver());
 }

 @Test
 public void testGetEntityResolver() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getEntityResolver());
 outputter.setEntityResolver(new TestEntityResolver());
 assertSame(new TestEntityResolver(), outputter.getEntityResolver());
 }

 @Test
 public void testSetLexicalHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getLexicalHandler());
 outputter.setLexicalHandler(new TestLexicalHandler());
 assertSame(new TestLexicalHandler(), outputter.getLexicalHandler());
 }

 @Test
 public void testGetLexicalHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getLexicalHandler());
 outputter.setLexicalHandler(new TestLexicalHandler());
 assertSame(new TestLexicalHandler(), outputter.getLexicalHandler());
 }

 @Test
 public void testSetDeclHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getDeclHandler());
 outputter.setDeclHandler(new TestDeclHandler());
 assertSame(new TestDeclHandler(), outputter.getDeclHandler());
 }

 @Test
 public void testGetDeclHandler() {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getDeclHandler());
 outputter.setDeclHandler(new TestDeclHandler());
 assertSame(new TestDeclHandler(), outputter.getDeclHandler());
 }

 @Test
 public void testGetReportNamespaceDeclarations() {
 SAXOutputter outputter = new SAXOutputter();
 assertFalse(outputter.getReportNamespaceDeclarations());
 outputter.setReportNamespaceDeclarations(true);
 assertTrue(outputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testGetReportDTDEvents() {
 SAXOutputter outputter = new SAXOutputter();
 assertTrue(outputter.getReportDTDEvents());
 outputter.setReportDTDEvents(false);
 assertFalse(outputter.getReportDTDEvents());
 }

 @Test
 public void testSetFormat() {
 SAXOutputter outputter = new SAXOutputter();
 assertNotNull(outputter.getFormat());
 outputter.setFormat(Format.getPrettyFormat());
 assertSame(Format.getPrettyFormat(), outputter.getFormat());
 }

 @Test
 public void testGetFormat() {
 SAXOutputter outputter = new SAXOutputter();
 assertNotNull(outputter.getFormat());
 outputter.setFormat(Format.getPrettyFormat());
 assertSame(Format.getPrettyFormat(), outputter.getFormat());
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 assertNull(outputter.getContentHandler().getOutputDocument());
 outputter.output(doc);
 assertSame(doc, outputter.getContentHandler().getOutputDocument());
 }

 @Test
 public void testOutputList() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 List<Content> list = doc.getContent();
 assertNotNull(outputter.getContentHandler().getDocument());
 outputter.output(list);
 assertSame(doc, outputter.getContentHandler().getDocument());
 }

 @Test
 public void testOutputElement() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Element element = doc.getRootElement();
 assertNull(outputter.getContentHandler().getDocument());
 outputter.output(element);
 assertSame(doc, outputter.getContentHandler().getDocument());
 }

 @Test
 public void testOutputFragmentList() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 List<Content> fragmentList = doc.getContent();
 assertNull(outputter.getContentHandler().getDocument());
 outputter.outputFragment(fragmentList);
 assertNull(outputter.getContentHandler().getDocument());
 }

 @Test
 public void testOutputFragmentContent() throws JDOMException {
 SAXOutputter outputter = new SAXOutputter();
 Content fragment = doc.getRootElement();
 assertNull(outputter.getContentHandler().getDocument());
 outputter.outputFragment(fragment);
 assertNull(outputter.getContentHandler().getDocument());
 }
}

 class TestContentHandler implements ContentHandler {
 @Override
 public void startDocument() throws SAXException {
 //Empty
 }

 @Override
 public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
 //Empty
 }

 @Override
 public void endElement(final String uri, final String localName, final String qName) throws SAXException {
 //Empty
 }

 @Override
 public void characters(final char[] ch, final int start, final int length) throws SAXException {
 //Empty
 }

 @Override
 public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
 //Empty
 }

 @Override
 public void processingInstruction(final String target, final String data) throws SAXException {
 //Empty
 }

 @Override
 public void skippedEntity(final String name) throws SAXException {
 //Empty
 }

 @Override
 public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
 //Empty
 }

 @Override
 public void endPrefixMapping(final String prefix) throws SAXException {
 //Empty
 }

 @Override
 public void setDocumentLocator(final Locator locator) throws SAXException {
 //Empty
 }

 @Override
 public void startDocument() throws SAXException {
 //Empty
 }

 @Override
 public void endDocument() throws SAXException {
 //Empty
 }

 @Override
 public void characters(final char[] ch, final int start, final int length) throws SAXException {
 outputDocument.setContent(new DocumentBuilder().buildDocument(new StringReader(new String(ch, start, length))));
 }

 @Override
 public void endElement(final String uri, final String localName, final String qName) throws SAXException {
 //Empty
 }

 @Override
 public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
 //Empty
 }

 @Override
 public void processingInstruction(final String target, final String data) throws SAXException {
 //Empty
 }

 @Override
 public void skippedEntity(final String name) throws SAXException {
 //Empty
 }

 @Override
 public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
 //Empty
 }

 @Override
 public void endPrefixMapping(final String prefix) throws SAXException {
 //Empty
 }

 @Override
 public void setDocumentLocator(final Locator locator) throws SAXException {
 //Empty
 }
 }

 class TestErrorHandler implements ErrorHandler {
 @Override
 public void warning(final SAXParseException exception) throws SAXException {
 //Empty
 }

 @Override
 public void error(final SAXParseException exception) throws SAXException {
 //Empty
 }

 @Override
 public void fatalError(final SAXParseException exception) throws SAXException {
 //Empty
 }
 }

 class TestDTDHandler implements DTDHandler {
 @Override
 public void notationDecl(final String name, final String publicId, final String systemId) throws SAXException {
 //Empty
 }

 @Override
 public void unparsedEntityDecl(final String name, final String publicId, final String systemId, final String notationName) throws SAXException {
 //Empty
 }

 @Override
 public void elementDecl(final String name, final String model) throws SAXException {
 //Empty
 }

 @Override
 public void attributeDecl(final String eName, final String aName, final String type, final String defaultValue, final String value) throws SAXException {
 //Empty
 }

 @Override
 public void internalSubset(final String name, final String systemId, final String xmlDecl) throws SAXException {
 //Empty
 }

 @Override
 public void externalSubset(final String systemId, final String xmlDecl) throws SAXException {
 //Empty
 }
 }

 class TestEntityResolver implements EntityResolver {
 @Override
 public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
 return new InputSource(new StringReader(""));
 }
 }

 class TestLexicalHandler implements LexicalHandler {
 @Override
 public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
 //Empty
 }

 @Override
 public void endDTD() throws SAXException {
 //Empty
 }

 @Override
 public void startEntity(final String name) throws SAXException {
 //Empty
 }

 @Override
 public void endEntity(final String name) throws SAXException {
 //Empty
 }

 @Override
 public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
 //Empty
 }

 @Override
 public void endElement(final String uri, final String localName, final String qName) throws SAXException {
 //Empty
 }

 @Override
 public void characters(final char[] ch, final int start, final int length) throws SAXException {
 //Empty
 }

 @Override
 public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
 //Empty
 }

 @Override
 public void processingInstruction(final String target, final String data) throws SAXException {
 //Empty
 }

 @Override
 public void skippedEntity(final String name) throws SAXException {
 //Empty
 }

 @Override
 public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
 //Empty
 }

 @Override
 public void endPrefixMapping(final String prefix) throws SAXException {
 //Empty
 }

 @Override
 public void setDocumentLocator(final Locator locator) throws SAXException {
 //Empty
 }
 }

 class TestDeclHandler implements DeclHandler {
 @Override
 public void declaration(final Declaration declaration) throws SAXException {
 //Empty
 }
 }