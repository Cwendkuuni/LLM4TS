import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"table1\" comments=\"This is a test table.\">\n" +
         "  <TableColumn name=\"col1\" comments=\"This is a test column.\"/>\n" +
         "  <TableColumn name=\"col2\" comments=\"Another test column.\"/>\n" +
         "  <attribute name=\"remoteSchema\" value=\"test_schema\"/>\n" +
         "  <attribute name=\"remoteCatalog\" value=\"test_catalog\"/>\n" +
         "</Table>";
 private Document document;
 private Node tableNode;
 private TableMeta tableMeta;

 @Before
 public void setUp() throws Exception {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = factory.newDocumentBuilder();
 document = builder.parse(new org.xml.XMLInputSource(new java.io.StringReader(XML_TABLE_META_Data)));
 tableNode = document.getDocumentElement();
 }

 @Test
 public void testTableMetaCreation() {
 tableMeta = new TableMeta(tableNode);
 assertEquals("table1", tableMeta.getName());
 assertNull(tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertNotNull(columns.get(0).getName());
 assertNotNull(columns.get(1).getName());
 assertEquals("test_catalog", tableMeta.getRemoteCatalog());
 assertEquals("test_schema", tableMeta.getRemoteSchema());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaNoName() {
 Node invalidTableNode = document.getElementsByTagName("Table").item(1);
 try {
 new TableMeta(invalidTableNode);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaNoColumns() {
 Element invalidTableElement = document.createElement("Table");
 invalidTableElement.setAttribute("name", "testTable");
 document.appendChild(invalidTableElement);
 try {
 new TableMeta(document.getDocumentElement());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLogging() {
 TableMeta tableMeta = new TableMeta(tableNode);
 assertTrue(TableMeta.logger.isLoggable(Level.FINE));
 assertFalse(TableMeta.logger.isLoggable(Level.SEVERE));
}
}