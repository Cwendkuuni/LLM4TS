package net.sourceforge.schemaspy.model.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TableMetaTest {

    private static final Logger logger = Logger.getLogger(TableMeta.class.getName());

    @Test
    public void testGetName() {
        String expectedName = "expectedTableName";
        TableMeta tableMeta = new TableMeta(createMockNode("name", expectedName));
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetComments() {
        String expectedComments = "Expected comments";
        TableMeta tableMeta = new TableMeta(createMockNode("comments", expectedComments));
        String actualComments = tableMeta.getComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetColumns() {
        String columnName1 = "Column1";
        String columnName2 = "Column2";
        TableColumnMeta columnMeta1 = new TableColumnMeta("column1");
        TableColumnMeta columnMeta2 = new TableColumnMeta("column2");
        List<TableColumnMeta> expectedColumns = Arrays.asList(columnMeta1, columnMeta2);

        Node tableNode = createMockNode("", "");
        Node columnNode1 = createMockNode("column", columnName1);
        Node columnNode2 = createMockNode("column", columnName2);
        tableNode.appendChild(columnNode1);
        tableNode.appendChild(columnNode2);

        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();

        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void testGetRemoteCatalogAndSchema() {
        String remoteCatalog = "ExpectedCatalog";
        String remoteSchema = "ExpectedSchema";
        TableMeta tableMeta = new TableMeta(createMockNode("remoteCatalog", remoteCatalog), createMockNode("remoteSchema", remoteSchema));
        String actualRemoteCatalog = tableMeta.getRemoteCatalog();
        String actualRemoteSchema = tableMeta.getRemoteSchema();

        assertEquals(remoteCatalog, actualRemoteCatalog);
        assertEquals(remoteSchema, actualRemoteSchema);
    }

    @Test
    public void testTableMetaConstructorEdgeCases() {
        // Test cases for missing name attribute
        Node tableNode1 = createMockNode("", "");
        assertThrows(NullPointerException.class, () -> new TableMeta(tableNode1));

        // Test cases for missing comments attribute
        Node tableNode2 = createMockNode("name", "");
        Node tableNode3 = createMockNode("name", "C1");
        assertThrows(NullPointerException.class, () -> new TableMeta(tableNode2));
        assertThrows(NullPointerException.class, () -> new TableMeta(tableNode3));

        // Test cases for missing remoteCatalog and remoteSchema attributes
        Node tableNode4 = createMockNode("name", "C1", "comments", "");
        assertThrows(NullPointerException.class, () -> new TableMeta(tableNode4));
        Node tableNode5 = createMockNode("name", "C1", "comments", "R1", "");
        assertThrows(NullPointerException.class, () -> new TableMeta(tableNode5));
    }

    private Node createMockNode(String... nodeNamesAndValues) {
        Document doc = new DocumentImpl();
        Element tableNode = doc.createElement("table");

        for (int i = 0; i < nodeNamesAndValues.length / 2; i++) {
            String nodeName = nodeNamesAndValues[i * 2];
            String nodeValue = nodeNamesAndValues[i * 2 + 1];
            Attr attr = doc.createAttribute(nodeName);
            attr.setValue(nodeValue);
            tableNode.setAttributeNode(attr);
        }

        return tableNode;
    }
}