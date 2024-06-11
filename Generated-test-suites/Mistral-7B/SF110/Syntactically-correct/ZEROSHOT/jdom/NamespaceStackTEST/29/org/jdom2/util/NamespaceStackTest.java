package org.jdom2.util;

import org.jdom2.internal.*;
import org.jdom2.*;
import java.util.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Namespace xmlNamespace;
 private Namespace ns1;
 private Namespace ns2;
 private Namespace ns3;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 xmlNamespace = Namespace.XML_NAMESPACE;
 ns1 = new Namespace("http://www.example.com/ns1");
 ns2 = new Namespace("http://www.example.com/ns2");
 ns3 = new Namespace("http://www.example.com/ns3");
 element = new Element("root", "root element");
 element.setNamespace(ns1);
 attribute = new Attribute("attr", "attribute value", ns2);
 }

 @Test
 public void testEmptyStack() {
 NamespaceStack emptyStack = new NamespaceStack();
 assertTrue(emptyStack.isEmpty());
 assertEquals(0, emptyStack.getDepth());
 assertNull(emptyStack.getScope());
 }

 @Test
 public void testDefaultConstructor() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertEquals(0, stack.getDepth());
 assertArrayEquals(NamespaceStack.DEFAULTSEED, stack.getScope());
 }

 @Test
 public void testConstructorWithSeed() {
 NamespaceStack stack = new NamespaceStack(new Namespace[] { xmlNamespace, ns1 });
 assertFalse(stack.isEmpty());
 assertEquals(1, stack.getDepth());
 assertArrayEquals(new Namespace[] { xmlNamespace, ns1 }, stack.getScope());
 }

 @Test
 public void testPushElement() {
 stack = new NamespaceStack();
 stack.push(element);
 Namespace[] expectedScope = new Namespace[] { xmlNamespace, ns1 };
 assertEquals(1, stack.getDepth());
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushAttribute() {
 stack = new NamespaceStack();
 stack.push(attribute);
 Namespace[] expectedScope = new Namespace[] { xmlNamespace, ns2 };
 assertEquals(1, stack.getDepth());
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPushMultipleElementsAndAttributes() {
 stack = new NamespaceStack();
 stack.push(element);
 stack.push(attribute);
 stack.push(new Element("child1", "child1 element"));
 stack.push(new Attribute("attr1", "attr1 value", ns3));
 Namespace[] expectedScope = new Namespace[] { xmlNamespace, ns1, ns2, ns3 };
 assertEquals(4, stack.getDepth());
 assertArrayEquals(expectedScope, stack.getScope());
 }

 @Test
 public void testPop() {
 stack = new NamespaceStack();
 stack.push(element);
 stack.pop();
 assertEquals(0, stack.getDepth());
 assertArrayEquals(NamespaceStack.DEFAULTSEED, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testAddedForward() {
 stack = new NamespaceStack();
 stack.push(element);
 stack.push(attribute);
 Iterable<Namespace> addedForward = stack.addedForward();
 Namespace[] expected = new Namespace[] { ns1, ns2 };
 assertArrayEquals(expected, addedForward.toArray(new Namespace[0]));
 }

 @Test
 public void testAddedReverse() {
 stack = new NamespaceStack();
 stack.push(element);
 stack.push(attribute);
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] expected = new Namespace[] { ns2, ns1 };
 assertArrayEquals(expected, addedReverse.toArray(new Namespace[0]));
 }

 @Test
 public void testIsInScope() {
 stack = new NamespaceStack();
 stack.push(element);
 assertTrue(stack.isInScope(ns1));
 assertTrue(stack.isInScope(ns2)); // attribute is not considered
 assertFalse(stack.isInScope(ns3)); // element's child elements are not considered
 }
}