package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.Namespace;
import java.util.*;

public class NamespaceStackTest {

 private static final Namespace DEFAULT_NAMESPACE = Namespace.getNamespace("http://www.w3.org/2000/xmlns/");
 private static final Namespace XML_NAMESPACE = Namespace.getNamespace("http://www.w3.org/XML/1998/namespace");

 @Test
 public void testDefaultConstructor() {
 NamespaceStack stack = new NamespaceStack();
 assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, XML_NAMESPACE}, stack.getScope());
 }

 @Test
 public void testConstructorWithNamespaces() {
 NamespaceStack stack = new NamespaceStack(new Namespace[]{Namespace.NO_NAMESPACE, XML_NAMESPACE});
 assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, XML_NAMESPACE}, stack.getScope());
 }

 @Test
 public void testPushElement() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("foo", "bar");
 stack.push(element);
 Namespace[] expectedScope = {Namespace.NO_NAMESPACE, XML_NAMESPACE, element.getNamespace()};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 Element element = new Element("foo", "bar");
 List<Namespace> additionalNamespaces = new ArrayList<>();
 additionalNamespaces.add(new Namespace("http://example.com/", "ex"));
 element.setAdditionalNamespaces(additionalNamespaces);
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 Namespace[] expectedScope = {Namespace.NO_NAMESPACE, XML_NAMESPACE, element.getNamespace(), additionalNamespaces.get(0)};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushAttribute() {
 Attribute attribute = new Attribute("foo", "bar");
 NamespaceStack stack = new NamespaceStack();
 stack.push(attribute);
 Namespace[] expectedScope = {Namespace.NO_NAMESPACE, XML_NAMESPACE, attribute.getNamespace()};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushAttributeWithAdditionalNamespaces() {
 Attribute attribute = new Attribute("foo", "bar");
 List<Namespace> additionalNamespaces = new ArrayList<>();
 additionalNamespaces.add(new Namespace("http://example.com/", "ex"));
 attribute.setNamespace(additionalNamespaces.get(0));
 NamespaceStack stack = new NamespaceStack();
 stack.push(attribute);
 Namespace[] expectedScope = {Namespace.NO_NAMESPACE, XML_NAMESPACE, additionalNamespaces.get(0)};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleElementsAndAttributes() {
 Element element1 = new Element("foo", "bar1");
 List<Namespace> additionalNamespaces1 = new ArrayList<>();
 additionalNamespaces1.add(new Namespace("http://example1.com/", "ex1"));
 element1.setAdditionalNamespaces(additionalNamespaces1);
 Element element2 = new Element("foo", "bar2");
 List<Namespace> additionalNamespaces2 = new ArrayList<>();
 additionalNamespaces2.add(new Namespace("http://example2.com/", "ex2"));
 element2.setAdditionalNamespaces(additionalNamespaces2);
 Attribute attribute1 = new Attribute("foo", "bar1");
 List<Namespace> additionalNamespaces3 = new ArrayList<>();
 additionalNamespaces3.add(new Namespace("http://example3.com/", "ex3"));
 attribute1.setNamespace(additionalNamespaces3.get(0));
 Attribute attribute2 = new Attribute("foo", "bar2");
 List<Namespace> additionalNamespaces4 = new ArrayList<>();
 additionalNamespaces4.add(new Namespace("http://example4.com/", "ex4"));
 attribute2.setNamespace(additionalNamespaces4.get(0));
 NamespaceStack stack = new NamespaceStack();
 stack.push(element1);
 stack.push(element2);
 stack.push(attribute1);
 stack.push(attribute2);
 Namespace[] expectedScope = {Namespace.NO_NAMESPACE, XML_NAMESPACE, additionalNamespaces1.get(0), additionalNamespaces2.get(0), additionalNamespaces3.get(0), additionalNamespaces4.get(0)};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPop() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("foo", "bar");
 stack.push(element);
 stack.pop();
 Namespace[] expectedScope = {Namespace.NO_NAMESPACE, XML_NAMESPACE};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testGetAddedForward() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("foo", "bar");
 stack.push(element);
 Iterable<Namespace> addedNamespaces = stack.addedForward();
 List<Namespace> expectedNamespaces = Arrays.asList(element.getNamespace());
 assertEquals(expectedNamespaces, addedNamespaces);
 }

 @Test
 public void testGetAddedReverse() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("foo", "bar");
 stack.push(element);
 Iterable<Namespace> addedNamespaces = stack.addedReverse();
 List<Namespace> expectedNamespaces = Arrays.asList(element.getNamespace());
 assertEquals(expectedNamespaces, addedNamespaces);
 }

 @Test
 public void testIsInScope() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("foo", "bar");
 stack.push(element);
 assertTrue(stack.isInScope(element.getNamespace()));
 }

 @Test
 public void testIsInScopeDifferentNamespace() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("foo", "bar");
 stack.push(element);
 assertFalse(stack.isInScope(new Namespace("http://example.com/", "ex")));
 }

 @Test
 public void testIsInScopeEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertFalse(stack.isInScope(Namespaces.NO_NAMESPACE));
 }

 @Test
 public void testIsInScopeEmptyNamespace() {
 NamespaceStack stack = new NamespaceStack();
 Namespace namespace = new Namespace("", "");
 assertFalse(stack.isInScope(namespace));
 }
}