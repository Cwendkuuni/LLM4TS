package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
         "<Table name=\"table1\">" +
         " <Column name=\"col1\" typeName=\"int\">" +
         " <ColumnName>col1</ColumnName>" +
         " </Column>" +
         " <Column name=\"col2\" typeName=\"varchar(50)\">" +
         " <ColumnName>col2</ColumnName>" +
         " <ColumnLength>50</ColumnLength>" +
         " </Column>" +
         " <TableType>BASE TABLE</TableType>" +
         " <Comments xml:lang=\"en-US\">Table comments</Comments>" +
         " <RemoteSchema name=\"remote_schema\"/>" +
         " <RemoteCatalog name=\"remote_catalog\"/>" +
         "</Table>" +
         "</DatabaseMetaData>";

 @Test
 public void testTableMetaConstruction() {
  Node tableNode = getNodeFromString(XML_TABLE_META_DATA);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("table1", tableMeta.getName());
  assertNull(tableMeta.getComments());

  List<TableColumnMeta> expectedColumns = new ArrayList<>();
  expectedColumns.add(new TableColumnMeta("col1", "int", null, null, null));
  expectedColumns.add(new TableColumnMeta("col2", "varchar", "50", null, null));

  List<TableColumnMeta> actualColumns = tableMeta.getColumns();

  assertEquals(expectedColumns.size(), actualColumns.size());
  for (int i = 0; i < expectedColumns.size(); i++) {
      TableColumnMeta expectedColumn = expectedColumns.get(i);
      TableColumnMeta actualColumn = actualColumns.get(i);

      assertEquals(expectedColumn.getName(), actualColumn.getName());
      assertEquals(expectedColumn.getTypeName(), actualColumn.getTypeName());
      assertEquals(expectedColumn.getColumnLength(), actualColumn.getColumnLength());
      assertEquals(expectedColumn.getColumnName(), actualColumn.getColumnName());
      assertEquals(expectedColumn.getColumnDefault(), actualColumn.getColumnDefault());
      assertEquals(expectedColumn.getJdbcTypeName(), actualColumn.getJdbcTypeName());
  }

  assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
  assertEquals("remote_schema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaNullTableNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaEmptyTableNode() {
  Node tableNode = getNodeFromString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaNoNameAttribute() {
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
          "<Table>" +
          "<Column name=\"col1\" typeName=\"int\">" +
          "<ColumnName>col1</ColumnName>" +
          "</Column>" +
          "</Table>" +
          "</DatabaseMetaData>";

  Node tableNode = getNodeFromString(xml);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertNull(tableMeta.getName());
 }

 @Test
 public void testTableMetaComments() {
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
          "<Table name=\"table1\">" +
          "<Column name=\"col1\" typeName=\"int\">" +
          "<ColumnName>col1</ColumnName>" +
          "</Column>" +
          "<TableType>BASE TABLE</TableType>" +
          "<Comments xml:lang=\"en-US\">Table comments</Comments>" +
          "</Table>" +
          "</DatabaseMetaData>";

  Node tableNode = getNodeFromString(xml);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("Table comments", tableMeta.getComments());
 }

 @Test
 public void testTableMetaNoComments() {
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
          "<Table name=\"table1\">" +
          "<Column name=\"col1\" typeName=\"int\">" +
          "<ColumnName>col1</ColumnName>" +
          "</Column>" +
          "<TableType>BASE TABLE</TableType>" +
          "</Table>" +
          "</DatabaseMetaData>";

  Node tableNode = getNodeFromString(xml);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaRemoteCatalog() {
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
          "<Table name=\"table1\">" +
          "<Column name=\"col1\" typeName=\"int\">" +
          "<ColumnName>col1</ColumnName>" +
          "</Column>" +
          "<TableType>BASE TABLE</TableType>" +
          "<RemoteSchema name=\"remote_schema\"/>" +
          "<RemoteCatalog name=\"remote_catalog\"/>" +
          "</Table>" +
          "</DatabaseMetaData>";

  Node tableNode = getNodeFromString(xml);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaRemoteCatalogNull() {
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
          "<Table name=\"table1\">" +
          "<Column name=\"col1\" typeName=\"int\">" +
          "<ColumnName>col1</ColumnName>" +
          "</Column>" +
          "<TableType>BASE TABLE</TableType>" +
          "</Table>" +
          "</DatabaseMetaData>";

  Node tableNode = getNodeFromString(xml);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaRemoteSchema() {
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
          "<Table name=\"table1\">" +
          "<Column name=\"col1\" typeName=\"int\">" +
          "<ColumnName>col1</ColumnName>" +
          "</Column>" +
          "<TableType>BASE TABLE</TableType>" +
          "<RemoteSchema name=\"remote_schema\"/>" +
          "</Table>" +
          "</DatabaseMetaData>";

  Node tableNode = getNodeFromString(xml);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("remote_schema", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaRemoteSchemaNull() {
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<DatabaseMetaData xmlns=\"http://schemas.microsoft.com/sqlserver/management/xml/metadata/2004-07-31/DatabaseMetaData\">" +
          "<Table name=\"table1\">" +
          "<Column name=\"col1\" typeName=\"int\">" +
          "<ColumnName>col1</ColumnName>" +
          "</Column>" +
          "<TableType>BASE TABLE</TableType>" +
          "</Table>" +
          "</DatabaseMetaData>";

  Node tableNode = getNodeFromString(xml);
  TableMeta tableMeta = new TableMeta(tableNode);

  assertNull(tableMeta.getRemoteSchema());
 }

 private Node getNodeFromString(String xmlString) {
  try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
      document.getDocumentElement().normalize();

      return document.getDocumentElement();
  } catch (Exception e) {
      throw new RuntimeException(e);
  }
 }

}