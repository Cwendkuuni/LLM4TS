package org.jdom2.util.test;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.internal.Names;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 Assert.assertTrue(stack.isEmpty());
 }

 @Test
 public void testPushPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.pop();
 Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());

 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.XML_NAMESPACE));
 stack.pop();
 stack.pop();
 Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 }

 @Test
 public void testPushMultipleElements() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element1", Namespace.XML_NAMESPACE));
 stack.push(new Element("element2", Namespace.XML_NAMESPACE));

 Namespace[] expected = {Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE};
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.XHTML_NAMESPACE));

 Namespace[] expected = {Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XHTML_NAMESPACE};
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushAttributes() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", Namespace.XML_NAMESPACE);
 element.addAttribute(new Attribute("attr", "value", "http://example.com/"));
 stack.push(element);
 stack.pop();

 Namespace[] expected = {Namespace.XML_NAMESPACE};
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushMultipleAttributes() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", Namespace.XML_NAMESPACE);
 element.addAttribute(new Attribute("attr1", "value1", "http://example.com/"));
 element.addAttribute(new Attribute("attr2", "value2", "http://example.com/"));
 stack.push(element);
 stack.pop();

 Namespace[] expected = {Namespace.XML_NAMESPACE};
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushTwoElementsWithSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("element1", Namespace.XML_NAMESPACE));
 stack.push(new Element("element2", Namespace.XML_NAMESPACE));

 Namespace[] expected = {Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE};
 Assert.assertArrayEquals(expected, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPushDuplicateNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("element", Namespace.XML_NAMESPACE));
 stack.push(new Element("element", Namespace.XML_NAMESPACE));
 }

 @Test(expected = IllegalStateException.class)
 public void testPopEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }
}