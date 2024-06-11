package org.jdom2.util;

import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testNamespaceStackEmptyConstruction() {
 stack = new NamespaceStack();
 assertEmptyStack(stack);
 }

 @Test
 public void testNamespaceStackCustomConstruction() {
 Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 stack = new NamespaceStack(seed);
 assertScopeEquals(seed, stack.getScope());
 }

 @Test
 public void testPushElement() {
 stack = new NamespaceStack();
 pushTest("test.xml", stack);
 }

 @Test
 public void testPushAttribute() {
 stack = new NamespaceStack();
 pushTest("test.xml", stack);
 pushAttributeTest("test.xml", stack);
 }

 @Test
 public void testPushMultipleElementsAndAttributes() {
 stack = new NamespaceStack();
 pushTest("test1.xml", stack);
 pushAttributeTest("test1.xml", stack);
 pushTest("test2.xml", stack);
 pushAttributeTest("test2.xml", stack);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack = new NamespaceStack();
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenNotFull() {
 stack = new NamespaceStack();
 stack.push("test.xml");
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenDepthNegative() {
 stack = new NamespaceStack();
 stack.depth = -1;
 stack.pop();
 }

 @Test
 public void testGetScope() {
 stack = new NamespaceStack();
 pushTest("test.xml", stack);
 pushAttributeTest("test.xml", stack);
 assertScopeEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE}, stack.getScope());
 }

 @Test
 public void testIsInScope() {
 stack = new NamespaceStack();
 pushTest("test.xml", stack);
 pushAttributeTest("test.xml", stack);
 Assert.assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 Assert.assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 Assert.assertFalse(stack.isInScope(new Namespace("foo", "bar")));
 }

 @Test
 public void testAddedForward() {
 stack = new NamespaceStack();
 pushTest("test.xml", stack);
 pushAttributeTest("test.xml", stack);
 Iterable<Namespace> addedForward = stack.addedForward();
 Assert.assertEquals(Arrays.asList(Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE), toList(addedForward));
 }

 @Test
 public void testAddedReverse() {
 stack = new NamespaceStack();
 pushTest("test.xml", stack);
 pushAttributeTest("test.xml", stack);
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Assert.assertEquals(Arrays.asList(Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE), toList(addedReverse));
 }

 @Test
 public void testIterator() {
 stack = new NamespaceStack();
 pushTest("test.xml", stack);
 pushAttributeTest("test.xml", stack);
 Iterator<Namespace> iterator = stack.iterator();
 List<Namespace> list = new ArrayList<>();
 while (iterator.hasNext()) {
 list.add(iterator.next());
 }
 Assert.assertEquals(Arrays.asList(Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE), list);
 }

 private void pushTest(String xmlFilename, NamespaceStack stack) {
 Element element = new Element("root", Namespace.getXmlNamespace());
 stack.push(element);
 }

 private void pushAttributeTest(String xmlFilename, NamespaceStack stack) {
 Attribute attribute = new Attribute("key", "value", Namespace.getXmlNamespace());
 stack.push(attribute);
 }

 private <T> List<T> toList(Iterable<T> iterable) {
 List<T> list = new ArrayList<>();
 for (T t : iterable) {
 list.add(t);
 }
 return list;
 }

 private void assertEmptyStack(NamespaceStack stack) {
 assertNotNull(stack);
 Assert.assertArrayEquals(new Namespace[0], stack.getScope());
 Assert.assertTrue(stack.isEmpty());
 }

 private void assertScopeEquals(Namespace[] expected, Namespace[] actual) {
 assertNotNull(actual);
 Assert.assertArrayEquals(expected, actual);
 }
}