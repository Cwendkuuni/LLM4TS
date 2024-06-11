package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());

 @Test
 public void testTableMetaCreation() {
 TableMeta tableMeta = new TableMeta(createTestTableNode());

 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());

 logger.finest("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test
 public void testTableMetaCreationWithComments() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithComments());

 assertEquals("testTable", tableMeta.getName());
 assertEquals("This is a test table.", tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());

 logger.finest("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: This is a test table.");
 }

 @Test
 public void testTableMetaCreationWithRemoteCatalogAndSchema() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithRemoteCatalogAndSchema());

 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertEquals("testSchema", tableMeta.getRemoteSchema());
 assertEquals("testCatalog", tableMeta.getRemoteCatalog());

 logger.finest("Found XML table metadata for testTable remoteCatalog: testCatalog remoteSchema: testSchema comments: null");
 }

 @Test
 public void testTableMetaCreationWithColumns() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithColumns());

 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());

 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());

 TableColumnMeta column1 = columns.get(0);
 assertEquals("column1", column1.getName());
 assertEquals("This is column 1.", column1.getComments());
 assertNull(column1.getDataType());

 TableColumnMeta column2 = columns.get(1);
 assertEquals("column2", column2.getName());
 assertEquals("This is column 2.", column2.getComments());
 assertNull(column2.getDataType());
 }

 @Test
 public void testTableMetaCreationWithColumnsAndDataType() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithColumnsAndDataType());

 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());

 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());

 TableColumnMeta column1 = columns.get(0);
 assertEquals("column1", column1.getName());
 assertEquals("This is column 1.", column1.getComments());
 assertEquals("VARCHAR", column1.getDataType());

 TableColumnMeta column2 = columns.get(1);
 assertEquals("column2", column2.getName());
 assertEquals("This is column 2.", column2.getComments());
 assertEquals("INT", column2.getDataType());
 }

 private Node createTestTableNode() {
 Element tableNode = createElement("table");
 setAttribute(tableNode, "name", "testTable");
 return tableNode;
 }

 private Node createTestTableNodeWithComments() {
 Element tableNode = createElement("table");
 setAttribute(tableNode, "name", "testTable");
 setAttribute(tableNode, "comments", "This is a test table.");
 return tableNode;
 }

 private Node createTestTableNodeWithRemoteCatalogAndSchema() {
 Element tableNode = createElement("table");
 setAttribute(tableNode, "name", "testTable");
 setAttribute(tableNode, "remoteCatalog", "testCatalog");
 setAttribute(tableNode, "remoteSchema", "testSchema");
 return tableNode;
 }

 private Node createTestTableNodeWithColumns() {
 Element tableNode = createElement("table");
 setAttribute(tableNode, "name", "testTable");
 Element columnNode1 = createElement("column");
 setAttribute(columnNode1, "name", "column1");
 Element commentsNode1 = createElement("comments");
 setTextNode(commentsNode1, "This is column 1.");
 setChild(columnNode1, commentsNode1);
 Element dataTypeNode1 = createElement("dataType");
 setTextNode(dataTypeNode1, "VARCHAR");
 setChild(columnNode1, dataTypeNode1);
 tableNode.appendChild(columnNode1);

 Element columnNode2 = createElement("column");
 setAttribute(columnNode2, "name", "column2");
 Element commentsNode2 = createElement("comments");
 setTextNode(commentsNode2, "This is column 2.");
 setChild(columnNode2, commentsNode2);
 Element dataTypeNode2 = createElement("dataType");
 setTextNode(dataTypeNode2, "INT");
 setChild(columnNode2, dataTypeNode2);
 tableNode.appendChild(columnNode2);
 return tableNode;
 }

 private Node createTestTableNodeWithColumnsAndDataType() {
 Element tableNode = createTestTableNodeWithColumns();
 Element column1 = tableNode.getElementsByTagName("column").item(0);
 Element dataTypeElement1 = column1.getElementsByTagName("dataType").item(0);
 setTextNode(dataTypeElement1, "VARCHAR");
 Element column2 = tableNode.getElementsByTagName("column").item(1);
 Element dataTypeElement2 = column2.getElementsByTagName("dataType").item(0);
 setTextNode(dataTypeElement2, "INT");
 return tableNode;
 }

 private Element createElement(String tagName) {
 Document document = new DocumentImpl();
 return document.createElement(tagName);
 }

 private void setAttribute(Element element, String name, String value) {
 element.setAttribute(name, value);
 }

 private void setTextNode(Element element, String text) {
 Text textNode = document.createTextNode(text);
 setChild(element, textNode);
 }

 private void setChild(Element parent, Node child) {
 parent.appendChild(child);
 }

}