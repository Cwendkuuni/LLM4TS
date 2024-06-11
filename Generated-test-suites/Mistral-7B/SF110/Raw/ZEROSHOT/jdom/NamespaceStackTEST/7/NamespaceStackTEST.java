package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.JDOMNamespaces;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertNull(stack.getScope());
 assertSame(NamespaceStack.EMPTY, stack.addedForward().toArray(new Namespace[0]));
 assertSame(NamespaceStack.EMPTY, stack.addedReverse().toArray(new Namespace[0]));
 assertFalse(stack.iterator().hasNext());
 }

 @Test
 public void testPushElement() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "root");
 stack.push(element);
 Namespace[] scope = stack.getScope();
 Namespace mns = element.getNamespace();
 Namespace[] expected = {mns, Namespace.XML_NAMESPACE};
 assertArrayEquals(expected, scope);
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] newscope = stack.addedForward().toArray(new Namespace[0]);
 assertArrayEquals(newscope, toadd.toArray(new Namespace[0]));
 }

 @Test
 public void testPushElementWithAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "root");
 stack.push(element);
 element.setAdditionalNamespaces(Arrays.asList(new Namespace("xmlns:x", "http://example.com"), new Namespace("xmlns:y", "http://example.org")));
 stack.push(element);
 Namespace[] scope = stack.getScope();
 Namespace[] expected = {Namespace.XML_NAMESPACE, new Namespace("xmlns:x", "http://example.com"), new Namespace("xmlns:y", "http://example.org"), mns};
 assertArrayEquals(expected, scope);
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] newscope = stack.addedForward().toArray(new Namespace[0]);
 assertArrayEquals(newscope, toadd.toArray(new Namespace[0]));
 }

 @Test
 public void testPushAttribute() {
 NamespaceStack stack = new NamespaceStack();
 Attribute att = new Attribute("xmlns:x", "http://example.com");
 stack.push(att);
 Namespace mns = att.getNamespace();
 Namespace[] expected = {mns};
 assertArrayEquals(expected, stack.getScope());
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] newscope = stack.addedForward().toArray(new Namespace[0]);
 assertArrayEquals(newscope, toadd.toArray(new Namespace[0]));
 }

 @Test
 public void testPushInnerElementWithSameNamespace() {
 NamespaceStack stack = new NamespaceStack();
 Element parent = new Element("parent", "parent");
 stack.push(parent);
 Element child = new Element("child", "child");
 child.setName("child"); // set the local name to be the same as the parent's
 stack.push(child);
 Namespace[] scope = stack.getScope();
 Namespace mns = child.getNamespace();
 Namespace[] expected = {Namespace.XML_NAMESPACE, mns};
 assertArrayEquals(expected, scope);
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] newscope = stack.addedForward().toArray(new Namespace[0]);
 assertArrayEquals(newscope, toadd.toArray(new Namespace[0]));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNegativeDepth() {
 NamespaceStack stack = new NamespaceStack();
 stack.push((Element) null); // null Element should throw an exception in real code
 }

 @Test
 public void testPop() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "root");
 stack.push(element);
 stack.pop();
 assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
 assertSame(NamespaceStack.EMPTY, stack.addedForward().toArray(new Namespace[0]));
 assertSame(NamespaceStack.EMPTY, stack.addedReverse().toArray(new Namespace[0]));
 assertFalse(stack.iterator().hasNext());
 }

 @Test
 public void testPopWithEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 assertTrue(stack.isEmpty());
 assertNull(stack.getScope());
 assertSame(NamespaceStack.EMPTY, stack.addedForward().toArray(new Namespace[0]));
 assertSame(NamespaceStack.EMPTY, stack.addedReverse().toArray(new Namespace[0]));
 assertFalse(stack.iterator().hasNext());
 }

 @Test
 public void testPushAndPopMultipleTimes() {
 NamespaceStack stack = new NamespaceStack();
 Element element = new Element("root", "root");
 stack.push(element);
 stack.push(element);
 stack.pop();
 Namespace[] scope = stack.getScope();
 Namespace[] expected = {Namespace.XML_NAMESPACE};
 assertArrayEquals(expected, scope);
 List<Namespace> toadd = new ArrayList<>();
 Namespace[] newscope = stack.addedForward().toArray(new Namespace[0]);
 assertArrayEquals(newscope, toadd.toArray(new Namespace[0]));
 }
}