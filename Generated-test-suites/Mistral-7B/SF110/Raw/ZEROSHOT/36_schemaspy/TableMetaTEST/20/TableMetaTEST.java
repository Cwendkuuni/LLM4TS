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
         "<TABLE name=\"testTable\">\n" +
         "  <COMMENTS>Test Table</COMMENTS>\n" +
         "  <REMOTE_SCHEMA>testSchema</REMOTE_SCHEMA>\n" +
         "  <REMOTE_CATALOG>testCatalog</REMOTE_CATALOG>\n" +
         "  <COLUMN>\n" +
         "    <NAME>column1</NAME>\n" +
         "    <TYPE>INT</TYPE>\n" +
         "  </COLUMN>\n" +
         "  <COLUMN>\n" +
         "    <NAME>column2</NAME>\n" +
         "    <TYPE>VARCHAR</TYPE>\n" +
         "  </COLUMN>\n" +
         "</TABLE>";

 @Test
 public void testTableMetaInitialization() {
  Node tableNode = parseXml(XML_TABLE_META_DATA);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  TableColumnMeta column1 = columns.get(0);
  assertEquals("column1", column1.getName());
  assertEquals("INT", column1.getType());

  TableColumnMeta column2 = columns.get(1);
  assertEquals("column2", column2.getName());
  assertEquals("VARCHAR", column2.getType());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaWithoutNameAttribute() {
  Node tableNode = parseXml(XML_TABLE_META_DATA.replaceAll("name", ""));
  new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaWithoutColumns() {
  Node tableNode = parseXml("<TABLE/>");
  TableMeta tableMeta = new TableMeta(tableNode);
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(0, columns.size());
 }

 @Test
 public void testTableMetaLogger() {
  Node tableNode = parseXml(XML_TABLE_META_DATA);
  TableMeta tableMeta = new TableMeta(tableNode);
  assertNotNull(TableMeta.logger);
 }

 private Node parseXml(String xmlString) {
  try {
   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   DocumentBuilder builder = factory.newDocumentBuilder();
   Document document = builder.parse(new InputSource(new StringReader(xmlString)));
   return document.getDocumentElement();
  } catch (Exception e) {
   throw new RuntimeException(e);
  }
 }

}