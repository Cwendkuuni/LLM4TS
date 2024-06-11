package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.w3c.dom.*;
import static org.mockito.Mockito.*;;

public class TableMetaTest {

 private static final String TABLE_NAME = "testTable";
 private static final String TABLE_COMMENTS = "This is a test table.";
 private static final String TABLE_REMOTE_CATALOG = "remoteCatalog";
 private static final String TABLE_REMOTE_SCHEMA = "remoteSchema";
 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 tableNode = mock(Node.class);
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstruction() {
 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 assertEmpty(tableMeta.getColumns());
 }

 @Test
 public void testTableMetaCommentsAreTrimmed() {
 tableNode = createMockNode("comments", TABLE_COMMENTS);
 tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_COMMENTS, tableMeta.getComments());
 }

 @Test
 public void testTableMetaCommentsAreNull() {
 tableNode = createMockNode("comments", "");
 tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaColumnsArePopulated() {
 tableNode = createMockNode("columnNodes", "column1, column2, column3");
 when(tableNode.getChildNodes()).thenReturn(createMockNodeList(createMockNode("column", "column1"), createMockNode("column", "column2"), createMockNode("column", "column3")));
 tableMeta = new TableMeta(tableNode);
 assertEquals(3, tableMeta.getColumns().size());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaThrowsExceptionIfTableNodeIsNull() {
 tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaThrowsExceptionIfTableNodeHasNoNameAttribute() {
 tableNode = createMockNode("tableNodeWithoutNameAttribute", "");
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaLoggerIsInitialized() {
 assertNotNull(TableMeta.logger);
 }

 @Test
 private Node createMockNode(String tagName, String textContent) {
 Node node = mock(Node.class);
 when(node.getNodeName()).thenReturn(tagName);
 when(node.getNodeValue()).thenReturn(textContent);
 return node;
 }

 @Test
 private NodeList createMockNodeList(Node... nodes) {
 NodeList nodeList = mock(NodeList.class);
 when(nodeList.getLength()).thenReturn((short) nodes.length);
 when(nodeList.item(anyInt())).thenReturn(nodes[0]);
 return nodeList;
 }
}