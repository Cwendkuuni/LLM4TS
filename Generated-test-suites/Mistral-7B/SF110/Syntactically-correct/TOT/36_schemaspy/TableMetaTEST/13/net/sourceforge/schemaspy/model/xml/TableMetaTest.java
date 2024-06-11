package net.sourceforge.schemaspy.model.xml;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.List;

public class TableMetaTest {
    private Document doc;
    private Element tableNode;

    @Before
    public void setup() {
        this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        this.tableNode = this.doc.createElement("table");
        this.tableNode.setAttribute("name", "test_table");
        this.tableNode.setAttribute("comments", "This is a test table.");
        this.tableNode.setAttribute("remoteSchema", "test_schema");
        this.tableNode.setAttribute("remoteCatalog", "test_catalog");

        Element columnNode = this.doc.createElement("column");
        columnNode.setAttribute("name", "test_column");
        this.tableNode.appendChild(columnNode);

        this.doc.appendChild(this.tableNode);
    }

    @Test
    public void testTableMetaConstructionAndGetName() {
        TableMeta tableMeta = new TableMeta(this.tableNode);
        String name = tableMeta.getName();
        assertEquals("test_table", name);
        // Check that the other attributes were extracted correctly as well (if needed)
        String comments = tableMeta.getComments();
        assertEquals("This is a test table.", comments);
        String remoteCatalog = tableMeta.getRemoteCatalog();
        assertEquals("test_catalog", remoteCatalog);
        String remoteSchema = tableMeta.getRemoteSchema();
        assertEquals("test_schema", remoteSchema);
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(1, columns.size());
        TableColumnMeta columnMeta = columns.get(0);
        assertEquals("test_column", columnMeta.getName());
    }

    @Test
    public void testTableMetaGetColumns() {
        TableMeta tableMeta = new TableMeta(this.tableNode);
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(1, columns.size());
        TableColumnMeta columnMeta = columns.get(0);
        assertEquals("test_column", columnMeta.getName());
    }

    @Test
    public void testTableMetaErrorScenarios() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element tableNode;

        // Create a new document with a table node having no 'name' attribute
        tableNode = doc.createElement("table");
        tableNode.setAttribute("remoteSchema", "test_schema");

        try {
            new TableMeta(tableNode);
            fail("Expected an IllegalArgumentException when the 'name' attribute is missing.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Create a new document with a table node having an empty 'name' attribute
        tableNode = doc.createElement("table");
        tableNode.setAttribute("name", "");
        tableNode.setAttribute("remoteSchema", "test_schema");

        try {
            new TableMeta(tableNode);
            fail("Expected an IllegalArgumentException when the 'name' attribute is empty.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Create a new document with a table node having an invalid 'name' attribute
        tableNode = doc.createElement("table");
        tableNode.setAttribute("name", "<script>alert('XSS')</script>");
        tableNode.setAttribute("remoteSchema", "test_schema");

        try {
            new TableMeta(tableNode);
            fail("Expected an IllegalArgumentException when the 'name' attribute contains invalid characters.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}