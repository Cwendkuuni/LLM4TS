package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

import java.util.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> resultList;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setup() {
 jdomResult = new JDOMResult();
 resultList = new ArrayList<>();
 factory = new DefaultJDOMFactory();
 }

 @Test
 public void testJDOMResultEmptyConstructor() {
 assertEquals(null, jdomResult.getResult());
 assertNull(jdomResult.getDocument());
 assertNull(jdomResult.getFactory());
 }

 @Test
 public void testSetResultMethod() {
 resultList.add(new Element("testElement"));
 jdomResult.setResult(resultList);
 List<Content> result = jdomResult.getResult();
 assertEquals(resultList, result);
 }

 @Test
 public void testGetResultMethodEmptyResultList() {
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testGetResultMethodSetDocument() {
 Document doc = factory.newDocument();
 doc.setContent(resultList);
 jdomResult.setDocument(doc);
 List<Content> result = jdomResult.getResult();
 assertEquals(resultList, result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentNullResultList() {
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 jdomResult.getDocument();
 }

 @Test
 public void testGetDocumentNullDocument() {
 jdomResult.setDocument(null);
 List<Content> result = jdomResult.getResult();
 Document doc = jdomResult.getDocument();
 assertNull(doc);
 }

 @Test
 public void testGetDocumentValidResult() {
 Document doc = factory.newDocument();
 doc.setContent(resultList);
 jdomResult.setDocument(doc);
 Document resultDoc = jdomResult.getDocument();
 assertEquals(doc, resultDoc);
 }

 @Test
 public void testSetFactoryMethod() {
 JDOMFactory newFactory = new DefaultJDOMFactory();
 jdomResult.setFactory(newFactory);
 assertEquals(newFactory, jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResultWhenBothResultListAndDocumentAreNull() {
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertNotNull(result);
 }

 @Test
 public void testRetrieveResultWhenResultListIsNotNull() {
 resultList = new ArrayList<>();
 jdomResult.setResult(resultList);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertEquals(resultList, result);
 }

 @Test
 public void testRetrieveResultWhenDocumentIsNotNull() {
 Document doc = factory.newDocument();
 doc.setContent(resultList);
 jdomResult.setDocument(doc);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertEquals(resultList, result);
 }

 @Test(expected = RuntimeException.class)
 public void testSetHandlerMethodNullHandler() {
 jdomResult.setHandler(null);
 }

 @Test(expected = RuntimeException.class)
 public void testSetLexicalHandlerMethodNullHandler() {
 jdomResult.setLexicalHandler(null);
 }

}