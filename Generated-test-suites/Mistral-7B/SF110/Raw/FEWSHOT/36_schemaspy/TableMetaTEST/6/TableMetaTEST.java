package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.logging.*;
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
 private Logger logger;

 @Before
 public void setUp() {
 logger = Logger.getLogger(TableMeta.class.getName());
 logger.setLevel(Level.FINE);
 tableNode = mock(Node.class);
 when(tableNode.getNodeName()).thenReturn("table");
 when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(tableNode.getChildNodes()).thenReturn(mock(NodeList.class));

 TableMeta.logger.setLevel(Level.FINE);
 }

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEquals(0, tableMeta.getColumns().size());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta.logger.fine("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test
 public void testTableMetaConstructionWithComments() {
 final Node commentsNode = mock(Node.class);
 when(tableNode.getChildNodes().item(0)).thenReturn(commentsNode);
 when(commentsNode.getNodeName()).thenReturn("comments");
 when(commentsNode.getNodeValue()).thenReturn(TABLE_COMMENTS);
 tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertEquals(TABLE_COMMENTS, tableMeta.getComments());
 assertEquals(0, tableMeta.getColumns().size());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta.logger.fine("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: null remoteSchema: null comments: " + TABLE_COMMENTS);
 }

 @Test
 public void testTableMetaConstructionWithRemoteSchemaAndCatalog() {
 final Node remoteSchemaNode = mock(Node.class);
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 when(remoteSchemaNode.getNodeValue()).thenReturn(TABLE_REMOTE_SCHEMA);
 final Node remoteCatalogNode = mock(Node.class);
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 when(remoteCatalogNode.getNodeValue()).thenReturn(TABLE_REMOTE_CATALOG);
 tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEquals(0, tableMeta.getColumns().size());
 assertEquals(TABLE_REMOTE_CATALOG, tableMeta.getRemoteCatalog());
 assertEquals(TABLE_REMOTE_SCHEMA, tableMeta.getRemoteSchema());
 TableMeta.logger.fine("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: " + TABLE_REMOTE_CATALOG + " remoteSchema: " + TABLE_REMOTE_SCHEMA + " comments: null");
 }

 @Test
 public void testTableMetaConstructionWithColumns() {
 final Node columnNode1 = mock(Node.class);
 when(tableNode.getChildNodes().item(0)).thenReturn(columnNode1);
 when(columnNode1.getNodeName()).thenReturn("column");
 final TableColumnMeta columnMeta1 = mock(TableColumnMeta.class);
 when(new TableColumnMeta(columnNode1)).thenReturn(columnMeta1);
 when(tableNode.getChildNodes()).thenReturn(new NodeList(){
 @Override public Node item(int index) {
 if (index == 0) {
 return columnNode1;
 }
 return null;
 }

 @Override public int getLength() {
 return 1;
 }
 });
 tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEquals(1, tableMeta.getColumns().size());
 assertEquals(columnMeta1, tableMeta.getColumns().get(0));
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta.logger.fine("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test
 public void testTableMetaConstructionWithMultipleColumns() {
 final Node columnNode1 = mock(Node.class);
 when(tableNode.getChildNodes().item(0)).thenReturn(columnNode1);
 when(columnNode1.getNodeName()).thenReturn("column");
 final TableColumnMeta columnMeta1 = mock(TableColumnMeta.class);
 when(new TableColumnMeta(columnNode1)).thenReturn(columnMeta1);
 final Node columnNode2 = mock(Node.class);
 when(tableNode.getChildNodes().item(1)).thenReturn(columnNode2);
 when(columnNode2.getNodeName()).thenReturn("column");
 final TableColumnMeta columnMeta2 = mock(TableColumnMeta.class);
 when(new TableColumnMeta(columnNode2)).thenReturn(columnMeta2);
 when(tableNode.getChildNodes()).thenReturn(new NodeList(){
 @Override public Node item(int index) {
 if (index < 2) {
 return index == 0 ? columnNode1 : columnNode2;
 }
 return null;
 }

 @Override public int getLength() {
 return 2;
 }
 });
 tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEquals(2, tableMeta.getColumns().size());
 assertEquals(columnMeta1, tableMeta.getColumns().get(0));
 assertEquals(columnMeta2, tableMeta.getColumns().get(1));
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta.logger.fine("Found XML table metadata for " + TABLE_NAME + " remoteCatalog: null remoteSchema: null comments: null");
 }
}