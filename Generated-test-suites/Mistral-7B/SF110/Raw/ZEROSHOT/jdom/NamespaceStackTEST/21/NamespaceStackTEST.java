package org.jdom2.util;

import org.jdom2.internal.JDOMNamespaces;
import org.jdom2.Namespace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;
    private final Namespace xmlNamespace = JDOMNamespaces.XML_NAMESPACE;
    private final Namespace ns1 = new Namespace("ns1");
    private final Namespace ns2 = new Namespace("ns2");
    private final Namespace ns3 = new Namespace("ns3");
    private final Namespace ns4 = new Namespace("ns4");

    @Before
    public void setUp() {
        namespaceStack = new NamespaceStack();
    }

    @After
    public void tearDown() {
        namespaceStack = null;
    }

    @Test
    public void testEmptyStack() {
        assertTrue(namespaceStack.isEmpty());
        assertNull(namespaceStack.getScope());
    }

    @Test
    public void testPushElements() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns2);

        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Namespace[] expectedScope = {ns1, ns2};
        Namespace[] actualScope = namespaceStack.getScope();
        assertArrayEquals(expectedScope, actualScope);

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[0]));
        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns1, ns2);
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);
    }

    @Test
    public void testPushAttributes() {
        Attribute attribute1 = new Attribute("attr1", ns1.getURI(), "value1");
        Attribute attribute2 = new Attribute("attr2", ns2.getURI(), "value2");

        namespaceStack.push(attribute1);
        namespaceStack.push(attribute2);

        Namespace[] expectedScope = {ns1, ns2};
        Namespace[] actualScope = namespaceStack.getScope();
        assertArrayEquals(expectedScope, actualScope);

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(attribute1);
        namespaceStack.push(attribute2);
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[0]));
        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns1, ns2);
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);
    }

    @Test
    public void testPushWithSameNamespace() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns1);

        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Namespace[] expectedScope = {ns1};
        Namespace[] actualScope = namespaceStack.getScope();
        assertArrayEquals(expectedScope, actualScope);

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[0]));
        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns1);
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);
    }

    @Test
    public void testPushWithSamePrefix() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns2);

        namespaceStack.push(element1);
        namespaceStack.push(element2);

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[0]));
        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns1, ns2);
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);

        // ns1 and ns2 have the same prefix, so they should be merged
        Namespace[] expectedScope = {ns1};
        Namespace[] actualScope = namespaceStack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPushWithDifferentPrefixes() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns3);

        namespaceStack.push(element1);
        namespaceStack.push(element2);

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[0]));
        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns1, ns3);
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);

        Namespace[] expectedScope = {ns1, ns3};
        Namespace[] actualScope = namespaceStack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPop() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns2);

        namespaceStack.push(element1);
        namespaceStack.push(element2);
        namespaceStack.pop();

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        namespaceStack.pop();
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[0]));
        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns1);
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);

        Namespace[] expectedScope = {ns1};
        Namespace[] actualScope = namespaceStack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPopEmptyStack() {
        namespaceStack.pop();

        assertTrue(namespaceStack.isEmpty());
        assertNull(namespaceStack.getScope());
    }

    @Test
    public void testIsInScope() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns2);

        namespaceStack.push(element1);
        namespaceStack.push(element2);

        assertTrue(namespaceStack.isInScope(ns1));
        assertTrue(namespaceStack.isInScope(ns2));
        assertFalse(namespaceStack.isInScope(ns3));
        assertFalse(namespaceStack.isInScope(ns4));
    }

    @Test
    public void testAddedForward() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns2);

        namespaceStack.push(element1);
        namespaceStack.push(element2);

        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns1, ns2);
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedForward().toArray(new Namespace[0]));
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);
    }

    @Test
    public void testAddedReverse() {
        Element element1 = new Element("root", ns1);
        Element element2 = new Element("child", ns2);

        namespaceStack.push(element1);
        namespaceStack.push(element2);

        List<Namespace> expectedAddedNamespaces = Arrays.asList(ns2, ns1);
        List<Namespace> actualAddedNamespaces = Arrays.asList(namespaceStack.addedReverse().toArray(new Namespace[0]));
        assertEquals(expectedAddedNamespaces, actualAddedNamespaces);
    }
}