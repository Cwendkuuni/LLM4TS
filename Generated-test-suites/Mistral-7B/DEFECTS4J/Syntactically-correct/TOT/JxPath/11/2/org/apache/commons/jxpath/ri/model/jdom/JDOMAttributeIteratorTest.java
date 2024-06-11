import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSingleAttributeIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer1);
        Assert.assertEquals("value1", attributePointer1.getNode().getValue());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer2);
        Assert.assertEquals("value2", attributePointer2.getNode().getValue());
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithNamespacePrefix() {
        Element root = new Element("root");
        root.addNamespaceDeclaration("x", "http://example.com/");
        root.addAttribute("x:attr1", "value1");
        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("x", "attr1"));
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithWrongParentNode() {
        Element root = new Element("root");
        NodePointer parent = new JDOMPointer(new Element("parent"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithWrongAttributeName() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr2"));
    }

    @Test
    public void testIteratorWithEmptyAttributeList() {
        Element root = new Element("root");
        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIteratorWithNullParentNode() {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIteratorWithNullAttributeName() {
        Element root = new Element("root");
        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }
}