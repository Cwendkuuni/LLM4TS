package org.jdom2.transform;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 expectedResult = new ArrayList<>();
 expectedResult.add(new Element("expected1"));
 expectedResult.add(new Element("expected2"));
 }

 @After
 public void tearDown() {
 jdomResult.setResult(null);
 jdomResult.setDocument(null);
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(expectedResult);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetResult_EmptyResult() {
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetResult_Document() throws SAXException {
 String xml = "<root><element1/><element2/></root>";
 SAXBuilder builder = new SAXBuilder();
 Document document = builder.build(new StringReader(xml));
 jdomResult.setDocument(document);
 List<Content> result = jdomResult.getResult();
 assertEquals(2, result.size());
 assertSame(document.getRootElement(), result.get(0));
 }

 @Test(expected = RuntimeException.class)
 public void testGetResult_NullDocument() {
 jdomResult.setDocument(null);
 List<Content> result = jdomResult.getResult();
 fail("Expected a RuntimeException but nothing was thrown.");
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new JDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testGetFactory_NullFactory() {
 jdomResult.setFactory(null);
 assertNull(jdomResult.getFactory());
 }
}