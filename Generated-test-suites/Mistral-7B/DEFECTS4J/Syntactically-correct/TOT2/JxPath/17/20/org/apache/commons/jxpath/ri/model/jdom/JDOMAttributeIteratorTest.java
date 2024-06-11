import org.junit.Test;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_validArgument() throws JDOMException {
        NodePointer parent = new NodePointer(new Element("parent"), null);
        QName name = new QName("test", "xml", "http://test.com");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.attributes != null);
    }

    @Test
    public void testConstructor_invalidArgument() {
        NodePointer parent = new NodePointer(null, null);
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertFalse(iterator.attributes != null);
    }
}