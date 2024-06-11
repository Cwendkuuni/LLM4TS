package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testNewInstance() {
        NamespaceStack nsStack = new NamespaceStack();
        assertNull(nsStack.getScope());
        assertEquals(0, nsStack.depth);
    }

    @Test
    public void testNewInstanceWithSeed() {
        NamespaceStack nsStack = new NamespaceStack(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE });
        assertArrayEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, nsStack.getScope());
        assertEquals(2, nsStack.depth);
    }

    @Test
    public void testPushElement() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element());
        nsStack.push(new Element());
        final Namespace[] expected = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expected, nsStack.getScope());
    }

    @Test
    public void testPushElementWithNamespaces() {
        NamespaceStack nsStack = new NamespaceStack();
        final Element element = new Element();
        final List<Namespace> addedNamespaces = new ArrayList<>(2);
        nsStack.push(element);
        final Namespace mns = element.getNamespace();
        final Namespace[] newscope = NamespaceStack.checkNamespace(addedNamespaces, mns, new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE });
        nsStack.pushStack(mns, newscope, addedNamespaces);
        final Element anotherElement = new Element();
        addedNamespaces.clear();
        final Namespace anotherMns = anotherElement.getNamespace();
        newscope = NamespaceStack.checkNamespace(addedNamespaces, anotherMns, nsStack.scope[nsStack.depth]);
        nsStack.pushStack(anotherMns, newscope, addedNamespaces);
        final Namespace[] expected = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, mns, Namespace.XML_NAMESPACE, anotherMns };
        assertArrayEquals(expected, nsStack.getScope());
    }

    @Test
    public void testPop() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element());
        nsStack.pop();
        assertArrayEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, nsStack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.pop();
    }

    @Test
    public void testIterator() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element());
        nsStack.push(new Element());
        Collection<Namespace> namespaces = nsStack.iterator();
        assertFalse(namespaces.isEmpty());
        Namespace namespace = namespaces.iterator().next();
        assertEquals(namespace.getPrefix(), nsStack.getScope()[0].getPrefix());
        namespace = namespaces.iterator().next();
        assertEquals(namespace.getPrefix(), nsStack.getScope()[1].getPrefix());
    }

    @Test
    public void testAddedForward() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element());
        nsStack.push(new Element());
        Collection<Namespace> namespaces = nsStack.addedForward();
        assertFalse(namespaces.isEmpty());
        Namespace namespace = namespaces.iterator().next();
        assertEquals(namespace.getPrefix(), nsStack.added[nsStack.depth][0].getPrefix());
        namespace = namespaces.iterator().next();
        assertEquals(namespace.getPrefix(), nsStack.added[nsStack.depth][1].getPrefix());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element());
        nsStack.push(new Element());
        Collection<Namespace> namespaces = nsStack.addedReverse();
        assertFalse(namespaces.isEmpty());
        Namespace namespace = namespaces.iterator().next();
        assertEquals(namespace.getPrefix(), nsStack.added[nsStack.depth][1].getPrefix());
        namespace = namespaces.iterator().next();
        assertEquals(namespace.getPrefix(), nsStack.added[nsStack.depth][0].getPrefix());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element());
        nsStack.push(new Element());
        assertTrue(nsStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(nsStack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(nsStack.isInScope(nsStack.getScope()[0]));
        assertTrue(nsStack.isInScope(nsStack.getScope()[1]));
        assertFalse(nsStack.isInScope(new Namespace("test", "http://test.com")));
    }
}