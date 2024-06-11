package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;
    private NodeIterator nodeIterator;
    private NodePointer nodePointer;

    /**
     * Set up mocks and initial objects.
     */
    @Before
    public void setUp() {
        parentNamespaceResolver = mock(NamespaceResolver.class);
        nodeIterator = mock(NodeIterator.class);
        nodePointer = mock(NodePointer.class);

        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNull(namespaceResolver.getParent());
        assertEquals(HashMap.class, namespaceResolver.getNamespaceMap().getClass());
        assertNull(namespaceResolver.getReverseMap());
        assertNull(namespaceResolver.getPointer());
        assertFalse(namespaceResolver.isSealed());
    }

    /**
     * Test registerNamespace method.
     */
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("x", "http://example.com/x");

        assertEquals("x", namespaceResolver.getNamespaceURI("http://example.com/x"));
        assertEquals("http://example.com/x", namespaceResolver.getPrefix("x"));

        assertSame(namespaceResolver, namespaceResolver.clone());

        namespaceResolver.seal();
        try {
            namespaceResolver.registerNamespace("y", "http://example.com/y");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test getNamespaceURI method.
     */
    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("x", "http://example.com/x");

        assertEquals("http://example.com/x", namespaceResolver.getNamespaceURI("x"));
        assertNull(namespaceResolver.getNamespaceURI("y"));

        when(nodePointer.getNamespaceURI()).thenReturn("http://example.com/ns");
        when(nodePointer.getName().getName()).thenReturn("prefix");

        when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
        when(nodeIterator.setPosition(1)).thenReturn(true);

        namespaceResolver.setNamespaceContextPointer(nodePointer);
        namespaceResolver.registerNamespace("ns", "http://example.com/ns");

        assertEquals("http://example.com/ns", namespaceResolver.getNamespaceURI("ns"));
        assertNull(namespaceResolver.getNamespaceURI("x"));
        assertEquals("prefix", namespaceResolver.getPrefix("http://example.com/ns"));
        assertEquals("http://example.com/ns", namespaceResolver.getNamespaceURI("prefix"));

        namespaceResolver.seal();
        assertNull(namespaceResolver.getNamespaceURI("x"));
        assertNull(namespaceResolver.getNamespaceURI("y"));
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    /**
     * Test getPrefix method.
     */
    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("x", "http://example.com/x");

        assertEquals("x", namespaceResolver.getPrefix("http://example.com/x"));
        assertNull(namespaceResolver.getPrefix("http://example.com/y"));

        namespaceResolver.registerNamespace("y", "http://example.com/y");
        assertEquals("y", namespaceResolver.getPrefix("http://example.com/y"));

        namespaceResolver.seal();
        assertNull(namespaceResolver.getPrefix("http://example.com/x"));
        assertNull(namespaceResolver.getPrefix("http://example.com/y"));
    }

    /**
     * Test isSealed method.
     */
    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());

        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        assertFalse(namespaceResolver.isSealed());
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver cloned = (NamespaceResolver) namespaceResolver.clone();

        assertEquals("x", cloned.getNamespaceURI("http://example.com/x"));
        assertEquals("http://example.com/x", cloned.getPrefix("x"));

        cloned.registerNamespace("y", "http://example.com/y");
        assertEquals("x", cloned.getNamespaceURI("http://example.com/x"));
        assertEquals("http://example.com/x", cloned.getPrefix("x"));
        assertEquals("y", cloned.getNamespaceURI("http://example.com/y"));
        assertEquals("http://example.com/y", cloned.getPrefix("y"));

        namespaceResolver.seal();
        cloned.seal();

        assertEquals("x", namespaceResolver.getNamespaceURI("http://example.com/x"));
        assertEquals("http://example.com/x", namespaceResolver.getPrefix("x"));
        assertEquals("y", cloned.getNamespaceURI("http://example.com/y"));
        assertEquals("http://example.com/y", cloned.getPrefix("y"));

        assertNull(namespaceResolver.getNamespaceURI("http://example.com/x"));
        assertNull(namespaceResolver.getNamespaceURI("http://example.com/y"));
        assertNull(namespaceResolver.getNamespaceURI("http://example.com/z"));

        assertNull(cloned.getNamespaceURI("http://example.com/x"));
        assertNull(cloned.getNamespaceURI("http://example.com/y"));
        assertNull(cloned.getNamespaceURI("http://example.com/z"));
    }

}