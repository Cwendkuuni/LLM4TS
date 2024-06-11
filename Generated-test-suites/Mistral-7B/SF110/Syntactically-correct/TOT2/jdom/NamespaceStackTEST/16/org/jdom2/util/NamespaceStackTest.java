import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = new Namespace("test");
        stack.push(new Element("root", namespace));
        Namespace[] expected = {namespace, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, stack.getScope());
        assertEquals(2, stack.depth);
    }
}