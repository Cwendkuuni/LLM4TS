package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"table1\" comments=\"Table comments\" remoteSchema=\"remote_schema\" remoteCatalog=\"remote_catalog\">\n" +
         "<Column name=\"col1\" comments=\"Column comments 1\" type=\"int\"/>\n" +
         "<Column name=\"col2\" comments=\"Column comments 2\" type=\"varchar\"/>\n" +
         "</Table>";

 @Test
 public void testTableMetaInstance() {
  TableMeta tableMeta = new TableMeta(createXmlDocument(XML_TABLE_META_DATA));
  TableMeta expectedTableMeta = new TableMeta(createElementFromString(XML_TABLE_META_DATA));

  assertEquals(expectedTableMeta.getName(), tableMeta.getName());
  assertEquals(expectedTableMeta.getComments(), tableMeta.getComments());
  assertEquals(expectedTableMeta.getRemoteCatalog(), tableMeta.getRemoteCatalog());
  assertEquals(expectedTableMeta.getRemoteSchema(), tableMeta.getRemoteSchema());
  assertEquals(expectedTableMeta.getColumns().size(), tableMeta.getColumns().size());

  for (int i = 0; i < expectedTableMeta.getColumns().size(); i++) {
   TableColumnMeta expectedColumn = expectedTableMeta.getColumns().get(i);
   TableColumnMeta actualColumn = tableMeta.getColumns().get(i);

   assertEquals(expectedColumn.getName(), actualColumn.getName());
   assertEquals(expectedColumn.getComments(), actualColumn.getComments());
  }
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaInstanceWithMissingNameAttribute() {
  String xmlWithoutNameAttribute = XML_TABLE_META_DATA.replaceAll("name=\"table1\"", "");
  Node node = createXmlDocument(xmlWithoutNameAttribute);

  try {
   new TableMeta(node);
   fail("Expected NullPointerException due to missing 'name' attribute.");
  } catch (NullPointerException e) {
   // Expected exception
  }
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaInstanceWithMissingCommentsAttribute() {
  String xmlWithoutCommentsAttribute = XML_TABLE_META_DATA.replaceAll("comments=\"Table comments\"", "");
  Node node = createXmlDocument(xmlWithoutCommentsAttribute);

  try {
   new TableMeta(node);
   fail("Expected NullPointerException due to missing 'comments' attribute.");
  } catch (NullPointerException e) {
   // Expected exception
  }
 }

 @Test
 public void testTableMetaLogger() {
  String tableName = "table1";
  String remoteCatalog = "remote_catalog";
  String remoteSchema = "remote_schema";
  String comments = "Table comments";

  Node tableNode = createElementFromString("<Table name=\"" + tableName + "\" " +
          "comments=\"" + comments + "\" remoteSchema=\"" + remoteSchema + "\" remoteCatalog=\"" + remoteCatalog + "\">");

  TableMeta tableMeta = new TableMeta(tableNode);

  assertTrue(TableMeta.logger.isLoggable(Level.FINE));
  String logMessage = TableMeta.logger.getLogMessage();
  String expectedLogMessage = "Found XML table metadata for " + tableName + " remoteCatalog: " + remoteCatalog + " remoteSchema: " + remoteSchema + " comments: " + comments;
  assertEquals(expectedLogMessage, logMessage);
 }

 @Test
 public void testTableMetaGetters() {
  String tableName = "table1";
  String comments = "Table comments";
  String remoteCatalog = "remote_catalog";
  String remoteSchema = "remote_schema";

  Node tableNode = createElementFromString("<Table name=\"" + tableName + "\" comments=\"" + comments + "\" " +
          "remoteSchema=\"" + remoteSchema + "\" remoteCatalog=\"" + remoteCatalog + "\">");

  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals(tableName, tableMeta.getName());
  assertEquals(comments, tableMeta.getComments());
  assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
  assertEquals(remoteSchema, tableMeta.getRemoteSchema());

  String expectedEmptyListMessage = "Expected an empty List<TableColumnMeta>";
  assertTrue(tableMeta.getColumns().isEmpty());
  assertFalse(tableMeta.getColumns() instanceof NullPointerException);
 }

 @Test
 public void testTableMetaWithEmptyColumnList() {
  String tableName = "table1";

  Node tableNode = createElementFromString("<Table name=\"" + tableName + "\"/>");

  TableMeta tableMeta = new TableMeta(tableNode);

  assertTrue(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testTableMetaWithSingleColumn() {
  String tableName = "table1";
  String columnName = "col1";
  String columnComments = "Column comments 1";

  Node tableNode = createElementFromString("<Table name=\"" + tableName + "\">\n" +
          "<Column name=\"" + columnName + "\" comments=\"" + columnComments + "\"/>\n" +
          "</Table>");

  TableMeta tableMeta = new TableMeta(tableNode);

  List<TableColumnMeta> tableColumns = tableMeta.getColumns();
  TableColumnMeta expectedColumn = new TableColumnMeta(createElementFromString("<Column name=\"" + columnName + "\">"));

  assertEquals(1, tableColumns.size());
  assertEquals(expectedColumn, tableColumns.get(0));
 }

 @Test
 public void testTableMetaWithMultipleColumns() {
  String tableName = "table1";
  String col1Name = "col1";
  String col1Comments = "Column comments 1";
  String col2Name = "col2";
  String col2Comments = "Column comments 2";

  Node tableNode = createElementFromString("<Table name=\"" + tableName + "\">\n" +
          "<Column name=\"" + col1Name + "\" comments=\"" + col1Comments + "\"/>\n" +
          "<Column name=\"" + col2Name + "\" comments=\"" + col2Comments + "\"/>\n" +
          "</Table>");

  TableMeta tableMeta = new TableMeta(tableNode);

  List<TableColumnMeta> tableColumns = tableMeta.getColumns();

  TableColumnMeta col1 = new TableColumnMeta(createElementFromString("<Column name=\"" + col1Name + "\">"));
  TableColumnMeta col2 = new TableColumnMeta(createElementFromString("<Column name=\"" + col2Name + "\">"));

  assertEquals(2, tableColumns.size());
  assertEquals(col1, tableColumns.get(0));
  assertEquals(col2, tableColumns.get(1));
 }

 private Node createXmlDocument(String xmlData) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  try {
   Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlData)));
   document.getDocumentElement().normalize();
   return document.getDocumentElement();
  } catch (Exception e) {
   throw new RuntimeException(e);
  }
 }

 private Element createElementFromString(String xmlData) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  try {
   DocumentBuilder builder = factory.newDocumentBuilder();
   return builder.parse(new InputSource(new StringReader(xmlData))).getDocumentElement();
  } catch (Exception e) {
   throw new RuntimeException(e);
  }
 }
}