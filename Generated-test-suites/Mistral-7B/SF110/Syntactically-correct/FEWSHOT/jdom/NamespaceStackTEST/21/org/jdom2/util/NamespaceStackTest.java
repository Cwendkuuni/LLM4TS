package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
        assertIterableEquals(Collections.emptyList(), stack.addedForward());
        assertIterableEquals(Collections.emptyList(), stack.addedReverse());
        assertArrayEquals(new Namespace[0], stack.getScope());
    }

    @Test
    public void testPushAndPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("foo"));
        stack.push(new Element("bar"));
        stack.pop();
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getDefaultNamespace()));
        assertIterableEquals(Arrays.asList(Namespace.getDefaultNamespace()), stack.addedForward());
        assertIterableEquals(Arrays.asList(Namespace.getDefaultNamespace()), stack.addedReverse());
        assertArrayEquals(new Namespace[] { Namespace.getDefaultNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(Namespace.getDefaultNamespace()));
        assertNull(stack.getScope());
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element foo = new Element("foo");
        Element bar = new Element("bar");
        Element baz = new Element("baz");
        stack.push(foo);
        stack.push(bar);
        stack.push(baz);
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getDefaultNamespace()));
        assertTrue(stack.isInScope(foo.getNamespace()));
        assertTrue(stack.isInScope(bar.getNamespace()));
        assertTrue(stack.isInScope(baz.getNamespace()));
        assertIterableEquals(Arrays.asList(Namespace.getDefaultNamespace(), foo.getNamespace(), bar.getNamespace(), baz.getNamespace()), stack.addedForward());
        assertIterableEquals(Arrays.asList(baz.getNamespace(), bar.getNamespace(), foo.getNamespace(), Namespace.getDefaultNamespace()), stack.addedReverse());
        assertArrayEquals(new Namespace[] { Namespace.getDefaultNamespace(), foo.getNamespace(), bar.getNamespace(), baz.getNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(Namespace.getDefaultNamespace()));
        assertArrayEquals(new Namespace[] { foo.getNamespace(), bar.getNamespace(), baz.getNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(foo.getNamespace()));
        assertArrayEquals(new Namespace[] { bar.getNamespace(), baz.getNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(bar.getNamespace()));
        assertNull(stack.getScope());
    }

    @Test
    public void testPushMultipleNamespacesWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element foo = new Element("foo");
        Element bar = new Element("bar");
        Element baz = new Element("baz");
        Attribute attr = foo.getAttribute("attr");
        stack.push(foo);
        stack.push(bar);
        stack.push(baz);
        stack.push(attr);
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getDefaultNamespace()));
        assertTrue(stack.isInScope(foo.getNamespace()));
        assertTrue(stack.isInScope(bar.getNamespace()));
        assertTrue(stack.isInScope(baz.getNamespace()));
        assertTrue(stack.isInScope(attr.getNamespace()));
        assertIterableEquals(Arrays.asList(Namespace.getDefaultNamespace(), foo.getNamespace(), bar.getNamespace(), baz.getNamespace(), attr.getNamespace()), stack.addedForward());
        assertIterableEquals(Arrays.asList(attr.getNamespace(), baz.getNamespace(), bar.getNamespace(), foo.getNamespace(), Namespace.getDefaultNamespace()), stack.addedReverse());
        assertArrayEquals(new Namespace[] { Namespace.getDefaultNamespace(), foo.getNamespace(), bar.getNamespace(), baz.getNamespace(), attr.getNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(Namespace.getDefaultNamespace()));
        assertArrayEquals(new Namespace[] { foo.getNamespace(), bar.getNamespace(), baz.getNamespace(), attr.getNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(foo.getNamespace()));
        assertArrayEquals(new Namespace[] { bar.getNamespace(), baz.getNamespace(), attr.getNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(bar.getNamespace()));
        assertArrayEquals(new Namespace[] { baz.getNamespace(), attr.getNamespace() }, stack.getScope());
        stack.pop();
        assertFalse(stack.isInScope(baz.getNamespace()));
        assertFalse(stack.isInScope(attr.getNamespace()));
        assertNull(stack.getScope());
    }
}