import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMPointerMock(); // mock parent NodePointer
        QName name = new QName("", "", ""); // empty QName
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value");
        element.addContent(attribute);
        NodePointer parent = new JDOMPointerMock(element); // mock parent NodePointer
        QName name = new QName("", "name"); // QName for attribute "name"
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        NodePointer attributePointer = iterator.next();
        Assert.assertEquals(attribute, attributePointer.getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1");
        Attribute attribute2 = new Attribute("name2", "value2");
        Attribute attribute3 = new Attribute("name3", "value3");
        element.addContent(attribute1);
        element.addContent(attribute2);
        element.addContent(attribute3);
        NodePointer parent = new JDOMPointerMock(element); // mock parent NodePointer
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*")); // wildcard QName
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        NodePointer attributePointer1 = iterator.next();
        Assert.assertEquals(attribute1, attributePointer1.getNode());
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        NodePointer attributePointer2 = iterator.next();
        Assert.assertEquals(attribute2, attributePointer2.getNode());
        Assert.assertEquals(3, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        NodePointer attributePointer3 = iterator.next();
        Assert.assertEquals(attribute3, attributePointer3.getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespace() {
        Element element = new Element("root", new QName("http://test.com", "root"));
        Attribute attribute = new Attribute("prefix:name", "value");
        attribute.setNamespace(new Namespace("prefix", "http://test.com"));
        element.addContent(attribute);
        NodePointer parent = new JDOMPointerMock(element); // mock parent NodePointer
        QName name = new QName("prefix", "name"); // QName for attribute "name" with prefix "prefix"
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        NodePointer attributePointer = iterator.next();
        Assert.assertEquals(attribute, attributePointer.getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithInvalidParent() {
        Integer number = 1; // not a NodePointer
        QName name = new QName("", "*"); // wildcard QName
        NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointerMock(number), name);
        iterator.next(); // this line will throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithInvalidQName() {
        Element element = new Element("root");
        NodePointer parent = new JDOMPointerMock(element); // mock parent NodePointer
        NodeIterator iterator = new JDOMAttributeIterator(parent, null); // null QName
        iterator.next(); // this line will throw an IllegalArgumentException
    }
}