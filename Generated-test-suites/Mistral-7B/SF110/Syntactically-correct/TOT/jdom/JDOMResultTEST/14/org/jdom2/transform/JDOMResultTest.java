package org.jdom2.transform;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
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
 public void testEmptyResult() {
 result.setDocument(null);
 List<Content> list = result.getResult();
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testWithDocumentResult() throws SAXException {
 document = new SAXBuilder().build(getClass().getResource("/test.xml"));
 result.setDocument(document);
 List<Content> list = result.getResult();
 assertNotNull(list);
 assertEquals(document.getRootElement().getContentSize(), list.size());
 }

 @Test
 public void testWithContentResult() {
 List<Content> content = new ArrayList<>();
 content.add(new Element("test"));
 result.setResult(content);
 List<Content> list = result.getResult();
 assertNotNull(list);
 assertEquals(content.size(), list.size());
 }

 @Test
 public void testWithFactoryResult() {
 result.setFactory(factory);
 Document doc = factory.document(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"));
 result.setResult(doc.getContent());
 List<Content> list = result.getResult();
 assertNotNull(list);
 assertEquals(doc.getContentSize(), list.size());
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 result.setDocument(null);
 result.getResult();
 }

 @Test(expected = NullPointerException.class)
 public void testNullFactory() {
 result.setFactory(null);
 Document doc = factory.document(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"));
 result.setResult(doc.getContent());
 result.setFactory(null);
 result.getResult();
 }
}