package org.jdom2.transform;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private Document inputDocument;
 private List<Content> expectedResult;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 inputDocument = new SAXBuilder().build(getClass().getResource("/input.xml"));
 }

 @Test
 public void testSetResult() {
 List<Content> result = new ArrayList<>();
 result.add(new Element("element1"));
 result.add(new Text("text1"));
 jdomResult.setResult(result);
 assertEquals(result, jdomResult.getResult());
 }

 @Test
 public void testGetDocument_whenResultIsSet_shouldReturnDocument() throws Exception {
 Document document = jdomResult.getDocument();
 assertEquals(inputDocument, document);
 }

 @Test
 public void testGetDocument_whenResultIsNotSet_shouldReturnNull() {
 assertNull(jdomResult.getDocument());
 }

 @Test
 public void testGetDocument_whenResultIsSetAndResultIsEmpty_shouldReturnEmptyDocument() throws Exception {
 jdomResult.setResult(Collections.emptyList());
 Document document = jdomResult.getDocument();
 assertTrue(document.isEmpty());
 }

 @Test
 public void testSetDocument() throws Exception {
 Document document = new Document();
 document.setRootElement(new Element("root"));
 document.getRootElement().addContent(new Element("element1"));
 document.getRootElement().addContent(new Text("text1"));
 jdomResult.setDocument(document);
 assertEquals(document, jdomResult.getDocument());
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test(expected = RuntimeException.class)
 public void testRetrieveResult_whenResultIsNotSet_shouldThrowNullPointerException() {
 jdomResult.retrieveResult();
 fail("Should have thrown RuntimeException");
 }

 @Test
 public void testRetrieveResult_whenResultIsSet_shouldSetResultToNullAndReturnDocument() throws Exception {
 jdomResult.setResult(Collections.singletonList(new Element("element1")));
 Document document = jdomResult.getDocument();
 assertEquals(inputDocument, document);
 assertEquals(Collections.emptyList(), jdomResult.getResult());
 }

 @Test(expected = SAXException.class)
 public void testDocumentBuilder_whenStartDTDEntityIsNotStarted_shouldThrowException() throws Exception {
 jdomResult.setFactory(new DefaultJDOMFactory());
 jdomResult.getDocument().setRootElement(new Element("root"));
 jdomResult.getDocument().getRootElement().addContent(new Text("text1"));
 jdomResult.getDocument().getRootElement().addNameSpace("test", "http://test.com");
 jdomResult.getDocument().getRootElement().addProcessingInstruction("xml", "version=\"1.0\"");
 jdomResult.getDocument().getRootElement().addStartElement("test:elem", null, null);
 jdomResult.getDocument().getRootElement().addStartEntity("test:ent", null);
 jdomResult.getDocument().getRootElement().getContentHandler().startDTD("test", null, null);
 jdomResult.getDocument().getRootElement().getContentHandler().endDTD();
 jdomResult.getDocument().getRootElement().getContentHandler().startEntity("test:ent", null);
 jdomResult.getDocument().getRootElement().getContentHandler().endEntity("test:ent");
 DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
 documentBuilder.startDocument();
 documentBuilder.startDTD("test", null, null);
 documentBuilder.endDTD();
 documentBuilder.startEntity("test:ent", null);
 documentBuilder.endEntity("test:ent");
 documentBuilder.characters("test".getBytes(), 0, "test".length());
 fail("Should have thrown SAXException");
 }

 @Test
 public void testGetFactory_whenFactoryIsSet_shouldReturnFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testGetFactory_whenFactoryIsNotSet_shouldReturnDefaultFactory() {
 JDOMFactory factory = jdomResult.getFactory();
 assertTrue(factory instanceof DefaultJDOMFactory);
 }

 @Test
 public void testFragmentHandler_whenStartDocumentIsNotReceived_shouldReturnEmptyList() throws Exception {
 FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(jdomResult.getFactory());
 List<Content> result = fragmentHandler.getResult();
 assertArrayEquals(new Content[0], result.toArray());
 }

 @Test
 public void testFragmentHandler_whenStartDocumentIsReceived_shouldReturnCorrectResult() throws Exception {
 Document document = new Document();
 document.setRootElement(new Element("root"));
 document.getRootElement().addContent(new Element("element1"));
 document.getRootElement().addContent(new Text("text1"));
 document.getRootElement().addNameSpace("test", "http://test.com");
 document.getRootElement().addProcessingInstruction("xml", "version=\"1.0\"");
 document.getRootElement().addStartElement("test:elem", null, null);
 document.getRootElement().addStartDTD("test", null, null);
 document.getRootElement().addEndDTD();
 document.getRootElement().addStartEntity("test:ent", null);
 document.getRootElement().addEndEntity("test:ent");
 document.getRootElement().getContentHandler().startDocument();
 document.getRootElement().getContentHandler().startDTD("test", null, null);
 document.getRootElement().getContentHandler().endDTD();
 document.getRootElement().getContentHandler().startEntity("test:ent", null);
 document.getRootElement().getContentHandler().endEntity("test:ent");
 document.getRootElement().getContentHandler().characters("test".getBytes(), 0, "test".length());
 fragmentHandler = new JDOMResult.FragmentHandler(jdomResult.getFactory());
 jdomResult.setDocument(document);
 List<Content> result = fragmentHandler.getResult();
 expectedResult = new ArrayList<>();
 expectedResult.add(document.getRootElement());
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFragmentHandler_whenEndDocumentIsNotReceived_shouldReturnCorrectResult() throws Exception {
 Document document = new Document();
 document.setRootElement(new Element("root"));
 document.getRootElement().addContent(new Element("element1"));
 document.getRootElement().addContent(new Text("text1"));
 document.getRootElement().addNameSpace("test", "http://test.com");
 document.getRootElement().addProcessingInstruction("xml", "version=\"1.0\"");
 document.getRootElement().addStartElement("test:elem", null, null);
 document.getRootElement().addStartDTD("test", null, null);
 document.getRootElement().addEndDTD();
 document.getRootElement().addStartEntity("test:ent", null);
 document.getRootElement().addEndEntity("test:ent");
 document.getRootElement().getContentHandler().startDocument();
 document.getRootElement().getContentHandler().startDTD("test", null, null);
 document.getRootElement().getContentHandler().endDTD();
 document.getRootElement().getContentHandler().startEntity("test:ent", null);
 document.getRootElement().getContentHandler().endEntity("test:ent");
 document.getRootElement().getContentHandler().characters("test".getBytes(), 0, "test".length());
 fragmentHandler = new JDOMResult.FragmentHandler(jdomResult.getFactory());
 jdomResult.setDocument(document);
 List<Content> result = fragmentHandler.getResult();
 expectedResult = new ArrayList<>();
 expectedResult.addAll(document.getRootElement().getChildren());
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFragmentHandler_whenEndDocumentIsReceived_shouldReturnEmptyList() throws Exception {
 Document document = new Document();
 document.setRootElement(new Element("root"));
 document.getRootElement().addContent(new Element("element1"));
 document.getRootElement().addContent(new Text("text1"));
 document.getRootElement().addNameSpace("test", "http://test.com");
 document.getRootElement().addProcessingInstruction("xml", "version=\"1.0\"");
 document.getRootElement().addStartElement("test:elem", null, null);
 document.getRootElement().addStartDTD("test", null, null);
 document.getRootElement().addEndDTD();
 document.getRootElement().addStartEntity("test:ent", null);
 document.getRootElement().addEndEntity("test:ent");
 document.getRootElement().getContentHandler().startDocument();
 document.getRootElement().getContentHandler().startDTD("test", null, null);
 document.getRootElement().getContentHandler().endDTD();
 document.getRootElement().getContentHandler().startEntity("test:ent", null);
 document.getRootElement().getContentHandler().endEntity("test:ent");
 document.getRootElement().getContentHandler().characters("test".getBytes(), 0, "test".length());
 document.getRootElement().getContentHandler().endDocument();
 fragmentHandler = new JDOMResult.FragmentHandler(jdomResult.getFactory());
 jdomResult.setDocument(document);
 List<Content> result = fragmentHandler.getResult();
 assertArrayEquals(new Content[0], result.toArray());
 }
}