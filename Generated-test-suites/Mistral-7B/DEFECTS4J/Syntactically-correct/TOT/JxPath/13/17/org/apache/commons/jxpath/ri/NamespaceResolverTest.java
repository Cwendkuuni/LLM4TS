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
     * Test for registering namespaces.
     */
    @org.junit.Test
    public void testRegisterNamespaces() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map<String, String> map = new HashMap<>();
        map.putAll(resolver.getNamespaceMap());
        assertSame(map, resolver.clone().getNamespaceMap());
        assertEquals("xpath", resolver.getPrefix("http://www.w3.org/2005/XPath"));
        assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertNull(resolver.getPrefix("http://example.com"));
    }

    /**
     * Test for registering namespaces with existing prefixes.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testRegisterNamespacesWithExistingPrefixes() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
        resolver.registerNamespace("xpath", "http://example.com");
    }

    /**
     * Test for setting the namespace context pointer.
     */
    @org.junit.Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("some:node");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getNamespaceContextPointer().getParent());
    }

    /**
     * Test for getting the namespace context pointer.
     */
    @org.junit.Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("some:node");
        resolver.setNamespaceContextPointer(pointer);
        assertSame(pointer, resolver.getNamespaceContextPointer());
        pointer = new NodePointer("some:other:node");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNotSame(pointer, resolver.getNamespaceContextPointer(1));
    }

    /**
     * Test for finding the namespace URI for a prefix.
     */
    @org.junit.Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
        assertEquals("http://www.w3.org/2005/XPath", resolver.getNamespaceURI("xpath"));
        assertNull(resolver.getNamespaceURI("unknown"));

        NodePointer pointer = new NodePointer("some:node");
        resolver.setNamespaceContextPointer(pointer);
        pointer.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
        assertEquals("http://www.w3.org/2001/XMLSchema", pointer.getNamespaceURI("xsd"));
        assertEquals("http://www.w3.org/2005/XPath", resolver.getNamespaceURI("xpath"));
    }

    /**
     * Test for finding the prefix for a namespace URI.
     */
    @org.junit.Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
        assertEquals("xpath", resolver.getPrefix("http://www.w3.org/2005/XPath"));
        assertNull(resolver.getPrefix("http://example.com"));

        NodePointer pointer = new NodePointer("some:node");
        resolver.setNamespaceContextPointer(pointer);
        pointer.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertEquals("xsd", pointer.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertEquals("xpath", resolver.getPrefix("http://www.w3.org/2005/XPath"));
    }

    /**
     * Test for registering namespaces with the same URI but different prefixes.
     */
    @org.junit.Test
    public void testRegisterNamespacesWithSameURIButDifferentPrefixes() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xpath1", "http://www.w3.org/2005/XPath");
        resolver.registerNamespace("xpath2", "http://www.w3.org/2005/XPath");
        assertEquals("xpath1", resolver.getPrefix("http://www.w3.org/2005/XPath"));
        assertEquals("xpath2", resolver.getPrefix("http://www.w3.org/2005/XPath"));
    }

    /**
     * Test for learning whether this NamespaceResolver has been sealed.
     */
    @org.junit.Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        NamespaceResolver cloned = resolver.clone();
        assertFalse(cloned.isSealed());
    }

    /**
     * Test for sealing a NamespaceResolver.
     */
    @org.junit.Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.seal();
        assertSame(resolver.getNamespaceMap(), resolver.clone().getNamespaceMap());
    }

    /**
     * Test for cloning a NamespaceResolver.
     */
    @org.junit.Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertSame(resolver.getNamespaceMap(), cloned.getNamespaceMap());
        assertFalse(resolver.isSealed());
        assertFalse(cloned.isSealed());

        resolver.seal();
        assertSame(resolver.getNamespaceMap(), resolver.clone().getNamespaceMap());
        assertSame(resolver.getNamespaceMap(), cloned.getNamespaceMap());
        assertTrue(resolver.isSealed());
        assertFalse(cloned.isSealed());
    }

    /**
     * Test for getting the nearest prefix found that matches an externally-registered namespace.
     */
    @org.junit.Test
    public void testGetPrefixFromExtRegisteredNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("namespace:node");
        pointer.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("xpath", resolver.getPrefix("http://www.w3.org/2005/XPath"));
        assertEquals("xpath", pointer.getPrefix("http://www.w3.org/2005/XPath"));
    }

    /**
     * Test for getting the nearest prefix found that matches a namespace declared in the context node.
     */
    @org.junit.Test
    public void testGetPrefixFromContextNode() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("namespace:node");
        pointer.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertEquals("xsd", pointer.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    /**
     * Test for getting the nearest prefix found that matches an unregistered namespace.
     */
    @org.junit.Test
    public void testGetPrefixFromUnregisteredNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("namespace:node");
        resolver.setNamespaceContextPointer(pointer);
        assertNull(resolver.getPrefix("http://example.com"));
        assertNull(pointer.getPrefix("http://example.com"));
    }
}