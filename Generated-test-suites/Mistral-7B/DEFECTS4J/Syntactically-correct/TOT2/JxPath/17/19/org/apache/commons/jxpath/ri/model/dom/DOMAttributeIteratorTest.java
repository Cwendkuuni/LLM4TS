import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstructionWithElementAndQName() {
        Node root = new Element("root"); // create an element for testing
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(root), new QName("test"));
        // Check if the constructor was called correctly
        Assert.assertEquals(root, iterator.parent.getNode());
        Assert.assertEquals("test", iterator.name.getName());
        // TODO: check attributes list is empty
    }
}