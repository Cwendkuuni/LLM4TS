
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TableMetaTest {
    @Test
    public void testGetName() {
        Node tableNode = mock(Node.class);
        when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(tableNode.getAttributes().getNamedItem("name")).thenReturn(mockNode("name", "expectedTableName"));

        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();

        assertEquals("expectedTableName", actualName);
    }

    @Test
    public void testGetColumns() {
        Node tableNode = mock(Node.class);
        when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(tableNode.getAttributes().getNamedItem("name")).thenReturn(mockNode("name", "expectedTableName"));
        when(tableNode.getChildNodes()).thenReturn(mock(NodeList.class));
        when(tableNode.getChildNodes().getLength()).thenReturn(2);
        when(tableNode.getChildNodes().item(0)).thenReturn(mockColumnNode("column1"));
        when(tableNode.getChildNodes().item(1)).thenReturn(mockColumnNode("column2"));

        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();

        List<TableColumnMeta> expectedColumns = Arrays.asList(new TableColumnMeta(mockColumnNode("column1")), new TableColumnMeta(mockColumnNode("column2")));
        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void testTableMetaConstructionAndGetComments() {
        Node tableNode = mock(Node.class);
        when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(tableNode.getAttributes().getNamedItem("name")).thenReturn(mockNode("name", "expectedTableName"));
        when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(mockNode("comments", "expectedComments"));

        NodeList columnNodes = mock(NodeList.class);
        when(tableNode.getChildNodes()).thenReturn(columnNodes);
        when(columnNodes.getLength()).thenReturn(2);
        when(columnNodes.item(0)).thenReturn(mockColumnNode("column1"));
        when(columnNodes.item(1)).thenReturn(mockColumnNode("column2"));

        TableMeta tableMeta = new TableMeta(tableNode);

        String expectedName = "expectedTableName";
        String expectedComments = "expectedComments";
        String actualName = tableMeta.getName();
        String actualComments = tableMeta.getComments();

        assertEquals(expectedName, actualName);
        assertEquals(expectedComments, actualComments);

        verify(tableNode, times(1)).getAttributes();
        verify(tableNode, times(1)).getChildNodes();
        verify(tableNode, times(1)).getAttributes().getNamedItem("name");
        verify(tableNode, times(1)).getAttributes().getNamedItem("comments");
        verify(tableNode, times(1)).getChildNodes().getLength();
        verify(tableNode, times(2)).getChildNodes().item(0);
    }

    private Node mockNode(String nodeName, String nodeValue) {
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn(nodeName);
        when(node.getNodeValue()).thenReturn(nodeValue);
        return node;
    }

    private Node mockColumnNode(String name) {
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("column");
        return node;
    }
}
