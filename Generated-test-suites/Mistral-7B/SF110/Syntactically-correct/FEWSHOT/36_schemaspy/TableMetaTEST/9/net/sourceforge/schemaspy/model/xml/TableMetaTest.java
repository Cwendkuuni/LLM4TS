package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "" +
         "<table name=\"table1\" comments=\"Comments for table1\">" +
         "<column name=\"col1\" comments=\"Comments for col1\" type=\"int\"/>" +
         "<column name=\"col2\" type=\"varchar\"/>" +
         "</table>";

 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 tableNode = parseXml(XML_TABLE_META_DATA);
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstructor() {
 assertEquals("table1", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNotNull(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testGetComments() {
 assertEquals("Comments for table1", tableMeta.getComments());
 }

 @Test
 public void testGetColumns() {
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertEquals("col1", columns.get(0).getName());
 assertNull(columns.get(0).getComments());
 assertEquals("int", columns.get(0).getType());
 assertEquals("col2", columns.get(1).getName());
 assertNull(columns.get(1).getComments());
 assertEquals("varchar", columns.get(1).getType());
 }

 @Test
 public void testGetRemoteCatalogAndRemoteSchema() {
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testLogger() {
 Logger loggerMock = mock(Logger.class);
 TableMeta.logger = loggerMock;
 TableMeta tableMeta = spy(new TableMeta(tableNode));
 doCallRealMethod().when(tableMeta).getName();
 tableMeta.getName();
 verify(loggerMock).fine("Found XML table metadata for table1 remoteCatalog: null remoteSchema: null comments: Comments for table1");
 }

 @Test(expected = NullPointerException.class)
 public void testNullTableNode() {
 new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTableNode() {
 Node emptyTableNode = new Document();
 new TableMeta(emptyTableNode);
 }
}