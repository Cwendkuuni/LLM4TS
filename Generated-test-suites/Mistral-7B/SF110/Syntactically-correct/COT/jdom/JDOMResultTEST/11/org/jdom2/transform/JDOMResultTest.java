package org.jdom2.transform;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom2.JDOMException;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.processing.SAXHandler;
import org.junit.Before;
import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> resultList;
 private Document document;
 private SAXHandler saxHandler;

 @Before
 public void setup() {
 jdomResult = new JDOMResult();
 resultList = Arrays.asList(new Element("element1"), new Element("element2"));
 document = new SAXBuilder().build(getClass().getResourceAsStream("/test.xml")).getDocument();
 saxHandler = new SAXHandler() {
 @Override
 public List<Content> getResult() {
 return Collections.emptyList();
 }
 };
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(resultList);
 List<Content> getResult = jdomResult.getResult();
 assertEquals(resultList, getResult);
 }

 @Test
 public void testGetResultWhenResultlistIsNotNull() {
 jdomResult.setResult(resultList);
 List<Content> getResult = jdomResult.getResult();
 assertEquals(resultList, getResult);
 }

 @Test
 public void testGetResultWhenResultdocIsNotNull() throws JDOMException {
 jdomResult.setDocument(document);
 List<Content> getResult = jdomResult.getResult();
 assertNotNull(getResult);
 }

 @Test(expected = NullPointerException.class)
 public void testGetResultWhenBothResultlistAndResultdocAreNull() {
 jdomResult.retrieveResult();
 List<Content> getResult = jdomResult.getResult();
 fail("Expected NullPointerException");
 }

 @Test
 public void testSetDocument() {
 jdomResult.setDocument(document);
 Document getDocument = jdomResult.getDocument();
 assertEquals(document, getDocument);
 }

 @Test
 public void testGetDocumentWhenResultlistIsNotNull() throws JDOMException {
 jdomResult.setResult(resultList);
 Document getDocument = jdomResult.getDocument();
 assertNotNull(getDocument);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDocumentWhenResultlistIsEmpty() throws JDOMException {
 jdomResult.setResult(Collections.emptyList());
 Document getDocument = jdomResult.getDocument();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory getFactory = jdomResult.getFactory();
 assertEquals(factory, getFactory);
 }

 @Test
 public void testGetFactoryWhenFactoryIsNotNull() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory getFactory = jdomResult.getFactory();
 assertEquals(factory, getFactory);
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.retrieveResult();
 assertFalse(jdomResult.queried);
 }

 @Test
 public void testSetHandler() {
 // Not testing the functionality of setHandler as it is not used in this class
 }

 @Test
 public void testSetLexicalHandler() {
 // Not testing the functionality of setLexicalHandler as it is not used in this class
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWhenHandlerIsNull() {
 new JDOMResult(null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testConstructorWhenLexicalHandlerIsNull() {
 new JDOMResult(null, null);
 }

 @Test
 public void testDocumentBuilder() throws Exception {
 DocumentBuilder documentBuilder = new JDOMResult().getDocumentBuilder();
 List<Content> result = documentBuilder.getResult();
 assertNotNull(result);
 }

 @Test
 public void testFragmentHandler() {
 FragmentHandler fragmentHandler = new JDOMResult().new FragmentHandler(null);
 List<Content> result = fragmentHandler.getResult();
 assertNotNull(result);
 }

}