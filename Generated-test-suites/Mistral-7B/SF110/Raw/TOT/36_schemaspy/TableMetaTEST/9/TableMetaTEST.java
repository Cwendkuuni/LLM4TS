package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import org.junit.Test;

public class TableMetaTest {
    @Test
    public void testGetName() {
        String expectedName = "testTableName";
        Node tableNode = createMockTableNode("name", expectedName);
        TableMeta tableMeta = new TableMeta(tableNode);
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetColumns() {
        String expectedColumnName1 = "column1";
        String expectedColumnName2 = "column2";
        Node tableNode = createMockTableNodeWithColumns(expectedColumnName1, expectedColumnName2);
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();
        assertNotNull(actualColumns);
        assertEquals(2, actualColumns.size());
        TableColumnMeta column1 = actualColumns.get(0);
        assertEquals(expectedColumnName1, column1.getName());
        TableColumnMeta column2 = actualColumns.get(1);
        assertEquals(expectedColumnName2, column2.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(createMockTableNodeWithoutComments());
        assertNull(tableMeta.getComments());
    }

    private Node createMockTableNode(String nodeName, String nodeValue) {
        // Implement the logic to create a mock Node with the given name and value
    }

    private Node createMockTableNodeWithColumns(String column1Name, String column2Name) {
        // Implement the logic to create a mock Node with the given columns
    }

    private Node createMockTableNodeWithoutComments() {
        // Implement the logic to create a mock Node without comments
    }
}