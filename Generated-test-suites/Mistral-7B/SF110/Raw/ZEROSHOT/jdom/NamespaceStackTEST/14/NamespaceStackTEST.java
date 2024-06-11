import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.EmptyIterable;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackConstruction() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
        assertEquals(0, stack.depth);
        assertEquals(NamespaceStack.DEFAULTSEED, stack.scope[0]);
        assertArrayEquals(NamespaceStack.EMPTY, stack.added[0]);

        NamespaceStack stackWithSeed = new NamespaceStack(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE});
        assertEquals(NamespaceStack.DEFAULTSEED, stackWithSeed.scope[0]);
        assertArrayEquals(new Namespace[0], stackWithSeed.added[0]);
        assertEquals(NamespaceStack.DEFAULTSEED.length, stackWithSeed.depth);
    }

    @Test
    public void testPushPop() {
        NamespaceStack stack = new NamespaceStack();

        stack.push(new Element("root"));
        assertEquals(NamespaceStack.DEFAULTSEED, stack.scope[0]);
        assertArrayEquals(new Namespace[0], stack.added[0]);
        assertEquals(1, stack.depth);
        assertEquals(Namespace.NO_NAMESPACE, stack.scope[1][0]);
        assertArrayEquals(new Namespace[0], stack.added[1]);

        stack.pop();
        assertEquals(NamespaceStack.DEFAULTSEED, stack.scope[0]);
        assertArrayEquals(new Namespace[0], stack.added[0]);
        assertEquals(0, stack.depth);

        stack.push(new Element("root"));
        stack.push(new Attribute("attr", "value"));
        assertEquals(Namespace.XML_NAMESPACE, stack.scope[1][0]);
        assertEquals(Namespace.NO_NAMESPACE, stack.scope[2][0]);
        assertEquals(3, stack.depth);

        stack.pop();
        assertEquals(Namespace.XML_NAMESPACE, stack.scope[1][0]);
        assertEquals(0, stack.depth);

        try {
            stack.pop();
            fail("Should not be able to pop empty stack");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();

        Element root = new Element("root");
        root.setName("root", "http://example.com/");
        root.addNamespaceDeclaration("ns1", "http://example.com/ns1");
        root.addNamespaceDeclaration("ns2", "http://example.com/ns2");

        stack.push(root);
        assertEquals("http://example.com/", stack.scope[1][0].getURI().toString());
        assertEquals("http://example.com/ns1", stack.scope[1][1].getURI().toString());
        assertEquals("http://example.com/ns2", stack.scope[1][2].getURI().toString());
        assertEquals(3, stack.depth);

        stack.pop();
        assertEquals(1, stack.depth);
        assertEquals("http://example.com/", stack.scope[1][0].getURI().toString());

        try {
            stack.pop();
            fail("Should not be able to pop empty stack");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testAddedNamespaces() {
        NamespaceStack stack = new NamespaceStack();

        Element root = new Element("root");
        root.setName("root", "http://example.com/");
        root.addNamespaceDeclaration("ns1", "http://example.com/ns1");
        root.addNamespaceDeclaration("ns2", "http://example.com/ns2");

        stack.push(root);

        List<Namespace> addedNamespacesForward = new ArrayList<>();
        Iterable<Namespace> addedNamespacesIterableForward = stack.addedForward();
        for (Namespace namespace : addedNamespacesIterableForward) {
            addedNamespacesForward.add(namespace);
        }

        List<Namespace> addedNamespacesReverse = new ArrayList<>();
        Iterable<Namespace> addedNamespacesIterableReverse = stack.addedReverse();
        for (Namespace namespace : addedNamespacesIterableReverse) {
            addedNamespacesReverse.add(namespace);
        }

        assertEquals("http://example.com/ns1", addedNamespacesForward.get(0).getURI().toString());
        assertEquals("http://example.com/ns2", addedNamespacesForward.get(1).getURI().toString());
        assertEquals("http://example.com/ns1", addedNamespacesReverse.get(0).getURI().toString());
        assertEquals("http://example.com/ns2", addedNamespacesReverse.get(1).getURI().toString());

        stack.pop();
        assertFalse(stack.addedForward().hasNext());
        assertFalse(stack.addedReverse().hasNext());
    }

    @Test
    public void testIterables() {
        NamespaceStack stack = new NamespaceStack();

        Element root = new Element("root");
        root.setName("root", "http://example.com/");
        root.addNamespaceDeclaration("ns1", "http://example.com/ns1");
        root.addNamespaceDeclaration("ns2", "http://example.com/ns2");

        stack.push(root);

        List<Namespace> expected = new ArrayList<>();
        expected.add(root.getNamespace());
        expected.addAll(Arrays.asList(root.getAdditionalNamespaces()));

        assertEquals(expected.size(), stack.scope[1].length);
        assertEquals(expected, Arrays.asList(stack.scope[1]));

        Iterator<Namespace> iterator = stack.iterator();
        List<Namespace> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }
}