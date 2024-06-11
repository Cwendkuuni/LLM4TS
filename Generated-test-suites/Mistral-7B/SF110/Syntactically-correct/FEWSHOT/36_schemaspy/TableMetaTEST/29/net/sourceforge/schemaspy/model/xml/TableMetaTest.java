package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String TABLE_NAME = "testTable";
 private static final String TABLE_COMMENTS = "This is a test table.";
 private static final String TABLE_REMOTE_CATALOG = "testCatalog";
 private static final String TABLE_REMOTE_SCHEMA = "testSchema";
 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setup() {
 tableNode = mock(Node.class);
 when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(tableNode.getNodeName()).thenReturn("table");

 Node commentsNode = mock(Node.class);
 Node remoteSchemaNode = mock(Node.class);
 Node remoteCatalogNode = mock(Node.class);

 NodeList columnNodes = new NodeList() {
 @Override
 public int getLength() {
 return 3;
 }

 @Override
 public Node item(int index) {
 return mock(Node.class);
 }
 };

 doReturn(commentsNode).when(tableNode.getAttributes()).getNamedItem("comments");
 doReturn(remoteSchemaNode).when(tableNode.getAttributes()).getNamedItem("remoteSchema");
 doReturn(remoteCatalogNode).when(tableNode.getAttributes()).getNamedItem("remoteCatalog");
 doReturn(columnNodes).when(tableNode).getChildNodes();

 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstruction() {
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta.logger.fine(eq("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: null remoteSchema: null comments: null"));
 }

 @Test
 public void testTableMetaWithComments() {
 String comments = TABLE_COMMENTS;
 doReturn(commentsNode).when(tableNode.getAttributes()).getNamedItem("comments");
 tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertEquals(TABLE_COMMENTS, tableMeta.getComments());
 TableMeta.logger.fine(eq("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: null remoteSchema: null comments: " + TABLE_COMMENTS));
 }

 @Test
 public void testTableMetaWithRemoteSchemaAndRemoteCatalog() {
 doReturn(remoteCatalogNode).when(tableNode.getAttributes()).getNamedItem("remoteCatalog");
 doReturn(remoteSchemaNode).when(tableNode.getAttributes()).getNamedItem("remoteSchema");
 tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEquals(TABLE_REMOTE_CATALOG, tableMeta.getRemoteCatalog());
 assertEquals(TABLE_REMOTE_SCHEMA, tableMeta.getRemoteSchema());
 TableMeta.logger.fine(eq("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: " + TABLE_REMOTE_CATALOG + " remoteSchema: " + TABLE_REMOTE_SCHEMA + " comments: null"));
 }

 @Test
 public void testGetColumns() {
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(3, columns.size());
 }
}