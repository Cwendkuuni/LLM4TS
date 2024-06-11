import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        // Test with a valid NodePointer and QName
        Assert.assertNotNull(iterator);
        Assert.assertFalse(iterator.isDone());

        // Test with an empty list of attributes
        NodePointer emptyNodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(emptyNodePointer, new QName("", ""));
        Assert.assertTrue(emptyIterator.isDone());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) iterator.getNodePointer();
        JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) iterator.getNodePointer();
        JDOMAttributePointer attributePointer3 = (JDOMAttributePointer) iterator.getNodePointer();

        // Test if the correct attributes are returned for each position
        Assert.assertEquals(attributePointer1.getAttribute().getName(), "attributeName");
        Assert.assertEquals(attributePointer2.getAttribute().getName(), "attributeName");
        Assert.assertEquals(attributePointer3.getAttribute().getName(), "attributeName"); // This should be the same attribute as position 1

        // Test if the iterator returns null after reaching the last position
        iterator.setPosition(iterator.getPosition() + 1);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPositionAndSetPosition() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        // Test if the iterator starts at position 0
        Assert.assertEquals(iterator.getPosition(), 0);

        // Test if the iterator can be moved to any valid position
        iterator.setPosition(2);
        Assert.assertEquals(iterator.getPosition(), 2);

        // Test if the iterator returns null when setting an invalid position
        iterator.setPosition(-1);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testEmptyNodePointer() {
        NodePointer nodePointer = new NodePointer(null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        Assert.assertFalse(iterator.isDone());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNullAttribute() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        iterator.setPosition(1);
        iterator.setAttributeList(Collections.emptyList());

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        // Test if the iterator can be moved to the first position
        iterator.setPosition(1);
        Assert.assertNotNull(iterator.getNodePointer());

        // Test if the iterator can be moved to the last position
        iterator.setPosition(iterator.getPosition() + iterator.size() - 1);
        Assert.assertNotNull(iterator.getNodePointer());

        // Test if the iterator can be moved to any other valid position
        iterator.setPosition(iterator.getPosition() + 1);
        Assert.assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorSize() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        iterator.setPosition(iterator.getPosition() + iterator.size());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorPositionAfterSize() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        iterator.setPosition(iterator.getPosition() + iterator.size() + 1);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorPositionNegative() {
        NodePointer nodePointer = new NodePointer(new Element("root"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attributeName"));

        iterator.setPosition(-1);
        Assert.assertNull(iterator.getNodePointer());
    }
}