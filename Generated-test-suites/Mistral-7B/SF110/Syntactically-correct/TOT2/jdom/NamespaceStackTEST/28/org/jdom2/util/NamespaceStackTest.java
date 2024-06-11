import org.jdom2.*;
import org.jdom2.util.Namespaces;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {
    @Test
    public void testPushElement() {
        NamespaceStack namespacestack = new NamespaceStack();
        Element element = new Element("root", "root");
        Namespace namespace = new Namespace("test", "http://example.com/test");
        element.setNamespace(namespace);
        Namespace expectedNamespace = new Namespace("test", "http://example.com/test");
        namespacestack.push(element);
        Namespace[] stack = namespacestack.getScope();
        Namespace actualNamespace = stack[stack.length - 1];
        assertEquals(expectedNamespace, actualNamespace);
    }
}