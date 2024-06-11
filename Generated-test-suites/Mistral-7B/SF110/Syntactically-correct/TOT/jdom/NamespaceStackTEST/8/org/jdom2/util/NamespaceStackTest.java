package org.jdom2.util;

import org.jdom2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 }

 @Test
 public void testSingleElementStack() {
 NamespaceStack stack = new NamespaceStack(new Namespace[] { Namespace.XML_NAMESPACE });
 assertFalse(stack.isEmpty());
 assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
 assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
 }

 @Test
 public void testPushMultipleElements() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("elem1", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem2", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem3", Namespace.XML_NAMESPACE_3));

 Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE_3, Namespace.XML_NAMESPACE_2, Namespace.XML_NAMESPACE_2, Namespace.XML_NAMESPACE };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test
 public void testPopSingleElement() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.pop();
 assertTrue(stack.isEmpty());
 assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
 }

 @Test
 public void testPopMultipleElements() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("elem1", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem2", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem3", Namespace.XML_NAMESPACE_3));
 stack.pop();
 stack.pop();

 Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE_3, Namespace.XML_NAMESPACE_2 };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testPushAndPopMultipleTimes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", Namespace.XML_NAMESPACE));
 stack.push(new Element("elem1", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem2", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem3", Namespace.XML_NAMESPACE_3));
 stack.pop();
 stack.push(new Element("elem4", Namespace.XML_NAMESPACE_3));
 stack.pop();
 stack.pop();
 stack.push(new Element("elem5", Namespace.XML_NAMESPACE_2));
 stack.pop();
 stack.push(new Element("elem6", Namespace.XML_NAMESPACE_2));

 Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE_2, Namespace.XML_NAMESPACE_2 };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testSameNamespaceMultipleTimes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("elem1", Namespace.XML_NAMESPACE));
 stack.push(new Element("elem2", Namespace.XML_NAMESPACE));
 stack.push(new Element("elem3", Namespace.XML_NAMESPACE));
 stack.push(new Element("elem4", Namespace.XML_NAMESPACE));

 Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testDifferentNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("elem1", Namespace.XML_NAMESPACE_1));
 stack.push(new Element("elem2", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem3", Namespace.XML_NAMESPACE_3));
 stack.push(new Element("elem4", Namespace.XML_NAMESPACE_4));

 Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE_4, Namespace.XML_NAMESPACE_3, Namespace.XML_NAMESPACE_2, Namespace.XML_NAMESPACE_1 };
 assertArrayEquals(expected, stack.getScope());
 }

 @Test
 public void testIterators() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("elem1", Namespace.XML_NAMESPACE_1));
 stack.push(new Element("elem2", Namespace.XML_NAMESPACE_2));
 stack.push(new Element("elem3", Namespace.XML_NAMESPACE_3));
 stack.push(new Element("elem4", Namespace.XML_NAMESPACE_4));
 stack.push(new Element("elem5", Namespace.XML_NAMESPACE_5));

 Iterator<Namespace> itForward = stack.iterator();
 List<Namespace> listForward = new ArrayList<>();
 while (itForward.hasNext()) {
 listForward.add(itForward.next());
 }
 Namespace[] expectedForward = listForward.toArray(new Namespace[0]);
 Namespace[] actualForward = stack.getScope();
 assertArrayEquals(expectedForward, actualForward);

 Iterator<Namespace> itReverse = stack.addedReverse();
 List<Namespace> listReverse = new ArrayList<>();
 while (itReverse.hasNext()) {
 listReverse.add(itReverse.next());
 }
 Namespace[] expectedReverse = listReverse.toArray(new Namespace[0]);
 Namespace[] actualReverse = stack.getScope();
 assertArrayEquals(expectedReverse, actualReverse);
 }

}