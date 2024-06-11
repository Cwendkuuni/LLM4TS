package org.jdom2.util;

import org.jdom2.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class NamespaceStackTest {

 private NamespaceStack stack;

 @Before
 public void setUp() {
 stack = new NamespaceStack();
 }

 // Test constructor
 @Test
 public void testConstructor() {
 // Default constructor
 stack = new NamespaceStack();
 assertArrayEquals(NamespaceStack.EMPTY, stack.scope[0]);
 assertSame(NamespaceStack.EMPTYITER, stack.addedForward());
 assertSame(NamespaceStack.EMPTYITER, stack.addedReverse());

 // Constructor with seed
 Namespace xmlNs = new Namespace("xml", "http://www.w3.org/XML/1998/namespace");
 Namespace ns1 = new Namespace("ns1", "http://example.com/ns1");
 Namespace ns2 = new Namespace("ns2", "http://example.com/ns2");
 Namespace[] seed = {ns1, ns2, xmlNs};
 stack = new NamespaceStack(seed);
 assertArrayEquals(seed, stack.scope[0]);
 assertSame(NamespaceStack.EMPTYITER, stack.addedForward());
 assertSame(NamespaceStack.EMPTYITER, stack.addedReverse());
 }

 // Test push
 @Test
 public void testPush() {
 Element elem = new Element("root", new QName("http://example.com/test", "root"));
 stack.push(elem);
 Namespace[] newScope = stack.scope[0];
 Namespace mns = elem.getNamespace();
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] expectedNewScope = checkNamespace(toadd, mns, newScope);
 assertArrayEquals(expectedNewScope, newScope);
 stack.pop();
 }

 @Test
 public void testPushWithAdditionalNamespaces() {
 Element elem = new Element("root", new QName("http://example.com/test", "root"));
 stack.push(elem);
 stack.push(elem.getAdditionalNamespaces().get(0));
 stack.push(elem.getAdditionalNamespaces().get(1));
 Namespace[] newScope = stack.scope[0];
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] expectedNewScope = checkNamespace(toadd, newScope[newScope.length - 1], newScope);
 assertArrayEquals(expectedNewScope, newScope);
 stack.pop();
 stack.pop();
 stack.pop();
 }

 @Test
 public void testPushWithAttributes() {
 Attribute att = new Attribute("attr", "value", new QName("http://example.com/test", "attr"));
 stack.push(att);
 Namespace[] newScope = stack.scope[0];
 Namespace mns = att.getNamespace();
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] expectedNewScope = checkNamespace(toadd, mns, newScope);
 assertArrayEquals(expectedNewScope, newScope);
 stack.pop();
 }

 // Test pushStack
 @Test
 public void testPushStack() {
 Namespace mns = new Namespace("ns1", "http://example.com/ns1");
 Namespace ns1 = new Namespace("ns1:a", "http://example.com/ns1/a");
 Namespace ns2 = new Namespace("ns2:b", "http://example.com/ns2/b");
 stack.push(new Namespace("root", null));
 stack.push(mns);
 stack.push(ns1);
 stack.push(ns2);
 Namespace[] newScope = stack.scope[0];
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] expectedNewScope = checkNamespace(toadd, ns2, newScope);
 assertArrayEquals(expectedNewScope, newScope);
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 }

 // Test pop
 @Test
 public void testPop() {
 Element elem = new Element("root", new QName("http://example.com/test", "root"));
 stack.push(elem);
 stack.pop();
 assertSame(NamespaceStack.EMPTYITER, stack.addedForward());
 assertSame(NamespaceStack.EMPTYITER, stack.addedReverse());
 }

 // Test getScope
 @Test
 public void testGetScope() {
 Element elem = new Element("root", new QName("http://example.com/test", "root"));
 stack.push(elem);
 Namespace[] expectedScope = {elem.getNamespace()};
 assertArrayEquals(expectedScope, stack.getScope());
 stack.pop();
 }
 
 // Test isInScope
 @Test
 public void testIsInScope() {
 Element elem = new Element("root", new QName("http://example.com/test", "root"));
 stack.push(elem);
 assertTrue(stack.isInScope(elem.getNamespace()));
 stack.pop();
 assertFalse(stack.isInScope(elem.getNamespace()));
 }

 // Test edge cases and exceptions
 @Test(expected = IllegalStateException.class)
 public void testCannotPopEmptyStack() {
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testCannotPopOverEmptyStack() {
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullElement() {
 Element elem = null;
 stack.push(elem);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAdditionalNamespaces() {
 Element elem = new Element("root", new QName("http://example.com/test", "root"));
 stack.push(elem);
 elem.setAdditionalNamespaces(null);
 stack.push(elem.getAdditionalNamespaces().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttribute() {
 Attribute att = null;
 stack.push(att);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNamespace() {
 Namespace ns = new Namespace("", "");
 stack.push(ns);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNamespace() {
 Namespace ns = null;
 stack.push(ns);
 }

 private List<Namespace> checkNamespace(List<Namespace> toadd, Namespace namespace, Namespace[] scope) {
 Namespace[] newScope = NamespaceStack.checkNamespace(toadd, namespace, scope);
 return toadd;
 }
}