package org.jdom2.output;

import org.xml.sax.ext.*;
import java.util.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.support.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private ContentHandler contentHandlerMock;
 private ErrorHandler errorHandlerMock;
 private DTDHandler dtdHandlerMock;
 private EntityResolver entityResolverMock;
 private LexicalHandler lexicalHandlerMock;
 private DeclHandler declHandlerMock;

 @Before
 public void setUp() {
 contentHandlerMock = mock(ContentHandler.class);
 errorHandlerMock = mock(ErrorHandler.class);
 dtdHandlerMock = mock(DTDHandler.class);
 entityResolverMock = mock(EntityResolver.class);
 lexicalHandlerMock = mock(LexicalHandler.class);
 declHandlerMock = mock(DeclHandler.class);
 saxOutputter = new SAXOutputter();
 }

 @Test
 public void testSAXOutputterDefaultConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(errorHandlerMock, saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(errorHandlerMock, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(errorHandlerMock, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertSame(entityResolverMock, saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock, lexicalHandlerMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(errorHandlerMock, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertSame(entityResolverMock, saxOutputter.getEntityResolver());
 assertSame(lexicalHandlerMock, saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterSAXOutputProcessorFormatConstructor() {
 saxOutputter = new SAXOutputter(null, null, null, null, null);
 assertNull(saxOutputter.getContentHandler());
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNotNull(saxOutputter.getFormat());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 }

 @Test
 public void testSAXOutputterSAXOutputProcessorFormatContentHandlerConstructor() {
 SAXOutputProcessor processorMock = mock(SAXOutputProcessor.class);
 saxOutputter = new SAXOutputter(processorMock, null, contentHandlerMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(processorMock, saxOutputter.getSAXOutputProcessor());
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterSAXOutputProcessorFormatContentHandlerErrorHandlerConstructor() {
 SAXOutputProcessor processorMock = mock(SAXOutputProcessor.class);
 saxOutputter = new SAXOutputter(processorMock, null, null, contentHandlerMock, errorHandlerMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(errorHandlerMock, saxOutputter.getErrorHandler());
 assertSame(processorMock, saxOutputter.getSAXOutputProcessor());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterSAXOutputProcessorFormatContentHandlerErrorHandlerDTDHandlerConstructor() {
 SAXOutputProcessor processorMock = mock(SAXOutputProcessor.class);
 saxOutputter = new SAXOutputter(processorMock, null, null, contentHandlerMock, errorHandlerMock, dtdHandlerMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(errorHandlerMock, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertSame(processorMock, saxOutputter.getSAXOutputProcessor());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterSAXOutputProcessorFormatContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 SAXOutputProcessor processorMock = mock(SAXOutputProcessor.class);
 saxOutputter = new SAXOutputter(processorMock, null, null, contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock);
 assertSame(contentHandlerMock, saxOutputter.getContentHandler());
 assertSame(errorHandlerMock, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertSame(entityResolverMock, saxOutputter.getEntityResolver());
 assertSame(processorMock, saxOutputter.getSAXOutputProcessor());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterSetContentHandler() {
 ContentHandler newContentHandler = mock(ContentHandler.class);
 saxOutputter.setContentHandler(newContentHandler);
 assertSame(newContentHandler, saxOutputter.getContentHandler());
 }

 @Test
 public void testGetContentHandler() {
 ContentHandler returnedContentHandler = saxOutputter.getContentHandler();
 assertNull(returnedContentHandler);
 }

 @Test
 public void testSetErrorHandler() {
 ErrorHandler newErrorHandler = mock(ErrorHandler.class);
 saxOutputter.setErrorHandler(newErrorHandler);
 assertSame(newErrorHandler, saxOutputter.getErrorHandler());
 }

 @Test
 public void testGetErrorHandler() {
 ErrorHandler returnedErrorHandler = saxOutputter.getErrorHandler();
 assertNull(returnedErrorHandler);
 }

 @Test
 public void testSetDTDHandler() {
 DTDHandler newDTDHandler = mock(DTDHandler.class);
 saxOutputter.setDTDHandler(newDTDHandler);
 assertSame(newDTDHandler, saxOutputter.getDTDHandler());
 }

 @Test
 public void testGetDTDHandler() {
 DTDHandler returnedDTDHandler = saxOutputter.getDTDHandler();
 assertNull(returnedDTDHandler);
 }

 @Test
 public void testSetEntityResolver() {
 EntityResolver newEntityResolver = mock(EntityResolver.class);
 saxOutputter.setEntityResolver(newEntityResolver);
 assertSame(newEntityResolver, saxOutputter.getEntityResolver());
 }

 @Test
 public void testGetEntityResolver() {
 EntityResolver returnedEntityResolver = saxOutputter.getEntityResolver();
 assertNull(returnedEntityResolver);
 }

 @Test
 public void testSetLexicalHandler() {
 LexicalHandler newLexicalHandler = mock(LexicalHandler.class);
 saxOutputter.setLexicalHandler(newLexicalHandler);
 assertSame(newLexicalHandler, saxOutputter.getLexicalHandler());
 }

 @Test
 public void testGetLexicalHandler() {
 LexicalHandler returnedLexicalHandler = saxOutputter.getLexicalHandler();
 assertNull(returnedLexicalHandler);
 }

 @Test
 public void testSetDeclHandler() {
 DeclHandler newDeclHandler = mock(DeclHandler.class);
 saxOutputter.setDeclHandler(newDeclHandler);
 assertSame(newDeclHandler, saxOutputter.getDeclHandler());
 }

 @Test
 public void testGetDeclHandler() {
 DeclHandler returnedDeclHandler = saxOutputter.getDeclHandler();
 assertNull(returnedDeclHandler);
 }

 @Test
 public void testGetReportNamespaceDeclarations() {
 boolean returnedDeclareNamespaces = saxOutputter.getReportNamespaceDeclarations();
 assertEquals(false, returnedDeclareNamespaces);
 }

 @Test
 public void testSetReportNamespaceDeclarations() {
 saxOutputter.setReportNamespaceDeclarations(true);
 assertTrue(saxOutputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testGetReportDTDEvents() {
 boolean returnedReportDtdEvents = saxOutputter.getReportDTDEvents();
 assertEquals(true, returnedReportDtdEvents);
 }

 @Test
 public void testSetReportDTDEvents() {
 saxOutputter.setReportDTDEvents(false);
 assertFalse(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSetFormat() {
 Format newFormat = mock(Format.class);
 saxOutputter.setFormat(newFormat);
 assertSame(newFormat, saxOutputter.getFormat());
 }

 @Test
 public void testGetFormat() {
 Format returnedFormat = saxOutputter.getFormat();
 assertNotNull(returnedFormat);
 }

 @Test
 public void testGetSAXOutputProcessor() {
 SAXOutputProcessor returnedSAXOutputProcessor = saxOutputter.getSAXOutputProcessor();
 assertNull(returnedSAXOutputProcessor);
 }

 @Test
 public void testSetSAXOutputProcessor() {
 SAXOutputProcessor newSAXOutputProcessor = mock(SAXOutputProcessor.class);
 saxOutputter.setSAXOutputProcessor(newSAXOutputProcessor);
 assertSame(newSAXOutputProcessor, saxOutputter.getSAXOutputProcessor());
 }
}