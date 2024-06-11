package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.EmptyIterable;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
        assertEquals(-1, stack.depth);
        assertArrayEquals(Namespace.NO_NAMESPACE, stack.scope[0]);
        assertArrayEquals(Namespace.NO_NAMESPACE, stack.added[0]);
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        stack.push(element);
        assertEquals(0, stack.depth);
        assertArrayEquals(Namespace.NO_NAMESPACE, stack.scope[0]);
        assertArrayEquals(new Namespace[]{mns}, stack.added[0]);

        Element element2 = new Element("root2");
        Namespace mns2 = element2.getNamespace();
        stack.push(element2);
        assertEquals(1, stack.depth);
        assertArrayEquals(new Namespace[]{mns, mns2}, stack.scope[0]);
        assertArrayEquals(new Namespace[]{mns, mns2}, stack.added[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute att = new Attribute("att", "value");
        Namespace mns = att.getNamespace();
        stack.push(att);
        assertEquals(0, stack.depth);
        assertArrayEquals(Namespace.NO_NAMESPACE, stack.scope[0]);
        assertArrayEquals(new Namespace[]{mns}, stack.added[0]);
    }

    @Test
    public void testPushWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Attribute att1 = new Attribute("att1", "value1");
        stack.push(att1);
        Attribute att2 = new Attribute("att2", "value2");
        stack.push(att2);

        Element element2 = new Element("root2");
        stack.push(element2);
        Attribute att3 = new Attribute("att3", "value3");
        stack.push(att3);

        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, stack.scope[stack.depth]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, Arrays.copyOf(stack.added, stack.added.length));
    }

    @Test
    public void testPushWithSameNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("root");
        stack.push(element1);
        Element element2 = new Element("root");
        stack.push(element2);

        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, stack.scope[stack.depth]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, Arrays.copyOf(stack.added, stack.added.length));
    }

    @Test
    public void testPushWithSamePrefixNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("{http://example.com}root");
        stack.push(element1);
        Element element2 = new Element("{http://example.com}root");
        stack.push(element2);

        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("http://example.com", "example")}, stack.scope[stack.depth]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE, new Namespace("http://example.com", "example")}, Arrays.copyOf(stack.added, stack.added.length));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        stack.push(new Element("root2"));
        stack.push(new Element("root3"));

        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE};
        Namespace[] expectedAdded = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE};

        stack.pop();
        assertEquals(1, stack.depth);
        assertArrayEquals(expectedScope, stack.scope[stack.depth]);
        assertArrayEquals(expectedAdded, Arrays.copyOf(stack.added, stack.added.length));

        stack.pop();
        assertEquals(0, stack.depth);
        assertArrayEquals(expectedScope, stack.scope[stack.depth]);
        assertArrayEquals(expectedAdded, Arrays.copyOf(stack.added, stack.added.length));
    }

    @Test
    public void testPushWithNullElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = null;
        stack.push(element);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, stack.scope[stack.depth]);
        assertArrayEquals(new Namespace[0], stack.added[stack.depth]);
    }

    @Test
    public void testPushWithNullAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = null;
        stack.push(attribute);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, stack.scope[stack.depth]);
        assertArrayEquals(new Namespace[0], stack.added[stack.depth]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPushWithEmptyList() {
        NamespaceStack stack = new NamespaceStack();
        List<Namespace> toadd = new ArrayList<>();
        stack.pushStack(Namespace.NO_NAMESPACE, new Namespace[0], toadd);
    }

    @Test
    public void testPushWithNullNamespace() {
        NamespaceStack stack = new NamespaceStack();
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = new Namespace[0];
        stack.pushStack(null, newscope, toadd);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, stack.scope[stack.depth]);
        assertArrayEquals(new Namespace[0], stack.added[stack.depth]);
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Attribute att = new Attribute("att", "value");
        stack.push(att);

        Iterator<Namespace> iterator = stack.iterator();
        List<Namespace> namespaces = new ArrayList<>();
        while (iterator.hasNext()) {
            namespaces.add(iterator.next());
        }

        assertEquals(2, namespaces.size());
        assertEquals(Namespace.NO_NAMESPACE, namespaces.get(0));
        assertEquals(Namespace.NO_NAMESPACE, namespaces.get(1));
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Attribute att = new Attribute("att", "value");
        stack.push(att);

        Namespace[] expected = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE};
        Namespace[] actual = stack.getScope();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Attribute att = new Attribute("att", "value");
        stack.push(att);

        Namespace namespace = Namespace.NO_NAMESPACE;
        assertFalse(stack.isInScope(namespace));

        Namespace namespace2 = Namespace.XML_NAMESPACE;
        assertTrue(stack.isInScope(namespace2));

        Namespace namespace3 = Namespace.NO_NAMESPACE;
        Namespace namespace4 = new Namespace("http://example.com", "example");
        stack.push(namespace4);
        assertTrue(stack.isInScope(namespace4));
        assertTrue(stack.isInScope(namespace3));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Attribute att = new Attribute("att", "value");
        stack.push(att);

        Iterable<Namespace> expected = new NamespaceIterable(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, true);
        Iterable<Namespace> actual = stack.addedForward();

        assertEquals(expected.getClass(), actual.getClass());

        Iterator<Namespace> iterator = expected.iterator();
        while (iterator.hasNext()) {
            Namespace namespace = iterator.next();
            Namespace namespace2 = actual.iterator().next();
            assertEquals(namespace, namespace2);
        }
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Attribute att = new Attribute("att", "value");
        stack.push(att);

        Iterable<Namespace> expected = new NamespaceIterable(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.NO_NAMESPACE}, false);
        Iterable<Namespace> actual = stack.addedReverse();

        assertEquals(expected.getClass(), actual.getClass());

        Iterator<Namespace> iterator = expected.iterator();
        while (iterator.hasNext()) {
            Namespace namespace = iterator.next();
            Namespace namespace2 = actual.iterator().next();
            assertEquals(namespace, namespace2);
        }
    }
}