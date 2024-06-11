package org.junit.test;

import static org.junit.Assert.*;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;
 private Element element;
 private Attribute attribute;

 @Before
 public void setup() {
 Namespace xmlNs = new Namespace("http://www.w3.org/XML/1998/namespace");
 Namespace prefixNs = new Namespace("prefix", "http://example.com/");
 Namespace otherNs = new Namespace("other", "http://example.org/");

 element = new Element("root");
 element.setName("root");
 element.getAttributes().add(new Attribute("xmlns:x", xmlNs.getURI()));
 element.getAttributes().add(new Attribute("xmlns:prefix", prefixNs.getURI()));
 element.getAdditionalNamespaces().add(otherNs);

 attribute = new Attribute("attr", "value");
 attribute.setNamespace(prefixNs);

 stack = new NamespaceStack();
 }

 @Test
 public void testEmptyStack() {
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(prefixNs));
 assertFalse(stack.isInScope(otherNs));
 assertFalse(stack.isInScope(xmlNs));
 }

 @Test
 public void testPushElement() {
 stack.push(element);
 assertArrayEquals(new Namespace[]{xmlNs, prefixNs, otherNs}, stack.getScope());
 assertTrue(stack.isInScope(xmlNs));
 assertTrue(stack.isInScope(prefixNs));
 assertTrue(stack.isInScope(otherNs));
 }

 @Test
 public void testPushElementMultipleNamespaces() {
 stack.push(element);
 stack.push(attribute);
 assertArrayEquals(new Namespace[]{xmlNs, prefixNs, otherNs, xmlNs, prefixNs}, stack.getScope());
 assertTrue(stack.isInScope(xmlNs));
 assertTrue(stack.isInScope(prefixNs));
 assertTrue(stack.isInScope(otherNs));
 }

 @Test
 public void testPushAttribute() {
 stack.push(attribute);
 assertArrayEquals(new Namespace[]{xmlNs, prefixNs}, stack.getScope());
 assertTrue(stack.isInScope(xmlNs));
 assertTrue(stack.isInScope(prefixNs));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithEmptyStack() {
 stack.push(element);
 stack.pop();
 stack.push(element); // should throw an exception
 }

 @Test
 public void testPop() {
 stack.push(element);
 stack.push(attribute);
 stack.pop();
 assertArrayEquals(new Namespace[]{xmlNs, prefixNs, otherNs}, stack.getScope());
 assertFalse(stack.isInScope(prefixNs));
 assertFalse(stack.isInScope(otherNs));
 }

 @Test
 public void testIterators() {
 stack.push(element);
 stack.push(attribute);

 Iterable<Namespace> addedForward = stack.addedForward();
 List<Namespace> addedForwardList = new ArrayList<>();
 addedForwardList.addAll(addedForward);
 assertEquals(Arrays.asList(xmlNs, prefixNs, otherNs, xmlNs, prefixNs), addedForwardList);

 Iterable<Namespace> addedReverse = stack.addedReverse();
 List<Namespace> addedReverseList = new ArrayList<>();
 addedReverseList.addAll(addedReverse);
 assertEquals(Arrays.asList(prefixNs, otherNs, xmlNs, prefixNs, xmlNs), addedReverseList);

 Iterator<Namespace> iterator = stack.iterator();
 List<Namespace> iteratorList = new ArrayList<>();
 iteratorList.addAll(iterator);
 assertEquals(Arrays.asList(xmlNs, prefixNs, otherNs), iteratorList);
 }

}