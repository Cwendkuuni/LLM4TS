import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertEquals(0, stack.getDepth());
        Assert.assertArrayEquals(new Namespace[0], stack.getScope());
        Assert.assertSame(NamespaceStack.EMPTY, stack.addedForward().iterator().next());
        Assert.assertSame(NamespaceStack.EMPTY, stack.addedReverse().iterator().next());
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Namespace mns = element.getNamespace();
        Namespace[] newscope = stack.scope[1];
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] expectedAdded = new Namespace[]{mns};
        Namespace[] expectedScope = new Namespace[]{mns, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedAdded, stack.added[1]);
        Assert.assertArrayEquals(expectedScope, stack.scope[1]);
        Assert.assertEquals(1, stack.getDepth());
        Assert.assertSame(mns, newscope[0]);
        Assert.assertEquals(1, newscope.length);
        Assert.assertSame(NamespaceStack.XML_NAMESPACE, newscope[1]);
        Assert.assertEquals(1, toadd.size());
        Assert.assertSame(Namespace.NO_NAMESPACE, toadd.get(0));

        Element element2 = new Element("elem", "http://test.com/");
        stack.push(element2);
        Namespace mns2 = element2.getNamespace();
        Namespace[] newscope2 = stack.scope[2];
        List<Namespace> toadd2 = new ArrayList<>();
        Namespace[] expectedAdded2 = new Namespace[]{mns, mns2};
        Namespace[] expectedScope2 = new Namespace[]{mns, mns2, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedAdded2, stack.added[2]);
        Assert.assertArrayEquals(expectedScope2, stack.scope[2]);
        Assert.assertEquals(2, stack.getDepth());
        Assert.assertSame(mns2, newscope2[0]);
        Assert.assertEquals(2, newscope2.length);
        Assert.assertEquals(1, toadd2.size());
        Assert.assertSame(mns, toadd2.get(0));
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute att = new Attribute("name", "value", "http://test.com/");
        stack.push(att);
        Namespace mns = att.getNamespace();
        Namespace[] newscope = stack.scope[1];
        List<Namespace> toadd = new ArrayList<>();
        Namespace[] expectedAdded = new Namespace[]{mns};
        Namespace[] expectedScope = new Namespace[]{mns, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedAdded, stack.added[1]);
        Assert.assertArrayEquals(expectedScope, stack.scope[1]);
        Assert.assertEquals(1, stack.getDepth());
        Assert.assertSame(mns, newscope[0]);
        Assert.assertEquals(1, newscope.length);
        Assert.assertEquals(1, toadd.size());
        Assert.assertSame(Namespace.NO_NAMESPACE, toadd.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenDepthIsZero() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack(new Namespace[]{"http://test1.com/", "http://test2.com/"});
        Assert.assertEquals(2, stack.getDepth());
        stack.pop();
        Assert.assertEquals(1, stack.getDepth());
        Assert.assertArrayEquals(new Namespace[]{"http://test2.com/"}, stack.getScope());
        Assert.assertSame(NamespaceStack.EMPTY, stack.addedForward().iterator().next());
        Assert.assertSame(NamespaceStack.EMPTY, stack.addedReverse().iterator().next());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack(new Namespace[]{"http://test1.com/", "http://test2.com/"});
        Namespace namespace1 = new Namespace("http://test1.com/");
        Namespace namespace2 = new Namespace("http://test3.com/");
        Assert.assertTrue(stack.isInScope(namespace1));
        Assert.assertTrue(stack.isInScope(namespace2));
        stack.push(new Element("elem", "http://test3.com/"));
        Assert.assertTrue(stack.isInScope(namespace2));
        stack.pop();
        Assert.assertFalse(stack.isInScope(namespace2));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack(new Namespace[]{"http://test1.com/", "http://test2.com/"});
        Namespace namespace1 = new Namespace("http://test3.com/");
        stack.push(new Element("elem1", "http://test3.com/"));
        stack.push(new Element("elem2", "http://test3.com/"));
        Assert.assertEquals(3, stack.getDepth());
        for (Namespace ns : stack.addedForward()) {
            Assert.assertSame(namespace1, ns);
        }
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack(new Namespace[]{"http://test1.com/", "http://test2.com/"});
        Namespace namespace1 = new Namespace("http://test3.com/");
        stack.push(new Element("elem1", "http://test3.com/"));
        stack.push(new Element("elem2", "http://test3.com/"));
        Assert.assertEquals(3, stack.getDepth());
        for (Namespace ns : stack.addedReverse()) {
            Assert.assertSame(namespace1, ns);
        }
    }

    @Test
    public void testBinarySearch() {
        Namespace[] data = new Namespace[]{new Namespace("http://test1.com/"), new Namespace("http://test2.com/"), new Namespace("http://test3.com/")};
        int left = 0;
        int right = data.length - 1;
        Namespace key = new Namespace("http://test2.com/");
        int expectedResult = 1;
        int result = NamespaceStack.binarySearch(data, left, right, key);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBinarySearchWithNullArray() {
        int left = 0;
        int right = 0;
        Namespace key = new Namespace("http://test.com/");
        NamespaceStack.binarySearch(null, left, right, key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBinarySearchWithNullKey() {
        Namespace[] data = new Namespace[3];
        int left = 0;
        int right = 2;
        Namespace key = null;
        NamespaceStack.binarySearch(data, left, right, key);
    }
}