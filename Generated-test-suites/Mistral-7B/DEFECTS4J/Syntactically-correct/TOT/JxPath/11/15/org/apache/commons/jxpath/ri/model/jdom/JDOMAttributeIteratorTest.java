import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyAttributeIterator() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
        assertFalse("Iterator should be empty", iterator.hasNext());
        assertNull("No next element", iterator.next());
    }

    @Test
    public void testSingleAttributeIterator() {
        String xmlns = "http://www.w3.org/XML/1998/namespace";
        String namespace = "test";
        String prefix = "t";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns);
        root.addNamespaceDeclaration(new Namespace(namespace, prefix));
        root.addAttribute(new Attribute("id", "1", new QName(namespace, "t:name")));

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(namespace, "t:name"));

        assertNotNull("Iterator should have next element", iterator.next());
        Attribute attribute = (Attribute) iterator.next().getNode();
        assertEquals("Attribute name should be 'name'", "name", attribute.getName());
        assertEquals("Attribute value should be '1'", "1", attribute.getText());
        assertFalse("Iterator should not have next element", iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() {
        String xmlns = "http://www.w3.org/XML/1998/namespace";
        String namespace = "test";
        String prefix = "t";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns);
        root.addNamespaceDeclaration(new Namespace(namespace, prefix));
        root.addAttribute(new Attribute("id", "1", new QName(namespace, "t:name")));
        root.addAttribute(new Attribute("class", "test", new QName(namespace, "t:class")));

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(namespace, "t:*"));

        assertNotNull("Iterator should have next element", iterator.next().getNode());
        assertNotNull("Iterator should have next element", iterator.next().getNode());
        assertFalse("Iterator should not have next element", iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributeIterator() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        new JDOMAttributeIterator(nodePointer, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNamespaceAttributeIterator() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        new JDOMAttributeIterator(nodePointer, new QName(null, "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParentIterator() {
        new JDOMAttributeIterator(null, new QName("", "name"));
    }
}