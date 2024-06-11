import org.jdom2.Namespace;
import org.jdom2.util.NamespacesStack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Assert.assertArrayEquals(new Namespace[0], namespaceStack.getScope());
        Assert.assertTrue(namespaceStack.isEmpty());
    }

    @Test
    public void testPushElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        namespaceStack.push(element);
        Namespace[] expectedScope = {Namespace.UNDEFINED, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
        Assert.assertEquals(Namespace.UNDEFINED, namespaceStack.peek());
    }

    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root", "http://example.com");
        namespaceStack.push(element);
        Namespace[] expectedScope = {Namespace.UNDEFINED, Namespace.XML_NAMESPACE, new Namespace("example", "http://example.com")};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
        Assert.assertEquals(Namespace.UNDEFINED, namespaceStack.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPushElementWithNullElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(null);
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute = new Attribute("attr", "http://example.com");
        namespaceStack.push(attribute);
        Namespace[] expectedScope = {Namespace.UNDEFINED, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
        Assert.assertEquals(Namespace.UNDEFINED, namespaceStack.peek());
    }

    @Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        namespaceStack.push(element);
        namespaceStack.pop();
        Assert.assertArrayEquals(new Namespace[0], namespaceStack.getScope());
        Assert.assertTrue(namespaceStack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.pop();
    }

    @Test
    public void testIsInScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("root");
        namespaceStack.push(element);
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        Assert.assertTrue(namespaceStack.isInScope(Namespace.UNDEFINED));
        Assert.assertFalse(namespaceStack.isInScope(new Namespace("example", "http://example.com")));
    }

    @Test
    public void testPushMultipleElementsWithSameNamespace() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element1 = new Element("root1");
        Element element2 = new Element("root2");
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        Namespace[] expectedScope = {Namespace.UNDEFINED, Namespace.XML_NAMESPACE, Namespace.UNDEFINED};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
        Assert.assertEquals(Namespace.UNDEFINED, namespaceStack.peek());
    }

    @Test
    public void testPushMultipleElementsWithDifferentNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element1 = new Element("root1", "http://namespace1.com");
        Element element2 = new Element("root2", "http://namespace2.com");
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        Namespace[] expectedScope = {Namespace.UNDEFINED, Namespace.XML_NAMESPACE, new Namespace("namespace1", "http://namespace1.com"), new Namespace("namespace2", "http://namespace2.com")};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
        Assert.assertEquals(Namespace.UNDEFINED, namespaceStack.peek());
    }

    @Test
    public void testPushMultipleAttributesWithSameNamespace() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute1 = new Attribute("attr1");
        Attribute attribute2 = new Attribute("attr2");
        namespaceStack.push(attribute1);
        namespaceStack.push(attribute2);
        Namespace[] expectedScope = {Namespace.UNDEFINED, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
        Assert.assertEquals(Namespace.UNDEFINED, namespaceStack.peek());
    }

    @Test
    public void testPushMultipleAttributesWithDifferentNamespaces() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute1 = new Attribute("attr1", "http://namespace1.com");
        Attribute attribute2 = new Attribute("attr2", "http://namespace2.com");
        namespaceStack.push(attribute1);
        namespaceStack.push(attribute2);
        Namespace[] expectedScope = {Namespace.UNDEFINED, Namespace.XML_NAMESPACE, new Namespace("namespace1", "http://namespace1.com"), new Namespace("namespace2", "http://namespace2.com")};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
        Assert.assertEquals(Namespace.UNDEFINED, namespaceStack.peek());
    }
}