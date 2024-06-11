package org.jdom2.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack namespaceStack;
 private Namespace ns1;
 private Namespace ns2;
 private Namespace ns3;
 private Namespace ns4;
 private Namespace ns5;
 private Element element;
 private Attribute attribute;

 @Before
 public void setUp() {
 ns1 = new Namespace("prefix1");
 ns2 = new Namespace("prefix2");
 ns3 = new Namespace("prefix3");
 ns4 = new Namespace("prefix4");
 ns5 = new Namespace("prefix5");
 element = new Element("root", ns1);
 attribute = new Attribute("attr", ns2);
 }

 @After
 public void tearDown() {
 namespaceStack = null;
 }

 @Test
 public void testConstructor() {
 namespaceStack = new NamespaceStack();
 assertNotNull(namespaceStack);
 assertEquals(new Namespace[0], namespaceStack.getScope());
 assertEquals(0, namespaceStack.depth);
 }

 @Test
 public void testConstructorWithSeed() {
 namespaceStack = new NamespaceStack(new Namespace[] { ns1, ns2 });
 assertNotNull(namespaceStack);
 assertEquals(new Namespace[] { ns1, ns2 }, namespaceStack.getScope());
 assertEquals(0, namespaceStack.depth);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionWhenAddingNullScopeToConstructor() {
 try {
 new NamespaceStack(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPushElement() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(element);
 assertEquals(new Namespace[] { ns1 }, namespaceStack.getScope());
 assertEquals(1, namespaceStack.depth);
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(element);
 element.setAdditionalNamespaces(Arrays.asList(ns3, ns4));
 namespaceStack.push(element);
 String[] expectedScope = new String[] { ns1.getPrefix(), ns3.getPrefix(), ns4.getPrefix() };
 Namespace[] actualScope = namespaceStack.getScope();
 assertArrayEquals(expectedScope, Arrays.stream(actualScope).map(Namespace::getPrefix).toArray());
 assertEquals(2, namespaceStack.depth);
 }

 @Test
 public void testPushAttribute() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(attribute);
 assertEquals(new Namespace[] { ns2 }, namespaceStack.getScope());
 assertEquals(1, namespaceStack.depth);
 }

 @Test
 public void testPushAttributeWithAdditionalNamespace() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(attribute);
 attribute.setNamespace(ns3);
 namespaceStack.push(attribute);
 assertEquals(new Namespace[] { ns2, ns3 }, namespaceStack.getScope());
 assertEquals(2, namespaceStack.depth);
 }

 @Test
 public void testPushAndPop() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(element);
 namespaceStack.push(attribute);
 namespaceStack.pop();
 assertEquals(new Namespace[] { ns1 }, namespaceStack.getScope());
 assertEquals(1, namespaceStack.depth);
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionWhenPoppingEmptyStack() {
 namespaceStack = new NamespaceStack();
 namespaceStack.pop();
 fail("IllegalStateException expected");
 }

 @Test
 public void testAddedForwardIterator() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(element);
 namespaceStack.push(attribute);
 Iterator<Namespace> iterator = namespaceStack.addedForward();
 List<Namespace> expectedList = new ArrayList<>();
 expectedList.add(ns1);
 expectedList.add(ns2);
 List<Namespace> actualList = new ArrayList<>();
 while (iterator.hasNext()) {
 actualList.add(iterator.next());
 }
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAddedReverseIterator() {
 namespaceStack = new NamespaceStack();
 namespaceStack.push(element);namespaceStack.push(attribute);
 Iterator<Namespace> iterator = namespaceStack.addedReverse();
 List<Namespace> expectedList = new ArrayList<>();
 expectedList.add(ns2);
 expectedList.add(ns1);
 List<Namespace> actualList = new ArrayList<>();
 while (iterator.hasNext()) {
 actualList.add(iterator.next());
 }
 assertEquals(expectedList, actualList);
 }
}