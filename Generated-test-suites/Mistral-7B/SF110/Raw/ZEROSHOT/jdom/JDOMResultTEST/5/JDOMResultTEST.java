package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMBuilderFactory;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResultList;
 private Document expectedDocument;
 private JDOMFactory jdomFactory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 expectedResultList = new ArrayList<>();
 expectedDocument = new JDOMFactory().newDocument();
 jdomFactory = new JDOMFactory() {
 @Override
 public ContentHandler getContentHandler() {
 return new DefaultHandler() {
 @Override
 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 expectedResultList.add(expectedDocument.getRootElement().addContent(new Element(localName, attributes)));
 }
 };
 }
 };
 }

 @Test
 public void testGetResultWithEmptyList() {
 jdomResult.setResult(Collections.emptyList());
 List<Content> result = jdomResult.getResult();
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetResultWithListAsResult() {
 jdomResult.setResult(expectedResultList);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResultList, result);
 }

 @Test
 public void testGetResultWithDocumentAsResult() {
 jdomResult.setDocument(expectedDocument);
 Document result = jdomResult.getDocument();
 assertEquals(expectedDocument, result);
 }

 @Test
 public void testGetDocumentWithListAsResult() {
 jdomResult.setResult(expectedResultList);
 Document result = jdomResult.getDocument();
 assertEquals(expectedDocument, result);
 }

 @Test
 public void testGetDocumentWithNullAsResult() {
 jdomResult.setResult(null);
 Document result = jdomResult.getDocument();
 assertNull(result);
 }

 @Test
 public void testGetDocumentWithEmptyListAsResult() {
 jdomResult.setResult(Collections.emptyList());
 Document result = jdomResult.getDocument();
 assertNull(result);
 }

 @Test
 public void testSetFactoryWithNullFactory() {
 JDOMResult jdomResult = new JDOMResult();
 jdomResult.setFactory(null);
 assertNull(jdomResult.getFactory());
 }

 @Test
 public void testSetFactoryWithJDOMFactory() {
 JDOMResult jdomResult = new JDOMResult();
 JDOMFactory jdomFactory = new JDOMFactory();
 jdomResult.setFactory(jdomFactory);
 assertEquals(jdomFactory, jdomResult.getFactory());
 }

 @Test
 public void testSetResultWithList() {
 JDOMResult jdomResult = new JDOMResult();
 List<Content> resultList = new ArrayList<>();
 jdomResult.setResult(resultList);
 List<Content> result = jdomResult.getResult();
 assertSame(resultList, result);
 }

 @Test
 public void testSetResultWithDocument() {
 JDOMResult jdomResult = new JDOMResult();
 Document document = new JDOMFactory().newDocument();
 jdomResult.setDocument(document);
 Document result = jdomResult.getDocument();
 assertEquals(document, result);
 }

 @Test
 public void testRetrieveResult() {
 JDOMResult jdomResult = new JDOMResult();
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertTrue(result != null || jdomResult.getDocument() != null);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWithEmptyResultList() {
 JDOMResult jdomResult = new JDOMResult();
 jdomResult.setResult(Collections.emptyList());
 Document result = jdomResult.getDocument();
 fail("Expected RuntimeException but got " + result);
 }

 @Test
 public void testSetHandler() {
 JDOMResult jdomResult = new JDOMResult();
 ContentHandler handler = new DefaultHandler() {
 };
 jdomResult.setHandler(handler);
 assertSame(handler, jdomResult.getHandler());
 }

 @Test
 public void testSetLexicalHandler() {
 JDOMResult jdomResult = new JDOMResult();
 LexicalHandler handler = new DefaultHandler() {
 };
 jdomResult.setLexicalHandler(handler);
 assertSame(handler, jdomResult.getLexicalHandler());
 }
}