package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TableMetaTest {

 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setup() {
 NodeList columnNodes = createMock(NodeList.class);
 when(columnNodes.getLength()).thenReturn(2);
 when(columnNodes.getItem(0)).thenReturn(createMock(Node.class));
 when(columnNodes.getItem(1)).thenReturn(createMock(Node.class));
 tableNode = createMock(Node.class);
 when(tableNode.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
 when(tableNode.getChildNodes()).thenReturn(columnNodes);
 }

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("name", "Table1", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaComments() {
 final String comments = "This table contains information about users.";
 Node commentsNode = createMock(Node.class);
 when(commentsNode.getNodeValue()).thenReturn(comments);
 when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testTableMetaColumns() {
 final Node columnNode1 = createMock(Node.class);
 final Node columnNode2 = createMock(Node.class);
 TableColumnMeta columnMeta1 = new TableColumnMeta(columnNode1);
 TableColumnMeta columnMeta2 = new TableColumnMeta(columnNode2);
 when(columnNodes.getItem(0)).thenReturn(columnNode1);
 when(columnNodes.getItem(1)).thenReturn(columnNode2);
 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertEquals(columnMeta1, columns.get(0));
 assertEquals(columnMeta2, columns.get(1));
 }

 @Test
 public void testTableMetaRemoteCatalogAndSchema() {
 final String remoteCatalog = "catalog1";
 final String remoteSchema = "schema1";
 Node remoteCatalogNode = createMock(Node.class);
 Node remoteSchemaNode = createMock(Node.class);
 when(remoteCatalogNode.getNodeValue()).thenReturn(remoteCatalog);
 when(remoteSchemaNode.getNodeValue()).thenReturn(remoteSchema);
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }
}