package org.jdom2.output;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import static org.junit.Assert.*;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private DefaultHandler handler;

 @Before
 public void setup() {
 saxOutputter = new SAXOutputter();
 handler = new DefaultHandler();
 saxOutputter.setContentHandler(handler);
 }

 @Test
 public void testEmptyOutput() throws JDOMException, SAXException {
 Document document = new Document();
 saxOutputter.output(document);
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Text("Hello World"));
 saxOutputter.output(document);
 }

 @Test
 public void testOutputList() throws JDOMException, SAXException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Text("Hello"));
 root.addContent(new Text("World"));
 List<Content> list = document.getContent();
 saxOutputter.output(list);
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
 Element element = new Element("element");
 saxOutputter.output(element);
 }

 @Test
 public void testOutputFragmentList() throws JDOMException, SAXException {
 List<Content> fragment = newArrayList();
 fragment.add(new Text("Fragment 1"));
 fragment.add(new Text("Fragment 2"));
 saxOutputter.outputFragment(fragment);
 }

 @Test
 public void testOutputFragmentElement() throws JDOMException, SAXException {
 Element fragment = new Element("fragment");
 saxOutputter.outputFragment(fragment);
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentInvalidContent() throws JDOMException, SAXException {
 Content invalidContent = new CDATA("Invalid Content");
 saxOutputter.outputFragment(invalidContent);
 }

 @Test
 public void testSetContentHandler() {
 saxOutputter.setContentHandler(null);
 saxOutputter.setContentHandler(handler);
 assertSame(handler, saxOutputter.getContentHandler());
 }

 @Test
 public void testSetErrorHandler() {
 ErrorHandler errorHandler = new DefaultErrorHandler();
 saxOutputter.setErrorHandler(errorHandler);
 assertSame(errorHandler, saxOutputter.getErrorHandler());
 }

 @Test
 public void testSetDTDHandler() {
 DTDHandler dtdHandler = new DefaultDTDHandler();
 saxOutputter.setDTDHandler(dtdHandler);
 assertSame(dtdHandler, saxOutputter.getDTDHandler());
 }

 @Test
 public void testSetEntityResolver() {
 EntityResolver entityResolver = new DefaultEntityResolver();
 saxOutputter.setEntityResolver(entityResolver);
 assertSame(entityResolver, saxOutputter.getEntityResolver());
 }

 @Test
 public void testSetLexicalHandler() {
 LexicalHandler lexicalHandler = new DefaultLexicalHandler();
 saxOutputter.setLexicalHandler(lexicalHandler);
 assertSame(lexicalHandler, saxOutputter.getLexicalHandler());
 }

 @Test
 public void testSetDeclHandler() {
 DeclHandler declHandler = new DefaultDeclHandler();
 saxOutputter.setDeclHandler(declHandler);
 assertSame(declHandler, saxOutputter.getDeclHandler());
 }

 @Test
 public void testSetReportNamespaceDeclarations() {
 boolean reportDeclarations = true;
 saxOutputter.setReportNamespaceDeclarations(reportDeclarations);
 assertEquals(reportDeclarations, saxOutputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testSetReportDTDEvents() {
 boolean reportDtdEvents = false;
 saxOutputter.setReportDTDEvents(reportDtdEvents);
 assertEquals(reportDtdEvents, saxOutputter.getReportDTDEvents());
 }

 @Test
 public void testSetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
 saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
 assertFalse(saxOutputter.getReportNamespaceDeclarations());
 saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
 assertFalse(saxOutputter.getReportDTDEvents());
 }

 @Test(expected = SAXNotRecognizedException.class)
 public void testSetFeatureUnrecognized() throws SAXNotRecognizedException, SAXNotSupportedException {
 saxOutputter.setFeature("http://xml.org/sax/features/notRecognized", false);
 }

 @Test(expected = SAXNotSupportedException.class)
 public void testSetFeatureNotSupported() throws SAXNotRecognizedException, SAXNotSupportedException {
 saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
 }

 @Test
 public void testSetProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
 LexicalHandler customLexicalHandler = new DefaultLexicalHandler() {
 @Override
 public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
 super.startElement(uri, localName, qName, atts);
 System.out.println("Lexical Handler: " + qName + " started");
 }
 };
 saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", customLexicalHandler);
 saxOutputter.setProperty("http://xml.org/sax/handlers/LexicalHandler", customLexicalHandler);
 DeclHandler customDeclHandler = new DefaultDeclHandler() {
 @Override
 public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
 super.startElement(uri, localName, qName, atts);
 System.out.println("Decl Handler: " + qName + " started");
 }
 };
 saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", customDeclHandler);
 saxOutputter.setProperty("http://xml.org/sax/handlers/DeclHandler", customDeclHandler);
 }
}