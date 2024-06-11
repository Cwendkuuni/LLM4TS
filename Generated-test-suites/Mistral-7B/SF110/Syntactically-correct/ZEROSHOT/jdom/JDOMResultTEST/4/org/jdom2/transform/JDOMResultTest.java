package org.jdom2.transform;

import org.jdom2.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> resultList;
 private Document resultDocument;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 resultList = new ArrayList<>();
 resultDocument = new JDOMFactory().newDocument();
 factory = new JDOMFactory();
 }

 @Test
 public void testSetAndGetResult() {
 // set result and get result list
 jdomResult.setResult(resultList);
 List<Content> getResultList = jdomResult.getResult();
 assertEquals(resultList, getResultList);

 // set result document and get list
 jdomResult.setDocument(resultDocument);
 List<Content> getDocumentList = jdomResult.getResult();
 assertEquals(resultDocument.getContent(), getDocumentList);
 }

 @Test
 public void testSetAndGetDocument() {
 // set document and get list
 jdomResult.setDocument(resultDocument);
 Document getDocument = jdomResult.getDocument();
 assertEquals(resultDocument, getDocument);

 // set list and get document
 jdomResult.setResult(resultList);
 Document getResultDocument = jdomResult.getDocument();
 assertEquals(resultDocument, getResultDocument);
 }

 @Test
 public void testRetrieveResult() {
 // test retrieving result from list
 jdomResult.setResult(resultList);
 resultList.clear();
 resultList.add(new Element("element1"));
 jdomResult.retrieveResult();
 List<Content> getResult = jdomResult.getResult();
 assertEquals(1, getResult.size());
 assertEquals("element1", getResult.get(0).getName());

 // test retrieving result from document
 Document document = new JDOMFactory().newDocument();
 document.getRootElement().addContent(new Element("element1"));
 jdomResult.setDocument(document);
 resultDocument.setContent(new Element("root"));
 jdomResult.retrieveResult();
 Document getDocument = jdomResult.getDocument();
 assertEquals("element1", getDocument.getRootElement().getChildren().get(0).getName());
 }

 @Test
 public void testGetFactory() {
 JDOMFactory expectedFactory = new JDOMFactory();
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test
 public void testSettingFactory() {
 JDOMFactory newFactory = new JDOMFactory();
 jdomResult.setFactory(newFactory);
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals(newFactory, actualFactory);
 }
}