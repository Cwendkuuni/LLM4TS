import org.jdom2.Namespace;
import org.jdom2.util.NamespacesStack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackConstructor() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Assert.assertArrayEquals(new Namespace[0], namespaceStack.getScope());
        Assert.assertEquals(0, namespaceStack.depth);
    }

    @Test
    public void testNamespaceStackWithSeed() {
        Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        NamespaceStack namespaceStack = new NamespaceStack(seed);
        Assert.assertArrayEquals(seed, namespaceStack.getScope());
        Assert.assertEquals(1, namespaceStack.depth);
    }

    @Test
    public void testPushElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        Namespace[] newscope = namespaceStack.scope[namespaceStack.depth];
        Assert.assertArrayEquals(new Namespace[]{mns}, newscope);
        Assert.assertEquals(1, namespaceStack.depth);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushElementWithNegativeDepth() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        namespaceStack.pop();
        namespaceStack.push(element);
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute att = new Attribute("foo", "bar");
        Namespace mns = att.getNamespace();
        namespaceStack.push(att);
        Namespace[] newscope = namespaceStack.scope[namespaceStack.depth];
        Assert.assertArrayEquals(new Namespace[]{mns}, newscope);
        Assert.assertEquals(1, namespaceStack.depth);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushAttributeWithNegativeDepth() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute att = new Attribute("foo", "bar");
        namespaceStack.pop();
        namespaceStack.push(att);
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        Attribute att = new Attribute("foo", "bar");
        Namespace mns2 = att.getNamespace();
        namespaceStack.push(att);
        Namespace[] newscope = namespaceStack.scope[namespaceStack.depth];
        Assert.assertArrayEquals(new Namespace[]{mns, mns2}, newscope);
        Assert.assertEquals(2, namespaceStack.depth);
    }

    @Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        namespaceStack.pop();
        Namespace[] scope = namespaceStack.getScope();
        Assert.assertArrayEquals(new Namespace[0], scope);
        Assert.assertEquals(0, namespaceStack.depth);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWithEmptyStack() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.pop();
    }

    @Test
    public void testGetScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        Namespace[] scope = namespaceStack.getScope();
        Assert.assertArrayEquals(new Namespace[]{mns}, scope);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        Assert.assertTrue(namespaceStack.isInScope(mns));
        Assert.assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        Iterable<Namespace> addedForward = namespaceStack.addedForward();
        Assert.assertNotNull(addedForward);
        Assert.assertEquals(1, addedForward.size());
        Assert.assertArrayEquals(new Namespace[]{mns}, addedForward.toArray(new Namespace[0]));
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
        Assert.assertNotNull(addedReverse);
        Assert.assertEquals(1, addedReverse.size());
        Assert.assertArrayEquals(new Namespace[]{mns}, addedReverse.toArray(new Namespace[0]));
    }

    @Test
    public void testIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        Namespace mns = element.getNamespace();
        namespaceStack.push(element);
        Iterator<Namespace> iterator = namespaceStack.iterator();
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(mns, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}