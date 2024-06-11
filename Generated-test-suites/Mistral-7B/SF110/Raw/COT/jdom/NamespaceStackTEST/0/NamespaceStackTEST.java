package org.jdom2.util;

import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertNull(stack.getScope());
        Assert.assertEquals(0, stack.depth);
    }

    @Test
    public void testPushWithEmptyNamespaceStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertThat(stack.getScope(), is(expected));
    }

    @Test
    public void testPushWithSingleNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", "prefix:local");
        stack.push(element);
        Namespace[] expected = {new Namespace("prefix", "prefix"), Namespace.XML_NAMESPACE};
        assertThat(stack.getScope(), is(expected));
    }

    @Test
    public void testPushWithMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", "prefix1:local1");
        stack.push(element);
        Attribute attribute1 = new Attribute("attr1", "prefix2:local2");
        stack.push(attribute1);
        Attribute attribute2 = new Attribute("attr2", "prefix3:local3");
        stack.push(attribute2);
        Namespace[] expected = {new Namespace("prefix3", "prefix3"), new Namespace("prefix2", "prefix2"),
                new Namespace("prefix1", "prefix1"), Namespace.XML_NAMESPACE};
        assertThat(stack.getScope(), is(expected));
    }

    @Test
    public void testPushWithDuplicateNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", "prefix:local");
        stack.push(element);
        Attribute attribute = new Attribute("attr", "prefix:local");
        stack.push(attribute);
        stack.push(element);
        Namespace[] expected = {new Namespace("prefix", "prefix"), Namespace.XML_NAMESPACE};
        assertThat(stack.getScope(), is(expected));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", "prefix:local");
        stack.push(element);
        stack.pop();
        Namespace[] expected = {Namespace.XML_NAMESPACE};
        assertThat(stack.getScope(), is(expected));
    }

    @Test
    public void testPushAndPopMultipleTimes() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("root1", "prefix1:local1");
        stack.push(element1);
        Element element2 = new Element("root2", "prefix2:local2");
        stack.push(element2);
        stack.pop();
        Namespace[] expected = {new Namespace("prefix1", "prefix1"), Namespace.XML_NAMESPACE};
        assertThat(stack.getScope(), is(expected));
    }

    @Test
    public void testAddedForwardIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", "prefix:local");
        stack.push(element);
        Attribute attribute1 = new Attribute("attr1", "prefix2:local2");
        stack.push(attribute1);
        Attribute attribute2 = new Attribute("attr2", "prefix3:local3");
        stack.push(attribute2);
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        Namespace[] expected = {new Namespace("prefix3", "prefix3"), new Namespace("prefix2", "prefix2"),
                new Namespace("prefix1", "prefix1")};
        while (iterator.hasNext()) {
            Namespace namespace = iterator.next();
            assertThat(namespace, is(expected[iterator.nextInt()]));
        }
    }

    @Test
    public void testAddedReverseIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", "prefix:local");
        stack.push(element);
        Attribute attribute1 = new Attribute("attr1", "prefix2:local2");
        stack.push(attribute1);
        Attribute attribute2 = new Attribute("attr2", "prefix3:local3");
        stack.push(attribute2);
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        Namespace[] expected = {new Namespace("prefix1", "prefix1"), new Namespace("prefix2", "prefix2"),
                new Namespace("prefix3", "prefix3")};
        while (iterator.hasNext()) {
            Namespace namespace = iterator.next();
            assertThat(namespace, is(expected[iterator.nextInt()]));
        }
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", "prefix:local");
        stack.push(element);
        Attribute attribute1 = new Attribute("attr1", "prefix2:local2");
        stack.push(attribute1);
        Attribute attribute2 = new Attribute("attr2", "prefix3:local3");
        stack.push(attribute2);
        Namespace namespace1 = new Namespace("prefix1", "prefix1");
        Namespace namespace2 = new Namespace("prefix2", "prefix2");
        Namespace namespace3 = new Namespace("prefix3", "prefix3");
        Namespace namespace4 = new Namespace("prefix4", "prefix4");
        Namespace namespace5 = new Namespace("prefix5", "prefix5");
        Assert.assertTrue(stack.isInScope(namespace1));
        Assert.assertTrue(stack.isInScope(namespace2));
        Assert.assertTrue(stack.isInScope(namespace3));
        Assert.assertFalse(stack.isInScope(namespace4));
        Assert.assertFalse(stack.isInScope(namespace5));
    }
}