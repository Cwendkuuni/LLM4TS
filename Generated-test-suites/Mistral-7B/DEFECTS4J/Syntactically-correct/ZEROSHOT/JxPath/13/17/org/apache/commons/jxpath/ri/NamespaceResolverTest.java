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
 * NamespaceResolverTest class.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;
    private NodePointer nodePointer;
    private String prefix;
    private String namespaceURI;

    /**
     * Sets up the NamespaceResolverTest.
     */
    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        parentNamespaceResolver = new NamespaceResolver(null);
        nodePointer = new NodePointer(new Object(), "element");
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertEquals(null, namespaceResolver.getParent());
        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(namespaceResolver);
        assertEquals(namespaceResolver, namespaceResolverWithParent.getParent());
    }

    /**
     * Test registerNamespace method.
     */
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertNull(namespaceResolver.getPrefix("http://www.w3.org/1999/XPath"));

        namespaceResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        assertEquals("xmlns", namespaceResolver.getPrefix("http://www.w3.org/2000/xmlns/"));
        assertEquals("xmlns", namespaceResolver.getPrefix(""));

        namespaceResolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        assertNull(namespaceResolver.getPrefix("http://www.w3.org/1999/XPath"));

        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("xsi", namespaceResolver.getPrefix("{http://www.w3.org/2001/XMLSchema-instance}"));

        namespaceResolver.registerNamespace("test", "test");
        assertEquals("test", namespaceResolver.getPrefix("test"));
        assertEquals("test", namespaceResolver.getPrefix("{test}"));

        namespaceResolver.registerNamespace("test2", "test");
        assertEquals("test", namespaceResolver.getPrefix("test"));
        assertEquals("test2", namespaceResolver.getPrefix("{test2}"));

        namespaceResolver.registerNamespace("test3", "test");
        assertEquals("test", namespaceResolver.getPrefix("test"));
        assertEquals("test3", namespaceResolver.getPrefix("{test3}"));
        assertEquals("test3", namespaceResolver.getPrefix("{test}test3"));

        namespaceResolver.registerNamespace("test4", "test");
        assertEquals("test", namespaceResolver.getPrefix("test"));
        assertEquals("test4", namespaceResolver.getPrefix("{test4}"));
        assertEquals("test4", namespaceResolver.getPrefix("{test}test4"));
        assertEquals("test4", namespaceResolver.getPrefix("{test2}test4"));
        assertEquals("test4", namespaceResolver.getPrefix("{test3}test4"));
        assertEquals("test4", namespaceResolver.getPrefix("{test2}test3:test4"));
        assertEquals("test4", namespaceResolver.getPrefix("{test3}test3:test3:test4"));

        namespaceResolver.registerNamespace("test5", "test");
        assertEquals("test", namespaceResolver.getPrefix("test"));
        assertEquals("test5", namespaceResolver.getPrefix("{test5}"));
        assertEquals("test5", namespaceResolver.getPrefix("{test}:test5"));
        assertEquals("test5", namespaceResolver.getPrefix("{test2}:test2:test5"));
        assertEquals("test5", namespaceResolver.getPrefix("{test3}:test3:test3:test5"));
        assertEquals("test5", namespaceResolver.getPrefix("{test3}:test3:test3:test3:test5"));
        assertEquals("test5", namespaceResolver.getPrefix("{test2}:test2:test3:test3:test3:test5"));
    }

    /**
     * Test registerNamespace method with sealed NamespaceResolver.
     */
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    /**
     * Test getNamespaceURI method.
     */
    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        assertNull(namespaceResolver.getNamespaceURI("ns1"));

        parentNamespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        namespaceResolver.setParent(parentNamespaceResolver);
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        assertNull(namespaceResolver.getNamespaceURI("ns1"));

        namespaceResolver.registerNamespace("ns1", "http://example.com/ns1");
        assertEquals("http://example.com/ns1", namespaceResolver.getNamespaceURI("ns1"));
        assertEquals("http://example.com/ns1", namespaceResolver.getNamespaceURI("{http://example.com/ns1}"));
    }

    /**
     * Test getPrefix method.
     */
    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertNull(namespaceResolver.getPrefix("ns1"));

        parentNamespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        parentNamespaceResolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        namespaceResolver.setParent(parentNamespaceResolver);
        assertEquals("xsd", namespaceResolver.getPrefix("xsd"));
        assertEquals("xml", namespaceResolver.getPrefix("xml"));
        assertNull(namespaceResolver.getPrefix("ns1"));

        namespaceResolver.registerNamespace("ns1", "http://example.com/ns1");
        assertEquals("ns1", namespaceResolver.getPrefix("{http://example.com/ns1}"));
        assertEquals("ns1", namespaceResolver.getPrefix("ns1"));
    }

    /**
     * Test getPrefix method with sealed NamespaceResolver.
     */
    @Test
    public void testGetPrefixSealed() {
        namespaceResolver.seal();
        assertNull(namespaceResolver.getPrefix("xsd"));
    }

    /**
     * Test setNamespaceContextPointer method.
     */
    @Test
    public void testSetNamespaceContextPointer() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertSame(nodePointer, namespaceResolver.getNamespaceContextPointer());

        parentNamespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        parentNamespaceResolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        namespaceResolver.setParent(parentNamespaceResolver);
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertSame(nodePointer, namespaceResolver.getNamespaceContextPointer());
    }

    /**
     * Test isSealed method.
     */
    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
        assertFalse(parentNamespaceResolver.isSealed());
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
        assertEquals("xsd", clone.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }
}