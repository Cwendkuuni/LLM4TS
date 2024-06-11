import org.junit.Test;
import org.jdom2.util.Namespace;
import org.jdom2.util.NamespaceStack;

public class NamespaceStackTest {
    // Test constructor
    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
        assertEquals(NamespaceStack.EMPTY, stack.getScope());
        assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
        assertEquals(0, stack.depth());
    }

    // Test push method with empty stack
    @Test
    public void testPushWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = new Namespace("test", "http://test.com");
        stack.push(namespace);
        assertEquals(new Namespace[] {namespace}, stack.getScope());
        assertEquals(new ArrayList<Namespace>() {{add(namespace);}}, stack.addedForward());
        assertEquals(new ArrayList<Namespace>() {{add(namespace);}}, stack.addedReverse());
        assertEquals(1, stack.depth());
    }

    // Test push method with non-empty stack
    @Test
    public void testPushWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE});
        Namespace namespace = new Namespace("test", "http://test.com");
        stack.push(namespace);
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE, namespace};
        assertEquals(expectedScope, stack.getScope());
        assertEquals(new ArrayList<Namespace>() {{add(namespace);}}, stack.addedForward());
        assertEquals(new ArrayList<Namespace>() {{add(namespace);}}, stack.addedReverse());
        assertEquals(2, stack.depth());
    }

    // Test push method with multiple namespaces
    @Test
    public void testPushWithMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        stack.push(element);
        Namespace namespace1 = new Namespace("test1", "http://test1.com");
        stack.push(namespace1);
        Attribute attribute = new Attribute("attr", "value", namespace1);
        stack.push(attribute);
        assertEquals(new Namespace[] {namespace1, Namespace.XML_NAMESPACE, Namespace.NO_NAMESPACE}, stack.getScope());
        assertEquals(new ArrayList<Namespace>() {{add(namespace1); add(Namespace.XML_NAMESPACE); add(Namespace.NO_NAMESPACE);}}, stack.addedForward());
        assertEquals(new ArrayList<Namespace>() {{add(Namespace.NO_NAMESPACE); add(Namespace.XML_NAMESPACE); add(namespace1);}}, stack.addedReverse());
        assertEquals(4, stack.depth());
    }

    // Test pop method with empty stack
    @Test(expected = IllegalStateException.class)
    public void testPopWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    // Test pop method with non-empty stack
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE});
        stack.push(new Namespace("test", "http://test.com"));
        stack.pop();
        assertEquals(NamespaceStack.EMPTY, stack.getScope());
        assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        assertEquals(NamespaceStack.EMPTYITER, stack.addedReverse());
        assertEquals(0, stack.depth());
    }

    // Test isInScope method with empty stack
    @Test
    public void testIsInScopeWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = new Namespace("test", "http://test.com");
        assertFalse(stack.isInScope(namespace));
    }

    // Test isInScope method with non-empty stack and found namespace
    @Test
    public void testIsInScopeWithFoundNamespace() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("test", "http://test.com")});
        Namespace namespace = new Namespace("test", "http://test.com");
        assertTrue(stack.isInScope(namespace));
    }

    // Test isInScope method with non-empty stack and not found namespace
    @Test
    public void testIsInScopeWithNotFoundNamespace() {
        NamespaceStack stack = new NamespaceStack(new Namespace[] {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("test", "http://test.com")});
        Namespace namespace = new Namespace("notTest", "http://test.com");
        assertFalse(stack.isInScope(namespace));
    }
}