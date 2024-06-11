import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackEmpty() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
        Assert.assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
        Assert.assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testNamespaceStackWithDefaultSeed() {
        NamespaceStack stack = new NamespaceStack(Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE);
        Assert.assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
        Assert.assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        Assert.assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        Assert.assertFalse(stack.isInScope(Namespace.UNSPECIFIED));

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        Assert.assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
        Assert.assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        Assert.assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testNamespaceStackWithCustomNamespaces() {
        Namespace xsd = new Namespace("http://www.w3.org/2001/XMLSchema", "xsd");
        Namespace mc = new Namespace("http://schemas.openxmlformats.org/markup-compatibility/2006", "mc");

        List<Namespace> toAdd = new ArrayList<>();
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root", "xml", Namespace.XML_NAMESPACE));
        stack.push(new Element("root", "xsd:element", xsd));
        stack.push(new Element("root", "mc:element", mc));
        stack.push(new Attribute("root", "xml:attribute", Namespace.XML_NAMESPACE));
        stack.push(new Attribute("root", "xsd:attribute", xsd));

        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE, xsd, mc}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[1][0]);
        Assert.assertEquals(xsd, stack.scope[1][1]);
        Assert.assertEquals(mc, stack.scope[1][2]);
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.added[stack.depth][0]);
        Assert.assertEquals(xsd, stack.added[stack.depth][1]);
        Assert.assertEquals(mc, stack.added[stack.depth][2]);

        Assert.assertEquals(new NamespaceIterable(stack.added[stack.depth], true), stack.addedForward());
        Assert.assertEquals(new NamespaceIterable(stack.added[stack.depth], false), stack.addedReverse());

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE, xsd}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);
        Assert.assertEquals(xsd, stack.scope[1][0]);

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);

        stack.pop();
        Assert.assertArrayEquals(new Namespace[]{Namespace.XML_NAMESPACE}, stack.getScope());
        Assert.assertEquals(Namespace.XML_NAMESPACE, stack.scope[0][0]);

        Assert.assertFalse(stack.isInScope(xsd));
        Assert.assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }
}