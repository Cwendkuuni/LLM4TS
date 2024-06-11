package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
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
        Document document = mock(Document.class);
        Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        Node node1 = mock(Element.class);
        Node node2 = mock(Element.class);
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                switch (index) {
                    case 0:
                        return node1;
                    case 1:
                        return node2;
                    default:
                        return null;
                }
            }
        });

        OperationsHelper helper = new OperationsHelperImpl();

        List<Node> children = helper.children(document);
        assertEquals(2, children.size());
        assertEquals(node1, children.get(0));
        assertEquals(node2, children.get(1));
    }

    @Test
    public void testChildrenEmpty() {
        Document document = mock(Document.class);
        OperationsHelper helper = new OperationsHelperImpl();

        List<Node> children = helper.children(document);
        assertEquals(0, children.size());
    }

    @Test
    public void testChildrenNull() {
        OperationsHelper helper = new OperationsHelperImpl();

        try {
            helper.children(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFind() {
        Document document = mock(Document.class);
        Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        Element node1 = mock(Element.class);
        Element node2 = mock(Element.class);
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                switch (index) {
                    case 0:
                        return root;
                    case 1:
                        return node1;
                    default:
                        return null;
                }
            }
        });

        when(node1.getNodeName()).thenReturn("name1");

        OperationsHelper helper = new OperationsHelperImpl();

        Node result = helper.find(document, "/name1");
        assertEquals(node1, result);

        Node result2 = helper.find(document, "/nonExistent");
        assertNull(result2);

        Node result3 = helper.find(null, "/nonExistent");
        assertNull(result3);
    }

    @Test
    public void testFindEmptyDocument() {
        Document document = mock(Document.class);
        OperationsHelper helper = new OperationsHelperImpl();

        Node result = helper.find(document, "/");
        assertNull(result);
    }

    @Test
    public void testFindNullPath() {
        Document document = mock(Document.class);
        OperationsHelper helper = new OperationsHelperImpl();

        try {
            helper.find(document, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testNodeWithName() {
        Document document = mock(Document.class);
        Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        Element node1 = mock(Element.class);
        Element node2 = mock(Element.class);
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                switch (index) {
                    case 0:
                        return root;
                    case 1:
                        return node1;
                    default:
                        return null;
                }
            }
        });

        when(node1.getNodeName()).thenReturn("name1");

        OperationsHelper helper = new OperationsHelperImpl();

        Node result = helper.nodeWithName(Collections.singletonList(root), "name1");
        assertEquals(node1, result);

        Node result2 = helper.nodeWithName(Collections.singletonList(root), "nonExistent");
        assertNull(result2);

        Node result3 = helper.nodeWithName(Collections.emptyList(), "name1");
        assertNull(result3);
    }

    @Test
    public void testNodeWithNameNullNodes() {
        OperationsHelper helper = new OperationsHelperImpl();

        try {
            helper.nodeWithName(null, "name");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSublistWithName() {
        Document document = mock(Document.class);
        Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        Element node1 = mock(Element.class);
        Element node2 = mock(Element.class);
        when(root.getChildNodes()).thenReturn(new NodeList() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Node item(int index) {
                switch (index) {
                    case 0:
                        return root;
                    case 1:
                        return node1;
                    default:
                        return null;
                }
            }
        });

        when(node1.getNodeName()).thenReturn("name1");

        OperationsHelper helper = new OperationsHelperImpl();

        List<Node> result = helper.sublistWithName(Collections.singletonList(root), "name1");
        assertEquals(1, result.size());
        assertEquals(node1, result.get(0));

        List<Node> result2 = helper.sublistWithName(Collections.singletonList(root), "nonExistent");
        assertEquals(0, result2.size());

        List<Node> result3 = helper.sublistWithName(Collections.emptyList(), "name1");
        assertEquals(0, result3.size());
    }

    @Test
    public void testSublistWithNameNullNodes() {
        OperationsHelper helper = new OperationsHelperImpl();

        try {
            helper.sublistWithName(null, "name");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSublistWithNameEmptyList() {
        OperationsHelper helper = new OperationsHelperImpl();

        List<Node> result = helper.sublistWithName(Collections.emptyList(), "name");
        assertEquals(0, result.size());
    }
}