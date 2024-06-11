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
         "<tables>\n" +
         "  <table name=\"table1\" comments=\"This is table1\" remoteSchema=\"schema1\" remoteCatalog=\"catalog1\">\n" +
         "    <columns>\n" +
         "      <column name=\"col1\" type=\"int\" />\n" +
         "      <column name=\"col2\" type=\"varchar\" />\n" +
         "    </columns>\n" +
         "  </table>\n" +
         "</tables>";

 @Test
 public void testTableMetaInstance() {
  Node node = createDocument(XML_TABLE_META_DATA).getDocumentElement();
  TableMeta tableMeta = new TableMeta(node);

  assertEquals("table1", tableMeta.getName());
  assertEquals("This is table1", tableMeta.getComments());
  assertEquals("schema1", tableMeta.getRemoteSchema());
  assertEquals("catalog1", tableMeta.getRemoteCatalog());

  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());
  assertEquals("col1", columns.get(0).getName());
  assertEquals("int", columns.get(0).getType());
  assertEquals("col2", columns.get(1).getName());
  assertEquals("varchar", columns.get(1).getType());
 }

 @Test
 public void testTableMetaInstanceWithNoComments() {
  Node node = createDocument(XML_TABLE_META_DATA.replaceAll("This is table1", "table2")).getDocumentElement();
  TableMeta tableMeta = new TableMeta(node);

  assertEquals("table2", tableMeta.getName());
  assertNull(tableMeta.getComments());
  assertNull(tableMeta.getRemoteCatalog());
  assertNull(tableMeta.getRemoteSchema());

  List<TableColumnMeta> columns = tableMeta.getColumns();
  assertEquals(2, columns.size());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaInstanceWithNullNode() {
  TableMeta tableMeta = new TableMeta(null);
 }

 @Test
 public void testTableMetaLogger() {
  Node node = createDocument(XML_TABLE_META_DATA).getDocumentElement();
  TableMeta tableMeta = new TableMeta(node);

  assertTrue(TableMeta.logger.isLoggable(Level.FINEST));
  assertTrue(TableMeta.logger.isLoggable(Level.FINE));
  assertTrue(TableMeta.logger.isLoggable(Level.INFO));
  assertTrue(TableMeta.logger.isLoggable(Level.WARNING));
  assertTrue(TableMeta.logger.isLoggable(Level.SEVERE));

  TableMeta.logger.finest("Test log message.");
 }

 private Document createDocument(String xmlData) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = null;
  try {
   builder = factory.newDocumentBuilder();
  } catch (ParserConfigurationException e) {
   e.printStackTrace();
  }

  Document document = null;
  try {
   document = builder.parse(new InputSource(new StringReader(xmlData)));
  } catch (SAXException | IOException e) {
   e.printStackTrace();
  }

  return document;
 }

}