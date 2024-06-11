package org.jdom2.transform;

import static org.junit. Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private Document document;
 private List<Content> resultList;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 document = new Document();
 resultList = new ArrayList<>();
 factory = new DefaultJDOMFactory();
 }

 @Test
 public void testEmptyConstructor() {
 JDOMResult result = new JDOMResult();
 assertNull(result.getResult());
 assertNull(result.getDocument());
 assertNull(result.getFactory());
 }

 @Test
 public void testSetResult() {
 resultList.add(new Element("test", null, null));
 jdomResult.setResult(resultList);
 List<Content> result = jdomResult.getResult();
 assertEquals(resultList, result);
 }

 @Test
 public void testGetResultWhenResultDocIsNotNullAndQueriedIsFalse() {
 resultList.clear();
 document.setContent(resultList);
 jdomResult.setDocument(document);
 List<Content> result = jdomResult.getResult();
 assertEquals(resultList, result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenResultDocIsNotNullAndQueriedIsTrue() {
 resultList.clear();
 document.setContent(resultList);
 jdomResult.setDocument(document);
 jdomResult.getDocument();
 }

 @Test
 public void testSetDocument() {
 jdomResult.setDocument(document);
 Document doc = jdomResult.getDocument();
 assertEquals(document, doc);
 }

 @Test
 public void testGetDocumentWhenResultListIsNotNullAndQueriedIsFalse() {
 resultList.clear();
 jdomResult.setResult(resultList);
 Document doc = jdomResult.getDocument();
 assertNull(doc);
 }

 @Test(expected = NullPointerException.class)
 public void testSetFactoryWhenFactoryIsNull() {
 jdomResult.setFactory(null);
 }

 @Test
 public void testGetFactory() {
 jdomResult.setFactory(factory);
 JDOMFactory result = jdomResult.getFactory();
 assertEquals(factory, result);
 }

 @Test
 public void testRetrieveResultWhenResultListIsNotNull() {
 resultList.clear();
 jdomResult.setResult(resultList);
 jdomResult.retrieveResult();
 assertFalse(jdomResult.queried);
 }

 @Test
 public void testRetrieveResultWhenResultDocIsNotNull() {
 resultList.clear();
 document.setContent(resultList);
 jdomResult.setDocument(document);
 jdomResult.retrieveResult();
 assertFalse(jdomResult.queried);
 }

 @Test
 public void testSetHandler() {
 // Not testing the functionality of setHandler since it is empty in the given class
 }

 @Test
 public void testSetLexicalHandler() {
 // Not testing the functionality of setLexicalHandler since it is empty in the given class
 }

 @Test
 public void testGetDocumentWhenResultListIsEmptyAndQueriedIsFalse() {
 jdomResult.setResult(Collections.emptyList());
 Document doc = jdomResult.getDocument();
 assertNull(doc);
 }

 @Test
 public void testGetDocumentWhenResultListIsEmptyAndQueriedIsTrue() {
 jdomResult.setResult(Collections.emptyList());
 jdomResult.queried = true;
 Document doc = jdomResult.getDocument();
 assertNull(doc);
 }

 @Test
 public void testSetFactoryAndGetDocument() {
 resultList.clear();
 document.setContent(resultList);
 jdomResult.setDocument(document);
 jdomResult.setFactory(factory);
 Document doc = jdomResult.getDocument();
 assertEquals(document, doc);
 }

}