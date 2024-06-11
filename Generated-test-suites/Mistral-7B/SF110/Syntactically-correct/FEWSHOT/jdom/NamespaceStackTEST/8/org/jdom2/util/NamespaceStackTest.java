package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.namespace.Namespace;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(0, stack.size());
        assertNull(stack.getScope());
    }

    @Test
    public void testNamespaceStackWithDefaultSeed() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", Namespace.XML_NAMESPACE));
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testNamespaceStackWithCustomSeed() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] {Namespace.XML_NAMESPACE, new Namespace("test", "urn:test")});
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        root.addNamespaceDeclaration("test", "urn:test");
        stack.push(root);
        Namespace[] expectedScope = {new Namespace("test", "urn:test"), Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPushMultipleElementsWithSameNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        stack.push(root);
        Element child1 = new Element("child1", Namespace.XML_NAMESPACE);
        stack.push(child1);
        Element child2 = new Element("child2", Namespace.XML_NAMESPACE);
        stack.push(child2);
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        root.addNamespaceDeclaration("test", "urn:test");
        stack.push(root);
        Element child = new Element("child", Namespace.XML_NAMESPACE);
        child.addNamespaceDeclaration("test2", "urn:test2");
        stack.push(child);
        Namespace[] expectedScope = {new Namespace("test", "urn:test"), new Namespace("test2", "urn:test2"), Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        stack.push(root);
        Element child = new Element("child", Namespace.XML_NAMESPACE);
        stack.push(child);
        stack.pop();
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testOverPop() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        stack.push(root);
        stack.pop();
        stack.pop();
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        stack.push(root);
        Element child = new Element("child", Namespace.XML_NAMESPACE);
        stack.push(child);
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(child.getNamespace()));
        assertFalse(stack.isInScope(new Namespace("test", "urn:test")));
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        stack.push(root);
        Element child1 = new Element("child1", Namespace.XML_NAMESPACE);
        stack.push(child1);
        Element child2 = new Element("child2", Namespace.XML_NAMESPACE);
        stack.push(child2);
        Iterator<Namespace> iterator = stack.iterator();
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertEquals(child1.getNamespace(), iterator.next());
        assertEquals(child2.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedForwardIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        stack.push(root);
        Element child1 = new Element("child1", Namespace.XML_NAMESPACE);
        stack.push(child1);
        Element child2 = new Element("child2", Namespace.XML_NAMESPACE);
        stack.push(child2);
        Iterator<Namespace> iterator = stack.addedForward();
        assertEquals(root.getNamespace(), iterator.next());
        assertEquals(child1.getNamespace(), iterator.next());
        assertEquals(child2.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverseIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root", Namespace.XML_NAMESPACE);
        stack.push(root);
        Element child1 = new Element("child1", Namespace.XML_NAMESPACE);
        stack.push(child1);
        Element child2 = new Element("child2", Namespace.XML_NAMESPACE);
        stack.push(child2);
        Iterator<Namespace> iterator = stack.addedReverse();
        assertEquals(child2.getNamespace(), iterator.next());
        assertEquals(child1.getNamespace(), iterator.next());
        assertEquals(root.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }
}