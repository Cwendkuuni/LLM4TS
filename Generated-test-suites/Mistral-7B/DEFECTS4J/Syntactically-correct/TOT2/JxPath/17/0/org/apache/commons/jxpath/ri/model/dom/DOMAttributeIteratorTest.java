import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for DOMAttributeIterator constructor.
 */
public class DOMAttributeIteratorConstructorTest {
    @Test
    public void testDOMAttributeIteratorConstruction() {
        NodePointer parentPointer = new NodePointer(null, null, null, null, null);
        QName name = new QName("", "", "test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        Assert.assertNotNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorWithElementNode() {
        NodePointer parentPointer = new NodePointer(new Element("root"), null, null, null, null);
        QName name = new QName("", "", "test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithWildcardQName() {
        NodePointer parentPointer = new NodePointer(new Element("root"), null, null, null, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("", "*", ""));

        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertNotNull(iterator.getNodePointer().getNode());
    }
}