package org.jdom2.transform;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.filter.Filter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;
 private SAXBuilder saxBuilder;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 factory = new JDOMFactory();
 saxBuilder = new SAXBuilder(factory);
 }

 @Test
 public void testEmptyConstructor() {
 assertEquals(null, jdomResult.getResult());
 }

 @Test
 public void testSetResult() {
 List<Content> result = new ArrayList<>();
 result.add(new Element("testElement"));
 jdomResult.setResult(result);
 List<Content> expectedResult = jdomResult.getResult();
 assertEquals(result, expectedResult);
 }

 @Test
 public void testGetDocument_EmptyResultList() {
 Document expectedDocument = null;
 Document actualDocument = jdomResult.getDocument();
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testGetDocument_ResultDocNotEmpty() throws SAXException {
 String xml = "<root><element>test</element></root>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 saxBuilder.setEntityResolver(new DefaultEntityResolver());
 Document doc = saxBuilder.build(inputSource);
 jdomResult.setDocument(doc);
 Document expectedDocument = doc;
 Document actualDocument = jdomResult.getDocument();
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testGetDocument_ResultListNotEmpty() throws SAXException {
 String xml = "<root><element>test1</element><element>test2</element></root>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 saxBuilder.setEntityResolver(new DefaultEntityResolver());
 Document doc = saxBuilder.build(inputSource);
 List<Content> result = new ArrayList<>();
 result.add(new Element("root"));
 result.addAll(doc.getContent());
 jdomResult.setResult(result);
 Document expectedDocument = doc;
 Document actualDocument = jdomResult.getDocument();
 assertEquals(expectedDocument, actualDocument);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_NullFactory() {
 jdomResult.setFactory(null);
 jdomResult.getDocument();
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.setResult(null);
 jdomResult.retrieveResult();
 List<Content> expectedResult = jdomResult.getResult();
 assertEquals(null, expectedResult);
 jdomResult.setDocument(new Document("dummyDocument"));
 List<Content> expectedResultDocument = jdomResult.getResult();
 assertEquals(null, expectedResultDocument);
 jdomResult.setResult(new ArrayList<>());
 jdomResult.retrieveResult();
 List<Content> actualResult = jdomResult.getResult();
 assertEquals(null, actualResult);
 jdomResult.setDocument(new Document("dummyDocument"));
 jdomResult.setResult(null);
 List<Content> expectedResultDocumentWithResultList = jdomResult.getResult();
 List<Content> actualResultDocumentWithResultList = jdomResult.getResult();
 assertEquals(expectedResultDocumentWithResultList.size(), actualResultDocumentWithResultList.size());
 }

 @Test(expected = RuntimeException.class)
 public void testSetFactory_NullFactory() {
 jdomResult.setFactory(null);
 }

 @Test
 public void testGetFactory_NullFactory() {
 JDOMFactory expectedFactory = null;
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test
 public void testGetFactory_DefaultFactory() {
 JDOMFactory expectedFactory = new DefaultJDOMFactory();JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test
 public void testGetFactory_CustomFactory() {
 JDOMFactory customFactory = new JDOMFactory() {
 @Override
 public Document document(final Filter filter) throws SAXException {
 return null;
 }
 };
 jdomResult.setFactory(customFactory);
 JDOMFactory expectedFactory = customFactory;
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }
}