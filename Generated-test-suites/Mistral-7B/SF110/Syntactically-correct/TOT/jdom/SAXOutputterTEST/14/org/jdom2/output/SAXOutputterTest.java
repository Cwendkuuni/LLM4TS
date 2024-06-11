package org.jdom2.output.test;

import org.junit.Before;
import org.junit.Test;
import org.jdom2.DocumentBuilderFactory;
import org.jdom2.Document;
import org.jdom2.output.SAXOutputter;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SAXOutputterTest {

 private SAXOutputter outputter;
 private DefaultHandler handler;
 private StringWriter writer;

 @Before
 public void setUp() {
 writer = new StringWriter();
 handler = new DefaultHandler() {
 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 writer.write(new String(ch, start, length));
 }
 };
 outputter = new SAXOutputter(handler, null, null, null, null);
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
 final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 doc.setDocumentType("TestDocumentType");
 doc.getRootElement().setName("TestRoot");
 doc.getRootElement().addContent(new Text("Test content"));

 outputter.setFormat(new org.jdom2.output.Format());
 outputter.output(doc);

 assertEquals("<TestDocumentType xmlns=\"\">" +
         "<TestRoot><Text>Test content</Text></TestRoot>" +
         "</TestDocumentType>", writer.toString());
 }

 @Test
 public void testOutputNullDocument() throws JDOMException {
 outputter.output(null);
 assertNotNull(writer.toString());
 assertEquals("", writer.toString());
 }

 @Test
 public void testOutputEmptyList() throws JDOMException, SAXException {
 final List<Object> emptyList = Collections.emptyList();

 outputter.output(emptyList);
 assertNotNull(writer.toString());
 assertEquals("", writer.toString());
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
 final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 final Element root = new Element("TestRoot");
 root.setText("Test content");
 doc.setDocumentElement(root);

 outputter.setFormat(new org.jdom2.output.Format());
 outputter.output(doc);

 assertEquals("<TestRoot>Test content</TestRoot>", writer.toString());
 }

 @Test(expected = JDOMException.class)
 public void testOutputNullHandler() throws JDOMException, SAXException {
 outputter.setFormat(new org.jdom2.output.Format());
 outputter.output(null);
 }

 @Test
 public void testSetContentHandler() {
 outputter = new SAXOutputter();
 outputter.setContentHandler(handler);
 assertNotNull(outputter.getContentHandler());
 assertEquals(handler, outputter.getContentHandler());
 }

 @Test
 public void testSetErrorHandler() {
 outputter = new SAXOutputter();
 outputter.setErrorHandler(handler);
 assertNotNull(outputter.getErrorHandler());
 assertEquals(handler, outputter.getErrorHandler());
 }

 @Test
 public void testSetDTDHandler() {
 outputter = new SAXOutputter();
 outputter.setDTDHandler(handler);
 assertNotNull(outputter.getDTDHandler());
 assertEquals(handler, outputter.getDTDHandler());
 }

 @Test
 public void testSetEntityResolver() {
 outputter = new SAXOutputter();
 outputter.setEntityResolver(handler);
 assertNotNull(outputter.getEntityResolver());
 assertEquals(handler, outputter.getEntityResolver());
 }

 @Test
 public void testSetLexicalHandler() {
 outputter = new SAXOutputter();
 outputter.setLexicalHandler(handler);
 assertNotNull(outputter.getLexicalHandler());
 assertEquals(handler, outputter.getLexicalHandler());
 }

 @Test
 public void testSetDeclHandler() {
 outputter = new SAXOutputter();
 outputter.setDeclHandler(handler);
 assertNotNull(outputter.getDeclHandler());
 assertEquals(handler, outputter.getDeclHandler());
 }

 @Test
 public void testSetReportNamespaceDeclarations() {
 outputter = new SAXOutputter();
 outputter.setReportNamespaceDeclarations(false);
 assertFalse(outputter.getReportNamespaceDeclarations());

 outputter.setReportNamespaceDeclarations(true);
 assertTrue(outputter.getReportNamespaceDeclarations());
 }

 @Test
 public void testSetReportDTDEvents() {
 outputter = new SAXOutputter();
 outputter.setReportDTDEvents(false);
 assertFalse(outputter.getReportDTDEvents());

 outputter.setReportDTDEvents(true);
 assertTrue(outputter.getReportDTDEvents());
 }

 @Test
 public void testSetFeature() {
 outputter = new SAXOutputter();outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
 assertTrue(outputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));
 }

 @Test
 public void testSetProperty() {
 outputter = new SAXOutputter();
 outputter.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
 assertNotNull(outputter.getLexicalHandler());
 assertEquals(handler, outputter.getLexicalHandler());
 }
}