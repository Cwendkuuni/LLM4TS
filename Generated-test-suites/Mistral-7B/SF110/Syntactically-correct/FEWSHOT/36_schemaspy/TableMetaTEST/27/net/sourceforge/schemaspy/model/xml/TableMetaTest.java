package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;
import org.junit.Test;

public class TableMetaTest {

 static final String XML_TABLE_META_DATA = ""
 + "<table name=\"table1\">"
 + " <comments>This is a table comment</comments>"
 + " <remoteSchema>my_schema</remoteSchema>"
 + " <remoteCatalog>my_catalog</remoteCatalog>"
 + " <column name=\"col1\">"
 + " <dataType>INT</dataType>"
 + " <columnName>col1</columnName>"
 + " <columnComment>Column 1 comment</columnComment>"
 + " </column>"
 + " <column name=\"col2\">"
 + " <dataType>VARCHAR</dataType>"
 + " <columnName>col2</columnName>"
 + " <columnComment>Column 2 comment</columnComment>"
 + " </column>"
 + "</table>";

 @Test
 public void testTableMetaConstruction() {
 TableMeta tableMeta = new TableMeta(createNodeFromString(XML_TABLE_META_DATA));
 assertEquals("table1", tableMeta.getName());
 assertEquals("This is a table comment", tableMeta.getComments());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 assertColumnEquals("col1", columns.get(0), "INT", "col1", "Column 1 comment");
 assertColumnEquals("col2", columns.get(1), "VARCHAR", "col2", "Column 2 comment");
 assertEquals("my_catalog", tableMeta.getRemoteCatalog());
 assertEquals("my_schema", tableMeta.getRemoteSchema());
 }

 @Test (expected = NullPointerException.class)
 public void testTableMetaConstructionWithNullNode() {
 TableMeta tableMeta = new TableMeta(null);
 fail("TableMeta constructor should throw NullPointerException for null node");
 }

 @Test
 public void testTableMetaWithNullCommentsNode() {
 TableMeta tableMeta = new TableMeta(createNodeFromString("<table name=\"table1\"/>"));
 assertNull(tableMeta.getComments());
 }

 @Test
 public void testTableMetaWithNullRemoteSchemaNode() {
 TableMeta tableMeta = new TableMeta(createNodeFromString("<table name=\"table1\" remoteSchema=\"my_schema\"/>"));
 assertEquals("my_schema", tableMeta.getRemoteSchema());
 TableMeta tableMeta2 = new TableMeta(createNodeFromString("<table name=\"table1\"/>"));
 assertNull(tableMeta2.getRemoteSchema());
 }

 @Test
 public void testTableMetaWithNullRemoteCatalogNode() {
 TableMeta tableMeta = new TableMeta(createNodeFromString("<table name=\"table1\" remoteCatalog=\"my_catalog\"/>"));
 assertEquals("my_catalog", tableMeta.getRemoteCatalog());
 TableMeta tableMeta2 = new TableMeta(createNodeFromString("<table name=\"table1\"/>"));
 assertNull(tableMeta2.getRemoteCatalog());
 }

 @Test
 public void testTableMetaLogging() {
 String logMessage = "Found XML table metadata for table1 remoteCatalog: null remoteSchema: my_schema comments: This is a table comment";
 TableMeta tableMeta = new TableMeta(createNodeFromString("<table name=\"table1\" remoteSchema=\"my_schema\" comments=\"This is a table comment\"/>"));
 assertEquals(logMessage, TableMeta.logger.getMessage(Level.FINE).toString());
 }

 private Node createNodeFromString(String xmlString) {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = null;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 Document doc = null;
 try {
 doc = builder.parse(new InputSource(new StringReader(xmlString)));
 } catch (SAXException | IOException e) {
 e.printStackTrace();
 }
 return doc.getDocumentElement();
 }

 private static void assertColumnEquals(String columnName, TableColumnMeta expectedColumnMeta, String expectedDataType, String expectedColumnName, String expectedColumnComment) {
 TableColumnMeta actualColumnMeta = expectedColumnMeta;
 assertEquals(columnName, actualColumnMeta.getName());
 assertEquals(expectedDataType, actualColumnMeta.getDataType());
 assertEquals(expectedColumnName, actualColumnMeta.getColumnName());
 assertEquals(expectedColumnComment, actualColumnMeta.getColumnComment());
 }
}