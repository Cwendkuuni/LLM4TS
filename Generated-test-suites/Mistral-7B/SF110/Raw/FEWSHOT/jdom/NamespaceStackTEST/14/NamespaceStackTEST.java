package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.util.NamespaceStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testNewInstance() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(Arrays.asList(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE), Arrays.asList(stack.getScope()));
    }

    @Test
    public void testNewInstanceWithSeed() {
        Namespace[] seed = new Namespace[]{Namespace.NO_NAMESPACE, new Namespace("prefix1", "uri1")};
        NamespaceStack stack = new NamespaceStack(seed);
        assertEquals(seed, stack.getScope());
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        when(element.getAdditionalNamespaces()).thenReturn(Arrays.asList(new Namespace("prefix2", "uri2")));
        stack.push(element);

        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix1", "uri1"), new Namespace("prefix2", "uri2")};
        Namespace[] actualScope = stack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPushElementWithoutAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        stack.push(element);

        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix1", "uri1")};
        Namespace[] actualScope = stack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = mock(Attribute.class);
        when(attribute.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        stack.push(attribute);

        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("prefix1", "uri1")};
        Namespace[] actualScope = stack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        stack.push(element);
        stack.pop();

        assertEquals(Arrays.asList(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE), Arrays.asList(stack.getScope()));
    }

    @Test
    public void testGetAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        stack.push(element);

        Iterable<Namespace> expectedAdded = Arrays.asList(new Namespace("prefix1", "uri1"));
        Iterable<Namespace> actualAdded = stack.addedForward();
        assertEquals(expectedAdded, actualAdded);
    }

    @Test
    public void testGetAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        stack.push(element);

        Iterable<Namespace> expectedAdded = Arrays.asList(new Namespace("prefix1", "uri1"));
        Iterable<Namespace> actualAdded = stack.addedReverse();
        assertEquals(expectedAdded, actualAdded);
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        stack.push(element);

        Iterator<Namespace> actualIterator = stack.iterator();
        Namespace expectedNamespace = stack.getScope()[stack.getScope().length - 1];
        Namespace namespace = actualIterator.next();
        assertEquals(expectedNamespace, namespace);
        assertFalse(actualIterator.hasNext());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = mock(Element.class);
        when(element.getNamespace()).thenReturn(new Namespace("prefix1", "uri1"));
        stack.push(element);

        assertTrue(stack.isInScope(new Namespace("prefix1", "uri1")));
        assertTrue(stack.isInScope(new Namespace("prefix2", "uri2"))); // added with element
        assertTrue(stack.isInScope(new Namespace("prefix3", "uri3"))); // seed
        assertFalse(stack.isInScope(new Namespace("prefix4", "uri4")));
    }
}