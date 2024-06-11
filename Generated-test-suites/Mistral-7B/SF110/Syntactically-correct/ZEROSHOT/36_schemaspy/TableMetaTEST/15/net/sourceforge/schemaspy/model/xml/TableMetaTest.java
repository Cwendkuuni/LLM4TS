package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
         + "<Table name=\"testTable\" comments=\"Test table comments\" remoteSchema=\"testRemoteSchema\" remoteCatalog=\"testRemoteCatalog\">\n"
         + "  <Column name=\"col1\" type=\"INTEGER\" comments=\"Column 1 comments\"/>\n"
         + "  <Column name=\"col2\" type=\"VARCHAR(50)\" comments=\"Column 2 comments\"/>\n"
         + "</Table>";

 @Test
 public void testTableMetaConstruction() {
  Logger logger = Logger.getLogger(TableMeta.class.getName());
  logger.setLevel(Level.FINE);
  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
  try {
   Document doc = dbFactory.newDocumentBuilder().parse(new ByteArrayInputStream(XML_TABLE_META_DATA.getBytes()));
   doc.getDocumentElement().normalize();
   Node tableNode = doc.getDocumentElement();
   TableMeta tableMeta = new TableMeta(tableNode);

   assertEquals("testTable", tableMeta.getName());
   assertNull(tableMeta.getComments());
   List<TableColumnMeta> expectedColumns = new ArrayList<TableColumnMeta>();
   expectedColumns.add(new TableColumnMeta(doc.getElementsByTagName("Column").item(0)));
   expectedColumns.add(new TableColumnMeta(doc.getElementsByTagName("Column").item(1)));
   assertEquals(expectedColumns, tableMeta.getColumns());
   assertNull(tableMeta.getRemoteCatalog());
   assertNull(tableMeta.getRemoteSchema());

   logger.fine("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: null");
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
  Logger logger = Logger.getLogger(TableMeta.class.getName());
  logger.setLevel(Level.FINE);
  Node tableNode = null;
  TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testGetters() {
  Logger logger = Logger.getLogger(TableMeta.class.getName());
  logger.setLevel(Level.FINE);
  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
  try {
   Document doc = dbFactory.newDocumentBuilder().parse(new ByteArrayInputStream(XML_TABLE_META_DATA.getBytes()));
   doc.getDocumentElement().normalize();
   Node tableNode = doc.getDocumentElement();
   TableMeta tableMeta = new TableMeta(tableNode);

   assertEquals("testTable", tableMeta.getName());
   String comments = tableMeta.getComments();
   assertNull(comments);

   List<TableColumnMeta> columns = tableMeta.getColumns();
   List<TableColumnMeta> expectedColumns = new ArrayList<TableColumnMeta>();
   expectedColumns.add(new TableColumnMeta(doc.getElementsByTagName("Column").item(0)));
   expectedColumns.add(new TableColumnMeta(doc.getElementsByTagName("Column").item(1)));
   assertEquals(expectedColumns, columns);

   String remoteCatalog = tableMeta.getRemoteCatalog();
   assertNull(remoteCatalog);

   String remoteSchema = tableMeta.getRemoteSchema();
   assertNull(remoteSchema);

   logger.fine("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: null");
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
}