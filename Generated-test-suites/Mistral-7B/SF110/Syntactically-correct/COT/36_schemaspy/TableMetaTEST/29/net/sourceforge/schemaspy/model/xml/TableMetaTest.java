package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table type=\"TABLE\" name=\"employees\">\n" +
         "  <TableMeta remoteCatalog=\"catalog1\" remoteSchema=\"schema1\"/>\n" +
         "  <Column name=\"id\" type=\"integer\" comments=\"ID\"/>\n" +
         "  <Column name=\"name\" type=\"varchar\" comments=\"Name\"/>\n" +
         "  <Column name=\"age\" type=\"integer\" comments=\"Age\"/>\n" +
         "</Table>";

 @BeforeClass
 public static void setup() {
 Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
 }

 @Test
 public void testTableMetaConstruction() throws Exception {
     Node root = parseXML(XML_TABLE_META_DATA);
     TableMeta tableMeta = new TableMeta(root);
     
     assertEquals("employees", tableMeta.getName());
     assertNull(tableMeta.getComments());
     assertNotNull(tableMeta.getColumns());
     assertNull(tableMeta.getRemoteCatalog());
     assertNull(tableMeta.getRemoteSchema());
     
     List<TableColumnMeta> columns = tableMeta.getColumns();
     assertEquals(3, columns.size());
     assertEquals("id", columns.get(0).getName());
     assertEquals("Name", columns.get(1).getName());
     assertEquals("Age", columns.get(2).getName());
     
     Logger.getLogger(TableMeta.class.getName()).fine("Found XML table metadata for employees remoteCatalog: null remoteSchema: null comments: null");
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullTableNode() {
     Node root = null;
     assertNull(new TableMeta(root));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaConstructionWithEmptyTableNode() {
     Node root = new DOMParser().parseFromString("<Table/>", null).getDocumentElement();
     assertNull(new TableMeta(root));
 }

 @Test
 public void testTableMetaGetters() {
     Node root = parseXML(XML_TABLE_META_DATA);
     TableMeta tableMeta = new TableMeta(root);
     
     assertEquals("employees", tableMeta.getName());
     assertEquals("ID\n", tableMeta.getComments());
     assertNotNull(tableMeta.getColumns());
     assertNull(tableMeta.getRemoteCatalog());
     assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaLogging() {
     Node root = parseXML(XML_TABLE_META_DATA);
     TableMeta tableMeta = new TableMeta(root);
     
     String expectedLog = "Found XML table metadata for employees remoteCatalog: null remoteSchema: null comments: ID\n";
     List<String> logMessages = new ArrayList<>();
     Handler logHandler = new Handler() {
         @Override
         public void publish(LogRecord record) {
             logMessages.add(record.getMessage().toString());
         }
     };
     Logger logger = Logger.getLogger(TableMeta.class.getName());
     logger.setUseParentHandlers(false);
     logger.addHandler(logHandler);
     
     tableMeta = new TableMeta(root);
     
     assertEquals(expectedLog, logMessages.get(0));
 }

 @AfterClass
 public static void teardown() {
     Logger.getLogger(TableMeta.class.getName()).setUseParentHandlers(true);
 }

 private static Node parseXML(String xml) throws Exception {
     DOMParser parser = new DOMParser();
     parser.parseFromString(xml, null);
     return parser.getDocumentElement();
 }

}