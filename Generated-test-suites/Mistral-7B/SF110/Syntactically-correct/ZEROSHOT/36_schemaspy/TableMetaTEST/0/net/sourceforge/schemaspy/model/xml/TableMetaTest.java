package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TableMetaTest class to test TableMeta methods.
 */
public class TableMetaTest {

    @Test
    public void testTableMetaInstance() {
        Node tableNode = createTestTableMetaNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNotNull(tableMeta);
        assertEquals("Expected table metadata name", "ExpectedTableName", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
        assertEquals("Expected number of columns", 3, tableMeta.getColumns().size());
    }

    @Test
    public void testTableMetaComments() {
        Node tableNode = createTestTableMetaNodeWithComments();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNotNull(tableMeta);
        assertEquals("Expected table metadata name", "ExpectedTableName", tableMeta.getName());
        assertEquals("Expected table metadata comments", "ExpectedComments", tableMeta.getComments());
    }

    @Test
    public void testTableMetaRemoteCatalogAndSchema() {
        Node tableNode = createTestTableMetaNodeWithRemoteCatalogAndSchema();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNotNull(tableMeta);
        assertEquals("Expected table metadata name", "ExpectedTableName", tableMeta.getName());
        assertEquals("Expected table metadata remoteCatalog", "ExpectedRemoteCatalog", tableMeta.getRemoteCatalog());
        assertEquals("Expected table metadata remoteSchema", "ExpectedRemoteSchema", tableMeta.getRemoteSchema());
    }

    @Test
    public void testTableMetaColumns() {
        Node tableNode = createTestTableMetaNodeWithColumns();
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertNotNull(columns);
        assertEquals("Expected number of columns", 3, columns.size());
        TableColumnMeta firstColumn = columns.get(0);
        assertEquals("Expected first column name", "ExpectedColumnName1", firstColumn.getName());
        TableColumnMeta secondColumn = columns.get(1);
        assertEquals("Expected second column name", "ExpectedColumnName2", secondColumn.getName());
        TableColumnMeta thirdColumn = columns.get(2);
        assertEquals("Expected third column name", "ExpectedColumnName3", thirdColumn.getName());
    }

    private Node createTestTableMetaNode() {
        Element tableElement = new Element("table");
        tableElement.setAttribute("name", "ExpectedTableName");
        return tableElement;
    }

    private Node createTestTableMetaNodeWithComments() {
        Element tableElement = new Element("table");
        tableElement.setAttribute("name", "ExpectedTableName");
        tableElement.setAttribute("comments", "ExpectedComments");
        return tableElement;
    }

    private Node createTestTableMetaNodeWithRemoteCatalogAndSchema() {
        Element tableElement = new Element("table");
        tableElement.setAttribute("name", "ExpectedTableName");
        tableElement.setAttribute("remoteSchema", "ExpectedRemoteSchema");
        tableElement.setAttribute("remoteCatalog", "ExpectedRemoteCatalog");
        return tableElement;
    }

    private Node createTestTableMetaNodeWithColumns() {
        Element tableElement = new Element("table");
        tableElement.setAttribute("name", "ExpectedTableName");

        Node column1Element = new Element("column");
        column1Element.setAttribute("name", "ExpectedColumnName1");
        tableElement.appendChild(column1Element);

        Node column2Element = new Element("column");
        column2Element.setAttribute("name", "ExpectedColumnName2");
        tableElement.appendChild(column2Element);

        Node column3Element = new Element("column");
        column3Element.setAttribute("name", "ExpectedColumnName3");
        tableElement.appendChild(column3Element);

        return tableElement;
    }
}