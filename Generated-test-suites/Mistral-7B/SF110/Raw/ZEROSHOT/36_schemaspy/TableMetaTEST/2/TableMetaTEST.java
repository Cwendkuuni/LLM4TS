package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"testTable\" comments=\"Test table comments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">\n" +
         "<Column name=\"column1\" comments=\"Column 1 comments\" dataType=\"INTEGER\"/>\n" +
         "<Column name=\"column2\" comments=\"Column 2 comments\" dataType=\"VARCHAR\"/>\n" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
     Node tableNode = parseXML(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("testTable", tableMeta.getName());
     assertNull(tableMeta.getComments());
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(2, columns.size());
     TableColumnMeta column1 = columns.get(0);
     assertEquals("column1", column1.getName());
     assertEquals("Column 1 comments", column1.getComments());
     TableColumnMeta column2 = columns.get(1);
     assertEquals("column2", column2.getName());
     assertEquals("Column 2 comments", column2.getComments());
     assertEquals("testCatalog", tableMeta.getRemoteCatalog());
     assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
     Node tableNode = null;
     new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaLogging() {
     String logMessage = "Found XML table metadata for testTable remoteCatalog: testCatalog remoteSchema: testSchema comments: null";
     Node tableNode = parseXML(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);
     assertTrue(TableMeta.logger.isLoggable(Level.FINE));
     assertEquals(logMessage, TableMeta.logger.getMessages().get(0).getMessage().toString());
 }

 @Test
 public void testTableMetaEmptyComments() {
     Node tableNode = parseXML(XML_TABLE_METADATA.replace("Test table comments", "").replace("Column 1 comments", "").replace("Column 2 comments", ""));
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaNullComments() {
     Node tableNode = parseXML(XML_TABLE_METADATA.replace("Test table comments", "").replace("Column 1 comments", "").replace("Column 2 comments", "").replace("comments", ""));
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaMissingRemoteCatalogAndRemoteSchema() {
     Node tableNode = parseXML(XML_TABLE_METADATA.replace("remoteSchema=\"testSchema\"", "").replace("remoteCatalog=\"testCatalog\"", ""));
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getRemoteCatalog());
     assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaEmptyColumns() {
     Node tableNode = parseXML(XML_TABLE_METADATA.replace("<Column name=\"column1\" comments=\"Column 1 comments\" dataType=\"INTEGER\"/>", ""));
     TableMeta tableMeta = new TableMeta(tableNode);
     assertTrue(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testTableMetaOneColumn() {
     Node tableNode = parseXML(XML_TABLE_METADATA.replace("<Column name=\"column2\" comments=\"Column 2 comments\" dataType=\"VARCHAR\"/>", ""));
     TableMeta tableMeta = new TableMeta(tableNode);
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(1, columns.size());
 }

 @Test
 public void testTableMetaMultipleColumns() {
     Node tableNode = parseXML(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(2, columns.size());
 }

 private Node parseXML(String xml) {
     try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document document = builder.parse(new InputSource(new StringReader(xml)));
         return document.getDocumentElement();
     } catch (Exception e) {
         throw new RuntimeException(e);
     }
 }

}