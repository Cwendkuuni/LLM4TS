package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NamespaceResolver}.
 *
 * @author John Doe
 */
public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NamespaceResolver parentResolver;
    private NodePointer pointer;
    private String prefix;
    private String namespaceURI;

    @Before
    public void setUp() {
        parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        pointer = new NodePointer();
        namespaceURI = "http://apache.org/";
        prefix = "ap";
    }

    @Test
    public void testEmptyResolver() {
        assertNull(resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSealed() {
        resolver.seal();
        resolver.registerNamespace(prefix, namespaceURI);
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix)); // Test that it's not a new instance

        parentResolver.registerNamespace(prefix, "anotherURI");
        assertNull(resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetNamespaceURIWithPointer() {
        resolver.registerNamespace(prefix, namespaceURI);
        pointer.setNamespaceContext(resolver);
        pointer.setNamespaceURI(namespaceURI);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetNamespaceURIWithParentPointer() {
        parentResolver.registerNamespace(prefix, namespaceURI);
        pointer.setNamespaceContext(resolver);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(prefix, resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testGetPrefixWithParent() {
        parentResolver.registerNamespace(prefix, namespaceURI);
        assertEquals(prefix, resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testGetPrefixWithRegisteredPrefix() {
        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(prefix, resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testHasSealedParent() {
        parentResolver.seal();
        resolver.seal();
        assertSame(parentResolver, resolver.getParent());
    }

    @Test
    public void testClone() {
        resolver.registerNamespace(prefix, namespaceURI);
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals(namespaceURI, cloned.getNamespaceURI(prefix));
    }

}