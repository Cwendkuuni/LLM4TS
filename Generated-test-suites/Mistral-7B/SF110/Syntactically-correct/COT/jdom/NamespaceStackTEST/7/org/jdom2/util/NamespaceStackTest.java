package org.jdom2.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.NamespaceStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;

    @Before
    public void setup() {
        namespaceStack = new NamespaceStack();
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(namespaceStack.getScope());
        Assert.assertEquals(0, namespaceStack.getScope().length);
        Assert.assertEquals(-1, namespaceStack.getDepth());
    }

    @Test
    public void testPushElement() {
        Element element = new Element("root");
        namespaceStack.push(element);

        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace()};
        Namespace[] currentScope = namespaceStack.getScope();
        Assert.assertArrayEquals(expectedScope, currentScope);
        Assert.assertEquals(1, namespaceStack.getDepth());

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(element);
        Namespace[] pushScope = namespaceStack.getScope();
        namespaceStack.pop();

        Namespace[] expectedAddedNamespaces = new Namespace[]{element.getNamespace()};
        Assert.assertArrayEquals(expectedAddedNamespaces, addedNamespaces.toArray(new Namespace[0]));
        Assert.assertArrayEquals(expectedScope, pushScope);
        Assert.assertEquals(0, namespaceStack.getDepth());
    }

    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("attr", "value");
        namespaceStack.push(attribute);

        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, attribute.getNamespace()};
        Namespace[] currentScope = namespaceStack.getScope();
        Assert.assertArrayEquals(expectedScope, currentScope);
        Assert.assertEquals(1, namespaceStack.getDepth());

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.push(attribute);
        Namespace[] pushScope = namespaceStack.getScope();
        namespaceStack.pop();

        Namespace[] expectedAddedNamespaces = new Namespace[]{attribute.getNamespace()};
        Assert.assertArrayEquals(expectedAddedNamespaces, addedNamespaces.toArray(new Namespace[0]));
        Assert.assertArrayEquals(expectedScope, pushScope);
        Assert.assertEquals(0, namespaceStack.getDepth());
    }

    @Test
    public void testPushMultipleElementsWithSameNamespace() {
        Element element1 = new Element("root");
        Element element2 = new Element("root");
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element1.getNamespace()};
        Namespace[] currentScope = namespaceStack.getScope();
        Assert.assertArrayEquals(expectedScope, currentScope);
        Assert.assertEquals(2, namespaceStack.getDepth());

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.pop();

        Namespace[] expectedAddedNamespaces = new Namespace[]{element1.getNamespace()};
        Assert.assertArrayEquals(expectedAddedNamespaces, addedNamespaces.toArray(new Namespace[0]));
        Assert.assertArrayEquals(expectedScope, currentScope);
        Assert.assertEquals(1, namespaceStack.getDepth());

        addedNamespaces.clear();
        namespaceStack.pop();

        Assert.assertEquals(0, namespaceStack.getDepth());
        Assert.assertArrayEquals(new Namespace[0], namespaceStack.getScope());
    }

    @Test
    public void testPushMultipleElementsWithDifferentNamespaces() {
        Element element1 = new Element("root1");
        Element element2 = new Element("root2");
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element1.getNamespace(), element2.getNamespace()};
        Namespace[] currentScope = namespaceStack.getScope();
        Assert.assertArrayEquals(expectedScope, currentScope);
        Assert.assertEquals(2, namespaceStack.getDepth());

        List<Namespace> addedNamespaces = new ArrayList<>();
        namespaceStack.pop();

        Namespace[] expectedAddedNamespaces = new Namespace[]{element1.getNamespace()};
        Assert.assertArrayEquals(expectedAddedNamespaces, addedNamespaces.toArray(new Namespace[0]));
        Assert.assertArrayEquals(expectedScope, currentScope);
        Assert.assertEquals(1, namespaceStack.getDepth());

        addedNamespaces.clear();
        namespaceStack.pop();

        Assert.assertEquals(0, namespaceStack.getDepth());
        Assert.assertArrayEquals(new Namespace[0], namespaceStack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        namespaceStack.pop();
    }

    @Test
    public void testPushPopMultipleTimes() {
        Element element = new Element("element");
        for (int i = 0; i < 10; i++) {
            namespaceStack.push(element);
            Assert.assertEquals(i + 1, namespaceStack.getDepth());
            namespaceStack.pop();
            Assert.assertEquals(i, namespaceStack.getDepth());
        }

        Assert.assertEquals(0, namespaceStack.getDepth());
        Assert.assertArrayEquals(new Namespace[0], namespaceStack.getScope());
    }

    @Test
    public void testIsInScope() {
        Element element = new Element("root");
        namespaceStack.push(element);
        Assert.assertTrue(namespaceStack.isInScope(element.getNamespace()));
        Assert.assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        namespaceStack.pop();
        Assert.assertFalse(namespaceStack.isInScope(element.getNamespace()));
    }

    @Test
    public void testIterator() {
        Element element1 = new Element("root1");
        Element element2 = new Element("root2");
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Iterator<Namespace> iterator = namespaceStack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(element1.getNamespace(), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(element2.getNamespace(), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedForwardIterator() {
        Element element1 = new Element("root1");
        Element element2 = new Element("root2");
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Iterator<Namespace> iterator = namespaceStack.addedForward();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(element2.getNamespace(), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(element1.getNamespace(), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverseIterator() {
        Element element1 = new Element("root1");
        Element element2 = new Element("root2");
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        Iterator<Namespace> iterator = namespaceStack.addedReverse();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(element1.getNamespace(), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(element2.getNamespace(), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}