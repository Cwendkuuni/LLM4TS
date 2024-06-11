package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Node;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class TableMetaTest {

 private static final String XML_FILE_PATH = "TableMetaTest.xml"; // Replace with the path to your XML file

 @Test
 public void testTableMetaConstruction() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document doc = builder.build(XML_FILE_PATH);
  Element tableElement = doc.getRootElement().getChild("Table");
  TableMeta tableMeta = new TableMeta(tableElement);

  String expectedName = "testTable";
  String expectedComments = "This is a test table";
  String expectedRemoteCatalog = "test_catalog";
  String expectedRemoteSchema = "test_schema";
  List<TableColumnMeta> expectedColumns = new ArrayList<>();

  TableColumnMeta column1 = new TableColumnMeta(new Element("column").addContent("name", "column1").addContent("type", "int"));
  TableColumnMeta column2 = new TableColumnMeta(new Element("column").addContent("name", "column2").addContent("type", "varchar"));

  expectedColumns.add(column1);
  expectedColumns.add(column2);

  String actualName = tableMeta.getName();
  String actualComments = tableMeta.getComments();
  String actualRemoteCatalog = tableMeta.getRemoteCatalog();
  String actualRemoteSchema = tableMeta.getRemoteSchema();
  List<TableColumnMeta> actualColumns = tableMeta.getColumns();

  assertEquals(expectedName, actualName);
  assertEquals(expectedComments, actualComments);
  assertEquals(expectedRemoteCatalog, actualRemoteCatalog);
  assertEquals(expectedRemoteSchema, actualRemoteSchema);
  assertEquals(expectedColumns, actualColumns);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaWithNullTableElement() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test
 public void testTableMetaWithEmptyTableElement() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document doc = builder.build(XML_FILE_PATH);
  Element tableElement = doc.getRootElement().getChild("Table");
  tableElement.removeChildren(); // Empty the Table element
  TableMeta tableMeta = new TableMeta(tableElement);

  String actualName = tableMeta.getName();
  String actualComments = tableMeta.getComments();
  List<TableColumnMeta> actualColumns = tableMeta.getColumns();

  assertNull(actualName);
  assertNull(actualComments);
  assertTrue(actualColumns.isEmpty());
 }

 @Test
 public void testTableMetaWithMissingNameAttribute() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document doc = builder.build(XML_FILE_PATH);
  Element tableElement = doc.getRootElement().getChild("Table");
  tableElement.setAttribute("name", null); // Remove the name attribute
  TableMeta tableMeta = new TableMeta(tableElement);

  String actualName = tableMeta.getName();
  assertNull(actualName);
 }

 @Test
 public void testTableMetaWithEmptyCommentsAttribute() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document doc = builder.build(XML_FILE_PATH);
  Element tableElement = doc.getRootElement().getChild("Table");
  Element commentsElement = tableElement.getChild("comments");
  commentsElement.setText("");
  TableMeta tableMeta = new TableMeta(tableElement);

  String actualComments = tableMeta.getComments();
  assertNull(actualComments);
 }

 @Test
 public void testTableMetaWithMissingRemoteCatalogAndRemoteSchemaAttributes() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document doc = builder.build(XML_FILE_PATH);
  Element tableElement = doc.getRootElement().getChild("Table");
  tableElement.setAttribute("remoteCatalog", null);
  tableElement.setAttribute("remoteSchema", null);
  TableMeta tableMeta = new TableMeta(tableElement);

  String actualRemoteCatalog = tableMeta.getRemoteCatalog();
  String actualRemoteSchema = tableMeta.getRemoteSchema();
  assertNull(actualRemoteCatalog);
  assertNull(actualRemoteSchema);
 }

 @Test
 public void testTableMetaWithEmptyColumnsList() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document doc = builder.build(XML_FILE_PATH);
  Element tableElement = doc.getRootElement().getChild("Table");
  tableElement.getChild("column").detach(); // Remove all column elements
  TableMeta tableMeta = new TableMeta(tableElement);

  List<TableColumnMeta> actualColumns = tableMeta.getColumns();
  assertTrue(actualColumns.isEmpty());
 }
}