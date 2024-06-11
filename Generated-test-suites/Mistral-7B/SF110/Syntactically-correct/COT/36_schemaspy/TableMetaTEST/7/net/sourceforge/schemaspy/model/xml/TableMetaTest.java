package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;
import org.w3c.dom.*;
import static org.mockito.Mockito.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"testTable\" comments=\"This is a test table\" remoteCatalog=\"catalog1\" remoteSchema=\"schema1\">\n" +
         "\t<Column name=\"col1\" dataType=\"INT\" comments=\"Column 1\"" +
         "\t\tcolumnSize=\"10\" nullable=\"true\"/>\n" +
         "\t<Column name=\"col2\" dataType=\"VARCHAR\" comments=\"Column 2\"" +
         "\t\tcolumnSize=\"20\" nullable=\"false\"/>\n" +
         "</Table>";

 @BeforeClass
 static public void setupClass() {
 Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
 }

 @AfterClass
 static public void tearDownClass() {
 Logger.getLogger(TableMeta.class.getName()).setLevel(Level.SEVERE);
 }

 @Test
 public void testTableMetaConstruction() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NodeList columnNodes = mock(NodeList.class);
 when(columnNodes.getLength()).thenReturn(2);
 when(columnNodes.item(0)).thenReturn(mock(Node.class));
 when(columnNodes.item(1)).thenReturn(mock(Node.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class));
 when(attribs.getNamedItem("comments")).thenReturn(mock(Node.class));
 when(attribs.getNamedItem("remoteSchema")).thenReturn(mock(Node.class));
 when(attribs.getNamedItem("remoteCatalog")).thenReturn(mock(Node.class));

 Node tableNode = mock(Node.class);
 when(tableNode).thenReturn(xmlTableNode);

 TableMeta tableMeta = new TableMeta(tableNode);
 String name = tableMeta.getName();
 assertEquals("testTable", name);

 String comments = tableMeta.getComments();
 assertNull(comments);

 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 }

 @Test
 public void testTableMetaComments() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class));
 when(attribs.getNamedItem("comments")).thenReturn(mock(Node.class, "This is a test table comment"));

 Node tableNode = mock(Node.class);
 when(tableNode).thenReturn(xmlTableNode);

 TableMeta tableMeta = new TableMeta(tableNode);
 String comments = tableMeta.getComments();
 assertEquals("This is a test table comment", comments);
 }

 @Test
 public void testTableMetaColumnsEmpty() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class));

 Node tableNode = mock(Node.class);
 when(tableNode).thenReturn(xmlTableNode);

 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertTrue(columns.isEmpty());
 }

 @Test
 public void testTableMetaColumnsNotNull() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class));

 NodeList columnNodes = mock(NodeList.class);
 when(columnNodes.getLength()).thenReturn(1);
 when(columnNodes.item(0)).thenReturn(mock(Node.class));

 Node tableNode = mock(Node.class);
 when(tableNode).thenReturn(xmlTableNode);

 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertFalse(columns.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaNullTableNode() {
 TableMeta tableMeta = new TableMeta(null);
 fail("Should throw a NullPointerException");
 }

 @Test
 public void testTableMetaNullColumns() {
 TableMeta tableMeta = new TableMeta(mock(Node.class));
 tableMeta.setColumns(null);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertTrue(columns.isEmpty());
 }

 @Test
 public void testTableMetaSetColumns() {
 TableMeta tableMeta = new TableMeta(mock(Node.class));
 List<TableColumnMeta> columns = Arrays.asList(new TableColumnMeta(mock(Node.class)), new TableColumnMeta(mock(Node.class)));
 tableMeta.setColumns(columns);
 List<TableColumnMeta> tableMetaColumns = tableMeta.getColumns();
 assertEquals(2, tableMetaColumns.size());
 }

 @Test
 public void testTableMetaGetters() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class, "testTable"));
 when(attribs.getNamedItem("comments")).thenReturn(mock(Node.class, "This is a test table comment"));

 Node remoteCatalogNode = mock(Node.class, "remoteCatalogNode");
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 when(remoteCatalogNode.getNodeValue()).thenReturn("catalog1");

 Node remoteSchemaNode = mock(Node.class, "remoteSchemaNode");
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 when(remoteSchemaNode.getNodeValue()).thenReturn("schema1");

 Node columnNode1 = mock(Node.class);
 Node columnNode2 = mock(Node.class);

 NodeList columnNodes = mock(NodeList.class);
 when(columnNodes.getLength()).thenReturn(2);
 when(columnNodes.item(0)).thenReturn(columnNode1);
 when(columnNodes.item(1)).thenReturn(columnNode2);

 when(xmlTableNode.getChildNodes()).thenReturn(columnNodes);

 TableMeta tableMeta = new TableMeta(xmlTableNode);
 String name = tableMeta.getName();
 assertEquals("testTable", name);

 String comments = tableMeta.getComments();
 assertEquals("This is a test table comment", comments);

 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 }

 @Test
 public void testTableMetaToString() {
 TableMeta tableMeta = new TableMeta(mock(Node.class));
 String toString = tableMeta.toString();
 assertTrue(toString.contains("TableMeta{name=null, comments=null, columns=[]}"));
 }

 @Test
 public void testTableMetaWithEmptyComments() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class));
 when(attribs.getNamedItem("comments")).thenReturn(mock(Node.class, ""));

 Node tableNode = mock(Node.class);
 when(tableNode).thenReturn(xmlTableNode);

 TableMeta tableMeta = new TableMeta(tableNode);
 String comments = tableMeta.getComments();
 assertEquals("", comments);
 }

 @Test
 public void testTableMetaWithNullRemoteCatalogAndSchema() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class, "testTable"));
 when(attribs.getNamedItem("remoteCatalog")).thenReturn(null);
 when(attribs.getNamedItem("remoteSchema")).thenReturn(null);

 Node tableNode = mock(Node.class);
 when(tableNode).thenReturn(xmlTableNode);

 TableMeta tableMeta = new TableMeta(tableNode);
 String remoteCatalog = tableMeta.getRemoteCatalog();
 assertNull(remoteCatalog);

 String remoteSchema = tableMeta.getRemoteSchema();
 assertNull(remoteSchema);
 }

 @Test
 public void testTableMetaWithRemoteCatalogAndSchema() {
 Node xmlTableNode = mock(Node.class);
 when(xmlTableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(xmlTableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 NamedNodeMap attribs = mock(NamedNodeMap.class);
 when(xmlTableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(mock(Node.class, "testTable"));
 when(attribs.getNamedItem("remoteCatalog")).thenReturn(mock(Node.class, "catalog1"));
 when(attribs.getNamedItem("remoteSchema")).thenReturn(mock(Node.class, "schema1"));

 Node tableNode = mock(Node.class);
 when(tableNode).thenReturn(xmlTableNode);

 TableMeta tableMeta = new TableMeta(tableNode);
 String remoteCatalog = tableMeta.getRemoteCatalog();
 assertEquals("catalog1", remoteCatalog);

 String remoteSchema = tableMeta.getRemoteSchema();
 assertEquals("schema1", remoteSchema);
 }
}