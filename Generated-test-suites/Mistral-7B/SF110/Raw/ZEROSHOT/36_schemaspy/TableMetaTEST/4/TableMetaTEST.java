package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<tables>\n" +
         "<table name=\"testTable\" comments=\"This is a test table\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">\n" +
         "<columns>\n" +
         "<column name=\"column1\" type=\"int\" comments=\"Test column 1\"/>\n" +
         "<column name=\"column2\" type=\"varchar(10)\" comments=\"Test column 2\"/>\n" +
         "</columns>\n" +
         "</table>\n" +
         "</tables>";

 private Node tableNode;

 @Before
 public void setUp() {
 DOMParser parser = new DOMParser();
 try {
 parser.parseFromString(XML_TABLE_META_DATA, "text/xml");
 Element docElement = (Element)parser.getDocumentElement();
 tableNode = docElement.getElementsByTagName("table").item(0);
 } catch (Exception e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testTableMetaConstructor() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 TableColumnMeta column1 = columns.get(0);
 assertEquals("column1", column1.getName());
 assertEquals("Test column 1", column1.getComments());
 TableColumnMeta column2 = columns.get(1);
 assertEquals("column2", column2.getName());
 assertEquals("Test column 2", column2.getComments());
 assertEquals("testCatalog", tableMeta.getRemoteCatalog());
 assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructorWithNullTableNode() {
 new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructorWithEmptyTableNode() {
 Element emptyTableNode = new Element("table");
 new TableMeta(emptyTableNode);
 }

 @Test(expected = NullPointerException.class)
 public void testGetColumnsWhenColumnsIsNull() {
 TableMeta tableMeta = new TableMeta(new Element("table"));
 assertNull(tableMeta.getColumns());
 }

 @Test
 public void testGetColumns() {
 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 }

 @Test
 public void testGetName() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("testTable", tableMeta.getName());
 }

 @Test
 public void testGetComments() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("This is a test table", tableMeta.getComments());
 }

 @Test
 public void testGetRemoteCatalog() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("testCatalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchema() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("testSchema", tableMeta.getRemoteSchema());
 }
}