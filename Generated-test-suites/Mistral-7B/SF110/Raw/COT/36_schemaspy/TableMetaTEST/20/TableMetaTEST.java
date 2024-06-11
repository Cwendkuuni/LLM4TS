package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

private TableMeta tableMeta;
private Node tableNode;

@Before
public void setup() {
// Set up the test data
String xmlData = "<table name=\"testTable\" comments=\"This is a test table\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">"
+ "<column name=\"col1\" type=\"INT\"/>"
+ "<column name=\"col2\" type=\"VARCHAR(50)\"/>"
+ "</table>";
tableNode = new DOMParser().parseFromString(xmlData, "text/xml").getDocument().getDocumentElement();
}

@Test
public void testTableMetaConstruction() {
// Test the basic construction of TableMeta
tableMeta = new TableMeta(tableNode);
assertNotNull(tableMeta.getName());
assertEquals("testTable", tableMeta.getName());
assertNull(tableMeta.getComments());
assertNull(tableMeta.getRemoteCatalog());
assertNull(tableMeta.getRemoteSchema());
assertTrue(tableMeta.getColumns().isEmpty());
TableMeta.logger.fine("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: null");
}

@Test
public void testTableMetaWithComments() {
// Test the construction of TableMeta with comments
tableNode.getAttributes().setNamedItem(new Attribute("comments", "This is a test comment."));
tableMeta = new TableMeta(tableNode);
assertEquals("This is a test comment.", tableMeta.getComments());
}

@Test
public void testTableMetaWithRemoteCatalogAndSchema() {
// Test the construction of TableMeta with remote catalog and schema
tableNode.getAttributes().setNamedItem(new Attribute("remoteSchema", "testSchemaRemote"));
tableNode.getAttributes().setNamedItem(new Attribute("remoteCatalog", "testCatalogRemote"));
tableMeta = new TableMeta(tableNode);
assertEquals("testCatalogRemote", tableMeta.getRemoteCatalog());
assertEquals("testSchemaRemote", tableMeta.getRemoteSchema());
}

@Test
public void testTableMetaWithMissingAttributes() {
// Test the construction of TableMeta with missing attributes
tableNode.getAttributes().removeNamedItem("name");
try {
tableMeta = new TableMeta(tableNode);
fail("Expected IllegalStateException as name attribute is missing.");
} catch (IllegalStateException e) {
// Expected exception
}
}

@Test
public void testTableMetaWithMissingColumns() {
// Test the construction of TableMeta with missing columns
tableNode.getElementsByTagName("column").item(0).getParentNode().removeChild(tableNode.getElementsByTagName("column").item(0));
try {
tableMeta = new TableMeta(tableNode);
fail("Expected IllegalStateException as columns are missing.");
} catch (IllegalStateException e) {
// Expected exception
}
}

@Test
public void testGetName() {
// Test the getName method
tableMeta = new TableMeta(tableNode);
assertEquals("testTable", tableMeta.getName());
}

@Test
public void testGetComments() {
// Test the getComments method
tableMeta = new TableMeta(tableNode);
assertEquals("This is a test comment.", tableMeta.getComments());
}

@Test
public void testGetColumns() {
// Test the getColumns method
tableMeta = new TableMeta(tableNode);
assertEquals(2, tableMeta.getColumns().size());
}

@Test
public void testGetRemoteCatalog() {
// Test the getRemoteCatalog method
tableMeta = new TableMeta(tableNode);
assertEquals("testCatalog", tableMeta.getRemoteCatalog());
}

@Test
public void testGetRemoteSchema() {
// Test the getRemoteSchema method
tableMeta = new TableMeta(tableNode);
assertEquals("testSchema", tableMeta.getRemoteSchema());
}

@Test(expected = NullPointerException.class)
public void testGetColumnsNullNode() {
// Test the getColumns method with null node
tableMeta = new TableMeta(null);
}

@Test
public void testGetColumnsEmptyList() {
// Test the getColumns method with empty column list
TableMeta tableMetaEmpty = new TableMeta(new Element("table"));
assertEquals(0, tableMetaEmpty.getColumns().size());
}
}