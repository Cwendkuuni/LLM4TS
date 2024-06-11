package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        final Document doc = mock(Document.class);
        final Element root = mock(Element.class);
        when(doc.getDocumentElement()).thenReturn(root);
        final NodeList nodeList = mock(NodeList.class);
        when(root.getChildNodes()).thenReturn(nodeList);

        final OperationsHelperImpl helper = new OperationsHelperImpl();
        final List<Node> result = helper.children(doc);
        assertTrue(result.isEmpty());

        when(nodeList.getLength()).thenReturn(1);
        final Node child1 = mock(Node.class);
        when(nodeList.item(0)).thenReturn(child1);
        when(child1.getNodeType()).thenReturn(Node.ELEMENT_NODE);

        result.clear();
        result.addAll(helper.children(doc));
        assertEquals(1, result.size());
        assertSame(child1, result.get(0));

        when(nodeList.getLength()).thenReturn(2);
        final Node child2 = mock(Node.class);
        when(nodeList.item(1)).thenReturn(child2);
        when(child2.getNodeType()).thenReturn(Node.ELEMENT_NODE);

        result.clear();
        result.addAll(helper.children(doc));
        assertEquals(2, result.size());
        assertNotSame(child1, result.get(1));
        assertSame(child2, result.get(1));
    }

    @Test
    public void testFind() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        final Element child1 = mock(Element.class);
        when(root.getChildNodes()).thenReturn(Collections.singletonList(child1));

        final Element child2 = mock(Element.class);
        when(child1.getChildNodes()).thenReturn(Collections.singletonList(child2));

        final OperationsHelperImpl helper = new OperationsHelperImpl();
        final Node result = helper.find(document, "/root/child1/child2");

        assertNull(result);

        when(child2.getNodeName()).thenReturn("grandchild");

        result = helper.find(document, "/root/child1/child2");
        assertSame(child2, result);

        when(document.getDocumentElement().getNodeName()).thenReturn("document");
        result = helper.find(document, "/document/child1/child2");
        assertNull(result);
    }

    @Test
    public void testNodeWithName() {
        final NodeList nodeList = new NodeList() {
            @Override
            public int getLength() {
                return 3;
            }

            @Override
            public Node item(int index) {
                final Element element = new Element("element") {
                    @Override
                    public String getNodeName() {
                        return "name" + index;
                    }
                };
                return (Node) element;
            }
        };

        final OperationsHelperImpl helper = new OperationsHelperImpl();
        final Node result = helper.nodeWithName(nodeList, "name1");
        assertEquals("name1", result.getNodeName());

        result = helper.nodeWithName(nodeList, "name3");
        assertNull(result);
    }

    @Test
    public void testSublistWithName() {
        final NodeList nodeList = new NodeList() {
            @Override
            public int getLength() {
                return 5;
            }

            @Override
            public Node item(int index) {
                final Element element = new Element("element") {
                    @Override
                    public String getNodeName() {
                        return "name" + index;
                    }
                };
                return (Node) element;
            }
        };

        final OperationsHelperImpl helper = new OperationsHelperImpl();
        final List<Node> result = helper.sublistWithName(nodeList, "name2");
        assertTrue(result.isEmpty());

        result = helper.sublistWithName(nodeList, "name3");
        final List<Node> expected = Arrays.asList(nodeList.item(3), nodeList.item(4));
        assertEquals(expected.size(), result.size());
        assertFalse(result.isEmpty());
        assertTrue(result.containsAll(expected));
    }
}