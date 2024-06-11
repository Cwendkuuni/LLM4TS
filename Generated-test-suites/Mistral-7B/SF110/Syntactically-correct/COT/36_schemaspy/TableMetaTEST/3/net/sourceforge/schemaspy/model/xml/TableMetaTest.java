package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(createTestTableNode());
 String expectedName = "testTable";
 String expectedComments = "test table comments";
 List<TableColumnMeta> expectedColumns = new ArrayList<>();
 TableColumnMeta column1 = new TableColumnMeta(createTestTableColumnNode(1));
 TableColumnMeta column2 = new TableColumnMeta(createTestTableColumnNode(2));
 expectedColumns.add(column1);
 expectedColumns.add(column2);

 assertEquals(expectedName, tableMeta.getName());
 assertEquals(expectedComments, tableMeta.getComments());
 assertEquals(expectedColumns, tableMeta.getColumns());
 assertEquals(null, tableMeta.getRemoteCatalog());
 assertEquals(null, tableMeta.getRemoteSchema());
 logger.info("Test case: TableMeta construction test passed.");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaNullNode() {
 TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaEmptyColumns() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithEmptyColumnsList());
 }

 @Test
 public void testGetName() {
 TableMeta tableMeta = new TableMeta(createTestTableNode());
 String expectedName = "testTable";
 assertEquals(expectedName, tableMeta.getName());
 }

 @Test
 public void testGetCommentsNull() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithNullComments());
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testGetCommentsEmptyString() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithEmptyComments());
 assertEquals("", tableMeta.getComments());
 }

 @Test
 public void testGetCommentsNonEmptyString() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithComments());
 String expectedComments = "test table comments";
 assertEquals(expectedComments, tableMeta.getComments());
 }

 @Test
 public void testGetColumns() {
 TableMeta tableMeta = new TableMeta(createTestTableNode());
 List<TableColumnMeta> expectedColumns = new ArrayList<>();
 TableColumnMeta column1 = new TableColumnMeta(createTestTableColumnNode(1));
 TableColumnMeta column2 = new TableColumnMeta(createTestTableColumnNode(2));
 expectedColumns.add(column1);
 expectedColumns.add(column2);
 assertEquals(expectedColumns, tableMeta.getColumns());
 }

 @Test
 public void testGetRemoteCatalogNull() {
 TableMeta tableMeta = new TableMeta(createTestTableNode());
 assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteCatalogNonEmptyString() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithRemoteCatalog());
 String expectedRemoteCatalog = "testRemoteCatalog";
 assertEquals(expectedRemoteCatalog, tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchemaNull() {
 TableMeta tableMeta = new TableMeta(createTestTableNode());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testGetRemoteSchemaNonEmptyString() {
 TableMeta tableMeta = new TableMeta(createTestTableNodeWithRemoteSchema());
 String expectedRemoteSchema = "testRemoteSchema";
 assertEquals(expectedRemoteSchema, tableMeta.getRemoteSchema());
 }

 private Node createTestTableNode() {
 Element tableElement = createTestElement("table", "name", "testTable", "comments", "test table comments", "remoteSchema", null, "remoteCatalog", null);
 return tableElement;
 }

 private Node createTestTableNodeWithEmptyColumnsList() {
 Element tableElement = createTestElement("table", "name", "testTable", "comments", "test table comments", "remoteSchema", null, "remoteCatalog", null);
 tableElement.getElementsByTagName("column").clear();
 return tableElement;
 }

 private Node createTestTableNodeWithNullComments() {
 Element tableElement = createTestElement("table", "name", "testTable", null, null, "remoteSchema", null, "remoteCatalog", null);
 return tableElement;
 }

 private Node createTestTableNodeWithEmptyComments() {
 Element tableElement = createTestElement("table", "name", "testTable", "", "", "remoteSchema", null, "remoteCatalog", null);
 return tableElement;
 }

 private Node createTestTableNodeWithComments() {
 Element tableElement = createTestElement("table", "name", "testTable", "comments", "test table comments", "remoteSchema", null, "remoteCatalog", null);
 return tableElement;
 }

 private Node createTestTableNodeWithRemoteCatalog() {
 Element tableElement = createTestElement("table", "name", "testTable", "comments", "test table comments", "remoteSchema", "testRemoteCatalog", "remoteCatalog", null);
 return tableElement;
 }

 private Node createTestTableNodeWithRemoteSchema() {
 Element tableElement = createTestElement("table", "name", "testTable", "comments", "test table comments", "remoteSchema", null, "remoteCatalog", "testRemoteSchema");
 return tableElement;
 }

 private Element createTestElement(String tagName, String... attributes) {
 Element element = null;
 try {
 Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 element = document.createElement(tagName);
 for (int i = 0; i < attributes.length; i += 2) {
 Node attributeNode = document.createAttribute(attributes[i]);
 attributeNode.setNodeValue(attributes[i + 1]);
 element.setAttributeNode(attributeNode);
 }
 } catch (Exception e) {
 logger.severe("Error creating test element: " + e.getMessage());
 }
 return element;
 }
}