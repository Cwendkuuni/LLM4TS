package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.namespace.Namespace;
import java.util.*;

public class NamespaceStackTest {

 private static final Namespace NS_A = new Namespace("a");
 private static final Namespace NS_B = new Namespace("b");
 private static final Namespace NS_C = new Namespace("c");
 private static final Namespace NS_D = new Namespace("d");
 private static final Namespace NS_E = new Namespace("e");
 private static final Namespace NS_F = new Namespace("f");
 private static final Namespace NS_G = new Namespace("g");
 private static final Namespace NS_H = new Namespace("h");

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isEmpty());
 assertNull(stack.getScope());
 assertNull(stack.addedForward().iterator().next());
 assertNull(stack.addedReverse().iterator().next());
 }

 @Test
 public void testPushPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Namespace nsA = new Namespace("a");
 stack.push(new Element("foo", nsA));
 assertEquals(2, scope.length);
 assertEquals(nsA, scope[1]);
 addedForward.add(nsA);
 assertEquals(1, addedForward.size());
 addedReverse.add(nsA);
 assertEquals(1, addedReverse.size());
 stack.pop();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertFalse(addedForward.isEmpty());
 assertEquals(nsA, addedForward.get(0));
 assertFalse(addedReverse.isEmpty());
 assertEquals(nsA, addedReverse.get(0));
 }

 @Test
 public void testPushWithAdditionalNamespaces() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Namespace nsA = new Namespace("a");
 stack.push(new Element("foo", nsA));
 assertEquals(2, scope.length);
 assertEquals(nsA, scope[1]);
 addedForward.add(nsA);
 assertEquals(1, addedForward.size());
 addedReverse.add(nsA);
 assertEquals(1, addedReverse.size());
 Namespace nsB = new Namespace("b");
 stack.push(new Element("bar", nsB));
 assertEquals(3, scope.length);
 assertEquals(nsB, scope[2]);
 addedForward.add(nsB);
 assertEquals(2, addedForward.size());
 addedReverse.add(nsB);
 assertEquals(2, addedReverse.size());
 stack.pop();
 assertEquals(2, scope.length);
 stack.pop();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertFalse(addedForward.isEmpty());
 assertFalse(addedReverse.isEmpty());
 }

 @Test
 public void testPushWithAttributes() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Element root = new Element("root");
 Attribute attr = new Attribute("attr", NS_A, "value");
 root.addContent(attr);
 stack.push(root);
 assertEquals(2, scope.length);
 assertEquals(NS_A, scope[1]);
 addedForward.add(NS_A);
 assertEquals(1, addedForward.size());
 addedReverse.add(NS_A);
 assertEquals(1, addedReverse.size());
 Element foo = new Element("foo");
 foo.addAttribute(new Attribute("attr", NS_B, "value"));
 stack.push(foo);
 assertEquals(3, scope.length);
 assertEquals(NS_B, scope[2]);
 addedForward.add(NS_B);
 assertEquals(2, addedForward.size());
 addedReverse.add(NS_B);
 assertEquals(2, addedReverse.size());
 stack.pop();
 assertEquals(2, scope.length);
 stack.pop();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertFalse(addedForward.isEmpty());
 assertFalse(addedReverse.isEmpty());
 }

 @Test
 public void testPushWithDeepNesting() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Element root = new Element("root");
 Attribute attr1 = new Attribute("attr1", NS_A, "value1");
 root.addContent(attr1);
 Element foo = new Element("foo");
 foo.addAttribute(new Attribute("attr2", NS_B, "value2"));
 root.addContent(foo);
 Element bar = new Element("bar");
 foo.addContent(bar);
 Attribute attr2 = new Attribute("attr3", NS_C, "value3");
 bar.addContent(new Element("baz", NS_D).addContent(attr2));
 stack.push(root);
 assertEquals(10, scope.length);
 assertEquals(NS_C, scope[9]);
 addedForward.addAll(Arrays.asList(NS_A, NS_B, NS_C, NS_D));
 assertEquals(4, addedForward.size());
 addedReverse.addAll(Arrays.asList(NS_D, NS_C, NS_B, NS_A));
 assertEquals(4, addedReverse.size());
 stack.pop();
 assertEquals(9, scope.length);
 stack.pop();
 assertEquals(8, scope.length);
 stack.pop();
 assertEquals(7, scope.length);
 stack.pop();
 assertEquals(6, scope.length);
 stack.pop();
 assertEquals(5, scope.length);
 stack.pop();
 assertEquals(4, scope.length);
 stack.pop();
 assertEquals(3, scope.length);
 stack.pop();
 assertEquals(2, scope.length);
 stack.pop();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 assertFalse(addedForward.isEmpty());
 assertFalse(addedReverse.isEmpty());
 }

 @Test
 public void testIsInScope() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Namespace nsA = new Namespace("a");
 stack.push(new Element("foo", nsA));
 assertEquals(2, scope.length);
 assertEquals(nsA, scope[1]);
 addedForward.add(nsA);
 assertEquals(1, addedForward.size());
 addedReverse.add(nsA);
 assertEquals(1, addedReverse.size());
 assertTrue(stack.isInScope(nsA));
 assertTrue(stack.isInScope(NS_NO_NAMESPACE));
 assertFalse(stack.isInScope(NS_B));
 }

 @Test
 public void testIterator() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Namespace nsA = new Namespace("a");
 stack.push(new Element("foo", nsA));
 assertEquals(2, scope.length);
 assertEquals(nsA, scope[1]);
 addedForward.add(nsA);
 assertEquals(1, addedForward.size());
 addedReverse.add(nsA);
 assertEquals(1, addedReverse.size());
 Iterator<Namespace> iterator = stack.iterator();
 assertEquals(nsA, iterator.next());
 assertEquals(Namespace.NO_NAMESPACE, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAddedForwardIterator() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Namespace nsA = new Namespace("a");
 stack.push(new Element("foo", nsA));
 assertEquals(2, scope.length);
 assertEquals(nsA, scope[1]);
 addedForward.add(nsA);
 assertEquals(1, addedForward.size());
 addedReverse.add(nsA);
 assertEquals(1, addedReverse.size());
 Iterator<Namespace> iterator = stack.addedForward().iterator();
 assertEquals(nsA, iterator.next());
 assertEquals(Namespace.NO_NAMESPACE, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAddedReverseIterator() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root"));
 Namespace[] scope = stack.getScope();
 List<Namespace> addedForward = new ArrayList<>();
 List<Namespace> addedReverse = new ArrayList<>();
 assertEquals(1, scope.length);
 assertEquals(Namespace.NO_NAMESPACE, scope[0]);
 Namespace nsA = new Namespace("a");
 stack.push(new Element("foo", nsA));
 assertEquals(2, scope.length);
 assertEquals(nsA, scope[1]);
 addedForward.add(nsA);
 assertEquals(1, addedForward.size());
 addedReverse.add(nsA);
 assertEquals(1, addedReverse.size());
 Iterator<Namespace> iterator = stack.addedReverse().iterator();
 assertEquals(nsA, iterator.next());
 assertEquals(Namespace.NO_NAMESPACE, iterator.next());
 assertFalse(iterator.hasNext());
 }
}