import org.jdom2.Namespace;
import org.jdom2.util.NamespacesStack;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceStackTest1 {

    @Test
    public void testEmptyStack() {
        NamespacesStack stack = new NamespacesStack();
        Assert.assertArrayEquals(NamespacesStack.EMPTY, stack.getScope());
        Assert.assertEquals(0, stack.size());
    }

    @Test
    public void testPushAndPop() {
        NamespacesStack stack = new NamespacesStack();
        Namespace namespace = new Namespace("test");
        stack.push(new Element("root", namespace));
        Assert.assertEquals(namespace, stack.getScope()[0]);
        stack.pop();
        Assert.assertArrayEquals(NamespacesStack.EMPTY, stack.getScope());
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespacesStack stack = new NamespacesStack();
        Namespace namespace1 = new Namespace("test1");
        Namespace namespace2 = new Namespace("test2");
        stack.push(new Element("root", namespace1));
        stack.push(new Element("root", namespace2));
        Assert.assertEquals(new Namespace[]{namespace2, namespace1}, stack.getScope());
        stack.pop();
        Assert.assertEquals(new Namespace[]{namespace1}, stack.getScope());
        stack.pop();
        Assert.assertArrayEquals(NamespacesStack.EMPTY, stack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespacesStack stack = new NamespacesStack();
        stack.pop();
    }
}