import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.mockito.MockitoXml;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"table1\" comments=\"This is a test table\" remoteSchema=\"test_schema\" remoteCatalog=\"test_catalog\">\n" +
         "<Column name=\"col1\" comments=\"This is a test column\"/>\n" +
         "<Column name=\"col2\" comments=\"Another test column\"/>\n" +
         "</Table>";

 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 tableNode = MockitoXml.parseToString(XML_TABLE_META_DATA);
 }

 @Test
 public void testTableMetaConstructors() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("table1", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEmpty(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());

 verify(tableNode, times(1)).getAttributes();
 verify(tableNode, times(1)).getChildNodes();
 }

 @Test
 public void testGetName() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertEquals("table1", tableMeta.getName());
 }

 @Test
 public void testGetComments() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getComments());

 String comments = "This is a test comment";
 tableNode.getAttributes().setNamedItem(mock(Node.class));
 when(tableNode.getAttributes().getItemNamed("comments")).thenReturn(tableNode.getAttributes().getNamedItem("comments"));
 when(tableNode.getAttributes().getItemNamed("remarks")).thenReturn(tableNode.getAttributes().getNamedItem("comments"));

 tableMeta = new TableMeta(tableNode);
 assertEquals(comments, tableMeta.getComments());
 }

 @Test
 public void testGetColumns() {
 TableMeta tableMeta = new TableMeta(tableNode);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 }

 @Test(expected = NullPointerException.class)
 public void testGetColumnsNullNode() {
 TableMeta tableMeta = new TableMeta(null);
 }

 @Test
 public void testGetRemoteCatalog() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getRemoteCatalog());

 String remoteCatalog = "test_catalog";
 tableNode.getAttributes().setNamedItem(mock(Node.class));
 when(tableNode.getAttributes().getItemNamed("remoteCatalog")).thenReturn(tableNode.getAttributes().getNamedItem("remoteCatalog"));

 tableMeta = new TableMeta(tableNode);
 assertEquals(remoteCatalog, tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchema() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertNull(tableMeta.getRemoteSchema());

 String remoteSchema = "test_schema";
 tableNode.getAttributes().setNamedItem(mock(Node.class));
 when(tableNode.getAttributes().getItemNamed("remoteSchema")).thenReturn(tableNode.getAttributes().getNamedItem("remoteSchema"));

 tableMeta = new TableMeta(tableNode);
 assertEquals(remoteSchema, tableMeta.getRemoteSchema());
 }
}