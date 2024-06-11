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
 TableMeta.logger = logger;
 NodeList columnNodes = createMock(NodeList.class);
 when(columnNodes.getLength()).thenReturn(2);
 Node columnNode1 = createMock(Node.class);
 Node columnNode2 = createMock(Node.class);
 when(columnNodes.item(0)).thenReturn(columnNode1);
 when(columnNodes.item(1)).thenReturn(columnNode2);

 tableNode = createMock(Node.class);
 when(tableNode.getNodeName()).thenReturn("table");
 when(tableNode.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
 when(tableNode.getChildNodes()).thenReturn(columnNodes);
 }

 @Test
 public void testTableMetaConstruction() {
 tableMeta = new TableMeta(tableNode);
 assertEquals("table", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 verify(logger).fine("Found XML table metadata for table remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test
 public void testTableMetaWithComments() {
 String comments = "Some comments";
 Node commentsNode = createMock(Node.class);
 when(commentsNode.getNodeValue()).thenReturn(comments);
 when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(commentsNode);
 tableMeta = new TableMeta(tableNode);
 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testTableMetaWithRemoteCatalogAndRemoteSchema() {
 String remoteCatalog = "remoteCatalog";
 String remoteSchema = "remoteSchema";
 Node remoteCatalogNode = createMock(Node.class);
 Node remoteSchemaNode = createMock(Node.class);
 when(remoteCatalogNode.getNodeValue()).thenReturn(remoteCatalog);
 when(remoteSchemaNode.getNodeValue()).thenReturn(remoteSchema);
 when(tableNode.getAttributes()).thenReturn(new AttributesImpl());
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
 tableMeta = new TableMeta(tableNode);
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test
 public void testGetColumns() {
 List<TableColumnMeta> expectedColumns = Arrays.asList(new TableColumnMeta(columnNode1), new TableColumnMeta(columnNode2));
 when(columnNode1.getNodeValue()).thenReturn("column1");
 when(columnNode2.getNodeValue()).thenReturn("column2");
 tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> actualColumns = tableMeta.getColumns();
 assertEquals(expectedColumns, actualColumns);
 }
}