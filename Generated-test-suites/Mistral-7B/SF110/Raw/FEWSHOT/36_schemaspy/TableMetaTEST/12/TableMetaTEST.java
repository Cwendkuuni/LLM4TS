package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = mock(Logger.class);
 private static final String TABLE_NAME = "testTable";
 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 tableNode = mock(Node.class);
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstructor() {
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGetName() {
 String tableName = "testTableName";
 when(tableNode.getAttributes().getNamedItem("name")).thenReturn(createNamedNode("name", tableName));
 assertEquals(tableName, tableMeta.getName());
 }

 @Test
 public void testTableMetaGetComments() {
 String comments = "test comments";
 when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(createNamedNode("comments", comments));
 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testTableMetaGetEmptyComments() {
 when(tableNode.getAttributes().getNamedItem("comments")).thenReturn(null);
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaGetRemoteCatalog() {
 String remoteCatalog = "testRemoteCatalog";
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(createNamedNode("remoteCatalog", remoteCatalog));
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaGetEmptyRemoteCatalog() {
 when(tableNode.getAttributes().getNamedItem("remoteCatalog")).thenReturn(null);
 assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaGetRemoteSchema() {
 String remoteSchema = "testRemoteSchema";
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(createNamedNode("remoteSchema", remoteSchema));
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGetEmptyRemoteSchema() {
 when(tableNode.getAttributes().getNamedItem("remoteSchema")).thenReturn(null);
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGetColumns() {
 List<TableColumnMeta> columns = new ArrayList<>();
 columns.add(new TableColumnMeta(mock(Node.class)));
 when(tableNode.getChildNodes()).thenReturn(createNodeList(columns.toArray(new Node[0])));
 assertEquals(columns, tableMeta.getColumns());
 }

 @Test
 public void testTableMetaGetEmptyColumns() {
 when(tableNode.getChildNodes()).thenReturn(createNodeList(new Node[0]));
 assertNotNull(tableMeta.getColumns());
 assertTrue(tableMeta.getColumns().isEmpty());
 }

 private Node createNodeList(Node[] nodes) {
 Node nodeList = mock(NodeList.class);
 when(nodeList.getLength()).thenReturn(nodes.length);
 when(nodeList.item(anyInt())).thenAnswer(i -> nodes[i]);
 return nodeList;
 }

 private Node createNamedNode(String nodeName, String nodeValue) {
 Node namedNode = mock(Node.class);
 when(namedNode.getNodeName()).thenReturn(nodeName);
 when(namedNode.getNodeValue()).thenReturn(nodeValue);
 return namedNode;
 }
}