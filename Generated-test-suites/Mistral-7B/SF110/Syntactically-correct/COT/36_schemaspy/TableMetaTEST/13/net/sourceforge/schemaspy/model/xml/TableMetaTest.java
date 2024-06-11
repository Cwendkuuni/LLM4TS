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
         "<Table name=\"testTable\" comments=\"Test table comments\" remoteSchema=\"testSchema\"" +
         " remoteCatalog=\"testCatalog\">" +
         "<Column name=\"id\" type=\"int\" comments=\"Column id comments\"/>" +
         "<Column name=\"name\" type=\"varchar\" comments=\"Column name comments\"/>" +
         "</Table>";

 @Test
 public void testTableMetaInstance() {
     Node tableNode = parseXmlString(XML_TABLE_META_DATA);
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNotNull(tableMeta);
     assertEquals("testTable", tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertNull(tableMeta.getRemoteCatalog());
     assertNull(tableMeta.getRemoteSchema());
     assertFalse(tableMeta.getColumns().isEmpty());
     TableColumnMeta firstColumnMeta = tableMeta.getColumns().get(0);
     assertEquals("id", firstColumnMeta.getName());
     assertEquals("int", firstColumnMeta.getType());
     assertNull(firstColumnMeta.getComments());
     TableColumnMeta secondColumnMeta = tableMeta.getColumns().get(1);
     assertEquals("name", secondColumnMeta.getName());
     assertEquals("varchar", secondColumnMeta.getType());
     assertNotNull(secondColumnMeta.getComments());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaNullNode() {
     TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaEmptyColumnsNodeList() {
     Node tableNode = parseXmlString("<Table name=\"testTable\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaNullNameAttribute() {
     Node tableNode = parseXmlString("<Table/>");
     TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaCommentsNullNode() {
     Node tableNode = parseXmlString("<Table name=\"testTable\" comments=\"\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaCommentsNotNullNode() {
     Node tableNode = parseXmlString("<Table name=\"testTable\" comments=\"Test table comments\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertEquals("Test table comments", tableMeta.getComments());
 }

 @Test
 public void testTableMetaRemoteSchemaNullNode() {
     Node tableNode = parseXmlString("<Table name=\"testTable\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaRemoteSchemaNotNullNode() {
     Node tableNode = parseXmlString("<Table name=\"testTable\" remoteSchema=\"testSchema\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaRemoteCatalogNullNode() {
     Node tableNode = parseXmlString("<Table name=\"testTable\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaRemoteCatalogNotNullNode() {
     Node tableNode = parseXmlString("<Table name=\"testTable\" remoteCatalog=\"testCatalog\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertEquals("testCatalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaColumnsEmptyList() {
     Node tableNode = parseXmlString("<Table name=\"testTable\" />");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertTrue(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testTableMetaColumnsNotEmptyList() {
     Node tableNode = parseXmlString(XML_TABLE_META_DATA);
     TableMeta tableMeta = new TableMeta(tableNode);
     assertFalse(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testTableMetaGetColumnByName() {
     Node tableNode = parseXmlString(XML_TABLE_META_DATA);
     TableMeta tableMeta = new TableMeta(tableNode);
     TableColumnMeta columnMeta = tableMeta.getColumnByName("id");
     assertNotNull(columnMeta);
     assertEquals("id", columnMeta.getName());
 }

 @Test(expected = NoSuchElementException.class)
 public void testTableMetaGetColumnByNameNotFound() {
     Node tableNode = parseXmlString(XML_TABLE_META_DATA);
     TableMeta tableMeta = new TableMeta(tableNode);
     tableMeta.getColumnByName("nonExistentColumn");
 }

 private Node parseXmlString(String xmlString) {
     try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         Document document = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
         document.getDocumentElement();
         return document.getDocumentElement();
     } catch (Exception e) {
         throw new RuntimeException(e);
     }
 }

}