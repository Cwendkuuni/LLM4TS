package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.w3c.dom.*;
import org.junit.Test;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\" comments=\"Test table comments\" remoteCatalog=\"testCatalog\" remoteSchema=\"testSchema\">" +
         "<Column name=\"column1\" type=\"int\" comments=\"Column 1 comments\"/>" +
         "<Column name=\"column2\" type=\"varchar\" comments=\"Column 2 comments\"/>" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
  Document doc = null;
  try {
   doc = JAXPUtils.newDocument(XML_TABLE_METADATA);
  } catch (Exception e) {
   fail("Error creating document: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());

  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  TableColumnMeta column1 = columns.get(0);
  assertEquals("column1", column1.getName());
  assertEquals("Column 1 comments", column1.getComments());
  assertEquals("int", column1.getType());

  TableColumnMeta column2 = columns.get(1);
  assertEquals("column2", column2.getName());
  assertEquals("Column 2 comments", column2.getComments());
  assertEquals("varchar", column2.getType());

  assertEquals("testCatalog", tableMeta.getRemoteCatalog());
  assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionNullTableNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionNullNameNode() {
  Document doc = null;
  try {
   doc = JAXPUtils.newDocument(XML_TABLE_METADATA);
  } catch (Exception e) {
   fail("Error creating document: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  tableNode.setNodeValue(null);
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaGetters() {
  Document doc = null;
  try {
   doc = JAXPUtils.newDocument(XML_TABLE_METADATA);
  } catch (Exception e) {
   fail("Error creating document: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertEquals("Test table comments", tableMeta.getComments());

  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  String remoteCatalog = tableMeta.getRemoteCatalog();
  assertNull(remoteCatalog);

  String remoteSchema = tableMeta.getRemoteSchema();
  assertNull(remoteSchema);
 }

 @Test
 public void testTableMetaLogger() {
  Document doc = null;
  try {
   doc = JAXPUtils.newDocument(XML_TABLE_METADATA);
  } catch (Exception e) {
   fail("Error creating document: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);
  TableMeta.logger.isLoggable(Level.FINE);
  TableMeta.logger.finer("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: null");
 }
}