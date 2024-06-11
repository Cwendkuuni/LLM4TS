import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() throws JDOMException {
        Element emptyElement = new Element("root");
        NodePointer emptyParent = new NodePointer(emptyElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(emptyParent, null);

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSingleAttributeIterator() throws JDOMException {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String attrName = "class";

        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        Attribute attribute = new Attribute("class", "test", new Namespace(xmlns, prefix));
        element.addContent(attribute);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, attrName));

        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertEquals(attribute, attributePointer.getNode());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());

        iterator.next();
        Assert.assertFalse(iterator.hasPrevious());

        iterator.next();
        Assert.assertFalse(iterator.hasPrevious());

        Attribute nextAttribute = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertNull(nextAttribute);

        iterator.next();
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testMultipleAttributesIterator() throws JDOMException {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String attrName1 = "class";
        String attrName2 = "style";

        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        Attribute attr1 = new Attribute("class", "test1", new Namespace(xmlns, prefix));
        Attribute attr2 = new Attribute("style", "background-color: red", new Namespace(xmlns, prefix));
        element.addContent(attr1);
        element.addContent(attr2);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertEquals(attr1, attributePointer1.getNode());
        iterator.next();
        Assert.assertEquals(attr2, iterator.getNodePointer().getNode());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());

        iterator.next();
        Assert.assertFalse(iterator.hasPrevious());

        iterator.next();
        Attribute nextAttribute = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertNull(nextAttribute);

        iterator.next();
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNegativePosition() throws JDOMException {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String attrName = "class";

        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        Attribute attribute = new Attribute("class", "test", new Namespace(xmlns, prefix));
        element.addContent(attribute);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, attrName));
        iterator.next();

        Assert.assertFalse(iterator.hasPrevious());
        iterator.previous();
        Assert.assertFalse(iterator.getNodePointer() == null);
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testNoMatchingAttribute() throws JDOMException {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String attrName = "class";

        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        Attribute attribute = new Attribute("style", "background-color: red", new Namespace(xmlns, prefix));
        element.addContent(attribute);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, attrName));

        Assert.assertNull(iterator.getNodePointer());
    }
}