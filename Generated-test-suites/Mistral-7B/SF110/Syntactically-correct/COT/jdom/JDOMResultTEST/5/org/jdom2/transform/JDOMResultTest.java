package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;

 @Before
 public void setup() {
 factory = new DefaultJDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testConstructor() {
 JDOMResult jdomResult = new JDOMResult();
 assertNull(jdomResult.getResult());
 assertNull(jdomResult.getDocument());
 assertNull(jdomResult.getFactory());
 }

 @Test
 public void testSetResult() {
 List<Content> resultList = new ArrayList<>();
 result.setResult(resultList);
 assertEquals(resultList, result.getResult());
 }

 @Test
 public void testGetResult_nullDocument() {
 result.setDocument(null);
 assertEquals(Collections.emptyList(), result.getResult());
 }

 @Test
 public void testGetResult_emptyDocument() throws SAXException {
 Document document = factory.newDocument("", new Element("root"));
 result.setDocument(document);
 assertEquals(Collections.emptyList(), result.getResult());
 }

 @Test
 public void testGetResult_nonEmptyDocument() throws SAXException {
 Document document = factory.newDocument("", new Element("root")).setContent(new Element("child"));
 result.setDocument(document);
 assertEquals(Arrays.asList(document.getRootElement()), result.getResult());
 }

 @Test(expected = NullPointerException.class)
 public void testGetResult_nullResultdoc() {
 result.retrieveResult();
 result.queried = false;
 assertNull(result.getDocument());
 assertNotSame(Collections.emptyList(), result.getResult());
 }

 @Test
 public void testGetDocument_nullResultlist() {
 result.setResult(null);
 assertNull(result.getDocument());
 }

 @Test
 public void testGetDocument_emptyResultlist() {
 List<Content> resultList = new ArrayList<>();
 result.setResult(resultList);
 result.retrieveResult();
 assertNotNull(result.getDocument());
 assertEquals(0, result.getDocument().getContentSize());
 }

 @Test
 public void testGetDocument_nonEmptyResultlist() throws SAXException {
 List<Content> resultList = new ArrayList<>();
 resultList.add(new Element("child1"));
 resultList.add(new Element("child2"));
 result.setResult(resultList);
 result.retrieveResult();
 Document document = result.getDocument();
 assertEquals("child1", document.getRootElement().getChild(0).getName());
 assertEquals("child2", document.getRootElement().getChild(1).getName());
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 assertEquals(factory, result.getFactory());
 }

 @Test(expected = RuntimeException.class)
 public void testGetFactory_nullFactory() {
 result.setFactory(null);
 result.retrieveResult();
 assertNull(result.getDocument());
 assertNotSame(Collections.emptyList(), result.getResult());
 }

 @Test
 public void testRetrieveResult() {
 result.retrieveResult();
 assertTrue(result.queried);
 }

 @Test
 public void testGetHandler() {
 ContentHandler handler = new DefaultContentHandler();
 result.setHandler(handler);
 assertSame(handler, result.getHandler());
 }

 @Test
 public void testSetLexicalHandler() {
 LexicalHandler handler = new DefaultHandler();
 result.setLexicalHandler(handler);
 assertSame(handler, result.getLexicalHandler());
 }
}