package org.jdom2.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.NamespaceStack;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.NamespaceIterable;

import org.junit.Before;
import org.junit.Test;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Namespace xmlNamespace;
 private Namespace customNamespace;

 @Before
 public void setUp() {
 xmlNamespace = Namespace.XML_NAMESPACE;
 customNamespace = new Namespace("custom", "http://example.com/");
 }

 @Test
 public void testEmptyNamespaceStack() {
 NamespaceStack emptyStack = new NamespaceStack();
 assertTrue(emptyStack.isEmpty());
 assertNull(emptyStack.getScope());
 assertSame(emptyStack.EMPTYITER, emptyStack.addedForward());
 assertSame(emptyStack.EMPTYITER, emptyStack.addedReverse());
 }

 @Test
 public void testNamespaceStackWithDefaultSeed() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(xmlNamespace);
 Namespace[] expectedScope = new Namespace[] { xmlNamespace, Namespace.NO_NAMESPACE };
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testNamespaceStackWithCustomSeed() {
 NamespaceStack stack = new NamespaceStack(new Namespace[] { customNamespace, xmlNamespace });
 Namespace[] expectedScope = new Namespace[] { customNamespace, xmlNamespace };
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 List<Namespace> toAdd = new ArrayList<>();
 Namespace[] newScope = checkNamespaces(toAdd, xmlNamespace, new Namespace[] { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE });
 stack.push(createMockElement(xmlNamespace, toAdd));

 assertSame(newScope, stack.getScope());
 }

 @Test
 public void testPushAttributeWithAdditionalNamespace() {
 List<Namespace> toAdd = new ArrayList<>();
 Namespace[] newScope = checkNamespaces(toAdd, xmlNamespace, new Namespace[] { Namespace.XML_NAMESPACE });
 stack.push(createMockAttribute(xmlNamespace, toAdd));

 assertSame(newScope, stack.getScope());
 }

 @Test
 public void testPushSameNamespaceTwice() {
 stack.push(xmlNamespace);
 stack.push(xmlNamespace);

 Namespace[] expectedScope = new Namespace[] { xmlNamespace, xmlNamespace, xmlNamespace };
 assertEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushDifferentNamespacesInDifferentOrder() {
 List<Namespace> toAdd = new ArrayList<>();
 Namespace[] newScope = checkNamespaces(toAdd, customNamespace, new Namespace[] { xmlNamespace, Namespace.NO_NAMESPACE });
 stack.push(createMockElement(customNamespace, toAdd));
 toAdd.clear();
 newScope = checkNamespaces(toAdd, xmlNamespace, newNamespaces(newScope));
 stack.push(createMockElement(xmlNamespace, toAdd));

 assertSame(newNamespaces(newNamespaceArray("xml", "http://www.w3.org/XML/1998/namespace", "custom", "http://example.com/")), stack.getScope());
 }

 @Test
 public void testPop() {
 stack.push(xmlNamespace);
 stack.push(customNamespace);
 stack.pop();

 Namespace[] expectedScope = new Namespace[] { xmlNamespace };
 assertEquals(expectedScope, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenStackIsEmpty() {
 stack.pop();
 }

 @Test
 public void testAddedForwardIterator() {
 stack.push(xmlNamespace);
 stack.push(customNamespace);

 Namespace[] expectedAdded = new Namespace[] { xmlNamespace, customNamespace };
 NamespaceIterator expectedIterator = new NamespaceIterator(expectedAdded);
 assertEquals(expectedIterator, stack.addedForward().iterator());
 }

 @Test
 public void testAddedReverseIterator() {
 stack.push(xmlNamespace);
 stack.push(customNamespace);

 Namespace[] expectedAdded = new Namespace[] { xmlNamespace, customNamespace };
 NamespaceIterator expectedReverseIterator = new ReverseNamespaceIterator(expectedAdded);
 assertEquals(expectedReverseIterator, stack.addedReverse().iterator());
 }

 @Test
 public void testIterator() {
 stack.push(xmlNamespace);
 stack.push(customNamespace);

 Namespace[] expectedScope = new Namespace[] { xmlNamespace, customNamespace };
 NamespaceIterator expectedIterator = new NamespaceIterator(expectedScope);
 assertEquals(expectedIterator, stack.iterator());
 }

 @Test
 public void testIsInScope() {
 stack.push(xmlNamespace);
 stack.push(customNamespace);

 assertTrue(stack.isInScope(xmlNamespace));
 assertFalse(stack.isInScope(new Namespace("namespace", "http://other.com/")));
 }

 private Element createMockElement(final Namespace namespace, final List<Namespace> toAdd) {
 return new Element("root") {{
 setNamespace(namespace);
 setAdditionalNamespaces(toAdd);
 setAttribute(new Attribute("attr", "value"));
 }};
 }

 private Attribute createMockAttribute(final Namespace namespace, final List<Namespace> toAdd) {
 return new Attribute("attr", "value") {{
 setNamespace(namespace);
 setAdditionalNamespaces(toAdd);
 }};
 }

 private static Namespace[] checkNamespaces(List<Namespace> toAdd, Namespace namespace, Namespace[] scope) {
 return NamespaceStack.checkNamespace(toAdd, namespace, scope);
 }

 private static Namespace[] newNamespaces(Namespace... namespaces) {
 return Arrays.copyOf(namespaces, namespaces.length);
 }

 private static NamespaceIterator newNamespaceIterator(Namespace[] namespaces) {
 return new ForwardWalker(namespaces);
 }

 private static class ReverseNamespaceIterator extends ForwardWalker {
 private ReverseNamespaceIterator(Namespace[] namespaces) {
 super(namespaces);
 }

 @Override
 public Namespace next() {
 return super.previous();
 }
 }

 private static class NamespaceIterator implements Iterator<Namespace> {
 private final Namespace[] namespaces;

 NamespaceIterator(Namespace[] namespaces) {
 this.namespaces = namespaces;
 }

 @Override
 public boolean hasNext() {
 return cursor < namespaces.length;
 }

 @Override
 public Namespace next() {
 return namespaces[cursor++];
 }
 }
}