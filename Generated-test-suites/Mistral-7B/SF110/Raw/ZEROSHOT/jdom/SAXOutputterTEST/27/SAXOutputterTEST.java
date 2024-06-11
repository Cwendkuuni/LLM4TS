package org.jdom2.output;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.Arrays;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private StringWriter stringWriter;

 @Test
 public void testSAXOutputterDefaultConstructor() {
     saxOutputter = new SAXOutputter();
     Assert.assertFalse(saxOutputter.isReportDTDEvents());
     Assert.assertFalse(saxOutputter.isReportNamespaceDeclarations());
     Assert.assertNull(saxOutputter.getContentHandler());
     Assert.assertNull(saxOutputter.getDeclHandler());
     Assert.assertNull(saxOutputter.getDTDHandler());
     Assert.assertNull(saxOutputter.getEntityResolver());
     Assert.assertNull(saxOutputter.getLexicalHandler());
     Assert.assertNull(saxOutputter.getErrorHandler());
     Assert.assertNotNull(saxOutputter.getSAXOutputProcessor());
     Assert.assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerConstructor() {
     saxOutputter = new SAXOutputter(new MyContentHandler());
     Assert.assertFalse(saxOutputter.isReportDTDEvents());
     Assert.assertFalse(saxOutputter.isReportNamespaceDeclarations());
     Assert.assertNotNull(saxOutputter.getContentHandler());
     Assert.assertNull(saxOutputter.getDeclHandler());
     Assert.assertNull(saxOutputter.getDTDHandler());
     Assert.assertNull(saxOutputter.getEntityResolver());
     Assert.assertNull(saxOutputter.getLexicalHandler());
     Assert.assertNull(saxOutputter.getErrorHandler());
     Assert.assertNotNull(saxOutputter.getSAXOutputProcessor());
     Assert.assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterContentHandlerConstructorWithHandlers() {
     saxOutputter = new SAXOutputter(new MyContentHandler(), new MyErrorHandler(), new MyDTDHandler(), new MyEntityResolver(), new MyLexicalHandler());
     Assert.assertFalse(saxOutputter.isReportDTDEvents());
     Assert.assertFalse(saxOutputter.isReportNamespaceDeclarations());
     Assert.assertNotNull(saxOutputter.getContentHandler());
     Assert.assertNotNull(saxOutputter.getDeclHandler());
     Assert.assertNotNull(saxOutputter.getDTDHandler());
     Assert.assertNotNull(saxOutputter.getEntityResolver());
     Assert.assertNotNull(saxOutputter.getLexicalHandler());
     Assert.assertNull(saxOutputter.getErrorHandler());
     Assert.assertNotNull(saxOutputter.getSAXOutputProcessor());
     Assert.assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterHandlerConstructor() {
     saxOutputter = new SAXOutputter(new MySAXOutputProcessor(), new Format(), new MyContentHandler(), new MyErrorHandler(), new MyDTDHandler(), new MyEntityResolver(), new MyLexicalHandler());
     Assert.assertFalse(saxOutputter.isReportDTDEvents());
     Assert.assertFalse(saxOutputter.isReportNamespaceDeclarations());
     Assert.assertNotNull(saxOutputter.getContentHandler());
     Assert.assertNotNull(saxOutputter.getDeclHandler());
     Assert.assertNotNull(saxOutputter.getDTDHandler());
     Assert.assertNotNull(saxOutputter.getEntityResolver());
     Assert.assertNotNull(saxOutputter.getLexicalHandler());
     Assert.assertNotNull(saxOutputter.getErrorHandler());
     Assert.assertNotNull(saxOutputter.getSAXOutputProcessor());
     Assert.assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testSAXOutputterSetters() {
     saxOutputter = new SAXOutputter();
     saxOutputter.setReportDTDEvents(true);
     saxOutputter.setReportNamespaceDeclarations(true);
     saxOutputter.setContentHandler(new MyContentHandler());
     saxOutputter.setDeclHandler(new MyDeclHandler());
     saxOutputter.setDTDHandler(new MyDTDHandler());
     saxOutputter.setEntityResolver(new MyEntityResolver());
     saxOutputter.setLexicalHandler(new MyLexicalHandler());
     saxOutputter.setErrorHandler(new MyErrorHandler());
     saxOutputter.setSAXOutputProcessor(new MySAXOutputProcessor());
     saxOutputter.setFormat(new MyFormat());
     Assert.assertTrue(saxOutputter.isReportDTDEvents());
     Assert.assertTrue(saxOutputter.isReportNamespaceDeclarations());
     Assert.assertNotNull(saxOutputter.getContentHandler());
     Assert.assertNotNull(saxOutputter.getDeclHandler());
     Assert.assertNotNull(saxOutputter.getDTDHandler());
     Assert.assertNotNull(saxOutputter.getEntityResolver());
     Assert.assertNotNull(saxOutputter.getLexicalHandler());
     Assert.assertNotNull(saxOutputter.getErrorHandler());
     Assert.assertNotNull(saxOutputter.getSAXOutputProcessor());
     Assert.assertNotNull(saxOutputter.getFormat());
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
     Document document = new Document(new Element("root"));
     saxOutputter = new SAXOutputter();
     stringWriter = new StringWriter();
     saxOutputter.setContentHandler(new MyContentHandler(stringWriter));
     saxOutputter.output(document);
     String xmlString = stringWriter.toString();
     Assert.assertNotNull(xmlString);
     Assert.assertTrue(xmlString.startsWith("<root>"));
 }

 @Test
 public void testOutputListOfContents() throws JDOMException, SAXException {
     Document document = new Document();
     document.setRootElement(new Element("root"));
     document.getRootElement().addContent(new Element("elem1"));
     document.getRootElement().addContent(new Element("elem2"));
     saxOutputter = new SAXOutputter();
     stringWriter = new StringWriter();
     saxOutputter.setContentHandler(new MyContentHandler(stringWriter));
     saxOutputter.output(document.getContent());
     String xmlString = stringWriter.toString();
     Assert.assertNotNull(xmlString);
     Assert.assertTrue(xmlString.startsWith("<root>"));
     Assert.assertTrue(xmlString.contains("<elem1>"));
     Assert.assertTrue(xmlString.contains("<elem2>"));
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
     Document document = new Document();
     Element element = new Element("element");
     document.setRootElement(element);
     saxOutputter = new SAXOutputter();
     stringWriter = new StringWriter();
     saxOutputter.setContentHandler(new MyContentHandler(stringWriter));
     saxOutputter.output(element);
     String xmlString = stringWriter.toString();
     Assert.assertNotNull(xmlString);
     Assert.assertTrue(xmlString.startsWith("<element>"));
 }

 @Test
 public void testOutputFragmentListOfContents() throws JDOMException, SAXException {
     Document document = new Document();
     document.setRootElement(new Element("root"));
     document.getRootElement().addContent(new Element("elem1"));
     document.getRootElement().addContent(new Element("elem2"));
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(document.getContent());
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNullContent() throws SAXException {
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(null);
 }

 @Test
 public void testOutputFragmentElement() throws JDOMException, SAXException {
     Document document = new Document();
     Element element = new Element("element");
     document.setRootElement(element);
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(element);
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNullElement() throws SAXException {
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(null);
 }

 @Test
 public void testOutputFragmentCDATA() throws JDOMException, SAXException {
     Document document = new Document();
     Element element = new Element("element");
     element.addContent(new CDATA("content"));
     document.setRootElement(element);
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(element.getContent());
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNullCDATA() throws SAXException {
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(null);
 }

 @Test
 public void testOutputFragmentComment() throws JDOMException, SAXException {
     Document document = new Document();
     Element element = new Element("element");
     element.addContent(new Comment("comment"));
     document.setRootElement(element);
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(element.getContent());
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNullComment() throws SAXException {
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(null);
 }

 @Test
 public void testOutputFragmentText() throws JDOMException, SAXException {
     Document document = new Document();
     Element element = new Element("element");
     element.addContent(new Text("text"));
     document.setRootElement(element);
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(element.getContent());
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNullText() throws SAXException {
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(null);
 }

 @Test
 public void testOutputFragmentProcessingInstruction() throws JDOMException, SAXException {
     Document document = new Document();
     Element element = new Element("element");
     element.addContent(new ProcessingInstruction("target", "data"));
     document.setRootElement(element);
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(element.getContent());
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNullProcessingInstruction() throws SAXException {
     saxOutputter = new SAXOutputter();
     saxOutputter.outputFragment(null);
 }

 private static class MyContentHandler extends ContentHandler {
     private StringWriter stringWriter;

     public MyContentHandler(StringWriter stringWriter) {
         this.stringWriter = stringWriter;
     }

     @Override
     public void startDocument() throws SAXException {
         // Empty implementation
     }

     @Override
     public void endDocument() throws SAXException {
         // Empty implementation
     }

     @Override
     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         stringWriter.write("<" + qName);
         for (int i = 0; i < attributes.getLength(); i++) {
             stringWriter.write(" " + attributes.getLocalName(i) + "='" + attributes.getValue(i) + "'");
         }
         stringWriter.write(">");
     }

     @Override
     public void endElement(String uri, String localName, String qName) throws SAXException {
         stringWriter.write("</" + qName + ">");
     }

     @Override
     public void characters(char[] ch, int start, int length) throws SAXException {
         stringWriter.write(new String(ch, start, length));
     }
 }

 private static class MyDeclHandler extends DeclHandler {
     // Empty implementation
 }

 private static class MyDTDHandler extends DTDHandler {
     // Empty implementation
 }

 private static class MyEntityResolver implements EntityResolver {
     // Empty implementation
 }

 private static class MyLexicalHandler extends LexicalHandler {
     // Empty implementation
 }

 private static class MyErrorHandler implements ErrorHandler {
     // Empty implementation
 }

 private static class MyFormat implements Format {
     // Empty implementation
 }

 private static class MySAXOutputProcessor extends AbstractSAXOutputProcessor {
     // Empty implementation
 }
}