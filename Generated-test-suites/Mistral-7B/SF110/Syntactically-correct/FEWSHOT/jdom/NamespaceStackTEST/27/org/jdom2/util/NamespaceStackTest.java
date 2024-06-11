package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.NamespaceConstants;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.EmptyIterable;
import org.jdom2.util.NamespaceStack;

public class NamespaceStackTest {

    @Test
    public void testNewInstance() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertArrayEquals(Namespace.NO_NAMESPACE, stack.getScope());
        assertArrayEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals(0, stack.getDepth());
    }

    @Test
    public void testNewInstanceWithSeed() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE });
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[0]);
        assertArrayEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, stack.getScope());
        assertArrayEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, stack.getScope()[0]);
        assertEquals(0, stack.getDepth());
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[0]);
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[1]);
        assertEquals(1, stack.getDepth());
    }

    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushElementToStack(stack, new Element("root", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[1]);
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[2]);
        assertEquals(2, stack.getDepth());
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushAttributeToStack(stack, new Attribute("attr", "value", Namespace.NS_FOR_TEST));
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[1]);
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[2]);
        assertEquals(2, stack.getDepth());
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushElementToStack(stack, new Element("root", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[1]);
        assertEquals(1, stack.getDepth());
        stack.pop();
        assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE }, stack.getScope());
        assertEquals(0, stack.getDepth());
    }

    @Test
    public void testPushAndPopMultipleTimes() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushElementToStack(stack, new Element("root", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        pushNamespaceToStack(stack, Namespace.NS_FOR_TEST);
        pushElementToStack(stack, new Element("child", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        pushAttributeToStack(stack, new Attribute("attr", "value", Namespace.NS_FOR_TEST));
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[1]);
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[2]);
        assertEquals(3, stack.getDepth());
        stack.pop();
        assertEquals(Namespace.NS_FOR_TEST, stack.getScope()[1]);
        assertEquals(2, stack.getDepth());
        stack.pop();
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[1]);
        assertEquals(1, stack.getDepth());
        stack.pop();
        assertArrayEquals(Namespace.NO_NAMESPACE, stack.getScope());
        assertEquals(0, stack.getDepth());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushElementToStack(stack, new Element("root", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.NS_FOR_TEST));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.NS_FOR_TEST2));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushElementToStack(stack, new Element("root", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        Iterable<Namespace> namespaces = stack.addedForward();
        assertEquals(Namespace.XML_NAMESPACE, namespaces.iterator().next());
        assertEquals(Namespace.NS_FOR_TEST, namespaces.iterator().next());
        assertFalse(namespaces.iterator().hasNext());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushElementToStack(stack, new Element("root", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        Iterable<Namespace> namespaces = stack.addedReverse();
        assertFalse(namespaces.iterator().hasNext());
        assertEquals(Namespace.NS_FOR_TEST, namespaces.iterator().next());
        assertEquals(Namespace.XML_NAMESPACE, namespaces.iterator().next());
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotOverPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
        stack.pop();
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        pushNamespaceToStack(stack, Namespace.XML_NAMESPACE);
        pushElementToStack(stack, new Element("root", Namespace.NS_FOR_TEST, new QName("http://test.com/ns", "elem"), null, null));
        Iterator<Namespace> iterator = stack.iterator();
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertEquals(Namespace.NS_FOR_TEST, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testCannotIterateEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Iterator<Namespace> iterator = stack.iterator();
        iterator.next();
        iterator.next();
    }

    private void pushNamespaceToStack(NamespaceStack stack, Namespace namespace) {
        Element element = new Element("root", Namespace.NO_NAMESPACE, new QName("http://test.com/ns", "elem"), null, null);
        element.setNamespace(namespace);
        stack.push(element);
    }

    private void pushElementToStack(NamespaceStack stack, Element element) {
        stack.push(element);
    }

    private void pushAttributeToStack(NamespaceStack stack, Attribute attribute) {
        stack.push(attribute);
    }
}