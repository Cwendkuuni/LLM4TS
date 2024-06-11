package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setup() {
 tableNode = mock(Node.class);
 when(tableNode.getNodeName()).thenReturn("table");
 when(tableNode.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
 when(tableNode.getChildNodes()).thenReturn(createMock(NodeList.class));
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstruction() {
 assertEquals("table", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaName() {
 assertEquals("table", tableMeta.getName());
 }

 @Test
 public void testTableMetaComments() {
 String comments = "This is a test table";
 Node commentsNode = createMock(Node.class);
 when(commentsNode.getNodeValue()).thenReturn(comments);
 when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);
 tableMeta = new TableMeta(tableNode);
 assertEquals("This is a test table", tableMeta.getComments());
 }

 @Test
 public void testTableMetaColumns() {
 NodeList columnNodes = createMock(NodeList.class);
 when(columnNodes.getLength()).thenReturn(3);
 when(tableNode.getChildNodes()).thenReturn(columnNodes);
 TableColumnMeta column1 = new TableColumnMeta(createMock(Node.class));
 TableColumnMeta column2 = new TableColumnMeta(createMock(Node.class));
 TableColumnMeta column3 = new TableColumnMeta(createMock(Node.class));
 when(columnNodes.item(0)).thenReturn(column1.getNode());
 when(columnNodes.item(1)).thenReturn(column2.getNode());
 when(columnNodes.item(2)).thenReturn(column3.getNode());
 tableMeta = new TableMeta(tableNode);
 assertEquals(3, tableMeta.getColumns().size());
 }

 @Test
 public void testTableMetaRemoteCatalog() {
 String remoteCatalog = "test_catalog";
 Node remoteCatalogNode = createMock(Node.class);
 when(remoteCatalogNode.getNodeValue()).thenReturn(remoteCatalog);
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 tableMeta = new TableMeta(tableNode);
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaRemoteSchema() {
 String remoteSchema = "test_schema";
 Node remoteSchemaNode = createMock(Node.class);
 when(remoteSchemaNode.getNodeValue()).thenReturn(remoteSchema);
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 tableMeta = new TableMeta(tableNode);
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaLogger() {
 String tableName = "test_table";
 String remoteCatalog = "test_catalog";
 String remoteSchema = "test_schema";
 String comments = "This is a test table";
 TableMeta tableMeta = new TableMeta(tableNode);
 TableMeta.logger.fine("Found XML table metadata for " + tableName + " remoteCatalog: " + remoteCatalog + " remoteSchema: " + remoteSchema + " comments: " + comments);
 verify(TableMeta.logger, times(1)).fine("Found XML table metadata for " + tableName + " remoteCatalog: " + remoteCatalog + " remoteSchema: " + remoteSchema + " comments: " + comments);
 }
}