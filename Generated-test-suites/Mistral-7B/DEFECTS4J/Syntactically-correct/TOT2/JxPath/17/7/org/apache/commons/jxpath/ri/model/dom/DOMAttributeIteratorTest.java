import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIteratorInitialization() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = new QName("test:attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }
}