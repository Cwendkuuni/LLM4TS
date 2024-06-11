package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMetaTest {

 private static final String XML_TABLE_METADATA = ""
 + "<table name=\"table1\">"
 + " <comments>Comments for table1</comments>"
 + " <remoteSchema>schema1</remoteSchema>"
 + " <remoteCatalog>catalog1</remoteCatalog>"
 + " <column name=\"col1\">"
 + " <dataType>int</dataType>"
 + " <columnName>col1_name</columnName>"
 + " <isNullable>true</isNullable>"
 + " </column>"
 + " <column name=\"col2\">"
 + " <dataType>varchar</dataType>"
 + " <columnName>col2_name</columnName>"
 + " <isNullable>false</isNullable>"
 + " </column>"
 + "</table>";

 @Test
 public void testTableMeta() {
 TableMeta tableMeta = new TableMeta(parseXML(XML_TABLE_METADATA));
 assertEquals("table1", tableMeta.getName());
 assertEquals("Comments for table1", tableMeta.getComments());
 assertEquals(2, tableMeta.getColumns().size());
 assertEquals("catalog1", tableMeta.getRemoteCatalog());
 assertEquals("schema1", tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaEmpty() {
 TableMeta tableMeta = new TableMeta(parseXML(""));
 assertNull(tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEmpty(tableMeta.getColumns());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 }

 @Test
 public void testTableMetaNull() {
 assertNull(new TableMeta(null));
 }

 @Test
 public void testTableMetaMissingName() {
 assertNull(new TableMeta(parseXML("<table><column></column></table>")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaInvalidXML() {
 new TableMeta(parseXML("<table name>"));
 }

 @Test
 public void testTableMetaColumnsSize() {
 Node xmlTableNode = parseXML(XML_TABLE_METADATA);
 TableMeta tableMeta = new TableMeta(xmlTableNode);
 assertEquals(2, tableMeta.getColumns().size());
 }

 @Test
 public void testTableMetaColumnsGet() {
 TableMeta tableMeta = new TableMeta(parseXML(XML_TABLE_METADATA));
 TableColumnMeta col1 = tableMeta.getColumns().get(0);
 TableColumnMeta col2 = tableMeta.getColumns().get(1);
 assertEquals("col1_name", col1.getName());
 assertEquals("int", col1.getDataType());
 assertTrue(col1.isNullable());
 assertEquals("col2_name", col2.getName());
 assertEquals("varchar", col2.getDataType());
 assertFalse(col2.isNullable());
 }

 @Test
 public void testTableMetaColumnsGetEmptyList() {
 TableMeta tableMeta = new TableMeta(parseXML(""));
 assertTrue(tableMeta.getColumns().isEmpty());
 }

 private static Node parseXML(String xml) {
 try {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = factory.newDocumentBuilder();
 Document doc = builder.parse(new InputSource(new StringReader(xml)));
 return doc.getDocumentElement();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
}

 @BeforeClass
 public static void setupLogger() {
 Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
 }

}