package org.jdom2.output;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.DocumentBuilderFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.xml.sax.SAXException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private Document document;

 @Before
 public void setUp() {
 saxOutputter = new SAXOutputter();
 document = DocumentBuilderFactory.getDocumentBuilder().newDocument();
 }

 @After
 public void tearDown() {
 saxOutputter = null;
 document = null;
 }

 @Test
 public void testDefaultConstructor() {
 SAXOutputter saxOutputter = new SAXOutputter();
 assertNotNull(saxOutputter.getContentHandler());
 assertNotNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testContentHandlerConstructor() {
 ContentHandler contentHandler = mock(ContentHandler.class);
 SAXOutputter saxOutputter = new SAXOutputter(contentHandler);
 assertEquals(contentHandler, saxOutputter.getContentHandler());
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testContentHandlerErrorHandlerConstructor() {
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler);
 assertEquals(contentHandler, saxOutputter.getContentHandler());
 assertEquals(errorHandler, saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testContentHandlerErrorHandlerDTDHandlerConstructor() {
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 DTDHandler dtdHandler = mock(DTDHandler.class);
 SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
 assertEquals(contentHandler, saxOutputter.getContentHandler());
 assertEquals(errorHandler, saxOutputter.getErrorHandler());
 assertEquals(dtdHandler, saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 DTDHandler dtdHandler = mock(DTDHandler.class);
 EntityResolver entityResolver = mock(EntityResolver.class);
 SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
 assertEquals(contentHandler, saxOutputter.getContentHandler());
 assertEquals(errorHandler, saxOutputter.getErrorHandler());
 assertEquals(dtdHandler, saxOutputter.getDTDHandler());
 assertEquals(entityResolver, saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputer.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputProcessorAndFormatConstructor() {
 SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
 Format format = mock(Format.class);
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 SAXOutputter saxOutputter = new SAXOutputter(processor, format, contentHandler, errorHandler);
 assertEquals(processor, saxOutputter.getSAXOutputProcessor());
 assertEquals(format, saxOutputter.getFormat());
 assertEquals(contentHandler, saxOutputter.getContentHandler());
 assertEquals(errorHandler, saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSetters() {
 SAXOutputter saxOutputter = new SAXOutputter();
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 DTDHandler dtdHandler = mock(DTDHandler.class);
 EntityResolver entityResolver = mock(EntityResolver.class);
 LexicalHandler lexicalHandler = mock(LexicalHandler.class);
 DeclHandler declHandler = mock(DeclHandler.class);

 saxOutputter.setContentHandler(contentHandler);
 assertEquals(contentHandler, saxOutputter.getContentHandler());

 saxOutputter.setErrorHandler(errorHandler);
 assertEquals(errorHandler, saxOutputter.getErrorHandler());

 saxOutputter.setDTDHandler(dtdHandler);
 assertEquals(dtdHandler, saxOutputter.getDTDHandler());

 saxOutputter.setEntityResolver(entityResolver);
 assertEquals(entityResolver, saxOutputter.getEntityResolver());

 saxOutputter.setLexicalHandler(lexicalHandler);
 assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());

 saxOutputter.setDeclHandler(declHandler);
 assertEquals(declHandler, saxOutputter.getDeclHandler());

 saxOutputter.setReportNamespaceDeclarations(true);
 assertTrue(saxOutputter.getReportNamespaceDeclarations());

 saxOutputter.setReportDTDEvents(false);
 assertFalse(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testOutputDocument() throws SAXException, JDOMException {
 ContentHandler handler = mock(ContentHandler.class);
 SAXOutputter saxOutputter = new SAXOutputter();
 Document document = this.document;
 saxOutputter.setContentHandler(handler);
 saxOutputter.output(document);
 verify(handler, times(1)).startDocument();
 verify(handler, times(1)).endDocument();
 }

 @Test
 public void testOutputNodes() throws SAXException, JDOMException {
 ContentHandler handler = mock(ContentHandler.class);
 SAXOutputter saxOutputter = new SAXOutputter();
 List<? extends Content> nodes = new ArrayList<>();
 nodes.add(document.getRootElement());
 saxOutputter.setContentHandler(handler);
 saxOutputter.output(nodes);
 verify(handler, times(1)).startDocument();
 verify(handler, atLeastOnce()).startElement("root");
 for (Content content : nodes) {
 if (content instanceof Element) {
 verify(handler, atLeastOnce()).startElement(content.getName().getLocalPart());
 }
 }
 verify(handler, times(1)).endDocument();
 }

 @Test
 public void testOutputElement() throws SAXException, JDOMException {
 ContentHandler handler = mock(ContentHandler.class);
 SAXOutputter saxOutputter = new SAXOutputter();
 Element element = new Element("root");
 saxOutputter.setContentHandler(handler);
 saxOutputter.output(element);
 verify(handler, times(1)).startDocument();
 verify(handler, times(1)).startElement("root");
 verify(handler, times(1)).endElement("root");
 verify(handler, times(1)).endDocument();
 }

 @Test
 public void testOutputFragment() throws SAXException, JDOMException {
 SAXOutputter saxOutputter = new SAXOutputter();
 Content fragment = new Element("root").getContent().get(0);
 saxOutputter.outputFragment(fragment);
 assertEquals(fragment.getClass(), SAXTarget.class);
 }

 @Test(expected = JDOMException.class)
 public void testOutputInvalidContent() throws SAXException, JDOMException {
 SAXOutputter saxOutputter = new SAXOutputter();
 Content invalidContent = new Text("Invalid content");
 saxOutputter.outputFragment(invalidContent);
 }
}