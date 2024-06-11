package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(createMockTableNode());
 assertEquals("tableName", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 assertFalse(tableMeta.getColumns().isEmpty());
 logger.fine("Found XML table metadata for tableName");
 }

 @Test
 public void testTableMetaComments() {
 Node commentsNode = createMockCommentNode("table comments");
 TableMeta tableMeta = new TableMeta(createMockTableNode(commentsNode));
 assertEquals("table comments", tableMeta.getComments());
 }

 @Test
 public void testTableMetaNoComments() {
 TableMeta tableMeta = new TableMeta(createMockTableNode());
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaRemoteCatalogAndSchema() {
 Node remoteCatalogNode = createMockNode("remoteCatalog", "value1");
 Node remoteSchemaNode = createMockNode("remoteSchema", "value2");
 TableMeta tableMeta = new TableMeta(createMockTableNode(remoteCatalogNode, remoteSchemaNode));
 assertEquals("value1", tableMeta.getRemoteCatalog());
 assertEquals("value2", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaNoRemoteCatalogOrSchema() {
 TableMeta tableMeta = new TableMeta(createMockTableNode());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaColumns() {
 List<TableColumnMeta> columns = new ArrayList<>();
 columns.add(new TableColumnMeta(createMockColumnNode()));
 TableMeta tableMeta = new TableMeta(createMockTableNode(createMockColumnNodes(columns)));
 assertEquals(columns, tableMeta.getColumns());
 }

 @Test
 public void testTableMetaEmptyColumns() {
 TableMeta tableMeta = new TableMeta(createMockTableNode());
 assertTrue(tableMeta.getColumns().isEmpty());
 }

 private Node createMockTableNode() {
 Element tableNode = createMockElement("table");
 return tableNode;
 }

 private Node createMockTableNode(Node... nodes) {
 Element tableNode = createMockElement("table");
 for (Node node : nodes) {
 tableNode.appendChild(node);
 }
 return tableNode;
 }

 private Node createMockCommentNode(String text) {
 Element commentNode = createMockElement("comments");
 commentNode.setTextContent(text);
 return commentNode;
 }

 private Node createMockColumnNode() {
 Element columnNode = createMockElement("column");
 return columnNode;
 }

 private Node createMockColumnNodes(Collection<TableColumnMeta> columns) {
 Element columnNodes = createMockElement("columns");
 for (TableColumnMeta columnMeta : columns) {
 Element columnNode = createMockElement("column");
 columnNode.appendChild(createMockElement("columnname").setTextContent(columnMeta.getName()));
 columnNodes.appendChild(columnNode);
 }
 return columnNodes;
 }

 private Element createMockElement(String tagName) {
 Element element = mock(Element.class);
 when(element.getTagName()).thenReturn(tagName);
 return element;
 }

}