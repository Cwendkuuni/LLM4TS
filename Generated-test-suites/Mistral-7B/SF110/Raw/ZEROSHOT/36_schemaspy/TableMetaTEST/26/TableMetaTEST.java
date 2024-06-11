package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"testTable\" comments=\"Test Table Comments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">\n" +
         "<Column name=\"col1\" comments=\"Column 1 comments\" dataType=\"INT\" />\n" +
         "<Column name=\"col2\" comments=\"Column 2 comments\" dataType=\"VARCHAR\" />\n" +
         "</Table>";

 @BeforeClass
 public static void setup() {
 TableMeta.logger.setLevel(Level.FINE);
 }

 @Test
 public void testTableMetaInstance() {
 Node tableNode = createTestXMLDocument(XML_TABLE_META_DATA);
 TableMeta tableMeta = new TableMeta(tableNode);

 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());

 TableColumnMeta col1 = columns.get(0);
 assertEquals("col1", col1.getName());
 assertNull(col1.getComments());
 assertEquals("INT", col1.getDataType());

 TableColumnMeta col2 = columns.get(1);
 assertEquals("col2", col2.getName());
 assertNotNull(col2.getComments());
 assertEquals("Column 2 comments", col2.getComments());
 assertEquals("VARCHAR", col2.getDataType());
 }

 @Test
 public void testTableMetaEmptyComments() {
 Node tableNode = createTestXMLDocument(XML_TABLE_META_DATA.replaceAll("Test Table Comments", ""));
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaNullComments() {
 Node tableNode = createTestXMLDocument("<Table name=\"testTable\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"></Table>");
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaNullRemoteCatalogAndSchema() {
 Node tableNode = createTestXMLDocument("<Table name=\"testTable\"></Table>");
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaWithRemoteCatalogAndSchema() {
 Node tableNode = createTestXMLDocument(XML_TABLE_META_DATA);
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("testCatalog", tableMeta.getRemoteCatalog());
 assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 private Node createTestXMLDocument(String xmlData) {
 try {
 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
 Document document = documentBuilder.parse(new InputSource(new StringReader(xmlData)));
 document.getDocumentElement().normalize();
 return document;
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

}