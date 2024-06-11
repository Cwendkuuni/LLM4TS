package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

public class JDOMResultTest {

 private JDOMResult result;
 private List<Content> expectedResult;
 private Document document;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 result = new JDOMResult();
 factory = new JDOMFactory();
 document = new JDOMImpl();
 }

 @Test
 public void testSetResult() {
 List<Content> testResult = new ArrayList<>();
 testResult.add(new Element("testElement"));
 result.setResult(testResult);
 List<Content> retrievedResult = result.getResult();
 assertEquals(testResult, retrievedResult);
 }

 @Test
 public void testSetDocument() {
 Element root = new Element("root");
 root.addContent(new Element("testElement"));
 document.setContent(new Element[] { root });
 result.setDocument(document);
 List<Content> retrievedResult = result.getResult();
 assertEquals(retrievedResult.size(), 1);
 assertEquals(retrievedResult.get(0).getName(), "testElement");
 }

 @Test
 public void testGetDocument() {
 Element root = new Element("root");
 root.addContent(new Element("testElement"));
 document.setContent(new Element[] { root });
 result.setDocument(document);
 Document retrievedDocument = result.getDocument();
 assertEquals(retrievedDocument.getRootElement().getName(), "root");
 assertEquals(retrievedDocument.getRootElement().getContentSize(), 1);
 }

 @Test
 public void testGetResultWhenDocumentIsEmpty() {
 result.setDocument(document);
 List<Content> retrievedResult = result.getResult();
 assertEquals(retrievedResult.size(), 0);
 }

 @Test
 public void testGetResultWhenDocumentIsSet() {
 Element root = new Element("root");
 root.addContent(new Element("testElement1"));
 root.addContent(new Element("testElement2"));
 document.setContent(new Element[] { root });
 result.setDocument(document);
 List<Content> retrievedResult = result.getResult();
 assertEquals(retrievedResult.size(), 2);
 assertEquals(retrievedResult.get(0).getName(), "testElement1");
 assertEquals(retrievedResult.get(1).getName(), "testElement2");
 }

 @Test
 public void testGetFactory() {
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 JDOMFactory retrievedFactory = result.getFactory();
 assertEquals(factory, retrievedFactory);
 }

 @Test
 public void testRetrieveResultWhenBothResultAndDocumentAreEmpty() {
 result.retrieveResult();
 List<Content> retrievedResult = result.getResult();
 assertEquals(retrievedResult.size(), 0);
 }

 @Test
 public void testRetrieveResultWhenResultIsEmptyButDocumentIsNot() throws Exception {
 SAXBuilder builder = new SAXBuilder();
 builder.setValidation(false);
 document = builder.build(getClass().getResource("/test.xml"));
 result.setDocument(document);
 result.retrieveResult();
 List<Content> retrievedResult = result.getResult();
 assertEquals(retrievedResult.size(), 12);
 }

 @Test(expected = RuntimeException.class)
 public void testRetrieveResultWhenBothResultAndDocumentAreNotEmpty() throws Exception {
 List<Content> testResult = new ArrayList<>();
 testResult.add(new Element("testElement"));
 result.setResult(testResult);
 Element root = new Element("root");
 root.addContent(new Element("testElement1"));
 root.addContent(new Element("testElement2"));
 document.setContent(new Element[] { root });
 result.setDocument(document);
 result.retrieveResult();
 List<Content> retrievedResult = result.getResult();
 assertEquals(retrievedResult.size(), 14);
 }
}