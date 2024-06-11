package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(createMockTableNode());
 assertEquals("tableName", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertTrue(tableMeta.getColumns().isEmpty());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 logger.fine("Found XML table metadata for tableName");
 }

 @Test
 public void testTableMetaConstructionWithComments() {
 TableMeta tableMeta = new TableMeta(createMockTableNodeWithComments());
 assertEquals("tableName", tableMeta.getName());
 assertEquals("table comments", tableMeta.getComments());
 assertTrue(tableMeta.getColumns().isEmpty());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 logger.fine("Found XML table metadata for tableName comments: table comments");
 }

 @Test
 public void testTableMetaConstructionWithRemoteCatalogAndSchema() {
 TableMeta tableMeta = new TableMeta(createMockTableNodeWithRemoteCatalogAndSchema());
 assertEquals("tableName", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertTrue(tableMeta.getColumns().isEmpty());
 assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
 assertEquals("remoteSchema", tableMeta.getRemoteSchema());
 logger.fine("Found XML table metadata for tableName remoteCatalog: remoteCatalog remoteSchema: remoteSchema");
 }

 @Test
 public void testTableMetaConstructionWithColumns() {
 TableMeta tableMeta = new TableMeta(createMockTableNodeWithColumns());
 assertEquals("tableName", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertFalse(tableMeta.getColumns().isEmpty());
 logger.fine("Found XML table metadata for tableName");
 }

 @Test
 public void testTableMetaGetName() {
 TableMeta tableMeta = new TableMeta(createMockTableNode());
 assertEquals("tableName", tableMeta.getName());
 }

 @Test
 public void testTableMetaGetComments() {
 TableMeta tableMeta = new TableMeta(createMockTableNodeWithComments());
 assertEquals("table comments", tableMeta.getComments());
 }

 @Test
 public void testTableMetaGetColumns() {
 TableMeta tableMeta = new TableMeta(createMockTableNodeWithColumns());
 assertFalse(tableMeta.getColumns().isEmpty());
 }

 @Test
 public void testTableMetaGetRemoteCatalog() {
 TableMeta tableMeta = new TableMeta(createMockTableNodeWithRemoteCatalogAndSchema());
 assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaGetRemoteSchema() {
 TableMeta tableMeta = new TableMeta(createMockTableNodeWithRemoteCatalogAndSchema());
 assertEquals("remoteSchema", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaNullTableNode() {
 new TableMeta(null);
 }

 private Node createMockTableNode() {
 Element tableElement = new Element("table");
 return tableElement;
 }

 private Node createMockTableNodeWithComments() {
 Element tableElement = new Element("table");
 Attr commentsAttr = new Attr("comments", "table comments");
 tableElement.setAttributes(new NamedNodeMap() {
 {
 setNamedItem(new Attr("name", "tableName"));
 setNamedItem(commentsAttr);
 }
 });
 return tableElement;
 }

 private Node createMockTableNodeWithRemoteCatalogAndSchema() {
 Element tableElement = new Element("table");
 Attr remoteCatalogAttr = new Attr("remoteCatalog", "remoteCatalog");
 Attr remoteSchemaAttr = new Attr("remoteSchema", "remoteSchema");
 tableElement.setAttributes(new NamedNodeMap() {
 {
 setNamedItem(new Attr("name", "tableName"));
 setNamedItem(remoteCatalogAttr);
 setNamedItem(remoteSchemaAttr);
 }
 });
 return tableElement;
 }

 private Node createMockTableNodeWithColumns() {
 Element tableElement = new Element("table");
 Element columnElement1 = new Element("column");
 columnElement1.setTextContent("columnName1");
 Element columnElement2 = new Element("column");
 columnElement2.setTextContent("columnName2");
 tableElement.appendChild(columnElement1);
 tableElement.appendChild(columnElement2);
 return tableElement;
 }

}