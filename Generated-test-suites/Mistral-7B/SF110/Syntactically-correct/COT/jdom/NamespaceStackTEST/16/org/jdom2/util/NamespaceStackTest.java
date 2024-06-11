package org.jdom2.util;

import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NamespaceStackTest {

private static final Namespace[] DEFAULT_SEED = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};

@Test
public void testConstructorWithEmptyArgument() {
NamespaceStack stack = new NamespaceStack();
Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
Assert.assertTrue(stack.isEmpty());
}

@Test
public void testConstructorWithDefaultSeed() {
NamespaceStack stack = new NamespaceStack(DEFAULT_SEED);
Assert.assertArrayEquals(Arrays.copyOf(DEFAULT_SEED, 2), stack.getScope());
}

@Test
public void testPushElement() {
NamespaceStack stack = new NamespaceStack();
Element element = new Element("root");
Namespace mns = element.getNamespace();
stack.push(element);
Namespace[] expectedScope = new Namespace[]{mns, Namespace.XML_NAMESPACE};
Assert.assertArrayEquals(expectedScope, stack.getScope());
}

@Test
public void testPushElementWithAdditionalNamespaces() {
NamespaceStack stack = new NamespaceStack();
Element element = new Element("root");
Namespace mns = element.getNamespace();
stack.push(element);
Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());

for (Namespace ns : element.getAdditionalNamespaces()) {
stack.push(ns);
}

Namespace[] expectedScope = Arrays.copyOf(element.getNamespaces().toArray(new Namespace[0]), element.getNamespaces().size());
Assert.assertArrayEquals(expectedScope, stack.getScope());
}

@Test
public void testPushAttribute() {
NamespaceStack stack = new NamespaceStack();
Attribute att = new Attribute("attr", "value");
Namespace mns = att.getNamespace();
stack.push(att);
Namespace[] expectedScope = new Namespace[]{mns};
Assert.assertArrayEquals(expectedScope, stack.getScope());
}

@Test(expected = IllegalStateException.class)
public void testPopWhenEmpty() {
NamespaceStack stack = new NamespaceStack();
stack.pop();
}

@Test
public void testPop() {
NamespaceStack stack = new NamespaceStack();
Element element = new Element("root");
Namespace mns = element.getNamespace();
stack.push(element);
stack.pop();
Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
}

@Test
public void testPushAndPopMultipleTimes() {
NamespaceStack stack = new NamespaceStack();
List<Namespace> namespaces = new ArrayList<>();
for (int i = 0; i < 10; i++) {
Element element = new Element("root" + i);
Namespace mns = element.getNamespace();
stack.push(element);
namespaces.add(mns);
}
for (int i = 0; i < 10; i++) {
Assert.assertTrue(stack.isInScope(namespaces.get(i)));
stack.pop();
}
Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
}

@Test
public void testIsInScope() {
NamespaceStack stack = new NamespaceStack();
Element element = new Element("root");
Namespace mns = Namespace.XML_NAMESPACE;
stack.push(element);
Assert.assertTrue(stack.isInScope(mns));

mns = Namespace.NO_NAMESPACE;
Assert.assertTrue(stack.isInScope(mns));

mns = element.getNamespace();
Assert.assertTrue(stack.isInScope(mns));

mns = new Namespace("http://example.com", "prefix");
Assert.assertFalse(stack.isInScope(mns));
}
}