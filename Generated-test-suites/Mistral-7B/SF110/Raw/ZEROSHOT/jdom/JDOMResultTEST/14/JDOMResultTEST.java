package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 expectedResult = new ArrayList<>();
 expectedResult.add(new Element("element1"));
 expectedResult.add(new Text("Text1"));
 expectedResult.add(new Element("element2"));
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(expectedResult);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetResultWhenNoResultIsSet() {
 List<Content> result = jdomResult.getResult();
 assertNull(result);
 }

 @Test
 public void testGetDocumentWhenResultIsSet() {
 Document doc = jdomResult.getDocument();
 assertEquals(expectedResult.get(0).getName(), doc.getRootElement().getName());
 }

 @Test
 public void testGetDocumentWhenNoResultIsSetAndResultdocIsSet() {
 Element rootElement = new Element("root");
 Document document = new Document(rootElement);
 jdomResult.setDocument(document);
 Document resultDoc = jdomResult.getDocument();
 assertEquals(rootElement, resultDoc);
 }

 @Test
 public void testGetFactory() {
 JDOMFactory factory = jdomResult.getFactory();
 assertNotNull(factory);
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.setResult(expectedResult);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetDocument() {
 Element rootElement = new Element("root");
 Document document = new Document(rootElement);
 jdomResult.setDocument(document);
 Document resultDoc = jdomResult.getDocument();
 assertEquals(rootElement, resultDoc);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenNoResultIsSetAndNoDocumentIsSet() {
 Document doc = jdomResult.getDocument();
 fail("Expected RuntimeException");
 }

 @Test
 public void testSetHandler() {
 // Method under test does not have setHandler method
 }

 @Test
 public void testSetLexicalHandler() {
 // Method under test does not have setLexicalHandler method
 }

 @Test
 public void testFragmentHandlerGetResult() {
 SAXBuilder builder = new SAXBuilder();
 String xmlString = "<root><element1>Text1</element1><element2></element2></root>";
 InputSource inputSource = new InputSource(new StringReader(xmlString));
 jdomResult.setFactory(builder.getInputFactory());
 builder.setInputFactory(jdomResult);
 List<Content> result = jdomResult.getResult();
 assertEquals(2, result.size());
 }
}