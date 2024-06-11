package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult result;
 private List<Content> expectedResult;
 private Document doc;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 result = new JDOMResult();
 factory = new JDOMFactory();
 }

 @Test
 public void testEmptyResult() {
 result.setResult(null);
 List<Content> resultList = result.getResult();
 assertEquals(Collections.emptyList(), resultList);
 }

 @Test
 public void testSetResultAndGetDocument() throws SAXException {
 expectedResult = new ArrayList<>();
 expectedResult.add(new Element("element1", null, null));
 expectedResult.add(new Text("content1"));

 SAXBuilder builder = new SAXBuilder(factory);
 doc = builder.build(getClass().getResource("/test.xml"));
 result.setResult(expectedResult);

 List<Content> resultList = result.getResult();
 Document document = result.getDocument();

 assertEquals(expectedResult, resultList);
 assertEquals(doc, document);
 }

 @Test
 public void testSetDocumentAndGetResult() throws SAXException {
 expectedResult = new ArrayList<>();
 expectedResult.add(new Element("element1", null, null));
 expectedResult.add(new Text("content1"));

 SAXBuilder builder = new SAXBuilder(factory);
 doc = builder.build(getClass().getResource("/test.xml"));
 result.setDocument(doc);

 List<Content> resultList = result.getResult();
 assertEquals(expectedResult, resultList);
 }

 @Test
 public void testGetFactory() {
 assertEquals(factory, result.getFactory());
 }

 @Test
 public void testSetFactory() {
 JDOMFactory newFactory = new JDOMFactory() {
 @Override
 public Document newDocument() {
 return null;
 }
 };
 result.setFactory(newFactory);
 assertEquals(newFactory, result.getFactory());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDocumentAfterQueried() {
 result.setDocument(doc);
 result.getResult();
 result.getDocument();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResultAfterSetDocument() {
 result.setDocument(doc);
 List<Content> resultList = result.getResult();
 result.setDocument(null);
 resultList = result.getResult();
 }
}