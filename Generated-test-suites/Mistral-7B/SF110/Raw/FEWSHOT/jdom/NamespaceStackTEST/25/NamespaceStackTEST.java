package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.namespace.QName;
import org.jdom2.namespace.Namespace;
import org.junit.Test;
import java.util.*;

public class NamespaceStackTest {

 static Namespace[] createNamespaceArray(Namespace... namespaces) {
 return namespaces;
 }

 @Test
 public void testNamespaceStackEmpty() {
 NamespaceStack stack = new NamespaceStack();
 assertNull(stack.getScope());
 assertTrue(stack.isEmpty());
 }

 @Test
 public void testNamespaceStackInitialization() {
 NamespaceStack stack = new NamespaceStack(createNamespaceArray(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE));
 Namespace[] expectedScope = createNamespaceArray(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPush() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", new QName("test", "foo")));
 Namespace[] expectedScope = createNamespaceArray(Namespace.XML_NAMESPACE, new Namespace("test", "foo"));
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushMultiple() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", new QName("test1", "foo1")));
 stack.push(new Element("child", new QName("test2", "foo2")));
 Namespace[] expectedScope = createNamespaceArray(Namespace.XML_NAMESPACE, new Namespace("test1", "foo1"), new Namespace("test2", "foo2"));
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", new QName("test", "foo")));
 Namespace[] expectedScope = createNamespaceArray(Namespace.XML_NAMESPACE);
 stack.pop();
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushElementWithAdditionalNamespaces() {
 Element element = new Element("root", new QName("test", "foo"));
 element.setAdditionalNamespaces(createNamespaceArray(new Namespace("test1", "foo1"), new Namespace("test2", "foo2")));
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 Namespace[] expectedScope = createNamespaceArray(new Namespace("test1", "foo1"), new Namespace("test2", "foo2"), Namespace.XML_NAMESPACE, new Namespace("test", "foo"));
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushAttribute() {
 Attribute attribute = new Attribute("attr", "value", new QName("test", "foo"));
 NamespaceStack stack = new NamespaceStack();
 stack.push(attribute);
 Namespace[] expectedScope = createNamespaceArray(Namespace.XML_NAMESPACE, new Namespace("test", "foo"));
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackPushAttributeWithAdditionalNamespaces() {
 Attribute attribute = new Attribute("attr", "value", new QName("test", "foo"));
 attribute.setNamespace(new Namespace("test1", "foo1"));
 NamespaceStack stack = new NamespaceStack();
 stack.push(attribute);
 Namespace[] expectedScope = createNamespaceArray(new Namespace("test1", "foo1"), Namespace.XML_NAMESPACE, new Namespace("test", "foo"));
 Namespace[] actualScope = stack.getScope();
 assertArrayEquals(expectedScope, actualScope);
 }

 @Test
 public void testNamespaceStackIterator() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", new QName("test", "foo")));
 stack.push(new Element("child", new QName("test2", "foo2")));
 Iterator<Namespace> iterator = stack.iterator();
 List<Namespace> expected = new ArrayList<>();
 expected.add(Namespace.XML_NAMESPACE);
 expected.add(new Namespace("test", "foo"));
 expected.add(new Namespace("test2", "foo2"));
 List<Namespace> actual = new ArrayList<>();
 while (iterator.hasNext()) {
 actual.add(iterator.next());
 }
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }
}