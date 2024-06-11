package org.junit.test;

import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "test";
        String namespaceURI = "http://test.com";
        resolver.registerNamespace(prefix, namespaceURI);

        String result = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI, result);
    }

    @Test
    public void testGetNamespaceURI_NonExistentPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "test";
        String result = resolver.getNamespaceURI(prefix);
        assertNull(result);
    }

    @Test
    public void testGetNamespaceURI_SealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "test";
        String namespaceURI = "http://test.com";
        resolver.registerNamespace(prefix, namespaceURI);
        resolver.seal();

        try {
            resolver.registerNamespace(prefix, "http://anotherTest.com");
            fail();
        } catch (IllegalStateException e) {
            //Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURI_NullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = null;
        String result = resolver.getNamespaceURI(prefix);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURI_NullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "test";
        String namespaceURI = null;
        resolver.registerNamespace(prefix, namespaceURI);

        String result = resolver.getNamespaceURI(prefix);
    }

    @Test
    public void testGetNamespaceURI_AlreadyRegisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "test";
        String namespaceURI1 = "http://test1.com";
        String namespaceURI2 = "http://test2.com";

        resolver.registerNamespace(prefix, namespaceURI1);
        String result = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI1, result);

        resolver.registerNamespace(prefix, namespaceURI2);
        String result2 = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI1, result2);
    }
}