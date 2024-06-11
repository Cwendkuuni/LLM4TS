package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private Logger logger;
 private Document doc;
 private Element tableNode;
 private TableMeta tableMeta;

 @Before
 public void setup() {
 logger = Logger.getLogger(TableMeta.class.getName());
 doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 tableNode = doc.createElement("table");
 }

 @Test
 public void testTableMetaConstruction() {
 // Test empty TableMeta
 tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 assertFalse(tableMeta.getColumns().isEmpty());
 TableMeta.logger.fine("");

 // Test TableMeta with name, comments, remoteSchema and remoteCatalog
 tableNode.setAttribute("name", "testTable");
 tableNode.appendChild(doc.createTextNode("Test table comments"));
 tableNode.setAttribute("remoteSchema", "testRemoteSchema");
 tableNode.setAttribute("remoteCatalog", "testRemoteCatalog");
 tableMeta = new TableMeta(tableNode);
 assertEquals("testTable", tableMeta.getName());
 assertEquals("Test table comments", tableMeta.getComments());
 assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
 assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
 TableMeta.logger.fine("Found XML table metadata for testTable remoteCatalog: testRemoteCatalog remoteSchema: testRemoteSchema comments: Test table comments");

 // Test TableMeta with no name
 tableNode.removeAttribute("name");
 tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getName());
 TableMeta.logger.fine("");

 // Test TableMeta with no comments
 Node commentsNode = doc.createTextNode("Test table comments");
 tableNode.appendChild(commentsNode);
 tableNode.removeAttribute("comments");
 tableNode.removeAttribute("remarks");
 tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getComments());
 TableMeta.logger.fine("");

 // Test TableMeta with no remoteSchema and remoteCatalog
 tableNode.removeAttribute("remoteSchema");
 tableNode.removeAttribute("remoteCatalog");
 tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getRemoteSchema());
 assertNull(tableMeta.getRemoteCatalog());
 TableMeta.logger.fine("");
 }

 @Test
 public void testGetColumns() {
 tableNode.appendChild(createColumnNode("id", "int", "1"));
 tableNode.appendChild(createColumnNode("name", "varchar(50)", "255"));

 tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertSame(tableMeta.getColumns().get(0), columns.get(0));
 assertSame(tableMeta.getColumns().get(1), columns.get(1));
 TableMeta.logger.fine("Found " + columns.size() + " columns in tableMetadata: " + tableMeta.getName());
 }

 private Node createColumnNode(String name, String type, String size) {
 Element columnElement = doc.createElement("column");
 columnElement.setAttribute("name", name);
 columnElement.setAttribute("type", type);
 columnElement.setTextContent(size);
 return columnElement;
 }
}