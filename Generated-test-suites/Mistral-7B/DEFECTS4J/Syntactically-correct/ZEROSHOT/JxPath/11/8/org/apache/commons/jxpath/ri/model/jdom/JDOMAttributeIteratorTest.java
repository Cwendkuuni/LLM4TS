import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for JDOMAttributeIterator
 */
public class JDOMAttributeIteratorTest {

    /**
     * Test constructor with a specific attribute name
     */
    @Test
    public void testConstructorWithSpecificAttributeName() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr1", nodePointer.getNodeName().getName());
        Assert.assertEquals("value1", nodePointer.getNodeName().getValue());
        Assert.assertEquals(nodePointer.getNode().getName(), "root");

        iterator.setPosition(2); // should throw an exception
        try {
            nodePointer = iterator.getNodePointer();
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        iterator.setPosition(0);
        nodePointer = iterator.getNodePointer();
        Assert.assertEquals("value1", nodePointer.getNodeValue());

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        // not supported
        Assert.assertNull(nodePointer);
    }

    /**
     * Test constructor with an asterisk (*) as attribute name
     */
    @Test
    public void testConstructorWithAsteriskAttributeName() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        int position = 0;
        NodePointer nodePointer;
        while ((nodePointer = iterator.getNodePointer()) != null) {
            switch (position++) {
                case 0:
                    Assert.assertEquals("attr1", nodePointer.getNodeName().getName());
                    Assert.assertEquals("value1", nodePointer.getNodeValue());
                    break;
                case 1:
                    Assert.assertEquals("attr2", nodePointer.getNodeName().getName());
                    Assert.assertEquals("value2", nodePointer.getNodeValue());
                    break;
                default:
                    Assert.fail();
            }
            iterator.setPosition(1 + position);
        }
        Assert.assertNull(iterator.getNodePointer());
    }

    /**
     * Test empty case
     */
    @Test
    public void testEmptyCase() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator.getNodePointer());

        iterator.setPosition(1);
        Assert.assertNull(iterator.getNodePointer());
    }
}