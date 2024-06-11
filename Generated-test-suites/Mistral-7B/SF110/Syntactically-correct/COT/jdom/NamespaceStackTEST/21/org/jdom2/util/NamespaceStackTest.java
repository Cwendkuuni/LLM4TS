package org.jdom2.util;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    // Test constructor
    @Test
    public void testConstructor() {
        Assert.assertNotNull(stack.getScope());
        Assert.assertEquals(0, stack.getScope().length);
        Assert.assertEquals(-1, stack.getDepth());
    }

    // Test push method with an element
    @Test
    public void testPushElement() {
        Element element = new Element("root", "root");
        stack.push(element);

        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace()};
        Namespace[] actual = stack.getScope();

        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(1, stack.getDepth());
    }

    // Test push method with an attribute
    @Test
    public void testPushAttribute() {
        Element root = new Element("root", "root");
        Attribute attribute = new Attribute("attr", "attr", "http://example.com/");
        root.addContent(attribute);

        stack.push(root);
        stack.push(attribute);

        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, root.getNamespace(), attribute.getNamespace()};
        Namespace[] actual = stack.getScope();

        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(2, stack.getDepth());
    }

    // Test push method with nested elements and attributes
    @Test
    public void testPushNestedElementsAndAttributes() {
        Element root = new Element("root", "root");
        Attribute rootAttribute = new Attribute("attr1", "attr1", "http://example.com/");
        root.addContent(rootAttribute);
        Element child = new Element("child", "child");
        Attribute childAttribute = new Attribute("attr2", "attr2", "http://example.com/");
        child.addContent(childAttribute);
        root.addContent(child);

        stack.push(root);
        stack.push(child);
        stack.push(childAttribute);
        stack.push(rootAttribute);

        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, root.getNamespace(), child.getNamespace(), childAttribute.getNamespace(), rootAttribute.getNamespace()};
        Namespace[] actual = stack.getScope();

        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(5, stack.getDepth());
    }

    // Test pop method
    @Test
    public void testPop() {
        Element root = new Element("root", "root");
        Attribute rootAttribute = new Attribute("attr1", "attr1", "http://example.com/");
        root.addContent(rootAttribute);
        Element child = new Element("child", "child");
        Attribute childAttribute = new Attribute("attr2", "attr2", "http://example.com/");
        child.addContent(childAttribute);
        root.addContent(child);

        stack.push(root);
        stack.push(child);
        stack.push(childAttribute);
        stack.push(rootAttribute);

        stack.pop();
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, root.getNamespace(), child.getNamespace()};
        Namespace[] actual = stack.getScope();

        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(3, stack.getDepth());
    }

    // Test isInScope method
    @Test
    public void testIsInScope() {
        Element root = new Element("root", "root");
        Attribute rootAttribute = new Attribute("attr1", "attr1", "http://example.com/");
        root.addContent(rootAttribute);
        Element child = new Element("child", "child");
        Attribute childAttribute = new Attribute("attr2", "attr2", "http://example.com/");
        child.addContent(childAttribute);
        root.addContent(child);

        stack.push(root);
        stack.push(child);
        stack.push(childAttribute);
        stack.push(rootAttribute);

        Assert.assertTrue(stack.isInScope(root.getNamespace()));
        Assert.assertTrue(stack.isInScope(child.getNamespace()));
        Assert.assertTrue(stack.isInScope(childAttribute.getNamespace()));
        Assert.assertTrue(stack.isInScope(rootAttribute.getNamespace()));
        Assert.assertFalse(stack.isInScope(new Namespace("http://other-example.com/")));
    }

    // Test push method with duplicate namespaces
    @Test(expected = IllegalStateException.class)
    public void testPushDuplicateNamespaces() {
        Element element = new Element("root", "root");
        stack.push(element);
        stack.push(element.getNamespace());
    }

    // Test push method with null element
    @Test(expected = NullPointerException.class)
    public void testPushNullElement() {
        stack.push(null);
    }

    // Test push method with null attribute
    @Test(expected = NullPointerException.class)
    public void testPushNullAttribute() {
        Element root = new Element("root", "root");
        stack.push(root);
        Attribute attribute = null;
        stack.push(attribute);
    }
}