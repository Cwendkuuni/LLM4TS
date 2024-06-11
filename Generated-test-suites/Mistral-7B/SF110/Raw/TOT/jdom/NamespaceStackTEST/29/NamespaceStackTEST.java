package org.jdom2.util;

import org.jdom2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

 @Test
 public void testEmptyStack() {
 NamespaceStack stack = new NamespaceStack();
 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(new Namespace("test", "http://test.com")));
 assertFalse(stack.isInScope(new Namespace("xml", "http://www.w3.org/XML/1998/namespace")));
 assertFalse(stack.isInScope(new Namespace("foo", "http://foo.com")));

 assertEquals(NamespaceStack.EMPTY, stack.getScope());
 assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
 assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
 }

 @Test
 public void testPushPop() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", "test.xml"));
 stack.push(new Attribute("attr", "value", "http://test.com"));

 Namespace mns1 = new Namespace("test1", "http://test1.com");
 stack.push(new Element("test1", ""));
 stack.push(new Attribute("attr1", "value1", "http://test1.com"));
 stack.push(new Element("test2", ""));
 stack.push(new Attribute("attr2", "value2", "http://test2.com"));

 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();

 Namespace mns2 = new Namespace("test2", "http://test2.com");
 stack.push(new Element("test2", ""));
 stack.push(new Attribute("attr2", "value2", "http://test2.com"));

 assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
 assertFalse(stack.isInScope(mns1));
 assertTrue(stack.isInScope(mns2));
 assertFalse(stack.isInScope(new Namespace("xml", "http://www.w3.org/XML/1998/namespace")));
 assertFalse(stack.isInScope(new Namespace("foo", "http://foo.com")));

 assertEquals(new Namespace[] { mns2 }, stack.getScope());
 assertEquals(new Namespace[] { mns1, mns2 }, stack.addedForward().toArray(new Namespace[0]));
 assertEquals(new Namespace[] { mns2, mns1 }, stack.addedReverse().toArray(new Namespace[0]));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullElement() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullAttribute() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Attribute(null, null, "http://test.com"));
 }

 @Test(expected = IllegalStateException.class)
 public void testPushWithNullNamespace() {
 NamespaceStack stack = new NamespaceStack();
 stack.push(new Element("root", "test.xml"));
 stack.push(new Attribute("attr", "value", null));
 }

 @Test(expected = IllegalStateException.class)
 public void testPopWhenEmpty() {
 NamespaceStack stack = new NamespaceStack();
 stack.pop();
 }

 @Test(expected = IllegalStateException.class)
 public void testPopBeyondLimit() {
 NamespaceStack stack = new NamespaceStack();
 for (int i = 0; i < 11; i++) {
 stack.push(new Element("root", "test.xml"));
 }
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 stack.pop();
 }
}