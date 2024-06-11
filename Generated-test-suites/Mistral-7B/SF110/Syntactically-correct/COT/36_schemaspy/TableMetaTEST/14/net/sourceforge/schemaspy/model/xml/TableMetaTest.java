package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\"?>\n" +
         "<Table name=\"testTable\" comments=\"This is a test table\"><!--comment-->\n" +
         "<Column name=\"col1\" type=\"INT\" comments=\"Test column 1\"/\n" +
         "<Column name=\"col2\" comments=\"Test column 2\"/>\n" +
         "</Table>";

 @Test
 public void testTableMetaConstructor() {
     Node tableNode = createXMLNodeFromString(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("testTable", tableMeta.getName());
     assertNull(tableMeta.getComments());
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(2, columns.size());
     TableColumnMeta col1 = columns.get(0);
     assertEquals("col1", col1.getName());
     assertEquals("Test column 1", col1.getComments());
     TableColumnMeta col2 = columns.get(1);
     assertNull(col2.getComments());
     assertEquals("", tableMeta.getRemoteCatalog());
     assertEquals("", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructorNullNode() {
     Node tableNode = null;
     try {
         new TableMeta(tableNode);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructorEmptyTableName() {
     Node tableNode = createXMLNodeFromString("<Table />");
     try {
         new TableMeta(tableNode);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testGetters() {
     Node tableNode = createXMLNodeFromString(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("testTable", tableMeta.getName());
     assertEquals("This is a test table\r\n<!--comment-->", tableMeta.getComments());
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(2, columns.size());
     TableColumnMeta col1 = columns.get(0);
     assertEquals("col1", col1.getName());
     assertEquals("Test column 1", col1.getComments());
     TableColumnMeta col2 = columns.get(1);
     assertNull(col2.getComments());
     assertEquals("", tableMeta.getRemoteCatalog());
     assertEquals("", tableMeta.getRemoteSchema());
 }

 private Node createXMLNodeFromString(String xmlString) {
     try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         return documentBuilder.parse(new InputSource(new StringReader(xmlString)));
     } catch (Exception e) {
         throw new RuntimeException(e);
     }
 }

}