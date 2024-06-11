package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespacesResolver;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        NamespacesResolver resolver = new NamespacesResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.size());

        NamespacesResolver parentResolver = new NamespacesResolver();
        NamespacesResolver resolverWithParent = new NamespacesResolver(parentResolver);
        assertEquals(parentResolver, resolverWithParent.getParent());
        assertNull(parentResolver.getParent());
    }

    /**
     * Test registerNamespace method.
     */
    @org.junit.Test
    public void testRegisterNamespace() {
        NamespacesResolver resolver = new NamespacesResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));

        assertNull(resolver.getPrefix("http://www.w3.org/1999/xhtml"));
        assertNull(resolver.getNamespaceURI("foobar"));

        NamespacesResolver parentResolver = new NamespacesResolver();
        parentResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        NamespacesResolver resolverWithParent = new NamespacesResolver(parentResolver);
        resolverWithParent.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        assertEquals("xmlns", resolverWithParent.getParent().getPrefix("http://www.w3.org/2000/xmlns/"));
        assertEquals("http://www.w3.org/2000/xmlns/", resolverWithParent.getParent().getNamespaceURI("xmlns"));
        assertEquals("xsi", resolverWithParent.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolverWithParent.getNamespaceURI("xsi"));

        // Test if parent's namespaces are available in child
        assertEquals("xmlns", resolverWithParent.getPrefix("http://www.w3.org/2000/xmlns/"));
        assertEquals("http://www.w3.org/2000/xmlns/", resolverWithParent.getNamespaceURI("xmlns"));
    }

    /**
     * Test getNamespaceContextPointer method.
     */
    @org.junit.Test
    public void testGetNamespaceContextPointer() {
        NamespacesResolver resolver = new NamespacesResolver();
        Pointer pointer = new NodePointer(new Object());
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getParent().getNamespaceContextPointer());

        // Test if parent's namespace context pointer is available in child
        Pointer parentPointer = new NodePointer(new Object());
        NamespacesResolver parentResolver = new NamespacesResolver();
        parentResolver.setNamespaceContextPointer(parentPointer);
        NamespacesResolver resolverWithParent = new NamespacesResolver(parentResolver);
        assertEquals(pointer, resolverWithParent.getNamespaceContextPointer());
        assertEquals(parentPointer, parentResolver.getNamespaceContextPointer());
    }

    /**
     * Test getNamespaceURI method.
     */
    @org.junit.Test
    public void testGetNamespaceURI() {
        NamespacesResolver resolver = new NamespacesResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
        assertEquals("", resolver.getNamespaceURI(""));
        assertNull(resolver.getNamespaceURI("foobar"));

        NamespacesResolver parentResolver = new NamespacesResolver();
        parentResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        NamespacesResolver resolverWithParent = new NamespacesResolver(parentResolver);
        assertEquals("http://www.w3.org/2000/xmlns/", resolverWithParent.getNamespaceURI("xmlns"));
        assertEquals("", resolverWithParent.getNamespaceURI(""));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolverWithParent.getNamespaceURI("xsi"));
        assertNull(resolverWithParent.getNamespaceURI("foobar"));

        // Test multiple registered namespaces
        resolver = new NamespacesResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
        assertEquals("", resolver.getNamespaceURI(""));
        assertNull(resolver.getNamespaceURI("foobar"));
    }

    /**
     * Test getPrefix method.
     */
    @org.junit.Test
    public void testGetPrefix() {
        NamespacesResolver resolver = new NamespacesResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("", resolver.getPrefix(""));
        assertNull(resolver.getPrefix("foobar"));

        NamespacesResolver parentResolver = new NamespacesResolver();
        parentResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        NamespacesResolver resolverWithParent = new NamespacesResolver(parentResolver);
        assertEquals("xmlns", resolverWithParent.getPrefix("http://www.w3.org/2000/xmlns/"));
        assertEquals("", resolverWithParent.getPrefix(""));
        assertEquals("xsi", resolverWithParent.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertNull(resolverWithParent.getPrefix("foobar"));

        // Test multiple registered namespaces
        resolver = new NamespacesResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertEquals("", resolver.getPrefix(""));
        assertNull(resolver.getPrefix("foobar"));
    }

    /**
     * Test isSealed method.
     */
    @org.junit.Test
    public void testIsSealed() {
        NamespacesResolver resolver = new NamespacesResolver();
        assertFalse(resolver.isSealed());

        NamespacesResolver parentResolver = new NamespacesResolver();
        NamespacesResolver resolverWithParent = new NamespacesResolver(parentResolver);
        assertFalse(resolverWithParent.isSealed());

        resolver = new NamespacesResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
        assertFalse(resolver.getParent().isSealed());

        resolverWithParent = new NamespacesResolver(resolver);
        assertFalse(resolverWithParent.isSealed());
        assertTrue(resolver.isSealed());
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testClone() {
        NamespacesResolver parentResolver = new NamespacesResolver();
        parentResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        parentResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        NamespacesResolver resolver = new NamespacesResolver(parentResolver);
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        NamespacesResolver clonedResolver = (NamespacesResolver) resolver.clone();
        assertEquals(resolver, clonedResolver);
        assertEquals(parentResolver, clonedResolver.getParent());

        Map namespaceMap = resolver.getNamespaceMap();
        Map clonedNamespaceMap = clonedResolver.getNamespaceMap();
        assertEquals(namespaceMap.size(), clonedNamespaceMap.size());
        for (Object key : namespaceMap.keySet()) {
            assertEquals(namespaceMap.get(key), clonedNamespaceMap.get(key));
        }
    }
}