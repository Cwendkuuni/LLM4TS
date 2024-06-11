package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\"?>\n" +
         "<!-- Comment1 -->\n" +
         "<Table name=\"table1\" remoteSchema=\"schema1\" remoteCatalog=\"catalog1\">\n" +
         "  <comments>Comment2</comments>\n" +
         "  <column name=\"col1\" type=\"int\"/>\n" +
         "  <column name=\"col2\" type=\"varchar(255)\"/>\n" +
         "</Table>\n";

 private TableMeta tableMeta;

 @Before
 public void setUp() throws Exception {
 final Document document = javax.xml.parsers.DocumentBuilderFactory.newInstance(). newDocumentBuilder().parse(new org.jdom2.input.JDOMParser().parse(new java.io.StringReader(XML_TABLE_META_DATA)));
 tableMeta = new TableMeta(document.getDocumentElement());
 }

 @Test
 public void testTableMetaConstructionWithValidXMLData() {
 // Test name
 assertEquals("table1", tableMeta.getName());
 // Test comments
 assertEquals("Comment2", tableMeta.getComments());
 // Test columns
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertEquals("col1", columns.get(0).getName());
 assertEquals("int", columns.get(0).getType());
 assertEquals("col2", columns.get(1).getName());
 assertEquals("varchar(255)", columns.get(1).getType());
 // Test remoteSchema and remoteCatalog
 assertNull(tableMeta.getRemoteCatalog());
 assertNotNull(tableMeta.getRemoteSchema());
 assertEquals("schema1", tableMeta.getRemoteSchema());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithMissingNameAttribute() {
 final Node tableNode = newDomElement("Table");
 tableNode.setAttribute("remoteSchema", "schema1");
 tableNode.setAttribute("remoteCatalog", "catalog1");
 final TableMeta tableMeta = new TableMeta(tableNode);
 fail("Expected NullPointerException but TableMeta was constructed successfully");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithMissingColumnNodes() {
 final Element tableElement = newDomElement("Table", "name", "table1");
 tableElement.setAttribute("remoteSchema", "schema1");
 tableElement.setAttribute("remoteCatalog", "catalog1");
 final TableMeta tableMeta = new TableMeta(tableElement);
 fail("Expected NullPointerException but TableMeta was constructed successfully");
 }

 @Test
 public void testTableMetaLogging() {
 final Logger logger = Logger.getLogger(TableMeta.class.getName());
 final Level level = logger.getLevel();
 logger.setLevel(Level.FINEST);
 tableMeta = new TableMeta(newDomElement("Table", "name", "table1"));
 assertEquals(Level.FINEST, logger.getLevel());
 tableMeta = null;
 logger.setLevel(level);
 }

 private Element newDomElement(String tagName, String... attributes) {
 final Element element = new Element(tagName);
 for (int i = 0; i < attributes.length; ++i) {
 final String[] attribute = attributes[i].split("\\s+");
 element.setAttribute(attribute[0], attribute[1]);
 }
 return element;
 }

}