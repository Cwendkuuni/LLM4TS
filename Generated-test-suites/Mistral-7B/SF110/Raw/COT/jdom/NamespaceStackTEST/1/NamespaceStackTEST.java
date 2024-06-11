package org.jdom2.util;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Assert.assertEquals(0, namespaceStack.getDepth());
        Assert.assertArrayEquals(new Namespace[0], namespaceStack.getScope());
    }

    @Test
    public void testPushElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "xmlns:a", new QName("http://example.com/a"));
        namespaceStack.push(element);
        Assert.assertEquals(1, namespaceStack.getDepth());
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE, new Namespace("http://example.com/a", "a")};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", null, new QName("http://example.com/a"));
        List<Namespace> additionalNamespaces = new ArrayList<>();
        additionalNamespaces.add(new Namespace("http://example.com/b", "b"));
        additionalNamespaces.add(new Namespace("http://example.com/c", "c"));
        namespaceStack.push(element, additionalNamespaces);
        Assert.assertEquals(1, namespaceStack.getDepth());
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE, new Namespace("http://example.com/a", "a"), new Namespace("http://example.com/b", "b"), new Namespace("http://example.com/c", "c")};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute = new Attribute("xmlns:a", "http://example.com/a");
        namespaceStack.push(attribute);
        Assert.assertEquals(1, namespaceStack.getDepth());
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE, new Namespace("http://example.com/a", "a")};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmptyStack() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.pop();
    }

    @Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "xmlns:a", new QName("http://example.com/a"));
        namespaceStack.push(element);
        namespaceStack.push(new Element("child", null, null));
        Assert.assertEquals(2, namespaceStack.getDepth());
        namespaceStack.pop();
        Assert.assertEquals(1, namespaceStack.getDepth());
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE, new Namespace("http://example.com/a", "a")};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testAddedForwardIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "xmlns:a", new QName("http://example.com/a"));
        namespaceStack.push(element);
        namespaceStack.push(new Element("child", null, null));
        Iterable<Namespace> iterable = namespaceStack.addedForward();
        int i = 0;
        for (Namespace namespace : iterable) {
            Assert.assertEquals(namespace, namespaceStack.getScope()[i]);
            ++i;
        }
        Assert.assertEquals(2, i);
    }

    @Test
    public void testAddedReverseIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "xmlns:a", new QName("http://example.com/a"));
        namespaceStack.push(element);
        namespaceStack.push(new Element("child", null, null));
        Iterable<Namespace> iterable = namespaceStack.addedReverse();
        int i = 0;
        for (Namespace namespace : iterable) {
            Assert.assertEquals(namespace, namespaceStack.getScope()[namespaceStack.getDepth() - i - 1]);
            ++i;
        }
        Assert.assertEquals(2, i);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "xmlns:a", new QName("http://example.com/a"));
        namespaceStack.push(element);
        Assert.assertTrue(namespaceStack.isInScope(new Namespace("http://example.com/a", "a")));
        Assert.assertFalse(namespaceStack.isInScope(new Namespace("http://example.com/b", "b")));
    }
}