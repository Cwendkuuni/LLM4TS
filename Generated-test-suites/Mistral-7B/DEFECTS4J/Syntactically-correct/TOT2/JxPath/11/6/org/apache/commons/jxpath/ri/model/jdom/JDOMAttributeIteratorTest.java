import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = new QName("test:name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);

        Element element = new Element("testElement");
        parent = new NodePointer(element);
        name = new QName("xml:id");
        iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);

        parent = new NodePointer(new Element("testElement"));
        name = new QName("*");
        iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);

        parent = new NodePointer(null);
        name = new QName("test:name");
        iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator);

        parent = new NodePointer(new Element("testElement"));
        name = new QName("unknown:unknown");
        iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }
}