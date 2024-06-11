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
import org.jdom2.internal.ArrayCopy;
import org.jdom2.util.NamespaceIterable;
import org.jdom2.util.NamespaceStack;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testNewInstance() {
        NamespaceStack stack = new NamespaceStack();
        assertNull(stack.getScope());
        assertEquals(0, stack.depth);
    }

    @Test
    public void testNewInstanceWithSeed() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE });
        Namespace[] scope = stack.getScope();
        assertArrayEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, scope);
        assertEquals(2, stack.depth);
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(1, stack.depth);

        element.setNamespace("http://example.com/");
        stack.push(element);
        scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals("http://example.com/", scope[0].getURI());
        assertEquals("http://example.com/", stack.scope[stack.depth][0].getURI());
        assertEquals(2, stack.depth);
    }

    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        element.setNamespace("http://example.com/");
        element.setAdditionalNamespaces(Arrays.asList(new Namespace("x", "http://example.com/x"), new Namespace("y", "http://example.com/y")));
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertEquals(3, scope.length);
        assertEquals("http://example.com/", scope[0].getURI());
        assertEquals("http://example.com/x", scope[1].getURI());
        assertEquals("http://example.com/y", scope[2].getURI());
        assertEquals(2, stack.depth);
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("foo", "bar");
        stack.push(attribute);
        Namespace[] scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(1, stack.depth);

        attribute.setNamespace("http://example.com/");
        stack.push(attribute);
        scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals("http://example.com/", scope[0].getURI());
        assertEquals("http://example.com/", stack.scope[stack.depth][0].getURI());
        assertEquals(2, stack.depth);
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        element.setNamespace("http://example.com/");
        stack.push(element);
        assertEquals(1, stack.depth);
        stack.pop();
        assertEquals(0, stack.depth);
        assertNull(stack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        element.setNamespace("http://example.com/");
        stack.push(element);
        List<Namespace> namespaces = stack.addedForward();
        assertEquals(1, namespaces.size());
        assertEquals("http://example.com/", namespaces.get(0).getURI());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        element.setNamespace("http://example.com/");
        stack.push(element);
        List<Namespace> namespaces = stack.addedReverse();
        assertEquals(1, namespaces.size());
        assertEquals("http://example.com/", namespaces.get(0).getURI());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        element.setNamespace("http://example.com/");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
        assertTrue(stack.isInScope(new Namespace("x", "http://example.com/x")));
        assertTrue(stack.isInScope(new Namespace("y", "http://example.com/y")));
        assertFalse(stack.isInScope(new Namespace("z", "http://example.com/z")));
    }
}