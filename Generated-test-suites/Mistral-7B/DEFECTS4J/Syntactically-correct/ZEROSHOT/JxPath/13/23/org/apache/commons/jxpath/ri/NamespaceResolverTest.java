package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    /**
     * Test constructor, default.
     */
    @Test
    public void testConstructorDefault() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertNull(namespaceResolver.getParent());
        assertNotNull(namespaceResolver.getNamespaceMap());
        assertNull(namespaceResolver.getReverseMap());
        assertNull(namespaceResolver.getPointer());
        assertFalse(namespaceResolver.isSealed());
    }

    /**
     * Test constructor with parent.
     */
    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, childResolver.getParent());
        assertNotNull(childResolver.getNamespaceMap());
        assertNull(childResolver.getReverseMap());
        assertNull(childResolver.getPointer());
        assertFalse(childResolver.isSealed());
    }

    /**
     * Test method registerNamespace.
     */
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", namespaceResolver.getNamespaceURI("http://www.w3.org/2001/XMLSchema"));
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        assertNull(namespaceResolver.getNamespaceURI("unknown"));

        try {
            namespaceResolver.registerNamespace("xsd", "http://www.w3.org/1999/XPath");
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        namespaceResolver.seal();
        try {
            namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test method getNamespaceURI.
     */
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd:anyType"));
        assertNull(namespaceResolver.getNamespaceURI("unknown"));
        assertNull(namespaceResolver.getNamespaceURI(""));

        namespaceResolver.setNamespaceContextPointer(new NodePointer(new Object(), new String[]{"o"}));
        namespaceResolver.registerNamespace("xmlns", "http://www.w3.org/XML/1998/namespace");
        assertEquals("http://www.w3.org/XML/1998/namespace", namespaceResolver.getNamespaceURI("xmlns"));
        assertEquals("http://www.w3.org/XML/1998/namespace", namespaceResolver.getNamespaceURI("xmlns:xml"));
        assertEquals("o", namespaceResolver.getNamespaceURI(""));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver.setParent(parentResolver);
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi:schemaLocation"));
    }

    /**
     * Test method getPrefix.
     */
    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertEquals("xsd", namespaceResolver.getPrefix("xsd:anyType"));
        assertNull(namespaceResolver.getPrefix("unknown"));
        assertNull(namespaceResolver.getPrefix(""));

        namespaceResolver.seal();
        assertNull(namespaceResolver.getPrefix("xsd"));

        namespaceResolver.setNamespaceContextPointer(new NodePointer(new Object(), new String[]{"o"}));
        namespaceResolver.registerNamespace("xmlns", "http://www.w3.org/XML/1998/namespace");
        assertEquals("xmlns", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        assertEquals("xmlns", namespaceResolver.getPrefix("xmlns:xml"));
        assertEquals("o", namespaceResolver.getPrefix(""));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver.setParent(parentResolver);
        assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("xsi", namespaceResolver.getPrefix("xsi:schemaLocation"));
    }

    /**
     * Test method isSealed.
     */
    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertFalse(namespaceResolver.isSealed());

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());

        namespaceResolver.setParent(new NamespaceResolver());
        assertFalse(namespaceResolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        parentResolver.seal();
        namespaceResolver.setParent(parentResolver);
        assertTrue(namespaceResolver.isSealed());
    }

    /**
     * Test method clone.
     */
    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
        assertEquals(namespaceResolver, clone);
        assertNotSame(namespaceResolver, clone);
        assertEquals("xsd", clone.getNamespaceURI("xsd"));
        assertEquals("xsd", clone.getNamespaceURI("xsd:anyType"));
        assertNull(clone.getNamespaceURI("unknown"));
        assertNull(clone.getNamespaceURI(""));
        assertFalse(clone.isSealed());

        namespaceResolver.seal();
        clone = (NamespaceResolver) namespaceResolver.clone();
        assertEquals(namespaceResolver, clone);
        assertNotSame(namespaceResolver, clone);
        assertEquals("xsd", clone.getNamespaceURI("xsd"));
        assertEquals("xsd", clone.getNamespaceURI("xsd:anyType"));
        assertNull(clone.getNamespaceURI("unknown"));
        assertNull(clone.getNamespaceURI(""));
        assertTrue(clone.isSealed());
    }
}