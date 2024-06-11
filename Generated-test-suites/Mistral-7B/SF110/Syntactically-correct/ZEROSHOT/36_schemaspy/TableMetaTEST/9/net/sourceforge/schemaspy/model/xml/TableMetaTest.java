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

 private static final String XML_FILE_PATH = "path/to/your/xml/file.xml";

 @Test
 public void testTableMetaConstruction() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document document = builder.build(XML_FILE_PATH);
  Element root = document.getRootElement();
  Node tableNode = root.getChild("Table_1");

  TableMeta tableMeta = new TableMeta(tableNode);

  assertEquals("Table_1", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());
  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(3, columns.size());

  TableColumnMeta firstColumn = columns.get(0);
  assertEquals("Column_1", firstColumn.getName());
  assertNull(firstColumn.getComments());
  assertEquals(1, firstColumn.getDataType().size());
  assertEquals("INT", firstColumn.getDataType().get(0));

  TableColumnMeta secondColumn = columns.get(1);
  assertEquals("Column_2", secondColumn.getName());
  assertEquals("This is a comment", secondColumn.getComments());
  assertEquals(2, secondColumn.getDataType().size());
  assertEquals("VARCHAR", secondColumn.getDataType().get(0));
  assertEquals("50", secondColumn.getDataType().get(1));

  TableColumnMeta thirdColumn = columns.get(2);
  assertEquals("Column_3", thirdColumn.getName());
  assertNull(thirdColumn.getComments());
  assertEquals(1, thirdColumn.getDataType().size());
  assertEquals("DECIMAL", thirdColumn.getDataType().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testNullTableNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTableNode() {
  SAXBuilder builder = new SAXBuilder();
  Document document = builder.build(XML_FILE_PATH);
  Element root = document.getRootElement();
  Node tableNode = root.getChild("Table_1");
  tableNode.detach();
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingNameAttribute() throws JDOMException {
  SAXBuilder builder = new SAXBuilder();
  Document document = builder.build(XML_FILE_PATH);
  Element root = document.getRootElement();
  Node tableNode = root.getChild("Table_1");
  tableNode.setAttribute("name", "");
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testGetLogger() {
  Logger logger = Logger.getLogger(TableMeta.class.getName());
  assertEquals(TableMeta.class, logger.getParent());
 }

}