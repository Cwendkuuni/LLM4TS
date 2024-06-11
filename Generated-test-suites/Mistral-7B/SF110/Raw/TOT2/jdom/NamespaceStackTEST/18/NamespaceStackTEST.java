import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Namespace mns1 = new Namespace("http://example.com");
        stack.push(new Element("root", null, null));
        Namespace[] expectedScope1 = new Namespace[]{Namespace.XML_NAMESPACE, mns1};

        assertEquals(1, stack.depth);
        assertArrayEquals(expectedScope1, stack.getScope());

        stack.push(new Element("element", null, null));
        Namespace[] expectedScope2 = new Namespace[]{Namespace.XML_NAMESPACE, mns1};
        assertEquals(1, stack.depth);
        assertArrayEquals(expectedScope2, stack.getScope());

        stack.push(new Element("root", null, null)); // should not add new prefix since it's already on the stack
        assertEquals(1, stack.depth);
        assertArrayEquals(expectedScope1, stack.getScope());

        stack.push(new Attribute("attr", null, null, "http://example.com"));
        Namespace[] expectedScope3 = new Namespace[]{Namespace.XML_NAMESPACE, mns1};
        assertEquals(1, stack.depth);
        assertArrayEquals(expectedScope3, stack.getScope());

        stack.push(new Attribute("attr2", null, null, "http://example.com")); // should not add new namespace since it's already on the stack
        assertEquals(1, stack.depth);
        assertArrayEquals(expectedScope3, stack.getScope());
    }
}