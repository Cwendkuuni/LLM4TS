import org.junit.Test;
import org.jdom2.util.Namespace;
import org.jdom2.util.NamespacesStack;

public class NamespaceStackTest {

    @Test
    public void testEmptyStack() {
        NamespacesStack stack = new NamespacesStack();
        assert (stack.isEmpty()) ;
    }

    @Test
    public void testBasicStack() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", "test"));
        Namespace expectedNamespace = new Namespace("test");
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE, expectedNamespace};
        assert ArrayEquals.arrayEquals(expectedScope, stack.getScope());
        assert (stack.isInScope(expectedNamespace));
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", "test1"));
        stack.push(new Element("root", "test2"));
        Namespace[] expectedNamespace1 = {Namespace.XML_NAMESPACE, new Namespace("test1")};
        Namespace[] expectedNamespace2 = {Namespace.XML_NAMESPACE, new Namespace("test1"), new Namespace("test2")};
        assert ArrayEquals.arrayEquals(expectedNamespace1, stack.getScope());
        assert (stack.isInScope(new Namespace("test1"))) ;
        assert (stack.isInScope(new Namespace("test2"))) ;
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespacesStack stack = new NamespacesStack();
        stack.pop();
    }

    @Test
    public void testPopSingleNamespace() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", "test"));
        stack.pop();
        assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
    }

    @Test
    public void testPopMultipleNamespaces() {
        NamespacesStack stack = new NamespacesStack();
        stack.push(new Element("root", "test1"));
        stack.push(new Element("root", "test2"));
        stack.pop();
        Namespace[] expectedNamespace1 = {Namespace.XML_NAMESPACE, new Namespace("test1")};
        assert ArrayEquals.arrayEquals(expectedNamespace1, stack.getScope());
    }
}