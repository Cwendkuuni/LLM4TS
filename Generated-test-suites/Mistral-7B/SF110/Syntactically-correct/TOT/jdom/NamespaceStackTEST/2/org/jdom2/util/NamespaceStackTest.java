package org.jdom2.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class NamespaceStackTest {

 private static final Namespace XML_NAMESPACE = Namespace.XML_NAMESPACE;
 private static final Namespace NO_NAMESPACE = Namespace.NO_NAMESPACE;
 private static final Namespace NS1 = new Namespace("http://example.com/ns1");
 private static final Namespace NS2 = new Namespace("http://example.com/ns2");
 private static final Namespace NS3 = new Namespace("http://example.com/ns3");
 private static final Namespace NS4 = new Namespace("http://example.com/ns4");
 private static final Namespace NS5 = new Namespace("http://example.com/ns5");
 private static final Namespace[] SEED = {NS1, XML_NAMESPACE};

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertNull(stack.getScope());
 assertFalse(stack.isInScope(NS1));
 assertFalse(stack.isInScope(XML_NAMESPACE));
 assertFalse(stack.isInScope(NO_NAMESPACE));
 assertEquals(0, stack.depth);
 }

 @Test
 public void testSinglePush() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", NS1));
 Namespace[] scope = stack.getScope();
 assertEquals(NS1, scope[0]);
 assertTrue(stack.isInScope(NS1));
 assertTrue(stack.isInScope(XML_NAMESPACE));
 assertTrue(stack.isInScope(NO_NAMESPACE));
 assertEquals(1, stack.depth);
 }

 @Test
 public void testMultiplePush() {
 NamespaceStack stack = new NamespaceStack(SEED);
 stack.push(new Element("root1", NS1));
 stack.push(new Element("root2", NS2));
 stack.push(new Element("root3", NS3));
 stack.push(new Element("root4", NS1));
 stack.push(new Element("root5", NS4));
 stack.push(new Element("root6", NS5));
 Namespace[] scope = stack.getScope();
 assertEquals(NS5, scope[0]);
 assertTrue(stack.isInScope(NS5));
 assertTrue(stack.isInScope(NS4));
 assertTrue(stack.isInScope(NS3));
 assertTrue(stack.isInScope(NS2));
 assertTrue(stack.isInScope(NS1));
 assertTrue(stack.isInScope(XML_NAMESPACE));
 assertTrue(stack.isInScope(NO_NAMESPACE));
 assertEquals(6, stack.depth);
 }

 @Test
 public void testPushWithAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", NS1);
 root.setNamespaceAware(true);
 root.addNamespaceDeclaration("ns2", NS2);
 root.addNamespaceDeclaration("ns3", NS3);
 stack.push(root);
 Namespace[] scope = stack.getScope();
 assertEquals(NS1, scope[0]);
 assertEquals(3, scope.length);
 assertTrue(stack.isInScope(NS1));
 assertTrue(stack.isInScope(NS2));
 assertTrue(stack.isInScope(NS3));
 assertTrue(stack.isInScope(XML_NAMESPACE));
 assertTrue(stack.isInScope(NO_NAMESPACE));
 assertEquals(1, stack.depth);
 }

 @Test
 public void testPushWithAttributes() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", NS1);
 root.addAttribute(new Attribute("attr1", NS2, "value1"));
 stack.push(root);
 Namespace[] scope = stack.getScope();
 assertEquals(NS1, scope[0]);
 assertEquals(3, scope.length);
 assertTrue(stack.isInScope(NS1));
 assertTrue(stack.isInScope(NS2));
 assertTrue(stack.isInScope(XML_NAMESPACE));
 assertTrue(stack.isInScope(NO_NAMESPACE));
 assertEquals(1, stack.depth);
 }

 @Test
 public void testPushWithEmptyAttributes() {
 NamespaceStack stack = new NamespaceStack();
 Element root = new Element("root", NS1);
 stack.push(root);
 Namespace[] scope = stack.getScope();
 assertEquals(NS1, scope[0]);
 assertEquals(1, scope.length);
 assertTrue(stack.isInScope(NS1));
 assertTrue(stack.isInScope(XML_NAMESPACE));
 assertTrue(stack.isInScope(NO_NAMESPACE));
 assertEquals(1, stack.depth);
 }

 @Test
 public void testPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", NS1));
 stack.pop();
 Namespace[] scope = stack.getScope();
 assertNull(scope);
 assertEquals(-1, stack.depth);
 }

 @Test(expected = IllegalStateException.class)
 public void testPopAfterEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }
}