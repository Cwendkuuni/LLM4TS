package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String TABLE_NAME = "testTable";
 private static final String TABLE_COMMENTS = "Test Table";
 private static final String TABLE_REMOTE_CATALOG = "testCatalog";
 private static final String TABLE_REMOTE_SCHEMA = "testSchema";
 private static final String COLUMN_NAME_0 = "col0";
 private static final String COLUMN_NAME_1 = "col1";
 private static final String COLUMN_COMMENTS_0 = "Column 0";
 private static final String COLUMN_COMMENTS_1 = "Column 1";

 @Test
 public void testTableMetaWithValidAttributes() {
 TableMeta tableMeta = new TableMeta(getSampleTableNode());

 assertEquals(TABLE_NAME, tableMeta.getName());
 assertNotNull(tableMeta.getComments());
 assertEquals(TABLE_COMMENTS, tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());

 Logger logger = Logger.getLogger(TableMeta.class.getName());
 logger.fine("Found XML table metadata for " + TABLE_NAME);

 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());

 TableColumnMeta columnMeta0 = columns.get(0);
 assertEquals(COLUMN_NAME_0, columnMeta0.getName());
 assertNull(columnMeta0.getComments());

 TableColumnMeta columnMeta1 = columns.get(1);
 assertEquals(COLUMN_NAME_1, columnMeta1.getName());
 assertEquals(COLUMN_COMMENTS_1, columnMeta1.getComments());
 }

 @Test
 public void testTableMetaWithMissingNameAttribute() {
 Node tableNode = getSampleTableNodeWithMissingName();

 try {
 TableMeta tableMeta = new TableMeta(tableNode);
 fail("Expected TableMetaInitializationException");
 } catch (TableMetaInitializationException e) {
 // Expected
 }
 }

 @Test
 public void testTableMetaWithMissingCommentsAttribute() {
 Node tableNode = getSampleTableNodeWithMissingComments();
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaWithMissingColumnsAttribute() {
 Node tableNode = getSampleTableNodeWithMissingColumns();

 try {
 TableMeta tableMeta = new TableMeta(tableNode);
 fail("Expected TableMetaInitializationException");
 } catch (TableMetaInitializationException e) {
 // Expected
 }
 }

 @Test
 public void testTableMetaWithEmptyColumnsAttribute() {
 Node tableNode = getSampleTableNodeWithEmptyColumns();
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals(0, tableMeta.getColumns().size());
 }

 @Test
 public void testTableMetaWithInvalidNodeType() {
 Node tableNode = getInvalidNodeType();

 try {
 TableMeta tableMeta = new TableMeta(tableNode);
 fail("Expected TableMetaInitializationException");
 } catch (TableMetaInitializationException e) {
 // Expected
 }
 }

 @Test
 public void testTableMetaWithMultipleCommentAttributes() {
 Node tableNode = getSampleTableNodeWithMultipleCommentNodes();
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_COMMENTS, tableMeta.getComments());
 }

 @Test
 public void testTableMetaWithRemoteCatalogAndRemoteSchema() {
 Node tableNode = getSampleTableNodeWithRemoteCatalogAndSchema();
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals(TABLE_REMOTE_CATALOG, tableMeta.getRemoteCatalog());
 assertEquals(TABLE_REMOTE_SCHEMA, tableMeta.getRemoteSchema());
 }

 private Node getSampleTableNode() {
 Element tableElement = createElement("Table", TABLE_NAME);
 setAttribute(tableElement, "name", TABLE_NAME);
 setAttribute(tableElement, "comments", TABLE_COMMENTS);
 setAttribute(tableElement, "remoteSchema", TABLE_REMOTE_SCHEMA);
 setAttribute(tableElement, "remoteCatalog", TABLE_REMOTE_CATALOG);

 NodeList columnNodes = createColumnNodes();
 tableElement.appendChild(columnNodes.item(0));
 tableElement.appendChild(columnNodes.item(1));

 return tableElement;
 }

 private Node getSampleTableNodeWithMissingName() {
 Element tableElement = createElement("Table", TABLE_NAME);
 setAttribute(tableElement, "comments", TABLE_COMMENTS);
 NodeList columnNodes = createColumnNodes();
 tableElement.appendChild(columnNodes.item(0));
 tableElement.appendChild(columnNodes.item(1));
 return tableElement;
 }

 private Node getSampleTableNodeWithMissingComments() {
 Element tableElement = createElement("Table", TABLE_NAME);
 setAttribute(tableElement, "name", TABLE_NAME);
 NodeList columnNodes = createColumnNodes();
 tableElement.appendChild(columnNodes.item(0));
 tableElement.appendChild(columnNodes.item(1));
 return tableElement;
 }

 private Node getSampleTableNodeWithMissingColumns() {
 Element tableElement = createElement("Table", TABLE_NAME);
 setAttribute(tableElement, "name", TABLE_NAME);
 setAttribute(tableElement, "comments", TABLE_COMMENTS);
 return tableElement;
 }

 private Node getSampleTableNodeWithEmptyColumns() {
 Element tableElement = createElement("Table", TABLE_NAME);
 setAttribute(tableElement, "name", TABLE_NAME);
 setAttribute(tableElement, "comments", TABLE_COMMENTS);
 Node emptyColumns = createElement("columns");
 tableElement.appendChild(emptyColumns);
 return tableElement;
 }

 private Node getInvalidNodeType() {
 Element tableElement = createElement("InvalidType", TABLE_NAME);
 return tableElement;
 }

 private Node getSampleTableNodeWithMultipleCommentNodes() {
 Element tableElement = createElement("Table", TABLE_NAME);
 Node commentNode1 = createTextNode("Comments 1");
 Node commentNode2 = createTextNode("Comments 2");
 setAttribute(tableElement, "comments", TABLE_COMMENTS);
 tableElement.appendChild(commentNode1);
 tableElement.appendChild(commentNode2);

 NodeList columnNodes = createColumnNodes();
 tableElement.appendChild(columnNodes.item(0));
 tableElement.appendChild(columnNodes.item(1));

 return tableElement;
 }

 private Node getSampleTableNodeWithRemoteCatalogAndSchema() {
 Element tableElement = createElement("Table", TABLE_NAME);
 setAttribute(tableElement, "name", TABLE_NAME);
 setAttribute(tableElement, "comments", TABLE_COMMENTS);
 setAttribute(tableElement, "remoteSchema", TABLE_REMOTE_SCHEMA);
 setAttribute(tableElement, "remoteCatalog", TABLE_REMOTE_CATALOG);

 NodeList columnNodes = createColumnNodes();
 tableElement.appendChild(columnNodes.item(0));
 tableElement.appendChild(columnNodes.item(1));

 return tableElement;
 }

 private NodeList createColumnNodes() {
 NodeList columnNodes = getNodeListFromResources("/columns.xml");
 return columnNodes;
 }

 private Element createElement(String tagName, String name) {
 Element element = getDocument().createElement(tagName);
 setAttribute(element, "name", name);
 return element;
 }

 private void setAttribute(Node node, String name, String value) {
 node.setNodeValue(value);
 node.setNodeName(name);
 }

 private Document getDocument() {
 return getDocumentBuilder().newDocument();
 }

 private DocumentBuilder getDocumentBuilder() {
 return getDocumentBuilderFactory().newDocumentBuilder();
 }

 private DocumentBuilderFactory getDocumentBuilderFactory() {
 return JSDOMEngine.getDocumentBuilderFactory();
 }

 private NodeList getNodeListFromResources(String resourcePath) {
 return JSDOMEngine.getDocumentBuilderFactory().newDocumentBuilder().parse(getClass().getResource(resourcePath)).getElementsByTagName("column");
 }
}