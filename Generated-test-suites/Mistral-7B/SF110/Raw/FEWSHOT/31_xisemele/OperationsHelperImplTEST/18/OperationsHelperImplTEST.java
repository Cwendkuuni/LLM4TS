package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class OperationsHelperImplTest {

    private OperationsHelper operationsHelper;
    private Document document;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        operationsHelper = new OperationsHelperImpl();
        document = mock(Document.class);
    }

    @Test
    public void testChildren_EmptyNodeList() {
        when(document.getDocumentElement().getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Node item(int index) {
                return null;
            }
        });

        List<Node> children = operationsHelper.children(document);
        assertNull(children);
    }

    @Test
    public void testChildren_OneElementChild() {
        Node element = mock(Element.class);
        when(document.getDocumentElement().getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Node item(int index) {
                if (index == 0) {
                    return element;
                }
                return null;
            }
        });

        List<Node> children = operationsHelper.children(document);
        assertEquals(1, children.size());
        assertSame(element, children.get(0));
    }

    @Test
    public void testChildren_MultipleElementChildren() {
        Node element1 = mock(Element.class);
        Node element2 = mock(Element.class);
        when(document.getDocumentElement().getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                if (index == 0) {
                    return element1;
                }
                return element2;
            }
        });

        List<Node> children = operationsHelper.children(document);
        assertEquals(2, children.size());
        assertNotSame(element1, children.get(1));
        assertNotSame(element2, children.get(0));
        assertSame(element1, children.get(0));
        assertSame(element2, children.get(1));
    }

    @Test
    public void testFind_DocumentElementMatchesPath() {
        when(document.getDocumentElement().getNodeName()).thenReturn("root");
        Node root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        Node found = operationsHelper.find(document, "root");
        assertSame(root, found);
    }

    @Test
    public void testFind_PathMatchesElementWithSameNameAsDocumentElement() {
        when(document.getDocumentElement().getNodeName()).thenReturn("root");
        Node root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        Element child = mock(Element.class);
        when(root.getNodeName()).thenReturn("child");
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Node item(int index) {
                return child;
            }
        });

        Node found = operationsHelper.find(document, "root/child");
        assertSame(child, found);
    }

    @Test
    public void testFind_PathMatchesElementWithGivenName() {
        when(document.getDocumentElement().getNodeName()).thenReturn("root");
        Node root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        Element child1 = mock(Element.class);
        when(root.getNodeName()).thenReturn("child1");
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Node item(int index) {
                if (index == 0) {
                    return child1;
                }
                return null;
            }
        });

        Element child2 = mock(Element.class);
        when(child1.getNodeName()).thenReturn("child2");
        when(child1.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Node item(int index) {
                return child2;
            }
        });

        Node found = operationsHelper.find(document, "root/child1/child2");
        assertSame(child2, found);
    }

    @Test
    public void testFind_PathDoesNotMatchAnyNode() {
        when(document.getDocumentElement().getNodeName()).thenReturn("root");
        Node root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        Node found = operationsHelper.find(document, "root/child1/child2");
        assertNull(found);
    }

    @Test
    public void testFind_NullDocument() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(mock(Node.class));

        when(document.getDocumentElement()).thenReturn(mock(Node.class));

        Node found = operationsHelper.find(null, "path");
        assertNull(found);
    }

    @Test
    public void testNodeWithName_ListContainsNodeWithGivenName() {
        Node node1 = mock(Element.class);
        Node node2 = mock(Element.class);

        when(node1.getNodeName()).thenReturn("name1");
        when(node2.getNodeName()).thenReturn("name2");

        List<Node> nodes = Arrays.asList(node1, node2);

        Node found = operationsHelper.nodeWithName(nodes, "name1");
        assertSame(node1, found);
    }

    @Test
    public void testNodeWithName_ListDoesNotContainNodeWithGivenName() {
        Node node1 = mock(Element.class);
        Node node2 = mock(Element.class);

        when(node1.getNodeName()).thenReturn("name1");
        when(node2.getNodeName()).thenReturn("name2");

        List<Node> nodes = Arrays.asList(node1, node2);

        Node found = operationsHelper.nodeWithName(nodes, "name3");
        assertNull(found);
    }

    @Test
    public void testSublistWithName_ListContainsOnlyNodeWithGivenName() {
        Node node1 = mock(Element.class);
        Node node2 = mock(Element.class);

        when(node1.getNodeName()).thenReturn("name1");
        when(node2.getNodeName()).thenReturn("name2");

        List<Node> nodes = Arrays.asList(node1, node2);

        List<Node> sublist = operationsHelper.sublistWithName(nodes, "name1");
        assertEquals(1, sublist.size());
        assertSame(node1, sublist.get(0));
    }

    @Test
    public void testSublistWithName_ListContainsMultipleNodesWithGivenName() {
        Node node1 = mock(Element.class);
        Node node2 = mock(Element.class);
        Node node3 = mock(Element.class);

        when(node1.getNodeName()).thenReturn("name1");
        when(node2.getNodeName()).thenReturn("name1");
        when(node3.getNodeName()).thenReturn("name2");

        List<Node> nodes = Arrays.asList(node1, node2, node3);

        List<Node> sublist = operationsHelper.sublistWithName(nodes, "name1");
        assertEquals(2, sublist.size());
        assertTrue(sublist.contains(node1));
        assertTrue(sublist.contains(node2));
    }

    @Test
    public void testSublistWithName_ListDoesNotContainAnyNodeWithGivenName() {
        Node node1 = mock(Element.class);
        Node node2 = mock(Element.class);

        when(node1.getNodeName()).thenReturn("name1");
        when(node2.getNodeName()).thenReturn("name2");

        List<Node> nodes = Arrays.asList(node1, node2);

        List<Node> sublist = operationsHelper.sublistWithName(nodes, "name3");
        assertEquals(0, sublist.size());
    }
}