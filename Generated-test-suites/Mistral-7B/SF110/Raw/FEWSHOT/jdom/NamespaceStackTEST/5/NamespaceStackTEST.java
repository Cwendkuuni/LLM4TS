package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers;
import org.jdom2.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackCreation() {
        NamespaceStack namespaceStack = new NamespaceStack();
        assertNull(namespaceStack.getScope());
        assertTrue(namespaceStack.isEmpty());
        NamespaceStack namespaceStackWithSeed = new NamespaceStack(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE });
        assertSame(Namespace.NO_NAMESPACE, namespaceStackWithSeed.getScope()[0]);
        assertSame(Namespace.XML_NAMESPACE, namespaceStackWithSeed.getScope()[1]);
        assertFalse(namespaceStackWithSeed.isEmpty());
    }

    @Test
    public void testPushPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(new Element("root"));
        Element element = new Element("element");
        namespaceStack.push(element);
        assertSame(element.getNamespace(), namespaceStack.getScope()[0]);
        namespaceStack.pop();
        assertSame(Namespace.NO_NAMESPACE, namespaceStack.getScope()[0]);

        Element elementWithNamespace = new Element("element", Namespace.XML_NAMESPACE);
        namespaceStack.push(elementWithNamespace);
        assertSame(Namespace.XML_NAMESPACE, namespaceStack.getScope()[0]);
        namespaceStack.push(new Element("subElement"));
        Namespace expectedNamespace = new Namespace("http://example.com", "ex");
        elementWithNamespace.setName("element", expectedNamespace);
        assertSame(expectedNamespace, namespaceStack.getScope()[1]);

        namespaceStack.pop();
        assertSame(Namespace.XML_NAMESPACE, namespaceStack.getScope()[0]);
        namespaceStack.pop();
        assertSame(Namespace.NO_NAMESPACE, namespaceStack.getScope()[0]);
    }

    @Test
    public void testPushMultipleElementsWithSameNamespace() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element1 = new Element("element1", Namespace.XML_NAMESPACE);
        Element element2 = new Element("element2", Namespace.XML_NAMESPACE);
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Namespace[] expectedScope = new Namespace[] { Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushMultipleElementsWithDifferentNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element1 = new Element("element1", Namespace.XML_NAMESPACE);
        Element element2 = new Element("element2", new Namespace("http://example.com", "ex"));
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Namespace[] expectedScope = new Namespace[] { new Namespace("http://example.com", "ex"), Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushMultipleAttributesWithSameNamespace() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("attr1", Namespace.XML_NAMESPACE);
        Attribute attribute2 = new Attribute("attr2", Namespace.XML_NAMESPACE);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        namespaceStack.push(element);

        Namespace[] expectedScope = new Namespace[] { Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushMultipleAttributesWithDifferentNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("attr1", Namespace.XML_NAMESPACE);
        Attribute attribute2 = new Attribute("attr2", new Namespace("http://example.com", "ex"));
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        namespaceStack.push(element);

        Namespace[] expectedScope = new Namespace[] { new Namespace("http://example.com", "ex"), Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushMultipleAttributesWithSameNamespaceAndAdditionalNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("attr1", Namespace.XML_NAMESPACE);
        Attribute attribute2 = new Attribute("attr2", Namespace.XML_NAMESPACE);
        element.addAttribute(attribute1);
        element.setAdditionalNamespaces(Arrays.asList(new Namespace("http://example.com", "ex")));
        element.addAttribute(attribute2);
        namespaceStack.push(element);

        Namespace[] expectedScope = new Namespace[] { new Namespace("http://example.com", "ex"), Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushMultipleAttributesWithDifferentNamespacesAndAdditionalNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("attr1", Namespace.XML_NAMESPACE);
        Attribute attribute2 = new Attribute("attr2", new Namespace("http://example.com", "ex"));
        element.addAttribute(attribute1);
        element.setAdditionalNamespaces(Arrays.asList(new Namespace("http://example2.com", "ex2")));
        element.addAttribute(attribute2);
        namespaceStack.push(element);

        Namespace[] expectedScope = new Namespace[] { new Namespace("http://example2.com", "ex2"), new Namespace("http://example.com", "ex"), Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testGetNamespaceInScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element");
        Namespace expectedNamespace = new Namespace("http://example.com", "ex");
        element.setName("element", expectedNamespace);
        namespaceStack.push(element);

        assertTrue(namespaceStack.isInScope(expectedNamespace));
        assertFalse(namespaceStack.isInScope(new Namespace("http://example2.com", "ex2")));
    }

    @Test
    public void testIterable() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element");
        Namespace namespace1 = new Namespace("http://example1.com", "ex1");
        Namespace namespace2 = new Namespace("http://example2.com", "ex2");
        element.addNamespaceDeclaration(namespace1.getPrefix());
        element.addNamespaceDeclaration(namespace2.getPrefix());

        namespaceStack.push(element);

        List<Namespace> expectedNamespaces = new ArrayList<>(Arrays.asList(namespace1, namespace2, Namespace.XML_NAMESPACE));
        assertSame(expectedNamespaces, namespaceStack.addedForward());
        assertSame(expectedNamespaces.descendingIterator().asList(), namespaceStack.addedReverse());

        Iterator<Namespace> iterator = namespaceStack.iterator();
        Namespace namespace = iterator.next();
        assertEquals(namespace1, namespace);
        assertFalse(iterator.hasNext());

        iterator = namespaceStack.iterator();
        assertFalse(iterator.hasNext());
    }
}