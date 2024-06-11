package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sourceforge.schemaspy.model.xml.TableColumnMeta;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\" comments=\"Test table comments\" remoteCatalog=\"testCatalog\" remoteSchema=\"testSchema\">" +
         "<column name=\"col1\" type=\"int\" comments=\"Test column comments\"/>" +
         "<column name=\"col2\" type=\"varchar\" comments=\"Test column comments\"/>" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
  Node tableNode = getXmlNode(XML_TABLE_META_DATA);
  TableMeta tableMeta = new TableMeta(tableNode);

  String name = tableMeta.getName();
  assertEquals("testTable", name);

  String comments = tableMeta.getComments();
  assertNotNull(comments);
  assertEquals("Test table comments", comments);

  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  TableColumnMeta col1 = columns.get(0);
  assertEquals("col1", col1.getName());
  assertEquals("int", col1.getType());
  assertEquals("Test column comments", col1.getComments());

  TableColumnMeta col2 = columns.get(1);
  assertEquals("col2", col2.getName());
  assertEquals("varchar", col2.getType());
  assertNotNull(col2.getComments());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullTableNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyTableName() {
  Node tableNode = getXmlNode("<Table name=\"\" comments=\"Test table comments\" remoteCatalog=\"testCatalog\" remoteSchema=\"testSchema\">" +
          "<column name=\"col1\" type=\"int\" comments=\"Test column comments\"/>" +
          "</Table>");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithNullCommentsNode() {
  Node tableNode = getXmlNode("<Table name=\"testTable\" comments=\"\" remoteCatalog=\"testCatalog\" remoteSchema=\"testSchema\">" +
          "<column name=\"col1\" type=\"int\" comments=\"Test column comments\"/>" +
          "</Table>");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaGetters() {
  String remoteCatalog = "testRemoteCatalog";
  String remoteSchema = "testRemoteSchema";
  Node tableNode = getXmlNode(XML_TABLE_META_DATA + "<remoteCatalog>" + remoteCatalog + "</remoteCatalog>" +
          "<remoteSchema>" + remoteSchema + "</remoteSchema>");
  TableMeta tableMeta = new TableMeta(tableNode);

  String name = tableMeta.getName();
  assertEquals("testTable", name);

  String comments = tableMeta.getComments();
  assertNotNull(comments);
  assertEquals("Test table comments", comments);

  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  String remoteCatalogFromTableMeta = tableMeta.getRemoteCatalog();
  assertEquals(remoteCatalog, remoteCatalogFromTableMeta);

  String remoteSchemaFromTableMeta = tableMeta.getRemoteSchema();
  assertEquals(remoteSchema, remoteSchemaFromTableMeta);
 }

 private Node getXmlNode(String xml) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = null;
  try {
   builder = factory.newDocumentBuilder();
  } catch (ParserConfigurationException e) {
   e.printStackTrace();
  }
  Document document = null;
  try {
   document = builder.parse(new InputSource(new StringReader(xml)));
  } catch (SAXException | IOException e) {
   e.printStackTrace();
  }
  return document.getDocumentElement();
 }

}