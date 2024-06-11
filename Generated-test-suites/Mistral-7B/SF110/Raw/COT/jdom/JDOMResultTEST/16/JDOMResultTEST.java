package org.jdom2.transform.test;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;

public class JDOMResultTest {

 private JDOMResult result;
 private Document inputDocument;
 private Document expectedDocument;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 result = new JDOMResult();
 factory = new JDOMFactory();
 }

 @Test
 public void testJDOMResultConstructor() {
 // Test empty constructor
 result = new JDOMResult();
 assertNull(result.getResult());
 assertNull(result.getDocument());
 assertFalse(result.isQueried());
 assertNull(result.getFactory());
 }

 @Test
 public void testSetResult() {
 // Test setting result list
 List<Content> list = new ArrayList<>();
 list.add(new Element("element1"));
 result.setResult(list);
 assertEquals(list, result.getResult());
 assertNull(result.getDocument());
 assertFalse(result.isQueried());
 assertSame(factory, result.getFactory());

 // Test setting document
 inputDocument = new Document("test.xml");
 result.setDocument(inputDocument);
 assertEquals(Collections.emptyList(), result.getResult());
 assertSame(inputDocument, result.getDocument());
 assertFalse(result.isQueried());
 assertSame(factory, result.getFactory());
 }

 @Test
 public void testGetResult() throws SAXException {
 // Test getting empty result list
 result.retrieveResult();
 assertSame(Collections.emptyList(), result.getResult());
 assertNull(result.getDocument());
 assertTrue(result.isQueried());

 // Test getting result from list
 list = new ArrayList<>();
 list.add(new Element("element1"));
 result.setResult(list);
 result.retrieveResult();
 assertEquals(list, result.getResult());
 assertNull(result.getDocument());
 assertTrue(result.isQueried());

 // Test getting result from document
 inputDocument = new Document("test.xml");
 SAXBuilder builder = new SAXBuilder(factory);
 builder.setValidation(false);
 result.setDocument(builder.build(new InputSource("test.xml")));
 result.retrieveResult();
 expectedDocument = new Document("test.xml");
 assertEquals(expectedDocument.getContent(), result.getResult());
 assertSame(expectedDocument, result.getDocument());
 assertTrue(result.isQueried());
 }

 @Test
 public void testSetDocumentWithNullDocument() {
 result.setDocument(null);
 assertSame(Collections.emptyList(), result.getResult());
 assertNull(result.getDocument());
 assertFalse(result.isQueried());
 assertSame(factory, result.getFactory());
 }

 @Test(expected = RuntimeException.class)
 public void testSetDocumentWithInvalidDocument() {
 result.setDocument(new Document());
 result.retrieveResult();
 fail("Expected RuntimeException but got no exception");
 }

 @Test
 public void testSetFactory() {
 JDOMFactory newFactory = new DefaultJDOMFactory();
 result.setFactory(newFactory);
 assertSame(newFactory, result.getFactory());
 }

 @Test
 public void testGetFactory() {
 assertSame(factory, result.getFactory());
 }

 @Test
 public void testRetrieveResultWithNullResult() {
 result.setResult(null);
 result.retrieveResult();
 assertSame(Collections.emptyList(), result.getResult());
 assertNull(result.getDocument());
 assertTrue(result.isQueried());
 }

 @Test
 public void testRetrieveResultWithNullDocument() {
 result.setDocument(null);
 result.retrieveResult();
 assertEquals(Collections.emptyList(), result.getResult());
 assertNull(result.getDocument());
 assertTrue(result.isQueried());
 }

 @Test
 public void testRetrieveResultWithEmptyDocument() throws SAXException {
 InputSource inputSource = new InputSource(new StringReader(""));
 result.setDocument(new SAXBuilder(factory).build(inputSource));
 result.retrieveResult();
 assertEquals(Collections.emptyList(), result.getResult());
 assertNull(result.getDocument());
 assertTrue(result.isQueried());
 }

 @Test
 public void testRetrieveResultWithInvalidDocument() throws SAXException {
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root><element/>"));
 result.setDocument(new SAXBuilder(factory).build(inputSource));
 result.retrieveResult();
 assertEquals(Collections.emptyList(), result.getResult());
 assertNull(result.getDocument());
 assertTrue(result.isQueried());
 }

 @Test
 public void testRetrieveResultWithValidDocument() throws SAXException {
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root><element1><subElement/><anotherElement/></element1></root>"));
 result.setDocument(new SAXBuilder(factory).build(inputSource));
 result.retrieveResult();
 expectedDocument = new Document("<?xml version=\"1.0\"?>\n<root><element1><subElement/><anotherElement/></element1></root>");
 assertEquals(expectedDocument.getContent(), result.getResult());
 assertSame(expectedDocument, result.getDocument());
 assertTrue(result.isQueried());
 }
}