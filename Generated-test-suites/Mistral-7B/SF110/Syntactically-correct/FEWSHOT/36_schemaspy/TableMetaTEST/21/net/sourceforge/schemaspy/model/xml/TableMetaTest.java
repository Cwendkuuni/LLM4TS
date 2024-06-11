package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"tableName\" comments=\"comments\">" +
         "<Column name=\"columnName1\" comments=\"columnComments1\" />" +
         "<Column name=\"columnName2\" comments=\"columnComments2\" />" +
         "</Table>";

 @Test
 public void testTableMetaConstructionWithValidXML() {
     String xmlString = XML_TABLE_META_TEMPLATE.replace("tableName", "testTable")
             .replace("comments", "This is a test table")
             .replace("columnName1", "col1")
             .replace("columnName2", "col2")
             .replace("columnComments1", "This is column 1")
             .replace("columnComments2", "This is column 2");

     TableMeta tableMeta = new TableMeta(new DocumentBuilderFactory().newDocumentBuilder().parseString(xmlString));

     assertEquals("testTable", tableMeta.getName());
     assertEquals("This is a test table", tableMeta.getComments());
     assertTrue(tableMeta.getColumns().size() == 2);
     TableColumnMeta col1 = tableMeta.getColumns().get(0);
     TableColumnMeta col2 = tableMeta.getColumns().get(1);
     assertEquals("col1", col1.getName());
     assertEquals("This is column 1", col1.getComments());
     assertEquals("col2", col2.getName());
     assertEquals("This is column 2", col2.getComments());
 }

 @Test
 public void testTableMetaConstructionWithEmptyXML() {
     String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"\" comments=\"\"/>";

     TableMeta tableMeta = new TableMeta(new DocumentBuilderFactory().newDocumentBuilder().parseString(xmlString));

     assertNull(tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertTrue(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testTableMetaConstructionWithNullXML() {
     TableMeta tableMeta = new TableMeta(null);

     assertNull(tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertTrue(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testTableMetaLogging() {
     String xmlString = XML_TABLE_META_TEMPLATE.replace("tableName", "testTable")
             .replace("comments", "This is a test table");

     TableMeta tableMeta = new TableMeta(new DocumentBuilderFactory().newDocumentBuilder().parseString(xmlString));

     assertTrue(TableMeta.logger.isLoggable(Level.FINE));
     assertTrue(TableMeta.logger.isLoggable(Level.INFO));
     assertTrue(TableMeta.logger.isLoggable(Level.WARNING));
     assertTrue(TableMeta.logger.isLoggable(Level.ERROR));

     TableMeta.logger.fine("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: This is a test table");
 }

 @Test
 public void testTableMetaGetters() {
     String xmlString = XML_TABLE_META_TEMPLATE.replace("tableName", "testTable")
             .replace("comments", "This is a test table");

     TableMeta tableMeta = new TableMeta(new DocumentBuilderFactory().newDocumentBuilder().parseString(xmlString));

     assertEquals("testTable", tableMeta.getName());
     assertEquals("This is a test table", tableMeta.getComments());
     assertTrue(tableMeta.getColumns().size() == 2);
     assertTrue(tableMeta.getRemoteCatalog() == null);
     assertTrue(tableMeta.getRemoteSchema() == null);
 }

 @Test
 public void testTableMetaGettersWithRemoteCatalogAndSchema() {
     String xmlString = XML_TABLE_META_TEMPLATE.replace("tableName", "testTable")
             .replace("comments", "This is a test table")
             .replace("remoteCatalog", "remoteCatalog1")
             .replace("remoteSchema", "remoteSchema1");

     TableMeta tableMeta = new TableMeta(new DocumentBuilderFactory().newDocumentBuilder().parseString(xmlString));

     assertEquals("testTable", tableMeta.getName());
     assertEquals("This is a test table", tableMeta.getComments());
     assertTrue(tableMeta.getColumns().size() == 2);
     assertEquals("remoteCatalog1", tableMeta.getRemoteCatalog());
     assertEquals("remoteSchema1", tableMeta.getRemoteSchema());
 }
}