package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.w3c.dom.*;
import org.xml.names.*;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\"" +
         " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
         " xsi:schemaLocation=\"http://mynamespace.com/xsd/table.xsd\"" +
         " comments=\"This is a test table\"" +
         " remoteSchema=\"my_remote_schema\"" +
         " remoteCatalog=\"my_remote_catalog\">" +
         "<Column name=\"col1\" comments=\"col1 comments\"/>" +
         "<Column name=\"col2\">" +
         "<Comments>col2 comments</Comments>" +
         "</Column>" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  Document doc;
  try {
   doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(XML_TABLE_METADATA)));
  } catch (Exception e) {
   fail("Failed to parse XML: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertEquals("This is a test table", tableMeta.getComments());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  TableColumnMeta col1 = columns.get(0);
  assertEquals("col1", col1.getName());
  assertEquals(null, col1.getComments());

  TableColumnMeta col2 = columns.get(1);
  assertEquals("col2", col2.getName());
  assertEquals("col2 comments", col2.getComments());

  assertEquals("my_remote_catalog", tableMeta.getRemoteCatalog());
  assertEquals("my_remote_schema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionNullTableNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionEmptyNodeList() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  Document doc;
  try {
   doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader("<Table/>")));
  } catch (Exception e) {
   fail("Failed to parse XML: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionMissingNameAttribute() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  Document doc;
  try {
   doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader("<Table comments=\"comments\"/>")));
  } catch (Exception e) {
   fail("Failed to parse XML: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionMissingCommentsAttributes() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  Document doc;
  try {
   doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(XML_TABLE_METADATA.replace("comments=\"This is a test table\"", ""))));
  } catch (Exception e) {
   fail("Failed to parse XML: " + e.getMessage());
  }
  Node tableNode = doc.getDocumentElement();
  TableMeta tableMeta = new TableMeta(tableNode);
 }
}