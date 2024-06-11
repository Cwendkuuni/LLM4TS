
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {

    @Test
    public void testGetName() {
        String expectedName = "testTable";
        Node tableNode = createMockTableNode("name", expectedName, null, null, null, null);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetColumns() {
        String expectedColumnName1 = "column1";
        String expectedColumnName2 = "column2";
        Node tableNode = createMockTableNode(null, null, null, null, null, createMockColumnNodes(expectedColumnName1, expectedColumnName2));
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();

        assertEquals(2, actualColumns.size());
        TableColumnMeta column1 = actualColumns.get(0);
        TableColumnMeta column2 = actualColumns.get(1);
        assertEquals(expectedColumnName1, column1.getName());
        assertEquals(expectedColumnName2, column2.getName());
    }

    @Test
    public void testTableMetaConstructor() {
        String tableName = "testTable";
        String comments = "test comments";
        String remoteCatalog = "testCatalog";
        String remoteSchema = "testSchema";
        Node tableNode = createMockTableNode("name", tableName, "comments", comments, "remoteSchema", remoteSchema, "remoteCatalog", remoteCatalog);
        List<Node> columnNodes = createMockColumnNodes("column1", "column2");
        Element tableElement = createMockTableElement(tableNode, columnNodes.toArray(new Node[0]));

        TableMeta tableMeta = new TableMeta(tableElement);

        assertEquals(tableName, tableMeta.getName());
        assertEquals(comments, tableMeta.getComments());
        assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
        assertEquals(remoteSchema, tableMeta.getRemoteSchema());
        assertEquals(2, tableMeta.getColumns().size());
    }

    private Node createMockTableNode(String... attributes) {
        // Implementation of creating a mock Node object with the given attributes
    }

    private Node createMockColumnNodes(String... columnNames) {
        // Implementation of creating mock ColumnNodes with the given column names
    }

    private Element createMockTableElement(Node tableNode, Node... columnNodes) {
        // Implementation of creating a mock Element object with the given tableNode and columnNodes
    }
}
