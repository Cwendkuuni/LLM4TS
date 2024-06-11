package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.names.*;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"testTable\" comments=\"This is a test table\" remoteSchema=\"test_schema\" remoteCatalog=\"test_catalog\">\n" +
         "<Column name=\"col1\" type=\"int\" comments=\"Test column 1\" />\n" +
         "<Column name=\"col2\" type=\"varchar(255)\" comments=\"Test column 2\" />\n" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() throws ParseException {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = factory.newDocumentBuilder();
  Document doc = builder.parse(new InputSource(new StringReader(XML_TABLE_METADATA)));
  doc.getDocumentElement().normalize();

  TableMeta tableMeta = new TableMeta(doc.getDocumentElement());

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());

  TableColumnMeta col1 = columns.get(0);
  assertEquals("col1", col1.getName());
  assertEquals("Test column 1", col1.getComments());
  assertEquals("int", col1.getType());

  TableColumnMeta col2 = columns.get(1);
  assertEquals("col2", col2.getName());
  assertEquals("Test column 2", col2.getComments());
  assertEquals("varchar(255)", col2.getType());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionNullTableNode() {
  new TableMeta(null);
 }

 @Test
 public void testTableMetaGetters() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = factory.newDocumentBuilder();
  Document doc = builder.parse(new InputSource(new StringReader(XML_TABLE_METADATA)));
  doc.getDocumentElement().normalize();

  TableMeta tableMeta = new TableMeta(doc.getDocumentElement());

  assertEquals("testTable", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
  TableMeta.logger.fine("Found XML table metadata for testTable remoteCatalog: null remoteSchema: test_schema comments: null");

  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());
 }

 @Test
 public void testTableMetaLogger() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = factory.newDocumentBuilder();
  Document doc = builder.parse(new InputSource(new StringReader(XML_TABLE_METADATA)));
  doc.getDocumentElement().normalize();

  TableMeta tableMeta = new TableMeta(doc.getDocumentElement());

  assertTrue(TableMeta.logger.isLoggable(Level.FINE));
  String logMessage = TableMeta.logger.getLogMessage();
  assertTrue(logMessage.contains("Found XML table metadata for testTable remoteCatalog: null remoteSchema: test_schema comments: null"));
 }
}