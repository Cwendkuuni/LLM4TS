package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\" comments=\"This is a test table\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">" +
         "<Column name=\"col1\" comments=\"This is column 1\" type=\"INT\" />" +
         "<Column name=\"col2\" type=\"VARCHAR\" />" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
  Node tableNode = Dom4jUtils.parse(XML_TABLE_METADATA).getDocument().getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);

  // Test name
  assertEquals("testTable", tableMeta.getName());

  // Test comments
  assertNotNull(tableMeta.getComments());
  assertEquals("This is a test table", tableMeta.getComments());

  // Test columns
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  TableColumnMeta col1 = columns.get(0);
  assertEquals("col1", col1.getName());
  assertNotNull(col1.getComments());
  assertEquals("This is column 1", col1.getComments());

  TableColumnMeta col2 = columns.get(1);
  assertEquals("col2", col2.getName());
  assertNull(col2.getComments());

  // Test remoteCatalog and remoteSchema
  assertNull(tableMeta.getRemoteCatalog());
  assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
  Node tableNode = null;
  new TableMeta(tableNode);
 }

 @Test
 public void testLoggerMessage() {
  String name = "testTable";
  Node tableNode = Dom4jUtils.parse("<Table name=\"" + name + "\"></Table>").getDocument().getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);
  TableMeta.logger.fine("Found XML table metadata for " + name);
  assertEquals("Found XML table metadata for testTable", TableMeta.logger.getMessage(TableMeta.logger.getLevel()));
 }
}