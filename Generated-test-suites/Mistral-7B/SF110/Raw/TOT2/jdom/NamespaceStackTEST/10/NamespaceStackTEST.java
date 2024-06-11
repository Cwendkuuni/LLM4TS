import org.jdom2.Namespace;
import org.jdom2.util.Namespaces;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class NamespaceStackTest {
    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = new Namespace("testPrefix", "testURI");
        stack.push(new Element("root", namespace));

        Namespace[] expected = {namespace};
        Namespace[] actual = stack.getScope();

        assertArrayEquals(expected, actual);
    }
}