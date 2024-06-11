package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.*;

import java.io.*;
import java.util.*;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;
 private Document doc;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 result = new JDOMResult();
 factory = new DefaultJDOMFactory();
 }

 @Test
 public void testSetResult() {
 List<Content> list = new ArrayList<>();
 list.add(new Element("test", null, null));
 result.setResult(list);
 assertEquals(list, result.getResult());
 }

 @Test
 public void testGetResult_whenResultlistIsNotNull_returnsResultlist() {
 expectedResult = new ArrayList<>();
 expectedResult.add(new Element("test1", null, null));
 expectedResult.add(new Element("test2", null, null));
 result.setResult(expectedResult);
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGetResult_whenResultdocIsNotNull_returnsDocument() {
 doc = new SAXBuilder().build(new StringReader("<root><test>Test</test></root>"));
 result.setDocument(doc);
 Document actualResult = result.getDocument();
 assertEquals(doc, actualResult);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDocument_whenResultlistIsNullAndqueriedIsFalse_returnsNull() {
 result.setResult(null);
 Document actualResult = result.getDocument();
 assertNull(actualResult);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDocument_whenResultdocIsNullAndqueriedIsFalse_returnsNull() {
 result.setDocument(null);
 Document actualResult = result.getDocument();
 assertNull(actualResult);
 }

 @Test
 public void testGetDocument_whenResultlistIsNotNullAndqueriedIsTrue_returnsDocument() {
 result.setResult(expectedResult);
 result.getDocument(); // retrieve result
 Document actualResult = result.getDocument();
 assertEquals(expectedResult.getRootElement(), actualResult.getRootElement());
 }

 @Test(expected = RuntimeException.class)
 public void testSetFactory_whenFactoryIsNull_throwsRuntimeException() {
 try {
 result.setFactory(null);
 } catch (RuntimeException e) {
 //expected
 }
 }

 @Test
 public void testSetFactory_whenFactoryIsNotNull_setsFactory() {
 JDOMFactory factory = new JDOMFactory() {
 @Override
 public Document newDocument() {
 return new Document("");
 }
 };
 result.setFactory(factory);
 assertEquals(factory, result.getFactory());
 }

 @Test
 public void testRetrieveResult_whenResultlistIsNullAndResultdocIsNull_setsResultlist() throws SAXException, IOException {
 String xmlString = "<root><test>Test</test></root>";
 InputSource inputSource = new InputSource(new StringReader(xmlString));
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(new DefaultEntityResolver());
 result.setHandler(builder);
 builder.parse(inputSource);
 List<Content> expectedResult = builder.getDocument().getContent();
 result.retrieveResult();
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetResult_whenResultIsAlreadySet_throwsIllegalStateException() {
 List<Content> list = new ArrayList<>();
 list.add(new Element("test", null, null));
 result.setResult(list);
 result.setResult(new Element("test2", null, null));
 }

}