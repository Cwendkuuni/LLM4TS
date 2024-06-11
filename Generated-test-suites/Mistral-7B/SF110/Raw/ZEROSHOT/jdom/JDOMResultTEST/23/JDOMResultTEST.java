package org.jdom2.transform;

import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> resultList;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 resultList = new ArrayList<>();
 factory = new DefaultJDOMFactory();
 }

 @Test
 public void testJDOMResult_DefaultConstructor() {
 assertEquals(Collections.emptyList(), jdomResult.getResult());
 assertNull(jdomResult.getDocument());
 assertNull(jdomResult.getFactory());
 }

 @Test
 public void testSetResult() {
 resultList.add(new Element("element1"));
 jdomResult.setResult(resultList);
 assertEquals(resultList, jdomResult.getResult());
 }

 @Test
 public void testGetResult_EmptyResultList() {
 assertEquals(Collections.emptyList(), jdomResult.getResult());
 }

 @Test
 public void testGetResult_NonEmptyResultList() throws JDOMException, SAXException {
 SAXBuilder builder = new SAXBuilder();
 document = builder.build(getClass().getResourceAsStream("/test.xml"));
 jdomResult.setResult(document.getContent());
 assertEquals(document.getContent(), jdomResult.getResult());
 }

 @Test
 public void testSetDocument() {
 Document testDocument = new Document();
 jdomResult.setDocument(testDocument);
 assertEquals(testDocument, jdomResult.getDocument());
 }

 @Test
 public void testGetDocument_EmptyResultList() {
 assertNull(jdomResult.getDocument());
 }

 @Test
 public void testGetDocument_NonEmptyResultList() throws JDOMException, SAXException {
 SAXBuilder builder = new SAXBuilder();
 document = builder.build(getClass().getResourceAsStream("/test.xml"));
 jdomResult.setResult(document.getContent());
 Document testDocument = jdomResult.getDocument();
 assertSame(document, testDocument);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory testFactory = new JDOMFactory() {
 @Override
 public Document newDocument() {
 return new Document();
 }
 };
 jdomResult.setFactory(testFactory);
 assertSame(testFactory, jdomResult.getFactory());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFactory_NullFactory() {
 jdomResult.setFactory(null);
 }

 @Test
 public void testGetFactory_DefaultFactory() {
 assertSame(factory, jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.retrieveResult();
 assertTrue(jdomResult.isQueried());
 }
}