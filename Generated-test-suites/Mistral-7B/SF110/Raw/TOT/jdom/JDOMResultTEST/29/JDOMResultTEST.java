package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 factory = new JDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testSetResult() {
 List<Content> expectedResult = new ArrayList<>();
 Content element = new Element("testElement", null, null);
 expectedResult.add(element);
 result.setResult(expectedResult);
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGetDocumentWhenResultIsSet() {
 List<Content> expectedResult = new ArrayList<>();
 Content element = new Element("testElement", null, null);
 expectedResult.add(element);
 result.setResult(expectedResult);
 Document actualDocument = result.getDocument();
 assertEquals(expectedResult.get(0), actualDocument.getRootElement());
 }

 @Test
 public void testGetDocumentWhenResultIsNotSet() {
 result.setDocument(new Document());
 Document actualDocument = result.getDocument();
 assertNotNull(actualDocument);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenNeitherResultNorDocumentIsSet() {
 result.retrieveResult(); // trigger retrieveResult() in getDocument()
 }

 @Test
 public void testSetDocument() {
 Document document = new Document();
 result.setDocument(document);
 Document actualDocument = result.getDocument();
 assertEquals(document, actualDocument);
 }

 @Test
 public void testGetResultWhenDocumentIsSet() {
 Document document = new Document();
 result.setDocument(document);
 List<Content> actualResult = result.getResult();
 assertNotNull(actualResult);
 assertEquals(document.getContentSize(), actualResult.size());
 }

 @Test
 public void testRetrieveResult() {
 Document document = new Document();
 DocumentBuilder builder = new DocumentBuilder();
 result.setHandler(builder);
 SAXBuilder saxBuilder = new SAXBuilder();
 saxBuilder.build(getClass().getResource("/test.xml"), builder);
 result.setDocument(document);
 List<Content> actualResult = result.getResult();
 assertEquals(document.getContentSize(), actualResult.size());
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new JDOMFactory();
 result.setFactory(factory);
 assertEquals(factory, result.getFactory());
 }

 @Test
 public void testGetFactory() {
 JDOMFactory factory = new JDOMFactory();
 result.setFactory(factory);
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(factory, actualFactory);
 }
}