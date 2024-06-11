package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> expectedResultList;
 private Document expectedDocument;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 expectedResultList = new ArrayList<>();
 expectedResultList.add(new Element("element1"));
 expectedResultList.add(new Element("element2"));
 expectedDocument = new SAXBuilder().build(getClass().getResource("/input.xml")).getDocument();
 factory = new DefaultJDOMFactory();
 }

 @Test
 public void testGetResult_WhenSetResultList_ReturnsResultList() {
 jdomResult.setResult(expectedResultList);
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResultList, result);
 }

 @Test
 public void testGetResult_WhenSetDocument_CreatesDocumentFromResultList() {
 jdomResult.setDocument(expectedDocument);
 Document result = jdomResult.getDocument();
 assertEquals(expectedDocument, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResult_WhenBothResultListAndDocumentSet_ThrowsException() {
 jdomResult.setResult(expectedResultList);
 jdomResult.setDocument(expectedDocument);
 jdomResult.getResult();
 }

 @Test
 public void testGetDocument_WhenResultListIsSet_CreatesDocumentFromResultList() {
 jdomResult.setResult(expectedResultList);
 Document result = jdomResult.getDocument();
 assertEquals(expectedDocument, result);
 }

 @Test
 public void testGetDocument_WhenDocumentIsSet_ReturnsDocument() {
 jdomResult.setDocument(expectedDocument);
 Document result = jdomResult.getDocument();
 assertEquals(expectedDocument, result);
 }

 @Test
 public void testGetFactory_ReturnsFactory() {
 JDOMFactory result = jdomResult.getFactory();
 assertEquals(factory, result);
 }

 @Test
 public void testSetFactory_SetsFactory() {
 JDOMFactory newFactory = new DefaultJDOMFactory();
 jdomResult.setFactory(newFactory);
 JDOMFactory result = jdomResult.getFactory();
 assertEquals(newFactory, result);
 }

 @Test
 public void testRetrieveResult_WhenResultListIsnullAndDocumentIsNull_setsResultFromHandler() throws SAXException, JDOMException {
 jdomResult.setHandler(new ContentHandler() {
 @Override
 public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
 expectedResultList.add(new Element(qName));
 }

 @Override
 public void endElement(final String uri, final String localName, final String qName) throws SAXException {
 }

 @Override
 public void characters(final char[] ch, final int start, final int length) throws SAXException {
 // empty
 }
 });
 List<Content> result = jdomResult.getResult();
 assertEquals(expectedResultList, result);
 }

 @Test
 public void testSetHandler_DoesNothing() {
 // empty
 }

 @Test
 public void testSetLexicalHandler_DoesNothing() {
 // empty
 }
}