package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        final Node node1 = mock(Element.class);
        final Node node2 = mock(Element.class);
        when(node1.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node2.getNodeType()).thenReturn(Node.COMMENT_NODE);

        final NodeList nodeList = mock(NodeList.class);
        when(nodeList.getLength()).thenReturn(2);
        when(nodeList.item(0)).thenReturn(node1);
        when(nodeList.item(1)).thenReturn(node2);

        when(root.getChildNodes()).thenReturn(nodeList);

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> children = helper.children(document);
        assertEquals(1, children.size());
        assertSame(node1, children.get(0));
    }

    @Test
    public void testChildren_Empty() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        when(root.getChildNodes()).thenReturn(Collections.emptyNodeList());

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> children = helper.children(document);
        assertEquals(0, children.size());
    }

    @Test
    public void testChildren_NullDocument() {
        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> children = helper.children(null);
        assertNull(children);
    }

    @Test
    public void testFind() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        final Element child1 = mock(Element.class);
        final Element child2 = mock(Element.class);
        when(child1.getNodeName()).thenReturn("child1");
        when(child2.getNodeName()).thenReturn("child2");

        final NodeList childList1 = mock(NodeList.class);
        when(child1.getChildNodes()).thenReturn(childList1);

        final Element grandChild = mock(Element.class);
        when(childList1.item(0)).thenReturn(child1);
        when(child1.getChildNodes()).thenReturn(Collections.singletonList(grandChild));
        when(grandChild.getNodeName()).thenReturn("grandChild");

        final Node nodeToFind = grandChild;

        when(root.getChildNodes()).thenReturn(Collections.singletonList(child1));

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.find(document, "/child1/grandChild");
        assertSame(nodeToFind, found);
    }

    @Test
    public void testFind_NullDocument() {
        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.find(null, "/");
        assertNull(found);
    }

    @Test
    public void testFind_NullPath() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.find(document, null);
        assertNull(found);
    }

    @Test
    public void testFind_PathDoesNotExist() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.find(document, "/nonExistent");
        assertNull(found);
    }

    @Test
    public void testFind_MoreThanOneMatch() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        final Element child1 = mock(Element.class);
        final Element child2 = mock(Element.class);
        when(child1.getNodeName()).thenReturn("child1");
        when(child2.getNodeName()).thenReturn("child1");

        final NodeList childList1 = mock(NodeList.class);
        when(child1.getChildNodes()).thenReturn(childList1);
        when(child2.getChildNodes()).thenReturn(childList1);

        final Element grandChild1 = mock(Element.class);
        final Element grandChild2 = mock(Element.class);
        when(grandChild1.getNodeName()).thenReturn("grandChild");
        when(grandChild2.getNodeName()).thenReturn("grandChild");

        when(childList1.item(0)).thenReturn(child1);
        when(child1.getChildNodes()).thenReturn(Arrays.asList(grandChild1, grandChild2));
        when(childList1.item(1)).thenReturn(child2);
        when(child2.getChildNodes()).thenReturn(Collections.singletonList(grandChild2));

        when(root.getChildNodes()).thenReturn(Arrays.asList(child1, child2));

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.find(document, "/child1/grandChild");
        assertSame(grandChild1, found);
    }

    @Test
    public void testNodeWithName() {
        final List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.nodeWithName(nodes, "nodeName1");
        assertNull(found);

        final Node found2 = helper.nodeWithName(nodes, "nodeName2");
        assertNotNull(found2);
    }

    @Test
    public void testNodeWithName_NullList() {
        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.nodeWithName(null, "nodeName");
        assertNull(found);
    }

    @Test
    public void testNodeWithName_EmptyList() {
        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final Node found = helper.nodeWithName(Collections.emptyList(), "nodeName");
        assertNull(found);
    }

    @Test
    public void testSublistWithName() {
        final List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> sublist = helper.sublistWithName(nodes, "nodeName1");
        assertEquals(0, sublist.size());

        final List<Node> sublist2 = helper.sublistWithName(nodes, "nodeName2");
        assertEquals(1, sublist2.size());
    }

    @Test
    public void testSublistWithName_NullList() {
        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> sublist = helper.sublistWithName(null, "nodeName");
        assertNull(sublist);
    }

    @Test
    public void testSublistWithName_EmptyList() {
        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> sublist = helper.sublistWithName(Collections.emptyList(), "nodeName");
        assertEquals(0, sublist.size());
    }

    @Test
    public void testSublistWithName_NoMatchingNodes() {
        final List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> sublist = helper.sublistWithName(nodes, "nodeName1");
        assertEquals(0, sublist.size());
    }

    @Test
    public void testSublistWithName_MultipleMatchingNodes() {
        final List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

        final Node node1 = mock(Node.class);
        final Node node2 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("nodeName");
        when(node2.getNodeName()).thenReturn("nodeName");

        when(nodes.get(0)).thenReturn(node1);
        when(nodes.get(1)).thenReturn(node2);

        final OperationsHelperImpl helper = new OperationsHelperImpl();

        final List<Node> sublist = helper.sublistWithName(nodes, "nodeName");
        assertEquals(2, sublist.size());
    }
}