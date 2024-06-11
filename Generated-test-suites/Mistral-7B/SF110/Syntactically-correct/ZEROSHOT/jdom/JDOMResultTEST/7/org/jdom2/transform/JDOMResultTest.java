package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResultList;
 private Document expectedResultDocument;
 private JDOMFactory jdomFactory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 jdomFactory = new JDOMFactory();
 }

 @Test
 public void testSetResultAndGetResult() {
 List<Content> resultList = new ArrayList<>();
 resultList.add(new Element("element1"));
 resultList.add(new Text("content1"));
 jdomResult.setResult(resultList);
 List<Content> actualResult = jdomResult.getResult();
 assertEquals(resultList, actualResult);
 }

 @Test
 public void testSetDocumentAndGetDocument() throws SAXException {
 Document document = jdomFactory.newDocument();
 document.setContent(new Element("root").addContent(new Text("content")));
 jdomResult.setDocument(document);
 Document actualResult = jdomResult.getDocument();
 assertEquals(document, actualResult);
 }

 @Test
 public void testEmptyResult() {
 jdomResult.setDocument(null);
 jdomResult.retrieveResult();
 List<Content> actualResult = jdomResult.getResult();
 assertTrue(actualResult.isEmpty());
 }

 @Test
 public void testRetrieveResultFromDocument() throws SAXException {
 Document document = jdomFactory.newDocument();
 document.setContent(new Element("root").addContent(new Text("content1")).addContent(new Element("element2").addContent(new Text("content2"))));
 jdomResult.setDocument(document);
 jdomResult.retrieveResult();
 List<Content> actualResult = jdomResult.getResult();
 List<Content> expectedResult = new ArrayList<>();
 expectedResult.add(new Text("content1"));
 expectedResult.add(new Element("element2"));
 expectedResult.add(new Text("content2"));
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testEmptyGetFactory() {
 jdomResult.setFactory(null);
 assertNull(jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResult() throws SAXException {
 DocumentBuilder documentBuilder = new DocumentBuilder();
 jdomResult.setHandler(documentBuilder);
 String xmlString = "<root><element>content</element></root>";
 SAXBuilder saxBuilder = new SAXBuilder();
 InputSource inputSource = new InputSource(new StringReader(xmlString));
 saxBuilder.build(inputSource, jdomResult);
 List<Content> actualResult = jdomResult.getResult();
 List<Content> expectedResult = new ArrayList<>();
 expectedResult.add(new Text("content"));
 expectedResult.add(new Element("element"));
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenResultIsNull() {
 jdomResult.setResult(null);
 jdomResult.retrieveResult();
 Document actualResult = jdomResult.getDocument();
 fail("Expected a RuntimeException but got " + actualResult);
 }
}