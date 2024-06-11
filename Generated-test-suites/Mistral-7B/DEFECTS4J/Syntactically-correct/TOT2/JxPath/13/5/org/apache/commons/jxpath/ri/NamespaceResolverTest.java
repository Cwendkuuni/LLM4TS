package org.apache.commons.jxpath.ri.test;

import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        String namespaceURI = resolver.getNamespaceURI("xsi");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceURI);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURINull() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = resolver.getNamespaceURI(null);
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("namespace:test");
        resolver.setNamespaceContextPointer(pointer);
        Pointer actual = resolver.getNamespaceContextPointer();
        assertEquals(pointer, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNamespaceContextPointerNull() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = null;
        resolver.setNamespaceContextPointer(pointer);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", prefix);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPrefixNull() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = resolver.getPrefix(null);
    }

    @Test
    public void testGetPrefixUnknownNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = resolver.getPrefix("http://example.com");
        assertNull(prefix);
    }
}