import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }
}