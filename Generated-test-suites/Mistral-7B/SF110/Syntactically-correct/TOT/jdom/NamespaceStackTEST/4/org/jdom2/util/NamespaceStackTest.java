package org.jdom2.util;

import org.jdom2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertNull(stack.getScope());
 }

 @Test
 public void testPushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.XML_NAMESPACE_2));
 stack.pop();
 Namespace[] scope = stack.getScope();
 assertEquals(1, scope.length);
 assertEquals(Namespace.XML_NAMESPACE, scope[0]);
 }

 @Test
 public void testPushMultipleElementsWithSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 Namespace[] scope = stack.getScope();
 assertEquals(1, scope.length);
 assertEquals(Namespace.XML_NAMESPACE, scope[0]);
 }

 @Test
 public void testPushMultipleElementsWithDifferentNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root1", Namespace.XML_NAMESPACE));
 stack.push(new Element("root2", Namespace.XML_NAMESPACE_2));
 Namespace[] scope = stack.getScope();
 assertEquals(2, scope.length);
 assertEquals(Namespace.XML_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE_2, scope[1]);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPushMultipleElementsWithSameNamespaceInReverseOrder() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("element2", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("element1", Namespace.XML_NAMESPACE_2));
 Namespace[] scope = stack.getScope();
 assertEquals(2, scope.length);
 assertEquals(Namespace.XML_NAMESPACE_2, scope[1]);
 assertEquals(Namespace.XML_NAMESPACE_2, scope[0]);
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", Namespace.XML_NAMESPACE);
 stack.push(element);
 List<Namespace> additionalNamespaces = Arrays.asList(Namespace.XML_NAMESPACE_1, Namespace.XML_NAMESPACE_3);
 stack.push(new Element("element", Namespace.XML_NAMESPACE, additionalNamespaces.toArray(new Namespace[0])));
 Namespace[] scope = stack.getScope();
 assertEquals(3, scope.length);
 assertEquals(Namespace.XML_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE_1, scope[1]);
 assertEquals(Namespace.XML_NAMESPACE_3, scope[2]);
 }

 @Test
 public void testPushAttributeWithAdditionalNamespace() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", Namespace.XML_NAMESPACE);
 Attribute attribute = new Attribute("attr", "value", Namespace.XML_NAMESPACE_1);
 root.addNamespaceDeclaration(Namespace.XML_NAMESPACE_1.getNamespaceURI());
 stack.push(root);
 stack.push(attribute);
 Namespace[] scope = stack.getScope();
 assertEquals(2, scope.length);
 assertEquals(Namespace.XML_NAMESPACE, scope[0]);
 assertEquals(Namespace.XML_NAMESPACE_1, scope[1]);
 }
}