package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.NamespaceStack.ForwardWalker;
import org.jdom2.NamespaceStack.NamespaceIterable;
import org.jdom2.NamespaceStack. BackwardWalker;
import org.jdom2.util.EmptyIterable;

import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNull(stack.getScope());
        assertTrue(stack.isEmpty());
        stack.push(new Element("root"));
        Namespace[] scope = stack.getScope();
        assertArrayEquals(Namespace.NO_NAMESPACE, scope);
        assertTrue(stack.push(new Element("root", "prefix1:prefix2")));
        Namespace[] scope2 = stack.getScope();
        Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, new Namespace("prefix1", "prefix2") };
        assertArrayEquals(expected, scope2);
        assertFalse(stack.push(new Element("root", "prefix1:prefix2")));
        Namespace[] scope3 = stack.getScope();
        assertArrayEquals(expected, scope3);
    }

    @Test
    public void testPushWithEmptyList() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "prefix1:prefix2"));
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = checkNamespace(toadd, new Namespace("prefix3"), stack.scope[stack.depth]);
        stack.pushStack(new Namespace("prefix3"), newscope, toadd);
        Namespace[] scope = stack.getScope();
        Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE, new Namespace("prefix3"), new Namespace("prefix1", "prefix2") };
        assertArrayEquals(expected, scope);
    }

    @Test
    public void testPushWithNonEmptyList() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "prefix1:prefix2"));
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = checkNamespace(toadd, new Namespace("prefix3"), stack.scope[stack.depth]);
        stack.pushStack(new Namespace("prefix3"), newscope, toadd);
        Namespace[] newscope2 = checkNamespace(toadd, new Namespace("prefix4"), newscope);
        stack.pushStack(new Namespace("prefix4"), newscope2, toadd);
        Namespace[] scope = stack.getScope();
        Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE, new Namespace("prefix4"), new Namespace("prefix3"), new Namespace("prefix1", "prefix2") };
        assertArrayEquals(expected, scope);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushWithNullElement() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushWithNullNamespace() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        stack.push(null);
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "prefix1:prefix2"));
        stack.pop();
        Namespace[] scope = stack.getScope();
        Namespace[] expected = new Namespace[] { Namespace.XML_NAMESPACE };
        assertArrayEquals(expected, scope);
        try {
            stack.pop();
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "prefix1:prefix2"));
        Iterable<Namespace> iter = stack.addedForward();
        assertFalse(iter.isEmpty());
        Namespace[] expected = new Namespace[] { new Namespace("prefix1", "prefix2") };
        Namespace[] actual = iter.toArray(new Namespace[iter.size()]);
        assertArrayEquals(expected, actual);
        iter = stack.addedReverse();
        assertFalse(iter.isEmpty());
        actual = iter.toArray(new Namespace[iter.size()]);
        assertArrayEquals(expected, actual);
        Namespace[] scope = stack.getScope();
        assertArrayEquals(Namespace.XML_NAMESPACE, scope);
        stack.pop();
        iter = stack.iterator();
        assertFalse(iter.isEmpty());
        actual = iter.toArray(new Namespace[iter.size()]);
        assertArrayEquals(expected, actual);
        stack.pop();
        assertFalse(stack.isEmpty());
        iter = stack.iterator();
        assertTrue(iter.hasNext());
        Namespace next = iter.next();
        assertSame(next, expected[0]);
        assertFalse(iter.hasNext());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "prefix1:prefix2"));
        assertTrue(stack.isInScope(new Namespace("prefix1", "prefix2")));
        assertTrue(stack.isInScope(new Namespace("prefix1")));
        assertTrue(stack.isInScope(new Namespace("prefix2")));
        assertFalse(stack.isInScope(new Namespace("prefix3")));
        stack.pop();
        assertFalse(stack.isInScope(new Namespace("prefix1", "prefix2")));
        assertFalse(stack.isInScope(new Namespace("prefix1")));
        assertFalse(stack.isInScope(new Namespace("prefix2")));
        assertFalse(stack.isInScope(new Namespace("prefix3")));
    }

    private List<Namespace> checkNamespace(final List<Namespace> store, final Namespace namespace, final Namespace[] scope) {
        return NamespaceStack.checkNamespace(store, namespace, scope);
    }
}