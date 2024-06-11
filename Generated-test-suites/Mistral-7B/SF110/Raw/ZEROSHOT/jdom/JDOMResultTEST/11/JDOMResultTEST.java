package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResultList;
 private Document expectedDocument;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 expectedResultList = new ArrayList<>();
 expectedResultList.add(new Element("testElement1"));
 expectedResultList.add(new Text("Test content"));
 expectedDocument = new SAXBuilder().build(getClass().getResourceAsStream("/test.xml")).getDocument();
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(expectedResultList);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResultList, result);
 }

 @Test
 public void testSetDocument() {
 jdomResult.setDocument(expectedDocument);
 Document result = jdomResult.getDocument();
 assertEquals(expectedDocument, result);
 }

 @Test
 public void testGetResultWhenResultlistIsNotNull() {
 jdomResult.setResult(expectedResultList);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResultList, result);
 }

 @Test
 public void testGetResultWhenResultdocIsNotNull() {
 jdomResult.setDocument(expectedDocument);
 Document result = jdomResult.getResult();
 assertEquals(expectedDocument, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResultWhenBothResultlistAndResultdocAreNotNull() {
 jdomResult.setResult(expectedResultList);
 jdomResult.setDocument(expectedDocument);
 jdomResult.getResult();
 }

 @Test
 public void testRetrieveResultWhenResultlistIsNullAndResultdocIsNotNull() {
 jdomResult.setDocument(expectedDocument);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedDocument.getContent(), result);
 }

 @Test
 public void testRetrieveResultWhenResultdocIsNullAndResultlistIsNotNull() {
 jdomResult.setResult(expectedResultList);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResultList, result);
 }

 @Test
 public void testRetrieveResultWhenResultlistAndResultdocAreBothNotNull() {
 jdomResult.setDocument(expectedDocument);
 jdomResult.setResult(expectedResultList);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedDocument.getContent(), result);
 }
}