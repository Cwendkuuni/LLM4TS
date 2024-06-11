package org.junit.tests.apache.commons.jxpath.ri;

import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.example.com";
        String prefix = "prefix";
        resolver.registerNamespace(prefix, namespaceURI);

        String result = resolver.getNamespaceURI(prefix);

        assertNotNull("Namespace URI should not be null", result);
        assertEquals("Namespace URI should match the registered prefix", namespaceURI, result);
    }

    @Test
    public void testGetNamespaceURIWithUnregisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";

        String result = resolver.getNamespaceURI(prefix);

        assertNull("Namespace URI should be null for an unregistered prefix", result);
    }

    @Test
    public void testGetNamespaceURIWithNullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();

        String result = resolver.getNamespaceURI(null);

        assertNull("Namespace URI should be null for a null prefix", result);
    }

    @Test
    public void testGetNamespaceURIWithEmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String emptyNamespaceURI = "";

        resolver.registerNamespace(prefix, emptyNamespaceURI);

        String result = resolver.getNamespaceURI(prefix);

        assertNull("Namespace URI should be null for an empty namespaceURI", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURIWithNullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String nullNamespaceURI = null;

        resolver.registerNamespace(prefix, nullNamespaceURI);

        String result = resolver.getNamespaceURI(prefix);

        fail("Namespace URI should not be null, but was returned as null");
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURIWithNullResolver() {
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";

        NamespaceResolver nullResolver = null;

        String result = nullResolver.getNamespaceURI(prefix);

        fail("Namespace URI should not be null, but was returned as null");
    }
}