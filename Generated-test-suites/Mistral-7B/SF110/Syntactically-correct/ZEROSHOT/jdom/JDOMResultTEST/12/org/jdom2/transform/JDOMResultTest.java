package org.jdom2.transform;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private JDOMFactory factory;
 private List<Content> result;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 factory = new DefaultJDOMFactory();
 result = Arrays.asList(new Element("element1"), new Element("element2"));
 }

 @Test
 public void testSetAndGetResult() {
 jdomResult.setResult(result);
 List<Content> actualResult = jdomResult.getResult();
 assertEquals(result, actualResult);
 }

 @Test
 public void testGetResultWhenDocumentIsSet() throws SAXException {
 Document document = new SAXBuilder(factory).build(this.getClass().getResource("/test.xml"));
 jdomResult.setDocument(document);
 List<Content> actualResult = jdomResult.getResult();
 assertNotNull(actualResult);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResultWhenBothResultAndDocumentAreSet() throws SAXException {
 Document document = new SAXBuilder(factory).build(this.getClass().getResource("/test.xml"));
 jdomResult.setResult(result);
 jdomResult.setDocument(document);
 }

 @Test
 public void testGetDocumentWhenResultIsSet() throws SAXException {
 List<Content> content = result;
 jdomResult.setResult(content);
 Document actualDocument = jdomResult.getDocument();
 Document expectedDocument = factory.document(new Element("root"));
 expectedDocument.setContent(content);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDocumentWhenDocumentIsSet() throws SAXException {
 Document document = new SAXBuilder(factory).build(this.getClass().getResource("/test.xml"));
 jdomResult.setDocument(document);
 jdomResult.getDocument();
 }

 @Test
 public void testGetDocumentWhenResultIsEmptyAndQueried() throws SAXException {
 jdomResult.setResult(Collections.emptyList());
 Document actualDocument = jdomResult.getDocument();
 assertNull(actualDocument);
 }
}