package org.jdom2.output;

import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2. processing.SAXTarget;
import org.jdom2. output.support.DefaultSAXOutputProcessor;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.DefaultEntityResolver;
import org.xml.sax.helpers.DefaultDTDHandler;
import org.xml.sax.helpers.DefaultLexicalHandler;
import org.xml.sax.helpers.DefaultDeclHandler;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class SAXOutputterTest {

 SAXOutputter saxOutputter;
 Document document;
 Element element;
 List<Content> nodes;
 Content node;
 SAXTarget saxTarget;

 @Before
 public void setUp() {
 saxOutputter = new SAXOutputter();
 document = new Document();
 element = new Element("root");
 nodes = Arrays.asList(new Text("text"), new Comment("comment"), new Element("child"));
 node = new Text("text");
 saxTarget = saxOutputter.buildTarget(document);
 }

 @Test
 public void testSAXOutputterDefaultConstructor() {
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 assertNull(saxOutputter.getContentHandler());
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSAXOutputterContentHandlerConstructor() {
 ContentHandler contentHandlerMock = mock(ContentHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerMock);
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
 ContentHandler contentHandlerMock = mock(ContentHandler.class);
 ErrorHandler errorHandlerMock = mock(ErrorHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock);
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
 assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
 ContentHandler contentHandlerMock = mock(ContentHandler.class);
 ErrorHandler errorHandlerMock = mock(ErrorHandler.class);
 DTDHandler dtdHandlerMock = mock(DTDHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock);
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
 assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
 assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 ContentHandler contentHandlerMock = mock(ContentHandler.class);
 ErrorHandler errorHandlerMock = mock(ErrorHandler.class);
 DTDHandler dtdHandlerMock = mock(DTDHandler.class);
 EntityResolver entityResolverMock = mock(EntityResolver.class);
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock);
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
 assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
 assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
 ContentHandler contentHandlerMock = mock(ContentHandler.class);
 ErrorHandler errorHandlerMock = mock(ErrorHandler.class);
 DTDHandler dtdHandlerMock = mock(DTDHandler.class);
 EntityResolver entityResolverMock = mock(EntityResolver.class);
 LexicalHandler lexicalHandlerMock = mock(LexicalHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock, lexicalHandlerMock);
 assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
 assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
 assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
 assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
 assertEquals(lexicalHandlerMock, saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSAXOutputterProcessorAndFormatConstructor() {
 SAXOutputProcessor processorMock = mock(SAXOutputProcessor.class);
 Format formatMock = mock(Format.class);
 saxOutputter = new SAXOutputter(processorMock, formatMock, null, null, null, null, null, null);
 assertEquals(processorMock, saxOutputter.getSAXOutputProcessor());
 assertEquals(formatMock, saxOutputter.getFormat());
 assertNull(saxOutputter.getContentHandler());
 assertNull(saxOutputter.getErrorHandler());
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 assertTrue(saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSettersAndGetters() {
 ContentHandler contentHandlerMock = mock(ContentHandler.class);
 ErrorHandler errorHandlerMock = mock(ErrorHandler.class);
 DTDHandler dtdHandlerMock = mock(DTDHandler.class);
 EntityResolver entityResolverMock = mock(EntityResolver.class);
 LexicalHandler lexicalHandlerMock = mock(LexicalHandler.class);
 DeclHandler declHandlerMock = mock(DeclHandler.class);

 saxOutputter = new SAXOutputter();
 saxOutputter.setContentHandler(contentHandlerMock);
 assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
 saxOutputter.setErrorHandler(errorHandlerMock);
 assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
 saxOutputter.setDTDHandler(dtdHandlerMock);
 assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
 saxOutputter.setEntityResolver(entityResolverMock);
 assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
 saxOutputter.setLexicalHandler(lexicalHandlerMock);
 assertEquals(lexicalHandlerMock, saxOutputter.getLexicalHandler());
 saxOutputter.setDeclHandler(declHandlerMock);
 assertEquals(declHandlerMock, saxOutputter.getDeclHandler());
 saxOutputter.setReportNamespaceDeclarations(true);
 assertTrue(saxOutputter.getReportNamespaceDeclarations());
 saxOutputter.setReportDTDEvents(false);
 assertFalse(saxOutputter.getReportDTDEvents());
 saxOutputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 assertEquals(new DefaultSAXOutputProcessor(), saxOutputter.getSAXOutputProcessor());
 saxOutputter.setFormat(formatMock);
 assertEquals(formatMock, saxOutputter.getFormat());
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
 saxOutputter.output(document);
 verify(saxTarget, times(1)).setDocument(document);
 }

 @Test
 public void testOutputNodes() throws JDOMException, SAXException {
 saxOutputter.output(nodes);
 for (Content content : nodes) {
 verify(saxTarget, times(1)).startElement(content.getName().getLocalPart(), null);
 verify(saxTarget, times(1)).characters(content.getText().getBytes("UTF-8"));
 }
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
 saxOutputter.output(element);
 verify(saxTarget, times(1)).startElement("root", null);
 verify(saxTarget, times(1)).endElement("root");
 }

 @Test
 public void testOutputFragment() throws JDOMException, SAXException {
 saxOutputter.outputFragment(node);
 switch (node.getCType()) {
 case CDATA:
 verify(saxTarget, times(1)).ignorableWhitespace(node.getText().getBytes("UTF-8"), node.getText().length());
 break;
 case Comment:
 verify(saxTarget, times(1)).comment(node.getText().getBytes("UTF-8"), node.getText().length());
 break;
 case Element:
 verify(saxTarget, times(1)).startElement(node.getName().getLocalPart(), null);
 verify(saxTarget, times(1)).endElement(node.getName().getLocalPart());
 break;
 case EntityRef:
 verify(saxTarget, times(1)).processingInstruction("xml-internal-external", " SYSTEM " + node.getText(), null);
 break;
 case ProcessingInstruction:
 verify(saxTarget, times(1)).processingInstruction(node.getTarget(), node.getData().getBytes("UTF-8"), null);
 break;
 case Text:
 verify(saxTarget, times(1)).characters(node.getText().getBytes("UTF-8"), node.getText().length());
 break;
 }
 }
}