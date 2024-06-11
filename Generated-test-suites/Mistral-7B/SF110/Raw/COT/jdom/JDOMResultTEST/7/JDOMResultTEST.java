package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMBuilderFactory;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private Document inputDocument;
 private List<Content> expectedResultList;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 inputDocument = new JDOMBuilderFactory().buildDocument("<root><child/><anotherChild/></root>");
 }

 @Test
 public void testSetResult() {
 List<Content> resultList = new ArrayList<>();
 resultList.add(inputDocument.getRootElement());
 jdomResult.setResult(resultList);
 List<Content> actualResultList = jdomResult.getResult();
 assertEquals("Result list should not be empty", resultList, actualResultList);
 }

 @Test
 public void testGetResultEmpty() {
 List<Content> actualResultList = jdomResult.getResult();
 assertEquals("Result should be empty", Collections.emptyList(), actualResultList);
 }

 @Test
 public void testGetResultDocument() {
 jdomResult.setDocument(inputDocument);
 Document actualDocument = jdomResult.getDocument();
 assertEquals("Document should not be null", inputDocument, actualDocument);
 }

 @Test(expected = RuntimeException.class)
 public void testGetResultDocumentNull() {
 jdomResult.setResult(null);
 Document actualDocument = jdomResult.getDocument();
 fail("Document should be null, but it's not");
 }

 @Test
 public void testGetResultList() {
 List<Content> actualResultList = jdomResult.getResult();
 if (jdomResult.getDocument() != null) {
 JDOMResult newJDOMResult = new JDOMResult();
 newJDOMResult.setDocument(jdomResult.getDocument());
 List<Content> expectedResultList = newJDOMResult.getResult();
 assertEquals("Results should be equal", expectedResultList, actualResultList);
 } else {
 expectedResultList = jdomResult.getResult();
 }
 assertEquals("Results should match", expectedResultList, actualResultList);
 }

 @Test
 public void testSetFactoryNull() {
 jdomResult.setFactory(null);
 assertNull("Factory should be null", jdomResult.getFactory());
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals("Factories should be equal", factory, actualFactory);
 }

 @Test(expected = RuntimeException.class)
 public void testRetrieveResultNoResult() {
 jdomResult.retrieveResult();
 fail("Should throw exception as resultlist and resultdoc are null");
 }

 @Test(expected = RuntimeException.class)
 public void testSetResultNull() {
 jdomResult.setResult(null);
 fail("Should throw exception as resultlist can't be null");
 }

 @Test
 public void testGetResultWithDocument() {
 jdomResult.setDocument(inputDocument);
 Document actualDocument = jdomResult.getDocument();
 List<Content> actualResultList = jdomResult.getResult();
 List<Content> expectedResultList = actualDocument.getContent();
 assertEquals("Document and resultlist should match", expectedResultList, actualResultList);
 }
}