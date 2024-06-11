package net.sf.xisemele.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

    private Document document;
    private Element root;

    @Before
    public void setUp() {
        document = mock(Document.class);
        root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
    }

    @Test
    public void testChildrenEmpty() {
        Node node = mock(Node.class);
        NodeList nodeList = mock(NodeList.class);
        when(node.getChildNodes()).thenReturn(nodeList);
        assertEquals(Collections.emptyList(), OperationsHelperImpl.instance().children(node));
    }

    @Test
    public void testChildrenSingleElement() {
        Node node = mock(Node.class);
        NodeList nodeList = new NodeList() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Node item(int index) {
                return root;
            }
        };
        when(node.getChildNodes()).thenReturn(nodeList);
        List<Node> children = OperationsHelperImpl.instance().children(node);
        assertEquals(Arrays.asList(root), children);
    }

    @Test
    public void testChildrenMultipleElements() {
        Node node = mock(Node.class);
        NodeList nodeList = new NodeList() {
            @Override
            public int getLength() {
                return 3;
            }

            @Override
            public Node item(int index) {
                Element element = mock(Element.class);
                when(element.getNodeName()).thenReturn("element" + index);
                return element;
            }
        };
        when(node.getChildNodes()).thenReturn(nodeList);
        List<Node> children = OperationsHelperImpl.instance().children(node);
        List<String> expected = Arrays.asList("element0", "element1", "element2");
        assertEquals(expected, children.stream().map(node -> node.getNodeName()).collect(Collectors.toList()));
    }

    @Test
    public void testFindNullDocument() {
        assertNull(OperationsHelperImpl.instance().find(null, ""));
    }

    @Test
    public void testFindEmptyDocument() {
        assertNull(OperationsHelperImpl.instance().find(document, ""));
    }

    @Test
    public void testFindSingleNodeDocument() {
        when(root.getNodeName()).thenReturn("root");
        Element element = mock(Element.class);
        when(element.getNodeName()).thenReturn("element");
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("element");
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Node item(int index) {
                return node;
            }
        });
        assertEquals(element, OperationsHelperImpl.instance().find(document, "root/element"));
    }

    @Test
    public void testFindMultipleNodesDocument() {
        when(root.getNodeName()).thenReturn("root");
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        when(element1.getNodeName()).thenReturn("element1");
        when(element2.getNodeName()).thenReturn("element2");
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("element1");
        when(node2.getNodeName()).thenReturn("element2");
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                return index == 0 ? node1 : node2;
            }
        });
        assertEquals(element1, OperationsHelperImpl.instance().find(document, "root/element1"));
        assertEquals(element2, OperationsHelperImpl.instance().find(document, "root/element2"));
        assertNull(OperationsHelperImpl.instance().find(document, "root/nonexistent"));
    }

    @Test
    public void testNodeWithNameNullList() {
        assertNull(OperationsHelperImpl.instance().nodeWithName(null, ""));
    }

    @Test
    public void testNodeWithNameEmptyList() {
        assertNull(OperationsHelperImpl.instance().nodeWithName(Collections.emptyList(), ""));
    }

    @Test
    public void testNodeWithNameSingleNode() {
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("element");
        assertEquals(node, OperationsHelperImpl.instance().nodeWithName(Arrays.asList(node), "element"));
    }

    @Test
    public void testNodeWithNameMultipleNodes() {
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("element1");
        when(node2.getNodeName()).thenReturn("element2");
        assertEquals(node1, OperationsHelperImpl.instance().nodeWithName(Arrays.asList(node1, node2), "element1"));
        assertNotSame(node1, OperationsHelperImpl.instance().nodeWithName(Arrays.asList(node1, node2), "element2"));
    }

    @Test
    public void testSublistWithNameNullList() {
        assertNull(OperationsHelperImpl.instance().sublistWithName(null, ""));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        assertNull(OperationsHelperImpl.instance().sublistWithName(Collections.emptyList(), ""));
    }

    @Test
    public void testSublistWithNameSingleNode() {
        Node node = mock(Node.class);
        assertEquals(Arrays.asList(node), OperationsHelperImpl.instance().sublistWithName(Arrays.asList(node), ""));
    }

    @Test
    public void testSublistWithNameSingleElementList() {
        Node node1 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("element1");
        Node node2 = mock(Node.class);
        when(node2.getNodeName()).thenReturn("element2");
        List<Node> nodes = Arrays.asList(node1, node2);
        List<Node> sublist = OperationsHelperImpl.instance().sublistWithName(nodes, "element1");
        assertEquals(Arrays.asList(node1), sublist);
    }

    @Test
    public void testSublistWithNameMultipleMatchingNodes() {
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("element1");
        when(node2.getNodeName()).thenReturn("element1");
        when(node3.getNodeName()).thenReturn("element2");
        List<Node> nodes = Arrays.asList(node1, node2, node3);
        List<Node> sublist = OperationsHelperImpl.instance().sublistWithName(nodes, "element1");
        assertEquals(Arrays.asList(node1, node2), sublist);
    }

    @Test
    public void testSublistWithNameNoMatchingNodes() {
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("element1");
        when(node2.getNodeName()).thenReturn("element2");
        when(node3.getNodeName()).thenReturn("element2");
        List<Node> nodes = Arrays.asList(node1, node2, node3);
        assertNull(OperationsHelperImpl.instance().sublistWithName(nodes, "element3"));
    }
}