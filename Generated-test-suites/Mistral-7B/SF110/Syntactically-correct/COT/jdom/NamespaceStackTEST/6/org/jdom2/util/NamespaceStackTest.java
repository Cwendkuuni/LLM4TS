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
        NamespacesStack stack = new NamespacesStack();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        Assert.assertEquals(0, stack.depth);
    }

    @Test
    public void testPushElement() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root");
        stack.push(element);
        Namespace expectedNamespace = Namespace.getXmlNamespace();
        Namespace[] newScope = {expectedNamespace};
        List<Namespace> toAdd = new ArrayList<>();
        Namespace mns = element.getNamespace();
        Namespace[] newScopeCheck = stack.checkNamespace(toAdd, mns, NamespaceStack.EMPTY);
        Assert.assertArrayEquals(newScopeCheck, newScope);
        stack.pushStack(mns, newScope, toAdd);
        Namespace[] actualScope = stack.getScope();
        Assert.assertArrayEquals(newScope, actualScope);
    }

    @Test
    public void testPushAttribute() {
        NamespacesStack stack = new NamespacesStack();
        Attribute att = new Attribute("xmlns", "http://www.w3.org/2000/xmlns/");
        stack.push(att);
        Namespace expectedNamespace = Namespace.getXmlNamespace();
        Namespace[] newScope = {expectedNamespace};
        stack.pushStack(expectedNamespace, newScope, new ArrayList<>());
        Namespace[] actualScope = stack.getScope();
        Assert.assertArrayEquals(newScope, actualScope);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespacesStack stack = new NamespacesStack();
        stack.pop();
    }

    @Test
    public void testPop() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root");
        stack.push(element);
        stack.push(new Attribute("xmlns", "http://www.w3.org/2000/xmlns/"));
        stack.pop();
        Namespace[] actualScope = stack.getScope();
        Namespace[] expectedScope = new Namespace[1];
        expectedScope[0] = Namespace.getXmlNamespace();
        Assert.assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root");
        stack.push(element);
        Namespace namespace1 = new Namespace("ns1", "http://www.example.com/ns1");
        stack.push(namespace1);
        Namespace namespace2 = new Namespace("ns2", "http://www.example.com/ns2");
        stack.push(namespace2);
        Namespace[] expectedScope = {Namespace.getXmlNamespace(), namespace1, namespace2};
        Namespace[] actualScope = stack.getScope();
        Assert.assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInScope() {
        NamespacesStack stack = new NamespacesStack();
        Namespace namespace1 = new Namespace("ns1", "http://www.example.com/ns1");
        stack.push(namespace1);
        Assert.assertTrue(stack.isInScope(namespace1));
        Assert.assertFalse(stack.isInScope(new Namespace("ns2", "http://www.example.com/ns2")));
    }

    @Test
    public void testPushWithDuplicateNamespaces() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root");
        stack.push(element);
        Namespace namespace1 = new Namespace("ns1", "http://www.example.com/ns1");
        stack.push(namespace1);
        stack.push(namespace1);
        Namespace[] expectedScope = {Namespace.getXmlNamespace(), namespace1};
        Namespace[] actualScope = stack.getScope();
        Assert.assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPushEmptyList() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root"));
        stack.push(new Attribute("xmlns", "http://www.w3.org/2000/xmlns/"));
        Namespace[] expectedScope = NamespaceStack.EMPTY;
        Namespace[] actualScope = stack.getScope();
        Assert.assertArrayEquals(expectedScope, actualScope);
    }
}