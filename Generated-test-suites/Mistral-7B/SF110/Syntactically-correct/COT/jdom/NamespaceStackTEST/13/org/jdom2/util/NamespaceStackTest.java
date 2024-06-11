package org.jdom2.util;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.internal.Namespaces;
import org.jdom2.namespace.Namespace;
import org.junit.Test;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testConstructor() {
 stack = new NamespaceStack();
 assertNotNull(stack);
 }

 @Test
 public void testConstructorWithSeed() {
 stack = new NamespaceStack(new Namespace[] { Namespaces.XML_NAMESPACE, Namespaces.A11N_NAMESPACE });
 assertNotNull(stack);
 }

 @Test
 public void testPushElement() {
 Element element = new Element("root");
 stack = new NamespaceStack();
 stack.push(element);
 List<Namespace> expectedNamespaces = new ArrayList<Namespace>();
 expectedNamespaces.add(Namespace.XML_NAMESPACE);
 expectedNamespaces.addAll(element.getNamespaceDeclarations());
 Namespace[] newScope = checkNamespaces(expectedNamespaces.toArray(new Namespace[0]), Namespace.XML_NAMESPACE, stack.scope[stack.depth]);
 stack.pushStack(Namespace.XML_NAMESPACE, newScope, expectedNamespaces);
 verifyNamespaces(stack.scope[stack.depth]);
 }

 @Test
 public void testPushAttribute() {
 Attribute attribute = new Attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 stack = new NamespaceStack();
 stack.push(attribute);
 List<Namespace> expectedNamespaces = new ArrayList<Namespace>(2);
 expectedNamespaces.add(Namespace.XML_NAMESPACE);
 expectedNamespaces.add(attribute.getNamespace());
 Namespace[] newScope = checkNamespaces(expectedNamespaces.toArray(new Namespace[0]), Namespace.XML_NAMESPACE, stack.scope[stack.depth]);
 stack.pushStack(Namespace.XML_NAMESPACE, newScope, expectedNamespaces);
 verifyNamespaces(stack.scope[stack.depth]);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPop() {
 stack = new NamespaceStack();
 Element element = new Element("root");
 stack.push(element);
 stack.push(element);
 stack.pop();
 verifyNamespaces(stack.scope[stack.depth]);
 }

 @Test
 public void testAddedForward() {
 stack = new NamespaceStack();
 Element element = new Element("root");
 stack.push(element);
 Iterable<Namespace> expectedNamespaces = stack.addedForward();
 Iterable<Namespace> actualNamespaces = expectedNamespaces;
 for (Namespace namespace : expectedNamespaces) {
 assertEquals(namespace, actualNamespaces.iterator().next());
 actualNamespaces = actualNamespaces.iterator().next();
 }
 }

 @Test
 public void testAddedReverse() {
 stack = new NamespaceStack();
 Element element = new Element("root");
 stack.push(element);
 Iterable<Namespace> expectedNamespaces = stack.addedReverse();
 Iterable<Namespace> actualNamespaces = expectedNamespaces;
 for (Namespace namespace : expectedNamespaces) {
 assertEquals(namespace, actualNamespaces.iterator().next());
 actualNamespaces = actualNamespaces.iterator().next();
 }
 }

 @Test
 public void testIsInScope() {
 stack = new NamespaceStack();
 Namespace xmlNs = Namespace.XML_NAMESPACE;
 Namespace ns1 = new Namespace("ns1", "http://example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://example.com/ns2");
 stack.push(ns1);
 stack.push(xmlNs);
 stack.push(ns2);
 assertTrue(stack.isInScope(xmlNs));
 assertTrue(stack.isInScope(ns1));
 assertTrue(stack.isInScope(ns2));
 }

 @Test
 public void testIsInScopeWithEmptyStack() {
 stack = new NamespaceStack();
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 }

 @Test
 public void testIsInScopeWithNamespaceNotInStack() {
 stack = new NamespaceStack();
 Namespace ns = new Namespace("ns1", "http://example.com/ns1");
 assertFalse(stack.isInScope(ns));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 stack = new NamespaceStack();
 stack.push(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullAttribute() {
 stack = new NamespaceStack();
 stack.push(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPushWithNullNamespace() {
 stack = new NamespaceStack();
 stack.push(null);
 }

 private void verifyNamespaces(Namespace[] expectedNamespaces) {
 Namespace[] actualNamespaces = stack.scope[stack.depth];
 assertNotNull(actualNamespaces);
 assertEquals(expectedNamespaces.length, actualNamespaces.length);
 for (int i = 0; i < expectedNamespaces.length; i++) {
 assertEquals(expectedNamespaces[i], actualNamespaces[i]);
 }
 }

 private Namespace[] checkNamespaces(Namespace[] expectedNamespaces, Namespace prefix, Namespace[] currentScope) {
 List<Namespace> namespaces = new ArrayList<Namespace>();
 for (Namespace namespace : expectedNamespaces) {
 if (namespace.getPrefix().equals(prefix.getPrefix())) {
 namespaces.add(namespace);
 }
 else {
 Namespace[] newScope = checkNamespaces(new Namespace[] { namespace }, namespace, currentScope);
 namespaces.addAll(Arrays.asList(newScope));
 }
 }
 return namespaces.toArray(new Namespace[namespaces.size()]);
 }

}