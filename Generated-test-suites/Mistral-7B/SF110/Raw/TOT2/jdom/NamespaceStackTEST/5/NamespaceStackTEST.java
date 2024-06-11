import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("root");
        Namespace namespace = new Namespace("test");
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE, namespace};

        stack.push(element);
        Namespace[] actualScope = stack.getScope();

        assertArrayEquals(expectedScope, actualScope);
    }
}