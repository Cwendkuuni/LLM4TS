package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_NAME = "tableName";
 private static final String XML_TABLE_META_COMMENTS = "tableComments";
 private static final String XML_TABLE_META_REMOTE_SCHEMA = "remoteSchema";
 private static final String XML_TABLE_META_REMOTE_CATALOG = "remoteCatalog";
 private static final String XML_COLUMN_META_NAME = "columnName";

 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 tableNode = mock(Node.class);
 when(tableNode.getChildNodes()).thenReturn(createMock(NodeList.class));
 when(tableNode.getAttributes()).thenReturn(createMock(NamedNodeMap.class));

 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstructionWithValidXml() {
 NodeList columnNodes = createMock(NodeList.class);
 when(tableNode.getChildNodes()).thenReturn(columnNodes);
 when(columnNodes.getLength()).thenReturn(1);
 when(columnNodes.item(0)).thenReturn(createMock(Node.class));

 NamedNodeMap attribs = createMock(NamedNodeMap.class);
 when(tableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("name").getNodeValue()).thenReturn(XML_TABLE_META_NAME);
 when(attribs.getNamedItem("comments")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("comments")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("comments").getNodeValue()).thenReturn(XML_TABLE_META_COMMENTS);
 when(attribs.getNamedItem("remoteSchema")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("remoteSchema").getNodeValue()).thenReturn(XML_TABLE_META_REMOTE_SCHEMA);
 when(attribs.getNamedItem("remoteCatalog")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("remoteCatalog").getNodeValue()).thenReturn(XML_TABLE_META_REMOTE_CATALOG);

 TableColumnMeta columnMeta = mock(TableColumnMeta.class);
 when(columnNodes.item(0)).thenReturn(createMock(Node.class));
 when(columnNodes.item(0)).thenReturn(columnMeta);
 tableMeta.columns.add(columnMeta);

 assertEquals(XML_TABLE_META_NAME, tableMeta.getName());
 assertEquals(XML_TABLE_META_COMMENTS, tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNotNull(tableMeta.getRemoteCatalog());
 assertNotNull(tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullAttributes() {
 when(tableNode.getChildNodes()).thenReturn(createMock(NodeList.class));
 when(tableNode.getAttributes()).thenReturn(null);
 TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstructionWithEmptyComments() {
 NodeList columnNodes = createMock(NodeList.class);
 when(tableNode.getChildNodes()).thenReturn(columnNodes);
 when(columnNodes.getLength()).thenReturn(1);
 when(columnNodes.item(0)).thenReturn(createMock(Node.class));

 NamedNodeMap attribs = createMock(NamedNodeMap.class);
 when(tableNode.getAttributes()).thenReturn(attribs);
 when(attribs.getNamedItem("name")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("name").getNodeValue()).thenReturn(XML_TABLE_META_NAME);
 when(attribs.getNamedItem("comments")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("comments").getNodeValue()).thenReturn("");
 when(attribs.getNamedItem("remoteSchema")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("remoteSchema").getNodeValue()).thenReturn(XML_TABLE_META_REMOTE_SCHEMA);
 when(attribs.getNamedItem("remoteCatalog")).thenReturn(createMock(Node.class));
 when(attribs.getNamedItem("remoteCatalog").getNodeValue()).thenReturn(XML_TABLE_META_REMOTE_CATALOG);

 TableColumnMeta columnMeta = mock(TableColumnMeta.class);
 when(columnNodes.item(0)).thenReturn(createMock(Node.class));
 when(columnNodes.item(0)).thenReturn(columnMeta);
 tableMeta.columns.add(columnMeta);

 assertEquals(XML_TABLE_META_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNotNull(tableMeta.getRemoteCatalog());
 assertNotNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaLogMessage() {
 String tableName = "testTable";
 String remoteCatalog = "testCatalog";
 String remoteSchema = "testSchema";
 String comments = "testComments";

 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals(tableName, tableMeta.getName());
 assertEquals(comments, tableMeta.getComments());
 assertEquals(Collections.singletonList(tableMeta.getColumns().get(0)), tableMeta.getColumns());
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());

 TableMeta.logger.fine("Found XML table metadata for " + tableName + " remoteCatalog: " + remoteCatalog + " remoteSchema: " + remoteSchema + " comments: " + comments);
 }
}