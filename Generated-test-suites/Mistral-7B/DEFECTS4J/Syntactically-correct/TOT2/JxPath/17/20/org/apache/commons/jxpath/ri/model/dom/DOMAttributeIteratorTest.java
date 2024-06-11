import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstructor() {
        Node root = createRootElement();
        NodePointer rootPointer = new NodePointer(root);
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, name);
        Assert.assertNotNull(iterator);
        Assert.assertNotNull(iterator.getNodePointer());
    }

    private Node createRootElement() {
        Element root = document.createElement("root");
        root.setAttributeNode(document.createAttribute("test1"));
        root.setAttributeNode(document.createAttribute("test2"));
        document.appendChild(root);
        return root;
    }
}