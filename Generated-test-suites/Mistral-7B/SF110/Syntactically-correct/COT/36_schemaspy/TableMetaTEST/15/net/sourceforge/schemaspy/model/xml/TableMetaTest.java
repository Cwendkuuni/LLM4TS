package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\"?>\n" +
         "<Table name=\"testTable\" comments=\"Test table comments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">\n" +
         "<Column name=\"col1\" type=\"INT\" comments=\"Test column comments 1\"/>\n" +
         "<Column name=\"col2\" type=\"VARCHAR(10)\" comments=\"Test column comments 2\"/>\n" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
  Node tableNode = createXMLNode(XML_TABLE_META_DATA);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertEquals("Test table comments", tableMeta.getComments());
  assertEquals("testCatalog", tableMeta.getRemoteCatalog());
  assertEquals("testSchema", tableMeta.getRemoteSchema());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());
  TableColumnMeta col1 = columns.get(0);
  TableColumnMeta col2 = columns.get(1);
  assertEquals("col1", col1.getName());
  assertEquals("INT", col1.getType());
  assertEquals("Test column comments 1", col1.getComments());
  assertEquals("col2", col2.getName());
  assertEquals("VARCHAR(10)", col2.getType());
  assertEquals("Test column comments 2", col2.getComments());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyName() {
  Node tableNode = createXMLNode("<Table name=\"\" comments=\"testComments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyComments() {
  Node tableNode = createXMLNode("<Table name=\"testName\" comments=\"\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithNullRemoteCatalog() {
  Node tableNode = createXMLNode("<Table name=\"testName\" comments=\"testComments\" remoteSchema=\"testSchema\" remoteCatalog=\"null\">");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithNullRemoteSchema() {
  Node tableNode = createXMLNode("<Table name=\"testName\" comments=\"testComments\" remoteCatalog=\"testCatalog\" remoteSchema=\"null\">");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstructionWithEmptyColumnList() {
  Node tableNode = createXMLNode("<Table name=\"testName\" comments=\"testComments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"/>");
  TableMeta tableMeta = new TableMeta(tableNode);
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(0, columns.size());
 }

 private Node createXMLNode(String xmlData) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder;
  try {
   builder = factory.newDocumentBuilder();
  } catch (ParserConfigurationException e) {
   throw new RuntimeException(e);
  }
  Document document;
  try {
   document = builder.parse(new InputSource(new StringReader(xmlData)));
  } catch (SAXException | IOException e) {
   throw new RuntimeException(e);
  }
  return document.getDocumentElement();
 }

}