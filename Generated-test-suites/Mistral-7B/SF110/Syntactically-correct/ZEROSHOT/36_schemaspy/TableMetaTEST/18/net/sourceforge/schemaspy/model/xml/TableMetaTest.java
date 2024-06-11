package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMetaTest.class.getName());

 @Test
 public void testTableMetaCreation() {
  TableMeta tableMeta = new TableMeta(createTestTableNode());
  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(3, columns.size());
  for (TableColumnMeta columnMeta : columns) {
   assertEquals("column1", columnMeta.getName());
   assertNull(columnMeta.getComments());
  }
  logger.fine("Test table metadata creation passed.");
 }

 @Test
 public void testTableMetaWithComments() {
  TableMeta tableMeta = new TableMeta(createTestTableNodeWithComments());
  assertEquals("testTable", tableMeta.getName());
  assertEquals("This is a test table.", tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(3, columns.size());
  for (TableColumnMeta columnMeta : columns) {
   assertEquals("column1", columnMeta.getName());
   assertNull(columnMeta.getComments());
  }
  logger.fine("Test table metadata creation with comments passed.");
 }

 @Test
 public void testTableMetaWithRemoteSchema() {
  TableMeta tableMeta = new TableMeta(createTestTableNodeWithRemoteSchema());
  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertEquals("test_schema", tableMeta.getRemoteSchema());
  assertNull(tableMeta.getRemoteCatalog());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(3, columns.size());
  for (TableColumnMeta columnMeta : columns) {
   assertEquals("column1", columnMeta.getName());
   assertNull(columnMeta.getComments());
  }
  logger.fine("Test table metadata creation with remote schema passed.");
 }

 @Test
 public void testTableMetaWithRemoteCatalogAndSchema() {
  TableMeta tableMeta = new TableMeta(createTestTableNodeWithRemoteCatalogAndSchema());
  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertEquals("test_catalog", tableMeta.getRemoteCatalog());
  assertEquals("test_schema", tableMeta.getRemoteSchema());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(3, columns.size());
  for (TableColumnMeta columnMeta : columns) {
   assertEquals("column1", columnMeta.getName());
   assertNull(columnMeta.getComments());
  }
  logger.fine("Test table metadata creation with remote catalog and schema passed.");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaNullNode() {
  try {
   new TableMeta(null);
   fail("Expected NullPointerException.");
  } catch (NullPointerException e) {
   logger.fine("Test table metadata creation with null node passed.");
  }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetColumnsOutOfBounds() {
  TableMeta tableMeta = new TableMeta(createTestTableNode());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  try {
   TableColumnMeta columnMeta = columns.get(columns.size());
   fail("Expected IndexOutOfBoundsException.");
  } catch (IndexOutOfBoundsException e) {
   logger.fine("Test getting columns out of bounds passed.");
  }
 }

 private Node createTestTableNode() {
  Element tableElement = createElement("table", "name", "testTable");
  return tableElement;
 }

 private Node createTestTableNodeWithComments() {
  Element tableElement = createElement("table", "name", "testTable", "comments", "This is a test table.");
  return tableElement;
 }

 private Node createTestTableNodeWithRemoteSchema() {
  Element tableElement = createElement("table", "name", "testTable", "remoteSchema", "test_schema");
  return tableElement;
 }

 private Node createTestTableNodeWithRemoteCatalogAndSchema() {
  Element tableElement = createElement("table", "name", "testTable", "remoteCatalog", "test_catalog", "remoteSchema", "test_schema");
  return tableElement;
 }

 private Element createElement(String tagName, String... attributes) {
  Element element = createElement(tagName);
  for (String attribute : attributes) {
   Node attributeNode = createAttributeNode(attribute);
   element.setAttributeNode(attributeNode);
  }
  return element;
 }

 private Element createElement(String tagName) {
  Document document = createDocument();
  return document.createElement(tagName);
 }

 private Node createAttributeNode(String attributeName) {
  Document document = createDocument();
  return document.createAttribute(attributeName);
 }

 private Document createDocument() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  try {
   Document document = factory.newDocumentBuilder().newDocument();
   return document;
  } catch (ParserConfigurationException e) {
   throw new RuntimeException(e);
  }
 }

}