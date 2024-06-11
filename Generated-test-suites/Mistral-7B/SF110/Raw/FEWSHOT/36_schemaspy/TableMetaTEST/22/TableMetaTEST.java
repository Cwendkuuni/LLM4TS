package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.jdom2.Element;
import org.junit.Test;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = ""
 + "<table name=\"table1\" comments=\"This is a comment for table table1\""
 + " remoteCatalog=\"catalog1\" remoteSchema=\"schema1\">"
 + " <column name=\"col1\" type=\"int\" comments=\"This is a comment for column col1\"/>"
 + " <column name=\"col2\" type=\"varchar(50)\" comments=\"This is a comment for column col2\"/>"
 + "</table>";

 @Test
 public void testTableMetaCreation() {
 TableMeta tableMeta = new TableMeta(new Element(TableMeta.class.getName(), new StringReader(XML_TABLE_META_DATA)));
 assertEquals("table1", tableMeta.getName());
 assertEquals("This is a comment for table table1", tableMeta.getComments());
 assertEquals(2, tableMeta.getColumns().size());
 assertEquals("catalog1", tableMeta.getRemoteCatalog());
 assertEquals("schema1", tableMeta.getRemoteSchema());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaCreationWithNullNode() {
 new TableMeta(null);
 }

 @Test
 public void testTableMetaCreationWithMissingNameAttribute() {
 try {
 new TableMeta(new Element("table", new StringReader("")));
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTableMetaCreationWithMissingAttributes() {
 try {
 new TableMeta(new Element("table", new StringReader("<table>")));
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTableMetaCreationWithMissingColumns() {
 try {
 new TableMeta(new Element("table", new StringReader("<table name=\"table1\"/>")));
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTableMetaGetters() {
 TableMeta tableMeta = new TableMeta(new Element(TableMeta.class.getName(), new StringReader(XML_TABLE_META_DATA)));
 assertEquals("table1", tableMeta.getName());
 assertEquals("This is a comment for table table1", tableMeta.getComments());
 assertEquals(2, tableMeta.getColumns().size());
 assertEquals("catalog1", tableMeta.getRemoteCatalog());
 assertEquals("schema1", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaCommentsNull() {
 TableMeta tableMeta = new TableMeta(new Element(TableMeta.class.getName(), new StringReader("<table name=\"table1\"/>")));
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaColumnsEmpty() {
 TableMeta tableMeta = new TableMeta(new Element(TableMeta.class.getName(), new StringReader("<table name=\"table1\" comments=\"comments\"><columns/></table>")));
 assertTrue(tableMeta.getColumns().isEmpty());
 }
}