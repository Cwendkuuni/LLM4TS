import org.jdom2.Namespace;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = new Namespace("test");
        stack.push(new Element() {
            Namespace getNamespace() {
                return namespace;
            }
        });
        Namespace[] expected = {namespace};
        Namespace[] scope = stack.getScope();
        assertNotNull(scope);
        assertEquals(expected, scope);
    }

    @Test
    public void testPushWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element() {
            Namespace getNamespace() {
                return new Namespace("emptyStackTest");
            }
        });
        Namespace[] expected = {new Namespace("emptyStackTest")};
        Namespace[] scope = stack.getScope();
        assertEquals(expected, scope);
    }
}