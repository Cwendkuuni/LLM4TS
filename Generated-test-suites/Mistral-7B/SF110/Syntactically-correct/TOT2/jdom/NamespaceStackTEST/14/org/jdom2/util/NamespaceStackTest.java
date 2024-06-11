import org.jdom2.Namespace;
import org.jdom2.util.NamespacesStack;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceStackTest {
    // Test constructor
    @Test
    public void testConstructor() {
        NamespacesStack stack = new NamespacesStack();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        Assert.assertNotNull(stack.iterator());
        Assert.assertEquals(0, stack.getDepth());
    }

    // Test push method with single namespace
    @Test
    public void testPushSingleNamespace() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", null));
        Namespace expectedNamespace = new Namespace("http://test.com/");
        stack.push(new Element("child", expectedNamespace));

        Namespace[] expectedScope = {expectedNamespace, Namespace.XML_NAMESPACE};
        Namespace[] actualScope = stack.getScope();

        Assert.assertArrayEquals(expectedScope, actualScope);
        Assert.assertEquals(2, stack.getDepth());
    }

    // Test push method with multiple namespaces
    @Test
    public void testPushMultipleNamespaces() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", null));
        stack.push(new Element("child1", new Namespace("http://test1.com/")));
        stack.push(new Element("child2", new Namespace("http://test2.com/")));

        Namespace[] expectedScope = {new Namespace("http://test2.com/"), new Namespace("http://test1.com/"), Namespace.XML_NAMESPACE};
        Namespace[] actualScope = stack.getScope();

        Assert.assertArrayEquals(expectedScope, actualScope);
        Assert.assertEquals(3, stack.getDepth());
    }

    // Test push method with duplicate namespaces
    @Test(expected = IllegalArgumentException.class)
    public void testPushDuplicateNamespace() {
        NamespacesStack stack = new NamespacesStack();
        Element element = new Element("root", null);
        Namespace namespace = new Namespace("http://test.com/");
        stack.push(element);
        stack.push(namespace);
        stack.push(element);
    }

    // Test pop method
    @Test
    public void testPop() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", null));
        stack.push(new Element("child", new Namespace("http://test.com/")));
        stack.pop();

        Namespace[] expectedScope = {Namespace.XML_NAMESPACE};
        Namespace[] actualScope = stack.getScope();

        Assert.assertArrayEquals(expectedScope, actualScope);
        Assert.assertEquals(1, stack.getDepth());
    }

    // Test isInScope method
    @Test
    public void testIsInScope() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", null));
        stack.push(new Element("child1", new Namespace("http://test1.com/")));
        stack.push(new Element("child2", new Namespace("http://test2.com/")));

        Namespace namespaceToTest = new Namespace("http://test1.com/");
        Assert.assertTrue(stack.isInScope(namespaceToTest));

        namespaceToTest = new Namespace("http://testOther.com/");
        Assert.assertFalse(stack.isInScope(namespaceToTest));
    }

    // Test addedForward and addedReverse methods
    @Test
    public void testAddedForwardAndReverse() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", null));
        stack.push(new Element("child1", new Namespace("http://test1.com/")));
        stack.push(new Element("child2", new Namespace("http://test2.com/")));

        Iterator<Namespace> iterator = stack.iterator();
        List<Namespace> expectedList = new ArrayList<>();
        expectedList.add(Namespace.XML_NAMESPACE);
        expectedList.add(new Namespace("http://test2.com/"));
        expectedList.add(new Namespace("http://test1.com/"));

        while (iterator.hasNext()) {
            Namespace namespace = iterator.next();
            expectedList.remove(namespace);
        }

        Assert.assertEquals(expectedList, stack.addedForward());
        Assert.assertEquals(expectedList, stack.addedReverse());
    }
}