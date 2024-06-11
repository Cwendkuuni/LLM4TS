package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

    @Test
    public void testTableMetaConstruction() {
        Document doc = createDocument();
        Element tableElement = doc.createElement("table");
        tableElement.setAttribute("name", "table1");
        tableElement.setAttribute("comments", "This is a test table.");
        tableElement.setAttribute("remoteSchema", "testSchema");
        tableElement.setAttribute("remoteCatalog", "testCatalog");

        NodeList columnNodes = doc.createElement("columnList").getChildNodes();
        for (int i = 0; i < 3; i++) {
            Element columnElement = doc.createElement("column");
            columnElement.setAttribute("name", "column" + i);
            columnElement.setAttribute("type", "int");
            columnNodes.item(i) = columnElement;
        }

        tableElement.appendChild(columnNodes);
        TableMeta tableMeta = new TableMeta(tableElement);

        assertEquals("table1", tableMeta.getName());
        assertEquals("This is a test table.", tableMeta.getComments());
        assertNotNull(tableMeta.getColumns());
        assertEquals(3, tableMeta.getColumns().size());

        TableColumnMeta column1 = tableMeta.getColumns().get(0);
        assertEquals("column0", column1.getName());
        assertEquals("int", column1.getType());

        TableColumnMeta column2 = tableMeta.getColumns().get(1);
        assertEquals("column1", column2.getName());
        assertEquals("int", column2.getType());

        TableColumnMeta column3 = tableMeta.getColumns().get(2);
        assertEquals("column2", column3.getName());
        assertEquals("int", column3.getType());

        assertEquals("testCatalog", tableMeta.getRemoteCatalog());
        assertEquals("testSchema", tableMeta.getRemoteSchema());

        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());

        TableMeta tableMeta2 = new TableMeta(doc.createElement("table"));
        assertNull(tableMeta2.getName());
        assertNull(tableMeta2.getComments());
        assertNull(tableMeta2.getColumns());
        assertNull(tableMeta2.getRemoteCatalog());
        assertNull(tableMeta2.getRemoteSchema());
    }

    @Test
    public void testEmptyTableMetaConstruction() {
        Document doc = createDocument();
        TableMeta tableMeta = new TableMeta(doc.createElement("table"));
        assertNull(tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertNull(tableMeta.getColumns());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }

    @Test
    public void testTableMetaLogging() {
        Document doc = createDocument();
        Element tableElement = doc.createElement("table");
        tableElement.setAttribute("name", "table1");
        TableMeta tableMeta = new TableMeta(tableElement);
        TableMeta.logger.fine("Found XML table metadata for table1 remoteCatalog: null remoteSchema: null comments: null");
        assertTrue(TableMeta.logger.isLoggable(Level.FINE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTableNode() {
        TableMeta tableMeta = new TableMeta(null);
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            doc = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }
}