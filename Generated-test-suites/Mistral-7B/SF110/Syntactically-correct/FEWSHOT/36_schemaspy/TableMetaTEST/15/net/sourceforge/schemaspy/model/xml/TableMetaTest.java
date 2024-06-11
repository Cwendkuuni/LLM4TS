package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.logging.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());
 private Node tableNode;

 @Before
 public void setup() {
 tableNode = mock(Node.class);
 when(tableNode.getNodeName()).thenReturn("table");
 when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 }

 @Test
 public void testTableMetaInstance() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("table", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 TableMeta.logger.fine(eq("Found XML table metadata for ").and(eq("table"))).verify(logger, atLeastOnce());
 }

 @Test
 public void testTableMetaInstanceWithComments() {
 Node commentsNode = mock(Node.class);
 when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);
 when(commentsNode.getNodeValue()).thenReturn("Test comments");
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("Test comments", tableMeta.getComments());
 }

 @Test
 public void testTableMetaInstanceWithRemoteCatalogAndSchema() {
 Node remoteSchemaNode = mock(Node.class);
 Node remoteCatalogNode = mock(Node.class);
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 when(remoteSchemaNode.getNodeValue()).thenReturn("TestSchema");
 when(remoteCatalogNode.getNodeValue()).thenReturn("TestCatalog");
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("TestCatalog", tableMeta.getRemoteCatalog());
 assertEquals("TestSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaInstanceWithNullTableNode() {
 TableMeta tableMeta = new TableMeta(null);
 fail("TableMeta should not be instantiated with null tableNode");
 }

 @Test
 public void testGetColumns() {
 Node columnNodes = mock(NodeList.class);
 when(tableNode.getChildNodes()).thenReturn(columnNodes);
 when(columnNodes.getLength()).thenReturn(2);
 Node colNode1 = mock(Node.class);
 Node colNode2 = mock(Node.class);
 when(columnNodes.item(0)).thenReturn(colNode1);
 when(columnNodes.item(1)).thenReturn(colNode2);
 TableColumnMeta colMeta1 = new TableColumnMeta(colNode1);
 TableColumnMeta colMeta2 = new TableColumnMeta(colNode2);
 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertEquals(colMeta1, columns.get(0));
 assertEquals(colMeta2, columns.get(1));
 }
}