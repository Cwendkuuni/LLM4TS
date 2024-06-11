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
import java.util.Collections;
import java.util.List;

public class OperationsHelperImplTest {

    private Document document;
    private List<Node> nodes;
    private Element root;

    @Before
    public void setup() {
        document = mock(Document.class);
        nodes = new ArrayList<>();
        nodes.add(mock(Node.class));
        nodes.add(mock(Node.class));
        nodes.add(mock(Element.class));
        nodes.add(mock(Element.class));
        root = (Element) nodes.get(nodes.size() - 1);
        when(document.getDocumentElement()).thenReturn(root);
    }

    @Test
    public void testChildren() {
        final Node node = nodes.get(1);
        when(node.getChildNodes()).thenReturn(nodes.subList(2, nodes.size()).toArray(new Node[0]));

        final List<Node> result = OperationsHelperImpl.getInstance().children(node);
        assertEquals(2, result.size());
        assertEquals(nodes.get(2), result.get(0));
        assertEquals(nodes.get(3), result.get(1));
    }

    @Test
    public void testFindNullDocument() {
        assertNull(OperationsHelperImpl.getInstance().find(null, ""));
    }

    @Test
    public void testFindNullPath() {
        assertNull(OperationsHelperImpl.getInstance().find(document, null));
    }

    @Test
    public void testFindRoot() {
        assertEquals(root, OperationsHelperImpl.getInstance().find(document, ""));
    }

    @Test
    public void testFindLeaf() {
        when(nodes.get(3).getNodeName()).thenReturn("leaf");

        assertEquals(nodes.get(3), OperationsHelperImpl.getInstance().find(document, "root/leaf"));
    }

    @Test
    public void testFindNotExist() {
        assertNull(OperationsHelperImpl.getInstance().find(document, "root/nonexistent"));
    }

    @Test
    public void testFindMultipleMatchesLastOne() {
        when(nodes.get(1).getNodeName()).thenReturn("match");
        when(nodes.get(3).getNodeName()).thenReturn("match");

        assertEquals(nodes.get(3), OperationsHelperImpl.getInstance().find(document, "root/match"));
    }

    @Test
    public void testFindMultipleMatchesFirstOne() {
        when(nodes.get(0).getNodeName()).thenReturn("match");
        when(nodes.get(3).getNodeName()).thenReturn("match");

        assertEquals(nodes.get(0), OperationsHelperImpl.getInstance().find(document, "root/match"));
    }

    @Test
    public void testNodeWithNameNullList() {
        assertNull(OperationsHelperImpl.getInstance().nodeWithName(null, ""));
    }

    @Test
    public void testNodeWithNameEmptyList() {
        assertNull(OperationsHelperImpl.getInstance().nodeWithName(Collections.emptyList(), ""));
    }

    @Test
    public void testNodeWithNameNullName() {
        assertNull(OperationsHelperImpl.getInstance().nodeWithName(nodes, null));
    }

    @Test
    public void testNodeWithNameLeaf() {
        when(nodes.get(3).getNodeName()).thenReturn("leaf");

        assertEquals(nodes.get(3), OperationsHelperImpl.getInstance().nodeWithName(nodes, "leaf"));
    }

    @Test
    public void testNodeWithNameNotExist() {
        assertNull(OperationsHelperImpl.getInstance().nodeWithName(nodes, "nonexistent"));
    }

    @Test
    public void testNodeWithNameMultipleMatchesFirstOne() {
        when(nodes.get(0).getNodeName()).thenReturn("match");
        when(nodes.get(3).getNodeName()).thenReturn("match");

        assertEquals(nodes.get(0), OperationsHelperImpl.getInstance().nodeWithName(nodes, "match"));
    }

    @Test
    public void testNodeWithNameMultipleMatchesLastOne() {
        when(nodes.get(0).getNodeName()).thenReturn("match");
        when(nodes.get(3).getNodeName()).thenReturn("match");

        assertEquals(nodes.get(3), OperationsHelperImpl.getInstance().nodeWithName(Arrays.asList(nodes.get(0), nodes.get(3)), "match"));
    }

    @Test
    public void testSublistWithNameNullList() {
        assertFalse(OperationsHelperImpl.getInstance().sublistWithName(null, "").isEmpty());
    }

    @Test
    public void testSublistWithNameEmptyList() {
        assertTrue(OperationsHelperImpl.getInstance().sublistWithName(Collections.emptyList(), "").isEmpty());
    }

    @Test
    public void testSublistWithNameNullName() {
        assertFalse(OperationsHelperImpl.getInstance().sublistWithName(nodes, null).isEmpty());
    }

    @Test
    public void testSublistWithNameLeaf() {
        when(nodes.get(3).getNodeName()).thenReturn("leaf");

        assertEquals(Arrays.asList(nodes.get(3)), OperationsHelperImpl.getInstance().sublistWithName(nodes, "leaf"));
    }

    @Test
    public void testSublistWithNameNotExist() {
        assertFalse(OperationsHelperImpl.getInstance().sublistWithName(nodes, "nonexistent").isEmpty());
        assertTrue(OperationsHelperImpl.getInstance().sublistWithName(nodes, "nonexistent").isEmpty());
    }

    @Test
    public void testSublistWithNameMultipleMatchesFirstOne() {
        when(nodes.get(0).getNodeName()).thenReturn("match");
        when(nodes.get(3).getNodeName()).thenReturn("match");

        assertEquals(Arrays.asList(nodes.get(0)), OperationsHelperImpl.getInstance().sublistWithName(nodes, "match"));
    }

    @Test
    public void testSublistWithNameMultipleMatchesLastOne() {
        when(nodes.get(0).getNodeName()).thenReturn("match");
        when(nodes.get(3).getNodeName()).thenReturn("match");

        assertEquals(Arrays.asList(nodes.get(3)), OperationsHelperImpl.getInstance().sublistWithName(nodes, "match"));
    }
}