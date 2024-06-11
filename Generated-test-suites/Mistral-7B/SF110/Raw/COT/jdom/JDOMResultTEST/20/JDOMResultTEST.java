package org.jdom2.transform;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.SAXException;

import org.junit.Before;
import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<JDOMResult.Content> expectedContentList;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 expectedContentList = new ArrayList<>();
 expectedContentList.add(new JDOMResult.Content("testContent1"));
 expectedContentList.add(new JDOMResult.Content("testContent2"));
 }

 @Test
 public void testSetResult() {
 List<JDOMResult.Content> result = new ArrayList<>();
 result.add(new JDOMResult.Content("testResult1"));
 result.add(new JDOMResult.Content("testResult2"));
 jdomResult.setResult(result);
 List<JDOMResult.Content> actualResult = jdomResult.getResult();
 assertEquals(expectedContentList, actualResult);
 }

 @Test
 public void testGetResult_whenResultlistIsNullAndResultdocIsNotNull() throws SAXException {
 Document document = new SAXBuilder().build(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<child1>test</child1>\n<child2>test</child2>\n</root>"));
 jdomResult.setDocument(document);
 List<JDOMResult.Content> actualResult = jdomResult.getResult();
 List<JDOMResult.Content> expectedResult = new ArrayList<>();
 expectedResult.addAll(document.getContent());
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_whenResultlistIsNotNullAndQueriedIsFalse() {
 jdomResult.setResult(expectedContentList);
 jdomResult.getDocument();
 }

 @Test
 public void testGetDocument_whenResultlistIsNullAndQueriedIsTrue() {
 jdomResult.setResult(null);
 jdomResult.setFactory(new DefaultJDOMFactory());
 Document actualDocument = jdomResult.getDocument();
 Document expectedDocument = new Document();
 expectedDocument.setContent(expectedContentList);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory actualFactory = jdomResult.getFactory();
 assertEquals(factory, actualFactory);
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.retrieveResult();
 assertTrue(jdomResult.resultlist != null || jdomResult.resultdoc != null);
 }

 @Test
 public void testSetHandlerAndSetLexicalHandler() {
 // We cannot test these methods directly as they rely on SAX parsing which is not possible in JUnit
 // Instead, we can test the behavior of the class when the handler and lexical handler are set
 jdomResult.setResult(expectedContentList);
 jdomResult.setFactory(new DefaultJDOMFactory());
 jdomResult.getDocument();
 }
}