package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.w3c.dom.*;
import org.mockito.Mockito.*;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\" comments=\"This is a test table\" remoteCatalog=\"testCatalog\" remoteSchema=\"testSchema\">" +
         "<Column name=\"id\" type=\"INT\" comments=\"ID column\" />" +
         "<Column name=\"name\" type=\"VARCHAR\" comments=\"Name column\" />" +
         "</Table>";

 @Before
 public void setup() {
 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
 try {
 Document xmlDocument = documentBuilder.parse(new InputSource(new StringReader(XML_TABLE_METADATA)));
 TableMeta tableMeta = new TableMeta(xmlDocument.getElementsByTagName("Table").item(0));
 this.tableMeta = tableMeta;
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTableMetaWithValidNameCommentsColumnsRemoteCatalogAndRemoteSchema() {
 // Test case for constructor with valid arguments
 String expectedName = "testTable";
 String expectedComments = "This is a test table";
 List<TableColumnMeta> expectedColumns = Arrays.asList(
 new TableColumnMeta("id", "INT", "ID column"),
 new TableColumnMeta("name", "VARCHAR", "Name column")
 );
 TableMeta tableMeta = new TableMeta(mock(Node.class));
 assertEquals(expectedName, tableMeta.getName());
 assertEquals(expectedComments, tableMeta.getComments());
 assertEquals(expectedColumns, tableMeta.getColumns());
 assertEquals(null, tableMeta.getRemoteCatalog());
 assertEquals(null, tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaWithNullNode() {
 // Test case for constructor with null Node argument
 TableMeta tableMeta = new TableMeta(null);
 }

 @Test
 public void testGetName() {
 String expectedName = "testTable";
 String actualName = tableMeta.getName();
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testGetComments() {
 String expectedComments = "This is a test table";
 String actualComments = tableMeta.getComments();
 assertEquals(expectedComments, actualComments);
 }

 @Test
 public void testGetColumns() {
 List<TableColumnMeta> expectedColumns = Arrays.asList(
 new TableColumnMeta("id", "INT", "ID column"),
 new TableColumnMeta("name", "VARCHAR", "Name column")
 );
 List<TableColumnMeta> actualColumns = tableMeta.getColumns();
 assertEquals(expectedColumns, actualColumns);
 }

 @Test
 public void testGetRemoteCatalog() {
 String expectedRemoteCatalog = null;
 String actualRemoteCatalog = tableMeta.getRemoteCatalog();
 assertEquals(expectedRemoteCatalog, actualRemoteCatalog);
 }

 @Test
 public void testGetRemoteSchema() {
 String expectedRemoteSchema = null;
 String actualRemoteSchema = tableMeta.getRemoteSchema();
 assertEquals(expectedRemoteSchema, actualRemoteSchema);
 }

 @Test
 public void testTableMetaWithNullComments() {
 // Test case for constructor with null comments
 Node commentsNode = Mockito.mock(Node.class);
 TableMeta tableMeta = new TableMeta(mock(Node.class), commentsNode);
 String actualName = tableMeta.getName();
 String actualComments = tableMeta.getComments();
 List<TableColumnMeta> actualColumns = tableMeta.getColumns();
 String actualRemoteCatalog = tableMeta.getRemoteCatalog();
 String actualRemoteSchema = tableMeta.getRemoteSchema();
 assertEquals(expectedName, actualName);
 assertEquals(null, actualComments);
 assertEquals(expectedColumns, actualColumns);
 assertEquals(null, actualRemoteCatalog);
 assertEquals(null, actualRemoteSchema);
 }

 @Test
 public void testTableMetaWithEmptyComments() {
 // Test case for constructor with empty comments
 Node commentsNode = Mockito.mock(Node.class);
 commentsNode.getNodeValue = () -> "";
 TableMeta tableMeta = new TableMeta(mock(Node.class), commentsNode);
 String actualName = tableMeta.getName();
 String actualComments = tableMeta.getComments();
 List<TableColumnMeta> actualColumns = tableMeta.getColumns();
 String actualRemoteCatalog = tableMeta.getRemoteCatalog();
 String actualRemoteSchema = tableMeta.getRemoteSchema();
 assertEquals(expectedName, actualName);
 assertEquals("", actualComments);
 assertEquals(expectedColumns, actualColumns);
 assertEquals(null, actualRemoteCatalog);
 assertEquals(null, actualRemoteSchema);
 }

 @Test
 public void testTableMetaWithValidRemoteCatalogAndRemoteSchema() {
 // Test case for constructor with valid remoteCatalog and remoteSchema arguments
 Node remoteCatalogNode = Mockito.mock(Node.class);
 Node remoteSchemaNode = Mockito.mock(Node.class);
 TableMeta tableMeta = new TableMeta(mock(Node.class), null, remoteCatalogNode, remoteSchemaNode);
 String expectedRemoteCatalog = "testCatalog";
 String expectedRemoteSchema = "testSchema";
 String actualRemoteCatalog = tableMeta.getRemoteCatalog();
 String actualRemoteSchema = tableMeta.getRemoteSchema();
 assertEquals(expectedRemoteCatalog, actualRemoteCatalog);
 assertEquals(expectedRemoteSchema, actualRemoteSchema);
 }

 @Test
 public void testTableMetaWithInvalidRemoteCatalogAndRemoteSchemaNodes() {
 // Test case for constructor with invalid remoteCatalog and remoteSchema nodes
 Node remoteCatalogNode = Mockito.mock(Node.class);
 Node remoteSchemaNode = Mockito.mock(Node.class);
 TableMeta tableMeta = new TableMeta(mock(Node.class), null, remoteCatalogNode, remoteSchemaNode);
 String actualRemoteCatalog = tableMeta.getRemoteCatalog();
 String actualRemoteSchema = tableMeta.getRemoteSchema();
 assertEquals(null, actualRemoteCatalog);
 assertEquals(null, actualRemoteSchema);
 }

 @Test
 public void testTableMetaWithColumnsNotPresent() {
 // Test case for constructor with no columnNodes
 Node tableNode = Mockito.mock(Node.class);
 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> expectedColumns = Collections.emptyList();
 List<TableColumnMeta> actualColumns = tableMeta.getColumns();
 assertEquals(expectedColumns, actualColumns);
 }

}