import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
        Assert.assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testPushPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", Namespace.XML_NAMESPACE));
        stack.push(new Element("element", Namespace.XML_NAMESPACE));
        stack.push(new Element("subElement", Namespace.NO_NAMESPACE));
        stack.push(new Attribute("attr", Namespace.XML_NAMESPACE));
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
        Assert.assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test(expected = IllegalStateException.class)
    public void testPushWithInvalidNamespace() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", null));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", Namespace.XML_NAMESPACE));
        stack.push(new Element("element1", Namespace.XML_NAMESPACE_2));
        stack.push(new Element("element2", Namespace.XML_NAMESPACE));
        stack.push(new Attribute("attr1", Namespace.XML_NAMESPACE_3));
        stack.push(new Attribute("attr2", Namespace.XML_NAMESPACE));
        List<Namespace> expected = new ArrayList<>();
        expected.add(Namespace.XML_NAMESPACE_3);
        expected.add(Namespace.XML_NAMESPACE_2);
        expected.add(Namespace.XML_NAMESPACE_2);
        expected.add(Namespace.XML_NAMESPACE);
        expected.add(Namespace.XML_NAMESPACE);
        Assert.assertEquals(expected, stack.addedForward());
        List<Namespace> reversed = new ArrayList<>();
        for (int i = stack.depth; i >= 0; i--) {
            reversed.add(stack.scope[i][0]);
        }
        expected.reverse();
        Assert.assertEquals(expected, reversed);
    }
}