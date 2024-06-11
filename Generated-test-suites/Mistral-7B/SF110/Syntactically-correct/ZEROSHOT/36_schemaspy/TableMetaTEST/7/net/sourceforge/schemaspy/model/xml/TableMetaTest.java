import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

public class TableMetaTest {

 private static final Logger logger = Logger.getLogger(TableMeta.class.getName());
 private TableMeta tableMeta;

 @Before
 public void setUp() {
 String xml = "<table name=\"testTable\" comments=\"This is a test table.\">" +
         "<column name=\"col1\" type=\"int\"/>" +
         "<column name=\"col2\" type=\"varchar\"/>" +
         "</table>";

 Node tableNode = parseXml(xml);
 tableMeta = new TableMeta(tableNode);
 }

 @Test
 public void testTableMetaConstruction() {
 assertNotNull(tableMeta);
 assertEquals("testTable", tableMeta.getName());
 assertNotNull(tableMeta.getComments());
 assertEquals("This is a test table.", tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());

 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertNotNull(columns.get(0));
 assertNotNull(columns.get(1));
 }

 @Test
 public void testTableMetaEmptyComments() {
 String xml = "<table name=\"testTable\"/>";
 tableMeta = new TableMeta(parseXml(xml)[0]);
 assertNull(tableMeta.getComments());
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaNullNode() {
 tableMeta = new TableMeta(null);
 }

 @Test
 public void testTableMetaLogging() {
 verifyLogging("Found XML table metadata for testTable remoteCatalog: null remoteSchema: null comments: This is a test table.");
 }

 @Test
 public void testGetRemoteCatalog() {
 String xml = "<table name=\"testTable\" remoteCatalog=\"testCatalog\"/>";
 tableMeta = new TableMeta(parseXml(xml)[0]);
 assertEquals("testCatalog", tableMeta.getRemoteCatalog());
 }

 @Test
 public void testGetRemoteSchema() {
 String xml = "<table name=\"testTable\" remoteSchema=\"testSchema\"/>";
 tableMeta = new TableMeta(parseXml(xml)[0]);
 assertEquals("testSchema", tableMeta.getRemoteSchema());
 }

 @Test
 public void testGetColumns() {
 List<TableColumnMeta> columns = tableMeta.getColumns();
 TableColumnMeta col1 = columns.get(0);
 TableColumnMeta col2 = columns.get(1);

 assertEquals("col1", col1.getName());
 assertEquals("int", col1.getType());
 assertEquals("col2", col2.getName());
 assertEquals("varchar", col2.getType());
 }

 @Test
 public void testGetColumnsEmpty() {
 String xml = "<table name=\"testTable\"/>";
 tableMeta = new TableMeta(parseXml(xml)[0]);
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertFalse(columns.isEmpty());
 }

 private Node parseXml(String xml) {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 try {
 DocumentBuilder builder = factory.newDocumentBuilder();
 Document doc = builder.parse(new InputSource(new StringReader(xml)));
 return doc.getDocumentElement();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 private void verifyLogging(String message) {
 List<String> logs = new ArrayList<>();
 Handler handler = new Handler() {
 @Override
 public void publish(LogRecord record) {
 logs.add(record.getMessage().toString());
 }
 };
 logger.addHandler(handler);
 tableMeta = new TableMeta(parseXml("<table name=\"testTable\"/>")[0]);
 assertEquals(1, logs.size());
 assertEquals(message, logs.get(0));
 logger.removeHandler(handler);
}

}