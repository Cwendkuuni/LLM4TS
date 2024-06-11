package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());
 private static final Level level = Level.FINE;
 private TableMeta tableMeta;

 @Before
 public void setup() {
 logger.setLevel(level);
 try {
 Logger.getRootLogger().setLevel(level);
 } catch (SecurityException e) {
 e.printStackTrace();
 }

 Element tableElement = new Element("table");
 tableElement.setAttribute("name", "testTable");
 tableElement.setAttribute("comments", "test comments");
 tableElement.setAttribute("remoteSchema", "testRemoteSchema");
 tableElement.setAttribute("remoteCatalog", "testRemoteCatalog");

 NodeList columnNodes = new NodeList() {
 @Override
 public Node item(int index) {
 Element columnElement = new Element("column");
 columnElement.setAttribute("name", "columnName" + index);
 return columnElement;
 }

 @Override
 public int getLength() {
 return 5;
 }
 };
 tableElement.appendChild(columnNodes);

 tableMeta = new TableMeta(tableElement);
 }

 @Test
 public void testTableMetaConstruction() {
 assertEquals("testTable", tableMeta.getName());
 assertNull(tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(5, columns.size());
 for (int i = 0; i < columns.size(); i++) {
 TableColumnMeta columnMeta = columns.get(i);
 assertEquals("columnName" + i, columnMeta.getName());
 }
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTableNode() {
 try {
 new TableMeta(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullNameAttribute() {
 Element tableElement = new Element("table");
 tableElement.setAttribute("remoteSchema", "testRemoteSchema");
 tableElement.setAttribute("remoteCatalog", "testRemoteCatalog");
 TableMeta tableMeta = new TableMeta(tableElement);
 tableElement.removeAttribute("name");
 try {
 tableMeta.getName();
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }
}