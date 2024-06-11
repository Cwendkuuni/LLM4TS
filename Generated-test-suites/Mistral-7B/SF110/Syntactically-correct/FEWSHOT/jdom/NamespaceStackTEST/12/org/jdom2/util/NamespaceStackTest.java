package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.NamespaceImpl;
import org.jdom2.internal.ArrayCopy;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNull(stack.scope[0]);
        assertArrayEquals(new Namespace[0], stack.added[0]);
        assertSame(NamespaceStack.EMPTY, stack.added[1]);
        assertEquals(-1, stack.depth);

        Namespace[] seed = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        NamespaceStack stack2 = new NamespaceStack(seed);
        assertEquals(Namespace.NO_NAMESPACE, stack2.scope[0]);
        assertArrayEquals(seed, stack2.added[0]);
        assertEquals(0, stack2.depth);
    }

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
        stack.pushStack(Namespace.NO_NAMESPACE, newScope, toAdd);
        assertEquals(1, stack.depth);
        Namespace[] scope = stack.getScope();
        assertArrayEquals(Namespace.XML_NAMESPACE, scope);

        Element element = new Element("foo");
        Namespace mns = Namespace.getNamespace("foo");
        Namespace[] newScope2 = checkNamespace(toAdd, mns, Namespace.XML_NAMESPACE);
        stack.pushStack(mns, newScope2, toAdd);
        assertEquals(2, stack.depth);
        scope = stack.getScope();
        assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE, mns }, scope);

        Attribute att = new Attribute("bar", "baz");
        Namespace mns2 = Attribute.DEFAULT_NAMESPACE;
        Namespace[] newScope3 = checkNamespace(toAdd, mns2, newScope2);
        stack.pushStack(mns2, newScope3, toAdd);
        assertEquals(3, stack.depth);
        scope = stack.getScope();
        assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE, mns, mns2 }, scope);
    }

    @Test
    public void testPushWithMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
        stack.pushStack(Namespace.NO_NAMESPACE, newScope, toAdd);
        assertEquals(1, stack.depth);

        Namespace mns = Namespace.getNamespace("foo");
        Namespace[] newScope2 = checkNamespace(toAdd, mns, newScope);
        stack.pushStack(mns, newScope2, toAdd);
        assertEquals(2, stack.depth);

        Namespace mns2 = Namespace.getNamespace("bar");
        Namespace[] newScope3 = checkNamespace(toAdd, mns2, newScope2);
        stack.pushStack(mns2, newScope3, toAdd);
        assertEquals(3, stack.depth);

        Namespace mns3 = Namespace.getNamespace("baz");
        Namespace[] newScope4 = checkNamespace(toAdd, mns3, newScope3);
        stack.pushStack(mns3, newScope4, toAdd);
        assertEquals(4, stack.depth);

        Namespace[] scope = stack.getScope();
        assertArrayEquals(new Namespace[] { Namespace.XML_NAMESPACE, mns, mns2, mns3 }, scope);
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        assertEquals(1, stack.depth);
        stack.pop();
        assertEquals(0, stack.depth);
        assertNull(stack.scope[0]);
        assertArrayEquals(new Namespace[0], stack.added[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
        stack.pushStack(Namespace.NO_NAMESPACE, newScope, toAdd);
        stack.push(new Attribute("foo", "bar"));
        stack.push(new Attribute("baz", "qux"));

        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        Namespace ns = iterator.next();
        assertTrue(ns.getPrefix().equals(""));
        assertEquals(Namespace.XML_NAMESPACE, ns);

        assertTrue(iterator.hasNext());
        ns = iterator.next();
        assertTrue(ns.getPrefix().equals("foo"));
        assertEquals(Namespace.getNamespace("foo"), ns);

        assertTrue(iterator.hasNext());
        ns = iterator.next();
        assertTrue(ns.getPrefix().equals("baz"));
        assertEquals(Namespace.getNamespace("baz"), ns);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterableAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
        stack.pushStack(Namespace.NO_NAMESPACE, newScope, toAdd);
        stack.push(new Attribute("foo", "bar"));
        stack.push(new Attribute("baz", "qux"));

        Iterable<Namespace> iterable = stack.addedForward();
        Iterator<Namespace> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        Namespace ns = iterator.next();
        assertTrue(ns.getPrefix().equals("foo"));
        assertEquals(Namespace.getNamespace("foo"), ns);

        assertTrue(iterator.hasNext());
        ns = iterator.next();
        assertTrue(ns.getPrefix().equals("baz"));
        assertEquals(Namespace.getNamespace("baz"), ns);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterableAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
        stack.pushStack(Namespace.NO_NAMESPACE, newScope, toAdd);
        stack.push(new Attribute("foo", "bar"));
        stack.push(new Attribute("baz", "qux"));

        Iterable<Namespace> iterable = stack.addedReverse();
        Iterator<Namespace> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        Namespace ns = iterator.next();
        assertTrue(ns.getPrefix().equals("baz"));
        assertEquals(Namespace.getNamespace("baz"), ns);

        assertTrue(iterator.hasNext());
        ns = iterator.next();
        assertTrue(ns.getPrefix().equals("foo"));
        assertEquals(Namespace.getNamespace("foo"), ns);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE);
        stack.pushStack(Namespace.NO_NAMESPACE, newScope, toAdd);
        stack.push(new Attribute("foo", "bar"));
        stack.push(new Attribute("baz", "qux"));

        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getNamespace("foo")));
        assertTrue(stack.isInScope(Namespace.getNamespace("baz")));

        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.getNamespace("qux")));
    }

    private List<Namespace> checkNamespace(List<Namespace> store, Namespace namespace, Namespace[] scope) {
        Namespace[] newScope = NamespaceStack.checkNamespace(store, namespace, scope);
        assertEquals(newScope.length, store.size());
        store.forEach(ns -> assertTrue(Arrays.binarySearch(newScope, ns) >= 0));
        return store;
    }
}