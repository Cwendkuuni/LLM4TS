package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.junit.Test;
import org.xml.sax.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResult;
 private Document document;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 expectedResult = new ArrayList<Content>();
 expectedResult.add(new Element("element1", null, null));
 expectedResult.add(new Text("Text content"));
 document = new JDOMFactory().document(new Element("root", null, null));
 document.setContent(expectedResult);
 }

 @Test
 public void testSetResult() {
 List<Content> result = new ArrayList<Content>();
 result.add(new Element("element2", null, null));
 result.add(new Text("Another text content"));
 jdomResult.setResult(result);
 assertEquals(result, jdomResult.getResult());
 }

 @Test
 public void testGetResultWhenResultIsNotNull() {
 jdomResult.setResult(expectedResult);
 assertEquals(expectedResult, jdomResult.getResult());
 }

 @Test
 public void testGetResultWhenDocumentIsNotNull() {
 jdomResult.setDocument(document);
 assertEquals(expectedResult, jdomResult.getResult());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResultWhenNeitherResultNorDocumentIsSet() {
 jdomResult.getResult();
 }

 @Test
 public void testSetDocument() {
 Document document = new JDOMFactory().document(new Element("root2", null, null));
 jdomResult.setDocument(document);
 assertEquals(document, jdomResult.getDocument());
 }

 @Test
 public void testGetDocument() {
 jdomResult.setResult(expectedResult);
 assertEquals(document, jdomResult.getDocument());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDocumentWhenNeitherResultNorDocumentIsSet() {
 jdomResult.getDocument();
 }

 @Test
 public void testGetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.setResult(expectedResult);
 List<Content> result = jdomResult.getResult();
 assertSame(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHandler() {
 jdomResult.setHandler(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLexicalHandler() {
 jdomResult.setLexicalHandler(null);
 }
}