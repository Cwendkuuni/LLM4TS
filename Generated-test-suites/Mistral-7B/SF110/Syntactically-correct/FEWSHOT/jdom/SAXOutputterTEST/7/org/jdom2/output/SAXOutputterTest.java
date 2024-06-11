package org.jdom2.output;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;

import java.io.StringWriter;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private SAXTarget saxTarget;
 private Document document;
 private StringWriter writer;

 @Before
 public void setUp() {
 saxOutputter = new SAXOutputter();
 saxTarget = mock(SAXTarget.class);
 writer = new StringWriter();
 document = new JDOMBuilder().setOMFactory(new JDOMFactory()).buildDocument();
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 saxOutputter.setContentHandler(saxTarget);
 saxOutputter.setErrorHandler(saxTarget);
 saxOutputter.setWriter(writer);
 saxOutputter.output(document);
 verify(saxTarget).startDocument();
 verify(saxTarget).startElement("document", document.getRootElement());
 saxOutputter.outputEndDocument();
 verify(saxTarget).endDocument();
 assertEquals("<document>\n" + document.getRootElement().toString(Format.getPrettyFormat()) + "</document>", writer.toString());
 }

 @Test
 public void testOutputListOfContents() throws JDOMException {
 List<Content> nodes = new ArrayList<>();
 nodes.add(document.getRootElement().getChild("child1"));
 nodes.add(document.getRootElement().getChild("child2"));
 saxOutputter.setContentHandler(saxTarget);
 saxOutputter.setErrorHandler(saxTarget);
 saxOutputter.setWriter(writer);
 saxOutputter.output(nodes);
 verify(saxTarget, times(2)).startElement("element");
 verify(saxTarget, times(2)).characters(anyString());
 verify(saxTarget).endElement("element");
 verify(saxTarget, times(2)).startElement("element");
 verify(saxTarget, times(2)).endElement("element");
 assertEquals("<child1></child1>\n<child2></child2>", writer.toString());
 }

 @Test
 public void testOutputElement() throws JDOMException {
 saxOutputter.setContentHandler(saxTarget);
 saxOutputter.setErrorHandler(saxTarget);
 saxOutputter.setWriter(writer);
 saxOutputter.output(document.getRootElement());
 verify(saxTarget).startDocument();
 verify(saxTarget).startElement("document", document.getRootElement());
 saxOutputter.outputEndDocument();
 verify(saxTarget).endDocument();
 assertEquals("<document>\n<rootElement>\n</rootElement>\n</document>", writer.toString());
 }

 @Test
 public void testOutputFragmentListOfContents() throws JDOMException {
 List<Content> nodes = new ArrayList<>();
 nodes.add(document.getRootElement().getChild("child1"));
 nodes.add(document.getRootElement().getChild("child2"));
 saxOutputter.setContentHandler(saxTarget);
 saxOutputter.setErrorHandler(saxTarget);
 saxOutputter.outputFragment(nodes);
 verify(saxTarget, times(2)).startElement("element");
 verify(saxTarget, times(2)).characters(anyString());
 verify(saxTarget).endElement("element");
 }

 @Test
 public void testOutputFragmentElement() throws JDOMException {
 Element element = document.getRootElement().getChild("child1");
 saxOutputter.setContentHandler(saxTarget);
 saxOutputter.setErrorHandler(saxTarget);
 saxOutputter.outputFragment(element);
 verify(saxTarget).startElement("element");
 verify(saxTarget).characters(element.getText());
 verify(saxTarget).endElement("element");
 }

 @Test(expected = JDOMException.class)
 public void testOutputInvalidContent() throws JDOMException {
 Content invalidContent = new JDOMFactory().buildProcessingInstruction("xml", "test");
 saxOutputter.outputFragment(invalidContent);
 fail("Should throw JDOMException");
 }

 @Test
 public void testSetReportNamespaceDeclarations() {
 saxOutputter = new SAXOutputter();
 saxOutputter.setReportNamespaceDeclarations(true);
 saxOutputter = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), saxOutputter.getFormat(), saxOutputter.getContentHandler(), saxOutputter.getErrorHandler(), saxOutputter.getDTDHandler(), saxOutputter.getEntityResolver(), saxOutputter.getLexicalHandler());
 saxOutputter.setReportNamespaceDeclarations(false);
 }

 @Test
 public void testSetReportDTDEvents() {
 saxOutputter = new SAXOutputter();
 saxOutputter = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), saxOutputter.getFormat(), saxOutputter.getContentHandler(), saxOutputter.getErrorHandler(), saxOutputter.getDTDHandler(), saxOutputter.getEntityResolver(), saxOutputter.getLexicalHandler());
 saxOutputter.setReportDTDEvents(true);
 saxOutputter = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), saxOutputter.getFormat(), saxOutputter.getContentHandler(), saxOutputter.getErrorHandler(), saxOutputter.getDTDHandler(), saxOutputter.getEntityResolver(), saxOutputter.getLexicalHandler());
 saxOutputter.setReportDTDEvents(false);
 }

 @Test
 public void testSetFeatureNamespaces() throws SAXNotRecognizedException, SAXNotSupportedException {
 saxOutputter = new SAXOutputter();
 saxOutputter.setFeature("http://xml.org/sax/features/namespaces", true);
 saxOutputter = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), saxOutputter.getFormat(), saxOutputter.getContentHandler(), saxOutputter.getErrorHandler(), saxOutputter.getDTDHandler(), saxOutputter.getEntityResolver(), saxOutputter.getLexicalHandler());
 saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
 }

 @Test
 public void testSetFeatureValidation() throws SAXNotRecognizedException, SAXNotSupportedException {
 saxOutputter = new SAXOutputter();
 saxOutputter.setFeature("http://xml.org/sax/features/validation", true);
 saxOutputter = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), saxOutputter.getFormat(), saxOutputter.getContentHandler(), saxOutputter.getErrorHandler(), saxOutputter.getDTDHandler(), saxOutputter.getEntityResolver(), saxOutputter.getLexicalHandler());
 saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
 }

 @Test
 public void testSetFeatureNamespacePrefixes() throws SAXNotRecognizedException, SAXNotSupportedException {
 saxOutputter = new SAXOutputter();
 saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
 saxOutputter = new SAXOutputter(saxOutputter.getSAXOutputProcessor(), saxOutputter.getFormat(), saxOutputter.getContentHandler(), saxOutputter.getErrorHandler(), saxOutputter.getDTDHandler(), saxOutputter.getEntityResolver(), saxOutputter.getLexicalHandler());
 saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
 }

 @Test
 public void testSetSAXOutputProcessor() {
 SAXOutputProcessor saxOutputProcessor = mock(SAXOutputProcessor.class);
 saxOutputter = new SAXOutputter();
 saxOutputter.setSAXOutputProcessor(saxOutputProcessor);
 assertEquals(saxOutputProcessor, saxOutputter.getSAXOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 Format format = mock(Format.class);
 saxOutputter = new SAXOutputter();
 saxOutputter.setFormat(format);
 assertEquals(format, saxOutputter.getFormat());
 }
}