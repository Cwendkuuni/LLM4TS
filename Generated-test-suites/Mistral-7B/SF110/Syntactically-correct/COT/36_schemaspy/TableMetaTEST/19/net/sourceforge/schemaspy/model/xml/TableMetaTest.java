package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"testTable\" comments=\"A test table\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">" +
         "<Column name=\"id\" comments=\"Primary key\"/>" +
         "<Column name=\"name\" comments=\"Name of the user\"/>" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(createNodeFromXMLString(XML_TABLE_META_DATA));

 assertEquals("testTable", tableMeta.getName());
 assertEquals("A test table", tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertTrue(columns.get(0) instanceof TableColumnMeta);
 assertTrue(columns.get(1) instanceof TableColumnMeta);
 assertEquals("testSchema", tableMeta.getRemoteSchema());
 assertEquals("testCatalog", tableMeta.getRemoteCatalog());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
 new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyNodeName() {
 Node node = createNodeFromXMLString("<Table name=\"\"/>");
 new TableMeta(node);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithMissingNameAttribute() {
 Node node = createNodeFromXMLString("<Table/>");
 try {
 new TableMeta(node);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithDuplicateNameAttributes() {
 Node node = createNodeFromXMLString("<Table name=\"testTable\" name=\"anotherName\"/>");
 try {
 new TableMeta(node);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTableMetaGetters() {
 TableMeta tableMeta = new TableMeta(createNodeFromXMLString(XML_TABLE_META_DATA));
 TableColumnMeta column1 = tableMeta.getColumns().get(0);
 TableColumnMeta column2 = tableMeta.getColumns().get(1);

 assertEquals("testTable", tableMeta.getName());
 assertEquals("A test table", tableMeta.getComments());
 assertEquals(2, tableMeta.getColumns().size());
 assertEquals(column1, tableMeta.getColumns().get(0));
 assertEquals(column2, tableMeta.getColumns().get(1));
 assertEquals("testSchema", tableMeta.getRemoteSchema());
 assertEquals("testCatalog", tableMeta.getRemoteCatalog());
 }

 private Node createNodeFromXMLString(String xmlString) {
 Node node = null;
 try {
 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 Node rootNode = docBuilder.parse(new InputSource(new StringReader(xmlString)));
 rootNode.getDocumentElement().normalize();
 return rootNode;
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to parse XML string");
 }
 return node;
}

}