import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root", Namespaces.getNamespace("http://test.com"));

        stack.push(element);
        Namespace[] expected = { Namespaces.getNamespace("http://test.com") };
        assertArrayEquals(expected, stack.getScope());

        stack.pop();
        assertArrayEquals(new Namespace[0], stack.getScope());
    }
}