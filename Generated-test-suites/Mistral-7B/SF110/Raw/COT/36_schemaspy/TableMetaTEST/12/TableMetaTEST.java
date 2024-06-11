package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.w3c.dom.*;
import org.xml.namespace.QName;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\"?>\n"
         + "<Table name=\"testTable\" comments=\"Test table comment\">\n"
         + "<column name=\"col1\" type=\"int\" comments=\"Column comment 1\"/>\n"
         + "<column name=\"col2\" type=\"varchar\" comments=\"Column comment 2\"/>\n"
         + "</Table>";

 @BeforeClass
 public static void setupClass() {
 TableMeta.logger.setLevel(Level.FINE);
 }

 @AfterClass
 public static void teardownClass() {
 // No cleanup needed
 }

 @Before
 public void setup() {
 // No setup needed
 }

 @After
 public void teardown() {
 // No teardown needed
 }

 @Test
 public void testTableMetaConstruction() {
 Node tableNode = createTestNode(XML_TABLE_META_DATA);
 TableMeta tableMeta = new TableMeta(tableNode);

 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertFalse(tableMeta.getColumns().isEmpty());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaWithComments() {
 Node tableNode = createTestNode(XML_TABLE_META_DATA);
 TableMeta tableMeta = new TableMeta(tableNode);

 assertEquals("Test table comment", tableMeta.getComments());
 }

 @Test
 public void testTableMetaWithRemoteCatalogAndSchema() {
 String remoteCatalog = "testRemoteCatalog";
 String remoteSchema = "testRemoteSchema";
 Node tableNode = createTestNodeWithRemoteInfo(XML_TABLE_META_DATA, remoteCatalog, remoteSchema);
 TableMeta tableMeta = new TableMeta(tableNode);

 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertFalse(tableMeta.getColumns().isEmpty());
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaWithNullTableNode() {
 TableMeta tableMeta = new TableMeta(null);
 fail("Expected NullPointerException, but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaWithEmptyTableNode() {
 Node tableNode = createTestNode("");
 TableMeta tableMeta = new TableMeta(tableNode);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaWithMissingNameAttribute() {
 Node tableNode = createTestNodeWithMissingNameAttribute(XML_TABLE_META_DATA);
 try {
 new TableMeta(tableNode);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaWithMissingColumnNodes() {
 Node tableNode = createTestNodeWithMissingColumnNodes(XML_TABLE_META_DATA);
 try {
 new TableMeta(tableNode);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTableMetaWithInvalidColumnIndex() {
 Node tableNode = createTestNode(XML_TABLE_META_DATA);
 TableMeta tableMeta = new TableMeta(tableNode);

 try {
 TableColumnMeta columnMeta = tableMeta.getColumns().get(Integer.MAX_VALUE);
 fail("Expected IndexOutOfBoundsException, but no exception was thrown.");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 private Node createTestNode(String xmlData) {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = null;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 fail("Failed to create DocumentBuilder.");
 }
 Document document = null;
 try {
 document = builder.parse(new InputSource(new StringReader(xmlData)));
 } catch (SAXException e) {
 fail("Failed to parse XML data.");
 } catch (IOException e) {
 fail("Failed to read XML data.");
 }
 return document.getDocumentElement();
 }

 private Node createTestNodeWithRemoteInfo(String xmlData, String remoteCatalog, String remoteSchema) {
 String xmlWithRemoteInfo = xmlData.replaceAll("(?i)remoteSchema=.*|(?i)remoteCatalog=.*", "remoteCatalog=\"" + remoteCatalog + "\" remoteSchema=\"" + remoteSchema + "\"");
 return createTestNode(xmlWithRemoteInfo).getChildNodes().item(0);
 }

 private Node createTestNodeWithMissingNameAttribute(String xmlData) {
 String xmlWithMissingNameAttribute = xmlData.replaceAll("name=.*", "");
 return createTestNode(xmlWithMissingNameAttribute);
 }

 private Node createTestNodeWithMissingColumnNodes(String xmlData) {
 String xmlWithMissingColumnNodes = xmlData.replaceAll("<column>(.*)</column>", "");
 return createTestNode(xmlWithMissingColumnNodes);
 }
}