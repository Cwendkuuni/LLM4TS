package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.NamespaceStack;
import org.jdom2.internal.JDOMNamespaces;

import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        assertFalse(stack.isInScope(JDOMNamespaces.XHTML));
        assertFalse(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.depth());
    }

    @Test
    public void testSingleNamespaceStack() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] { JDOMNamespaces.XHTML });
        assertArrayEquals(new Namespace[] { JDOMNamespaces.XHTML }, stack.getScope());
        assertTrue(stack.isInScope(JDOMNamespaces.XHTML));
        assertFalse(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertEquals(1, stack.depth());
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "html"));
        stack.push(new Element("html", null));
        stack.push(new Element("head", null));
        stack.push(new Attribute("xmlns:xhtml", "http://www.w3.org/1999/xhtml"));
        stack.push(new Attribute("xmlns:xml", "http://www.w3.org/XML/1998/namespace"));
        stack.push(new Attribute("xml:lang", "en"));

        assertArrayEquals(new Namespace[] {
            Namespace.NO_NAMESPACE, JDOMNamespaces.XML_NAMESPACE,
            JDOMNamespaces.XHTML, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE
        }, stack.getScope());
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[3]);
        assertTrue(stack.isInScope(JDOMNamespaces.XHTML));
        assertTrue(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertEquals(5, stack.depth());

        stack.pop();
        assertArrayEquals(new Namespace[] {
            Namespace.NO_NAMESPACE, JDOMNamespaces.XML_NAMESPACE,
            JDOMNamespaces.XHTML, Namespace.XML_NAMESPACE
        }, stack.getScope());
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[3]);
        assertTrue(stack.isInScope(JDOMNamespaces.XHTML));
        assertTrue(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertEquals(4, stack.depth());

        stack.pop();
        assertArrayEquals(new Namespace[] {
            Namespace.NO_NAMESPACE, JDOMNamespaces.XML_NAMESPACE,
            JDOMNamespaces.XHTML
        }, stack.getScope());
        assertTrue(stack.isInScope(JDOMNamespaces.XHTML));
        assertTrue(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertEquals(3, stack.depth());

        stack.pop();
        assertArrayEquals(new Namespace[] {
            Namespace.NO_NAMESPACE, JDOMNamespaces.XML_NAMESPACE
        }, stack.getScope());
        assertTrue(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertEquals(2, stack.depth());

        stack.pop();
        assertArrayEquals(new Namespace[] { Namespace.NO_NAMESPACE }, stack.getScope());
        assertFalse(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertEquals(1, stack.depth());

        stack.pop();
        assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        assertFalse(stack.isInScope(JDOMNamespaces.XML_NAMESPACE));
        assertFalse(stack.isInScope(JDOMNamespaces.XHTML));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertEquals(0, stack.depth());
    }

    @Test
    public void testPushPopMultipleNamespacesForwardAndBackward() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "html"));
        stack.push(new Element("html", null));
        stack.push(new Element("head", null));
        stack.push(new Attribute("xmlns:xhtml", "http://www.w3.org/1999/xhtml"));
        stack.push(new Attribute("xmlns:xml", "http://www.w3.org/XML/1998/namespace"));
        stack.push(new Attribute("xml:lang", "en"));

        List<Namespace> namespacesForward = new ArrayList<>();
        for (Namespace ns : stack.addedForward()) {
            namespacesForward.add(ns);
        }
        List<Namespace> namespacesReverse = new ArrayList<>();
        for (Namespace ns : stack.addedReverse()) {
            namespacesReverse.add(ns);
        }

        assertEquals(5, namespacesForward.size());
        assertEquals(5, namespacesReverse.size());
        assertEquals(namespacesForward.get(0), namespacesReverse.get(4));
        assertEquals(namespacesForward.get(1), namespacesReverse.get(3));
        assertEquals(namespacesForward.get(2), namespacesReverse.get(2));
        assertEquals(namespacesForward.get(3), namespacesReverse.get(1));
        assertEquals(namespacesForward.get(4), namespacesReverse.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
}