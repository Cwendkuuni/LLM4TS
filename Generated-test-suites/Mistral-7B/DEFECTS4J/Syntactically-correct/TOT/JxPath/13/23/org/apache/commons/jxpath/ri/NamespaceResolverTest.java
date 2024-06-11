package org.apache.commons.jxpath.ri.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;

/**
 * Tests for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    /**
     * Test method for {@link NamespaceResolver#NamespaceResolver()}.
     */
    @org.junit.Test
    public void testNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
    }

    /**
     * Test method for {@link NamespaceResolver#NamespaceResolver(NamespaceResolver)}.
     */
    @org.junit.Test
    public void testNamespaceResolverWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertSame(parent, resolver.getParent());
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
    }

    /**
     * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
     */
    @org.junit.Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
    }

    /**
     * Test method for {@link NamespaceResolver#registerNamespace(String, String)}
     * with conflicting prefix.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceConflictingPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.registerNamespace("x", "http://example.org/x");
    }

    /**
     * Test method for {@link NamespaceResolver#setNamespaceContextPointer(NodePointer)}.
     */
    @org.junit.Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    /**
     * Test method for {@link NamespaceResolver#getNamespaceContextPointer()}.
     */
    @org.junit.Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    /**
     * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
     */
    @org.junit.Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals(null, resolver.getNamespaceURI("y"));

        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        resolver.registerNamespace("x", "http://example.org/x");
        assertEquals("http://example.org/x", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
    }

    /**
     * Test method for {@link NamespaceResolver#getPrefix(String)}.
     */
    @org.junit.Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals(null, resolver.getPrefix("http://example.org/x"));

        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        resolver.registerNamespace("x", "http://example.org/x");
        assertEquals("x", resolver.getPrefix("http://example.org/x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
    }

    /**
     * Test method for {@link NamespaceResolver#seal()}.
     */
    @org.junit.Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.seal();
        try {
            resolver.registerNamespace("x", "http://example.org/x");
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link NamespaceResolver#clone()}.
     */
    @org.junit.Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/x", cloned.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("x", cloned.getPrefix("http://example.com/x"));

        resolver.registerNamespace("y", "http://example.org/y");
        assertNull(cloned.getNamespaceURI("y"));
        assertNull(cloned.getPrefix("http://example.org/y"));
    }
}