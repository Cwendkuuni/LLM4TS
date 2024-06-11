import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.JDOMNamespaceContext;
import org.apache.commons.jxpath.ri.model.JDOMNode;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointerFactory;
import org.apache.commons.jxpath.ri.model.NodePointerFactoryImpl;
import org.apache.commons.jxpath.ri.model.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyAttributeIterator() {
        NodePointer parentPointer = new JDOMNode(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithNamespace() {
        NodePointer parentPointer = new JDOMNode(new Element("root", new Namespace("ns", "http://test.com")));
        parentPointer.getNamespaceContext().declareNamespace("ns", "http://test.com");
        Element rootElement = (Element) parentPointer.getNode();
        rootElement.addAttribute("attr1", "value1", "ns");
        rootElement.addAttribute("attr2", "value2", "ns");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("ns", "attr"));
        Assert.assertEquals(2, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr1", nodePointer.getName().getName());
        Assert.assertEquals("value1", nodePointer.getValue().getValue());
        Assert.assertEquals(nodePointer, iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());
        nodePointer = iterator.getNodePointer();
        Assert.assertEquals("attr2", nodePointer.getName().getName());
        Assert.assertEquals("value2", nodePointer.getValue().getValue());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithWildcard() {
        NodePointer parentPointer = new JDOMNode(new Element("root"));
        parentPointer.getNamespaceContext().declareNamespace("test", "http://test.com");
        Element rootElement = (Element) parentPointer.getNode();
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("test:attr2", "value2");
        rootElement.addAttribute("test:attr3", "value3");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "*"));
        Assert.assertEquals(3, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr1", nodePointer.getName().getName());
        Assert.assertEquals("value1", nodePointer.getValue().getValue());
        Assert.assertEquals(2, iterator.getPosition());
        nodePointer = iterator.getNodePointer();
        Assert.assertEquals("test:attr2", nodePointer.getName().getName());
        Assert.assertEquals("value2", nodePointer.getValue().getValue());
        Assert.assertEquals(1, iterator.getPosition());
        nodePointer = iterator.getNodePointer();
        Assert.assertEquals("test:attr3", nodePointer.getName().getName());
        Assert.assertEquals("value3", nodePointer.getValue().getValue());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithEmptyList() {
        NodePointer parentPointer = new JDOMNode(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithNullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", ""));
        Assert.assertNull(iterator);
    }

    @Test
    public void testAttributeIteratorWithNullQName() {
        NodePointer parentPointer = new JDOMNode(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, null);
        Assert.assertNull(iterator);
    }

    private static class JDOMNode extends JDOMNodeSupport implements NodePointer {
        public JDOMNode(Node node) {
            super(node);
        }

        @Override
        public NodePointer getParent() {
            return null;
        }

        @Override
        public NodeIterator getNodeIterator(QName name) {
            return new JDOMAttributeIterator(this, name);
        }
    }

    private static class JDOMNodeSupport implements NodePointer {
        private Node node;

        public JDOMNodeSupport(Node node) {
            this.node = node;
        }

        @Override
        public Node getNode() {
            return node;
        }

        @Override
        public String getValue() {
            return "";
        }

        @Override
        public String getValue(String encoding) {
            return "";
        }

        @Override
        public NodePointer getParent() {
            return null;
        }

        @Override
        public NodeIterator getNodeIterator(QName name) {
            return null;
        }

        @Override
        public NodePointer getAttribute(String localName, String namespaceURI) {
            return null;
        }

        @Override
        public NodePointer getAttribute(QName name) {
            return null;
        }

        @Override
        public NodePointer getFirstChild() {
            return null;
        }

        @Override
        public NodePointer getLastChild() {
            return null;
        }

        @Override
        public NodePointer getNextSibling() {
            return null;
        }

        @Override
        public NodePointer getPreviousSibling() {
            return null;
        }

        @Override
        public String getNamespaceURI() {
            return "";
        }

        @Override
        public String getNamespaceURI(String prefix) {
            return "";
        }

        @Override
        public String getLocalName() {
            return "";
        }

        @Override
        public String getLocalName(String namespaceURI) {
            return "";
        }

        @Override
        public String getPrefix() {
            return "";
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public String getText(String encoding) {
            return "";
        }

        @Override
        public NodePointer addAttribute(String namespaceURI, String localName, String value) {
            return null;
        }

        @Override
        public NodePointer addAttribute(String localName, String value) {
            return null;
        }

        @Override
        public NodePointer addElement(String namespaceURI, String qualifiedName, Node parent) {
            return null;
        }

        @Override
        public NodePointer addElement(String localName, Node parent) {
            return null;
        }

        @Override
        public NodePointer addChildren(Node children) {
            return null;
        }

        @Override
        public NodePointer addContent(Node content) {
            return null;
        }

        @Override
        public void detach() {
        }

        @Override
        public void detachDeep() {
        }
    }

    @Test
    public void testJDOMAttributePointer() {
        NodePointer parentPointer = new JDOMNode(new Element("root"));
        Element rootElement = (Element) parentPointer.getNode();
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "attr"));
        NodePointer nodePointer = iterator.getNodePointer();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals("attr1", attributePointer.getLocalName());
        Assert.assertEquals("value1", attributePointer.getValue().getValue());
        Assert.assertSame(iterator, attributePointer.getIterator());

        nodePointer = iterator.getNodePointer();
        attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals("attr2", attributePointer.getLocalName());
        Assert.assertEquals("value2", attributePointer.getValue().getValue());
        Assert.assertSame(iterator, attributePointer.getIterator());
    }

    @Test
    public void testCreateNodePointerFactory() {
        NodePointerFactory factory = new NodePointerFactoryImpl();
        Assert.assertNotNull(factory);
        Assert.assertNotNull(factory.createNodePointer(new Element("root")));
    }
}