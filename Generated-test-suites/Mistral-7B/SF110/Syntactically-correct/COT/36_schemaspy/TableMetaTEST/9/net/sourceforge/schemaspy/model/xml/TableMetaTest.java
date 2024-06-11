package net.sourceforge.schemaspy.model.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMetaTest.class.getName());

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(createXmlTableNode());
 String expectedName = "expectedTableName";
 String expectedComments = "expectedTableComments";
 String expectedRemoteCatalog = "expectedRemoteCatalog";
 String expectedRemoteSchema = "expectedRemoteSchema";
 List<TableColumnMeta> expectedColumns = new ArrayList<>();
 expectedColumns.add(new TableColumnMeta(createXmlColumnNode()));

 TableMeta actualTableMeta = new TableMeta(createXmlTableNode());

 Assert.assertEquals(expectedName, actualTableMeta.getName());
 Assert.assertEquals(expectedComments, actualTableMeta.getComments());
 Assert.assertEquals(expectedColumns, actualTableMeta.getColumns());
 Assert.assertEquals(expectedRemoteCatalog, actualTableMeta.getRemoteCatalog());
 Assert.assertEquals(expectedRemoteSchema, actualTableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullTableNode() {
 new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyNodeList() {
 Node tableNode = createXmlTableNode();
 ((Element) tableNode).setChildNodes(new NodeList());
 new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithMissingNameAttribute() {
 Node tableNode = createXmlTableNode();
 tableNode.getAttributes().removeNamedItem("name");
 new TableMeta(tableNode);
 }

 @Test
 public void testGetName() {
 String name = "testTableName";
 TableMeta tableMeta = new TableMeta(createXmlTableNodeWithNameAttribute(name));
 String actualName = tableMeta.getName();
 Assert.assertEquals(name, actualName);
 }

 @Test
 public void testGetComments() {
 String comments = "testTableComments";
 TableMeta tableMeta = new TableMeta(createXmlTableNodeWithCommentsAttribute(comments));
 String actualComments = tableMeta.getComments();
 Assert.assertEquals(comments, actualComments);
 }

 @Test
 public void testGetColumns() {
 String columnName = "testColumnName";
 TableMeta tableMeta = new TableMeta(createXmlTableNodeWithColumns(columnName));
 List<TableColumnMeta> actualColumns = tableMeta.getColumns();
 TableColumnMeta expectedColumn = new TableColumnMeta(createXmlColumnNodeWithNameAttribute(columnName));
 Assert.assertEquals(expectedColumn, actualColumns.get(0));
 }

 @Test
 public void testGetRemoteCatalog() {
 String remoteCatalog = "testRemoteCatalog";
 TableMeta tableMeta = new TableMeta(createXmlTableNodeWithRemoteCatalogAttribute(remoteCatalog));
 String actualRemoteCatalog = tableMeta.getRemoteCatalog();
 Assert.assertEquals(remoteCatalog, actualRemoteCatalog);
 }

 @Test
 public void testGetRemoteSchema() {
 String remoteSchema = "testRemoteSchema";
 TableMeta tableMeta = new TableMeta(createXmlTableNodeWithRemoteSchemaAttribute(remoteSchema));
 String actualRemoteSchema = tableMeta.getRemoteSchema();
 Assert.assertEquals(remoteSchema, actualRemoteSchema);
 }

 private Node createXmlTableNode() {
 Element tableElement = new Element("table");
 return tableElement;
 }

 private Node createXmlTableNodeWithNameAttribute(String name) {
 Element tableElement = new Element("table");
 tableElement.setAttribute("name", name);
 return tableElement;
 }

 private Node createXmlTableNodeWithCommentsAttribute(String comments) {
 Element tableElement = new Element("table");
 tableElement.setAttribute("comments", comments);
 return tableElement;
 }

 private Node createXmlTableNodeWithColumns(String columnName) {
 Element tableElement = new Element("table");
 Element columnElement = new Element("column");
 columnElement.setAttribute("name", columnName);
 tableElement.appendChild(columnElement);
 return tableElement;
 }

 private Node createXmlTableNodeWithRemoteCatalogAttribute(String remoteCatalog) {
 Element tableElement = new Element("table");
 tableElement.setAttribute("remoteCatalog", remoteCatalog);
 return tableElement;
 }

 private Node createXmlTableNodeWithRemoteSchemaAttribute(String remoteSchema) {
 Element tableElement = new Element("table");
 tableElement.setAttribute("remoteSchema", remoteSchema);
 return tableElement;
 }

}