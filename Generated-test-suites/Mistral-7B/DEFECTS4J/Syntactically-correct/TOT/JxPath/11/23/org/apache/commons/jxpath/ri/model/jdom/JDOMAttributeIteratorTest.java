import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private Element root;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2", "xmlns:x");
        root.addAttribute("attr3", "value3");
        root.addAttribute("attr4", "value4", "xmlns:y");
    }

    @Test
    public void testEmptyIterator() {
        NodeIterator iterator = new JDOMAttributeIterator(null, null);
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testBasicUsage() {
        iterator = new JDOMAttributeIterator(new NodePointer(root), null);
        NodePointer attribute1 = iterator.getNodePointer();
        NodePointer attribute2 = iterator.getNodePointer();
        NodePointer attribute3 = iterator.getNodePointer();
        NodePointer attribute4 = iterator.getNodePointer();

        Assert.assertEquals("attr1", attribute1.getNode().getName());
        Assert.assertEquals("value1", attribute1.getNode().getValue());
        Assert.assertEquals(1, iterator.getPosition());

        Assert.assertEquals("attr2", attribute2.getNode().getName());
        Assert.assertEquals("value2", attribute2.getNode().getValue());
        Assert.assertEquals(1, iterator.getPosition());

        Assert.assertEquals("attr3", attribute3.getNode().getName());
        Assert.assertEquals("value3", attribute3.getNode().getValue());
        Assert.assertEquals(2, iterator.getPosition());

        Assert.assertEquals("attr4", attribute4.getNode().getName());
        Assert.assertEquals("value4", attribute4.getNode().getValue());
        Assert.assertEquals(3, iterator.getPosition());

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testEmptyAttributeList() {
        iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("*", "xml"));
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidPosition() {
        iterator = new JDOMAttributeIterator(new NodePointer(root), null);
        iterator.setPosition(5);
    }

    @Test
    public void testNamespace() {
        iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("x:attr2", "xml"));
        NodePointer attribute2 = iterator.getNodePointer();

        Assert.assertEquals("attr2", attribute2.getNode().getName());
        Assert.assertEquals("value2", attribute2.getNode().getValue());
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParentPointer() {
        iterator = new JDOMAttributeIterator(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameQName() {
        iterator = new JDOMAttributeIterator(new NodePointer(root), null);
    }
}