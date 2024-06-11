package com.example.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.w3c.dom.Attribute;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {

    @Test
    public void testgetName() {
        String name = "testTable";
        TableMeta tableMeta = new TableMeta(createTestTableNode("name", name, null, null, null, null, null, null));
        assertEquals(name, tableMeta.getName());
    }

    @Test
    public void testgetComments() {
        String comments = "This is a test table.";
        Node tableNode = createTestTableNode("name", null, "comments", comments, null, null, null, null);
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals(comments, tableMeta.getComments());
    }

    @Test
    public void testgetColumns() {
        String[] columnNames = {"column1", "column2"};
        TableMeta tableMeta = new TableMeta(createTestTableNode("name", null, null, null, null, null, null, createTestColumnNodes(columnNames)));
        List<TableColumnMeta> columns = tableMeta.getColumns();
        for (TableColumnMeta column : columns) {
            assertTrue(Arrays.asList(columnNames).contains(column.getName()));
        }
    }

    @Test
    public void testgetRemoteCatalog() {
        String remoteCatalog = "testCatalog";
        Node tableNode = createTestTableNode(null, null, null, null, null, null, "remoteCatalog", remoteCatalog);
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
    }

    @Test
    public void testgetRemoteSchema() {
        String remoteSchema = "testSchema";
        Node tableNode = createTestTableNode(null, null, null, null, null, null, "remoteSchema", remoteSchema);
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals(remoteSchema, tableMeta.getRemoteSchema());
    }

    // Helper method to create test nodes
    private Node createTestTableNode(String... attributes) {
        Element tableElement = new Element("table");
        for (int i = 0; i < attributes.length; i += 2) {
            Attribute attribute = new Attribute();
            attribute.setName(attributes[i]);
            attribute.setValue(attributes[i + 1]);
            tableElement.setAttributeNode(attribute);
        }
        return tableElement;
    }

    private NodeList createTestColumnNodes(String... names) {
        NodeList columnNodes = new NodeList();
        for (String name : names) {
            Element columnElement = new Element("column");
            Attribute nameAttribute = new Attribute();
            nameAttribute.setName("name");
            nameAttribute.setValue(name);
            columnElement.setAttributeNode(nameAttribute);
            columnNodes.add(columnElement);
        }
        return columnNodes;
    }

    @Test(expected = NullPointerException.class)
    public void testNullTableNode() {
        TableMeta tableMeta = new TableMeta(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingNameAttribute() {
        String[] attributes = new String[]{"remoteSchema", "testSchema"};
        Node tableNode = createTestTableNode(attributes);
        TableMeta tableMeta = new TableMeta(tableNode);
    }
}