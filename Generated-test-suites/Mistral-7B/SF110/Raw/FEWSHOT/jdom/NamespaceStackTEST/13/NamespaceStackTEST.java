package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 stack = new NamespaceStack();
 }

 @Test
 public void testNamespaceStackEmpty() {
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 assertEquals(0, addedForward.size());
 assertEquals(0, addedReverse.size());
 }

 @Test
 public void testPushPop() {
 stack.push(createElement("root", "http://www.example.com/foo/bar"));
 stack.push(createElement("element", "http://www.example.com/baz"));
 stack.pop();
 stack.pop();

 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 assertEquals(0, addedForward.size());
 assertEquals(0, addedReverse.size());
 }

 @Test
 public void testPushPopWithAdditionalNamespaces() {
 Element root = createElement("root", "http://www.example.com/foo/bar");
 root.setAdditionalNamespaces(new Namespace[]{"http://www.example.com/baz/qux", "http://www.example.com/quux"});
 stack.push(root);
 stack.pop();

 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 assertEquals(0, addedForward.size());
 assertEquals(0, addedReverse.size());
 }

 @Test
 public void testIsInScope() {
 stack.push(createElement("root", "http://www.example.com/foo/bar"));
 stack.push(createElement("element", "http://www.example.com/baz"));

 assertTrue(stack.isInScope(new QName("", "element", "http://www.example.com/baz")));
 assertTrue(stack.isInScope(new QName("", "element", "http://www.example.com/baz/qux")));
 assertTrue(stack.isInScope(new QName("", "element", "http://www.example.com/baz/qux/quux")));

 assertFalse(stack.isInScope(new QName("", "element", "http://www.example.com/foo/bar")));
 assertFalse(stack.isInScope(new QName("", "element", "http://www.example.com/quux")));
 }

 @Test
 public void testGetScope() {
 stack.push(createElement("root", "http://www.example.com/foo/bar"));
 stack.push(createElement("element", "http://www.example.com/baz"));

 Namespace[] expectedScope = new Namespace[]{Namespace.XML_NAMESPACE, new Namespace("http://www.example.com/foo/bar"), new Namespace("http://www.example.com/baz")};
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testPushWithSamePrefix() {
 Element root = createElement("root", "http://www.example.com/foo/bar");
 Element element = createElement("element", "http://www.example.com/foo/bar");
 stack.push(root);
 stack.push(element);

 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 assertEquals(2, addedForward.size());
 assertEquals(2, addedReverse.size());
 Namespace rootNamespace = addedForward.iterator().next();
 Namespace elementNamespace = addedForward.iterator().next();
 assertEquals(rootNamespace, elementNamespace);
 }

 @Test
 public void testPushWithSamePrefixButDifferentURI() {
 Element root = createElement("root", "http://www.example1.com/foo/bar");
 Element element = createElement("element", "http://www.example2.com/foo/bar");
 stack.push(root);
 stack.push(element);

 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 assertEquals(2, addedForward.size());
 assertEquals(2, addedReverse.size());
 Namespace rootNamespace = addedForward.iterator().next();
 Namespace elementNamespace = addedForward.iterator().next();
 assertNotEquals(rootNamespace, elementNamespace);
 }

 @Test
 public void testPushWithSamePrefixButDifferentPrefix() {
 Element root = createElement("root", "http://www.example.com/foo:bar");
 Element element = createElement("element", "http://www.example.com/foo:baz");
 stack.push(root);
 stack.push(element);

 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 assertEquals(2, addedForward.size());
 assertEquals(2, addedReverse.size());
 Namespace rootNamespace = addedForward.iterator().next();
 Namespace elementNamespace = addedForward.iterator().next();
 assertNotEquals(rootNamespace, elementNamespace);
 }

 private Element createElement(String name, String namespaceURI) {
 return new Element(name, new QName(name, namespaceURI));
 }
}