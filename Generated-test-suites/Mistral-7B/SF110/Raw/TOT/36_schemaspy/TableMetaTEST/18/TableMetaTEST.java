
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.w3c.dom.*;
import static org.mockito.Mockito.*;

public class TableMetaTest {

    @Test
    public void testgetName() {
        String expectedName = "testTable";
        Node tableNode = mock(Node.class);
        when(tableNode.getAttributes()).thenReturn(createMockNamedNodeMap("name", expectedName));
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testgetColumns() {
        String expectedColumnName1 = "column1";
        String expectedColumnName2 = "column2";
        Node columnNode1 = mock(Node.class);
        Node columnNode2 = mock(Node.class);
        when(columnNode1.getNodeName()).thenReturn("column");
        when(columnNode1.getAttributes()).thenReturn(createMockNamedNodeMap("name", expectedColumnName1));
        when(columnNode2.getNodeName()).thenReturn("column");
        when(columnNode2.getAttributes()).thenReturn(createMockNamedNodeMap("name", expectedColumnName2));
        Node tableNode = mock(Node.class);
        when(tableNode.getNodeName()).thenReturn("table");
        when(tableNode.getChildNodes()).thenReturn(Arrays.asList(columnNode1, columnNode2).toArray(new Node[0]));
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();
        assertNotNull(actualColumns);
        assertEquals(2, actualColumns.size());
        TableColumnMeta actualColumn1 = actualColumns.get(0);
        assertEquals(expectedColumnName1, actualColumn1.getName());
        TableColumnMeta actualColumn2 = actualColumns.get(1);
        assertEquals(expectedColumnName2, actualColumn2.getName());
    }

    @Test
    public void testTableMetaConstructor() {
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
        assertNotNull(tableMeta.getColumns());
        assertFalse(tableMeta.getColumns().isEmpty());

        tableNode = mock(Node.class);
        String expectedName = "name";
        String expectedComments = "comments";
        String expectedRemoteCatalog = "remoteCatalog";
        String expectedRemoteSchema = "remoteSchema";
        when(tableNode.getAttributes()).thenReturn(createMockNamedNodeMap("name", expectedName, "comments", expectedComments, "remoteCatalog", expectedRemoteCatalog, "remoteSchema", expectedRemoteSchema));
        tableMeta = new TableMeta(tableNode);
        assertEquals(expectedName, tableMeta.getName());
        assertEquals(expectedComments, tableMeta.getComments());
        assertEquals(expectedRemoteCatalog, tableMeta.getRemoteCatalog());
        assertEquals(expectedRemoteSchema, tableMeta.getRemoteSchema());
    }

    private Node createMockTableNode(String... attributes) {
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createMockNamedNodeMap(attributes));
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("table");
        when(node.getParentNode()).thenReturn(element);
        return node;
    }

    private NamedNodeMap createMockNamedNodeMap(String... attributeNamesAndValues) {
        NamedNodeMap map = mock(NamedNodeMap.class);
        for (String attributeNameAndValue : attributeNamesAndValues) {
            String[] parts = attributeNameAndValue.split("=");
            String attributeName = parts[0].trim();
            String attributeValue = parts[1].trim();
            Node attributeNode = createMockNode("CDATASection", attributeValue);
            when(map.getNamedItem(attributeName)).thenReturn(attributeNode);
        }
        return map;
    }

    private Node createMockNode(String nodeName, String nodeValue) {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn(nodeName);
        when(node.getNodeValue()).thenReturn(nodeValue);
        when(node.getParentNode()).thenReturn(element);
        when(element.getOwnerDocument()).thenReturn(mock(Document.class));
        return node;
    }
}
