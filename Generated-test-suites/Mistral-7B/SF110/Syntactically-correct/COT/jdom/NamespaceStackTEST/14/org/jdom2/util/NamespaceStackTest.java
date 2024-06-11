package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.Namespace;
import org.jdom2.internal.NamespaceStack.BinarySearchException;
import org.jdom2.internal.NamespaceStack.EmptyIterable;
import org.jdom2.internal.NamespaceStack.ForwardWalker;
import org.jdom2.internal.NamespaceStack.NamespaceIterable;
import org.jdom2.internal.NamespaceStack.Namespace;
import org.jdom2.internal.NamespaceStack.BackwardWalker;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private static final Namespace[] DEFAULT_SEED = new Namespace[]{ Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };

 @Test
 public void testConstructor() {
 NamespaceStack stack = new NamespaceStack();
 assertEquals(DEFAULT_SEED, stack.scope[0]);
 assertEquals(0, stack.depth);
 assertNull(stack.added[0]);
 }

 @Test
 public void testConstructorWithSeed() {
 NamespaceStack stack = new NamespaceStack(DEFAULT_SEED);
 assertEquals(DEFAULT_SEED, stack.scope[0]);
 assertEquals(1, stack.depth);
 assertArrayEquals(DEFAULT_SEED, stack.added[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSeed() {
 NamespaceStack stack = new NamespaceStack(null);
 }

 @Test
 public void testPushElement() {
 final Element element = new Element("root");
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 Namespace[] newScope = stack.scope[1];
 List<Namespace> toAdd = new ArrayList<Namespace>();
 Namespace[] checkScope = NamespaceStack.checkNamespace(toAdd, element.getNamespace(), newScope);
 assertArrayEquals(checkScope, newScope);
 }

 @Test
 public void testPushAttribute() {
 final Attribute att = new Attribute("myAttribute", "value");
 NamespaceStack stack = new NamespaceStack();
 stack.push(att);
 Namespace[] newScope = stack.scope[1];
 List<Namespace> toAdd = new ArrayList<Namespace>(1);
 Namespace[] checkScope = NamespaceStack.checkNamespace(toAdd, att.getNamespace(), newScope);
 assertArrayEquals(checkScope, newScope);
 }

 @Test
 public void testPushMultipleNamespaces() {
 final Element element = new Element("root");
 final Attribute att1 = new Attribute("a", "a");
 final Attribute att2 = new Attribute("b", "b");
 NamespaceStack stack = new NamespaceStack();
 stack.push(element);
 stack.push(att1);
 stack.push(att2);
 Namespace[] newScope = stack.scope[2];
 List<Namespace> toAdd = new ArrayList<Namespace>(3);
 Namespace[] checkScope = NamespaceStack.checkNamespace(toAdd, att2.getNamespace(), newScope);
 assertArrayEquals(checkScope, newScope);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullElement() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(null);
 }

 @Test
 public void testPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 assertEquals(1, stack.depth);
 stack.pop();
 assertEquals(0, stack.depth);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnderflow() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 stack.pop();
 }

 @Test
 public void testAddedForward() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Attribute("attr", "attrValue"));
 List<Namespace> namespaces = stack.addedForward();
 assertEquals(2, namespaces.size());
 assertNotNull(namespaces.iterator().next());
 }

 @Test
 public void testAddedReverse() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Attribute("attr", "attrValue"));
 List<Namespace> namespaces = stack.addedReverse();
 assertEquals(2, namespaces.size());
 assertNotNull(namespaces.iterator().next());
 }

 @Test
 public void testIterator() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Attribute("attr1", "attrValue1"));
 stack.push(new Attribute("attr2", "attrValue2"));
 Iterator<Namespace> it = stack.iterator();
 assertEquals(3, it.next().getPrefix().length());
 assertEquals(2, it.next().getPrefix().length());
 assertEquals(1, it.next().getPrefix().length());
 assertFalse(it.hasNext());
 }

 @Test
 public void testGetScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Attribute("attr1", "attrValue1"));
 stack.push(new Attribute("attr2", "attrValue2"));
 Namespace[] scope = stack.getScope();
 assertEquals(3, scope.length);
 assertArrayEquals(scope[0], Namespace.NO_NAMESPACE);
 assertArrayEquals(scope[1], Namespace.XML_NAMESPACE);
 assertArrayEquals(scope[2], new Namespace[] { new Namespace("root", "root"), new Namespace("attr1", "attr1") });
 }

 @Test
 public void testIsInScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Attribute("attr1", "attrValue1"));
 assertTrue(stack.isInScope(new Namespace("root", "root")));
 assertTrue(stack.isInScope(new Namespace("attr1", "attr1")));
 assertFalse(stack.isInScope(new Namespace("namespace", "namespace")));
 }

 @Test(expected = IllegalStateException.class)
 public void testOutOfScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 stack.push(new Attribute("attr1", "attrValue1"));
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 }

 @Test
 public void testBinarySearch() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Namespace("root1", "root1"));
 stack.push(new Namespace("root2", "root2"));
 stack.push(new Namespace("root1", "root1"));
 int index = NamespaceStack.binarySearch(stack.scope[1], 0, stack.scope[1].length, new Namespace("root1", "root1"));
 assertEquals(1, index);
 }

 @Test(expected = BinarySearchException.class)
 public void testBinarySearchNotFound() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Namespace("root1", "root1"));
 stack.push(new Namespace("root2", "root2"));
 int index = NamespaceStack.binarySearch(stack.scope[1], 0, stack.scope[1].length, new Namespace("root3", "root3"));
 }
}