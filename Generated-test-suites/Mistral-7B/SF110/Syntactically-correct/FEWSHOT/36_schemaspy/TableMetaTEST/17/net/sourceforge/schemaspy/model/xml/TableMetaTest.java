package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

 private static final String XML_TABLE_META_TEMPLATE = ""
 + "<table name=\"table1\" comments=\"Comments for table1\">"
 + "<column name=\"col1\" comments=\"Comments for col1\"/>"
 + "<column name=\"col2\" comments=\"Comments for col2\"/>"
 + "</table>";

 @Test
 public void testTableMeta() {
 TableMeta tableMeta = new TableMeta(parseXmlStringToDocument(XML_TABLE_META_TEMPLATE).getDocumentElement());

 assertEquals("table1", tableMeta.getName());
 assertEquals("Comments for table1", tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 TableColumnMeta col1 = columns.get(0);
 assertEquals("col1", col1.getName());
 assertNull(col1.getComments());
 TableColumnMeta col2 = columns.get(1);
 assertEquals("col2", col2.getName());
 assertEquals("Comments for col2", col2.getComments());
 }

 @Test
 public void testTableMetaEmpty() {
 TableMeta tableMeta = new TableMeta(parseXmlStringToDocument("").getDocumentElement());
 assertNull(tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 assertEmpty(tableMeta.getColumns());
 }

 @Test
 public void testTableMetaNullComments() {
 TableMeta tableMeta = new TableMeta(parseXmlStringToDocument(XML_TABLE_META_TEMPLATE.replaceAll("comments", ""))
 .getDocumentElement());
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaNullColumns() {
 TableMeta tableMeta = new TableMeta(parseXmlStringToDocument(XML_TABLE_META_TEMPLATE.replaceAll("<column", ""))
 .getDocumentElement());
 assertEquals(0, tableMeta.getColumns().size());
 }

 @Test
 public void testTableMetaNullName() {
 TableMeta tableMeta = new TableMeta(parseXmlStringToDocument(XML_TABLE_META_TEMPLATE.replaceAll("name=\"table1\"", ""))
 .getDocumentElement());
 assertNull(tableMeta.getName());
 }

 @Test
 public void testTableMetaEmptyRemoteCatalog() {
 TableMeta tableMeta = new TableMeta(parseXmlStringToDocument(XML_TABLE_META_TEMPLATE.replaceAll("remoteCatalog=\"catalog1\"", ""))
 .getDocumentElement());
 assertNull(tableMeta.getRemoteCatalog());
 }

 @Test
 public void testTableMetaEmptyRemoteSchema() {
 TableMeta tableMeta = new TableMeta(parseXmlStringToDocument(XML_TABLE_META_TEMPLATE.replaceAll("remoteSchema=\"schema1\"", ""))
 .getDocumentElement());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaSameInstance() {
 TableMeta tableMeta1 = new TableMeta(parseXmlStringToDocument(XML_TABLE_META_TEMPLATE).getDocumentElement());
 TableMeta tableMeta2 = new TableMeta(tableMeta1.getDocumentElement());
 assertSame(tableMeta1.getName(), tableMeta2.getName());
 assertSame(tableMeta1.getComments(), tableMeta2.getComments());
 assertSame(tableMeta1.getRemoteCatalog(), tableMeta2.getRemoteCatalog());
 assertSame(tableMeta1.getRemoteSchema(), tableMeta2.getRemoteSchema());
 assertSame(tableMeta1.getColumns(), tableMeta2.getColumns());
 }

 private Document parseXmlStringToDocument(String xml) {
 try {
 return javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.StringReader(xml));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 private static void assertEmpty(List<?> list) {
 assertEquals(0, list.size());
 }
}