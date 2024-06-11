import org.jdom2.Namespace;
import org.jdom2.util.NamespacesStack;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespacesStack stack = new NamespacesStack();
        Assert.assertNotNull(stack);
        Assert.assertEquals(0, stack.depth);
        Assert.assertArrayEquals(new Namespace[0], stack.scope[0]);
        Assert.assertArrayEquals(Namespace.EMPTY, stack.added[0]);
    }

    @Test
    public void testPushElement() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        Namespace mns = element.getNamespace();
        Assert.assertEquals(1, stack.depth);
        List<Namespace> toadd = stack.added[stack.depth - 1];
        Assert.assertEquals(mns, toadd.get(0));
        Assert.assertEquals(1, toadd.size());
        Namespace[] newscope = stack.scope[stack.depth];
        Assert.assertEquals(mns, newscope[0]);
        Assert.assertEquals(1, newscope.length);
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        Attribute att = new Attribute("attr", "value", Namespace.XML_NAMESPACE);
        stack.push(att);
        Namespace mns1 = element.getNamespace();
        Namespace mns2 = att.getNamespace();
        List<Namespace> toadd = stack.added[stack.depth - 1];
        Assert.assertEquals(mns1, toadd.get(0));
        Assert.assertEquals(mns2, toadd.get(1));
        Assert.assertEquals(2, toadd.size());
        Namespace[] newscope = stack.scope[stack.depth];
        Assert.assertEquals(mns1, newscope[0]);
        Assert.assertEquals(mns2, newscope[1]);
        Assert.assertEquals(2, newscope.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testPushWithOverPoppedStack() {
        NamespacesStack stack = new NamespacesStack();
        stack.pop();
        stack.push(new Element("root", "name"));
    }

    @Test
    public void testPop() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        stack.pop();
        Assert.assertEquals(0, stack.depth);
        Assert.assertNull(stack.scope[0]);
        Assert.assertNull(stack.added[0]);
    }

    @Test
    public void testAddedForward() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        List<Namespace> namespaces = stack.addedForward();
        Assert.assertEquals(1, namespaces.size());
        Assert.assertEquals(element.getNamespace(), namespaces.get(0));
    }

    @Test
    public void testAddedReverse() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        List<Namespace> namespaces = stack.addedReverse();
        Assert.assertEquals(1, namespaces.size());
        Assert.assertEquals(element.getNamespace(), namespaces.get(0));
    }

    @Test
    public void testIterator() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        Iterator<Namespace> iterator = stack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(element.getNamespace(), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        Namespace[] scope = stack.getScope();
        Assert.assertEquals(1, scope.length);
        Assert.assertEquals(element.getNamespace(), scope[0]);
    }

    @Test
    public void testIsInScope() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", "name");
        stack.push(element);
        Assert.assertTrue(stack.isInScope(element.getNamespace()));
        Assert.assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }
}