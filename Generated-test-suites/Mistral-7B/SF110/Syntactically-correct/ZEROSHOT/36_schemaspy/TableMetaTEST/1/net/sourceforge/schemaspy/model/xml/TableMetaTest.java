package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\"?>\n" +
         "<table name=\"testTable\" comments=\"Test table comments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">\n" +
         "<column name=\"column1\" comments=\"Column 1 comments\" dataType=\"INT\" />\n" +
         "<column name=\"column2\" dataType=\"VARCHAR\" />\n" +
         "</table>";

 @BeforeClass
 public static void initLogger() {
 Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
 }

 @Test
 public void testTableMetaConstruction() {
  Node tableNode = createNodeFromString(XML_TABLE_META_DATA);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNotNull(tableMeta.getColumns());
  assertEquals(2, tableMeta.getColumns().size());

  TableColumnMeta column1 = tableMeta.getColumns().get(0);
  assertEquals("column1", column1.getName());
  assertEquals("Column 1 comments", column1.getComments());
  assertEquals("INT", column1.getDataTypes().get(0));

  TableColumnMeta column2 = tableMeta.getColumns().get(1);
  assertEquals("column2", column2.getName());
  assertNull(column2.getComments());
  assertTrue(column2.getDataTypes().isEmpty());

  assertEquals("testCatalog", tableMeta.getRemoteCatalog());
  assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaWithEmptyComments() {
  Node tableNode = createNodeFromString("<table name=\"testTable\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"></table>");
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNotNull(tableMeta.getColumns());
  assertEquals(0, tableMeta.getColumns().size());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaWithNullNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 private Node createNodeFromString(String xmlString) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = null;
  Document doc = null;
  try {
       builder = factory.newDocumentBuilder();
       doc = builder.parse(new InputSource(new StringReader(xmlString)));
  } catch (Exception e) {
       e.printStackTrace();
  }
  return doc;
 }
}