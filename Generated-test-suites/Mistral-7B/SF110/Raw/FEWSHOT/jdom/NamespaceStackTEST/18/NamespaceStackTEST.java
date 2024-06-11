package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.namespace.Namespace;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackEmpty() {
        NamespaceStack stack = new NamespaceStack();
        assertNull(stack.getScope());
        assertEquals(0, stack.size());
    }

    @Test
    public void testNamespaceStackPushAndPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElement("root", "ns1"));
        Namespace[] scope = stack.getScope();
        Namespace ns1 = new Namespace("ns1");
        assertEquals(1, stack.size());
        assertEquals(ns1, scope[0]);
        stack.pop();
        scope = stack.getScope();
        assertEquals(0, stack.size());
        assertNull(scope);
    }

    @Test
    public void testNamespaceStackPushMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElement("root", "ns1"));
        stack.push(createElement("root", "ns2"));
        Namespace[] scope = stack.getScope();
        assertEquals(2, stack.size());
        assertEquals(ns1, scope[0]);
        assertEquals(ns2, scope[1]);
        stack.pop();
        scope = stack.getScope();
        assertEquals(1, stack.size());
        assertEquals(ns1, scope[0]);
        stack.pop();
        scope = stack.getScope();
        assertEquals(0, stack.size());
        assertNull(scope);
    }

    @Test
    public void testNamespaceStackPushWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElement("root", "ns1", "ns3"));
        Namespace[] scope = stack.getScope();
        Namespace ns1 = new Namespace("ns1");
        Namespace ns3 = new Namespace("ns3");
        assertEquals(2, stack.size());
        assertEquals(ns1, scope[0]);
        assertEquals(ns3, scope[1]);
        stack.pop();
        scope = stack.getScope();
        assertEquals(1, stack.size());
        assertEquals(ns1, scope[0]);
        stack.pop();
        scope = stack.getScope();
        assertEquals(0, stack.size());
        assertNull(scope);
    }

    @Test
    public void testNamespaceStackPushWithAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element root = new Element("root");
        root.setAttribute(new Attribute("a", "ns1", "value1"));
        root.setAttribute(new Attribute("b", "ns2", "value2"));
        stack.push(root);
        Namespace[] scope = stack.getScope();
        assertEquals(2, stack.size());
        assertEquals(ns1, scope[0]);
        assertEquals(ns2, scope[1]);
        stack.pop();
        scope = stack.getScope();
        assertEquals(1, stack.size());
        assertEquals(ns1, scope[0]);
        stack.pop();
        scope = stack.getScope();
        assertEquals(0, stack.size());
        assertNull(scope);
    }

    @Test
    public void testNamespaceStackIterator() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElement("root", "ns1"));
        stack.push(createElement("root", "ns2"));
        Iterator<Namespace> iterator = stack.iterator();
        List<Namespace> namespaces = new ArrayList<>();
        while (iterator.hasNext()) {
            namespaces.add(iterator.next());
        }
        assertEquals(2, namespaces.size());
        assertEquals(ns1, namespaces.get(0));
        assertEquals(ns2, namespaces.get(1));
    }

    @Test
    public void testNamespaceStackAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElement("root", "ns1"));
        stack.push(createElement("root", "ns2"));
        Iterable<Namespace> addedForward = stack.addedForward();
        List<Namespace> namespaces = new ArrayList<>();
        for (Namespace namespace : addedForward) {
            namespaces.add(namespace);
        }
        assertEquals(2, namespaces.size());
        assertEquals(ns1, namespaces.get(0));
        assertEquals(ns2, namespaces.get(1));
    }

    @Test
    public void testNamespaceStackAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElement("root", "ns1"));
        stack.push(createElement("root", "ns2"));
        Iterable<Namespace> addedReverse = stack.addedReverse();
        List<Namespace> namespaces = new ArrayList<>();
        for (Namespace namespace : addedReverse) {
            namespaces.add(namespace);
        }
        assertEquals(2, namespaces.size());
        assertEquals(ns2, namespaces.get(0));
        assertEquals(ns1, namespaces.get(1));
    }

    private Element createElement(String name, String... namespaces) {
        Element element = new Element(name);
        if (namespaces != null) {
            for (String namespace : namespaces) {
                element.setNameSpace(namespace);
            }
        }
        return element;
    }
}