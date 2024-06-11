package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = Mockito.mock(Logger.class);
 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setup() {
 tableNode = mock(Node.class);
 when(tableNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 when(tableNode.getChildNodes()).thenReturn(mock(NodeList.class));
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstruction() {
 assertEquals("tableName", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testGetName() {
 String tableName = "tableName";
 when(tableNode.getAttributes().getNamedItem("name")).thenReturn(mock(Attr.class));
 when(mock(Attr.class).getNodeValue()).thenReturn(tableName);
 assertEquals(tableName, tableMeta.getName());
 }

 @Test
 public void testGetComments() {
 String comments = "table comments";
 when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(mock(Attr.class));
 when(mock(Attr.class).getNodeValue()).thenReturn(comments);
 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testGetColumns() {
 List<TableColumnMeta> columns = new ArrayList<>();
 TableColumnMeta column1 = mock(TableColumnMeta.class);
 columns.add(column1);
 when(tableNode.getChildNodes().getLength()).thenReturn(1);
 when(tableNode.getChildNodes().item(0)).thenReturn(tableNode);
 when(tableNode.getNodeName()).thenReturn("table");
 when(tableNode.getAttributes().getNamedItem("name")).thenReturn(mock(Attr.class));
 when(mock(Attr.class).getNodeValue()).thenReturn("tableName");
 when(column1.getName()).thenReturn("columnName1");
 TableColumnMeta column2 = new TableColumnMeta(mock(Node.class));
 columns.add(column2);
 when(tableNode.getChildNodes().getItem(1)).thenReturn(tableNode);
 when(tableNode.getNodeName()).thenReturn("table");
 when(tableNode.getAttributes().getNamedItem("name")).thenReturn(mock(Attr.class));
 when(mock(Attr.class).getNodeValue()).thenReturn("tableName");
 when(column2.getName()).thenReturn("columnName2");
 assertEquals(columns, tableMeta.getColumns());
 }

 @Test
 public void testGetRemoteCatalog() {
 String remoteCatalog = "remoteCatalog";
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(mock(Attr.class));
 when(mock(Attr.class).getNodeValue()).thenReturn(remoteCatalog);
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchema() {
 String remoteSchema = "remoteSchema";
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(mock(Attr.class));
 when(mock(Attr.class).getNodeValue()).thenReturn(remoteSchema);
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test
 public void testLoggerFine() {
 String tableName = "tableName";
 String remoteCatalog = "remoteCatalog";
 String remoteSchema = "remoteSchema";
 String comments = "table comments";
 TableMeta tableMeta = new TableMeta(tableNode);
 TableMeta.logger.fine(tableMeta.toString());
 verify(TableMeta.logger, times(1)).fine(containsString("Found XML table metadata for " + tableName + " remoteCatalog: " + remoteCatalog + " remoteSchema: " + remoteSchema + " comments: " + comments));
 }
}