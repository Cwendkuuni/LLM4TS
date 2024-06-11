import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link DOMAttributeIterator}.
 */
public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testNewIteratorWithEmptyElement() {
        NodePointer parent = new DOMPointers.DOMElementPointer(createElement("root"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNewIteratorWithSingleAttributeElement() {
        NodePointer parent = new DOMPointers.DOMElementPointer(createElement("root", "data", "1"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("data"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next, new DOMPointers.DOMAttributePointer(parent, getAttribute(parent.getNode(), new QName("data"))));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNewIteratorWithMultipleAttributesElement() {
        Element element = createElement("root", "a", "1", "b", "2");
        NodePointer parent = new DOMPointers.DOMElementPointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next, new DOMPointers.DOMAttributePointer(parent, getAttribute(element, new QName("a"))));
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next, new DOMPointers.DOMAttributePointer(parent, getAttribute(element, new QName("b"))));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNewIteratorWithFilteredAttributesElement() {
        Element element = createElement("root", "xmlns:xlink", null, "a", "1", "b", "2");
        NodePointer parent = new DOMPointers.DOMElementPointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xlink:href"));
        assertFalse(iterator.hasNext());
        iterator = new DOMAttributeIterator(parent, new QName("a"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next, new DOMPointers.DOMAttributePointer(parent, getAttribute(element, new QName("a"))));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNewIteratorWithFilteredAttributesElementNS() {
        Element element = createElementNS("http://example.com/ns", "root", null, "xmlns:xlink", null, "a", "1", "b", "2");
        NodePointer parent = new DOMPointers.DOMElementPointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("{http://example.com/ns}a"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next, new DOMPointers.DOMAttributePointer(parent, getAttributeNS(element, "{http://example.com/ns}a")));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testGetPositionWhenNotMoving() {
        NodePointer parent = new DOMPointers.DOMElementPointer(createElement("root"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));
        assertEquals(0, iterator.getPosition());
    }

    @org.junit.Test
    public void testGetPositionWhenMovingNext() {
        NodePointer parent = new DOMPointers.DOMElementPointer(createElement("root", "a", "1"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("a"));
        assertEquals(0, iterator.getPosition());
        NodePointer next = iterator.next();
        assertEquals(1, iterator.getPosition());
    }

    @org.junit.Test
    public void testSetPositionWhenMovingBack() {
        NodePointer parent = new DOMPointers.DOMElementPointer(createElement("root", "a", "1", "b", "2"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));
        iterator.next();
        iterator.next();
        assertTrue(iterator.setPosition(1));
        NodePointer next = iterator.next();
        assertSame(next, new DOMPointers.DOMAttributePointer(parent, getAttribute(parent.getNode(), new QName("a"))));
    }

    @org.junit.Test
    public void testSetPositionWhenOutOfBounds() {
        NodePointer parent = new DOMPointers.DOMElementPointer(createElement("root", "a", "1", "b", "2"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));
        assertFalse(iterator.setPosition(10));
    }

    private static Element createElement(String tagName, String... attributeNamesAndValues) {
        Element element = document.createElement(tagName);
        for (String attrNameValue : attributeNamesAndValues) {
            if (attrNameValue.contains("=")) {
                String[] attrNameValueParts = attrNameValue.split("=");
                Attr attr = document.createAttribute(attrNameValueParts[0]);
                attr.setValue(attrNameValueParts[1]);
                element. setAttributeNode(attr);
            }
        }
        return element;
    }

    private static Element createElementNS(String namespaceURI, String tagName, String... attributeNamesAndValues) {
        Element element = document.createProcessingInstruction(TAG, String.format("xmlns:%s \"%s\"", "prefix", namespaceURI));
        document.createElementNS(namespaceURI, tagName);
        for (String attrNameValue : attributeNamesAndValues) {
            if (attrNameValue.contains("=")) {
                String[] attrNameValueParts = attrNameValue.split("\\s+([:]|=)");
                String prefix = attrNameValueParts[0];
                String localName = attrNameValueParts[1];
                String namespaceURI = attrNameValueParts[2];
                Attr attr = document.createAttributeNS(namespaceURI, String.format("%s:%s", prefix, localName));
                attr.setValue(attrNameValue.substring(attrNameValue.indexOf("=") + 1));
                element. setAttributeNodeNS(attr);
            }
        }
        return element;
    }

    private static Attr getAttribute(Node node, QName name) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        if (name.getName().equals("*")) {
            NamedNodeMap map = node.getAttributes();
            int count = map.getLength();
            for (int i = 0; i < count; i++) {
                Attr attr = (Attr) map.item(i);
                if (testAttr(attr)) {
                    return attr;
                }
            }
            return null;
        }
        Attr attr = ((Element) node).getAttributeNodeNS(name.getNamespaceURI(), name.getName());
        return attr;
    }

    private static Attr getAttributeNS(Node node, String namespaceURI) {
        NamedNodeMap map = node.getAttributes();
        int count = map.getLength();
        for (int i = 0; i < count; i++) {
            Attr attr = (Attr) map.item(i);
            if (attr.getNamespaceURI().equals(namespaceURI)) {
                return attr;
            }
        }
        return null;
    }

    private static boolean testAttr(Attr attr) {
        String nodePrefix = DOMNodePointer.getPrefix(attr);
        String nodeLocalName = DOMNodePointer.getLocalName(attr);

        if (nodePrefix != null && nodePrefix.equals("xmlns")) {
            return false;
        }

        if (nodePrefix == null && nodeLocalName.equals("xmlns")) {
            return false;
        }

        String testLocalName = name.getName();
        if (testLocalName.equals("*") || testLocalName.equals(nodeLocalName)) {
            String testPrefix = name.getPrefix();

            if (equalStrings(testPrefix, nodePrefix)) {
                return true;
            }
            String testNS = null;
            if (testPrefix != null) {
                testNS = parent.getNamespaceURI(testPrefix);
            }
            String nodeNS = null;
            if (nodePrefix != null) {
                nodeNS = parent.getNamespaceURI(nodePrefix);
            }
            return equalStrings(testNS, nodeNS);
        }
        return false;
    }

    private static boolean equalStrings(String s1, String s2) {
        return s1 == s2 || s1 != null && s1.equals(s2);
    }

    private static Node document;

    @org.junit.BeforeClass
    public static void setUpBeforeClass() throws Exception {
        document = createDocument();
    }
}