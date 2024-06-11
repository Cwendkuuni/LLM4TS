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
 private Document document;
 private ContentHandler contentHandlerSpy;
 private ErrorHandler errorHandlerSpy;
 private DTDHandler dtdHandlerSpy;
 private EntityResolver entityResolverSpy;
 private LexicalHandler lexicalHandlerSpy;
 private DeclHandler declHandlerSpy;

 @Before
 public void setUp() {
 contentHandlerSpy = mock(ContentHandler.class);
 errorHandlerSpy = mock(ErrorHandler.class);
 dtdHandlerSpy = mock(DTDHandler.class);
 entityResolverSpy = mock(EntityResolver.class);
 lexicalHandlerSpy = mock(LexicalHandler.class);
 declHandlerSpy = mock(DeclHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy, lexicalHandlerSpy, declHandlerSpy);
 document = new Document();
 }

 @Test
 public void testSAXOutputterDefaultConstructor() {
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
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
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 verify(contentHandlerSpy).setDocumentLocator(any(Locator.class));
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy);
 assertNull(saxOutputter.getDTDHandler());
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 verify(contentHandlerSpy).setDocumentLocator(any(Locator.class));
 verify(errorHandlerSpy).setDocumentLocator(any(Locator.class));
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy);
 assertNull(saxOutputter.getEntityResolver());
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 verify(contentHandlerSpy).setDocumentLocator(any(Locator.class));
 verify(errorHandlerSpy).setDocumentLocator(any(Locator.class));
 verify(dtdHandlerSpy).setDocumentLocator(any(Locator.class));
 }

 @Test
 public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
 saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy);
 assertNull(saxOutputter.getLexicalHandler());
 assertNull(saxOutputter.getDeclHandler());
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 verify(contentHandlerSpy).setDocumentLocator(any(Locator.class));
 verify(errorHandlerSpy).setDocumentLocator(any(Locator.class));
 verify(dtdHandlerSpy).setDocumentLocator(any(Locator.class));
 verify(entityResolverSpy).setDocumentLocator(any(Locator.class));
 }

 @Test
 public void testSAXOutputterSAXOutputProcessorFormatConstructor() {
 saxOutputter = new SAXOutputter(null, null, null, null, null, null, null, false, true, null, null, new DefaultSAXOutputProcessor(), Format.getRawFormat(), contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy, lexicalHandlerSpy, declHandlerSpy);
 assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(true, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
 verify(contentHandlerSpy).setDocumentLocator(any(Locator.class));
 }

 @Test
 public void testSAXOutputterSettersGetters() {
 saxOutputter.setReportNamespaceDeclarations(true);
 saxOutputter.setReportDTDEvents(false);
 saxOutputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
 saxOutputter.setFormat(Format.getPrettyFormat());
 assertEquals(true, saxOutputter.getReportNamespaceDeclarations());
 assertEquals(false, saxOutputter.getReportDTDEvents());
 assertNotNull(saxOutputter.getSAXOutputProcessor());
 assertNotEquals(Format.getRawFormat(), saxOutputter.getFormat());
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 final List<String> output = new ArrayList<>();
 final SAXOutputter saxOutputter = spy(new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy, lexicalHandlerSpy, declHandlerSpy));
 final OutputStream os = new ByteArrayOutputStream();
 saxOutputter.setOutput(os);
 when(contentHandlerSpy.startDocument()).thenReturn(true);
 when(contentHandlerSpy.startElement("root", null, null, null)).thenReturn(true);
 when(contentHandlerSpy.startElement("child", null, null, null)).thenReturn(true);
 when(contentHandlerSpy.endElement("child")).thenReturn(true);
 when(contentHandlerSpy.endElement("root")).thenReturn(true);
 when(contentHandlerSpy.endDocument()).thenReturn(true);
 saxOutputter.output(document);
 String actualOutput = new String(os.toByteArray());
 String expectedOutput = "<root>\n<child/>\n</root>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputList() throws JDOMException {
 final List<String> output = new ArrayList<>();
 final SAXOutputter saxOutputter = spy(new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy, lexicalHandlerSpy, declHandlerSpy));
 final OutputStream os = new ByteArrayOutputStream();
 saxOutputter.setOutput(os);
 List<Content> nodes = new ArrayList<>();
 nodes.add(new Element("root"));
 nodes.add(new Text("text"));
 saxOutputter.output(nodes);
 String actualOutput = new String(os.toByteArray());
 String expectedOutput = "<root>\n<text/>\n</root>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputElement() throws JDOMException {
 final List<String> output = new ArrayList<>();
 final SAXOutputter saxOutputter = spy(new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy, lexicalHandlerSpy, declHandlerSpy));
 final OutputStream os = new ByteArrayOutputStream();
 saxOutputter.setOutput(os);
 saxOutputter.output(new Element("root"));
 String actualOutput = new String(os.toByteArray());
 String expectedOutput = "<root/>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testOutputFragment() throws JDOMException {
 final List<String> output = new ArrayList<>();
 final SAXOutputter saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy, lexicalHandlerSpy, declHandlerSpy);
 final OutputStream os = new ByteArrayOutputStream();
 saxOutputter.setOutput(os);
 Content node = new Element("root");
 node.addContent(new Text("text"));
 saxOutputter.outputFragment(node);
 String actualOutput = new String(os.toByteArray());
 String expectedOutput = "<root>\n<text/>\n</root>";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentInvalidNode() throws JDOMException {
 final SAXOutputter saxOutputter = new SAXOutputter(contentHandlerSpy, errorHandlerSpy, dtdHandlerSpy, entityResolverSpy, lexicalHandlerSpy, declHandlerSpy);
 final OutputStream os = new ByteArrayOutputStream();
 saxOutputter.setOutput(os);
 Content node = new Attribute("attribute");
 saxOutputter.outputFragment(node);
 }
}