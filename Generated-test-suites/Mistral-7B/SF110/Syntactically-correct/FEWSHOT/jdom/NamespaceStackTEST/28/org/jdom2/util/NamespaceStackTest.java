package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.EmptyIterable;
import org.jdom2.util.NamespaceStack;

import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackCreation() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(0, stack.depth);
        assertNull(stack.getScope());

        NamespaceStack stack2 = new NamespaceStack(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE });
        assertEquals(2, stack2.depth);
        assertArrayEquals(new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, stack2.getScope());
    }

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElementWithNamespace("root", "ns1"));
        assertEquals(1, stack.depth);
        Namespace ns1 = stack.getScope()[0];
        assertEquals("ns1", ns1.getPrefix());

        stack.push(createElementWithNamespace("child", "ns2"));
        assertEquals(2, stack.depth);
        Namespace ns2 = stack.getScope()[1];
        assertEquals("ns2", ns2.getPrefix());

        stack.pop();
        assertEquals(1, stack.depth);
        assertEquals("ns1", stack.getScope()[0].getPrefix());
    }

    @Test
    public void testMultiplePushes() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElementWithNamespace("root", "ns1"));
        stack.push(createElementWithNamespace("child1", "ns2"));
        stack.push(createElementWithNamespace("child2", "ns3"));
        assertEquals(3, stack.depth);
        assertEquals("ns3", stack.getScope()[2].getPrefix());

        stack.pop();
        assertEquals(2, stack.depth);
        assertEquals("ns2", stack.getScope()[1].getPrefix());

        stack.pop();
        assertEquals(1, stack.depth);
        assertEquals("ns1", stack.getScope()[0].getPrefix());
    }

    @Test
    public void testCheckNamespace() {
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] scope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        Namespace mns1 = new Namespace("ns1", "http://example.com/ns1");
        Namespace mns2 = new Namespace("ns2", "http://example.com/ns2");
        Namespace mns3 = new Namespace("ns3", "http://example.com/ns3");

        Namespace[] newscope = NamespaceStack.checkNamespace(toadd, mns1, scope);
        assertEquals(2, newscope.length);
        assertEquals("ns1", newscope[0].getPrefix());

        toadd.clear();
        newscope = NamespaceStack.checkNamespace(toadd, mns2, scope);
        assertEquals(3, newscope.length);
        assertEquals("ns2", newscope[1].getPrefix());

        toadd.clear();
        newscope = NamespaceStack.checkNamespace(toadd, mns3, scope);
        assertEquals(3, newscope.length);
        assertEquals("ns3", newscope[2].getPrefix());

        toadd.clear();
        newscope = NamespaceStack.checkNamespace(toadd, mns1, newscope);
        assertEquals(3, newscope.length);
        assertEquals("ns1", newscope[0].getPrefix());

        toadd.clear();
        newscope = NamespaceStack.checkNamespace(toadd, mns2, newscope);
        assertEquals(4, newscope.length);
        assertEquals("ns2", newscope[1].getPrefix());

        toadd.clear();
        newscope = NamespaceStack.checkNamespace(toadd, mns3, newscope);
        assertEquals(4, newscope.length);
        assertEquals("ns3", newscope[2].getPrefix());
    }

    @Test
    public void testPushStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pushStack(new Namespace("ns1", "http://example.com/ns1"), new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE }, new ArrayList<>());
        assertEquals(1, stack.depth);
        assertEquals("ns1", stack.getScope()[0].getPrefix());

        stack.pushStack(new Namespace("ns2", "http://example.com/ns2"), new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NS1 }, new ArrayList<>());
        assertEquals(2, stack.depth);
        assertEquals("ns2", stack.getScope()[1].getPrefix());

        stack.pop();
        assertEquals(1, stack.depth);
        assertEquals("ns1", stack.getScope()[0].getPrefix());
    }

    @Test
    public void testIterableForward() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElementWithNamespace("root", "ns1"));
        stack.push(createElementWithNamespace("child1", "ns2"));
        stack.push(createElementWithNamespace("child2", "ns3"));

        Iterable<Namespace> iterable = stack.addedForward();
        assertEquals(3, iterable.size());
        Namespace ns1 = iterable.iterator().next();
        assertEquals("ns1", ns1.getPrefix());
        Namespace ns2 = iterable.iterator().next();
        assertEquals("ns2", ns2.getPrefix());
        Namespace ns3 = iterable.iterator().next();
        assertEquals("ns3", ns3.getPrefix());
    }

    @Test
    public void testIterableReverse() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElementWithNamespace("root", "ns1"));
        stack.push(createElementWithNamespace("child1", "ns2"));
        stack.push(createElementWithNamespace("child2", "ns3"));

        Iterable<Namespace> iterable = stack.addedReverse();
        assertEquals(3, iterable.size());
        Namespace ns3 = iterable.iterator().next();
        assertEquals("ns3", ns3.getPrefix());
        Namespace ns2 = iterable.iterator().next();
        assertEquals("ns2", ns2.getPrefix());
        Namespace ns1 = iterable.iterator().next();
        assertEquals("ns1", ns1.getPrefix());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(createElementWithNamespace("root", "ns1"));
        stack.push(createElementWithNamespace("child1", "ns2"));
        stack.push(createElementWithNamespace("child2", "ns3"));

        assertTrue(stack.isInScope(new Namespace("ns1", "http://example.com/ns1")));
        assertTrue(stack.isInScope(new Namespace("ns2", "http://example.com/ns2")));
        assertTrue(stack.isInScope(new Namespace("ns3", "http://example.com/ns3")));
        assertFalse(stack.isInScope(new Namespace("ns4", "http://example.com/ns4")));
    }

    private Element createElementWithNamespace(String name, String prefix) {
        Element element = new Element(name);
        Namespace namespace = new Namespace(prefix, "http://example.com/" + prefix);
        element.setName(new QName(namespace, name));
        return element;
    }
}