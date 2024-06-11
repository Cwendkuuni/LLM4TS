package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.Namespaces;

/**
 * Test class for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    /**
     * Tests constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clone = resolver.clone();
        assertEquals(resolver, clone);
        assertSame(resolver.parent, clone.parent);
        assertNull(resolver.getNamespaceURI("x"));
        assertNull(resolver.getPrefix("http://apache.org/"));
    }

    /**
     * Tests registering namespace.
     */
    @org.junit.Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://apache.org/");
        assertEquals("http://apache.org/", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://apache.org/"));
        NamespaceResolver clone = resolver.clone();
        assertNull(clone.getNamespaceURI("x"));
        assertNull(clone.getPrefix("http://apache.org/"));
    }

    /**
     * Tests setting namespace context pointer.
     */
    @org.junit.Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(new Object(), Namespaces.NULL_NS, "root");
        resolver.setNamespaceContextPointer(pointer);
        assertSame(pointer, resolver.getNamespaceContextPointer());
    }

    /**
     * Tests getting namespace URI.
     */
    @org.junit.Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://apache.org/");
        assertEquals("http://apache.org/", resolver.getNamespaceURI("x"));
        assertEquals("http://apache.org/", resolver.getNamespaceURI("x"));
        NamespaceResolver clone = resolver.clone();
        assertNull(clone.getNamespaceURI("x"));
    }

    /**
     * Tests getting prefix.
     */
    @org.junit.Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://apache.org/");
        assertEquals("x", resolver.getPrefix("http://apache.org/"));
        assertEquals("x", resolver.getPrefix("http://apache.org/"));
        NamespaceResolver clone = resolver.clone();
        assertNull(clone.getPrefix("http://apache.org/"));
    }

    /**
     * Tests learning whether sealed.
     */
    @org.junit.Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    /**
     * Tests cloning.
     */
    @org.junit.Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://apache.org/");
        NamespaceResolver clone = resolver.clone();
        assertEquals(resolver, clone);
        assertNull(clone.getNamespaceURI("x"));
        assertNull(clone.getPrefix("http://apache.org/"));
    }
}