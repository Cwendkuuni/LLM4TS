package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMBuilderFactory;
import org.jdom2.transform.JDOMResult;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private JDOMResult result;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 result = new JDOMResult();
 factory = new JDOMFactory();
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 result = new JDOMResult();
 assertNull(result.getResult());
 assertNull(result.getDocument());
 assertNull(result.getFactory());
 }

 @Test
 public void testSetResult() {
 // Test setting result
 List<Content> content = new ArrayList<>();
 content.add(new Element("test"));
 result.setResult(content);
 List<Content> resultContent = result.getResult();
 assertEquals(content, resultContent);
 }

 @Test
 public void testGetResult_EmptyList() {
 // Test getting result when list is empty
 result.setResult(Collections.emptyList());
 List<Content> resultContent = result.getResult();
 assertSame(Collections.emptyList(), resultContent);
 }

 @Test
 public void testGetResult_Document() {
 // Test getting result as Document
 Document doc = factory.newDocument();
 doc.setContent(new Element("test"));
 result.setDocument(doc);
 Document resultDoc = result.getDocument();
 assertEquals(doc, resultDoc);
 }

 @Test(expected = NullPointerException.class)
 public void testGetResult_NullDocument() {
 // Test getting result with null Document
 result.setDocument(null);
 result.getDocument();
 }

 @Test
 public void testSetDocument_EmptyList() {
 // Test setting Document to an empty list
 result.setDocument(Collections.emptyList());
 List<Content> resultContent = result.getResult();
 assertSame(Collections.emptyList(), resultContent);
 }

 @Test
 public void testSetDocument_Document() {
 // Test setting Document to a Document
 Document doc = factory.newDocument();
 Document resultDoc = result.getDocument();
 result.setDocument(doc);
 assertEquals(doc, resultDoc);
 }

 @Test(expected = NullPointerException.class)
 public void testSetDocument_NullDocument() {
 // Test setting Document to null
 result.setDocument(null);
 }

 @Test
 public void testGetFactory() {
 // Test getting factory
 JDOMFactory resultFactory = result.getFactory();
 assertEquals(factory, resultFactory);
 }

 @Test
 public void testSetFactory() {
 // Test setting factory
 JDOMFactory newFactory = new DefaultJDOMFactory();
 result.setFactory(newFactory);
 JDOMFactory resultFactory = result.getFactory();
 assertEquals(newFactory, resultFactory);
 }

 @Test
 public void testRetrieveResult() throws SAXException {
 // Test retrieving result
 String xml = "<test><child>Hello</child></test>";
 SAXBuilder builder = new SAXBuilder(new JDOMBuilderFactory());
 Document doc = builder.build(new StringReader(xml));
 result.setDocument(doc);
 result.retrieveResult();
 List<Content> resultContent = result.getResult();
 assertEquals(1, resultContent.size());
 assertEquals("Hello", resultContent.get(0).getValue());
 }

 @Test
 public void testGetResult_EmptyDocument() {
 // Test getting result when Document is empty
 result.setDocument(factory.newDocument());
 List<Content> resultContent = result.getResult();
 assertSame(Collections.emptyList(), resultContent);
 }

 @Test
 public void testGetResult_DocumentWithChildren() {
 // Test getting result when Document has children
 String xml = "<test><child1>Hello</child1><child2>World</child2></test>";
 Document doc = factory.newDocument(xml);
 result.setDocument(doc);
 result.retrieveResult();
 List<Content> resultContent = result.getResult();
 assertEquals(2, resultContent.size());
 assertEquals("Hello", resultContent.get(0).getValue());
 assertEquals("World", resultContent.get(1).getValue());
 }

 @Test
 public void testGetResult_DocumentWithAttributes() {
 // Test getting result when Document has attributes
 String xml = "<test att1=\"value1\" att2=\"value2\"><child>Hello</child></test>";
 Document doc = factory.newDocument(xml);
 result.setDocument(doc);
 result.retrieveResult();
 List<Content> resultContent = result.getResult();
 assertEquals(2, resultContent.size());
 Content child = resultContent.get(0);
 assertEquals("Hello", child.getValue());
 assertEquals("child", child.getName());
 Attribute att1 = child.getAttributes().get("att1");
 assertEquals("value1", att1.getValue());
 Attribute att2 = child.getAttributes().get("att2");
 assertEquals("value2", att2.getValue());
 }
}