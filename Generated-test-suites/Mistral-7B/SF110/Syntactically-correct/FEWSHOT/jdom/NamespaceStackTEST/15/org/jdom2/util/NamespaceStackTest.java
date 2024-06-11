package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Namespace;
import org.jdom2.NamespaceContext;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.EmptyIterable;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testCreation() {
        final NamespaceStack stack = new NamespaceStack();
        assertNull(stack.getScope());
        assertEquals(0, stack.depth);
        assertTrue(Arrays.equals(NamespaceStack.DEFAULTSEED, stack.added[0]));
        assertTrue(Arrays.equals(NamespaceStack.DEFAULTSEED, stack.scope[0]));

        final NamespaceStack stackWithSeed = new NamespaceStack(NamespaceStack.DEFAULTSEED);
        assertSame(NamespaceStack.DEFAULTSEED, stackWithSeed.added[0]);
        assertSame(NamespaceStack.DEFAULTSEED, stackWithSeed.scope[0]);
    }

    @Test
    public void testPush() {
        final NamespaceStack stack = new NamespaceStack();
        stack.push(createTestElement());
        final Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE };
        assertArrayEquals(expectedScope, stack.getScope());
        assertEquals(1, stack.depth);

        stack.push(createTestElementWithNamespace());
        final Namespace[] newScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, new Namespace("http://test.com", "test") };
        assertArrayEquals(newScope, stack.getScope());
        assertEquals(2, stack.depth);

        try {
            stack.push(null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        stack.pop();
        assertEquals(1, stack.depth);
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPushWithAdditionalNamespaces() {
        final NamespaceStack stack = new NamespaceStack();
        final Element element = createTestElementWithAdditionalNamespaces();
        stack.push(element);

        final Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, new Namespace("http://namespace1.com", "namespace1"), new Namespace("http://namespace2.com", "namespace2") };
        assertArrayEquals(expectedScope, stack.getScope());
        assertEquals(1, stack.depth);
    }

    @Test
    public void testPushWithAttributes() {
        final NamespaceStack stack = new NamespaceStack();
        final Element element = createTestElementWithAttributes();
        stack.push(element);

        final Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, new Namespace("http://namespace1.com", "namespace1"), new Namespace("http://namespace2.com", "namespace2"), new Namespace("http://namespace3.com", "namespace3") };
        assertArrayEquals(expectedScope, stack.getScope());
        assertEquals(1, stack.depth);
    }

    @Test
    public void testPop() {
        final NamespaceStack stack = new NamespaceStack();
        stack.push(createTestElement());
        stack.pop();
        assertEquals(0, stack.depth);
        assertNull(stack.getScope());
    }

    @Test
    public void testPushAndPopMultipleTimes() {
        final NamespaceStack stack = new NamespaceStack();
        stack.push(createTestElement());
        stack.push(createTestElementWithNamespace());
        stack.pop();
        stack.pop();
        stack.push(createTestElementWithNamespace());
        stack.push(createTestElementWithAdditionalNamespaces());
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
        assertEquals(0, stack.depth);
        assertNull(stack.getScope());
    }

    @Test
    public void testIsInScope() {
        final NamespaceStack stack = new NamespaceStack();
        stack.push(createTestElement());
        stack.push(createTestElementWithNamespace());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(new Namespace("http://test.com", "test")));
        assertFalse(stack.isInScope(new Namespace("http://other.com", "other")));
    }

    @Test
    public void testAddedForward() {
        final NamespaceStack stack = new NamespaceStack();
        stack.push(createTestElementWithAdditionalNamespaces());
        final List<Namespace> expectedNamespaces = new ArrayList<Namespace>();
        expectedNamespaces.add(new Namespace("http://namespace1.com", "namespace1"));
        expectedNamespaces.add(new Namespace("http://namespace2.com", "namespace2"));
        expectedNamespaces.add(new Namespace("http://namespace3.com", "namespace3"));
        final Iterable<Namespace> addedForward = stack.addedForward();
        assertEquals(expectedNamespaces, addedForward);
    }

    @Test
    public void testAddedReverse() {
        final NamespaceStack stack = new NamespaceStack();
        stack.push(createTestElementWithAdditionalNamespaces());
        final List<Namespace> expectedNamespaces = new ArrayList<Namespace>();
        expectedNamespaces.add(new Namespace("http://namespace3.com", "namespace3"));
        expectedNamespaces.add(new Namespace("http://namespace2.com", "namespace2"));
        expectedNamespaces.add(new Namespace("http://namespace1.com", "namespace1"));
        final Iterable<Namespace> addedReverse = stack.addedReverse();
        assertEquals(expectedNamespaces, addedReverse);
    }

    @Test
    public void testIterator() {
        final NamespaceStack stack = new NamespaceStack();
        stack.push(createTestElementWithAdditionalNamespaces());
        final Iterator<Namespace> iterator = stack.iterator();
        final List<Namespace> expectedNamespaces = new ArrayList<Namespace>();
        expectedNamespaces.add(Namespace.NO_NAMESPACE);
        expectedNamespaces.add(Namespace.XML_NAMESPACE);
        expectedNamespaces.add(new Namespace("http://namespace1.com", "namespace1"));
        expectedNamespaces.add(new Namespace("http://namespace2.com", "namespace2"));
        expectedNamespaces.add(new Namespace("http://namespace3.com", "namespace3"));
        int i = 0;
        while (iterator.hasNext()) {
            final Namespace namespace = iterator.next();
            assertEquals(expectedNamespaces.get(i++), namespace);
        }
    }

    private Element createTestElement() {
        return new Element("test", new QualifiedName("test"));
    }

    private Element createTestElementWithNamespace() {
        final Namespace namespace = new Namespace("http://test.com", "test");
        return new Element("test", new QualifiedName("test", namespace));
    }

    private Element createTestElementWithAdditionalNamespaces() {
        final Namespace namespace1 = new Namespace("http://namespace1.com", "namespace1");
        final Namespace namespace2 = new Namespace("http://namespace2.com", "namespace2");
        final Namespace namespace3 = new Namespace("http://namespace3.com", "namespace3");
        return new Element("test", new QualifiedName("test"), new NamespaceContext() {
            public Namespace getNamespace(String prefix) {
                if (Namespace.XML_NAMESPACE.getURI().equals(prefix)) {
                    return Namespace.XML_NAMESPACE;
                }
                if (Namespace.NO_NAMESPACE.getURI().equals(prefix)) {
                    return Namespace.NO_NAMESPACE;
                }
                if (namespace1.getURI().equals(prefix)) {
                    return namespace1;
                }
                if (namespace2.getURI().equals(prefix)) {
                    return namespace2;
                }
                if (namespace3.getURI().equals(prefix)) {
                    return namespace3;
                }
                return null;
            }
        });
    }
}