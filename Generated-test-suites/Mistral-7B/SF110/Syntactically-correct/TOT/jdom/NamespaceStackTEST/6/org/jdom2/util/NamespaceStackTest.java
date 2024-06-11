package org.jdom2.util;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Attribute;
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
 Element element = new Element("root");
 stack.push(element);
 Namespace expectedNamespace = element.getNamespace();
 Namespace[] expectedScope = {expectedNamespace};
 assertArrayEquals(expectedScope, stack.getScope());
 stack.pop();
 assertArrayEquals(new Namespace[0], stack.getScope());
 }

 @Test
 public void testPushMultipleNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "prefix:localname");
 stack.push(element);
 Attribute attribute = new Attribute("attribute", "prefix2:localname");
 stack.push(attribute);
 Namespace[] expectedScope = {element.getNamespace(), attribute.getNamespace()};
 assertArrayEquals(expectedScope, stack.getScope());
 stack.pop();
 stack.pop();
 assertArrayEquals(new Namespace[0], stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 NamespaceStack stack = new NamespaceStack();
 Element nullElement = null;
 stack.push(nullElement);
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullAttribute() {
 NamespaceStack stack = new NamespaceStack();
 Attribute nullAttribute = null;
 stack.push(nullAttribute);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testGetScopeWithAddedNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "prefix:localname");
 stack.push(element);
 Attribute attribute = new Attribute("attribute", "prefix2:localname");
 stack.push(attribute);
 assertArrayEquals(new Namespace[]{element.getNamespace(), attribute.getNamespace()}, stack.getScope());
 }

 @Test
 public void testIsInScope() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "prefix:localname");
 stack.push(element);
 assertTrue(stack.isInScope(element.getNamespace()));
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 }

 @Test
 public void testPushAndPopMultipleTimes() {
 NamespaceStack stack = new NamespaceStack();
 Element element1 = new Element("root1", "prefix1:localname1");
 Attribute attribute1 = new Attribute("attr1", "prefix2:localname1");
 stack.push(element1);
 stack.push(attribute1);
 Element element2 = new Element("root2", "prefix2:localname2");
 stack.push(element2);
 Attribute attribute2 = new Attribute("attr2", "prefix3:localname2");
 stack.push(attribute2);
 Namespace[] expectedScope1 = {element1.getNamespace(), attribute1.getNamespace()};
 assertArrayEquals(expectedScope1, stack.getScope());
 stack.pop();
 Namespace[] expectedScope2 = {element2.getNamespace(), attribute2.getNamespace()};
 assertArrayEquals(expectedScope2, stack.getScope());
 stack.pop();
 assertArrayEquals(new Namespace[0], stack.getScope());
 }
}