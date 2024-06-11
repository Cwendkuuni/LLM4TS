import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer rootNodePointer;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");
        rootElement.addAttribute("xml:namespace", "http://test.com");

        rootNodePointer = new NodePointer(new Element("root"), null);
        iterator = new JDOMAttributeIterator(rootNodePointer, null);
    }

    @Test
    public void testNextAttribute() {
        Attribute firstAttribute = iterator.getNodePointer().getNode().getAttribute("attr1");
        Assert.assertNotNull(firstAttribute);
        iterator.next();
        Attribute secondAttribute = iterator.getNodePointer().getNode().getAttribute("attr2");
        Assert.assertNotNull(secondAttribute);
        iterator.next(); // no attribute, iterator should be finished
        Assert.assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testNextAttributeWithNamespace() {
        QName attributeName = new QName("xml", "xml:attr");
        iterator = new JDOMAttributeIterator(rootNodePointer, attributeName);
        Attribute xmlAttr = iterator.getNodePointer().getNode().getAttribute("xml:attr");
        Assert.assertNotNull(xmlAttr);
        iterator.next(); // no attribute, iterator should be finished
        Assert.assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testNextAttributeWithWildcardNamespace() {
        iterator = new JDOMAttributeIterator(rootNodePointer, null);
        Attribute firstAttribute = iterator.getNodePointer().getNode().getAttribute("attr1");
        Assert.assertNotNull(firstAttribute);
        iterator.next();
        Attribute secondAttribute = iterator.getNodePointer().getNode().getAttribute("attr2");
        Assert.assertNotNull(secondAttribute);
        iterator.next(); // no attribute, iterator should be finished
        Assert.assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testEmptyIterator() {
        Element rootElement = new Element("root");
        rootNodePointer = new NodePointer(rootElement, null);
        iterator = new JDOMAttributeIterator(rootNodePointer, null);
        Assert.assertNull(iterator.getNodePointer().getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParentNodePointer() {
        iterator = new JDOMAttributeIterator(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameQName() {
        iterator = new JDOMAttributeIterator(rootNodePointer, null);
    }
}