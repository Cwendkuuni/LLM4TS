package org.jdom2.transform;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMInputFactory;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.helpers.DefaultHandler;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private Document inputDocument;
 private JDOMInputFactory inputFactory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 inputFactory = new JDOMInputFactory();
 }

 @Test
 public void testEmptyResult() {
 jdomResult.setResult(null);
 List<Content> result = jdomResult.getResult();
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSetResult() {
 Content testContent = new Element("test");
 List<Content> testList = Arrays.asList(testContent);
 jdomResult.setResult(testList);
 List<Content> result = jdomResult.getResult();
 assertEquals(testList, result);
 }

 @Test
 public void testSetDocument() throws SAXException, IOException {
 String xmlInput = "<root><test>Test</test></root>";
 SAXBuilder builder = new SAXBuilder();
 inputDocument = builder.build(new StringReader(xmlInput));
 jdomResult.setDocument(inputDocument);
 List<Content> result = jdomResult.getResult();
 Document jdomResultDocument = jdomResult.getDocument();
 assertEquals(inputDocument, jdomResultDocument);
 assertEquals(inputDocument.getRootElement(), result.get(0));
 }

 @Test
 public void testGetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory resultFactory = jdomResult.getFactory();
 assertEquals(factory, resultFactory);
 }

 @Test
 public void testRetrieveResult() throws SAXException, IOException {
 String xmlInput = "<root><test>Test</test></root>";
 SAXBuilder builder = new SAXBuilder();
 inputDocument = builder.build(new StringReader(xmlInput));
 jdomResult.setDocument(inputDocument);
 List<Content> result = jdomResult.getResult();
 Document jdomResultDocument = jdomResult.getDocument();
 assertEquals(inputDocument, jdomResultDocument);
 assertEquals(inputDocument.getRootElement(), result.get(0));
 jdomResult.retrieveResult();
 result = jdomResult.getResult();
 jdomResultDocument = jdomResult.getDocument();
 assertEquals(inputDocument, jdomResultDocument);
 assertEquals(inputDocument.getRootElement(), result.get(0));
 }

 @Test
 public void testSetResultWithDocument() throws SAXException, IOException {
 String xmlInput = "<root><test>Test</test></root>";
 SAXBuilder builder = new SAXBuilder();
 inputDocument = builder.build(new StringReader(xmlInput));
 jdomResult.setDocument(inputDocument);
 List<Content> testList = Arrays.asList(inputDocument.getRootElement());
 jdomResult.setResult(testList);
 List<Content> result = jdomResult.getResult();
 assertEquals(testList, result);
 }

 @Test
 public void testGetDocumentWithList() throws SAXException, IOException {
 String xmlInput = "<root><test>Test</test></root>";
 SAXBuilder builder = new SAXBuilder();
 inputDocument = builder.build(new StringReader(xmlInput));
 jdomResult.setDocument(inputDocument);
 List<Content> testList = Arrays.asList(inputDocument.getRootElement());
 jdomResult.setResult(testList);
 Document jdomResultDocument = jdomResult.getDocument();
 assertEquals(inputDocument, jdomResultDocument);
 }

 @Test
 public void testGetDocumentWithEmptyResult() throws SAXException, IOException {
 jdomResult.setResult(Collections.emptyList());
 Document jdomResultDocument = jdomResult.getDocument();
 assertNull(jdomResultDocument);
 }

 @Test
 public void testGetDocumentWithNullResult() {
 jdomResult.setResult(null);
 Document jdomResultDocument = jdomResult.getDocument();
 assertNull(jdomResultDocument);
 }

 @Test
 public void testSetHandler() {
 DefaultHandler handler = new DefaultHandler();
 jdomResult.setHandler(handler);
 assertSame(handler, jdomResult.getHandler());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetHandlerWithNull() {
 jdomResult.setHandler(null);
 }

 @Test
 public void testSetLexicalHandler() {
 DefaultHandler handler = new DefaultHandler();
 jdomResult.setLexicalHandler(handler);
 assertSame(handler, jdomResult.getLexicalHandler());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetLexicalHandlerWithNull() {
 jdomResult.setLexicalHandler(null);
 }
}