import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyAttributeIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithOneAttribute() {
        String xmlNs = Namespace.XML_NAMESPACE.getNamespaceURI();
        String prefix = "xml";
        String localName = "xml:version";
        String value = "1.0";

        Element root = new Element("root");
        Attribute attr = new Attribute(localName, value, new Namespace(prefix, xmlNs));
        root.setAttribute(attr);

        NodePointer parent = new JDOMNodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        Attribute expectedAttribute = attr;
        NodePointer expectedNodePointer = new JDOMAttributePointer(parent, expectedAttribute);

        assertEquals(expectedNodePointer, iterator.getNodePointer());
        // Next attribute should be null
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithMultipleAttributes() {
        String xmlNs = Namespace.XML_NAMESPACE.getNamespaceURI();
        String prefix = "xml";
        String localName1 = "xml:version";
        String value1 = "1.0";
        String localName2 = "xml:encoding";
        String value2 = "UTF-8";

        Element root = new Element("root");
        Attribute attr1 = new Attribute(localName1, value1, new Namespace(prefix, xmlNs));
        Attribute attr2 = new Attribute(localName2, value2, new Namespace(prefix, xmlNs));
        root.setAttribute(attr1);
        root.setAttribute(attr2);

        NodePointer parent = new JDOMNodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));

        List<NodePointer> expectedNodePointers = new ArrayList<>();
        expectedNodePointers.add(new JDOMAttributePointer(parent, attr1));
        expectedNodePointers.add(new JDOMAttributePointer(parent, attr2));

        for (int i = 0; i < expectedNodePointers.size(); ++i) {
            NodePointer expectedNodePointer = expectedNodePointers.get(i);
            NodePointer actualNodePointer = iterator.getNodePointer();
            assertSame(expectedNodePointer, actualNodePointer);
            iterator.next();
        }

        // Next node should be null
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithNoMatchingAttribute() {
        String localName = "nonExistent";

        Element root = new Element("root");

        NodePointer parent = new JDOMNodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("", localName));

        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeIteratorWithNullParent() {
        new JDOMAttributeIterator(null, new QName("", ""));
    }

    @Test
    public void testAttributeIteratorWithNullQName() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }
}