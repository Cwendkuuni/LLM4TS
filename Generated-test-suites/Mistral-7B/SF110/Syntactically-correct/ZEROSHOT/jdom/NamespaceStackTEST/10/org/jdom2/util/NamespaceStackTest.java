package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;
    private Namespace[] seed;

    @Before
    public void setup() {
        seed = new Namespace[] { Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE };
        namespaceStack = new NamespaceStack(seed);
    }

    @Test
    public void testConstructorWithEmptyArray() {
        NamespaceStack emptyStack = new NamespaceStack();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, emptyStack.getScope());
        Assert.assertArrayEquals(NamespaceStack.EMPTY, emptyStack.addedForward().toArray(new Namespace[0]));
        Assert.assertArrayEquals(NamespaceStack.EMPTY, emptyStack.addedReverse().toArray(new Namespace[0]));
    }

    @Test
    public void testConstructorWithNamespaceArray() {
        Namespace mns1 = new Namespace("test1");
        Namespace mns2 = new Namespace("test2");
        List<Namespace> toadd = new ArrayList<>();
        toadd.add(mns2);
        toadd.add(mns1);

        NamespaceStack stack = new NamespaceStack(toadd.toArray(new Namespace[0]));
        Assert.assertArrayEquals(new Namespace[]{mns2, mns1}, stack.getScope());
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.addedForward().toArray(new Namespace[0]));
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.addedReverse().toArray(new Namespace[0]));
    }

    @Test
    public void testPushElement() {
        Element element = new JDOMMockElement("test-element");
        Namespace mns = new Namespace("test");
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = checkNamespace(toadd, mns, namespaceStack.scope[namespaceStack.depth]);

        namespaceStack.push(element);
        Assert.assertEquals(namespaceStack.depth, 1);
        Assert.assertArrayEquals(new Namespace[]{mns}, namespaceStack.scope[namespaceStack.depth]);
        Assert.assertArrayEquals(toadd.toArray(new Namespace[0]), namespaceStack.added[namespaceStack.depth]);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushElementWithNegativeDepth() {
        namespaceStack.pop();
        namespaceStack.push(new JDOMMockElement("test-element"));
    }

    @Test
    public void testPushAttribute() {
        Attribute att = new JDOMMockAttribute("test-attribute", "test-value");
        Namespace mns = new Namespace("test");

        namespaceStack.push(att);
        Assert.assertEquals(namespaceStack.depth, 1);
        Assert.assertArrayEquals(new Namespace[]{mns}, namespaceStack.scope[namespaceStack.depth]);
        Assert.assertArrayEquals(Namespace.class, namespaceStack.added[namespaceStack.depth].getClass());
        Assert.assertEquals(1, namespaceStack.added[namespaceStack.depth].length);
    }

    @Test
    public void testPushMultipleNamespacesElement() {
        Element element = new JDOMMockElement("test-element");
        Namespace mns1 = new Namespace("test1");
        Namespace mns2 = new Namespace("test2");

        namespaceStack.push(element);
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = checkNamespace(toadd, mns2, namespaceStack.scope[namespaceStack.depth]);

        namespaceStack.push(element);
        toadd.clear();
        newscope = checkNamespace(toadd, mns1, newscope);

        Assert.assertEquals(namespaceStack.depth, 2);
        Assert.assertArrayEquals(new Namespace[]{mns1, mns2}, namespaceStack.scope[namespaceStack.depth]);
        Assert.assertArrayEquals(new Namespace[]{mns1}, namespaceStack.added[namespaceStack.depth]);
    }

    @Test
    public void testPushMultipleNamespacesAttribute() {
        Attribute att1 = new JDOMMockAttribute("test1-attribute", "test1-value");
        Attribute att2 = new JDOMMockAttribute("test2-attribute", "test2-value");
        Namespace mns = new Namespace("test");

        namespaceStack.push(att1);
        namespaceStack.push(att2);
        Assert.assertEquals(namespaceStack.depth, 2);
        Assert.assertArrayEquals(new Namespace[]{mns, mns}, namespaceStack.scope[namespaceStack.depth]);
        Assert.assertArrayEquals(new Namespace[]{att2.getNamespace()}, namespaceStack.added[namespaceStack.depth]);
    }

    @Test
    public void testPop() {
        Element element = new JDOMMockElement("test-element");
        namespaceStack.push(element);
        Assert.assertEquals(namespaceStack.depth, 1);

        namespaceStack.pop();
        Assert.assertEquals(namespaceStack.depth, 0);
        Assert.assertArrayEquals(NamespaceStack.EMPTY, namespaceStack.getScope());
        Assert.assertArrayEquals(NamespaceStack.EMPTY, namespaceStack.addedForward().toArray(new Namespace[0]));
        Assert.assertArrayEquals(NamespaceStack.EMPTY, namespaceStack.addedReverse().toArray(new Namespace[0]));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWithNegativeDepth() {
        namespaceStack.pop();
        namespaceStack.pop();
    }

    @Test
    public void testIsInScope() {
        Element element = new JDOMMockElement("test-element");
        Namespace mns = new Namespace("test");
        namespaceStack.push(element);

        Assert.assertTrue(namespaceStack.isInScope(mns));
        Assert.assertFalse(namespaceStack.isInScope(new Namespace("test2")));
    }

    @Test
    public void testIterator() {
        Element element = new JDOMMockElement("test-element");
        Namespace mns1 = new Namespace("test1");
        Namespace mns2 = new Namespace("test2");

        namespaceStack.push(element);
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = checkNamespace(toadd, mns2, namespaceStack.scope[namespaceStack.depth]);

        namespaceStack.push(element);
        toadd.clear();
        newscope = checkNamespace(toadd, mns1, newscope);

        Iterator<Namespace> iterator = namespaceStack.iterator();
        Assert.assertFalse(iterator.hasNext());
        iterator.next();
        Assert.assertEquals(mns2, iterator.next());
        Assert.assertEquals(mns1, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedForward() {
        Element element = new JDOMMockElement("test-element");
        Namespace mns1 = new Namespace("test1");
        Namespace mns2 = new Namespace("test2");

        namespaceStack.push(element);
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = checkNamespace(toadd, mns2, namespaceStack.scope[namespaceStack.depth]);

        namespaceStack.push(element);
        toadd.clear();
        newscope = checkNamespace(toadd, mns1, newscope);

        Iterator<Namespace> iterator = namespaceStack.iterator();
        Assert.assertFalse(iterator.hasNext());
        iterator.next();
        Assert.assertEquals(mns2, iterator.next());
        Assert.assertEquals(mns1, iterator.next());
        Assert.assertFalse(iterator.hasNext());

        Iterable<Namespace> addedForward = namespaceStack.addedForward();
        Assert.assertArrayEquals(Arrays.asList(mns1, mns2).toArray(new Namespace[0]), addedForward.toArray(new Namespace[0]));
    }

    @Test
    public void testAddedReverse() {
        Element element = new JDOMMockElement("test-element");
        Namespace mns1 = new Namespace("test1");
        Namespace mns2 = new Namespace("test2");

        namespaceStack.push(element);
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] newscope = checkNamespace(toadd, mns2, namespaceStack.scope[namespaceStack.depth]);

        namespaceStack.push(element);
        toadd.clear();
        newscope = checkNamespace(toadd, mns1, newscope);

        Iterator<Namespace> iterator = namespaceStack.iterator();
        Assert.assertFalse(iterator.hasNext());
        iterator.next();
        Assert.assertEquals(mns2, iterator.next());
        Assert.assertEquals(mns1, iterator.next());
        Assert.assertFalse(iterator.hasNext());

        Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
        Assert.assertArrayEquals(Arrays.asList(mns1, mns2).toArray(new Namespace[0]), addedReverse.toArray(new Namespace[0]));
    }

    private List<Namespace> checkNamespace(List<Namespace> store, Namespace namespace, Namespace[] scope) {
        // Implement the checkNamespace method from the NamespaceStack class here
        // ...

        return store;
    }

    private static class JDOMMockElement implements Element {
        private final String name;

        public JDOMMockElement(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Attribute getAttribute(String localName) throws JDOMException {
            return null;
        }

        @Override
        public Attribute[] getAttributes() throws JDOMException {
            return new Attribute[0];
        }

        @Override
        public Element getChild(int index) throws JDOMException {
            return null;
        }

        @Override
        public Iterator<Element> iterator() throws JDOMException {
            return null;
        }

        @Override
        public Namespace getNamespace() {
            return null;
        }

        @Override
        public boolean hasNamespaceDeclaration() {
            return false;
        }

        @Override
        public String getNamespaceURI() {
            return null;
        }

        @Override
        public String getPrefix() {
            return null;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getQualifiedName() {
            return null;
        }

        @Override
        public int getAttributeCount() {
            return 0;
        }

        @Override
        public QName getName() {
            return null;
        }

        @Override
        public boolean hasAdditionalNamespaces() {
            return false;
        }

        @Override
        public Iterator<Namespace> getAdditionalNamespaces() {
            return null;
        }
    }

    private static class JDOMMockAttribute implements Attribute {
        private final String name;
        private final String value;

        public JDOMMockAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getLocalName() {
            return name;
        }

        @Override
        public String getQualifiedName() {
            return null;
        }

        @Override
        public Namespace getNamespace() {
            return null;
        }
    }
}

class NamespaceIterable implements Iterable<Namespace>, Iterator<Namespace> {
    // The code for this inner class is provided in the original NamespaceStack class
}

class EmptyIterable implements Iterable<Namespace>, Iterator<Namespace> {
    // The code for this inner class is provided in the original NamespaceStack class
}