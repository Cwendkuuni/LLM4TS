package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {

    @Test
    public void testGetName() {
        String expectedName = "testTable";
        Node tableNode = createMockTableNode("name", expectedName);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetComments() {
        String expectedComments = "This is a test table.";
        Node tableNode = createMockTableNode("comments", expectedComments);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualComments = tableMeta.getComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetColumns() {
        String expectedColumnsSize = "2";
        Node tableNode = createMockTableNodeWithColumns(2);
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();
        assertEquals(Integer.parseInt(expectedColumnsSize), actualColumns.size());
    }

    @Test
    public void testGetRemoteCatalogAndSchema() {
        String expectedRemoteCatalog = "test_catalog";
        String expectedRemoteSchema = "test_schema";
        Node tableNode = createMockTableNode("remoteCatalog", expectedRemoteCatalog, "remoteSchema", expectedRemoteSchema);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualRemoteCatalog = tableMeta.getRemoteCatalog();
        String actualRemoteSchema = tableMeta.getRemoteSchema();
        assertEquals(expectedRemoteCatalog, actualRemoteCatalog);
        assertEquals(expectedRemoteSchema, actualRemoteSchema);
    }

    // Edge case: Empty list of columns
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyColumnsList() {
        Node tableNode = createMockTableNodeWithColumns(0);
        new TableMeta(tableNode);
    }

    // Edge case: Null comments
    @Test
    public void testNullComments() {
        Node tableNode = createMockTableNode("comments", null);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualComments = tableMeta.getComments();
        assertNull(actualComments);
    }

    // Edge case: Invalid attribute names
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAttributeNames() {
        Node tableNode = new Element("table");
        tableNode.setNodeName("testTable");
        new TableMeta(tableNode);
    }

    // Edge case: Invalid attribute values
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAttributeValues() {
        Node tableNode = createMockTableNode("name", "testTable");
        tableNode.setNodeValue("testTable"); // Set a non-string value for Node value
        new TableMeta(tableNode);
    }

    // Edge case: Null values for remoteCatalog and remoteSchema
    @Test
    public void testNullRemoteCatalogAndSchema() {
        Node tableNode = createMockTableNode("remoteCatalog", null, "remoteSchema", expectedRemoteSchema);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualRemoteCatalog = tableMeta.getRemoteCatalog();
        assertNull(actualRemoteCatalog);
        String actualRemoteSchema = tableMeta.getRemoteSchema();
        assertEquals(expectedRemoteSchema, actualRemoteSchema);
    }

    // Helper method to create mock Node for testing
    private Node createMockTableNode(String... attributes) {
        Element tableElement = new Element("table");
        Attr nameAttr = new Attr("name", "testTable");
        tableElement.appendChild(nameAttr);

        for (int i = 0; i < attributes.length; i += 2) {
            String name = attributes[i];
            String value = attributes[i + 1];
            Attr attr = new Attr(name, value);
            tableElement.appendChild(attr);
        }

        return tableElement;
    }

    // Helper method to create mock Node for testing with columns
    private Node createMockTableNodeWithColumns(int columnCount) {
        Element tableElement = new Element("table");
        Attr nameAttr = new Attr("name", "testTable");
        Attr commentsAttr = new Attr("comments", "");
        tableElement.appendChild(nameAttr);
        tableElement.appendChild(commentsAttr);

        Element columnElement;
        for (int i = 0; i < columnCount; i++) {
            columnElement = new Element("column");
            tableElement.appendChild(columnElement);
        }

        return tableElement;
    }

    // Helper method to create mock Attr for testing
    private Attr createMockTableAttributeNode(String name, String value) {
        return new Attr(name, value);
    }
}