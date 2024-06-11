package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.Test;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\"?>\n" +
         "<Table name=\"test_table\" comments=\"comments for test_table\"" +
         " remoteSchema=\"remote_schema\" remoteCatalog=\"remote_catalog\">\n" +
         "<Column name=\"col1\" type=\"integer\" comments=\"col1 comments\"/>\n" +
         "<Column name=\"col2\" type=\"varchar(50)\" comments=\"col2 comments\"/>\n" +
         "</Table>";

 @Test
 public void testTableMetaConstruction() throws JDOMException {
     Document doc = new Document(new StringReader(XML_TABLE_META_DATA));
     Element tableNode = doc.getRootElement();
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("test_table", tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertNotNull(tableMeta.getColumns());
     assertEquals(2, tableMeta.getColumns().size());
     assertEquals("remote_schema", tableMeta.getRemoteSchema());
     assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullTableNode() {
     TableMeta tableMeta = new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyTableName() {
     Element tableNode = new Element("Table");
     tableNode.setAttribute("name", "");
     TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithNullTableNameAttribute() {
     Element tableNode = new Element("Table");
     TableMeta tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testGetters() {
     Document doc = new Document(new StringReader(XML_TABLE_META_DATA));
     Element tableNode = doc.getRootElement();
     TableMeta tableMeta = new TableMeta(tableNode);

     assertEquals("test_table", tableMeta.getName());
     assertEquals("comments for test_table", tableMeta.getComments());
     assertNotNull(tableMeta.getColumns());
     assertEquals(2, tableMeta.getColumns().size());
     assertEquals("remote_schema", tableMeta.getRemoteSchema());
     assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGettersWithEmptyXmlData() {
     TableMeta tableMeta = new TableMeta(new Element("Table"));
     assertEquals(null, tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertNotNull(tableMeta.getColumns());
     assertEquals(0, tableMeta.getColumns().size());
     assertNull(tableMeta.getRemoteCatalog());
     assertNull(tableMeta.getRemoteSchema());
 }

}