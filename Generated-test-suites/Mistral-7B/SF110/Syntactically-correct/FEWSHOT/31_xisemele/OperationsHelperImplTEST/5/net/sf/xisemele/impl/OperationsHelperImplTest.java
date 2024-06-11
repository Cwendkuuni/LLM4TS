package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationsHelperImplTest {
    private Document document;
    private Element rootElement;

    @Before
    public void setup() {
        document = mock(Document.class);
        rootElement = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(rootElement);
    }

    @Test
    public void testChildren() {
        final Node parentNode = mock(Node.class);
        final NodeList nodeList = mock(NodeList.class);
        when(parentNode.getChildNodes()).thenReturn(nodeList);
        final List<Node> expectedChildren = Arrays.asList(new Node[] { new Element("child1"), new Element("child2") });
        when(nodeList.getLength()).thenReturn(2);
        when(nodeList.item(0)).thenReturn(new Element("child1"));
        when(nodeList.item(1)).thenReturn(new Element("child2"));

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> result = helper.children(parentNode);

        assertEquals(expectedChildren, result);
    }

    @Test
    public void testFindNullDocument() {
        final OperationsHelper helper = new OperationsHelperImpl();
        assertNull(helper.find(null, ""));
    }

    @Test
    public void testFindNullPath() {
        final OperationsHelper helper = new OperationsHelperImpl();
        assertNull(helper.find(document, null));
    }

    @Test
    public void testFindEmptyDocument() {
        final OperationsHelper helper = new OperationsHelperImpl();
        assertNull(helper.find(document, ""));
    }

    @Test
    public void testFindRootElement() {
        final OperationsHelper helper = new OperationsHelperImpl();
        when(rootElement.getNodeName()).thenReturn("root");

        final Node result = helper.find(document, "");

        assertEquals(rootElement, result);
    }

    @Test
    public void testFindNodeWithOneLevel() {
        final Element child1 = new Element("child1");
        final Element child2 = new Element("child2");

        when(rootElement.getNodeName()).thenReturn("root");
        when(rootElement.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                if (index == 0) {
                    return child1;
                }
                return child2;
            }
        });

        final Node result = helper.find(document, "root/child1");

        assertEquals(child1, result);
    }

    @Test
    public void testFindNodeWithMultipleLevels() {
        final Element grandChild1 = new Element("grandChild1");
        final Element grandChild2 = new Element("grandChild2");
        final Element child1 = new Element("child1");
        final Element child2 = new Element("child2");

        when(rootElement.getNodeName()).thenReturn("root");
        when(rootElement.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                if (index == 0) {
                    return child1;
                }
                return child2;
            }
        });
        when(child1.getNodeName()).thenReturn("child1");
        when(child1.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                if (index == 0) {
                    return grandChild1;
                }
                return grandChild2;
            }
        });

        final Node result = helper.find(document, "root/child1/grandChild1");

        assertEquals(grandChild1, result);
    }

    @Test
    public void testFindNullNode() {
        final OperationsHelper helper = new OperationsHelperImpl();
        assertNull(helper.find(document, "/nonExistentNode"));
    }

    @Test
    public void testNodeWithNameNullList() {
        final OperationsHelper helper = new OperationsHelperImpl();
        assertNull(helper.nodeWithName(null, ""));
    }

    @Test
    public void testNodeWithNameEmptyList() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> emptyList = new ArrayList<>();
        assertNull(helper.nodeWithName(emptyList, ""));
    }

    @Test
    public void testNodeWithNameNullName() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> nodes = Arrays.asList(new Node[] { new Element("child1"), new Element("child2") });
        assertNull(helper.nodeWithName(nodes, null));
    }

    @Test
    public void testNodeWithNameNotExistingName() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> nodes = Arrays.asList(new Node[] { new Element("child1"), new Element("child2") });
        assertNull(helper.nodeWithName(nodes, "nonExistentName"));
    }

    @Test
    public void testNodeWithNameExistingName() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> nodes = Arrays.asList(new Node[] { new Element("child1"), new Element("child2") });
        final Node expectedNode = nodes.get(0);

        final Node result = helper.nodeWithName(nodes, "child1");

        assertEquals(expectedNode, result);
    }

    @Test
    public void testSublistWithNameNullList() {
        final OperationsHelper helper = new OperationsHelperImpl();
        assertNull(helper.sublistWithName(null, ""));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> emptyList = new ArrayList<>();
        assertEmpty(helper.sublistWithName(emptyList, ""));
    }

    @Test
    public void testSublistWithNameNullName() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> nodes = Arrays.asList(new Node[] { new Element("child1"), new Element("child2") });
        assertEmpty(helper.sublistWithName(nodes, null));
    }

    @Test
    public void testSublistWithNameNotExistingName() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> nodes = Arrays.asList(new Node[] { new Element("child1"), new Element("child2") });
        final List<Node> expectedEmptyList = new ArrayList<>();

        assertEmpty(helper.sublistWithName(nodes, "nonExistentName"));
    }

    @Test
    public void testSublistWithNameExistingName() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> nodes = Arrays.asList(new Node[] { new Element("child1"), new Element("child2"), new Element("child3") });
        final List<Node> expectedSublist = Arrays.asList(new Node[] { new Element("child1") });

        final List<Node> result = helper.sublistWithName(nodes, "child1");

        assertEquals(expectedSublist, result);
    }
}