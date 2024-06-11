package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.test.SimpleTestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDOMResultTest extends SimpleTestCase {

 private JDOMResult result;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 factory = new DefaultJDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testSetResult() {
 List<Content> inputList = Arrays.asList(new Element("elem1"), new Element("elem2"));
 result.setResult(inputList);
 List<Content> resultList = result.getResult();
 assertEquals(inputList.size(), resultList.size());
 for (int i = 0; i < inputList.size(); i++) {
 assertEquals(inputList.get(i), resultList.get(i));
 }
 }

 @Test
 public void testGetResultWhenResultDocIsNotNull() {
 Document doc = new SAXBuilder().build(new StringReader("<root><elem>elem1</elem><elem>elem2</elem></root>"));
 result.setDocument(doc);
 List<Content> resultList = result.getResult();
 assertEquals(doc.getRootElement().getChildren().size(), resultList.size());
 for (Content content : resultList) {
 assertEquals(content, doc.getRootElement().getChildren().get(0));
 }
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentWhenResultlistIsNullAndQueriedIsFalse() {
 result = new JDOMResult();
 result.setFactory(factory);
 result.retrieveResult();
 assertNull(result.getDocument());
 }

 @Test
 public void testGetDocumentWhenResultlistIsNotNullAndQueriedIsFalse() {
 List<Content> contentList = Arrays.asList(new Element("elem1"), new Element("elem2"));
 result.setResult(contentList);
 Document doc = result.getDocument();
 assertEquals(contentList.size(), doc.getContentSize());
 for (int i = 0; i < contentList.size(); i++) {
 assertEquals(contentList.get(i), doc.getContent().get(i));
 }
 result.queried = true;
 doc = result.getDocument();
 assertNull(doc);
 }

 @Test
 public void testGetDocumentWhenResultlistIsNotNullAndQueriedIsTrue() {
 List<Content> contentList = Arrays.asList(new Element("elem1"), new Element("elem2"));
 result.setResult(contentList);
 Document doc = result.getDocument();
 assertEquals(contentList.size(), doc.getContentSize());
 for (int i = 0; i < contentList.size(); i++) {
 assertEquals(contentList.get(i), doc.getContent().get(i));
 }
 result.queried = true;
 doc = result.getDocument();
 assertNull(doc);
 }

 @Test
 public void testGetFactory() {
 JDOMFactory expectedFactory = factory;
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(expectedFactory, actualFactory);
 }

 @Test
 public void testRetrieveResultWhenResultlistIsNullAndResultdocIsNotNull() {
 Document doc = new SAXBuilder().build(new StringReader("<root><elem>elem1</elem><elem>elem2</elem></root>"));
 result.setDocument(doc);
 result.retrieveResult();
 result.setResult(null);
 List<Content> resultList = result.getResult();
 assertEquals(doc.getRootElement().getChildren().size(), resultList.size());
 for (Content content : resultList) {
 assertEquals(content, doc.getRootElement().getChildren().get(0));
 }
 }

 @Test
 public void testSetHandlerAndSetLexicalHandler() {
 // TODO: Implement tests for setHandler and setLexicalHandler methods
 }

 @Test
 public void testEmptyConstructor() {
 result = new JDOMResult();
 assertNull(result.getFactory());
 assertNull(result.getDocument());
 assertNull(result.getResult());
 assertFalse(result.isQueried());
 }
}