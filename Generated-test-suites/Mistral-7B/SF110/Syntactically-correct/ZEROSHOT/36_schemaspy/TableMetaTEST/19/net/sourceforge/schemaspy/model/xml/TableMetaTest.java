package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.namespace.QName;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\" comments=\"This is a test table\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">" +
         "<Column name=\"col1\" comments=\"This is column 1\" type=\"VARCHAR(10)\" nullable=\"true\"/>" +
         "<Column name=\"col2\" comments=\"This is column 2\" type=\"INTEGER\" nullable=\"false\"/>" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA, null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("testTable", tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertNull(tableMeta.getRemoteCatalog());
     assertNull(tableMeta.getRemoteSchema());
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(2, columns.size());

     TableColumnMeta col1 = columns.get(0);
     assertEquals("col1", col1.getName());
     assertEquals("This is column 1", col1.getComments());
     assertNull(col1.getType());
     assertTrue(col1.isNullable());

     TableColumnMeta col2 = columns.get(1);
     assertEquals("col2", col2.getName());
     assertNull(col2.getComments());
     assertEquals("INTEGER", col2.getType().toString());
     assertFalse(col2.isNullable());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionNullNode() {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = null;
     new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionEmptyNode() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString("", null).getDocumentElement();
     new TableMeta(tableNode);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaGetCommentsNullNode() {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA, null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     tableMeta.setName(null);
     assertNull(tableMeta.getName());
 }

 @Test
 public void testTableMetaGetCommentsEmptyString() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA.replaceAll("comments=\"[^\"]+\"", "comments=\"\""), null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaGetCommentsNonTrimmed() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA, null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     String comments = tableMeta.getComments();
     assertEquals("This is a test table", comments);
 }

 @Test
 public void testTableMetaGetRemoteCatalogNullNode() {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA, null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaGetRemoteCatalogEmptyString() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA.replaceAll("remoteSchema=\"[^\"]+\"", "remoteSchema=\"\""), null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGetRemoteCatalogNonTrimmed() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA, null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     String remoteCatalog = tableMeta.getRemoteCatalog();
     assertEquals("testCatalog", remoteCatalog);
 }

 @Test
 public void testTableMetaGetColumnsEmptyList() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA.replaceAll("<Column[^>]*>", ""), null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(0, columns.size());
 }

 @Test
 public void testTableMetaGetColumnsNullNode() {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = null;
     TableMeta tableMeta = new TableMeta(tableNode);
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(0, columns.size());
 }

 @Test
 public void testTableMetaGetColumns() throws Exception {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     Node tableNode = new DOMParser().parseFromString(XML_TABLE_METADATA, null).getDocumentElement();
     TableMeta tableMeta = new TableMeta(tableNode);
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(2, columns.size());
 }

 @Test
 public void testTableMetaLogger() {
     Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
     new TableMeta(new DOMParser().parseFromString(XML_TABLE_METADATA, null).getDocumentElement());
     assertTrue(TableMeta.logger.isLoggable(Level.FINE));
 }

}