package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private StringWriter writer;

 @Before
 public void setUp() {
 saxOutputter = new SAXOutputter();
 writer = new StringWriter();
 }

 @Test
 public void testDefaultConstructor() {
 // Test default constructor
 SAXOutputter DefaultSAXOutputter = new SAXOutputter();
 assertEquals(SAXOutputter.getDefaultProcessor(), DefaultSAXOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), DefaultSAXOutputter.getFormat());
 assertNull(DefaultSAXOutputter.getContentHandler());
 assertNull(DefaultSAXOutputter.getErrorHandler());
 assertNull(DefaultSAXOutputter.getDTDHandler());
 assertNull(DefaultSAXOutputter.getEntityResolver());
 assertNull(DefaultSAXOutputter.getLexicalHandler());
 assertNull(DefaultSAXOutputter.getDeclHandler());
 assertFalse(DefaultSAXOutputter.getReportNamespaceDeclarations());
 assertTrue(DefaultSAXOutputter.getReportDTDEvents());
 }

 @Test
 public void testContentHandlerConstructor() {
 // Test ContentHandler constructor
 ContentHandler contentHandler = mock(ContentHandler.class);
 SAXOutputter saxOutputterWithContentHandler = new SAXOutputter(contentHandler);
 assertEquals(SAXOutputter.getDefaultProcessor(), saxOutputterWithContentHandler.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputterWithContentHandler.getFormat());
 assertEquals(contentHandler, saxOutputterWithContentHandler.getContentHandler());
 assertNull(saxOutputterWithContentHandler.getErrorHandler());
 assertNull(saxOutputterWithContentHandler.getDTDHandler());
 assertNull(saxOutputterWithContentHandler.getEntityResolver());
 assertNull(saxOutputterWithContentHandler.getLexicalHandler());
 assertNull(saxOutputterWithContentHandler.getDeclHandler());
 assertFalse(saxOutputterWithContentHandler.getReportNamespaceDeclarations());
 assertTrue(saxOutputterWithContentHandler.getReportDTDEvents());
 }

 @Test
 public void testContentHandlerErrorHandlerConstructor() {
 // Test ContentHandler and ErrorHandler constructor
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 SAXOutputter saxOutputterWithContentHandlerAndErrorHandler = new SAXOutputter(contentHandler, errorHandler);
 assertEquals(SAXOutputter.getDefaultProcessor(), saxOutputterWithContentHandlerAndErrorHandler.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputterWithContentHandlerAndErrorHandler.getFormat());
 assertEquals(contentHandler, saxOutputterWithContentHandlerAndErrorHandler.getContentHandler());
 assertEquals(errorHandler, saxOutputterWithContentHandlerAndErrorHandler.getErrorHandler());
 assertNull(saxOutputterWithContentHandlerAndErrorHandler.getDTDHandler());
 assertNull(saxOutputterWithContentHandlerAndErrorHandler.getEntityResolver());
 assertNull(saxOutputterWithContentHandlerAndErrorHandler.getLexicalHandler());
 assertNull(saxOutputterWithContentHandlerAndErrorHandler.getDeclHandler());
 assertFalse(saxOutputterWithContentHandlerAndErrorHandler.getReportNamespaceDeclarations());
 assertTrue(saxOutputterWithContentHandlerAndErrorHandler.getReportDTDEvents());
 }

 @Test
 public void testContentHandlerErrorHandlerDTDHandlerConstructor() {
 // Test ContentHandler, ErrorHandler, and DTDHandler constructor
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 DTDHandler dtdHandler = mock(DTDHandler.class);
 SAXOutputter saxOutputterWithContentHandlerErrorHandlerDTDHandler = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
 assertEquals(SAXOutputter.getDefaultProcessor(), saxOutputterWithContentHandlerErrorHandlerDTDHandler.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputterWithContentHandlerErrorHandlerDTDHandler.getFormat());
 assertEquals(contentHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandler.getContentHandler());
 assertEquals(errorHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandler.getErrorHandler());
 assertEquals(dtdHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandler.getDTDHandler());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandler.getEntityResolver());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandler.getLexicalHandler());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandler.getDeclHandler());
 assertFalse(saxOutputterWithContentHandlerErrorHandlerDTDHandler.getReportNamespaceDeclarations());
 assertTrue(saxOutputterWithContentHandlerErrorHandlerDTDHandler.getReportDTDEvents());
 }

 @Test
 public void testContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 // Test ContentHandler, ErrorHandler, DTDHandler, and EntityResolver constructor
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 DTDHandler dtdHandler = mock(DTDHandler.class);
 EntityResolver entityResolver = mock(EntityResolver.class);
 SAXOutputter saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
 assertEquals(SAXOutputter.getDefaultProcessor(), saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getFormat());
 assertEquals(contentHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getContentHandler());
 assertEquals(errorHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getErrorHandler());
 assertEquals(dtdHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getDTDHandler());
 assertEquals(entityResolver, saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getEntityResolver());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getLexicalHandler());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getDeclHandler());
 assertFalse(saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getReportNamespaceDeclarations());
 assertTrue(saxOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver.getReportDTDEvents());
 }

 @Test
 public void testContentHandlerErrorHandlerDTDHandlerLexicalHandlerConstructor() {
 // Test ContentHandler, ErrorHandler, DTDHandler, and LexicalHandler constructor
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 DTDHandler dtdHandler = mock(DTDHandler.class);
 LexicalHandler lexicalHandler = mock(LexicalHandler.class);
 SAXOutputter saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler = new SAXOutputter(contentHandler, errorHandler, dtdHandler, lexicalHandler);
 assertEquals(SAXOutputter.getDefaultProcessor(), saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getFormat());
 assertEquals(contentHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getContentHandler());
 assertEquals(errorHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getErrorHandler());
 assertEquals(dtdHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getDTDHandler());
 assertEquals(lexicalHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getLexicalHandler());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getEntityResolver());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getDeclHandler());
 assertFalse(saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getReportNamespaceDeclarations());
 assertTrue(saxOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler.getReportDTDEvents());
 }

 @Test
 public void testContentHandlerErrorHandlerDTDHandlerDeclHandlerConstructor() {
 // Test ContentHandler, ErrorHandler, DTDHandler, and DeclHandler constructor
 ContentHandler contentHandler = mock(ContentHandler.class);
 ErrorHandler errorHandler = mock(ErrorHandler.class);
 DTDHandler dtdHandler = mock(DTDHandler.class);
 DeclHandler declHandler = mock(DeclHandler.class);
 SAXOutputter saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler = new SAXOutputter(contentHandler, errorHandler, dtdHandler, declHandler);
 assertEquals(SAXOutputter.getDefaultProcessor(), saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getFormat());
 assertEquals(contentHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getContentHandler());
 assertEquals(errorHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getErrorHandler());
 assertEquals(dtdHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getDTDHandler());
 assertEquals(declHandler, saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getDeclHandler());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getEntityResolver());
 assertNull(saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getLexicalHandler());
 assertFalse(saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getReportNamespaceDeclarations());
 assertTrue(saxOutputterWithContentHandlerErrorHandlerDTDHandlerDeclHandler.getReportDTDEvents());
 }

 @Test
 public void testSAXOutputProcessorAndFormatConstructor() {
 // Test SAXOutputProcessor and Format constructor
 SAXOutputProcessor saxOutputProcessor = new DefaultSAXOutputProcessor();
 Format format = Format.getPrettyFormat();
 SAXOutputter saxOutputterWithSAXOutputProcessorAndFormat = new SAXOutputter(saxOutputProcessor, format, null, null, null, null, null, null);
 assertEquals(saxOutputProcessor, saxOutputterWithSAXOutputProcessorAndFormat.getSAXOutputProcessor());
 assertEquals(format, saxOutputterWithSAXOutputProcessorAndFormat.getFormat());
 assertNull(saxOutputterWithSAXOutputProcessorAndFormat.getContentHandler());
 assertNull(saxOutputterWithSAXOutputProcessorAndFormat.getErrorHandler());
 assertNull(saxOutputterWithSAXOutputProcessorAndFormat.getDTDHandler());
 assertNull(saxOutputterWithSAXOutputProcessorAndFormat.getEntityResolver());
 assertNull(saxOutputterWithSAXOutputProcessorAndFormat.getLexicalHandler());
 assertNull(saxOutputterWithSAXOutputProcessorAndFormat.getDeclHandler());
 assertFalse(saxOutputterWithSAXOutputProcessorAndFormat.getReportNamespaceDeclarations());
 assertTrue(saxOutputterWithSAXOutputProcessorAndFormat.getReportDTDEvents());
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 // Test outputting a Document
 Document document = new Document();
 document.setRootElement(new Element("root"));
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document);
 assertEquals(writer.toString(), "<root/>\n");
 }

 @Test
 public void testOutputNodes() throws JDOMException {
 // Test outputting a List of nodes
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Text("Hello"));
 root.addContent(new Element("element"));
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(document.getContent());
 assertEquals(writer.toString(), "<root>\n<Text xmlns=\"##default\">Hello</Text>\n<element/>\n</root>\n");
 }

 @Test
 public void testOutputElement() throws JDOMException {
 // Test outputting an Element
 Document document = new Document();
 Element element = new Element("element");
 document.setRootElement(element);
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.output(element);
 assertEquals(writer.toString(), "<element/>\n");
 }

 @Test
 public void testOutputFragmentList() throws JDOMException {
 // Test outputting a List of fragments
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 Text text = new Text("Hello");
 root.addContent(text);
 root.addContent(new Element("element"));
 List<Content> fragments = document.getContent();
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.outputFragment(fragments);
 assertEquals(writer.toString(), "<root>\n<Text xmlns=\"##default\">Hello</Text>\n<element/>\n</root>\n");
 }

 @Test
 public void testOutputFragmentElement() throws JDOMException {
 // Test outputting an Element fragment
 Document document = new Document();
 Element element = new Element("element");
 SAXOutputter saxOutputter = new SAXOutputter();
 saxOutputter.outputFragment(element);
 assertEquals(writer.toString(), "<element/>\n");
 }
}