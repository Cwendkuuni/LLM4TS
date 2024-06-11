package org.jdom2.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;
 private Document document;
 private List<Content> expectedResult;

 @Test
 public void testEmptyResult() {
 jdomResult = new JDOMResult();
 assertEquals(Collections.emptyList(), jdomResult.getResult());
 }

 @Test
 public void testSetResultList() {
 jdomResult = new JDOMResult();
 expectedResult = Arrays.asList(new Element("foo"), new Element("bar"));
 jdomResult.setResult(expectedResult);
 assertEquals(expectedResult, jdomResult.getResult());
 }

 @Test
 public void testSetDocument() {
 jdomResult = new JDOMResult();
 document = new Document("root");
 jdomResult.setDocument(document);
 assertEquals(Arrays.asList(document.getRootElement()), jdomResult.getResult());
 }

 @Test
 public void testGetDocument_NoResultListOrDocument() {
 jdomResult = new JDOMResult();
 assertNull(jdomResult.getDocument());
 }

 @Test
 public void testGetDocument_ResultList() {
 jdomResult = new JDOMResult();
 expectedResult = Arrays.asList(new Element("foo"), new Element("bar"));
 jdomResult.setResult(expectedResult);
 Document doc = jdomResult.getDocument();
 assertNotNull(doc);
 assertEquals(expectedResult, doc.getContent());
 }

 @Test
 public void testGetDocument_Document() {
 jdomResult = new JDOMResult();
 document = new Document("root");
 jdomResult.setDocument(document);
 Document doc = jdomResult.getDocument();
 assertNotNull(doc);
 assertEquals(document, doc);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new JDOMFactory();
 jdomResult = new JDOMResult();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResult() {
 jdomResult = new JDOMResult();
 DocumentBuilder documentBuilder = new DocumentBuilder();
 SAXBuilder saxBuilder = new SAXBuilder(documentBuilder);
 String xml = "<root><foo/><bar/></root>";
 try {
 saxBuilder.build(new StringReader(xml));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 jdomResult.setHandler(documentBuilder);
 jdomResult.handleStartDocument();
 jdomResult.handleEndDocument();
 List<Content> result = jdomResult.getResult();
 assertNotNull(result);
 assertEquals(2, result.size());
 }

 @Test
 public void testQueriedFlag() {
 jdomResult = new JDOMResult();
 List<Content> resultList = Arrays.asList(new Element("foo"), new Element("bar"));
 jdomResult.setResult(resultList);
 List<Content> result = jdomResult.getResult();
 assertNotNull(result);
 assertFalse(jdomResult.isQueried());
 result = jdomResult.getResult();
 assertTrue(jdomResult.isQueried());
 }

}