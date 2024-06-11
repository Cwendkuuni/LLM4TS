package org.jdom2.util;

import org.jdom2.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack namespaceStack;
 private Element element;

 @Before
 public void setup() {
 namespaceStack = new NamespaceStack();
 element = new Element("testElement");
 }

 @Test
 public void testEmptyStack() {
 assertNull(namespaceStack.getScope());
 assertTrue(namespaceStack.isEmpty());
 }

 @Test
 public void testPushElement() {
 Namespace namespace = new Namespace("testNamespace");
 namespaceStack.push(element);
 Namespace[] expectedScope = {namespace, Namespace.XML_NAMESPACE};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 Namespace additionalNamespace1 = new Namespace("additionalNamespace1");
 Namespace additionalNamespace2 = new Namespace("additionalNamespace2");
 element.setAdditionalNamespaces(Arrays.asList(additionalNamespace1, additionalNamespace2));
 namespaceStack.push(element);
 Namespace[] expectedScope = {Namespace.XML_NAMESPACE, additionalNamespace1, additionalNamespace2, namespace};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testPushAttribute() {
 Namespace namespace = new Namespace("testNamespace");
 Namespace attributeNamespace = new Namespace("attributeNamespace");
 Attribute attribute = new Attribute("testAttribute", "testValue", attributeNamespace);
 namespaceStack.push(attribute);
 Namespace[] expectedScope = {namespace, Namespace.XML_NAMESPACE};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testPushAttributeWithAdditionalNamespaces() {
 Namespace additionalNamespace = new Namespace("additionalNamespace");
 Attribute attribute = new Attribute("testAttribute", "testValue", additionalNamespace);
 namespaceStack.push(attribute);
 Namespace[] expectedScope = {Namespace.XML_NAMESPACE, additionalNamespace};
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testPushMultipleElementsAndAttributes() {
 Namespace namespace1 = new Namespace("namespace1");
 Namespace namespace2 = new Namespace("namespace2");
 Namespace additionalNamespace1 = new Namespace("additionalNamespace1");
 Namespace additionalNamespace2 = new Namespace("additionalNamespace2");
 element.setAdditionalNamespaces(Arrays.asList(additionalNamespace1, additionalNamespace2));
 Element element2 = new Element("testElement2");
 Namespace namespace3 = new Namespace("namespace3");
 Element element3 = new Element("testElement3");
 Namespace namespace4 = new Namespace("namespace4");
 List<Namespace> namespacesToAdd = new ArrayList<>();
 namespaceStack.push(element);
 namespaceStack.push(element2);
 namespaceStack.push(attribute);
 namespaceStack.push(attribute);
 namespaceStack.push(element3);
 namespaceStack.push(namespace3);
 namespaceStack.push(namespace1);
 namespaceStack.push(namespace2);
 namespacesToAdd.add(namespace1);
 namespacesToAdd.add(namespace2);
 for (Element e : Arrays.asList(element, element2, element3)) {
 if (e.hasAdditionalNamespaces()) {
 for (Namespace ns : e.getAdditionalNamespaces()) {
 namespacesToAdd.add(ns);
 }
 }
 }
 Namespace[] expectedScope = new Namespace[namespaceStack.depth() + 1];
 System.arraycopy(namespaceStack.getScope(namespaceStack.depth()), 0, expectedScope, 0, expectedScope.length);
 NamespaceIterable namespaceIterable = new NamespaceIterable(namespacesToAdd.toArray(new Namespace[namespacesToAdd.size()]), true);
 Iterator<Namespace> namespaceIterator = namespaceIterable.iterator();
 int i = 0;
 while (namespaceIterator.hasNext()) {
 expectedScope[i++] = namespaceIterator.next();
 }
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testPop() {
 assertFalse(namespaceStack.isEmpty());
 namespaceStack.pop();
 assertTrue(namespaceStack.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testOverPop() {
 namespaceStack.pop();
 namespaceStack.pop();
 }

 @Test
 public void testAddedForwardIterator() {
 Namespace namespace1 = new Namespace("namespace1");
 Namespace namespace2 = new Namespace("namespace2");
 List<Namespace> namespacesToAdd = new ArrayList<>();
namespaceStack.push(element);
 namespaceStack.push(attribute);
 namespaceStack.push(namespace1);
 namespaceStack.push(namespace2);
 namespacesToAdd.addAll(Arrays.asList(namespace1, namespace2));
 NamespaceIterable namespaceIterable = new NamespaceIterable(namespaceStack.addedForward().toArray(new Namespace[namespaceStack.size()]), true);
 Iterator<Namespace> namespaceIterator = namespaceIterable.iterator();
 Namespace namespace;
 while ((namespace = namespaceIterator.next()) != null) {
 assertEquals(namespacesToAdd.remove(namespace), namespace);
 }
 assertTrue(namespacesToAdd.isEmpty());
 }

 @Test
 public void testAddedReverseIterator() {
 Namespace namespace1 = new Namespace("namespace1");
 Namespace namespace2 = new Namespace("namespace2");
 List<Namespace> namespacesToAdd = new ArrayList<>();
namespaceStack.push(element);
 namespaceStack.push(namespace2);
 namespaceStack.push(namespace1);
 namespaceStack.push(attribute);
 namespacesToAdd.addAll(Arrays.asList(namespace1, namespace2));
 NamespaceIterable namespaceIterable = new NamespaceIterable(namespaceStack.addedReverse().toArray(new Namespace[namespaceStack.size()]), false);
 Iterator<Namespace> namespaceIterator = namespaceIterable.iterator();
 Namespace namespace;
 while ((namespace = namespaceIterator.next()) != null) {
 assertEquals(namespacesToAdd.remove(namespace), namespace);
 }
 assertTrue(namespacesToAdd.isEmpty());
 }

 @Test
 public void testIsInScope() {
 Namespace namespace1 = new Namespace("namespace1");
 Namespace namespace2 = new Namespace("namespace2");
 namespaceStack.push(element);
 namespaceStack.push(namespace1);
 namespaceStack.push(namespace2);
 assertTrue(namespaceStack.isInScope(namespace1));
 assertTrue(namespaceStack.isInScope(namespace2));
 }

 @Test
 public void testIsNotInScope() {
 Namespace namespace = new Namespace("namespace");
 namespaceStack.push(element);
 assertFalse(namespaceStack.isInScope(namespace));
 }
}