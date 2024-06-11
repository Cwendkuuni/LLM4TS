package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.Element;
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
 public void testBasicPushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] expected = {Namespace.XML_NAMESPACE};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 stack.pop();
 assertTrue(stack.isEmpty());
 }

 @Test
 public void testComplexPushAndPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", "prefix1:element1"));
 stack.push(new Element("root", "prefix2:element2"));
 stack.push(new Element("root", "prefix1:element3"));
 Namespace[] expected = {Namespace.XML_NAMESPACE, Namespace.getNamespace("prefix1"), Namespace.getNamespace("prefix2")};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 stack.pop();
 stack.pop();
 Namespace[] expectedPop = {Namespace.XML_NAMESPACE, Namespace.getNamespace("prefix1")};
 Namespace[] actualPop = stack.getScope();
 assertArrayEquals(expectedPop, actualPop);
 }

 @Test(expected = IllegalStateException.class)
 public void testOverPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Element("root"));
 stack.pop();
 stack.pop();
 stack.pop();
 }

 @Test
 public void testAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", "prefix1:element1"));
 stack.push(new Element("root", "prefix2:element2", "prefix3:element3"));
 Namespace[] expected = {Namespace.XML_NAMESPACE, Namespace.getNamespace("prefix1"), Namespace.getNamespace("prefix2"), Namespace.getNamespace("prefix3")};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAttributesNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "prefix1:element", new Namespace("prefix2"));
 stack.push(element);
 Namespace[] expected = {Namespace.XML_NAMESPACE, Namespace.getNamespace("prefix1"), Namespace.getNamespace("prefix2")};
 Namespace[] actual = stack.getScope();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyAddedNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", "prefix1:element1"));
 assertEmpty(stack.addedForward());
 }

 @Test
 public void testAddedNamespacesForward() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", "prefix1:element1"));
 stack.push(new Element("root", "prefix2:element2"));
 stack.push(new Element("root", "prefix1:element3"));
 List<Namespace> expected = Arrays.asList(Namespace.getNamespace("prefix2"), Namespace.getNamespace("prefix1"));
 List<Namespace> actual = new ArrayList<>(stack.addedForward());
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }

 @Test
 public void testAddedNamespacesReverse() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", "prefix1:element1"));
 stack.push(new Element("root", "prefix2:element2"));
 stack.push(new Element("root", "prefix1:element3"));
 List<Namespace> expected = Arrays.asList(Namespace.getNamespace("prefix1"), Namespace.getNamespace("prefix2"));
 List<Namespace> actual = new ArrayList<>(stack.addedReverse());
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }
}