package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.Comparatorutils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack namespaceStack;
 private final Namespace xmlNamespace = new Namespace("http://www.w3.org/XML/1998/namespace");
 private final Namespace myNamespace = new Namespace("myPrefix", "myURI");

 @Before
 public void setUp() {
 namespaceStack = new NamespaceStack();
 }

 @Test
 public void testConstructor() {
 NamespaceStack stack = new NamespaceStack();
 Assert.assertNotNull(stack.getScope());
 Assert.assertArrayEquals(Namespace.NO_NAMESPACE, stack.getScope());
 Assert.assertArrayEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
 Assert.assertEquals(-1, stack.depth);
 }

 @Test
 public void testPushElement() {
 namespaceStack.push(new Element("root").setName("root"));
 List<Namespace> namespacesToAdd = new ArrayList<>();
 Namespace[] newScope = NamespaceStack.checkNamespace(namespacesToAdd, xmlNamespace, namespaceStack.scope[namespaceStack.depth]);
 namespaceStack.push(new Element("child").setName("child"));
 namespaceStack.push(new Element("grandchild").setName("grandchild"));
 Namespace[] newScope2 = NamespaceStack.checkNamespace(namespacesToAdd, xmlNamespace, namespaceStack.scope[namespaceStack.depth]);
 Assert.assertEquals(3, namespaceStack.depth);
 Assert.assertArrayEquals(newScope2, namespaceStack.scope[namespaceStack.depth]);
 Assert.assertEquals(1, namespacesToAdd.size());
 Assert.assertEquals(xmlNamespace, namespacesToAdd.get(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushElementWithNegativeDepth() {
 namespaceStack.depth = -1;
 namespaceStack.push(xmlNamespace);
 }

 @Test
 public void testPushAttribute() {
 namespaceStack.push(new Attribute("attr", "value").setNamespace(xmlNamespace));
 List<Namespace> namespacesToAdd = new ArrayList<>();
 Namespace[] newScope = NamespaceStack.checkNamespace(namespacesToAdd, xmlNamespace, namespaceStack.scope[namespaceStack.depth]);
 namespaceStack.push(xmlNamespace);
 Assert.assertEquals(2, namespaceStack.depth);
 Assert.assertArrayEquals(newScope, namespaceStack.scope[namespaceStack.depth]);
 Assert.assertEquals(0, namespacesToAdd.size());
 }

 @Test
 public void testPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(xmlNamespace);
 stack.push(myNamespace);
 stack.pop();
 Assert.assertEquals(1, stack.depth);
 Assert.assertArrayEquals(new Namespace[]{xmlNamespace}, stack.getScope());
 }

 @Test
 public void testAddedForward() {
 namespaceStack.push(xmlNamespace);
 namespaceStack.push(myNamespace);
 Iterable<Namespace> addedForward = namespaceStack.addedForward();
 List<Namespace> namespaces = new ArrayList<>();
 namespaces.add(xmlNamespace);
 namespaces.add(myNamespace);
 Assert.assertEquals(namespaces, addedForward);
 }

 @Test
 public void testAddedReverse() {
 namespaceStack.push(xmlNamespace);
 namespaceStack.push(myNamespace);
 Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
 List<Namespace> namespacesReverse = new ArrayList<>();
 namespacesReverse.add(myNamespace);
 namespacesReverse.add(xmlNamespace);
 Assert.assertEquals(namespacesReverse, addedReverse);
 }

 @Test
 public void testIterator() {
 namespaceStack.push(xmlNamespace);
 namespaceStack.push(myNamespace);
 Iterator<Namespace> iterator = namespaceStack.iterator();
 List<Namespace> namespaces = new ArrayList<>();
 while (iterator.hasNext()) {
 namespaces.add(iterator.next());
 }
 List<Namespace> expectedNamespaces = Arrays.asList(xmlNamespace, myNamespace);
 Assert.assertEquals(expectedNamespaces, namespaces);
 }

 @Test
 public void testIsInScopeWithNullNamespace() {
 Assert.assertFalse(namespaceStack.isInScope(null));
 }

 @Test
 public void testIsInScopeWithSelfNamespace() {
 namespaceStack.push(xmlNamespace);
 Assert.assertTrue(namespaceStack.isInScope(xmlNamespace));
 }

 @Test
 public void testIsInScopeWithChildNamespace() {
 namespaceStack.push(xmlNamespace);
 namespaceStack.push(myNamespace);
 Assert.assertTrue(namespaceStack.isInScope(myNamespace));
 }

 @Test
 public void testIsInScopeWithParentNamespace() {
 namespaceStack.push(myNamespace);
 namespaceStack.push(xmlNamespace);
 Assert.assertTrue(namespaceStack.isInScope(xmlNamespace));
 }

 @Test
 public void testIsInScopeWithDescendantNamespace() {
 Namespace grandChildNamespace = new Namespace("grandchildPrefix", "grandchildURI");
 namespaceStack.push(xmlNamespace);
 namespaceStack.push(myNamespace);
 namespaceStack.push(new Element("grandchild").setName("grandchild"));
 List<Namespace> namespacesToAdd = new ArrayList<>();
 Namespace[] newScope = NamespaceStack.checkNamespace(namespacesToAdd, grandChildNamespace, namespaceStack.scope[namespaceStack.depth]);
 namespaceStack.push(grandChildNamespace);
 Assert.assertTrue(namespaceStack.isInScope(grandChildNamespace));
 }

 @Test(expected = IllegalStateException.class)
 public void testIsInScopeWithEmptyStack() {
 Assert.assertFalse(namespaceStack.isInScope(xmlNamespace));
 }

 @Test
 public void testBinarySearch() {
 Namespace[] data = new Namespace[10];
 Assert.assertEquals(-1, NamespaceStack.binarySearch(data, 0, 0, xmlNamespace));
 for (int i = 0; i < data.length; i++) {
 data[i] = new Namespace("prefix" + i, "uri" + i);
 }
 int index = 5;
 Assert.assertEquals(index, NamespaceStack.binarySearch(data, 0, data.length, new Namespace("prefix5", "uri5")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBinarySearchWithNegativeArraySize() {
 Namespace[] data = null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBinarySearchWithNullData() {
 Namespace[] data = null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBinarySearchWithNullKey() {
 Namespace[] data = new Namespace[10];
 }
}