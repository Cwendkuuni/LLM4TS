package org.jdom2.util;

import org.jdom2.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    private NamespaceStack stack;
    private Namespace mns1, mns2, mns3, mns4;
    private Element element1, element2, element3;
    private Attribute att1, att2;

    @Before
    public void setup() {
        mns1 = new Namespace("ns1");
        mns2 = new Namespace("ns2");
        mns3 = new Namespace("ns3");
        mns4 = new Namespace("ns4");

        element1 = new Element("root", mns1);
        element2 = new Element("element", mns2);
        element3 = new Element("element2", mns3);

        att1 = new Attribute("attr", mns4);
        att2 = new Attribute("attr2", mns4);

        stack = new NamespaceStack();
    }

    @Test
    public void testConstructor() {
        assertNotNull(stack);
        assertEquals(new Namespace[0], stack.getScope());
        assertEquals(0, stack.depth);
    }

    @Test
    public void testPushElement() {
        stack.push(element1);

        List<Namespace> expectedAdded = new ArrayList<>();
        expectedAdded.add(mns1);
        Namespace[] expectedNewScope = new Namespace[2];
        expectedNewScope[0] = mns1;
        expectedNewScope[1] = Namespace.XML_NAMESPACE;

        assertEquals(expectedAdded.toArray(new Namespace[expectedAdded.size()]), stack.added[stack.depth]);
        assertArrayEquals(expectedNewScope, stack.scope[stack.depth]);
        assertEquals(1, stack.depth);

        stack.push(element2);

        List<Namespace> added = new ArrayList<>();
        added.add(mns2);

        Namespace[] newScope = new Namespace[3];
        newScope[0] = mns2;
        newScope[1] = mns1;
        newScope[2] = Namespace.XML_NAMESPACE;

        assertEquals(added.toArray(new Namespace[added.size()]), stack.added[stack.depth]);
        assertArrayEquals(newScope, stack.scope[stack.depth]);
        assertEquals(2, stack.depth);

        stack.pop();

        assertEquals(1, stack.depth);
    }

    @Test
    public void testPushAttribute() {
        stack.push(att1);

        List<Namespace> expectedAdded = new ArrayList<>();
        expectedAdded.add(mns4);

        Namespace[] expectedNewScope = new Namespace[2];
        expectedNewScope[0] = mns4;
        expectedNewScope[1] = Namespace.XML_NAMESPACE;

        assertEquals(expectedAdded.toArray(new Namespace[expectedAdded.size()]), stack.added[stack.depth]);
        assertArrayEquals(expectedNewScope, stack.scope[stack.depth]);
        assertEquals(1, stack.depth);

        stack.push(att2);

        List<Namespace> added = new ArrayList<>();
        added.add(mns4);

        Namespace[] newScope = new Namespace[2];
        newScope[0] = mns4;
        newScope[1] = Namespace.XML_NAMESPACE;

        assertEquals(added.toArray(new Namespace[added.size()]), stack.added[stack.depth]);
        assertArrayEquals(newScope, stack.scope[stack.depth]);
        assertEquals(1, stack.depth);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        stack.pop();
    }

    @Test
    public void testAddedForward() {
        stack.push(element1);
        stack.push(element2);

        Iterable<Namespace> namespaces = stack.addedForward();

        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(mns1);
        expectedNamespaces.add(mns2);

        List<Namespace> actualNamespaces = new ArrayList<>();
        for (Namespace ns : namespaces) {
            actualNamespaces.add(ns);
        }

        assertEquals(expectedNamespaces.size(), actualNamespaces.size());
        assertEquals(expectedNamespaces, actualNamespaces);
    }

    @Test
    public void testAddedReverse() {
        stack.push(element1);
        stack.push(element2);

        Iterable<Namespace> namespaces = stack.addedReverse();

        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(mns2);
        expectedNamespaces.add(mns1);

        List<Namespace> actualNamespaces = new ArrayList<>();
        for (Namespace ns : namespaces) {
            actualNamespaces.add(ns);
        }

        assertEquals(expectedNamespaces.size(), actualNamespaces.size());
        assertEquals(expectedNamespaces, actualNamespaces);
    }

    @Test
    public void testIsInScope() {
        stack.push(element1);
        stack.push(element2);

        assertTrue(stack.isInScope(mns1));
        assertTrue(stack.isInScope(mns2));
        assertFalse(stack.isInScope(mns3));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testGetScope() {
        stack.push(element1);
        stack.push(element2);

        Namespace[] expectedScope = new Namespace[2];
        expectedScope[0] = mns2;
        expectedScope[1] = mns1;

        assertArrayEquals(expectedScope, stack.getScope());
    }
}