package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.namespace.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\"?>\n" +
         "<Table name=\"testTable\" comments=\"Test table comment\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">\n" +
         "<Column name=\"col1\" type=\"INT\" comments=\"Column 1 comment\"/>\n" +
         "<Column name=\"col2\" type=\"VARCHAR(50)\" comments=\"Column 2 comment\"/>\n" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
  Node tableNode = createNodeFromString(XML_TABLE_META_DATA);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNotNull(tableMeta.getColumns());
  assertEquals(2, tableMeta.getColumns().size());

  TableColumnMeta col1 = tableMeta.getColumns().get(0);
  TableColumnMeta col2 = tableMeta.getColumns().get(1);

  assertEquals("col1", col1.getName());
  assertEquals("INT", col1.getType());
  assertEquals("Column 1 comment", col1.getComments());

  assertEquals("col2", col2.getName());
  assertEquals("VARCHAR(50)", col2.getType());
  assertNull(col2.getComments());

  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
  Node tableNode = null;
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyNameAttribute() {
  Node tableNode = createNodeFromString("<Table name='' comments=\"Test table comment\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">...</Table>");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyCommentsAttribute() {
  Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">...</Table>");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithMissingNameAttribute() {
  Node tableNode = createNodeFromString("<Table comments=\"Test table comment\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">...</Table>");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithNullColumnNodes() {
  Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"Test table comment\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"></Table>");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 private Node createNodeFromString(String xmlString) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = null;
  try {
   builder = factory.newDocumentBuilder();
  } catch (ParserConfigurationException e) {
   e.printStackTrace();
  }
  Document document = null;
  try {
   document = builder.parse(new InputSource(new StringReader(xmlString)));
  } catch (SAXException | IOException e) {
   e.printStackTrace();
  }
  return document.getDocumentElement();
 }

}