package org.jdom2.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;

 @Test
 public void testDefaultConstructor() {
 result = new JDOMResult();
 assertNull(result.getResult());
 assertNull(result.getDocument());
 assertFalse(result.isQueried());
 assertNull(result.getFactory());
 }

 @Test
 public void testSetResult() {
 result = new JDOMResult();
 List<Content> content = new ArrayList<>();
 content.add(new Element("test"));
 result.setResult(content);
 assertEquals(content, result.getResult());
 assertNull(result.getDocument());
 assertFalse(result.isQueried());
 assertNull(result.getFactory());
 }

 @Test
 public void testGetDocumentFromResult() throws SAXException {
 result = new JDOMResult();
 List<Content> content = new ArrayList<>();
 content.add(new Element("test"));
 result.setResult(content);
 JDOMFactory factory = new DefaultJDOMFactory();
 result.setFactory(factory);
 Document doc = result.getDocument();
 assertEquals("test", doc.getRootElement().getName());
 assertNotNull(doc.getRootElement().getChildren());
 assertFalse(result.isQueried());
 }

 @Test
 public void testSetDocument() {
 result = new JDOMResult();
 Document doc = new Document();
 Element root = new Element("test");
 doc.setRootElement(root);
 result.setDocument(doc);
 assertEquals(root, result.getDocument().getRootElement());
 assertNull(result.getResult());
 assertFalse(result.isQueried());
 assertNull(result.getFactory());
 }

 @Test
 public void testGetFactory() {
 result = new JDOMResult();
 result.setFactory(new DefaultJDOMFactory());
 assertEquals(new DefaultJDOMFactory(), result.getFactory());
 }

 @Test
 public void testRetrieveResult() throws SAXException {
 result = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(null);
 Element root = new Element("test");
 root.addContent(new Element("child1"));
 root.addContent(new Element("child2"));
 Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?>\n<test>\n<child1/>\n<child2/>\n</test>"));
 result.setDocument(doc);
 List<Content> resultList = result.getResult();
 assertEquals(2, resultList.size());
 assertEquals("child1", resultList.get(0).getName());
 assertEquals("child2", resultList.get(1).getName());
 assertFalse(result.isQueried());
 }

 @Test
 public void testGetResultTwice() throws SAXException {
 result = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(null);
 Element root = new Element("test");
 root.addContent(new Element("child1"));
 root.addContent(new Element("child2"));
 Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?>\n<test>\n<child1/>\n<child2/>\n</test>"));
 result.setDocument(doc);
 List<Content> resultList = result.getResult();
 assertEquals(2, resultList.size());
 assertEquals("child1", resultList.get(0).getName());
 assertEquals("child2", resultList.get(1).getName());
 List<Content> resultList2 = result.getResult();
 assertEquals(2, resultList2.size());
 assertEquals("child1", resultList2.get(0).getName());
 assertEquals("child2", resultList2.get(1).getName());
 assertFalse(result.isQueried());
 }

 @Test
 public void testQueriedAfterGettingResult() throws SAXException {
 result = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(null);
 Element root = new Element("test");
 root.addContent(new Element("child1"));
 root.addContent(new Element("child2"));
 Document doc = builder.build(new StringReader("<?xml version=\"1.0\"?>\n<test>\n<child1/>\n<child2/>\n</test>"));
 result.setDocument(doc);
 List<Content> resultList = result.getResult();
 assertEquals(2, resultList.size());
 assertEquals("child1", resultList.get(0).getName());
 assertEquals("child2", resultList.get(1).getName());
 result.retrieveResult();
 resultList = result.getResult();
 assertEquals(2, resultList.size());
 assertEquals("child1", resultList.get(0).getName());
 assertEquals("child2", resultList.get(1).getName());
 assertTrue(result.isQueried());
 }

}