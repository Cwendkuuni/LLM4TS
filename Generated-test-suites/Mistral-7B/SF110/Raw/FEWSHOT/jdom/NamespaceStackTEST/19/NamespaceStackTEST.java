package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import org.jdom2.*;
import org.jdom2.internal.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertNull(stack.getScope());
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
        assertNull(stack.pop());
    }

    @Test
    public void testSingleNamespaceStack() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] {Namespace.NO_NAMESPACE});
        assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE}, stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertNull(stack.pop());
    }

    @Test
    public void testSingleXMLNamespaceStack() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] {Namespace.XML_NAMESPACE});
        assertArrayEquals(new Namespace[] {Namespace.XML_NAMESPACE}, stack.getScope());
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertNull(stack.pop());
    }

    @Test
    public void testPushAndPopSingleNamespace() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE}, stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        Namespace ns = stack.pop();
        assertNull(ns);
        assertArrayEquals(new Namespace[] {}, stack.getScope());
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPushAndPopMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        stack.push(new Element("foo"));
        stack.push(new Element("bar"));

        Namespace[] expected = new Namespace[] {Namespace.NO_NAMESPACE,
                Namespace.XML_NAMESPACE,
                Namespace.XML_NAMESPACE,
                Namespace.XML_NAMESPACE};

        assertArrayEquals(expected, stack.getScope());

        List<Namespace> namespaces = new ArrayList<>();
        stack.pop(namespaces);
        assertArrayEquals(new Namespace[] {Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE}, stack.getScope());
        assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE}, namespaces.toArray(new Namespace[0]));

        namespaces = new ArrayList<>();
        stack.pop(namespaces);
        assertArrayEquals(new Namespace[] {Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE}, stack.getScope());
        assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, namespaces.toArray(new Namespace[0]));

        stack.pop();
        assertArrayEquals(new Namespace[] {Namespace.XML_NAMESPACE}, stack.getScope());
        assertNull(stack.pop());
        assertArrayEquals(new Namespace[] {}, stack.getScope());
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPushAndPopMultipleNamespacesWithSamePrefix() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        stack.push(new Element("foo:bar"));
        stack.push(new Element("foo:baz"));

        Namespace[] expected = new Namespace[] {Namespace.NO_NAMESPACE,
                Namespace.XML_NAMESPACE,
                Namespace.XML_NAMESPACE,
                new Namespace("foo"),
                new Namespace("foo")};

        assertArrayEquals(expected, stack.getScope());

        List<Namespace> namespaces = new ArrayList<>();
        stack.pop(namespaces);
        assertArrayEquals(new Namespace[] {Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("foo")}, stack.getScope());
        assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, namespaces.toArray(new Namespace[0]));

        namespaces = new ArrayList<>();
        stack.pop(namespaces);
        assertArrayEquals(new Namespace[] {Namespace.XML_NAMESPACE, new Namespace("foo")}, stack.getScope());
        assertArrayEquals(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, namespaces.toArray(new Namespace[0]));

        stack.pop();
        assertArrayEquals(new Namespace[] {Namespace.XML_NAMESPACE}, stack.getScope());
        assertNull(stack.pop());
        assertArrayEquals(new Namespace[] {}, stack.getScope());
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }
}