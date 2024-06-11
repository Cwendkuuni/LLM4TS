package net.sourceforge.schemaspy.model.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TableMetaTest {
    private Node tableNode;
    private TableMeta tableMeta;

    @BeforeEach
    public void setUp() {
        // Create a mock Node for testing
        Document doc = Document.newDocument();
        Element tableElement = doc.createElement("table");
        tableNode = tableElement;

        // Set attributes for testing
        tableElement.setAttribute("name", "testTableName");
        tableElement.setAttribute("comments", "testComments");
        tableElement.setAttribute("remoteCatalog", "testRemoteCatalog");
        tableElement.setAttribute("remoteSchema", "testRemoteSchema");

        NodeList columnNodes = doc.createElement("columnList").getChildNodes();
        for (int i = 0; i < 5; i++) {
            Element columnElement = doc.createElement("column");
            columnElement.setAttribute("name", "columnName" + i);
            columnNodes.item(i) = columnElement;
        }
        tableElement.appendChild(columnNodes);

        tableMeta = new TableMeta(tableNode);
    }

    @Test
    public void testGetName() {
        assertEquals("testTableName", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        assertEquals("testComments", tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        assertNotNull(tableMeta.getColumns());
        assertEquals(5, tableMeta.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
    }

    @Test
    public void testGetEmptyName() {
        Node emptyNameNode = doc.createAttribute("name");
        emptyNameNode.setNodeValue("");
        tableNode.setAttributeNode(emptyNameNode);
        TableMeta emptyNameTableMeta = new TableMeta(tableNode);
        assertEquals("", emptyNameTableMeta.getName());
    }

    @Test
    public void testGetEmptyComments() {
        Node emptyCommentsNode = doc.createAttribute("comments");
        tableNode.setAttributeNode(emptyCommentsNode);
        TableMeta emptyCommentsTableMeta = new TableMeta(tableNode);
        assertNull(emptyCommentsTableMeta.getComments());
    }

    @Test
    public void testGetEmptyColumns() {
        NodeList emptyColumnNodes = doc.createElement("columnList").getChildNodes();
        TableMeta emptyColumnsTableMeta = new TableMeta(tableNode);
        assertNotNull(emptyColumnsTableMeta.getColumns());
        assertEquals(0, emptyColumnsTableMeta.getColumns().size());
    }

    @Test
    public void testGetEmptyRemoteCatalog() {
        Node emptyRemoteCatalogNode = doc.createAttribute("remoteCatalog");
        tableNode.setAttributeNode(emptyRemoteCatalogNode);
        TableMeta emptyRemoteCatalogTableMeta = new TableMeta(tableNode);
        assertNull(emptyRemoteCatalogTableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetEmptyRemoteSchema() {
        Node emptyRemoteSchemaNode = doc.createAttribute("remoteSchema");
        tableNode.setAttributeNode(emptyRemoteSchemaNode);
        TableMeta emptyRemoteSchemaTableMeta = new TableMeta(tableNode);
        assertNull(emptyRemoteSchemaTableMeta.getRemoteSchema());
    }

    @Test
    public void testGetNullName() {
        Node nullNameNode = null;
        tableNode.setAttributeNode(nullNameNode);
        assertThrows(NullPointerException.class, () -> new TableMeta(tableNode));
    }

    @Test
    public void testGetNullComments() {
        Node nullCommentsNode = doc.createAttribute("comments");
        tableNode.setAttributeNode(nullCommentsNode);
        TableMeta nullCommentsTableMeta = new TableMeta(tableNode);
        assertNull(nullCommentsTableMeta.getComments());
    }

    @Test
    public void testGetNullColumns() {
        NodeList nullColumnsNodes = null;
        TableMeta nullColumnsTableMeta = new TableMeta(tableNode);
        assertThrows(NullPointerException.class, () -> nullColumnsTableMeta.getColumns());
    }

    @Test
    public void testGetNullRemoteCatalog() {
        Node nullRemoteCatalogNode = null;
        tableNode.setAttributeNode(nullRemoteCatalogNode);
        TableMeta nullRemoteCatalogTableMeta = new TableMeta(tableNode);
        assertNull(nullRemoteCatalogTableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetNullRemoteSchema() {
        Node nullRemoteSchemaNode = null;
        tableNode.setAttributeNode(nullRemoteSchemaNode);
        TableMeta nullRemoteSchemaTableMeta = new TableMeta(tableNode);
        assertNull(nullRemoteSchemaTableMeta.getRemoteSchema());
    }

    @Test
    public void testGetNullTableNode() {
        assertThrows(NullPointerException.class, () -> new TableMeta(null));
    }
}