package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(2, stack.getScope().length);
        assertArrayEquals(Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, stack.getScope());

        NamespaceStack stackWithSeed = new NamespaceStack(new Namespace[] {Namespace.PREFIX1, Namespace.PREFIX2});
        assertEquals(2, stackWithSeed.getScope().length);
        Namespace expected = new Namespace(Namespace.PREFIX1, "http://example.com/prefix1");
        assertArrayEquals(expected, new Namespace(Namespace.PREFIX1, "http://example.com/prefix1"), stackWithSeed.getScope());
        expected = new Namespace(Namespace.PREFIX2, "http://example.com/prefix2");
        assertArrayEquals(expected, new Namespace(Namespace.PREFIX2, "http://example.com/prefix2"), stackWithSeed.getScope()[1]);
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", new QName("root", Namespace.XML_NAMESPACE));
        stack.push(element);
        Namespace expected = Namespace.XML_NAMESPACE;
        Namespace[] scope = stack.getScope();
        assertArrayEquals(expected, expected, scope[0]);
        assertEquals(1, stack.getScope().length);

        Element el2 = new Element("element", new QName("element", new QName("http://example.com/prefix1").getNamespaceURI()));
        stack.push(el2);
        Namespace[] newScope = checkNamespace(new ArrayList<>(), new Namespace("prefix1", "http://example.com/prefix1"), scope[0]);
        stack.pushStack(new Namespace("prefix1", newScope), toAdd -> {
            toAdd.add(el2.getNamespace());
            toAdd.add(new Namespace("prefix2", "http://example.com/prefix2"));
        });
        assertEquals(3, stack.getScope().length);
        assertArrayEquals(new Namespace("prefix2", "http://example.com/prefix2"), newNamespace("prefix2", "http://example.com/prefix2"), stack.getScope()[2]);
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute att = new Attribute("att", new QName("att", Namespace.XML_NAMESPACE));
        stack.push(att);
        Namespace expected = Namespace.XML_NAMESPACE;
        Namespace[] scope = stack.getScope();
        assertArrayEquals(expected, expected, scope[0]);
        assertEquals(1, stack.getScope().length);

        Element element = new Element("element", new QName("element", new QName("http://example.com/prefix1").getNamespaceURI()));
        Attribute att2 = new Attribute("att2", new QName("att2", new QName("http://example.com/prefix2").getNamespaceURI()));
        stack.push(element);
        stack.push(att2);
        Namespace[] newScope = checkNamespace(new ArrayList<>(), new Namespace("prefix2", "http://example.com/prefix2"), scope[0]);
        stack.pushStack(new Namespace("prefix2", newScope), toAdd -> toAdd.add(att2.getNamespace()));
        assertEquals(3, stack.getScope().length);
        assertArrayEquals(new Namespace("prefix2", "http://example.com/prefix2"), newNamespace("prefix2", "http://example.com/prefix2"), stack.getScope()[2]);
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", new QName("root", Namespace.XML_NAMESPACE));
        stack.push(element);
        stack.pop();
        assertArrayEquals(Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, stack.getScope()[0]);
        assertEquals(0, stack.getScope().length);

        Element el2 = new Element("element", new QName("element", new QName("http://example.com/prefix1").getNamespaceURI()));
        stack.push(el2);
        stack.pop();
        assertEquals(1, stack.getScope().length);
        assertArrayEquals(Namespace.XML_NAMESPACE, Namespace.XML_NAMESPACE, stack.getScope()[0]);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", new QName("root", Namespace.XML_NAMESPACE));
        stack.push(element);
        assertTrue(stack.isInScope(new Namespace(Namespace.XML_NAMESPACE.getPrefix(), Namespace.XML_NAMESPACE.getURI())));
        assertFalse(stack.isInScope(new Namespace("prefix1", "http://example.com/prefix1")));

        Element el2 = new Element("element", new QName("element", new QName("http://example.com/prefix1").getNamespaceURI()));
        stack.push(el2);
        assertTrue(stack.isInScope(new Namespace("prefix1", "http://example.com/prefix1")));
        assertFalse(stack.isInScope(new Namespace("prefix2", "http://example.com/prefix2")));
    }

    private List<Namespace> toAdd = new ArrayList<>();

    private Namespace[] checkNamespace(List<Namespace> toAdd, Namespace namespace, Namespace[] scope) {
        Namespace[] newScope = checkNamespace(toAdd, namespace, scope);
        return newScope;
    }

    private Namespace[] checkNamespace(List<Namespace> toAdd, Namespace namespace, Namespace[] scope) {
        if (namespace == scope[0]) {
            return scope;
        }
        if (namespace.getPrefix().equals(scope[0].getPrefix())) {
            toAdd.add(namespace);
            Namespace[] nscope = (Namespace[]) scope.clone();
            nscope[0] = namespace;
            return nscope;
        }
        int ip = binarySearch(scope, 1, scope.length, namespace);
        if (ip >= 0) {
            if (namespace == scope[ip]) {
                return scope;
            }
            Namespace[] nscope2 = (Namespace[]) scope.clone();
            nscope2[ip] = namespace;
            return nscope2;
        }
        toAdd.add(namespace);
        Namespace[] nscope2 = (Namespace[]) scope.clone();
        int ip2 = binarySearch(nscope2, 0, nscope2.length, namespace);
        if (ip2 >= 0) {
            nscope2[ip2] = namespace;
            return nscope2;
        }
        Namespace[] nscope3 = new Namespace[scope.length + 1];
        System.arraycopy(nscope2, 0, nscope3, 0, nscope2.length);
        int ip3 = binarySearch(nscope3, 0, nscope3.length, namespace);
        nscope3[ip3] = namespace;
        return nscope3;
    }

    private int binarySearch(final Namespace[] data, int left, int right, final Namespace key) {
        --right;
        while (left <= right) {
            final int mid = left + right >>> 1;
            if (data[mid].getPrefix().equals(key.getPrefix()) && data[mid].getURI().equals(key.getURI())) {
                return mid;
            }
            if (data[mid].getPrefix().compareTo(key.getPrefix()) < 0) {
                left = mid + 1;
            }
            else {
                if (data[mid].getPrefix().compareTo(key.getPrefix()) <= 0) {
                    return mid;
                }
                right = mid - 1;
            }
        }
        return -left - 1;
    }
}