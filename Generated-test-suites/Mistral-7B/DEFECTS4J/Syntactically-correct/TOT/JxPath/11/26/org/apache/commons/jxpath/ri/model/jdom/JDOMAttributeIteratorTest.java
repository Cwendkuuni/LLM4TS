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

    private NodePointer root;
    private Element rootElement;

    @Before
    public void setUp() {
        String xml = "<root xmlns:x='http://test'><child attr1='value1' attr2='value2' /></root>";
        rootElement = JdomUtils.stringToElement(xml);
        root = new NodePointer(rootElement);
    }

    @Test
    public void testBasicUsage() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
        iterator.setPosition(1);
        attributePointer = iterator.getNodePointer();
        Assert.assertEquals("value2", attributePointer.getNode().getValue());
        iterator.setPosition(0);
        attributePointer = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyList() {
        QName name = new QName("*", null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.getNodePointer();
    }

    @Test
    public void testNullName() {
        QName name = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.getNodePointer();
    }

    @Test
    public void testOutOfBounds() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(10);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNull(attributePointer);
    }

    @Test
    public void testSetPositionToZero() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(1);
        iterator.setPosition(0);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
    }

    @Test
    public void testSetPositionToLast() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(2);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertEquals("value2", attributePointer.getNode().getValue());
    }

    @Test
    public void testSetPositionToFirst() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(0);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
    }

    @Test
    public void testSetPositionToMiddle() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(1);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertEquals("value2", attributePointer.getNode().getValue());
    }

    @Test
    public void testSetPositionToInvalidPosition() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(-1);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNull(attributePointer);
    }

    @Test
    public void testSetPositionToLastAndNext() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(2);
        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertEquals("value2", attributePointer1.getNode().getValue());
        iterator.setPosition(1);
        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer2.getNode().getValue());
    }

    @Test
    public void testSetPositionToZeroAndNext() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(0);
        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer1.getNode().getValue());
        iterator.setPosition(1);
        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertEquals("value2", attributePointer2.getNode().getValue());
    }

    @Test
    public void testSetPositionToLastAndPrevious() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(2);
        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertEquals("value2", attributePointer1.getNode().getValue());
        iterator.setPosition(1);
        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer2.getNode().getValue());
        iterator.setPosition(0);
        NodePointer attributePointer3 = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer3.getNode().getValue());
    }

    @Test
    public void testSetPositionToZeroAndPrevious() {
        QName name = new QName("*", "x");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, name);
        iterator.setPosition(0);
        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer1.getNode().getValue());
        iterator.setPosition(-1);
        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertNull(attributePointer2);
    }
}