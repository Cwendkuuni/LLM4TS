package org.jdom2.util;

import org.jdom2.internal.JdomNamespaces;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class NamespaceStackTest {

    private NamespaceStack stack;
    private Namespace xmlNamespace;
    private Namespace mynNamespace;
    private Element element;
    private Attribute attribute;

    @Before
    public void setUp() {
        xmlNamespace = JdomNamespaces.XML_NAMESPACE;
        mynNamespace = new Namespace("myNamespace");
        element = Mockito.mock(Element.class);
        attribute = Mockito.mock(Attribute.class);

        stack = new NamespaceStack();
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(NamespaceStack.EMPTY, stack.getScope());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testConstructorWithDefaultSeed() {
        Namespace[] seed = new Namespace[]{JdomNamespaces.XML_NAMESPACE, JdomNamespaces.NO_NAMESPACE};
        stack = new NamespaceStack(seed);
        assertEquals(seed, stack.getScope());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testConstructorWithCustomSeed() {
        Namespace[] seed = new Namespace[]{mynNamespace};
        stack = new NamespaceStack(seed);
        assertEquals(seed, stack.getScope());
        assertTrue(stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testPushWithNullElement() {
        stack.push(null);
    }

    @Test
    public void testPushWithEmptyElement() {
        Mockito.doReturn(Collections.emptyList()).when(element).getAdditionalNamespaces();
        Mockito.doReturn(Collections.emptyMap()).when(element).getAttributes();

        stack.push(element);

        Namespace[] expected = new Namespace[]{xmlNamespace, mynNamespace};
        assertEquals(expected, stack.getScope());
        assertEquals(1, stack.getDepth());
    }

    @Test
    public void testPushWithElementWithAdditionalNamespaces() {
        Mockito.doReturn(Arrays.asList(mynNamespace)).when(element).getAdditionalNamespaces();
        Mockito.doReturn(Collections.singletonMap(new QName("test"), mynNamespace)).when(element).getAttributes();

        stack.push(element);

        Namespace[] expected = new Namespace[]{mynNamespace, xmlNamespace};
        assertEquals(expected, stack.getScope());
        assertEquals(1, stack.getDepth());
    }

    @Test
    public void testPushWithElementWithAdditionalAttributes() {
        Mockito.doReturn(Arrays.asList(mynNamespace)).when(element).getAdditionalNamespaces();
        Mockito.doReturn(Arrays.asList(attribute)).when(element).getAttributes();

        Namespace attributeNamespace = new Namespace("attributeNamespace");
        Mockito.doReturn(attributeNamespace).when(attribute).getNamespace();

        stack.push(element);

        Namespace[] expected = new Namespace[]{mynNamespace, attributeNamespace, xmlNamespace};
        assertEquals(expected, stack.getScope());
        assertEquals(1, stack.getDepth());
    }

    @Test
    public void testPushWithAttribute() {
        stack.push(attribute);

        Namespace[] expected = new Namespace[]{xmlNamespace, mynNamespace};
        assertEquals(expected, stack.getScope());
        assertEquals(1, stack.getDepth());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        stack.pop();
    }

    @Test
    public void testPop() {
        Namespace[] expected = new Namespace[]{xmlNamespace};
        stack.push(element);
        stack.pop();

        assertEquals(expected, stack.getScope());
        assertEquals(0, stack.getDepth());
    }

    @Test
    public void testIsInScope() {
        stack.push(element);

        assertTrue(stack.isInScope(xmlNamespace));
        assertTrue(stack.isInScope(mynNamespace));
        assertFalse(stack.isInScope(new Namespace("notInScope")));
    }

    @Test
    public void testAddedForward() {
        Mockito.doReturn(Arrays.asList(mynNamespace)).when(element).getAdditionalNamespaces();
        Mockito.doReturn(Arrays.asList(attribute)).when(element).getAttributes();

        stack.push(element);

        Iterable<Namespace> expected = new NamespaceIterable(new Namespace[]{mynNamespace}, true);
        assertEquals(expected, stack.addedForward());
    }

    @Test
    public void testAddedReverse() {
        Mockito.doReturn(Arrays.asList(mynNamespace)).when(element).getAdditionalNamespaces();
        Mockito.doReturn(Arrays.asList(attribute)).when(element).getAttributes();

        stack.push(element);

        Iterable<Namespace> expected = new NamespaceIterable(new Namespace[]{mynNamespace}, false);
        assertEquals(expected, stack.addedReverse());
    }

    @Test
    public void testIterator() {
        Mockito.doReturn(Arrays.asList(mynNamespace)).when(element).getAdditionalNamespaces();
        Mockito.doReturn(Arrays.asList(attribute)).when(element).getAttributes();

        stack.push(element);

        Iterator<Namespace> expected = new ForwardWalker(new Namespace[]{mynNamespace, xmlNamespace});
        Iterator<Namespace> actual = stack.iterator();

        assertEquals(expected.next(), actual.next());
        assertEquals(expected.hasNext(), actual.hasNext());
    }
}