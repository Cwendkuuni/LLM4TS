package org.jdom2.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.transform.sax.SAXResult;
import org.jdom2.*;
import org.xml.sax.helpers.DefaultHandler;
import org.junit.Test;

public class JDOMResultTest {

 JDOMResult jdomResult = null;
 JDOMFactory factory = new DefaultJDOMFactory();

 @Test
 public void testJDOMResultConstructor() {
 jdomResult = new JDOMResult();
 assertNotNull(jdomResult.getFactory());
 assertNull(jdomResult.getDocument());
 assertNull(jdomResult.getResultlist());
 assertFalse(jdomResult.isQueried());
 }

 @Test
 public void testSetResult() {
 jdomResult = new JDOMResult();
 List<Content> result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 assertNotNull(jdomResult.getResultlist());
 assertNull(jdomResult.getDocument());
 assertFalse(jdomResult.isQueried());
 }

 @Test
 public void testGetResult() {
 jdomResult = new JDOMResult();
 List<Content> result = new ArrayList<>();
 result.add(new Element("test"));
 jdomResult.setResult(result);
 List<Content> getResult = jdomResult.getResult();
 assertEquals(result, getResult);
 assertTrue(jdomResult.isQueried());
 }

 @Test
 public void testSetDocument() {
 jdomResult = new JDOMResult();
 Document document = new Document();
 jdomResult.setDocument(document);
 assertNotNull(jdomResult.getDocument());
 assertNull(jdomResult.getResultlist());
 assertFalse(jdomResult.isQueried());
 }

 @Test
 public void testGetDocument() {
 JDOMResult jdomResult = new JDOMResult();
 Document document = new Document();
 jdomResult.setDocument(document);
 Document getDocument = jdomResult.getDocument();
 assertEquals(document, getDocument);
 assertTrue(jdomResult.isQueried());
 }

 @Test
 public void testRetrieveResult() {
 JDOMResult jdomResult = new JDOMResult();
 DocumentBuilder builder = new DocumentBuilder();
 SAXResult saxResult = new SAXResult(builder);
 jdomResult.setHandler(builder);
 jdomResult.setLexicalHandler(builder);
 builder.startDocument();
 builder.startElement("root", null, null, null);
 builder.characters("test".toCharArray(), 0, 4);
 builder.endElement();
 builder.endDocument();
 List<Content> getResult = jdomResult.getResult();
 List<Content> expectedResult = new ArrayList<>();
 expectedResult.add(new Element("root").setText("test"));
 assertEquals(expectedResult, getResult);
 assertTrue(jdomResult.isQueried());
 }

 @Test
 public void testSetFactory() {
 JDOMResult jdomResult = new JDOMResult();
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testGetFactory() {
 JDOMResult jdomResult = new JDOMResult();
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory getFactory = jdomResult.getFactory();
 assertEquals(factory, getFactory);
 }
}