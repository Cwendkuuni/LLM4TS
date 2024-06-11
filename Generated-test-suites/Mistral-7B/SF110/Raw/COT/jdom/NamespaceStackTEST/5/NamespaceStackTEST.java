package org.jdom2.util;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackConstructor() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Assert.assertNotNull(namespaceStack);
    }

    @Test
    public void testNamespaceStackWithSeed() {
        List<Namespace> seed = new ArrayList<>();
        seed.add(Namespace.getXmlNamespace());
        NamespaceStack namespaceStack = new NamespaceStack(seed.toArray(new Namespace[seed.size()]));
        Namespace[] expected = {Namespace.getXmlNamespace(), Namespace.NO_NAMESPACE};
        Namespace[] actual = namespaceStack.getScope();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.length, is(2));
        assertThat(actual[0], is(Namespace.getXmlNamespace()));
        assertThat(actual[1], is(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPushElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "root");
        Namespace mns = element.getNamespace();
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, mns, new Namespace[0]);
        namespaceStack.push(element);
        Namespace[] expected = {Namespace.getXmlNamespace(), mns};
        Namespace[] actual = namespaceStack.getScope();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "root");
        List<Namespace> toAdd = new ArrayList<>();
        Namespace mns = element.getNamespace();
        Namespace[] newScope = checkNamespace(toAdd, mns, new Namespace[0]);
        for (Namespace ns : element.getAdditionalNamespaces()) {
            newScope = checkNamespace(toAdd, ns, newScope);
        }
        namespaceStack.push(element);
        Namespace[] expected = {Namespace.getXmlNamespace(), mns, ns};
        Namespace[] actual = namespaceStack.getScope();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute = new Attribute("xmlns:x", "http://example.com/x");
        Namespace mns = attribute.getNamespace();
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, mns, new Namespace[0]);
        namespaceStack.push(attribute);
        Namespace[] expected = {Namespace.getXmlNamespace(), mns};
        Namespace[] actual = namespaceStack.getScope();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.pop();
    }

    @Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "root");
        Namespace mns = element.getNamespace();
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, mns, new Namespace[0]);
        namespaceStack.push(element);
        Namespace[] expected = new Namespace[0];
        namespaceStack.pop();
        Namespace[] actual = namespaceStack.getScope();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testAddedForward() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "root");
        Namespace mns = element.getNamespace();
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, mns, new Namespace[0]);
        namespaceStack.push(element);
        Iterable<Namespace> expected = new NamespaceIterable(new Namespace[]{mns}, true);
        Iterable<Namespace> actual = namespaceStack.addedForward();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "root");
        Namespace mns = element.getNamespace();
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, mns, new Namespace[0]);
        namespaceStack.push(element);
        Iterable<Namespace> expected = new NamespaceIterable(new Namespace[]{mns}, false);
        Iterable<Namespace> actual = namespaceStack.addedReverse();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "root");
        Namespace mns = element.getNamespace();
        List<Namespace> toAdd = new ArrayList<>();
        Namespace[] newScope = checkNamespace(toAdd, mns, new Namespace[0]);
        namespaceStack.push(element);
        Iterator<Namespace> expected = new ForwardWalker(newScope);
        Iterator<Namespace> actual = namespaceStack.iterator();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace mns = Namespace.getXmlNamespace();
        namespaceStack.push(mns);
        Namespace ns = new Namespace("test", "http://example.com/test");
        boolean expected = true;
        boolean actual = namespaceStack.isInScope(ns);
        Assert.assertEquals(expected, actual);
        namespaceStack.pop();
        expected = false;
        actual = namespaceStack.isInScope(ns);
        Assert.assertEquals(expected, actual);
    }

    private List<Namespace> checkNamespace(List<Namespace> toAdd, Namespace namespace, Namespace[] scope) {
        // implementation of checkNamespace method from the NamespaceStack class
    }
}