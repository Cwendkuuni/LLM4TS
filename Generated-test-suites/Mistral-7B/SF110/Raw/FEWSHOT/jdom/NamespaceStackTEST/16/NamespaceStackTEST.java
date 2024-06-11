package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.internal.NamespaceInternal;
import org.jdom2.Namespace;
import org.jdom2.NamespaceException;
import org.junit.Test;

public class NamespaceStackTest {

    private static Namespace createNamespace(String prefix) {
        return new NamespaceInternal(prefix, null, null);
    }

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertNull(stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(createNamespace("prefix")));
        assertSame(NamespaceStack.EMPTY, stack.addedForward().toArray());
        assertSame(NamespaceStack.EMPTY, stack.addedReverse().toArray());
        assertSame(NamespaceStack.EMPTYITER, stack.iterator());
        assertEquals(0, stack.depth());
    }

    @Test
    public void testSingleNamespaceStack() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] { createNamespace("prefix") });
        assertEquals(new Namespace[] { createNamespace("prefix") }, stack.getScope());
        assertTrue(stack.isInScope(createNamespace("prefix")));
        assertFalse(stack.isInScope(createNamespace("otherPrefix")));
        assertSame(new Namespace[] { createNamespace("prefix") }, stack.addedForward().toArray());
        assertSame(new Namespace[] { createNamespace("prefix") }, stack.addedReverse().toArray());
        assertSame(new ForwardWalker(new Namespace[] { createNamespace("prefix") }), stack.iterator());
        assertEquals(1, stack.depth());

        stack.pop();
        assertNull(stack.getScope());
        assertFalse(stack.isInScope(createNamespace("prefix")));
        assertFalse(stack.isInScope(createNamespace("otherPrefix")));
        assertSame(NamespaceStack.EMPTY, stack.addedForward().toArray());
        assertSame(NamespaceStack.EMPTY, stack.addedReverse().toArray());
        assertSame(NamespaceStack.EMPTYITER, stack.iterator());
        assertEquals(0, stack.depth());
    }

    @Test
    public void testMultipleAddedNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElement("root", "prefix1", null));
        stack.push(createElement("root", null, "prefix2"));
        stack.push(createElement("root", null, "prefix3"));
        stack.push(createAttribute("root", "attr1", "prefix4"));

        Namespace[] expectedScope = new Namespace[] { createNamespace("prefix3"), createNamespace("prefix2"), createNamespace("prefix1"), createNamespace("prefix4"), Namespace.XML_NAMESPACE };
        Namespace[] expectedAdded = new Namespace[] { createNamespace("prefix1"), createNamespace("prefix2"), createNamespace("prefix3"), createNamespace("prefix4") };

        assertEquals(expectedScope, stack.getScope());
        assertEquals(expectedAdded, stack.addedForward().toArray());
        assertEquals(expectedAdded, stack.addedReverse().toArray());

        assertTrue(stack.isInScope(createNamespace("prefix1")));
        assertTrue(stack.isInScope(createNamespace("prefix2")));
        assertTrue(stack.isInScope(createNamespace("prefix3")));
        assertTrue(stack.isInScope(createNamespace("prefix4")));
        assertFalse(stack.isInScope(createNamespace("otherPrefix")));

        assertTrue(stack.isInScope(createElement("root", null, "prefix3").getNamespace()));
        assertTrue(stack.isInScope(createElement("root", null, "prefix2").getNamespace()));
        assertTrue(stack.isInScope(createElement("root", "prefix1", null).getNamespace()));
        assertTrue(stack.isInScope(createAttribute("root", "attr1", "prefix4").getNamespace()));

        stack.pop();
        assertEquals(expectedScope, stack.getScope());
        assertEquals(expectedAdded, stack.addedForward().toArray());
        assertEquals(new Namespace[] { createNamespace("prefix3"), createNamespace("prefix2"), createNamespace("prefix1") }, stack.addedReverse().toArray());

        stack.pop();
        assertEquals(new Namespace[] { createNamespace("prefix3"), createNamespace("prefix2") }, stack.getScope());
        assertEquals(expectedAdded, stack.addedForward().toArray());
        assertEquals(new Namespace[] { createNamespace("prefix3"), createNamespace("prefix2") }, stack.addedReverse().toArray());

        stack.pop();
        assertEquals(new Namespace[] { createNamespace("prefix3") }, stack.getScope());
        assertEquals(expectedAdded, stack.addedForward().toArray());
        assertEquals(new Namespace[] { createNamespace("prefix3") }, stack.addedReverse().toArray());

        stack.pop();
        assertNull(stack.getScope());
        assertSame(NamespaceStack.EMPTY, stack.addedForward().toArray());
        assertSame(NamespaceStack.EMPTY, stack.addedReverse().toArray());
    }

    @Test
    public void testDeepStack() {
        NamespaceStack stack = new NamespaceStack();

        for (int i = 0; i < 100; i++) {
            stack.push(createElement("root", "prefix" + i, null));
        }

        Namespace[] expectedScope = new Namespace[101];
        System.arraycopy(Namespace.XML_NAMESPACE.getNamespaceInfos(), 0, expectedScope, 0, Namespace.XML_NAMESPACE.getNamespaceInfos().length);
        for (int i = 0; i <= 100; i++) {
            expectedScope[i + Namespace.XML_NAMESPACE.getNamespaceInfos().length] = createNamespace("prefix" + i);
        }

        assertEquals(expectedScope, stack.getScope());

        stack.pop();
        assertEquals(expectedScope, stack.getScope());

        stack.pop();
        assertEquals(new Namespace[100][], stack.getScope());

        stack.pop();
        assertNull(stack.getScope());

        try {
            stack.pop();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test(expected = NamespaceException.class)
    public void testAddDuplicateNamespace() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createNamespace("prefix"));
        stack.push(createNamespace("prefix"));
    }

    private Element createElement(String name, String prefix, Namespace namespace) {
        return new Element(name, prefix, namespace);
    }

    private Attribute createAttribute(Element element, String name, String prefix) {
        return (Attribute) element.addAttribute(name, prefix, null);
    }
}