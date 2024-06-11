package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;
 private Document doc;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 factory = new DefaultJDOMFactory();
 result = new JDOMResult();
 doc = new SAXBuilder().build(getClass().getResourceAsStream("/test.xml")).getDocument();
 expectedResult = doc.getContent();
 result.setDocument(doc);
 }

 @Test
 public void testGetResult() throws Exception {
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult.size(), actualResult.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 assertEquals(expectedResult.get(i).getName(), actualResult.get(i).getName());
 }
 }

 @Test
 public void testSetResult() throws Exception {
 List<Content> newResult = new ArrayList<>();
 result.setResult(newResult);
 List<Content> actualResult = result.getResult();
 assertTrue(actualResult.isEmpty());
 }

 @Test
 public void testSetDocument() throws Exception {
 Document newDoc = new SAXBuilder().build(getClass().getResourceAsStream("/test.xml")).getDocument();
 result.setDocument(newDoc);
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult.size(), actualResult.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 assertEquals(expectedResult.get(i).getName(), actualResult.get(i).getName());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResultAfterQueried() throws Exception {
 result.retrieveResult();
 result.getResult();
 }

 @Test
 public void testGetFactory() throws Exception {
 JDOMFactory factory = result.getFactory();
 assertNotNull(factory);
 }

 @Test
 public void testSetFactory() throws Exception {
 JDOMFactory newFactory = new DefaultJDOMFactory();
 result.setFactory(newFactory);
 JDOMFactory actualFactory = result.getFactory();
 assertEquals(newFactory, actualFactory);
 }

 @Test
 public void testRetrieveResult() throws Exception {
 result.retrieveResult();
 List<Content> actualResult = result.getResult();
 assertEquals(expectedResult.size(), actualResult.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 assertEquals(expectedResult.get(i).getName(), actualResult.get(i).getName());
 }
 }

 @Test
 public void testSetHandler() {
 // Not testing this method as it is empty in the given class
 }

 @Test
 public void testSetLexicalHandler() {
 // Not testing this method as it is empty in the given class
 }
}