package org.apache.commons.jxpath.ri.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Name;
import org.apache.commons.jxpath.NodeContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.util.TestUtil;

/**
 * Tests for {@link NamespaceResolver} class.
 */
public class NamespaceResolverTest {

    /**
     * Test constructors.
     */
    @org.junit.Test
    public void testConstructors() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.size());

        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, resolverWithParent.getParent());
    }

    /**
     * Test registering namespaces.
     */
    @org.junit.Test
    public void testRegisterNamespaces() {
        NamespaceResolver resolver = new NamespaceResolver();

        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals(2, resolver.size());

        String xsiPrefix = resolver.getNamespaceURI("xsi");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xsiPrefix);

        String xsdPrefix = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", xsdPrefix);

        String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", prefix);

        String prefix2 = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", prefix2);

        Map namespaceMap = resolver.getNamespaceMap();
        assertEquals(2, namespaceMap.size());
        assertEquals("xsi", namespaceMap.get("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("xsd", namespaceMap.get("http://www.w3.org/2001/XMLSchema"));

        Map reverseMap = resolver.getReverseNamespaceMap();
        assertEquals(2, reverseMap.size());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", reverseMap.get("xsi"));
        assertEquals("http://www.w3.org/2001/XMLSchema", reverseMap.get("xsd"));
    }

    /**
     * Test setting namespace context pointer.
     */
    @org.junit.Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        NodeContext namespaceContext = TestUtil.createTestNodeContext();
        Pointer pointer = new NodePointer(namespaceContext, new Name("test"));
        resolver.setNamespaceContextPointer(pointer);

        String xsiPrefix = resolver.getNamespaceURI("xsi");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xsiPrefix);

        String xsiPrefix2 = resolver.getNamespaceURI("xsi");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xsiPrefix2);

        Map namespaceMap = resolver.getNamespaceMap();
        assertEquals(1, namespaceMap.size());
        assertEquals("xsi", namespaceMap.get("http://www.w3.org/2001/XMLSchema-instance"));

        Map reverseMap = resolver.getReverseNamespaceMap();
        assertEquals(1, reverseMap.size());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", reverseMap.get("xsi"));
    }

    /**
     * Test getting namespace URI.
     */
    @org.junit.Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        String xsiPrefix = resolver.getNamespaceURI("xsi");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xsiPrefix);

        String xsdPrefix = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", xsdPrefix);

        NodeContext namespaceContext = TestUtil.createTestNodeContext();
        NodePointer nodePointer = new NodePointer(namespaceContext, new Name("test"));
        resolver.setNamespaceContextPointer(nodePointer);
        String xsiPrefix2 = resolver.getNamespaceURI("xsi");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xsiPrefix2);

        String unknownPrefix = resolver.getNamespaceURI("unknown");
        assertNull(unknownPrefix);
    }

    /**
     * Test getting prefix.
     */
    @org.junit.Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        String xsiPrefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", xsiPrefix);

        String xsdPrefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", xsdPrefix);

        String unknownPrefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance");
        assertNull(unknownPrefix);

        NodeContext namespaceContext = TestUtil.createTestNodeContext();
        NodePointer nodePointer = new NodePointer(namespaceContext, new Name("test"));
        resolver.setNamespaceContextPointer(nodePointer);
        String xsiPrefix2 = resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", xsiPrefix2);
    }

    /**
     * Test learning whether this NamespaceResolver has been sealed.
     */
    @org.junit.Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        NamespaceResolver resolverWithParent = new NamespaceResolver(resolver);
        assertFalse(resolverWithParent.isSealed());
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        original.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        original.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        NamespaceResolver clone = (NamespaceResolver) original.clone();

        assertEquals("xsi", clone.getNamespaceURI("xsi"));
        assertEquals("xsd", clone.getNamespaceURI("xsd"));

        Map namespaceMap = clone.getNamespaceMap();
        assertEquals(2, namespaceMap.size());
        assertEquals("xsi", namespaceMap.get("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("xsd", namespaceMap.get("http://www.w3.org/2001/XMLSchema"));

        Map reverseMap = clone.getReverseNamespaceMap();
        assertEquals(2, reverseMap.size());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", reverseMap.get("xsi"));
        assertEquals("http://www.w3.org/2001/XMLSchema", reverseMap.get("xsd"));

        original.seal();
        clone.seal();

        assertEquals(original, clone);
    }
}