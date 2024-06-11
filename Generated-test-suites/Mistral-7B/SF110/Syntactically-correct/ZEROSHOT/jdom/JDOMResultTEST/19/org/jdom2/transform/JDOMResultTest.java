package org.jdom2.transform;

import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> resultList;
 private Document resultDocument;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 resultList = new ArrayList<>();
 resultDocument = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element1>Content 1</element1><element2><subElement>Content 2</subElement></element2></root>"));
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(resultList);
 assertNull(jdomResult.getDocument());
 List<Content> getResult = jdomResult.getResult();
 assertTrue(getResult.isEmpty());
 }

 @Test
 public void testSetDocument() {
 jdomResult.setDocument(resultDocument);
 Document document = jdomResult.getDocument();
 assertEquals(resultDocument, document);
 List<Content> result = jdomResult.getResult();
 assertEquals(resultDocument.getRootElement(), result.get(0));
 }

 @Test
 public void testGetResultWhenListIsNull() {
 jdomResult.setDocument(null);
 List<Content> result = jdomResult.getResult();
 assertTrue(result.isEmpty());
 }

 @Test(expected = JDOMException.class)
 public void testGetResultWhenListAndDocumentAreBothNull() {
 jdomResult.setFactory(new JDOMFactory());
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertTrue(result.isEmpty());
 jdomResult.setResult(null);
 jdomResult.getResult();
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new JDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.setDocument(resultDocument);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertEquals(3, result.size());
 }

 @Test
 public void testGetDocumentWhenListIsNotNull() {
 jdomResult.setDocument(null);
 jdomResult.setResult(resultList);
 Document document = jdomResult.getDocument();
 assertTrue(document != null);
 }

 @Test
 public void testGetDocumentWhenListIsEmpty() {
 jdomResult.setDocument(null);
 jdomResult.setResult(Collections.emptyList());
 Document document = jdomResult.getDocument();
 assertTrue(document == null);
 }

 @Test
 public void testGetDocumentWhenDocumentIsNotNull() {
 jdomResult.setDocument(resultDocument);
 Document document = jdomResult.getDocument();
 assertEquals(resultDocument, document);
 }
}