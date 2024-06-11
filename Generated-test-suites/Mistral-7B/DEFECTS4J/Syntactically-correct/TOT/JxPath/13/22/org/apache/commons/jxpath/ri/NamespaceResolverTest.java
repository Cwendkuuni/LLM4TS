package org.apache.commons.jxpath.ri.test;

import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link NamespaceResolver} class.
 */
public class NamespaceResolverTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getParent());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    /**
     * Test registering namespaces.
     */
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        Assert.assertEquals("x", resolver.getNamespaceURI("x"));
        Assert.assertNull(resolver.getNamespaceURI("y"));
    }

    /**
     * Test registering namespaces with the same prefix but different URIs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceWithSamePrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x1");
        resolver.registerNamespace("x", "http://example.com/x2");
    }

    /**
     * Test setting context pointer.
     */
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        NodePointer pointer = new NodePointer();
        pointer.setNode(new Object());
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    /**
     * Test getting namespace URIs.
     */
    @Test
    public void testGetNamespaceURI() {
        Map<String, String> namespaceMap = new HashMap<>();
        namespaceMap.put("x", "http://example.com/x");
        NamespaceResolver resolver = new NamespaceResolver() {{
            this.namespaceMap = namespaceMap;
            this.reverseMap = new HashMap<>(namespaceMap);
        }};
        Assert.assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        Assert.assertNull(resolver.getNamespaceURI("y"));

        NodePointer pointer = new NodePointer();
        pointer.setNode(new Object());
        pointer.setNamespaceURI("http://example.com/ns");
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals("http://example.com/ns", resolver.getNamespaceURI(""));
        Assert.assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
    }

    /**
     * Test getting prefixes.
     */
    @Test
    public void testGetPrefix() {
        Map<String, String> namespaceMap = new HashMap<>();
        namespaceMap.put("x", "http://example.com/x");
        NamespaceResolver resolver = new NamespaceResolver() {{
            this.namespaceMap = namespaceMap;
            this.reverseMap = new HashMap<>(namespaceMap);
        }};
        Assert.assertEquals("x", resolver.getPrefix("http://example.com/x"));
        Assert.assertNull(resolver.getPrefix("http://example.com/y"));

        NodePointer pointer = new NodePointer();
        pointer.setNode(new Object());
        pointer.setNamespaceURI("http://example.com/ns");
        pointer.setName("ns:prefix");
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals("ns", resolver.getPrefix("http://example.com/ns"));
    }

    /**
     * Test sealing.
     */
    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.seal();
        try {
            resolver.registerNamespace("y", "http://example.com/y");
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test cloning.
     */
    @Test
    public void testClone() {
        Map<String, String> namespaceMap = new HashMap<>();
        namespaceMap.put("x", "http://example.com/x");
        NamespaceResolver resolver = new NamespaceResolver() {{
            this.namespaceMap = namespaceMap;
            this.reverseMap = new HashMap<>(namespaceMap);
        }};
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        Assert.assertNotSame(resolver, cloned);
        Assert.assertEquals(resolver.getNamespaceURI("x"), cloned.getNamespaceURI("x"));
    }
}