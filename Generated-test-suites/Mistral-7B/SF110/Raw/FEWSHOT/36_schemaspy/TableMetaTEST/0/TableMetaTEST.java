package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "" +
        "<table name=\"table1\" comments=\"Comments for table1\" remoteSchema=\"remoteSchema1\" remoteCatalog=\"remoteCatalog1\">" +
        "<column name=\"col1\" type=\"int\" comments=\"Comments for col1\"/>" +
        "<column name=\"col2\" type=\"varchar(50)\" comments=\"Comments for col2\"/>" +
        "</table>";

 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() throws Exception {
 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
 InputSource is = new InputSource(new StringReader(XML_TABLE_META_DATA));
 tableNode = dBuilder.parse(is);
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaCreation() {
 assertEquals("table1", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEquals(2, tableMeta.getColumns().size());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaComments() {
 String comments = tableMeta.getComments();
 assertNotNull(comments);
 assertEquals("Comments for table1", comments);
 }

 @Test
 public void testTableMetaColumns() {
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals("col1", columns.get(0).getName());
 assertEquals("Comments for col1", columns.get(0).getComments());
 assertEquals("int", columns.get(0).getType());
 assertEquals("col2", columns.get(1).getName());
 assertEquals("Comments for col2", columns.get(1).getComments());
 assertEquals("varchar(50)", columns.get(1).getType());
 }

 @Test
 public void testTableMetaRemoteCatalogSchema() {
 assertEquals("remoteCatalog1", tableMeta.getRemoteCatalog());
 assertEquals("remoteSchema1", tableMeta.getRemoteSchema());
 }
}