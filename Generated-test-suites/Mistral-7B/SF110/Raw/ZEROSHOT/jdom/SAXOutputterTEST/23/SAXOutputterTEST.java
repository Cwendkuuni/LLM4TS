package org.jdom2.output;

import org.xml.sax.ext.*;
import java.util.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.support.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private ContentHandler contentHandlerSpy;
 private Document document;

 @Before
 public void setUp() {
 contentHandlerSpy = mock(ContentHandler.class);
 saxOutputter = new SAXOutputter();
 document = new JDOMBuilder().setOMematchesXML(true).buildDocument();
 }

 @Test
 public void testSAXOutputterDefaultConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNull(saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerSpy);
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertEquals(DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
 ContentHandler errorHandlerSpy = mock(ContentHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy);
 assertSame(contentHandlerSpy, saxOutputter.getContentHandler());
 assertSame(errorHandlerSpy, saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertEquals(DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
 ContentHandler errorHandlerSpy = mock(ContentHandler.class);
 DTDHandler dtdHandlerSpy = mock(DTDHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy);
 assertSame(contentHandlerSpy, saxOutputter.getContentHandler());
 assertSame(errorHandlerSpy, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerSpy, saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertEquals(DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 ContentHandler errorHandlerSpy = mock(ContentHandler.class);
 DTDHandler dtdHandlerSpy = mock(DTDHandler.class);
 EntityResolver entityResolverSpy = mock(EntityResolver.class);
 saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy);
 assertSame(contentHandlerSpy, saxOutputter.getContentHandler());
 assertSame(errorHandlerSpy, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerSpy, saxOutputter.getDTDHandler());
 assertSame(entityResolverSpy, saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertEquals(DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterSAXOutputProcessorFormatConstructor() {
 SAXOutputProcessor saxOutputProcessorSpy = mock(SAXOutputProcessor.class);
 Format formatSpy = mock(Format.class);
 saxOutputter = new SAXOutputter(saxOutputProcessorSpy, formatSpy, contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy);
 assertSame(saxOutputProcessorSpy, saxOutputter.getSAXOutputProcessor());
 assertSame(formatSpy, saxOutputter.getFormat());
 assertSame(contentHandlerSpy, saxOutputter.getContentHandler());
 assertSame(errorHandlerSpy, saxOutputter.getErrorHandler());
 assertSame(dtdHandlerSpy, saxOutputter.getDTDHandler());
 assertSame(entityResolverSpy, saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSetContentHandler() {
 ContentHandler contentHandlerNew = mock(ContentHandler.class);
 saxOutputter.setContentHandler(contentHandlerNew);
 assertSame(contentHandlerNew, saxOutputter.getContentHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testSetContentHandlerNull() {
 saxOutputter.setContentHandler(null);
 }

 @Test
 public void testGetContentHandler() {
 assertSame(contentHandlerSpy, saxOutputter.getContentHandler());
 }

 @Test
 public void testSetErrorHandler() {
 ErrorHandler errorHandlerNew = mock(ErrorHandler.class);
 saxOutputter.setErrorHandler(errorHandlerNew);
 assertSame(errorHandlerNew, saxOutputter.getErrorHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testSetErrorHandlerNull() {
 saxOutputter.setErrorHandler(null);
 }

 @Test
 public void testGetErrorHandler() {
 assertSame(contentHandlerSpy, saxOutputter.getErrorHandler());
 }

 @Test
 public void testSetDTDHandler() {
 DTDHandler dtdHandlerNew = mock(DTDHandler.class);
 saxOutputter.setDTDHandler(dtdHandlerNew);
 assertSame(dtdHandlerNew, saxOutputter.getDTDHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testSetDTDHandlerNull() {
 saxOutputter.setDTDHandler(null);
 }

 @Test
 public void testGetDTDHandler() {
 assertSame(contentHandlerSpy, saxOutputter.getDTDHandler());
 }

 @Test
 public void testSetEntityResolver() {
 EntityResolver entityResolverNew = mock(EntityResolver.class);
 saxOutputter.setEntityResolver(entityResolverNew);
 assertSame(entityResolverNew, saxOutputter.getEntityResolver());
 }

 @Test(expected = NullPointerException.class)
 public void testSetEntityResolverNull() {
 saxOutputter.setEntityResolver(null);
 }

 @Test
 public void testGetEntityResolver() {
 assertSame(contentHandlerSpy, saxOutputter.getEntityResolver());
 }

 @Test
 public void testSetLexicalHandler() {
 LexicalHandler lexicalHandlerNew = mock(LexicalHandler.class);
 saxOutputter.setLexicalHandler(lexicalHandlerNew);
 assertSame(lexicalHandlerNew, saxOutputter.getLexicalHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testSetLexicalHandlerNull() {
 saxOutputter.setLexicalHandler(null);
 }

 @Test
 public void testGetLexicalHandler() {
 assertSame(contentHandlerSpy, saxOutputter.getLexicalHandler());
 }

 @Test
 public void testSetDeclHandler() {
 DeclHandler declHandlerNew = mock(DeclHandler.class);
 saxOutputter.setDeclHandler(declHandlerNew);
 assertSame(declHandlerNew, saxOutputter.getDeclHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testSetDeclHandlerNull() {
 saxOutputter.setDeclHandler(null);
 }

 @Test
 public void testGetDeclHandler() {
 assertSame(contentHandlerSpy, saxOutputter.getDeclHandler());
 }

 @Test
 public void testGetReportNamespaceDeclarations() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testSetReportNamespaceDeclarationsTrue() {
 saxOutputter.setReportNamespaceDeclarations(true);
 assertEquals(true, saxOutputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testSetReportNamespaceDeclarationsFalse() {
 saxOutputter.setReportNamespaceDeclarations(false);
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testGetReportDTDEvents() {
 assertEquals(true, saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSetReportDTDEventsTrue() {
 saxOutputter.setReportDTDEvents(false);
 assertEquals(false, saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSetReportDTDEventsFalse() {
 saxOutputter.setReportDTDEvents(true);
 assertEquals(true, saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 OutputStream outputStreamSpy = mock(OutputStream.class);
 saxOutputter.output(document, outputStreamSpy);
 verify(outputStreamSpy, times(1)).write(anyByteArray());
 }

 @Test
 public void testOutputNodes() throws JDOMException {
 List<Content> nodes = Arrays.asList(document.getRootElement().getChildren().toList().toArray(new Content[0]));
 OutputStream outputStreamSpy = mock(OutputStream.class);
 saxOutputter.output(nodes, outputStreamSpy);
 verify(outputStreamSpy, times(1)).write(anyByteArray());
 }

 @Test
 public void testOutputElement() throws JDOMException {
 OutputStream outputStreamSpy = mock(OutputStream.class);
 saxOutputter.output(document.getRootElement(), outputStreamSpy);
 verify(outputStreamSpy, times(1)).write(anyByteArray());
 }

 @Test
 public void testOutputFragmentNodes() throws JDOMException {
 List<Content> nodes = Arrays.asList(document.getRootElement().getChildren().toList().toArray(new Content[0]));
 OutputStream outputStreamSpy = mock(OutputStream.class);
 saxOutputter.outputFragment(nodes, outputStreamSpy);
 verify(outputStreamSpy, times(1)).write(anyByteArray());
 }

 @Test
 public void testOutputFragmentContent() throws JDOMException {
 Content content = document.getRootElement().getChildren().get(0);
 OutputStream outputStreamSpy = mock(OutputStream.class);
 saxOutputter.outputFragment(content, outputStreamSpy);
 verify(outputStreamSpy, times(1)).write(anyByteArray());
 }

}