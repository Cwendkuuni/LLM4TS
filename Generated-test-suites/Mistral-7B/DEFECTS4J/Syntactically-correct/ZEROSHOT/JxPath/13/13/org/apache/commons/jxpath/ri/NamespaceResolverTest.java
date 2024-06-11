package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.jxpath.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorMock;
import org.apache.commons.jxpath.ri.model.NodePointerMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for NamespaceResolver class.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;
    private NodePointer nodePointer;
    private NodeIterator nodeIterator;

    @Before
    public void setup() {
        parentNamespaceResolver = mock(NamespaceResolver.class);
        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        nodePointer = new NodePointerMock();
        nodeIterator = new NodeIteratorMock();
    }

    @Test
    public void testNewInstance() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertNull(namespaceResolver.getParent());
        assertNull(namespaceResolver.getNamespaceMap());
        assertNull(namespaceResolver.getReverseMap());
        assertNull(namespaceResolver.getPointer());
        assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testInstanceWithParent() {
        when(parentNamespaceResolver.getNamespaceMap()).thenReturn(new HashMap<>());

        NamespaceResolver namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        assertEquals(parentNamespaceResolver, namespaceResolver.getParent());
        assertNull(namespaceResolver.getNamespaceMap());
        assertNull(namespaceResolver.getReverseMap());
        assertNull(namespaceResolver.getPointer());
        assertFalse(namespaceResolver.isSealed());

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map<String, String> parentNamespaceMap = namespaceResolver.getParent().getNamespaceMap();
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        assertEquals(namespaceMap.size(), parentNamespaceMap.size() + 1);
        assertEquals(namespaceMap.get("xsd"), "http://www.w3.org/2001/XMLSchema");
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        assertSame(namespaceResolver, namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema"));
        assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        try {
            namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            fail("Should not be able to register namespaces on a sealed NamespaceResolver.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertSame("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        assertNull(namespaceResolver.getNamespaceURI("foo"));

        NodePointer nodePointer = new NodePointerMock();
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        when(nodePointer.getNamespaceURI("xsd")).thenReturn("http://mycompany.com/xsd");
        assertEquals("http://mycompany.com/xsd", namespaceResolver.getNamespaceURI("xsd"));
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertNull(namespaceResolver.getPrefix("foo"));

        NodePointer nodePointer = new NodePointerMock();
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        when(nodePointer.getNamespaceURI("xsd")).thenReturn("http://mycompany.com/xsd");
        when(nodePointer.getPrefix("http://mycompany.com/xsd")).thenReturn("my");
        assertEquals("my", namespaceResolver.getPrefix("http://mycompany.com/xsd"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertFalse(namespaceResolver.equals(clonedResolver));
        assertEquals(namespaceResolver.getNamespaceMap(), clonedResolver.getNamespaceMap());
        assertFalse(namespaceResolver.isSealed());
        assertFalse(clonedResolver.isSealed());
    }

    @Test
    public void testNamespaceIterator() {
        Map<String, String> namespaceMap = new HashMap<>();
        namespaceMap.put("xsd", "http://www.w3.org/2001/XMLSchema");
        when(nodeIterator.getNodePointer(0)).thenReturn(nodePointer);
        when(nodePointer.getNamespaceURI("xsd")).thenReturn("http://www.w3.org/2001/XMLSchema");
        when(nodePointer.getName().getName()).thenReturn("xsd");

        namespaceResolver.setNamespaceContextPointer(nodePointer);
        namespaceResolver.setNamespaceMap(namespaceMap);

        NodeIterator expectedNodeIterator = nodeIterator;
        when(nodeIterator.next()).thenReturn(expectedNodeIterator);
        when(expectedNodeIterator.getNextIteration()).thenReturn(nodeIterator);

        Iterator<Map.Entry<String, String>> namespaceIterator = namespaceResolver.namespaceIterator();
        assertEquals("xsd", namespaceIterator.next().getKey());
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceIterator.next().getValue());
        assertNull(namespaceIterator.next());
    }

}