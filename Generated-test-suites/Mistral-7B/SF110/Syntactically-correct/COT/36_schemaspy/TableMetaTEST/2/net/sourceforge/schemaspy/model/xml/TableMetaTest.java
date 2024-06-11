package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.w3c.dom.*;
import org.xml.XMLConstants;
import org.xml.namespace.QName;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<Table name=\"testTable\" comments=\"This is a test table\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\">" +
         "<Column name=\"column1\" type=\"int\" comments=\"Column 1\"/>" +
         "<Column name=\"column2\" type=\"varchar\" comments=\"Column 2\"/>" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() {
     Node tableNode = createNodeFromString(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("testTable", tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertNotNull(tableMeta.getColumns());
     assertEquals(2, tableMeta.getColumns().size());
     assertEquals("testCatalog", tableMeta.getRemoteCatalog());
     assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
     TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyName() {
     Node tableNode = createNodeFromString("<Table name=\"\" comments=\"testComments\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithNullNameNode() {
     Node tableNode = createNodeFromString("<Table name=\"\" comments=\"testComments\"/>");
     Node nameNode = null;
     TableMeta tableMeta = new TableMeta(tableNode);
     tableMeta.setName(nameNode);
 }

 @Test
 public void testTableMetaGetters() {
     Node tableNode = createNodeFromString(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("testTable", tableMeta.getName());
     assertEquals("This is a test table", tableMeta.getComments());
     assertNotNull(tableMeta.getColumns());
     assertEquals(2, tableMeta.getColumns().size());
     assertEquals("testCatalog", tableMeta.getRemoteCatalog());
     assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGetterWithNullName() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"testComments\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     tableMeta.setName(null);
     assertNull(tableMeta.getName());
 }

 @Test
 public void testTableMetaGetterWithEmptyComments() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertEquals("", tableMeta.getComments());
 }

 @Test
 public void testTableMetaGetterWithNullCommentsNode() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     Node commentsNode = null;
     tableMeta.setComments(commentsNode);
     assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaGetterWithNullColumnsNode() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"testComments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     tableMeta.setColumns(null);
     assertNotNull(tableMeta.getColumns());
     assertEquals(0, tableMeta.getColumns().size());
 }

 @Test
 public void testTableMetaGetterWithEmptyColumnsList() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"testComments\" remoteSchema=\"testSchema\" remoteCatalog=\"testCatalog\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     List<TableColumnMeta> columns = new ArrayList<>();
     tableMeta.setColumns(columns);
     assertNotNull(tableMeta.getColumns());
     assertEquals(0, tableMeta.getColumns().size());
 }

 @Test
 public void testTableMetaGetterWithNullRemoteCatalogNode() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"testComments\" remoteSchema=\"testSchema\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     tableMeta.setRemoteCatalog(null);
     assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaGetterWithEmptyRemoteCatalog() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"testComments\" remoteSchema=\"testSchema\" remoteCatalog=\"\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertEquals("", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaGetterWithNullRemoteSchemaNode() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"testComments\" remoteCatalog=\"testCatalog\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     tableMeta.setRemoteSchema(null);
     assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaGetterWithEmptyRemoteSchema() {
     Node tableNode = createNodeFromString("<Table name=\"testTable\" comments=\"testComments\" remoteCatalog=\"testCatalog\" remoteSchema=\"\"/>");
     TableMeta tableMeta = new TableMeta(tableNode);
     assertEquals("", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaLogger() {
     Node tableNode = createNodeFromString(XML_TABLE_METADATA);
     TableMeta tableMeta = new TableMeta(tableNode);
     TableMeta.logger.isLoggable(Level.FINE);
     TableMeta.logger.log(Level.FINE, "Test log message");
     TableMeta.logger.info("Test info message");
     TableMeta.logger.warning("Test warning message");
     TableMeta.logger.severe("Test severe message");
     TableMeta tableMeta2 = new TableMeta(tableNode);
     assertEquals("Found XML table metadata for testTable remoteCatalog: testCatalog remoteSchema: testSchema comments: This is a test table", TableMeta.logger.getMessage(TableMeta.logger.getMostRecentLevel()));
 }

 private Node createNodeFromString(String xmlString) {
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
     DocumentBuilder builder = null;
     try {
         builder = factory.newDocumentBuilder();
     } catch (ParserConfigurationException e) {
         e.printStackTrace();
     }
     Document doc = null;
     try {
         doc = builder.parse(new InputSource(new StringReader(xmlString)));
     } catch (SAXException | IOException e) {
         e.printStackTrace();
     }
     return doc.getDocumentElement();
 }

}