package org.jdom2.util;

import org.jdom2.*;
import org.jdom2.internal.Namespaces;
import org.jdom2.namespace.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;

 @Before
 public void setUp() {
 Namespace xmlNs = Namespaces.getXmlNamespace();
 Namespace ns1 = new Namespace("test", "http://test.com");
 Namespace ns2 = new Namespace("test2", "http://test2.com");
 element = new Element("root", "", xmlNs);
 stack = new NamespaceStack();
 }

 @Test
 public void testConstructor() {
 NamespaceStack stack = new NamespaceStack();
 Assert.assertNull(stack.getScope());
 Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.addedForward().toArray(new Namespace[0]));
 }

 @Test
 public void testConstructorWithSeed() {
 stack = new NamespaceStack(new Namespace[]{Namespaces.getXmlNamespace(), Namespace.NO_NAMESPACE});
 Namespace[] scope = stack.getScope();
 Assert.assertNotNull(scope);
 Assert.assertEquals(2, scope.length);
 Assert.assertEquals(Namespaces.getXmlNamespace(), scope[0]);
 Assert.assertEquals(Namespace.NO_NAMESPACE, scope[1]);
 }

 @Test
 public void testPushElement() {
 stack.push(element);
 Namespace[] scope = stack.getScope();
 List<Namespace> toAdd = new ArrayList<>(8);
 Namespace[] newScope = checkNamespaces(toAdd, ns1, scope);
 Assert.assertArrayEquals(newScope, scope);
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 stack.push(element);
 stack.push(element);
 List<Namespace> toAdd = new ArrayList<>(8);
 Namespace[] newScope = checkNamespaces(toAdd, ns2, scope);
 Assert.assertArrayEquals(newScope, scope);
 }

 @Test
 public void testPushAttribute() {
 Attribute attribute = new Attribute("attr", "", ns1);
 stack.push(attribute);
 Namespace[] scope = stack.getScope();
 List<Namespace> toAdd = new ArrayList<>(1);
 Namespace[] newScope = checkNamespaces(toAdd, ns1, scope);
 Assert.assertArrayEquals(newScope, scope);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 stack.pop();
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.push(element);
 stack.pop();
 Namespace[] scope = stack.getScope();
 Assert.assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespaces.getXmlNamespace()}, scope);
 }

 @Test
 public void testAddedForward() {
 stack.push(element);
 stack.push(element);
 List<Namespace> added = new ArrayList<>(8);
 for (Namespace namespace : stack.addedForward()) {
 added.add(namespace);
 }
 List<Namespace> expected = Arrays.asList(ns1, ns2);
 Assert.assertThat(added, is(expected));
 }

 @Test
 public void testAddedReverse() {
 stack.push(element);
 stack.push(element);
 List<Namespace> added = new ArrayList<>(8);
 for (Namespace namespace : stack.addedReverse()) {
 added.add(namespace);
 }
 List<Namespace> expected = Arrays.asList(ns2, ns1);
 Assert.assertThat(added, is(expected));
 }

 @Test
 public void testIsInScope() {
 stack.push(element);
 stack.push(element);
 Assert.assertTrue(stack.isInScope(ns1));
 Assert.assertTrue(stack.isInScope(ns2));
 }

 @Test
 public void testIterator() {
 stack.push(element);
 stack.push(element);
 List<Namespace> expected = Arrays.asList(ns1, ns2);
 List<Namespace> actual = new ArrayList<>(8);
 for (Namespace namespace : stack) {
 actual.add(namespace);
 }
 Assert.assertThat(actual, is(expected));
 }

 private Namespace[] checkNamespaces(List<Namespace> toAdd, Namespace namespace, Namespace[] scope) {
 Namespace[] newScope = NamespaceStack.checkNamespace(toAdd, namespace, scope);
 return newScope;
 }

}