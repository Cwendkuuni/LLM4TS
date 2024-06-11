package org.jdom2.util;

import org.jdom2.internal.Namespace;
import org.jdom2.NamespaceIterable;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Namespace[] seed;

 @Before
 public void setUp() {
 seed = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
 stack = new NamespaceStack(seed);
 }

 @After
 public void tearDown() {
 stack.pop();
 stack = null;
 }

 @Test
 public void testEmptyStack() {
 assertNull(stack.getScope());
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 assertNull(stack.iterator().next());
 }

 @Test
 public void testPushPop() {
 Namespace xml = Namespace.XML_NAMESPACE;
 Namespace ns1 = new Namespace("p1", "u1");
 Namespace ns2 = new Namespace("p2", "u2");
 stack.push(new Element("root"));
 stack.push(ns1);
 stack.push(new Attribute("a", "v", ns1));
 stack.push(ns2);
 stack.push(new Attribute("b", "w", ns2));
 Namespace[] scope = stack.getScope();
 List<Namespace> added = new ArrayList<>();
 Namespace[] newScope = checkNamespace(added, ns2, scope);
 stack.push(new Attribute("c", "x", ns1));
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 scope = stack.getScope();
 assertArrayEquals(seed, scope);
 assertEquals(ns1, added.get(0));
 assertEquals(ns2, added.get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullScope() {
 stack.push(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 stack.push(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPushWithNullNamespace() {
 stack.push(new Attribute(null, null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPushWithNullAdditionalNamespaces() {
 Element root = new Element("root");
 root.setAdditionalNamespaces(null);
 stack.push(root);
 }

 @Test
 public void testPushWithEmptyAdditionalNamespaces() {
 Element root = new Element("root");
 root.setAdditionalNamespaces(new ArrayList<>());
 stack.push(root);
 assertArrayEquals(seed, stack.getScope());
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 assertNull(stack.iterator().next());
 }

 @Test
 public void testPushWithAdditionalNamespaces() {
 Element root = new Element("root");
 Namespace ns3 = new Namespace("p3", "u3");
 root.setAdditionalNamespaces(Arrays.asList(ns1, ns2, ns3));
 stack.push(root);
 Namespace[] scope = stack.getScope();
 Namespace[] newScope = checkNamespace(new ArrayList<>(), ns3, scope);
 stack.push(new Attribute("d", "y", ns3));
 stack.pop();
 scope = stack.getScope();
 assertArrayEquals(seed, scope);
 assertEquals(ns1, added.get(0));
 assertEquals(ns2, added.get(1));
 assertEquals(ns3, added.get(2));
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWithEmptyStack() {
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopAfterPushWithNullScope() {
 stack.push(null);
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopAfterPushWithNullElement() {
 stack.push(null);
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopAfterPushWithNullNamespace() {
 stack.push(new Attribute(null, null, null));
 stack.pop();
 }

 @Test(expected = JDOMException.class)
 public void testPushAfterPopWithEmptyStack() {
 stack.pop();
 stack.push(new Attribute("e", "z", Namespace.XML_NAMESPACE));
 }

 @Test(expected = JDOMException.class)
 public void testPushAfterPopWithNullScope() {
 stack.pop();
 stack.push(new Attribute("e", "z", null));
 }

 @Test(expected = JDOMException.class)
 public void testPushAfterPopWithNullElement() {
 stack.pop();
 stack.push(null);
 }

 private List<Namespace> checkNamespace(List<Namespace> store, Namespace namespace, Namespace[] scope) {
 return stack.checkNamespace(store, namespace, scope);
 }
}