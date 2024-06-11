package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.JDOMNamespaces;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private final Namespace xmlNamespace = JDOMNamespaces.XML_NAMESPACE;
 private final Namespace namespace1 = new Namespace("ns1");
 private final Namespace namespace2 = new Namespace("ns2");
 private final Namespace namespace3 = new Namespace("ns3");
 private final Namespace namespace4 = new Namespace("ns4");

 @Before
 public void setUp() {
 stack = new NamespaceStack();
 }

 @Test
 public void testEmptyStack() {
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 assertNull(stack.getScope());
 assertTrue(addedForward.isEmpty());
 assertTrue(addedReverse.isEmpty());
 assertFalse(stack.isInScope(xmlNamespace));
 }

 @Test
 public void testPushSingleElement() {
 stack.push(new Element("root", xmlNamespace));
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] scope = stack.getScope();
 assertArrayEquals(Namespace.NO_NAMESPACE, addedForward.toArray(new Namespace[0]));
 assertArrayEquals(Namespace.NO_NAMESPACE, addedReverse.toArray(new Namespace[0]));
 assertArrayEquals(xmlNamespace, scope);
 assertTrue(stack.isInScope(xmlNamespace));
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 Element element = new Element("root", xmlNamespace);
 element.setName("root:element", xmlNamespace);
 stack.push(element);
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] scope = stack.getScope();
 assertArrayEquals(new Namespace[] { xmlNamespace, Namespace.XML_NAMESPACE }, addedForward.toArray(new Namespace[0]));
 assertArrayEquals(new Namespace[] { xmlNamespace, Namespace.XML_NAMESPACE }, addedReverse.toArray(new Namespace[0]));
 assertArrayEquals(new Namespace[] { xmlNamespace, Namespace.XML_NAMESPACE }, scope);
 assertTrue(stack.isInScope(xmlNamespace));
 }

 @Test
 public void testPushAttribute() {
 Attribute attribute = new Attribute("attr", xmlNamespace);
 stack.push(attribute);
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] scope = stack.getScope();
 assertArrayEquals(xmlNamespace, addedForward.toArray(new Namespace[0]));
 assertArrayEquals(xmlNamespace, addedReverse.toArray(new Namespace[0]));
 assertArrayEquals(xmlNamespace, scope);
 assertTrue(stack.isInScope(xmlNamespace));
 }

 @Test
 public void testPushMultipleElements() {
 Element element1 = new Element("root1", xmlNamespace);
 Element element2 = new Element("root2", xmlNamespace);
 stack.push(element1);
 stack.push(element2);
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] scope = stack.getScope();
 assertArrayEquals(new Namespace[] { xmlNamespace, Namespace.XML_NAMESPACE }, addedForward.toArray(new Namespace[0]));
 assertArrayEquals(new Namespace[] { xmlNamespace, Namespace.XML_NAMESPACE }, addedReverse.toArray(new Namespace[0]));
 assertArrayEquals(new Namespace[] { xmlNamespace, Namespace.XML_NAMESPACE }, scope);
 assertArrayEquals(new Namespace[] { xmlNamespace, Namespace.XML_NAMESPACE }, stack.getAdded().toArray(new Namespace[0]));
 assertTrue(stack.isInScope(xmlNamespace));
 }

 @Test
 public void testPop() {
 stack.push(new Element("root", xmlNamespace));
 stack.pop();
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] scope = stack.getScope();
 assertArrayEquals(Namespace.NO_NAMESPACE, addedForward.toArray(new Namespace[0]));
 assertArrayEquals(Namespace.NO_NAMESPACE, addedReverse.toArray(new Namespace[0]));
 assertNull(scope);
 assertFalse(stack.isInScope(xmlNamespace));
 }

 @Test(expected = IllegalStateException.class)
 public void testOverPop() {
 stack.pop();
 stack.pop();
 }

 @Test
 public void testPushAndPopMultipleTimes() {
 stack.push(new Element("root1", xmlNamespace));
 stack.push(new Element("root2", namespace1));
 stack.push(new Element("root3", namespace2));
 stack.push(new Element("root4", namespace3));
 stack.push(new Element("root5", namespace4));
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 Iterable<Namespace> addedForward = stack.addedForward();
 Iterable<Namespace> addedReverse = stack.addedReverse();
 Namespace[] scope = stack.getScope();
 assertArrayEquals(namespace4, addedForward.toArray(new Namespace[0])[0]);
 assertArrayEquals(namespace4, addedReverse.toArray(new Namespace[0])[0]);
 assertArrayEquals(namespace4, scope);
 assertArrayEquals(new Namespace[] { namespace3, namespace2, namespace1, xmlNamespace }, stack.getAdded().toArray(new Namespace[0]));
 assertNull(scope);
 assertFalse(stack.isInScope(namespace4));
 }
}