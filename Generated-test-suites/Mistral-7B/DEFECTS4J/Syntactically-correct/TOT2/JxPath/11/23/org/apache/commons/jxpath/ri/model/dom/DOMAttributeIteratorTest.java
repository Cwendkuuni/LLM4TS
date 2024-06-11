import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_ElementWithSingleAttribute() {
        NodePointer parentNode = new NodePointer(new Element("root"));
        QName name = new QName("test:attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, name);
        Attr expectedAttribute = new Attr("test", "testValue");
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals(expectedAttribute, attribute);
    }

    // Edge cases and exception handling scenarios can be tested in other methods
}