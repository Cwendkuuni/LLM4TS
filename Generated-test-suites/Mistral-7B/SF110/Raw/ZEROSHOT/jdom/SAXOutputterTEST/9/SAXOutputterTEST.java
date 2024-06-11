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
 private Document document;
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
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock, lexicalHandlerMock, declHandlerMock);
 document = new JDOMBuilder().setOMMode(true).buildDocument();
 }

 @Test
 public void testSAXOutputterDefaultConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 verify(contentHandlerMock).setDocumentLocator(isA(Locator.class));
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 verify(contentHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(errorHandlerMock).setDocumentLocator(isA(Locator.class));
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 verify(contentHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(errorHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(dtdHandlerMock).setDocumentLocator(isA(Locator.class));
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 verify(contentHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(errorHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(dtdHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(entityResolverMock).setDocumentLocator(isA(Locator.class));
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 verify(contentHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(errorHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(dtdHandlerMock).setDocumentLocator(isA(Locator.class));
 verify(entityResolverMock).setDocumentLocator(isA(Locator.class));
 verify(lexicalHandlerMock).setDocumentLocator(isA(Locator.class));
 }

 @Test
 public void testSAXOutputterProcessorFormatConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterProcessorFormatContentHandlerConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 assertSame(saxOutputter.getContentHandler(), contentHandlerMock);
 }

 @Test
 public void testSAXOutputterProcessorFormatContentHandlerErrorHandlerConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotNull(saxOutputter.getFormat());
 assertSame(saxOutputter.getContentHandler(), contentHandlerMock);
 assertSame(saxOutputter.getErrorHandler(), errorHandlerMock);
 }

 @Test
 public void testSetContentHandler() {
 ContentHandler newContentHandler = mock(ContentHandler.class);
 saxOutputter.setContentHandler(newContentHandler);
 assertSame(newContentHandler, saxOutputter.getContentHandler());
 }

 @Test
 public void testGetContentHandler() {
 assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
 }

 @Test
 public void testSetErrorHandler() {
 ErrorHandler newErrorHandler = mock(ErrorHandler.class);
 saxOutputter.setErrorHandler(newErrorHandler);
 assertSame(newErrorHandler, saxOutputter.getErrorHandler());
 }

 @Test
 public void testGetErrorHandler() {
 assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
 }

 @Test
 public void testSetDTDHandler() {
 DTDHandler newDTDHandler = mock(DTDHandler.class);
 saxOutputter.setDTDHandler(newDTDHandler);
 assertSame(newDTDHandler, saxOutputter.getDTDHandler());
 }

 @Test
 public void testGetDTDHandler() {
 assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
 }

 @Test
 public void testSetEntityResolver() {
 EntityResolver newEntityResolver = mock(EntityResolver.class);
 saxOutputter.setEntityResolver(newEntityResolver);
 assertSame(newEntityResolver, saxOutputter.getEntityResolver());
 }

 @Test
 public void testGetEntityResolver() {
 assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
 }

 @Test
 public void testSetLexicalHandler() {
 LexicalHandler newLexicalHandler = mock(LexicalHandler.class);
 saxOutputter.setLexicalHandler(newLexicalHandler);
 assertSame(newLexicalHandler, saxOutputter.getLexicalHandler());
 }

 @Test
 public void testGetLexicalHandler() {
 assertEquals(lexicalHandlerMock, saxOutputter.getLexicalHandler());
 }

 @Test
 public void testSetDeclHandler() {
 DeclHandler newDeclHandler = mock(DeclHandler.class);
 saxOutputter.setDeclHandler(newDeclHandler);
 assertSame(newDeclHandler, saxOutputter.getDeclHandler());
 }

 @Test
 public void testGetDeclHandler() {
 assertEquals(declHandlerMock, saxOutputter.getDeclHandler());
 }

 @Test
 public void testSetReportNamespaceDeclarations() {
 saxOutputter.setReportNamespaceDeclarations(true);
 assertTrue(saxOutputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testGetReportNamespaceDeclarations() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testSetReportDTDEvents() {
 saxOutputter.setReportDTDEvents(false);
 assertFalse(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testGetReportDTDEvents() {
 assertEquals(true, saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSetFeatureNamespacePrefixes() {
 saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
 assertTrue(saxOutputter.getReportNamespaceDeclarations());
 }

 @Test(expected = SAXNotRecognizedException.class)
 public void testSetFeatureInvalidName() {
 saxOutputter.setFeature("http://xml.org/sax/features/invalidName", true);
 }

 @Test(expected = SAXNotSupportedException.class)
 public void testSetFeatureNotSupported() {
 saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
 }

 @Test
 public void testSetPropertyLexicalHandler() {
 LexicalHandler newLexicalHandler = mock(LexicalHandler.class);
 saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", newLexicalHandler);
 assertSame(newLexicalHandler, saxOutputter.getLexicalHandler());
 }

 @Test
 public void testSetPropertyDeclHandler() {
 DeclHandler newDeclHandler = mock(DeclHandler.class);
 saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", newDeclHandler);
 assertSame(newDeclHandler, saxOutputter.getDeclHandler());
 }

 @Test(expected = SAXNotRecognizedException.class)
 public void testSetPropertyInvalidName() {
 saxOutputter.setProperty("http://xml.org/sax/properties/invalidName", new DeclHandler());
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 saxOutputter.output(document);
 verify(saxOutputTarget).setDocument(document);
 }

 @Test
 public void testOutputNodes() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 List<Content> nodes = new ArrayList<>();
 nodes.add(document.getDocumentElement());
 saxOutputter.output(nodes);
 verify(saxOutputTarget).startDocument();
 verify(saxOutputTarget).characters(isA(CharSequence.class));
 }

 @Test
 public void testOutputElement() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 saxOutputter.output(document.getDocumentElement());
 verify(saxOutputTarget).startElement(isA(String.class), isA(String.class), null, anyArray());
 }

 @Test
 public void testOutputText() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 Text text = document.getDocumentElement().getTextTrim();
 saxOutputter.outputFragment(text);
 verify(saxOutputTarget).characters(isA(CharSequence.class), 0, text.getLength());
 }

 @Test
 public void testOutputComment() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 Comment comment = new Comment("Test comment");
 saxOutputter.outputFragment(comment);
 verify(saxOutputTarget).comment(comment.getText());
 }

 @Test
 public void testOutputProcessingInstruction() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 ProcessingInstruction pi = new ProcessingInstruction("xml-version", "1.0");
 saxOutputter.outputFragment(pi);
 verify(saxOutputTarget).processingInstruction(pi.getTarget(), pi.getData());
 }

 @Test
 public void testOutputCDATA() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 CDATA cdata = new CDATA("Test CDATA section");
 saxOutputter.outputFragment(cdata);
 verify(saxOutputTarget).characters(isA(CharSequence.class), 0, cdata.getText().length(), XMLConstants.CDATA_PREFIX, XMLConstants.CDATA_SUFFIX);
 }

 @Test
 public void testOutputEntityRef() throws JDOMException {
 SAXOutputTarget saxOutputTarget = mock(SAXOutputTarget.class);
 saxOutputter.setSAXOutputProcessor(new SAXOutputProcessor(saxOutputTarget));
 EntityRef entityRef = new EntityRef("Test entity ref");
 saxOutputter.outputFragment(entityRef);
 verify(saxOutputTarget).entityRef(entityRef.getName());
 }
}