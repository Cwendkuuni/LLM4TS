package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        final Document mockDocument = mock(Document.class);
        final Element mockElement = mock(Element.class);
        when(mockDocument.getDocumentElement()).thenReturn(mockElement);

        final Node mockFirstChild = mock(Node.class);
        final Node mockSecondChild = mock(Node.class);
        when(mockElement.getChildNodes()).thenReturn(mockNodes(mockFirstChild, mockSecondChild));

        final List<Node> expectedChildren = new ArrayList<Node>() {{
            add(mockFirstChild);
            add(mockSecondChild);
        }};

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> actualChildren = helper.children(mockDocument);

        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testChildrenEmpty() {
        final Document mockDocument = mock(Document.class);
        when(mockDocument.getDocumentElement()).thenReturn(mock(Element.class));

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> actualChildren = helper.children(mockDocument);

        assertEquals(Collections.emptyList(), actualChildren);
    }

    @Test
    public void testFind() {
        final Document mockDocument = mock(Document.class);
        final Element mockRoot = mock(Element.class);
        when(mockDocument.getDocumentElement()).thenReturn(mockRoot);

        final Element mockChild1 = mock(Element.class);
        final Element mockChild2 = mock(Element.class);
        when(mockRoot.getChildNodes()).thenReturn(mockNodes(mockChild1, mockChild2));

        final String path = "root/child1";
        final Element expected = mockChild1;

        final OperationsHelper helper = new OperationsHelperImpl();
        final Node actual = helper.find(mockDocument, path);

        assertSame(expected, actual);
    }

    @Test
    public void testFindNullDocument() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final Node actual = helper.find(null, "");

        assertNull(actual);
    }

    @Test
    public void testFindNullPath() {
        final Document mockDocument = mock(Document.class);
        final Element mockRoot = mock(Element.class);
        when(mockDocument.getDocumentElement()).thenReturn(mockRoot);

        final OperationsHelper helper = new OperationsHelperImpl();
        final Node actual = helper.find(mockDocument, null);

        assertNull(actual);
    }

    @Test
    public void testFindNotFound() {
        final Document mockDocument = mock(Document.class);
        final Element mockRoot = mock(Element.class);
        when(mockDocument.getDocumentElement()).thenReturn(mockRoot);

        final String path = "root/child1/nonexistent";

        final OperationsHelper helper = new OperationsHelperImpl();
        final Node actual = helper.find(mockDocument, path);

        assertNull(actual);
    }

    @Test
    public void testNodeWithName() {
        final List<Node> mockNodes = Arrays.asList(mock(Node.class), mock(Node.class));

        final Node expected = mock(Node.class);
        when(expected.getNodeName()).thenReturn("expectedName");

        final OperationsHelper helper = new OperationsHelperImpl();
        final Node actual = helper.nodeWithName(mockNodes, "expectedName");

        assertSame(expected, actual);
    }

    @Test
    public void testNodeWithNameNullList() {
        final OperationsHelper helper = new OperationsHelperImpl();
        final Node actual = helper.nodeWithName(null, "expectedName");

        assertNull(actual);
    }

    @Test
    public void testNodeWithNameNullName() {
        final List<Node> mockNodes = Arrays.asList(mock(Node.class), mock(Node.class));

        final OperationsHelper helper = new OperationsHelperImpl();
        final Node actual = helper.nodeWithName(mockNodes, null);

        assertNull(actual);
    }

    @Test
    public void testSublistWithName() {
        final List<Node> mockNodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

        final String name = "expectedName";
        final List<Node> expected = Arrays.asList(mock(Node.class), mock(Node.class));

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> actual = helper.sublistWithName(mockNodes, name);

        assertEquals(expected, actual);
    }

    @Test
    public void testSublistWithNameEmptyList() {
        final List<Node> mockNodes = Collections.emptyList();

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> actual = helper.sublistWithName(mockNodes, "expectedName");

        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testSublistWithNameNullName() {
        final List<Node> mockNodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

        final OperationsHelper helper = new OperationsHelperImpl();
        final List<Node> actual = helper.sublistWithName(mockNodes, null);

        assertEquals(Collections.emptyList(), actual);
    }

    private NodeList mockNodes(Node... nodes) {
        final NodeList mockNodeList = mock(NodeList.class);
        when(mockNodeList.getLength()).thenReturn(nodes.length);

        for (int i = 0; i < nodes.length; ++i) {
            when(mockNodeList.item(i)).thenReturn(nodes[i]);
        }

        return mockNodeList;
    }

}