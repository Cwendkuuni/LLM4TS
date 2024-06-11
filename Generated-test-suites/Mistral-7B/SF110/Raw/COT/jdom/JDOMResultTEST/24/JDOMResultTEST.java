package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult result;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 result = new JDOMResult();
 factory = new JDOMFactory();
 }

 @Test
 public void testConstructors() {
 JDOMResult jdomResult = new JDOMResult();
 assertNull(jdomResult.getResult());
 assertNull(jdomResult.getDocument());
 }

 @Test
 public void testSetResult() {
 List<Content> contentList = new ArrayList<>();
 Content content = new Element("content");
 contentList.add(content);
 result.setResult(contentList);
 List<Content> resultList = result.getResult();
 assertEquals(contentList, resultList);
 }

 @Test
 public void testGetResultEmpty() {
 assertNotNull(result.getResult());
 assertEquals(Collections.emptyList(), result.getResult());
 }

 @Test
 public void testGetResultWithDocument() throws SAXException {
 document = new SAXBuilder().build(getClass().getResourceAsStream("/test.xml"));
 result.setDocument(document);
 List<Content> resultList = result.getResult();
 assertNotNull(resultList);
 assertEquals(document.getRootElement(), resultList.get(0));
 }

 @Test
 public void testGetResultWithList() {
 List<Content> contentList = new ArrayList<>();
 Content content = new Element("content");
 contentList.add(content);
 result.setResult(contentList);
 Document document = factory.newDocument(new Element("root"));
 document.setContent(contentList);
 result.setDocument(document);
 List<Content> resultList = result.getResult();
 assertEquals(contentList, resultList);
 }

 @Test(expected = RuntimeException.class)
 public void testGetDocumentNullResult() {
 result.getDocument();
 }

 @Test
 public void testGetDocumentEmptyResult() {
 result.setResult(Collections.emptyList());
 Document document = result.getDocument();
 assertNull(document);
 }

 @Test
 public void testGetDocumentWithList() throws SAXException {
 List<Content> contentList = new ArrayList<>();
 Content content = new Element("content");
 contentList.add(content);
 result.setResult(contentList);
 Document document = result.getDocument();
 assertNotNull(document);
 document.setContent(contentList);
 assertEquals(contentList, document.getContent());
 }

 @Test
 public void testGetDocumentWithDocument() throws SAXException {
 document = new SAXBuilder().build(getClass().getResourceAsStream("/test.xml"));
 result.setDocument(document);
 Document resultDocument = result.getDocument();
 assertEquals(document, resultDocument);
 }

 @Test
 public void testSetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 assertEquals(factory, result.getFactory());
 }

 @Test
 public void testRetrieveResult() {
 result.retrieveResult();
 assertTrue(result.getResult() != null || result.getDocument() != null);
 }

 @Test
 public void testSetHandler() {
 // Not testing as the method is empty
 }

 @Test
 public void testSetLexicalHandler() {
 // Not testing as the method is empty
 }

 @Test
 public void testFragmentHandlerGetResult() throws SAXException {
 FragmentHandler fragmentHandler = new FragmentHandler(factory);
 List<Content> resultList = fragmentHandler.getResult();
 assertNotNull(resultList);
 assertEquals(0, resultList.size());
 }

 @Test
 public void testDocumentBuilderGetResultEmpty() throws SAXException {
 DocumentBuilder documentBuilder = new DocumentBuilder();
 List<Content> resultList = documentBuilder.getResult();
 assertNotNull(resultList);
 assertEquals(0, resultList.size());
 }

 @Test
 public void testDocumentBuilderGetResultWithContent() throws SAXException {
 DocumentBuilder documentBuilder = new DocumentBuilder();
 Content content = new Element("content");
 documentBuilder.startElement("root", null, null, null);
 documentBuilder.characters("test".toCharArray(), 0, "test".length());
 documentBuilder.endElement("root");
 List<Content> resultList = documentBuilder.getResult();
 assertEquals(1, resultList.size());
 assertEquals(content, resultList.get(0));
 }

 @Test
 public void testDocumentBuilderGetResultWithMultipleContents() throws SAXException {
 DocumentBuilder documentBuilder = new DocumentBuilder();
 String testString = "<root><element1>test1</element1><element2>test2</element2></root>";
 documentBuilder.setInputSource(new StringReader(testString));
 List<Content> resultList = documentBuilder.getResult();
 assertEquals(3, resultList.size());
 assertEquals("element1", resultList.get(0).getName());
 assertEquals("test1", resultList.get(0).getText());
 assertEquals("element2", resultList.get(1).getName());
 assertEquals("test2", resultList.get(1).getText());
 assertEquals("root", resultList.get(2).getName());
 }

}