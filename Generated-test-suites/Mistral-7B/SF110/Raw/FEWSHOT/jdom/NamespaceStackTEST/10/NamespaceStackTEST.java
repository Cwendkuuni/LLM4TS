package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.NamespaceImpl;
import org.jdom2.util.NamespaceStack;

import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertTrue(stack.isEmpty());
        assertNull(stack.getScope());
        assertSame(Collections.emptyList(), stack.addedForward());
        assertSame(Collections.emptyList(), stack.addedReverse());
    }

    @Test
    public void testPushPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        Namespace[] scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);

        stack.pop();
        scope = stack.getScope();
        assertEquals(0, scope.length);
    }

    @Test
    public void testPushMultipleElements() {
        NamespaceStack stack = new NamespaceStack();
        Element e1 = new Element("root");
        Element e2 = new Element("child");
        stack.push(e1);
        stack.push(e2);

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);

        List<Namespace> added = new ArrayList<>();
        stack.push(new Element("grandchild"));
        stack.push(new Attribute("grandchild", "attr"));
        Namespace[] newScope = checkNamespace(added, Namespace.XML_NAMESPACE, scope);
        stack.pushStack(Namespace.XML_NAMESPACE, newScope, added);

        scope = stack.getScope();
        assertEquals(3, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
        assertEquals(Namespace.XML_NAMESPACE, scope[2]);

        assertEquals(2, added.size());
        assertEquals(Namespace.XML_NAMESPACE, added.get(0));
        assertEquals(Namespace.NO_NAMESPACE, added.get(1));

        stack.pop();
        scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);

        stack.pop();
        scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    }

    @Test
    public void testPushMultipleElementsWithNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element e1 = new Element("root", new QName("http://example.com/", "root"));
        Element e2 = new Element("child", new QName("http://example.com/", "child"));
        stack.push(e1);
        stack.push(e2);

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertFalse(scope[0].getPrefix().equals(""));
        assertEquals("root", scope[0].getPrefix());
        assertEquals("child", scope[1].getPrefix());

        List<Namespace> added = new ArrayList<>();
        stack.push(new Element("grandchild", new QName("http://example.com/", "grandchild")));
        stack.push(new Attribute("grandchild", "attr", new QName("http://example.com/", "grandchild")));
        Namespace[] newScope = checkNamespace(added, Namespace.XML_NAMESPACE, scope);
        stack.pushStack(Namespace.XML_NAMESPACE, newScope, added);

        scope = stack.getScope();
        assertEquals(3, scope.length);
        assertEquals("root", scope[0].getPrefix());
        assertEquals("child", scope[1].getPrefix());
        assertEquals("grandchild", scope[2].getPrefix());

        assertEquals(2, added.size());
        assertEquals(Namespace.XML_NAMESPACE, added.get(0));
        assertEquals(new NamespaceImpl("http://example.com/", "grandchild"), added.get(1));

        stack.pop();
        scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals("root", scope[0].getPrefix());
        assertEquals("child", scope[1].getPrefix());

        stack.pop();
        scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals("root", scope[0].getPrefix());
    }

    @Test
    public void testPushMultipleElementsWithNamespacesAndPrefixes() {
        NamespaceStack stack = new NamespaceStack();
        Element e1 = new Element("root", new QName("http://example.com/", "root"));
        Element e2 = new Element("child", new QName("http://example2.com/", "child"));
        stack.push(e1);
        stack.push(e2);

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertFalse(scope[0].getPrefix().equals(""));
        assertEquals("root", scope[0].getPrefix());
        assertEquals("child", scope[1].getPrefix());

        List<Namespace> added = new ArrayList<>();
        stack.push(new Element("grandchild", new QName("http://example3.com/", "grandchild")));
        stack.push(new Attribute("grandchild", "attr", new QName("http://example4.com/", "grandchild")));
        Namespace[] newScope = checkNamespace(added, Namespace.XML_NAMESPACE, scope);
        stack.pushStack(Namespace.XML_NAMESPACE, newScope, added);

        scope = stack.getScope();
        assertEquals(3, scope.length);
        assertEquals("root", scope[0].getPrefix());
        assertEquals("child", scope[1].getPrefix());
        assertEquals("grandchild", scope[2].getPrefix());

        assertEquals(2, added.size());
        assertEquals(Namespace.XML_NAMESPACE, added.get(0));
        assertEquals(new NamespaceImpl("http://example3.com/", "grandchild"), added.get(1));

        stack.pop();
        scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals("root", scope[0].getPrefix());
        assertEquals("child", scope[1].getPrefix());

        stack.pop();
        scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals("root", scope[0].getPrefix());
    }

    // Add more test cases as needed

    private List<Namespace> checkNamespace(final List<Namespace> store, final Namespace namespace, final Namespace[] scope) {
        // Implement the checkNamespace method as in the original code
    }
}