package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult result;
 private Document document;
 private List<Content> expectedResultList;

 @Test
 public void testConstructor() {
 JDOMResult actualResult = new JDOMResult();
 assertNull(actualResult.getResult());
 assertNull(actualResult.getDocument());
 assertNull(actualResult.getFactory());
 }

 @Test
 public void testSetResult() {
 result = new JDOMResult();
 expectedResultList = new ArrayList<>();
 result.setResult(expectedResultList);
 assertEquals(expectedResultList, result.getResult());
 }

 @Test
 public void testGetResult_EmptyList() {
 result = new JDOMResult();
 result.retrieveResult();
 assertEquals(Collections.emptyList(), result.getResult());
 }

 @Test
 public void testGetResult_Document() {
 result = new JDOMResult();
 document = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"));
 result.setDocument(document);
 List<Content> expectedResultList = document.getRootElement().getContent();
 result.retrieveResult();
 assertEquals(expectedResultList, result.getResult());
 }

 @Test(expected = NullPointerException.class)
 public void testGetResult_NullResult() {
 result = new JDOMResult();
 result.retrieveResult();
 result.setResult(null);
 result.getResult();
 }

 @Test
 public void testSetDocument() {
 result = new JDOMResult();
 Document actualDocument = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"));
 result.setDocument(actualDocument);
 Document expectedDocument = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"));
 assertEquals(expectedDocument, result.getDocument());
 }

 @Test(expected = NullPointerException.class)
 public void testSetDocument_NullDocument() {
 Document nullDocument = null;
 result = new JDOMResult();
 result.setDocument(nullDocument);
 }

 @Test
 public void testGetFactory() {
 result = new JDOMResult();
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 assertEquals(factory, result.getFactory());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFactory_NullFactory() {
 result = new JDOMResult();
 JDOMFactory nullFactory = null;
 result.setFactory(nullFactory);
 result.getFactory();
 }

 @Test
 public void testRetrieveResult_BothResultListAndDocumentSet() {
 result = new JDOMResult();
 Document document = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<child element=\"value\"/>\n</root>"));
 List<Content> expectedResultList = document.getRootElement().getContent();
 result.setDocument(document);
 result.setResult(expectedResultList);
 result.retrieveResult();
 List<Content> actualResultList = result.getResult();
 assertEquals(expectedResultList, actualResultList);
 }

 @Test
 public void testRetrieveResult_ResultListOnly() {
 result = new JDOMResult();
 List<Content> resultList = new ArrayList<>();
 result.setResult(resultList);
 result.retrieveResult();
 List<Content> actualResultList = result.getResult();
 assertEquals(resultList, actualResultList);
 }

 @Test
 public void testRetrieveResult_DocumentOnly() {
 result = new JDOMResult();
 Document document = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<child element=\"value\"/>\n</root>"));
 result.setDocument(document);
 result.retrieveResult();
 List<Content> actualResultList = result.getResult();
 List<Content> expectedResultList = document.getContent();
 assertEquals(expectedResultList, actualResultList);
 }

 @Test
 public void testGetFactory_DefaultFactory() {
 result = new JDOMResult();
 result.retrieveResult();
 DefaultJDOMFactory expectedFactory = new DefaultJDOMFactory();
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test
 public void testSetFactory_NullFactory() {
 result = new JDOMResult();
 JDOMFactory nullFactory = null;
 result.setFactory(nullFactory);
 JDOMFactory actualFactory = result.getFactory();
 assertNull(actualFactory);
 }

 @Test
 public void testSetFactory_CustomFactory() {
 result = new JDOMResult();
 JDOMFactory customFactory = new CustomJDOMFactory();
 result.setFactory(customFactory);
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(customFactory, actualFactory);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_NullResult() {
 result = new JDOMResult();
 result.retrieveResult();
 result.setResult(null);
 Document actualDocument = result.getDocument();
 assertNull(actualDocument);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_ResultListAndDocumentSet() {
 result = new JDOMResult();
 Document document = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<child element=\"value\"/>\n</root>"));
 List<Content> expectedResultList = document.getRootElement().getContent();
 result.setDocument(document);
 result.setResult(expectedResultList);
 Document actualDocument = result.getDocument();
 assertNotNull(actualDocument);
 result.retrieveResult(); // This should clear the result
 List<Content> actualResultList = result.getResult();
 assertEquals(expectedResultList, actualResultList);
 actualDocument = result.getDocument();
 assertTrue(actualDocument.getContent().isEmpty());
 }

 @Test
 public void testGetDocument_ResultListOnly() {
 result = new JDOMResult();
 List<Content> resultList = new ArrayList<>();
 result.setResult(resultList);
 result.retrieveResult();
 Document actualDocument = result.getDocument();
 assertNull(actualDocument);
 }
}