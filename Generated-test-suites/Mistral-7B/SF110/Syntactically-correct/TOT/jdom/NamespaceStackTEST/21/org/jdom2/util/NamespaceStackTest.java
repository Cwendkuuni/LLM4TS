package org.jdom2.util;

import junit.framework.TestCase;
import org.jdom2.*;
import org.jdom2.namespace.Namespace;

public class NamespaceStackTest extends TestCase {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 Namespace xmlNamespace = Namespace.getXmlNamespace();
 stack = new NamespaceStack(new Namespace[] { Namespace.NO_NAMESPACE, xmlNamespace });
 }

 @Test
 public void testPushAndPop() {
 Element root = new Element("root");
 root.setName("{http://example.com}root");
 stack.push(root);
 assertNotNull(stack.getScope());
 assertEquals(2, stack.getScope().length);
 assertEquals("http://example.com", stack.getScope()[0].getURI().toString());
 stack.pop();
 assertNotNull(stack.getScope());
 assertEquals(1, stack.getScope().length);
 assertEquals("http://example.com", stack.getScope()[0].getURI().toString());
 }

 @Test
 public void testPushMultipleNamespaces() {
 Element root = new Element("root");
 root.setName("{http://example.com}root");
 stack.push(root);
 Attribute attr = new Attribute("attr", "value", Namespace.XML_NAMESPACE);
 root.addAttribute(attr);
 stack.push(attr);
 assertNotNull(stack.getScope());
 assertEquals(3, stack.getScope().length);
 assertEquals("http://example.com", stack.getScope()[0].getURI().toString());
 assertEquals("http://www.w3.org/XML/1998/namespace", stack.getScope()[1].getURI().toString());
 assertEquals("http://example.com", stack.getScope()[2].getURI().toString());
 stack.pop();
 stack.pop();
 assertNotNull(stack.getScope());
 assertEquals(1, stack.getScope().length);
 assertEquals("http://example.com", stack.getScope()[0].getURI().toString());
 }

 @Test
 public void testPushSameNamespaceMultipleTimes() {
 Element root = new Element("root");
 root.setName("{http://example.com}root");
 stack.push(root);
 Element child = new Element("child");
 child.setName("{http://example.com}child");
 stack.push(child);
 stack.push(child);
 assertNotNull(stack.getScope());
 assertEquals(3, stack.getScope().length);
 assertEquals("http://example.com", stack.getScope()[0].getURI().toString());
 assertEquals("http://example.com", stack.getScope()[1].getURI().toString());
 assertEquals("http://example.com", stack.getScope()[2].getURI().toString());
 stack.pop();
 stack.pop();
 assertNotNull(stack.getScope());
 assertEquals(1, stack.getScope().length);
 assertEquals("http://example.com", stack.getScope()[0].getURI().toString());
 }

 @Test
 public void testPushNullElement() {
 try {
 stack.push(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertNotNull(stack.getScope());
 assertEquals(0, stack.getScope().length);
 }

 @Test
 public void testPushEmptyElement() {
 Element emptyElement = new Element();
 stack.push(emptyElement);
 assertNotNull(stack.getScope());
 assertEquals(1, stack.getScope().length);
 assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
 }

 @Test
 public void testPopWhenEmpty() {
 try {
 stack.pop();
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 assertNotNull(stack.getScope());
 assertEquals(0, stack.getScope().length);
 }

 @Test
 public void testPushAndPopMultipleTimes() {
 Element root = new Element("root");
 root.setName("{http://example.com}root");
 stack.push(root);
 Attribute attr = new Attribute("attr", "value", Namespace.XML_NAMESPACE);
 root.addAttribute(attr);
 stack.push(attr);
 stack.push(new Element("element"));
 stack.pop();
 assertNotNull(stack.getScope());
 assertEquals(2, stack.getScope().length);
 assertEquals("http://example.com", stack.getScope()[0].getURI().toString());
 assertEquals("http://www.w3.org/XML/1998/namespace", stack.getScope()[1].getURI().toString());
 }

 @Test
 public void testIsInScope() {
 Element root = new Element("root");
 root.setName("{http://example.com}root");
 stack.push(root);
 Attribute attr = new Attribute("attr", "value", Namespace.XML_NAMESPACE);
 root.addAttribute(attr);
 stack.push(attr);
 assertTrue(stack.isInScope(attr.getNamespace()));
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 }

 @Test
 public void testAddedForward() {
 Element root = new Element("root");
 root.setName("{http://example.com}root");
 stack.push(root);
 Attribute attr = new Attribute("attr", "value", Namespace.XML_NAMESPACE);
 root.addAttribute(attr);
 stack.push(attr);
 Iterator<Namespace> iterator = stack.addedForward();
 assertEquals(attr.getNamespace(), iterator.next());
 assertEquals(root.getNamespace(), iterator.next());
 assertNull(iterator.hasNext());
 }

 @Test
 public void testAddedReverse() {
 Element root = new Element("root");
 root.setName("{http://example.com}root");
 stack.push(root);
 Attribute attr = new Attribute("attr", "value", Namespace.XML_NAMESPACE);
 root.addAttribute(attr);
 stack.push(attr);
 Iterator<Namespace> iterator = stack.addedReverse();
 assertEquals(root.getNamespace(), iterator.next());
 assertEquals(attr.getNamespace(), iterator.next());
 assertNull(iterator.hasNext());
 }

 @After
 public void tearDown() {
 stack = null;
 }
}