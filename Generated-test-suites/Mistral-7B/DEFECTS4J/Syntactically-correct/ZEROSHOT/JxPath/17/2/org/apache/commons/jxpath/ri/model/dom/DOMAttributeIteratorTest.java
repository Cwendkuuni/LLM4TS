package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_whenCreateNewIteratorWithNullParentAndNullName_thenThrowException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new DOMAttributeIterator(null, null));
    }

    @Test
    public void testDOMAttributeIterator_whenCreateNewIteratorWithNullParentAndValidName_thenThrowException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new DOMAttributeIterator(null, new QName("test", "*")));
    }

    @Test
    public void testDOMAttributeIterator_whenCreateNewIteratorWithValidParentAndNullName_thenThrowException() {
        NodePointer parentPointer = new DOMPoint(new DOMElement("parent"), null);
        Assert.assertThrows(IllegalArgumentException.class, () -> new DOMAttributeIterator(parentPointer, null));
    }

    @Test
    public void testDOMAttributeIterator_whenCreateNewIteratorWithValidParentAndValidName_thenInitializeCorrectly() {
        String xml = "<parent xmlns:test=\"http://test.com/namespace\"><child attr=\"value1\" test:attr2=\"value2\"/></parent>";
        Node parentNode = createNodeFromString(xml);
        NodePointer parentPointer = new DOMPoint(parentNode.getChildNodes().item(0), null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        List<Attr> expectedAttributes = new ArrayList<>();
        Attr attr1 = ((Element) parentNode.getChildNodes().item(0)).getAttributeNode("attr");
        expectedAttributes.add(attr1);
        Attr attr2 = ((Element) parentNode.getChildNodes().item(0)).getAttributeNodeNS("http://test.com/namespace", "attr2");
        expectedAttributes.add(attr2);

        Assert.assertEquals(expectedAttributes.size(), iterator.size());
        for (int i = 0; i < iterator.size(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            Assert.assertEquals(expectedAttributes.get(i), nodePointer.getNode());
            iterator.next();
        }
    }

    @Test
    public void testDOMAttributeIterator_whenCreateNewIteratorWithValidParentAndFilteredName_thenInitializeCorrectly() {
        String xml = "<parent xmlns:test=\"http://test.com/namespace\"><child attr1=\"value1\" attr2=\"value2\"/></parent>";
        Node parentNode = createNodeFromString(xml);
        NodePointer parentPointer = new DOMPoint(parentNode.getChildNodes().item(0), null);
        QName name = new QName("test", "attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        List<Attr> expectedAttributes = new ArrayList<>();
        Attr attr = ((Element) parentNode.getChildNodes().item(0)).getAttributeNode("attr1");
        expectedAttributes.add(attr);

        Assert.assertEquals(1, iterator.size());
        Assert.assertEquals(expectedAttributes.get(0), iterator.getNodePointer().getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_whenGetNodePointerWithInvalidPosition_thenThrowException() {
        String xml = "<parent xmlns:test=\"http://test.com/namespace\"><child attr=\"value1\" test:attr2=\"value2\"/></parent>";
        Node parentNode = createNodeFromString(xml);
        NodePointer parentPointer = new DOMPoint(parentNode.getChildNodes().item(0), null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        iterator.next();
        iterator.next();
        iterator.getNodePointer();
    }

    @Test
    public void testDOMAttributeIterator_whenSetPositionWithValidPosition_thenSetPositionCorrectly() {
        String xml = "<parent xmlns:test=\"http://test.com/namespace\"><child attr1=\"value1\" attr2=\"value2\"/></parent>";
        Node parentNode = createNodeFromString(xml);
        NodePointer parentPointer = new DOMPoint(parentNode.getChildNodes().item(0), null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        iterator.next();
        iterator.next();

        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertEquals(iterator.getNodePointer().getNode(), ((Element) parentNode.getChildNodes().item(0)).getAttributeNodeNS("http://test.com/namespace", "attr2"));
    }

    @Test
    public void testDOMAttributeIterator_whenSetPositionWithInvalidPosition_thenReturnFalse() {
        String xml = "<parent xmlns:test=\"http://test.com/namespace\"><child attr1=\"value1\" attr2=\"value2\"/></parent>";
        Node parentNode = createNodeFromString(xml);
        NodePointer parentPointer = new DOMPoint(parentNode.getChildNodes().item(0), null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.setPosition(0));
    }

    private Node createNodeFromString(String xml) {
        return new DOMParser().parseFromString(xml, "text/xml").getDocumentElement();
    }
}