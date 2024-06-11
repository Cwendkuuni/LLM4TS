package org.jdom2.util;

import org.jdom2.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Test
 public void testNamespaceStackConstruction() {
 stack = new NamespaceStack();
 assertNotNull(stack);
 assertArrayEquals(NamespaceStack.DEFAULTSEED, stack.getScope());
 }

 @Test
 public void testPushElement() {
 stack = new NamespaceStack();
 Element element = new Element("test");
 stack.push(element);
 Namespace expectedNamespace = new Namespace("test", "http://test.com");
 assertArrayEquals(new Namespace[]{expectedNamespace}, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushElementWithNegativeDepth() {
 stack = new NamespaceStack();
 stack.depth = -1;
 stack.push(new Element("test"));
 }

 @Test
 public void testPushElementWithMultipleNamespaces() {
 stack = new NamespaceStack();
 Element element = new Element("test", new QName("test", "http://test.com"));
 stack.push(element);
 Namespace mns = element.getNamespace();
 Namespace ns1 = new Namespace("ns1", "http://ns1.com");
 Namespace ns2 = new Namespace("ns2", "http://ns2.com");
 stack.push(ns1);
 stack.push(ns2);
 Namespace[] expectedScope = new Namespace[]{mns, ns2, ns1};
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushAttribute() {
 stack = new NamespaceStack();
 Attribute attribute = new Attribute("test", "test");
 stack.push(attribute);
 Namespace expectedNamespace = new Namespace("##default", "");
 assertArrayEquals(new Namespace[]{expectedNamespace}, stack.getScope());
 }

 @Test
 public void testPop() {
 stack = new NamespaceStack();
 Element element = new Element("test");
 stack.push(element);
 stack.pop();
 assertArrayEquals(NamespaceStack.DEFAULTSEED, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWithNegativeDepth() {
 stack = new NamespaceStack();
 stack.depth = -1;
 stack.pop();
 }

 @Test
 public void testAddedForward() {
 stack = new NamespaceStack();
 Element element = new Element("test");
 stack.push(element);
 Namespace mns = element.getNamespace();
 Namespace ns1 = new Namespace("ns1", "http://ns1.com");
 stack.push(ns1);
 Iterable<Namespace> expectedAdded = new NamespaceIterable(new Namespace[]{mns, ns1}, true);
 Iterable<Namespace> actualAdded = stack.addedForward();
 assertEquals(expectedAdded, actualAdded);
 }

 @Test
 public void testAddedReverse() {
 stack = new NamespaceStack();
 Element element = new Element("test");
 stack.push(element);
 Namespace mns = element.getNamespace();
 Namespace ns1 = new Namespace("ns1", "http://ns1.com");
 stack.push(ns1);
 Iterable<Namespace> expectedAdded = new NamespaceIterable(new Namespace[]{ns1, mns}, false);
 Iterable<Namespace> actualAdded = stack.addedReverse();
 assertEquals(expectedAdded, actualAdded);
 }

 @Test
 public void testIsInScope() {
 stack = new NamespaceStack();
 Element element = new Element("test");
 stack.push(element);
 Namespace mns = element.getNamespace();
 assertTrue(stack.isInScope(mns));
 }

 @Test
 public void testIsInScopeWithDifferentNamespace() {
 stack = new NamespaceStack();
 Element element = new Element("test");
 stack.push(element);
 Namespace differentNamespace = new Namespace("different", "http://different.com");
 assertFalse(stack.isInScope(differentNamespace));
 }

 @Test
 public void testBinarySearch() {
 Namespace[] data = {new Namespace("a", "a"), new Namespace("b", "b"), new Namespace("c", "c"), new Namespace("d", "d")};
 int left = 0;
 int right = data.length - 1;
 Namespace key = new Namespace("c", "c");
 int expectedResult = 1;
 int result = NamespaceStack.binarySearch(data, left, right, key);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testBinarySearchWithEmptyArray() {
 Namespace[] data = {};
 int left = 0;
 int right = 0;
 Namespace key = new Namespace("c", "c");
 int expectedResult = -1;
 int result = NamespaceStack.binarySearch(data, left, right, key);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testBinarySearchWithNegativeValues() {
 Namespace[] data = {new Namespace("a", "a"), new Namespace("b", "b"), new Namespace("c", "c"), new Namespace("d", "d")};
 int left = 1;
 int right = data.length - 1;
 Namespace key = new Namespace("a", "a");
 int expectedResult = 0;
 int result = NamespaceStack.binarySearch(data, left, right, key);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testBinarySearchWithNullArray() {
 int left = 0;
 int right = 0;
 Namespace key = null;
 int expectedResult = -1;
 int result = NamespaceStack.binarySearch(null, left, right, key);
 assertEquals(expectedResult, result);
 }

 @Test(expected = NullPointerException.class)
 public void testBinarySearchWithNullKey() {
 Namespace[] data = {new Namespace("a", "a"), new Namespace("b", "b"), new Namespace("c", "c"), new Namespace("d", "d")};
 int left = 0;
 int right = data.length - 1;
 Namespace key = null;
 }
}