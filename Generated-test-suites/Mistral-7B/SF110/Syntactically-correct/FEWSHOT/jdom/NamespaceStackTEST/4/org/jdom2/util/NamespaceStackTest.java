package org.jdom2.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.NamespaceImpl;
import org.jdom2.util.NamespaceStack;

import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testUtilStaticMethods() {
        assertEquals(Namespace.NO_NAMESPACE, NamespaceStack.checkNamespace(Collections.emptyList(), Namespace.NO_NAMESPACE, new Namespace[0]));
        Namespace n1 = new NamespaceImpl("p1", "u1");
        Namespace n2 = new NamespaceImpl("p2", "u2");
        Namespace n3 = new NamespaceImpl("p3", "u3");

        List<Namespace> store = new ArrayList<Namespace>(3);
        Namespace[] scope = {n1, n2};
        Namespace[] newscope = NamespaceStack.checkNamespace(store, n3, scope);
        assertEquals(3, newscope.length);
        assertEquals(n3, newscope[0]);
        assertEquals(n1, newscope[1]);
        assertEquals(n2, newscope[2]);

        store.clear();
        scope = new Namespace[2];
        scope[0] = n1;
        scope[1] = n2;
        Namespace[] nscope = NamespaceStack.checkNamespace(store, n1, scope);
        assertEquals(2, nscope.length);
        assertEquals(n1, nscope[0]);
        assertEquals(n1, nscope[1]);
    }

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertNull(stack.getScope());
        assertEquals(0, stack.depth);
        assertFalse(stack.isInScope(null));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertEquals(Collections.emptyList(), stack.addedForward());
        assertEquals(Collections.emptyList(), stack.addedReverse());
        assertSame(Collections.emptyList(), stack.iterator().next());
    }

    @Test
    public void testSimplePushPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("foo", "http://example.com"));
        Namespace[] scope = {Namespace.NO_NAMESPACE, new NamespaceImpl("x", "http://example.com")};
        stack.pushStack(new NamespaceImpl("y", "http://example.org"), scope, Collections.emptyList());
        stack.pop();
        assertEquals(new NamespaceImpl("x", "http://example.com"), stack.getScope()[0]);
        assertEquals(1, stack.depth);

        stack.pop();
        assertNull(stack.getScope());
        assertEquals(0, stack.depth);
    }

    @Test
    public void testMultiplePushPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("foo", "http://example.com"));
        stack.push(new Element("bar", "http://example.org"));
        Namespace[] scope = {Namespace.NO_NAMESPACE, new NamespaceImpl("x", "http://example.com"), new NamespaceImpl("y", "http://example.org")};
        stack.pushStack(new NamespaceImpl("z", "http://example.net"), scope, Collections.emptyList());

        assertEquals(new NamespaceImpl("z", "http://example.net"), stack.getScope()[0]);
        assertEquals(3, stack.depth);

        stack.pop();
        assertEquals(new NamespaceImpl("y", "http://example.org"), stack.getScope()[0]);
        assertEquals(2, stack.depth);

        stack.pop();
        assertEquals(new NamespaceImpl("x", "http://example.com"), stack.getScope()[0]);
        assertEquals(1, stack.depth);

        stack.pop();
        assertNull(stack.getScope());
        assertEquals(0, stack.depth);
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("foo", "http://example.com"));
        stack.push(new Element("bar", "http://example.org"));
        stack.push(new Element("baz", "http://example.net"));

        Iterator<Namespace> iterator = stack.iterator();
        assertEquals(new NamespaceImpl("http://example.net", ""), iterator.next());
        assertEquals(new NamespaceImpl("http://example.org", ""), iterator.next());
        assertEquals(new NamespaceImpl("http://example.com", ""), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Iterator<Namespace> iterator = stack.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testIteratorWithDifferentNamespaceOrder() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("foo", "http://example.org"));
        stack.push(new Element("bar", "http://example.net"));
        stack.push(new Element("baz", "http://example.com"));

        Iterator<Namespace> iterator = stack.iterator();
        List<Namespace> expectedOrder = new ArrayList<Namespace>();
        expectedOrder.add(new NamespaceImpl("http://example.com", ""));
        expectedOrder.add(new NamespaceImpl("http://example.net", ""));
        expectedOrder.add(new NamespaceImpl("http://example.org", ""));

        while (iterator.hasNext()) {
            Namespace ns = iterator.next();
            int index = Arrays.binarySearch(expectedOrder.toArray(new Namespace[expectedOrder.size()]), ns);
            assertTrue(index >= 0);
            assertEquals(ns, expectedOrder.get(index));
            expectedOrder.remove(index);
        }
        assertTrue(expectedOrder.isEmpty());
    }
}