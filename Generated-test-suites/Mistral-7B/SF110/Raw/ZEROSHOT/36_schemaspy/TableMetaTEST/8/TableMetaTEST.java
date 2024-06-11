package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;
import static org.mockito.Mockito.*;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(createMockNode("table"));;

 assertEquals("name", "testTableName", tableMeta.getName());
 assertTrue("comments is null", tableMeta.getComments() == null);
 assertTrue("columns size is 0", tableMeta.getColumns().size() == 0);
 assertTrue("remoteCatalog is null", tableMeta.getRemoteCatalog() == null);
 assertTrue("remoteSchema is null", tableMeta.getRemoteSchema() == null);

 logger.fine("Found XML table metadata for testTableName remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test
 public void testTableMetaConstructionWithComments() {
 TableMeta tableMeta = new TableMeta(createMockNode("table", createMockNode("comments", "test Comments")));

 assertEquals("name", "testTableName", tableMeta.getName());
 assertEquals("comments", "test Comments", tableMeta.getComments());
 assertTrue("columns size is 0", tableMeta.getColumns().size() == 0);
 assertTrue("remoteCatalog is null", tableMeta.getRemoteCatalog() == null);
 assertTrue("remoteSchema is null", tableMeta.getRemoteSchema() == null);

 logger.fine("Found XML table metadata for testTableName remoteCatalog: null remoteSchema: null comments: test Comments");
 }

 @Test
 public void testTableMetaConstructionWithRemoteCatalogAndRemoteSchema() {
 TableMeta tableMeta = new TableMeta(createMockNode("table", createMockNode("remoteCatalog", "testCatalog"), createMockNode("remoteSchema", "testSchema")));

 assertEquals("name", "testTableName", tableMeta.getName());
 assertTrue("comments is null", tableMeta.getComments() == null);
 assertTrue("columns size is 0", tableMeta.getColumns().size() == 0);
 assertEquals("remoteCatalog", "testCatalog", tableMeta.getRemoteCatalog());
 assertEquals("remoteSchema", "testSchema", tableMeta.getRemoteSchema());

 logger.fine("Found XML table metadata for testTableName remoteCatalog: testCatalog remoteSchema: testSchema comments: null");
 }

 @Test
 public void testTableMetaConstructionWithColumns() {
 TableMeta tableMeta = new TableMeta(createMockNode("table", createColumnsListNode()));

 assertEquals("name", "testTableName", tableMeta.getName());
 assertTrue("comments is null", tableMeta.getComments() == null);
 assertTrue("columns size is greater than 0", tableMeta.getColumns().size() > 0);

 logger.fine("Found XML table metadata for testTableName remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test
 public void testGetters() {
 TableMeta tableMeta = new TableMeta(createMockNode("table", createColumnsListNode(), createMockNode("comments", "test Comments")));

 assertEquals("name", "testTableName", tableMeta.getName());
 assertEquals("comments", "test Comments", tableMeta.getComments());
 assertTrue("columns size is greater than 0", tableMeta.getColumns().size() > 0);
 assertEquals("remoteCatalog", "testCatalog", tableMeta.getRemoteCatalog());
 assertEquals("remoteSchema", "testSchema", tableMeta.getRemoteSchema());
 }

 @Test
 public void testLogger() {
 TableMeta tableMeta = new TableMeta(createMockNode("table"));

 logger.fine("Found XML table metadata for " + tableMeta.getName() + " remoteCatalog: null remoteSchema: null comments: null");
 }

 private Node createMockNode(String tagName, Node... children) {
 Element mockElement = mock(Element.class);
 when(mockElement.getTagName()).thenReturn(tagName);
 if (children != null) {
 for (Node child : children) {
 when(mockElement.getChildNodes()).thenReturn(createMockNodeList(child));
 }
 }
 return mockElement;
 }

 private NodeList createMockNodeList(Node node) {
 NodeList mockNodeList = mock(NodeList.class);
 when(mockNodeList.getLength()).thenReturn(1);
 when(mockNodeList.item(0)).thenReturn(node);
 return mockNodeList;
 }

 private Node createColumnsListNode() {
 NodeList columns = new NodeList() {
 @Override
 public Node item(int index) {
 return createMockNode("column");
 }

 @Override
 public int getLength() {
 return 3;
 }
 };
 return createMockNode("column-group", createMockNode("column-key", createMockNode("column-name", "column1")), columns);
 }
}