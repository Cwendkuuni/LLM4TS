package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NamespaceResolver parentResolver;

    @Before
    public void setUp() {
        parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
    }

    @Test
    public void testCreateNewNamespaceResolver() {
        assertNull(resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testCreateNamespaceResolverWithParent() {
        assertNull(parentResolver.getParent());
        assertNull(parentResolver.getNamespaceMap());
        assertNull(parentResolver.getReverseMap());
        assertNull(parentResolver.getPointer());
        assertFalse(parentResolver.isSealed());

        assertNull(resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());

        assertEquals(parentResolver, resolver.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map<String, String> namespaceMap = resolver.getNamespaceMap();
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceMap.get("xsd"));
        assertEquals("xsd", reverseMapValue(namespaceMap, "http://www.w3.org/2001/XMLSchema"));

        resolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        namespaceMap = resolver.getNamespaceMap();
        assertEquals("http://www.w3.org/2000/xmlns/", namespaceMap.get("xmlns"));
        assertEquals("xmlns", reverseMapValue(namespaceMap, "http://www.w3.org/2000/xmlns/"));

        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        namespaceMap = resolver.getNamespaceMap();
        assertEquals("http://www.w3.org/XML/1998/namespace", namespaceMap.get("xml"));
        assertEquals("xml", reverseMapValue(namespaceMap, "http://www.w3.org/XML/1998/namespace"));

        try {
            resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            fail("Expected exception: Cannot register namespaces on a sealed NamespaceResolver");
        } catch (IllegalStateException e) {
            // Expected
        }

        resolver.seal();
        try {
            resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            fail("Expected exception: Cannot register namespaces on a sealed NamespaceResolver");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        String uri = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);

        uri = resolver.getNamespaceURI("xmlns");
        assertEquals("http://www.w3.org/2000/xmlns/", uri);

        uri = resolver.getNamespaceURI("xml");
        assertEquals("http://www.w3.org/XML/1998/namespace", uri);

        uri = resolver.getNamespaceURI("unknown");
        assertNull(uri);

        Pointer pointer = new NodePointer("test");
        resolver.setNamespaceContextPointer(pointer);

        uri = resolver.getNamespaceURI("xml:foo");
        assertEquals("http://www.w3.org/XML/1998/namespace", uri);

        resolver.registerNamespace("my", "http://example.com");
        uri = resolver.getNamespaceURI("my");
        assertEquals("http://example.com", uri);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        String prefix = resolver.getPrefix("xsd");
        assertEquals("xsd", prefix);

        prefix = resolver.getPrefix("xmlns");
        assertNull(prefix);

        prefix = resolver.getPrefix("xml");
        assertEquals("xml", prefix);

        prefix = resolver.getPrefix("unknown");
        assertNull(prefix);

        Pointer pointer = new NodePointer("test");
        resolver.setNamespaceContextPointer(pointer);

        prefix = resolver.getPrefix("xml:foo");
        assertEquals("xml", prefix);

        resolver.registerNamespace("my", "http://example.com");
        prefix = resolver.getPrefix("my");
        assertEquals("my", prefix);

        parentResolver.registerNamespace("my", "http://another-example.com");
        assertEquals("my", reverseMapValue(resolver.getNamespaceMap(), "http://example.com"));
        assertEquals("my", reverseMapValue(parentResolver.getNamespaceMap(), "http://another-example.com"));
        assertEquals("http://example.com", reverseMapValue(parentResolver.getNamespaceMap(), "my"));
        assertEquals("http://another-example.com", reverseMapValue(resolver.getNamespaceMap(), "my"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        parentResolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        Map<String, String> namespaceMap = resolver.getNamespaceMap();
        Map<String, String> clonedNamespaceMap = clonedResolver.getNamespaceMap();
        assertEquals(namespaceMap.size(), clonedNamespaceMap.size());
        for (Map.Entry<String, String> entry : namespaceMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clonedKey = (String) clonedNamespaceMap.get(key);
            String clonedValue = (String) clonedNamespaceMap.get(value);
            assertEquals(key, clonedKey);
            assertEquals(value, clonedValue);
        }

        resolver.seal();
        clonedResolver = (NamespaceResolver) resolver.clone();
        assertTrue(clonedResolver.isSealed());

        resolver.pointer = new NodePointer("test");
        clonedResolver.pointer = new NodePointer("test");
        assertEquals(resolver.pointer, clonedResolver.pointer);
    }

    private String reverseMapValue(Map<String, String> map, String value) {
        return (String) map.get(value);
    }
}