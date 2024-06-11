package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final Logger LOGGER = Logger.getLogger(TableMeta.class.getName());

 @Test
 public void testTableMetaConstructionWithValidAttributes() {
 TableMeta tableMeta = new TableMeta(createValidTableNode());

 assertEquals("tableName", tableMeta.getName());
 assertEquals("tableComments", tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 LOGGER.fine("Found XML table metadata for tableName remoteCatalog: null remoteSchema: null comments: tableComments");
 }

 @Test
 public void testTableMetaConstructionWithEmptyAttributes() {
 TableMeta tableMeta = new TableMeta(createEmptyTableNode());

 assertNull(tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 LOGGER.fine("Found XML table metadata for null remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test
 public void testTableMetaConstructionWithCommentsAttribute() {
 TableMeta tableMeta = new TableMeta(createCommentsTableNode());

 assertEquals("tableName", tableMeta.getName());
 assertEquals("tableComments", tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 LOGGER.fine("Found XML table metadata for tableName remoteCatalog: null remoteSchema: null comments: tableComments");
 }

 @Test
 public void testTableMetaConstructionWithRemoteSchemaAndRemoteCatalogAttributes() {
 TableMeta tableMeta = new TableMeta(createRemoteTableNode());

 assertEquals("tableName", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
 assertEquals("remoteSchema", tableMeta.getRemoteSchema());
 LOGGER.fine("Found XML table metadata for tableName remoteCatalog: remoteCatalog remoteSchema: remoteSchema comments: null");
 }

 @Test
 public void testGetColumns() {
 TableMeta tableMeta = new TableMeta(createValidTableNode());

 List<TableColumnMeta> columns = tableMeta.getColumns();

 assertEquals(5, columns.size());
 for (int i = 0; i < columns.size(); i++) {
 TableColumnMeta columnMeta = columns.get(i);
 assertTrue(columnMeta instanceof TableColumnMeta);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetColumnsWhenTableMetaHasNoColumns() {
 TableMeta tableMeta = new TableMeta(createEmptyTableNode());

 List<TableColumnMeta> columns = tableMeta.getColumns();
 fail("Expected NullPointerException");
 }

 private Node createValidTableNode() {
 Element tableElement = new Element("table");
 tableElement.setAttribute("name", "tableName");
 tableElement.setAttribute("comments", "tableComments");
 tableElement.setAttribute("remoteSchema", "");
 tableElement.setAttribute("remoteCatalog", "");
 Element columnElement1 = new Element("column");
 columnElement1.setAttribute("name", "column1Name");
 columnElement1.setAttribute("type", "column1Type");
 Element columnElement2 = new Element("column");
 columnElement2.setAttribute("name", "column2Name");
 columnElement2.setAttribute("type", "column2Type");
 Element columnElement3 = new Element("column");
 columnElement3.setAttribute("name", "column3Name");
 columnElement3.setAttribute("type", "column3Type");
 Element columnElement4 = new Element("column");
 columnElement4.setAttribute("name", "column4Name");
 columnElement4.setAttribute("type", "column4Type");
 Element columnElement5 = new Element("column");
 columnElement5.setAttribute("name", "column5Name");
 columnElement5.setAttribute("type", "column5Type");
 tableElement.appendChild(columnElement1);
 tableElement.appendChild(columnElement2);
 tableElement.appendChild(columnElement3);
 tableElement.appendChild(columnElement4);
 tableElement.appendChild(columnElement5);
 return tableElement;
 }

 private Node createEmptyTableNode() {
 Element tableElement = new Element("table");
 return tableElement;
 }

 private Node createCommentsTableNode() {
 Element tableElement = new Element("table");
 tableElement.setAttribute("name", "tableName");
 tableElement.setAttribute("comments", "tableComments");
 return tableElement;
 }

 private Node createRemoteTableNode() {
 Element tableElement = new Element("table");
 tableElement.setAttribute("name", "tableName");
 tableElement.setAttribute("comments", "");
 tableElement.setAttribute("remoteSchema", "remoteSchema");
 tableElement.setAttribute("remoteCatalog", "remoteCatalog");
 return tableElement;
 }
}