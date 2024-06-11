import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIteratorConstruction() {
        NodePointer parent = new NodePointer(new DOMNode());
        QName name = new QName("test:name");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }
}