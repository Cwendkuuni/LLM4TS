package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> contentList;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setup() {
 jdomResult = new JDOMResult();
 contentList = new ArrayList<>();
 factory = new JDOMFactory();
 }

 @Test
 public void testSetResult() {
 // Set result list
 jdomResult.setResult(contentList);
 assertNull(jdomResult.getDocument());
 List<Content> result = jdomResult.getResult();
 assertEquals(contentList, result);

 // Set document
 Document doc = factory.newDocument(new Element("root"));
 jdomResult.setDocument(doc);
 assertNull(jdomResult.getResult());
 Document resultDoc = jdomResult.getDocument();
 assertEquals(doc, resultDoc);
 }

 @Test
 public void testGetDocument() {
 // Test with null result list
 assertNull(jdomResult.getDocument());

 // Test with document
 jdomResult.setDocument(factory.newDocument(new Element("root")));
 Document resultDoc = jdomResult.getDocument();
 assertEquals(jdomResult.getDocument(), resultDoc);

 // Test with result list
 contentList.add(new Element("element1"));
 jdomResult.setResult(contentList);
 Document resultDoc2 = jdomResult.getDocument();
 assertEquals(jdomResult.getDocument(), resultDoc2);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory newFactory = new JDOMFactory() {
 @Override
 public Document newDocument(String rootName, Namespace namespace) {
 return factory.newDocument(rootName, namespace);
 }
 };
 jdomResult.setFactory(newFactory);
 JDOMFactory resultFactory = jdomResult.getFactory();
 assertEquals(newFactory, resultFactory);
 }

 @Test
 public void testRetrieveResult() {
 // Test with empty result list and document
 jdomResult.setResult(Collections.emptyList());
 jdomResult.setDocument(factory.newDocument(new Element("root")));
 List<Content> result = jdomResult.getResult();
 assertTrue(result.isEmpty());

 // Test with result list
 contentList.clear();
 contentList.add(new Element("element1"));
 jdomResult.setResult(contentList);
 jdomResult.retrieveResult();
 List<Content> result2 = jdomResult.getResult();
 assertEquals(contentList, result2);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_EmptyResultList() {
 jdomResult.setResult(Collections.emptyList());
 jdomResult.retrieveResult();
 Document resultDoc = jdomResult.getDocument();
 fail("Expected RuntimeException but got " + resultDoc);
 }

 @Test
 public void testGetDocument_NullResultList() {
 jdomResult.setResult(null);
 jdomResult.retrieveResult();
 Document resultDoc = jdomResult.getDocument();
 assertNull(resultDoc);
 }
}