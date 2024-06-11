package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;
 private List<Content> emptyList;
 private List<Content> resultList;
 private Document document;

 @Before
 public void setUp() {
 factory = new DefaultJDOMFactory();
 result = new JDOMResult();
 emptyList = Collections.emptyList();
 }

 @Test
 public void testSetResult() {
 result.setResult(new ArrayList<>());
 List<Content> expectedResult = result.getResult();
 assertEquals(emptyList, expectedResult);
 }

 @Test
 public void testGetResultWithNullResultlist() {
 result.setResult(null);
 List<Content> expectedResult = result.getResult();
 assertSame(emptyList, expectedResult);
 }

 @Test
 public void testGetResultWithResultdoc() throws SAXException {
 SAXBuilder builder = new SAXBuilder(factory);
 document = builder.build(getClass().getResource("/test.xml"));
 result.setDocument(document);
 List<Content> expectedResult = result.getResult();
 assertEquals(document.getContent(), expectedResult);
 }

 @Test(expected = NullPointerException.class)
 public void testGetResultWithNullFactory() {
 result.setFactory(null);
 result.getDocument();
 }

 @Test
 public void testGetFactory() {
 JDOMFactory expectedFactory = factory;
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory newFactory = new DefaultJDOMFactory();
 result.setFactory(newFactory);
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(newFactory, actualFactory);
 }

 @Test
 public void testRetrieveResultWithNullResultlistAndResultdoc() {
 result.retrieveResult();
 List<Content> expectedResult = result.getResult();
 assertSame(emptyList, expectedResult);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResultWithQueriedResultlistAndNullResultdoc() {
 result.queryResult(); // simulate querying resultlist
 result.setDocument(null);
 List<Content> expectedResult = result.getResult();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testGetResultWithQueriedResultlistAndEmptyResultdoc() {
 result.queryResult(); // simulate querying resultlist
 Document emptyDocument = new Document();
 result.setDocument(emptyDocument);
 List<Content> expectedResult = result.getResult();
 assertSame(emptyList, expectedResult);
 }

 @Test
 public void testGetResultWithQueriedResultlistAndNonEmptyResultdoc() throws SAXException {
 SAXBuilder builder = new SAXBuilder(factory);
 document = builder.build(getClass().getResource("/test.xml"));
 result.queryResult(); // simulate querying resultlist
 result.setDocument(document);
 List<Content> expectedResult = new ArrayList<>();
 expectedResult.add(new Element("test", null, null));
 assertEquals(expectedResult, result.getResult());
 }

}