package org.jdom2.util;

import junit.framework.Assert;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

    @org.junit.Test
    public void testEmptyStack() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, namespaceStack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedReverse());
        Assert.assertFalse(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
    }

    @org.junit.Test
    public void testNewStackWithSeed() {
        NamespaceStack namespaceStack = new NamespaceStack(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE});
        Assert.assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, namespaceStack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedReverse());
        Assert.assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testPushNullElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(null);
    }

    @org.junit.Test
    public void testPushElementWithEmptyListOfNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        namespaceStack.push(element);
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, namespaceStack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedReverse());
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
    }

    @org.junit.Test
    public void testPushElementWithListOfNamespacesContainingNamespaceToPush() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "prefix:", new QualifiedName("http://example.com/"));
        namespaceStack.push(element);
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE, new Namespace("prefix", "http://example.com/")}, namespaceStack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedReverse());
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        Assert.assertTrue(namespaceStack.isInScope(new Namespace("prefix", "http://example.com/")));
    }

    @org.junit.Test
    public void testPushElementWithListOfNamespacesNotContainingNamespaceToPush() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "prefix2:", new QualifiedName("http://example.com/"));
        namespaceStack.push(new Element("element"));
        namespaceStack.push(element);
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, new Namespace("prefix", "http://example.com/")}, namespaceStack.getScope());
        Assert.assertEquals(new ArrayList<Namespace>() {{
            add(Namespace.XML_NAMESPACE);
            add(Namespace.NO_NAMESPACE);
            add(new Namespace("prefix", "http://example.com/"));
        }}.toArray(new Namespace[0]), namespaceStack.addedForward().toArray(new Namespace[0]));
        Assert.assertEquals(new ArrayList<Namespace>() {{
            add(new Namespace("prefix", "http://example.com/"));
            add(Namespace.XML_NAMESPACE);
            add(Namespace.NO_NAMESPACE);
        }}.toArray(new Namespace[0]), namespaceStack.addedReverse().toArray(new Namespace[0]));
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        Assert.assertTrue(namespaceStack.isInScope(new Namespace("prefix", "http://example.com/")));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testPushElementWithSameNamespaceTwice() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "prefix:", new QualifiedName("http://example.com/"));
        namespaceStack.push(element);
        namespaceStack.push(element);
    }

    @org.junit.Test
    public void testPushAttribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute = new Attribute("attr");
        namespaceStack.push(attribute);
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, namespaceStack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, namespaceStack.addedReverse());
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
    }

    @org.junit.Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "prefix:", new QualifiedName("http://example.com/"));
        namespaceStack.push(element);
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE, new Namespace("prefix", "http://example.com/")}, namespaceStack.getScope());
        namespaceStack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, namespaceStack.getScope());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.pop();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testPopBeyondBoundaries() {
        NamespaceStack namespaceStack = new NamespaceStack();
        for (int i = 0; i < 11; i++) {
            Element element = new Element("element");
            namespaceStack.push(element);
        }
        namespaceStack.pop();
        namespaceStack.pop();
        // Popping twice is fine, but the following statement should throw an exception
        namespaceStack.pop();
    }
}