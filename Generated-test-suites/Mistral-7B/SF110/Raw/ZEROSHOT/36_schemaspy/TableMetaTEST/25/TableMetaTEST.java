package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.w3c.dom.*;
import org.xml. NamespaceContext;

public class TableMetaTest {

 private static final String XML_TABLE_META_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<Table name=\"table_name\" comments=\"Some comments\" remoteSchema=\"remote_schema\" remoteCatalog=\"remote_catalog\">\n" +
         " <Column name=\"column1\" comments=\"column1 comments\" type=\"integer\"/>\n" +
         " <Column name=\"column2\" comments=\"column2 comments\" type=\"varchar\"/>\n" +
         "</Table>";

 @BeforeClass
 static public void setup() {
 Logger.getLogger(TableMeta.class.getName()).setLevel(Level.FINE);
 }

 @Test
 public void testTableMetaInstance() {
 Node tableNode = createNodeFromString(XML_TABLE_META_DATA);
 TableMeta tableMeta = new TableMeta(tableNode);

 assertEquals("table_name", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 TableColumnMeta column1 = columns.get(0);
 TableColumnMeta column2 = columns.get(1);

 assertEquals("column1", column1.getName());
 assertEquals("column1 comments", column1.getComments());
 assertEquals("integer", column1.getType());

 assertEquals("column2", column2.getName());
 assertEquals("column2 comments", column2.getComments());
 assertEquals("varchar", column2.getType());
 }

 @Test
 public void testTableMetaInstanceWithComments() {
 Node tableNode = createNodeFromString(XML_TABLE_META_DATA.replace("Some comments", "Comments on table meta data"));
 TableMeta tableMeta = new TableMeta(tableNode);

 assertEquals("table_name", tableMeta.getName());
 assertEquals("Comments on table meta data", tableMeta.getComments());
 assertNull(tableMeta.getRemoteCatalog());
 assertNull(tableMeta.getRemoteSchema());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 TableColumnMeta column1 = columns.get(0);
 TableColumnMeta column2 = columns.get(1);

 assertEquals("column1", column1.getName());
 assertNull(column1.getComments());
 assertEquals("integer", column1.getType());

 assertEquals("column2", column2.getName());
 assertEquals("column2 comments", column2.getComments());
 assertEquals("varchar", column2.getType());
 }

 @Test
 public void testTableMetaInstanceWithRemoteCatalogAndSchema() {
 Node tableNode = createNodeFromString(XML_TABLE_META_DATA.replace("remote_schema", "remote_schema1")
         .replace("remote_catalog", "remote_catalog1"));
 TableMeta tableMeta = new TableMeta(tableNode);

 assertEquals("table_name", tableMeta.getName());
 assertNull(tableMeta.getComments());
 assertEquals("remote_catalog1", tableMeta.getRemoteCatalog());
 assertEquals("remote_schema1", tableMeta.getRemoteSchema());
 List<TableColumnMeta> columns = tableMeta.getColumns();
 assertEquals(2, columns.size());
 TableColumnMeta column1 = columns.get(0);
 TableColumnMeta column2 = columns.get(1);

 assertEquals("column1", column1.getName());
 assertNull(column1.getComments());
 assertEquals("integer", column1.getType());

 assertEquals("column2", column2.getName());
 assertNull(column2.getComments());
 assertEquals("varchar", column2.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaInstanceWithNullTableNode() {
 new TableMeta(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTableMetaInstanceWithEmptyTableNode() {
 Node emptyTableNode = createNodeFromString("");
 new TableMeta(emptyTableNode);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaInstanceWithNullAttributesNode() {
 Node tableNode = createNodeFromString("<Table name=\"table_name\"/>");
 new TableMeta(tableNode);
 }

 @Test(expected = NullPointerException.class)
 public void testTableMetaInstanceWithNullColumnNodes() {
 Node tableNode = createNodeFromString("<Table name=\"table_name\" />");
 new TableMeta(tableNode);
 }

 private Node createNodeFromString(String xmlData) {
 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder documentBuilder = null;
 try {
 documentBuilder = documentBuilderFactory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }

 InputSource inputSource = new InputSource();
 inputSource.setCharacterStream(new StringReader(xmlData));
 Document document = null;
 try {
 document = documentBuilder.parse(inputSource);
 } catch (SAXException | IOException e) {
 throw new RuntimeException(e);
 }
 return document.getDocumentElement();
 }
}