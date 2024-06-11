package org.jdom2.transform;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMInputFactory;
import org.jdom2.transform.JDOMResult;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private Document document;
 private List<Element> elements;
 private List<Content> contentList;
 private String xmlString;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 document = new JDOMFactory().newDocument();
 elements = new ArrayList<>();
 contentList = new ArrayList<>();
 xmlString = "<root><a><b>1</b></a><c><d>2</d></c></root>";
 }

 @Test
 public void testGetResult_whenResultIsSet_returnsTheSetResult() {
 contentList.add(new Element("a"));
 contentList.add(new Element("b"));
 contentList.add(new Text("1"));
 jdomResult.setResult(contentList);
 List<Content> result = jdomResult.getResult();
 assertArrayEquals(contentList.toArray(new Content[0]), result.toArray(new Content[0]));
 }

 @Test
 public void testGetResult_whenDocumentIsSet_convertsDocumentToContentAndReturnsIt() throws SAXException {
 JDOMInputFactory inputFactory = new JDOMInputFactory();
 StringReader reader = new StringReader(xmlString);
 document.setContentHandlers(new DefaultHandler());
 inputFactory.load(reader, document);
 jdomResult.setDocument(document);
 List<Content> result = jdomResult.getResult();
 List<Content> expected = document.getRootElement().getContent();
 assertArrayEquals(expected.toArray(new Content[0]), result.toArray(new Content[0]));
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new JDOMFactory();
 jdomResult.setFactory(factory);
 JDOMFactory resultFactory = jdomResult.getFactory();
 assertTrue(resultFactory == factory);
 }

 @Test
 public void testGetDocument_whenResultIsSet_returnsAnEmptyDocument() {
 jdomResult.setResult(contentList);
 Document doc = jdomResult.getDocument();
 assertNull(doc);
 }

 @Test
 public void testGetDocument_whenDocumentIsSet_returnsTheSetDocument() {
 jdomResult.setDocument(document);
 Document resultDoc = jdomResult.getDocument();
 assertTrue(resultDoc == document);
 }

 @Test
 public void testRetrieveResult_whenResultIsNotNull_setsResultToTheDocument() throws SAXException {
 jdomResult.setDocument(document);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 List<Content> expected = document.getRootElement().getContent();
 assertArrayEquals(expected.toArray(new Content[0]), result.toArray(new Content[0]));
 }

 @Test
 public void testRetrieveResult_whenResultIsNotNull_setsResultToTheList() {
 contentList.add(new Element("a"));
 contentList.add(new Element("b"));
 contentList.add(new Text("1"));
 jdomResult.setResult(contentList);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getResult();
 assertArrayEquals(contentList.toArray(new Content[0]), result.toArray(new Content[0]));
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_whenResultIsNotNull_throwsException() throws SAXException {
 jdomResult.setResult(contentList);
 jdomResult.retrieveResult();
 List<Content> resultDocList = jdomResult.getDocument().getContent();
 Document doc = jdomResult.getDocument();
 jdomResult.setDocument(document);
 List<Content> result = jdomResult.getResult();
 assertArrayEquals(resultDocList.toArray(new Content[0]), result.toArray(new Content[0]));
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocument_whenDocumentIsNotSet_throwsException() {
 jdomResult.setResult(contentList);
 jdomResult.retrieveResult();
 List<Content> result = jdomResult.getDocument().getContent();
 }
}