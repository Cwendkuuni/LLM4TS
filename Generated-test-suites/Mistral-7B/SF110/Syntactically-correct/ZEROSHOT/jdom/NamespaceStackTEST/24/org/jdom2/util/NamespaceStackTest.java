package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.InternalNames;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 Namespace xmlNs = new Namespace("http://www.w3.org/XML/1998/namespace", "xml");
 Namespace xsiNs = new Namespace("http://www.w3.org/2001/XMLSchema-instance", "xsi");
 Namespace ns1 = new Namespace("http://example.com/ns1", "ns1");
 Namespace ns2 = new Namespace("http://example.com/ns2", "ns2");

 this.stack = new NamespaceStack(new Namespace[]{xmlNs, xsiNs});
 }

 @Test
 public void testEmptyStack() {
 assertTrue(this.stack.isEmpty());
 assertEquals(NamespaceStack.EMPTY, this.stack.getScope());
 }

 @Test
 public void testPushElementWithNoAdditionalNamespaces() {
 Element element = new Element("root", "root");
 this.stack.push(element);
 Namespace[] expectedScope = {xmlNs, xsiNs, element.getNamespace()};
 assertArrayEquals(expectedScope, this.stack.getScope());
 assertEquals(Arrays.asList(xmlNs, xsiNs, element.getNamespace()), this.stack.addedForward().spliterator().collect(Collectors.toList()));
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 Element element = new Element("root", "root");
 element.setAdditionalNamespaces(Collections.singletonMap("additional:ns", ns1));
 this.stack.push(element);
 Namespace[] expectedScope = {xmlNs, xsiNs, ns1, element.getNamespace()};
 assertArrayEquals(expectedScope, this.stack.getScope());
 assertEquals(Arrays.asList(xmlNs, xsiNs, ns1, ns1, element.getNamespace()), this.stack.addedForward().spliterator().collect(Collectors.toList()));
 }

 @Test
 public void testPushAttributeWithNoAdditionalNamespaces() {
 Attribute attr = new Attribute("xmlns:foo", "bar");
 attr.setNamespace(ns1);
 this.stack.push(attr);
 Namespace[] expectedScope = {xmlNs, xsiNs, ns1};
 assertArrayEquals(expectedScope, this.stack.getScope());
 assertEquals(Arrays.asList(xmlNs, xsiNs, ns1), this.stack.addedForward().spliterator().collect(Collectors.toList()));
 }

 @Test
 public void testPushAttributeWithAdditionalNamespaces() {
 Attribute attr = new Attribute("xmlns:foo", "bar");
 attr.setNamespace(ns1);
 Element element = new Element("root", "root");
 element.setAdditionalNamespaces(Collections.singletonMap("additional:ns2", ns2));
 attr.setElement(element);
 this.stack.push(attr);
 Namespace[] expectedScope = {xmlNs, xsiNs, ns1, ns2};
 assertArrayEquals(expectedScope, this.stack.getScope());
 assertEquals(Arrays.asList(xmlNs, xsiNs, ns1, ns1, ns2), this.stack.addedForward().spliterator().collect(Collectors.toList()));
 }

 @Test
 public void testPop() {
 Element element = new Element("root", "root");
 element.setAdditionalNamespaces(Collections.singletonMap("additional:ns", ns1));
 this.stack.push(element);
 this.stack.push(new Attribute("xmlns:foo", "bar"));
 this.stack.pop();
 Namespace[] expectedScope = {xmlNs, xsiNs, ns1, element.getNamespace()};
 assertArrayEquals(expectedScope, this.stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 this.stack.pop();
 }

 @Test
 public void testIsInScope() {
 Namespace inScope = ns1;
 this.stack.push(new Element("root", "root").setNamespace(inScope));
 assertTrue(this.stack.isInScope(inScope));
 Namespace notInScope = new Namespace("http://example.com/ns3", "ns3");
 assertFalse(this.stack.isInScope(notInScope));
 }
}